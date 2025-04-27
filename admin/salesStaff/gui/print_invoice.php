<?php
$host = "localhost:3306";
$username = "root";
$password = "";
$database = "htttgame";

$conn = new mysqli($host, $username, $password, $database);

if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// Lấy tất cả các hóa đơn đã duyệt
$sql = "SELECT DISTINCT
            sales_invoice.SalesID,
            Customer.Username,
            Customer.Fullname,
            Customer.Email,
            Customer.Phone,
            Customer.Address,
            sales_invoice.Date
        FROM 
            Customer
        INNER JOIN 
            sales_invoice ON Customer.CustomerID = sales_invoice.CustomerID
        INNER JOIN 
            detail_sales_invoice ON sales_invoice.SalesID = detail_sales_invoice.SalesID
        WHERE 
            detail_sales_invoice.Order_status = 'Đã duyệt'
        GROUP BY
            sales_invoice.SalesID";

$result = $conn->query($sql);
$invoices = $result->fetch_all(MYSQLI_ASSOC);

// Lấy chi tiết sản phẩm cho mỗi hóa đơn
$invoice_details = [];
foreach ($invoices as $invoice) {
    $salesID = $invoice['SalesID'];
    
    $detail_sql = "SELECT 
                    Product.ProductName,
                    Product.Price,
                    Product.Author,
                    Supplier.SupplierName,
                    detail_sales_invoice.Quantity,
                    detail_sales_invoice.TotalPrice
                FROM 
                    detail_sales_invoice
                INNER JOIN 
                    Product ON detail_sales_invoice.ProductID = Product.ProductID
                INNER JOIN
                    Supplier ON Product.SupplierID = Supplier.SupplierID
                WHERE 
                    detail_sales_invoice.SalesID = '$salesID'
                    AND detail_sales_invoice.Order_status = 'Đã duyệt'";
    
    $detail_result = $conn->query($detail_sql);
    $invoice_details[$salesID] = $detail_result->fetch_all(MYSQLI_ASSOC);
}
?>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>In hóa đơn</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="../assets/css/print_invoice.css">

    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
</head>

