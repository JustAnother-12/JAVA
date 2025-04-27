<?php
$servername = "localhost:3306";
$username = "root";
$password = "";
$database = "htttgame";

$conn = new mysqli($servername, $username, $password, $database);
if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// Viết log ra file
function writeDebugLog($message)
{
    $logFile = "debug_orders.log";
    $timestamp = date('Y-m-d H:i:s');
    file_put_contents($logFile, "[$timestamp] $message" . PHP_EOL, FILE_APPEND);
}

// Xử lý tìm kiếm theo ngày
$date_filter = '';
$start_date = '';
$end_date = '';

if (isset($_GET['start_date']) && !empty($_GET['start_date'])) {
    $start_date = $_GET['start_date'];
    $date_filter .= " AND sales_invoice.Date >= '$start_date'";
}

if (isset($_GET['end_date']) && !empty($_GET['end_date'])) {
    $end_date = $_GET['end_date'];
    $date_filter .= " AND sales_invoice.Date <= '$end_date 23:59:59'";
}

// Đếm tổng số đơn hàng để phân trang
$count_sql = "SELECT COUNT(DISTINCT sales_invoice.SalesID) as total FROM 
                Customer
              INNER JOIN 
                sales_invoice ON Customer.CustomerID = sales_invoice.CustomerID
              INNER JOIN 
                detail_sales_invoice ON sales_invoice.SalesID = detail_sales_invoice.SalesID
              WHERE 
                1=1 $date_filter
              GROUP BY 
                sales_invoice.SalesID
              HAVING 
                CASE 
                    WHEN SUM(CASE WHEN detail_sales_invoice.Order_status = 'Chưa duyệt' THEN 1 ELSE 0 END) = COUNT(*) THEN 'Chưa duyệt'
                    WHEN SUM(CASE WHEN detail_sales_invoice.Order_status = 'Đã hủy' THEN 1 ELSE 0 END) = COUNT(*) THEN 'Đã hủy'
                    WHEN SUM(CASE WHEN detail_sales_invoice.Order_status = 'Đã duyệt' THEN 1 ELSE 0 END) = COUNT(*) THEN 'Đã duyệt'
                    ELSE 'Đang xử lý' 
                END = 'Chưa duyệt'";

$count_result = $conn->query($count_sql);
$total_orders = 0;
while ($row = $count_result->fetch_assoc()) {
    $total_orders++;
}

// Thiết lập phân trang
$items_per_page = 5;
$total_pages = ceil($total_orders / $items_per_page);
$current_page = isset($_GET['page_num']) ? max(1, min($total_pages, intval($_GET['page_num']))) : 1;
$offset = ($current_page - 1) * $items_per_page;

// Lấy danh sách đơn hàng, gom nhóm theo SalesID với phân trang
$sql = "SELECT 
            sales_invoice.SalesID,
            Customer.Username,
            Customer.Fullname,
            Customer.Email,
            Customer.Phone,
            sales_invoice.Date,
            COUNT(detail_sales_invoice.DetailSalesID) as ProductCount,
            SUM(detail_sales_invoice.Quantity) as TotalQuantity,
            SUM(detail_sales_invoice.TotalPrice) as TotalOrderPrice,
            CASE 
                WHEN SUM(CASE WHEN detail_sales_invoice.Order_status = 'Chưa duyệt' THEN 1 ELSE 0 END) = COUNT(*) THEN 'Chưa duyệt'
                WHEN SUM(CASE WHEN detail_sales_invoice.Order_status = 'Đã hủy' THEN 1 ELSE 0 END) = COUNT(*) THEN 'Đã hủy'
                WHEN SUM(CASE WHEN detail_sales_invoice.Order_status = 'Đã duyệt' THEN 1 ELSE 0 END) = COUNT(*) THEN 'Đã duyệt'
                ELSE 'Đang xử lý' 
            END as Order_status
        FROM 
            Customer
        INNER JOIN 
            sales_invoice ON Customer.CustomerID = sales_invoice.CustomerID
        INNER JOIN 
            detail_sales_invoice ON sales_invoice.SalesID = detail_sales_invoice.SalesID
        WHERE 
            1=1 $date_filter
        GROUP BY 
            sales_invoice.SalesID
        HAVING 
            Order_status = 'Chưa duyệt'
        ORDER BY 
            sales_invoice.Date DESC
        LIMIT $offset, $items_per_page";

