<?php
$host = "localhost:3306";
$username = "root";
$password = "";
$database = "htttgame";

// Kết nối cơ sở dữ liệu
$conn = new mysqli($host, $username, $password, $database);

if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// Truy vấn danh sách hóa đơn - Nhóm theo SalesID
$sql = "SELECT 
            sales_invoice.SalesID,
            Customer.Username,
            Customer.Fullname,
            Customer.Phone,
            Customer.Address,
            sales_invoice.Date,
            GROUP_CONCAT(Product.ProductName SEPARATOR '||') AS Products,
            GROUP_CONCAT(Product.Author SEPARATOR '||') AS Authors,
            GROUP_CONCAT(Product.Price SEPARATOR '||') AS Prices,
            GROUP_CONCAT(detail_sales_invoice.Quantity SEPARATOR '||') AS Quantities,
            GROUP_CONCAT(detail_sales_invoice.TotalPrice SEPARATOR '||') AS ItemTotalPrices,
            SUM(detail_sales_invoice.TotalPrice) AS GrandTotal
        FROM 
            Customer
        INNER JOIN 
            sales_invoice ON Customer.CustomerID = sales_invoice.CustomerID
        INNER JOIN 
            detail_sales_invoice ON sales_invoice.SalesID = detail_sales_invoice.SalesID
        INNER JOIN 
            Product ON detail_sales_invoice.ProductID = Product.ProductID
        WHERE 
            detail_sales_invoice.Order_status = 'Đã duyệt'
        GROUP BY 
            sales_invoice.SalesID, 
            Customer.Username,
            Customer.Fullname,
            Customer.Phone,
            Customer.Address,
            sales_invoice.Date
        ORDER BY 
            sales_invoice.Date DESC";

