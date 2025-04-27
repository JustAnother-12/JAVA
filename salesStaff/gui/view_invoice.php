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

// Thiết lập phân trang
$limit = 5; // Số lượng hóa đơn trên mỗi trang
$page = isset($_GET['page_num']) ? (int)$_GET['page_num'] : 1;
$start = ($page - 1) * $limit;

// Xử lý tìm kiếm theo ngày
$search_date = "";
$where_condition = "detail_sales_invoice.Order_status = 'Đã duyệt'";

if (isset($_GET['search_date']) && !empty($_GET['search_date'])) {
    $search_date = $_GET['search_date'];
    $where_condition .= " AND DATE(sales_invoice.Date) = '$search_date'";
}

// Đếm tổng số hóa đơn thỏa điều kiện tìm kiếm
$count_sql = "SELECT COUNT(DISTINCT sales_invoice.SalesID) as total FROM 
                sales_invoice 
              INNER JOIN detail_sales_invoice ON sales_invoice.SalesID = detail_sales_invoice.SalesID
              WHERE $where_condition";
              
$count_result = $conn->query($count_sql);
$total_records = $count_result->fetch_assoc()['total'];
$total_pages = ceil($total_records / $limit);

// Truy vấn danh sách hóa đơn với phân trang - Nhóm theo SalesID
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
            $where_condition
        GROUP BY 
            sales_invoice.SalesID, 
            Customer.Username,
            Customer.Fullname,
            Customer.Phone,
            Customer.Address,
            sales_invoice.Date
        ORDER BY 
            sales_invoice.Date DESC
        LIMIT $start, $limit";

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
    <style>
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            color: #333;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            border-radius: 8px;
        }

        /* Tiêu đề trang */
        h2.text-center {
            color: #2c3e50;
            font-weight: 600;
            margin-bottom: 30px;
            position: relative;
            padding-bottom: 10px;
        }

        h2.text-center:after {
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
            overflow: hidden;
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

        .modal-body .customer-info {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
        }

        .modal-body .customer-info h3 {
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
            border-bottom: 2px solid #3498db;
            padding-bottom: 5px;
        }

        .modal-body .customer-info p {
            margin: 0;
            padding: 5px 0;
            font-size: 14px;
            color: #555;
        }

        .modal-body .customer-info p strong {
            color: #2c3e50;
            font-weight: 600;
            min-width: 120px;
            display: inline-block;
        }

        .modal-body .product-section {
            margin-top: 20px;
        }

        .modal-body .product-section h3 {
            font-size: 18px;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
            border-bottom: 2px solid #3498db;
            padding-bottom: 5px;
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

        /* Thanh tìm kiếm */
        .search-bar {
            margin-bottom: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
        }

        .search-bar input[type="date"] {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            width: 250px;
        }

        .search-bar button {
            padding: 8px 20px;
            font-size: 14px;
            font-weight: 500;
            border-radius: 5px;
            border: none;
            background-color: #3498db;
            color: white;
            transition: all 0.3s ease;
        }

        .search-bar button:hover {
            background-color: #2980b9;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .search-bar a.btn-secondary {
            padding: 8px 20px;
            font-size: 14px;
            font-weight: 500;
            border-radius: 5px;
            border: none;
            background-color: #6c757d;
            color: white;
            transition: all 0.3s ease;
        }

        .search-bar a.btn-secondary:hover {
            background-color: #5a6268;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
    </style>
   
</head>

<body>
    <div class="container mt-5">
        <h2 class="text-center" style="margin-bottom:15px">Danh sách hóa đơn</h2>
        
        <!-- Form tìm kiếm theo ngày -->
        <div class="row mb-4">
            <div class="col-md-6 offset-md-3">
                <form method="GET" action="" class="d-flex search-bar">
                    <input type="hidden" name="page" value="view_invoice">
                    <input type="date" name="search_date" class="form-control me-2" value="<?= htmlspecialchars($search_date) ?>">
                    <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                    <?php if (!empty($search_date)): ?>
                        <a href="index.php?page=view_invoice" class="btn btn-secondary ms-2">Xóa bộ lọc</a>
                    <?php endif; ?>
                </form>
            </div>
        </div>
        
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
                    <?php $index = $start + 1; ?>
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
                            <td><?= date('d/m/Y', strtotime($row['Date'])) ?></td>
                            <td><?= $productCount ?></td>
                            <td><?= number_format($row['GrandTotal'], 0, ',', '.') ?> VND</td>
                            <td>
                                <button class="btn btn-info btn-sm view-invoice" data-bs-toggle="modal"
                                    data-bs-target="#invoiceModal" data-salesid="<?= $row['SalesID'] ?>"
                                    data-username="<?= $row['Username'] ?>" data-fullname="<?= $row['Fullname'] ?>"
                                    data-phone="<?= $row['Phone'] ?>" data-address="<?= $row['Address'] ?>"
                                    data-date="<?= date('d/m/Y H:i:s', strtotime($row['Date'])) ?>" 
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
        
        <!-- Phân trang -->
        <?php if ($total_pages > 1): ?>
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <li class="page-item <?= ($page <= 1) ? 'disabled' : '' ?>">
                    <a class="page-link" href="<?= ($page <= 1) ? '#' : "index.php?page=view_invoice&page_num=".($page-1).(!empty($search_date) ? "&search_date=$search_date" : "") ?>" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                
                <?php for($i = 1; $i <= $total_pages; $i++): ?>
                    <li class="page-item <?= ($page == $i) ? 'active' : '' ?>">
                        <a class="page-link" href="index.php?page=view_invoice&page_num=<?= $i ?><?= !empty($search_date) ? "&search_date=$search_date" : "" ?>">
                            <?= $i ?>
                        </a>
                    </li>
                <?php endfor; ?>
                
                <li class="page-item <?= ($page >= $total_pages) ? 'disabled' : '' ?>">
                    <a class="page-link" href="<?= ($page >= $total_pages) ? '#' : "index.php?page=view_invoice&page_num=".($page+1).(!empty($search_date) ? "&search_date=$search_date" : "") ?>" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
        <?php endif; ?>
    </div>

    <div class="modal fade" id="invoiceModal" tabindex="-1" aria-labelledby="invoiceModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="invoiceModalLabel">Chi tiết hóa đơn #<span id="modal-salesid"></span>
                    </h5>
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
                        <td>${Number(prices[i]).toLocaleString('vi')} VND</td>
                        <td>${quantities[i]}</td>
                        <td>${Number(itemTotalPrices[i]).toLocaleString('vi')} VND</td>
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