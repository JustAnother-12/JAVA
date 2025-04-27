<?php
$host = "localhost:3306";
$username = "root";
$password = "";
$database = "htttgame";

$conn = new mysqli($host, $username, $password, $database);

if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// Phân trang
$records_per_page = 5;
$page_num = isset($_GET['page_num']) ? intval($_GET['page_num']) : 1;
if ($page_num < 1) {
    $page_num = 1; // Đảm bảo giá trị $page_num luôn >= 1
}
$offset = ($page_num - 1) * $records_per_page;
if ($offset < 0) {
    $offset = 0; // Đảm bảo giá trị $offset luôn >= 0
}

// Xử lý tìm kiếm theo ngày
$search_date = isset($_GET['search_date']) ? $_GET['search_date'] : '';
$date_condition = '';
if (!empty($search_date)) {
    $date_condition = "AND DATE(sales_invoice.Date) = '" . $conn->real_escape_string($search_date) . "'";
}

// Đếm tổng số hóa đơn để phân trang
$count_sql = "SELECT COUNT(DISTINCT sales_invoice.SalesID) as total
        FROM 
            Customer
        INNER JOIN 
            sales_invoice ON Customer.CustomerID = sales_invoice.CustomerID
        INNER JOIN 
            detail_sales_invoice ON sales_invoice.SalesID = detail_sales_invoice.SalesID
        WHERE 
            detail_sales_invoice.Order_status = 'Đã duyệt'
            $date_condition";

$count_result = $conn->query($count_sql);
$count_row = $count_result->fetch_assoc();
$total_records = $count_row['total'];
$total_pages = ceil($total_records / $records_per_page);
if ($total_pages < 1) {
    $total_pages = 1; // Đảm bảo tổng số trang luôn >= 1
}