<body>
    <div class="invoice-container">
        <div class="invoice-header non-printable">
            <h2>Danh sách hóa đơn</h2>
        </div>

        <!-- Hiển thị danh sách hóa đơn -->
        <table class="table table-bordered">
            <thead class="table-primary">
                <tr>
                    <th>STT</th>
                    <th>Mã hóa đơn</th>
                    <th>Khách hàng</th>
                    <th>SĐT</th>
                    <th>Ngày mua</th>
                    <th>Số lượng SP</th>
                    <th>Tổng tiền</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <?php if (!empty($invoices)): ?>
                    <?php foreach ($invoices as $index => $invoice): ?>
                        <?php 
                        $salesID = $invoice['SalesID'];
                        $products = $invoice_details[$salesID];
                        $total_items = 0;
                        $total_amount = 0;
                        
                        foreach ($products as $product) {
                            $total_items += $product['Quantity'];
                            $total_amount += $product['TotalPrice'];
                        }
                        ?>
                        <tr>
                            <td><?= $index + 1 ?></td>
                            <td><?= $salesID ?></td>
                            <td><?= $invoice['Fullname'] ?></td>
                            <td><?= $invoice['Phone'] ?></td>
                            <td><?= $invoice['Date'] ?></td>
                            <td><?= $total_items ?></td>
                            <td><?= number_format($total_amount, 0, ',', '.') ?> VND</td>
                            <td>
                                <button class="btn btn-primary btn-sm" onclick="printInvoice('<?= $salesID ?>')">In</button>
                            </td>
                        </tr>
                    <?php endforeach; ?>
                <?php else: ?>
                    <tr>
                        <td colspan="8" class="text-center text-danger">Không có đơn hàng được duyệt</td>
                    </tr>
                <?php endif; ?>
            </tbody>
        </table>

        <!-- Phần này sẽ chứa dữ liệu đơn hàng được chọn để in -->
        <div class="invoice-details" style="display: none;">
            <div class="invoice-header">
                <h2>Hóa đơn <span id="print-invoice-id"></span></h2>
                <p>Ngày: <span id="print-date"></span></p>
            </div>

            <div class="customer-info">
                <h3>Thông tin khách hàng</h3>
                <p><strong>Họ và tên:</strong> <span id="print-fullname"></span></p>
                <p><strong>Email:</strong> <span id="print-email"></span></p>
                <p><strong>Số điện thoại:</strong> <span id="print-phone"></span></p>
                <p><strong>Địa chỉ:</strong> <span id="print-address"></span></p>
            </div>

            <div class="product-section">
                <h3 style="align-items:center">Chi tiết sản phẩm</h3>
                <table class="product-table">
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
                    <tbody id="print-products">
                        <!-- Dữ liệu sản phẩm sẽ được thêm ở đây -->
                    </tbody>
                </table>

                <div class="total-section">
                    <p>Tổng cộng: <span id="print-final-total"></span> VND</p>
                </div>
            </div>

            <div class="footer">
                <p>Cảm ơn quý khách đã mua hàng!</p>
            </div>
        </div>
    </div>

    <script>
        // Lưu trữ dữ liệu hóa đơn dưới dạng JSON để JavaScript có thể truy cập
        var invoices = <?php echo json_encode($invoices); ?>;
        var invoiceDetails = <?php echo json_encode($invoice_details); ?>;

        function printInvoice(salesID) {
            // Lấy thông tin hóa đơn
            var invoice = invoices.find(inv => inv.SalesID == salesID);
            var products = invoiceDetails[salesID];
            
            // Cập nhật nội dung hóa đơn để in
            document.getElementById('print-invoice-id').textContent = '#' + salesID;
            document.getElementById('print-fullname').textContent = invoice.Fullname;
            document.getElementById('print-email').textContent = invoice.Email;
            document.getElementById('print-phone').textContent = invoice.Phone;
            document.getElementById('print-address').textContent = invoice.Address;
            document.getElementById('print-date').textContent = invoice.Date;
            
            // Xóa dữ liệu sản phẩm cũ
            document.getElementById('print-products').innerHTML = '';
            
            // Thêm các sản phẩm vào bảng
            var productsHtml = '';
            var totalAmount = 0;
            
            products.forEach((product, index) => {
                productsHtml += `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${product.ProductName}</td>
                        <td>${product.Author}</td>
                        <td>${formatNumber(product.Price)} VND</td>
                        <td>${product.Quantity}</td>
                        <td>${formatNumber(product.TotalPrice)} VND</td>
                    </tr>
                `;
                totalAmount += parseFloat(product.TotalPrice);
            });
            
            document.getElementById('print-products').innerHTML = productsHtml;
            document.getElementById('print-final-total').textContent = formatNumber(totalAmount);
            
            // Hiển thị phần chi tiết hóa đơn
            document.querySelector(".invoice-details").style.display = "block";
            
            // Ẩn bảng và các phần tử không cần in
            document.querySelector(".table").style.display = "none";
            
            var nonPrintableElements = document.querySelectorAll('.non-printable');
            for (var i = 0; i < nonPrintableElements.length; i++) {
                nonPrintableElements[i].style.display = "none";
            }
            
            // In hóa đơn
            window.print();
            
            // Khôi phục giao diện sau khi in
            setTimeout(function() {
                document.querySelector(".invoice-details").style.display = "none";
                document.querySelector(".table").style.display = "table";
                
                var nonPrintableElements = document.querySelectorAll('.non-printable');
                for (var i = 0; i < nonPrintableElements.length; i++) {
                    nonPrintableElements[i].style.display = "";
                }
            }, 100);
        }
        
        // Hàm định dạng số
        function formatNumber(number) {
            return new Intl.NumberFormat('vi-VN').format(number);
        }
    </script>
</body>

</html>

<?php
$conn->close();
?>