$result = $conn->query($sql);

// Truy vấn lấy chi tiết sản phẩm của tất cả đơn hàng để lưu vào biến
$order_details = [];
if ($result && $result->num_rows > 0) {
    // Tạo mảng chứa tất cả SalesID để truy vấn một lần
    $sales_ids = [];
    $result_copy = $result;  // Tạo bản sao để không ảnh hưởng đến vòng lặp chính
    while ($row = $result_copy->fetch_assoc()) {
        $sales_ids[] = $row['SalesID'];
    }

    // Reset con trỏ để có thể dùng lại $result
    $result->data_seek(0);

    if (!empty($sales_ids)) {
        // Tạo câu điều kiện IN với các SalesID
        $sales_ids_str = "'" . implode("','", $sales_ids) . "'";

        // Truy vấn tất cả chi tiết sản phẩm của các đơn hàng
        $detail_sql = "SELECT 
                        d.SalesID,
                        p.ProductID,
                        p.ProductName,
                        d.Quantity,
                        d.TotalPrice / d.Quantity as UnitPrice,
                        d.TotalPrice,
                        d.Order_status
                    FROM 
                        detail_sales_invoice d
                    INNER JOIN 
                        Product p ON d.ProductID = p.ProductID
                    WHERE 
                        d.SalesID IN ($sales_ids_str)";

        $detail_result = $conn->query($detail_sql);

        if ($detail_result && $detail_result->num_rows > 0) {
            while ($detail_row = $detail_result->fetch_assoc()) {
                // Định dạng giá tiền
                $detail_row['UnitPrice'] = number_format($detail_row['UnitPrice'], 0, ',', '.') . " VND";
                $detail_row['TotalPrice'] = number_format($detail_row['TotalPrice'], 0, ',', '.') . " VND";

                // Thêm vào mảng kết quả, nhóm theo SalesID
                $order_details[$detail_row['SalesID']][] = $detail_row;
            }
        }
    }
}
?>

<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý đơn hàng</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="../assets/css/detail_order_managerment.css">
    <style>
        .search-box {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .pagination {
            justify-content: center;
            margin-top: 20px;
        }


        /* Thiết lập chung */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            color: #333;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        /* Tiêu đề */
        h2.text-center {
            color: #2c3e50;
            font-weight: 600;
            margin-bottom: 30px;
            position: relative;
            padding-bottom: 10px;
            text-align: center;
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
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
            margin-bottom: 30px;
            background-color: #ffffff;
        }

        .fw-bold {
            margin-bottom: 22px;
        }

        .table thead {
            background-color: #2ea1ed;
            color: white;
            text-transform: uppercase;
            font-size: 14px;
        }

        .table th {
            font-weight: 600;
            padding: 12px !important;
            text-align: center;
        }

        .table td {
            padding: 12px !important;
            vertical-align: middle;
            text-align: center;
            font-size: 14px;
            color: #495057;
        }

        .table tbody tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .table tbody tr:hover {
            background-color: rgba(52, 152, 219, 0.1);
            cursor: pointer;
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
            font-size: 18px;
        }

        .modal-body {
            padding: 20px;
            background-color: #f9f9f9;
        }

        .modal-body p {
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
            display: flex;
            align-items: center;
        }

        .modal-body p:last-child {
            border-bottom: none;
            margin-bottom: 0;
        }

        .modal-body p strong {
            min-width: 140px;
            color: #7f7f8d;
            display: inline-block;
        }

        .modal-body p span {
            font-weight: 500;
            color: #6ba1d7;
        }

        #modal-product {
            color: #eb53dc;
            font-weight: 600;
        }

        #modal-quantity {
            color: #7250ed;
            font-weight: 600;
        }

        #modal-totalprice {
            color: #27ae60;
            font-weight: 600;
        }

        .modal-footer {
            border-top: 1px solid #eee;
            padding: 15px 20px;
            justify-content: flex-end;
        }

        /* Trạng thái */
        .status-pending {
            padding: 5px 10px;
            background-color: #f39c12;
            color: white;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 500;
        }

        .status-approved {
            padding: 5px 10px;
            background-color: #2ecc71;
            color: white;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 500;
        }

        .status-cancelled {
            padding: 5px 10px;
            background-color: #e74c3c;
            color: white;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 500;
        }

        /* Hình ảnh */
        img {
            width: 50px;
            height: auto;
            border-radius: 4px;
        }

        /* Không có đơn hàng */
        .no-orders {
            text-align: center;
            font-size: 16px;
            color: #6c757d;
            padding: 20px;
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
        }
    </style>
