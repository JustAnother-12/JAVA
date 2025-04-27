<?php
include_once __DIR__ . '/../functions/db.php';

$importID = 'IMP' . date('YmdHis');
$employeeID = $_SESSION['employeeID'];
$date = date('Y-m-d');

$supplier_query = mysqli_query($conn, "SELECT SupplierID, SupplierName FROM supplier");
$product_query = mysqli_query($conn, "SELECT ProductID, ProductName, SupplierID, Price FROM product");

$products = [];
while ($row = mysqli_fetch_assoc($product_query)) {
    $products[] = $row;
}
?>

<h2>Lập phiếu nhập</h2>
<div class="import-form-box">
<form method="POST">
    <input type="hidden" name="active_section" value="import">

    <label>Import ID: </label>
    <input type="text" value="<?= $importID ?>" disabled><br>
    <input type="hidden" name="import_id" value="<?= $importID ?>">

    <label>Employee ID: </label>
    <input type="text" value="<?= $employeeID ?>" disabled><br>
    <input type="hidden" name="employee_id" value="<?= $employeeID ?>">

    <label>Supplier: </label>
    <select name="supplier_id" id="supplierSelect" required>
        <option value="">-- Chọn nhà cung cấp --</option>
        <?php while ($row = mysqli_fetch_assoc($supplier_query)) : ?>
            <option value="<?= $row['SupplierID'] ?>"><?= $row['SupplierName'] ?></option>
        <?php endwhile; ?>
    </select>

    <div id="productSection">
        <label>Chọn sản phẩm:</label>
        <select name="product_id" id="productSelect" required>
            <option value="">-- Chọn sản phẩm --</option>
        </select>

        <label>Số lượng:</label>
        <input type="number" name="quantity" id="quantityInput" min="1" required>
    </div>

    <label>Total Price:</label>
    <input type="text" id="totalPrice" readonly><br>

    <input type="submit" name="submit" value="Lưu phiếu nhập">
</form>
</div>
<script>
const allProducts = <?= json_encode($products) ?>;
const supplierSelect = document.getElementById("supplierSelect");
const productSelect = document.getElementById("productSelect");
const quantityInput = document.getElementById("quantityInput");
const totalPriceInput = document.getElementById("totalPrice");

let currentPrice = 0;

supplierSelect.addEventListener("change", function () {
    const supplierID = this.value;
    productSelect.innerHTML = '<option value="">-- Chọn sản phẩm --</option>';
    currentPrice = 0;
    totalPriceInput.value = '';
    productSelect.selectedIndex = 0;

    allProducts.forEach(product => {
        if (product.SupplierID == supplierID) {
            const option = document.createElement("option");
            option.value = product.ProductID;
            option.textContent = `${product.ProductName} - ${product.Price} VND`;
            productSelect.appendChild(option);
        }
    });
});

productSelect.addEventListener("change", function () {
    const selectedProduct = allProducts.find(p => p.ProductID === this.value);
    currentPrice = selectedProduct ? parseFloat(selectedProduct.Price) : 0;
    updateTotalPrice();
});

quantityInput.addEventListener("input", updateTotalPrice);

function updateTotalPrice() {
    const quantity = parseInt(quantityInput.value) || 0;
    const total = quantity * currentPrice;
    totalPriceInput.value = total.toLocaleString('vi-VN') + " VND";
}
</script>
<?php
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['submit'])) {
    $importID = $_POST['import_id'];
    $employeeID = $_POST['employee_id'];
    $supplierID = $_POST['supplier_id'];
    $productID = $_POST['product_id'];
    $quantity = (int)$_POST['quantity'];
    $date = date('Y-m-d');

    // Lấy giá sản phẩm
    $product_result = mysqli_query($conn, "SELECT Price FROM product WHERE ProductID = '$productID'");
    $product = mysqli_fetch_assoc($product_result);
    $price = $product['Price'];
    $total = $price * $quantity;

    // Lưu vào bảng import_invoice
    mysqli_query($conn, "INSERT INTO import_invoice (ImportID, EmployeeID, SupplierID, Date, TotalPrice) 
                         VALUES ('$importID', '$employeeID', '$supplierID', '$date', '$total')");

    // Sinh mã chi tiết
    $detailID = 'D' . rand(100000, 999999);

    // Lưu vào bảng detail_import_invoice
    mysqli_query($conn, "INSERT INTO detail_import_invoice (DetailImportID, ImportID, ProductID, Quantity, Price, TotalPrice) 
                         VALUES ('$detailID', '$importID', '$productID', $quantity, $price, $total)");

    // Cập nhật số lượng tồn kho
    mysqli_query($conn, "UPDATE product SET Quantity = Quantity + $quantity WHERE ProductID = '$productID'");

    echo "<p style='color: green;'>✔️ Lưu thành công phiếu nhập!</p>";
}
?>