// Lấy tất cả các hóa đơn đã duyệt với phân trang và tìm kiếm
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
            $date_condition
        GROUP BY
            sales_invoice.SalesID
        ORDER BY
            sales_invoice.Date DESC
        LIMIT $offset, $records_per_page";

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
    <style>
        .search-bar {
            margin-bottom: 20px;
        }
        .pagination {
            margin-top: 20px;
            justify-content: center;
        }
        @media print {
            .non-printable, .pagination, .search-bar {
                display: none !important;
            }
        }
        .invoice-details {
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #fff;
        }
        .customer-info {
            margin-bottom: 20px;
        }
        .product-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        .product-table th, .product-table td {
            padding: 8px;
            border: 1px solid #ddd;
        }
        .product-table th {
            background-color: #f2f2f2;
        }
        .total-section {
            text-align: right;
            font-weight: bold;
            font-size: 1.1em;
            margin-top: 10px;
        }
        .footer {
            margin-top: 30px;
            text-align: center;
            font-style: italic;
        }
        /* Thiết lập chung */
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f8f9fa;
            color: #333;
            margin: 0;
            padding: 30px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            border-radius: 12px;
        }

        /* Tiêu đề chung */
        h2.text-center,
        .invoice-header h2 {
            color: #2c3e50;
            font-weight: 500;
            margin-bottom: 30px;
            position: relative;
            padding-bottom: 10px;
            text-align: center;
            margin-top: 50px;
        }

        h2.text-center:after,
        .invoice-header h2:after {
            content: "";
            position: absolute;
            width: 80px;
            height: 3px;
            background-color: #3498db;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
        }

        /* Bảng */
        .table {
            width: 100%;
            margin-bottom: 20px;
            border-collapse: collapse;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
        }

        .table thead {
            background-color: #2ea1ed;
            color: white;
        }

        .table th {
            font-weight: 600;
            text-transform: uppercase;
            font-size: 14px;
            padding: 12px !important;
            text-align: center;
        }

        .table td {
            padding: 12px !important;
            vertical-align: middle;
            text-align: center;
            font-size: 14px;
            color: #555;
        }

        .table tbody tr:nth-child(even) {
            background-color: #f7faff;
        }

        .table tbody tr:hover {
            background-color: rgba(52, 152, 219, 0.05);
        }

        /* Nút */
        .btn {
            padding: 8px 20px;
            font-weight: 500;
            border-radius: 5px;
            transition: all 0.3s;
        }

        .btn-primary {
            background-color: #0d6efd;
            color: white;
            border: none;
        }

        .btn-primary:hover {
            background-color: #0b5ed7;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .btn-info {
            background-color: #3498db;
            color: white;
            border: none;
        }

        .btn-info:hover {
            background-color: #2980b9;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .btn-success {
            background-color: #2ecc71;
            color: white;
            border: none;
        }

        .btn-success:hover {
            background-color: #27ae60;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .btn-danger {
            background-color: #e74c3c;
            color: white;
            border: none;
        }

        .btn-danger:hover {
            background-color: #c0392b;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        /* Modal */
        .modal-content {
            border: none;
            border-radius: 10px;
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
        }

        .modal-header {
            background-color: #3498db;
            color: white;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
            padding: 15px 20px;
        }

        .modal-title {
            font-weight: 600;
            font-size: 20px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .modal-body {
            padding: 20px;
            background-color: #f9f9f9;
        }

        .modal-body .customer-info,
        .modal-body .product-section {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
        }

        .modal-body .customer-info h3,
        .modal-body .product-section h3 {
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
            border-bottom: 2px solid #3498db;
            padding-bottom: 5px;
            text-align: center;
        }

        .modal-body .table {
            margin-bottom: 0;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
        }

        .modal-body .table thead {
            background-color: #3498db;
            color: white;
            text-transform: uppercase;
            font-size: 14px;
        }

        .modal-body .table th,
        .modal-body .table td {
            padding: 10px;
            text-align: center;
            font-size: 14px;
            color: #555;
        }

        .modal-body .table tbody tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .modal-body .table tbody tr:hover {
            background-color: rgba(52, 152, 219, 0.1);
        }

        .modal-body .total-section {
            margin-top: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
            text-align: right;
        }

        .modal-body .total-section h4 {
            font-size: 18px;
            font-weight: 600;
            color: #27ae60;
            margin: 0;
        }

        .modal-footer {
            border-top: 1px solid #ddd;
            padding: 15px 20px;
            background-color: #f9f9f9;
            justify-content: flex-end;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .table {
                display: block;
                overflow-x: auto;
            }

            .modal-body p strong {
                min-width: 100px;
            }

            .modal-body .customer-info p strong {
                min-width: 100px;
            }

            .modal-body .table {
                display: block;
                overflow-x: auto;
            }
        }

        /* Printed Invoice Styling */
        @media print {
            body {
                background-color: white !important;
                color: black !important;
                font-size: 14px;
                margin: 0;
                padding: 0;
            }

            .invoice-container {
                width: 100%;
                max-width: 100%;
                padding: 15px;
                border: none;
                box-shadow: none;
            }

            .invoice-header {
                text-align: center;
                margin-bottom: 25px;
                padding-bottom: 15px;
                border-bottom: 1px solid #ddd;
            }

            .invoice-header h2 {
                font-size: 26px;
                color: #333 !important;
            }

            .table {
                display: none;
                /* Hide table when printing */
            }

            .invoice-details {
                display: block !important;
                width: 90%;
                margin: 0 auto;
            }

            .customer-info {
                margin-bottom: 25px;
                padding: 15px;
                border: 1px solid #e9f2ff;
                border-radius: 8px;
                background-color: #f8fbff;
            }

            .customer-info h3 {
                color: #0d6efd;
                border-bottom: 1px solid #e9f2ff;
                padding-bottom: 8px;
                margin-bottom: 15px;
                font-size: 18px;
            }

            .product-section h3 {
                color: #0d6efd;
                border-bottom: 1px solid #e9f2ff;
                padding-bottom: 8px;
                margin-bottom: 15px;
                font-size: 18px;
            }

            .product-table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }

            .product-table th,
            .product-table td {
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #e9f2ff;
            }

            .product-table th {
                font-weight: bold;
                background-color: #f0f7ff;
            }

            .total-section {
                margin-top: 30px;
                text-align: right;
                font-weight: bold;
                font-size: 16px;
                padding: 10px 15px;
                background-color: #f0f7ff;
                border-radius: 8px;
                border: 1px solid #cfe2ff;
            }

            .footer {
                margin-top: 40px;
                text-align: center;
                font-size: 12px;
                color: #777;
                border-top: 1px solid #eee;
                padding-top: 15px;
            }
        }

        /* Additional Styling */
        .text-center {
            text-align: center;
        }

        .text-danger {
            color: #dc3545 !important;
        }

        /* Making the invoice table more responsive */
        @media (max-width: 992px) {
            .table {
                display: block;
                overflow-x: auto;
                white-space: nowrap;
            }
        }

        /* Adding extra styling for row distinction */
        .table tbody tr {
            transition: all 0.2s ease;
        }

        /* Add a subtle border to the invoice container */
        .invoice-container {
            border: 1px solid #e9f2ff;
        }

        /* Style for the print button */
        .btn-primary {
            background-color: #0d6efd;
            border-color: #0d6efd;
            box-shadow: 0 2px 4px rgba(13, 110, 253, 0.2);
        }

        /* Tiêu đề nhỏ trong hóa đơn */
        .customer-info h3,
        .product-section h3 {
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
            position: relative;
            display: inline-block;
            padding-bottom: 5px;
            text-align: center;
            /* Căn giữa nội dung */
            width: 100%;
            /* Đảm bảo tiêu đề chiếm toàn bộ chiều rộng */
        }

        .customer-info h3:after,
        .product-section h3:after {
            content: "";
            position: absolute;
            width: 80px;
            /* Độ dài đường gạch dưới */
            height: 3px;
            background-color: #3498db;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
        }
        /* Căn giữa danh sách hóa đơn */
        .invoice-container {
            max-width: 1200px;
            margin: 0 auto;
            /* Căn giữa theo chiều ngang */
            padding: 20px;
            border-radius: 12px;
            background-color: #ffffff;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        /* Căn giữa tiêu đề */
        .invoice-header h2 {
            text-align: center;
            /* Căn giữa tiêu đề */
            color: #2c3e50;
            font-weight: 600;
            margin-bottom: 30px;
            position: relative;
            padding-bottom: 10px;
        }

        .invoice-header h2:after {
            content: "";
            position: absolute;
            width: 80px;
            height: 3px;
            background-color: #3498db;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
        }

        /* Căn giữa các phần tử trong bảng */
        .table {
            width: 100%;
            margin-bottom: 20px;
            border-collapse: collapse;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
        }

        .table th,
        .table td {
            text-align: center;
            /* Căn giữa nội dung trong bảng */
            vertical-align: middle;
            /* Căn giữa theo chiều dọc */
            padding: 12px;
            font-size: 14px;
            color: #555;
        }

        .table thead {
            background-color: #2ea1ed;
            color: white;
        }

        .table tbody tr:nth-child(even) {
            background-color: #f7faff;
        }

        .table tbody tr:hover {
            background-color: rgba(52, 152, 219, 0.05);
        }

        /* Nút */
        .btn {
            font-size: 14px;
            padding: 6px 12px;
            border-radius: 5px;
        }

        .btn-primary {
            background-color: #3498db;
            border-color: #3498db;
            color: white;
        }

        .btn-primary:hover {
            background-color: #2980b9;
            border-color: #2980b9;
        }
    </style>
</head>

<body>
    <div class="container invoice-container">
        <div class="invoice-header non-printable">
            <h2>Danh sách hóa đơn</h2>
        </div>

        <!-- Tìm kiếm theo ngày -->
        <div class="search-bar non-printable">
            <form method="GET" class="row g-3">
                <input type="hidden" name="page" value="print_invoice">
                <div class="col-md-4">
                    <input type="date" class="form-control" id="search_date" name="search_date" value="<?= htmlspecialchars($search_date) ?>">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                </div>
                <?php if (!empty($search_date)): ?>
                <div class="col-md-2 d-flex align-items-end">
                    <a href="index.php?page=print_invoice" class="btn btn-secondary">Xóa bộ lọc</a>
                </div>
                <?php endif; ?>
            </form>
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
                            <td><?= $offset + $index + 1 ?></td>
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

        <!-- Phân trang -->
        <?php if ($total_pages > 1): ?>
        <nav aria-label="Page navigation" class="non-printable">
            <ul class="pagination">
                <?php if ($page_num > 1): ?>
                    <li class="page-item">
                        <a class="page-link" href="index.php?page=print_invoice&page_num=1<?= !empty($search_date) ? '&search_date='.$search_date : '' ?>" aria-label="First">
                            <span aria-hidden="true">&laquo;&laquo;</span>
                        </a>
                    </li>
                    <li class="page-item">
                        <a class="page-link" href="index.php?page=print_invoice&page_num=<?= $page_num - 1 ?><?= !empty($search_date) ? '&search_date='.$search_date : '' ?>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                <?php endif; ?>

                <?php
                // Hiển thị tối đa 5 nút trang
                $start_page = max(1, min($page_num - 2, $total_pages - 4));
                $end_page = min($total_pages, max(5, $page_num + 2));

                for ($i = $start_page; $i <= $end_page; $i++):
                ?>
                    <li class="page-item <?= ($i == $page_num) ? 'active' : '' ?>">
                        <a class="page-link" href="index.php?page=print_invoice&page_num=<?= $i ?><?= !empty($search_date) ? '&search_date='.$search_date : '' ?>"><?= $i ?></a>
                    </li>
                <?php endfor; ?>

                <?php if ($page_num < $total_pages): ?>
                    <li class="page-item">
                        <a class="page-link" href="index.php?page=print_invoice&page_num=<?= $page_num + 1 ?><?= !empty($search_date) ? '&search_date='.$search_date : '' ?>" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                    <li class="page-item">
                        <a class="page-link" href="index.php?page=print_invoice&page_num=<?= $total_pages ?><?= !empty($search_date) ? '&search_date='.$search_date : '' ?>" aria-label="Last">
                            <span aria-hidden="true">&raquo;&raquo;</span>
                        </a>
                    </li>
                <?php endif; ?>
            </ul>
        </nav>
        <?php endif; ?>

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
            setTimeout(function () {
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