$result = $conn->query($sql);
?>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách hóa đơn</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="../assets/css/view_invoice.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body>
    <div class="container mt-5">
        <h2 class="text-center">Danh sách hóa đơn</h2>
        <table class="table table-bordered table-hover">
            <thead class="table-primary">
                <tr>
                    <th>STT</th>
                    <th>Mã hóa đơn</th>
                    <th>Username</th>
                    <th>Họ và tên</th>
                    <th>Ngày mua</th>
                    <th>Số lượng sản phẩm</th>
                    <th>Tổng giá</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <?php if ($result->num_rows > 0): ?>
                    <?php $index = 1; ?>
                    <?php while ($row = $result->fetch_assoc()): 
                        // Tách các giá trị đã được gộp bằng GROUP_CONCAT
                        $products = explode('||', $row['Products']);
                        $authors = explode('||', $row['Authors']);
                        $prices = explode('||', $row['Prices']);
                        $quantities = explode('||', $row['Quantities']);
                        $itemTotalPrices = explode('||', $row['ItemTotalPrices']);
                        $productCount = count($products);
                    ?>
                        <tr>
                            <td><?= $index++ ?></td>
                            <td><?= $row['SalesID'] ?></td>
                            <td><?= $row['Username'] ?></td>
                            <td><?= $row['Fullname'] ?></td>
                            <td><?= $row['Date'] ?></td>
                            <td><?= $productCount ?></td>
                            <td><?= number_format($row['GrandTotal'], 0, ',', '.') ?> VND</td>
                            <td>
                                <button class="btn btn-info btn-sm view-invoice" 
                                    data-bs-toggle="modal" 
                                    data-bs-target="#invoiceModal"
                                    data-salesid="<?= $row['SalesID'] ?>"
                                    data-username="<?= $row['Username'] ?>"
                                    data-fullname="<?= $row['Fullname'] ?>"
                                    data-phone="<?= $row['Phone'] ?>"
                                    data-address="<?= $row['Address'] ?>"
                                    data-date="<?= $row['Date'] ?>"
                                    data-products="<?= htmlspecialchars($row['Products']) ?>"
                                    data-authors="<?= htmlspecialchars($row['Authors']) ?>"
                                    data-prices="<?= htmlspecialchars($row['Prices']) ?>"
                                    data-quantities="<?= htmlspecialchars($row['Quantities']) ?>"
                                    data-itemtotalprices="<?= htmlspecialchars($row['ItemTotalPrices']) ?>"
                                    data-grandtotal="<?= number_format($row['GrandTotal'], 0, ',', '.') ?> VND">
                                    Xem
                                </button>
                            </td>
                        </tr>
                    <?php endwhile; ?>
                <?php else: ?>
                    <tr>
                        <td colspan="8" class="text-center text-danger">Không có hóa đơn nào.</td>
                    </tr>
                <?php endif; ?>
            </tbody>
        </table>
    </div>

    <div class="modal fade" id="invoiceModal" tabindex="-1" aria-labelledby="invoiceModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="invoiceModalLabel">Chi tiết hóa đơn #<span id="modal-salesid"></span></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="customer-info">
                        <h3>Thông tin khách hàng</h3>
                        <p><strong>Username:</strong> <span id="modal-username"></span></p>
                        <p><strong>Họ và tên:</strong> <span id="modal-fullname"></span></p>
                        <p><strong>Số điện thoại:</strong> <span id="modal-phone"></span></p>
                        <p><strong>Địa chỉ:</strong> <span id="modal-address"></span></p>
                        <p><strong>Ngày mua:</strong> <span id="modal-date"></span></p>
                    </div>

                    <div class="product-section">
                        <h3>Chi tiết sản phẩm</h3>
                        <table class="table table-bordered" id="products-table">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Sản phẩm</th>
                                    <th>Tác giả</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Dữ liệu sản phẩm sẽ được thêm vào bằng JavaScript -->
                            </tbody>
                        </table>
                    </div>

                    <div class="total-section text-end">
                        <h4>Tổng cộng: <span id="modal-grand-total"></span></h4>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        document.querySelectorAll('.view-invoice').forEach(button => {
            button.addEventListener('click', function () {
                // Lấy dữ liệu từ thuộc tính data-*
                const salesId = this.getAttribute('data-salesid');
                const username = this.getAttribute('data-username');
                const fullname = this.getAttribute('data-fullname');
                const phone = this.getAttribute('data-phone');
                const address = this.getAttribute('data-address');
                const date = this.getAttribute('data-date');
                const products = this.getAttribute('data-products').split('||');
                const authors = this.getAttribute('data-authors').split('||');
                const prices = this.getAttribute('data-prices').split('||');
                const quantities = this.getAttribute('data-quantities').split('||');
                const itemTotalPrices = this.getAttribute('data-itemtotalprices').split('||');
                const grandTotal = this.getAttribute('data-grandtotal');

                // Hiển thị thông tin khách hàng trong modal
                document.getElementById('modal-salesid').textContent = salesId;
                document.getElementById('modal-username').textContent = username;
                document.getElementById('modal-fullname').textContent = fullname;
                document.getElementById('modal-phone').textContent = phone;
                document.getElementById('modal-address').textContent = address;
                document.getElementById('modal-date').textContent = date;
                document.getElementById('modal-grand-total').textContent = grandTotal;

                // Xóa các hàng cũ trong bảng sản phẩm
                const productTableBody = document.querySelector('#products-table tbody');
                productTableBody.innerHTML = '';

                // Thêm từng sản phẩm vào bảng
                for (let i = 0; i < products.length; i++) {
                    const row = document.createElement('tr');
                    
                    row.innerHTML = `
                        <td>${i + 1}</td>
                        <td>${products[i]}</td>
                        <td>${authors[i]}</td>
                        <td>${prices[i]} VND</td>
                        <td>${quantities[i]}</td>
                        <td>${itemTotalPrices[i]} VND</td>
                    `;
                    
                    productTableBody.appendChild(row);
                }
            });
        });
    </script>
</body>

</html>

<?php
$conn->close();
?>