</head>

<body>
    <div class="container">
        <h2 class="text-center my-4">Quản lý đơn hàng</h2>

        <!-- Form tìm kiếm theo ngày -->
        <div class="search-box">
            <form method="GET" action="" class="row g-3">
                <input type="hidden" name="page" value="order_management">
                <div class="col-md-4">
                    <label for="start_date" class="form-label">Từ ngày:</label>
                    <input type="date" class="form-control" id="start_date" name="start_date"
                        value="<?php echo $start_date; ?>">
                </div>
                <div class="col-md-4">
                    <label for="end_date" class="form-label">Đến ngày:</label>
                    <input type="date" class="form-control" id="end_date" name="end_date"
                        value="<?php echo $end_date; ?>">
                </div>
                <div class="col-md-4 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary me-2" style="background-color: #3498db;">Tìm kiếm</button>
                    <a href="index.php?page=order_management" class="btn btn-secondary">Đặt lại</a>
                </div>
            </form>
        </div>

        <table class="table table-bordered table-hover">
            <thead class="table-primary">
                <tr>
                    <th>Mã đơn hàng</th>
                    <th>Username</th>
                    <th>Họ và tên</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Ngày mua</th>
                    <th>Số sản phẩm</th>
                    <th>Số lượng</th>
                    <th>Tổng giá</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <?php
                if ($result && $result->num_rows > 0) {
                    while ($row = $result->fetch_assoc()) {
                        echo "<tr>
                                <td>{$row['SalesID']}</td>
                                <td>{$row['Username']}</td>
                                <td>{$row['Fullname']}</td>
                                <td>{$row['Email']}</td>
                                <td>{$row['Phone']}</td>
                                <td>{$row['Date']}</td>
                                <td>{$row['ProductCount']}</td>
                                <td>{$row['TotalQuantity']}</td>
                                <td>" . number_format($row['TotalOrderPrice'], 0, ',', '.') . " VND</td>
                                <td>{$row['Order_status']}</td>
                                <td>
                                    <button class='btn btn-info btn-sm view-details' data-bs-toggle='modal' data-bs-target='#orderModal' 
                                        data-order-id='{$row['SalesID']}'
                                        data-username='{$row['Username']}'
                                        data-fullname='{$row['Fullname']}'
                                        data-email='{$row['Email']}'
                                        data-phone='{$row['Phone']}'
                                        data-date='{$row['Date']}'
                                        data-totalprice='" . number_format($row['TotalOrderPrice'], 0, ',', '.') . " VND'>
                                        Duyệt
                                    </button>
                                </td>
                            </tr>";
                    }
                } else {
                    echo "<tr><td colspan='11' class='text-center'>Không có đơn hàng nào.</td></tr>";
                }
                ?>
            </tbody>
        </table>

        <!-- Phân trang -->
        <?php if ($total_pages > 1): ?>
            <nav aria-label="Phân trang đơn hàng">
                <ul class="pagination">
                    <?php if ($current_page > 1): ?>
                        <li class="page-item">
                            <a class="page-link"
                                href="?page=order_management&page_num=<?php echo $current_page - 1; ?><?php echo $start_date ? '&start_date=' . $start_date : ''; ?><?php echo $end_date ? '&end_date=' . $end_date : ''; ?>"
                                aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    <?php endif; ?>

                    <?php
                    // Hiển thị các nút số trang
                    $start_page = max(1, $current_page - 2);
                    $end_page = min($total_pages, $current_page + 2);

                    if ($start_page > 1) {
                        echo '<li class="page-item"><a class="page-link" href="?page=order_management&page_num=1' . ($start_date ? '&start_date=' . $start_date : '') . ($end_date ? '&end_date=' . $end_date : '') . '">1</a></li>';
                        if ($start_page > 2) {
                            echo '<li class="page-item disabled"><a class="page-link" href="#">...</a></li>';
                        }
                    }

                    for ($i = $start_page; $i <= $end_page; $i++) {
                        echo '<li class="page-item ' . ($i == $current_page ? 'active' : '') . '">
                        <a class="page-link" href="?page=order_management&page_num=' . $i . ($start_date ? '&start_date=' . $start_date : '') . ($end_date ? '&end_date=' . $end_date : '') . '">' . $i . '</a>
                    </li>';
                    }

                    if ($end_page < $total_pages) {
                        if ($end_page < $total_pages - 1) {
                            echo '<li class="page-item disabled"><a class="page-link" href="#">...</a></li>';
                        }
                        echo '<li class="page-item"><a class="page-link" href="?page=order_management&page_num=' . $total_pages . ($start_date ? '&start_date=' . $start_date : '') . ($end_date ? '&end_date=' . $end_date : '') . '">' . $total_pages . '</a></li>';
                    }
                    ?>

                    <?php if ($current_page < $total_pages): ?>
                        <li class="page-item">
                            <a class="page-link"
                                href="?page=order_management&page_num=<?php echo $current_page + 1; ?><?php echo $start_date ? '&start_date=' . $start_date : ''; ?><?php echo $end_date ? '&end_date=' . $end_date : ''; ?>"
                                aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    <?php endif; ?>
                </ul>
            </nav>
        <?php endif; ?>

        <!-- Modal Chi tiết đơn hàng -->
        <div class="modal fade" id="orderModal" tabindex="-1" aria-labelledby="orderModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="orderModalLabel">Chi tiết đơn hàng #<span
                                id="modal-order-id-display"></span></h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Thông tin khách hàng -->
                        <div class="mb-4">
                            <h6 class="fw-bold">Thông tin khách hàng</h6>
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Username:</strong> <span id="modal-username"></span></p>
                                    <p><strong>Họ và tên:</strong> <span id="modal-fullname"></span></p>
                                    <p><strong>Email:</strong> <span id="modal-email"></span></p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Số điện thoại:</strong> <span id="modal-phone"></span></p>
                                    <p><strong>Ngày mua:</strong> <span id="modal-date"></span></p>
                                    <p><strong>Tổng giá:</strong> <span id="modal-totalprice"></span></p>
                                </div>
                            </div>
                        </div>

                        <!-- Danh sách sản phẩm -->
                        <h6 class="fw-bold">Danh sách sản phẩm</h6>
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <thead class="table-light">
                                    <tr>
                                        <th>Mã SP</th>
                                        <th>Tên sản phẩm</th>
                                        <th>Số lượng</th>
                                        <th>Đơn giá</th>
                                        <th>Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody id="product-details">
                                    <!-- Dữ liệu sản phẩm sẽ được thêm vào đây bằng JavaScript -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <form method="POST" action="">
                            <input type="hidden" name="sales_id" id="modal-order-id">
                            <input type="hidden" name="current_page" value="<?php echo $current_page; ?>">
                            <input type="hidden" name="start_date" value="<?php echo $start_date; ?>">
                            <input type="hidden" name="end_date" value="<?php echo $end_date; ?>">
                            <button type="submit" name="approve_all_order" class="btn btn-success">Duyệt đơn
                                hàng</button>
                            <button type="submit" name="cancel_all_order" class="btn btn-danger"
                                style="margin-left: 15px;" onclick="return confirmCancelOrder()">Hủy đơn hàng</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Tạo đối tượng JavaScript chứa dữ liệu chi tiết đơn hàng từ PHP
            const orderDetailsData = <?php echo json_encode($order_details); ?>;

            // Sự kiện khi modal hiển thị
            document.querySelectorAll('.view-details').forEach(button => {
                button.addEventListener('click', function () {
                    const salesId = this.getAttribute('data-order-id');

                    // Hiển thị thông tin cơ bản
                    document.getElementById('modal-order-id-display').textContent = salesId;
                    document.getElementById('modal-order-id').value = salesId;
                    document.getElementById('modal-username').textContent = this.getAttribute('data-username');
                    document.getElementById('modal-fullname').textContent = this.getAttribute('data-fullname');
                    document.getElementById('modal-email').textContent = this.getAttribute('data-email');
                    document.getElementById('modal-phone').textContent = this.getAttribute('data-phone');
                    document.getElementById('modal-date').textContent = this.getAttribute('data-date');
                    document.getElementById('modal-totalprice').textContent = this.getAttribute('data-totalprice');

                    // Hiển thị chi tiết sản phẩm từ dữ liệu đã nạp sẵn
                    const productDetails = document.getElementById('product-details');
                    productDetails.innerHTML = ''; // Xóa nội dung cũ

                    if (!orderDetailsData[salesId] || orderDetailsData[salesId].length === 0) {
                        productDetails.innerHTML = '<tr><td colspan="6" class="text-center">Không có sản phẩm nào.</td></tr>';
                        return;
                    }

                    // Thêm từng sản phẩm vào bảng
                    orderDetailsData[salesId].forEach(product => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${product.ProductID}</td>
                            <td>${product.ProductName}</td>
                            <td>${product.Quantity}</td>
                            <td>${product.UnitPrice}</td>
                            <td>${product.TotalPrice}</td>
                        `;
                        productDetails.appendChild(row);
                    });
                });
            });

            function confirmCancelOrder() {
                return confirm("Bạn có chắc chắn muốn hủy tất cả sản phẩm trong đơn hàng này không?");
            }

            // Kiểm tra và đặt giá trị cho trường ngày tìm kiếm
            document.addEventListener('DOMContentLoaded', function () {
                const startDate = document.getElementById('start_date');
                const endDate = document.getElementById('end_date');

                // Đặt giá trị mặc định là ngày hôm nay nếu chưa có
                endDate.addEventListener('change', function () {
                    if (startDate.value && this.value && new Date(startDate.value) > new Date(this.value)) {
                        alert('Ngày kết thúc phải sau ngày bắt đầu!');
                        this.value = '';
                    }
                });

                startDate.addEventListener('change', function () {
                    if (endDate.value && this.value && new Date(this.value) > new Date(endDate.value)) {
                        alert('Ngày bắt đầu phải trước ngày kết thúc!');
                        this.value = '';
                    }
                });
            });
        </script>
    </div>
</body>

</html>

<?php
// Xử lý khi duyệt tất cả sản phẩm trong đơn hàng
if (isset($_POST['approve_all_order'])) {
    $sales_id = $_POST['sales_id'];
    $current_page = isset($_POST['current_page']) ? $_POST['current_page'] : 1;
    $start_date_param = isset($_POST['start_date']) ? "&start_date=" . $_POST['start_date'] : "";
    $end_date_param = isset($_POST['end_date']) ? "&end_date=" . $_POST['end_date'] : "";

    writeDebugLog("Bắt đầu duyệt tất cả sản phẩm - SalesID: $sales_id");

    $conn->begin_transaction();

    try {
        // Cập nhật trạng thái tất cả sản phẩm trong đơn hàng
        $update_sql = "UPDATE detail_sales_invoice SET Order_status = 'Đã duyệt' WHERE SalesID = ? AND Order_status = 'Chưa duyệt'";
        $stmt = $conn->prepare($update_sql);
        $stmt->bind_param("s", $sales_id);
        $stmt->execute();

        $affected_rows = $stmt->affected_rows;
        writeDebugLog("Kết quả duyệt tất cả - Số sản phẩm đã duyệt: $affected_rows");

        if ($affected_rows > 0) {
            $conn->commit();
            echo "<script>alert('Đã duyệt thành công $affected_rows sản phẩm trong đơn hàng #$sales_id!');</script>";
        } else {
            $conn->rollback();
            echo "<script>alert('Không có sản phẩm nào được duyệt trong đơn hàng #$sales_id. Có thể sản phẩm đã được duyệt trước đó.');</script>";
        }
    } catch (Exception $e) {
        $conn->rollback();
        writeDebugLog("Lỗi khi duyệt đơn hàng: " . $e->getMessage());
        echo "<script>alert('Có lỗi xảy ra: " . $e->getMessage() . "');</script>";
    }

    // Reload lại trang với tham số phân trang và tìm kiếm
    echo "<script>window.location.href = 'index.php?page=order_management&page_num=$current_page$start_date_param$end_date_param';</script>";
}

// Xử lý khi hủy tất cả sản phẩm trong đơn hàng
if (isset($_POST['cancel_all_order'])) {
    $sales_id = $_POST['sales_id'];
    $current_page = isset($_POST['current_page']) ? $_POST['current_page'] : 1;
    $start_date_param = isset($_POST['start_date']) ? "&start_date=" . $_POST['start_date'] : "";
    $end_date_param = isset($_POST['end_date']) ? "&end_date=" . $_POST['end_date'] : "";

    writeDebugLog("Bắt đầu hủy tất cả sản phẩm - SalesID: $sales_id");

    $conn->begin_transaction();

    try {
        // Lấy danh sách các sản phẩm cần hủy để cập nhật lại số lượng
        $products_query = "SELECT ProductID, Quantity FROM detail_sales_invoice WHERE SalesID = ? AND Order_status = 'Chưa duyệt'";
        $stmt_products = $conn->prepare($products_query);
        $stmt_products->bind_param("s", $sales_id);
        $stmt_products->execute();
        $products_result = $stmt_products->get_result();

        $updated_products = 0;

        // Cập nhật số lượng cho từng sản phẩm
        while ($product = $products_result->fetch_assoc()) {
            $product_id = $product['ProductID'];
            $quantity = $product['Quantity'];

            $update_product_sql = "UPDATE Product SET Quantity = Quantity + ? WHERE ProductID = ?";
            $stmt_update = $conn->prepare($update_product_sql);
            $stmt_update->bind_param("ii", $quantity, $product_id);
            $stmt_update->execute();

            $updated_products += $stmt_update->affected_rows;
            writeDebugLog("Cập nhật số lượng cho sản phẩm ID: $product_id, Số lượng: $quantity");

            $stmt_update->close();
        }

        // Cập nhật trạng thái tất cả sản phẩm trong đơn hàng
        $update_status_sql = "UPDATE detail_sales_invoice SET Order_status = 'Đã hủy' WHERE SalesID = ? AND Order_status = 'Chưa duyệt'";
        $stmt_status = $conn->prepare($update_status_sql);
        $stmt_status->bind_param("s", $sales_id);
        $stmt_status->execute();

        $affected_rows = $stmt_status->affected_rows;
        writeDebugLog("Kết quả hủy tất cả - Số sản phẩm đã hủy: $affected_rows, Sản phẩm đã cập nhật số lượng: $updated_products");

        if ($affected_rows > 0) {
            $conn->commit();
            echo "<script>alert('Đã hủy thành công $affected_rows sản phẩm trong đơn hàng #$sales_id và cập nhật số lượng cho $updated_products sản phẩm!');</script>";
        } else {
            $conn->rollback();
            echo "<script>alert('Không có sản phẩm nào được hủy trong đơn hàng #$sales_id. Có thể sản phẩm đã được xử lý trước đó.');</script>";
        }
    } catch (Exception $e) {
        $conn->rollback();
        writeDebugLog("Lỗi khi hủy đơn hàng: " . $e->getMessage());
        echo "<script>alert('Có lỗi xảy ra: " . $e->getMessage() . "');</script>";
    }

    // Reload lại trang với tham số phân trang và tìm kiếm
    echo "<script>window.location.href = 'index.php?page=order_management&page_num=$current_page$start_date_param$end_date_param';</script>";
}

$conn->close();
?>