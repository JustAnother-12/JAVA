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
function writeDebugLog($message) {
    $logFile = "debug_orders.log";
    $timestamp = date('Y-m-d H:i:s');
    file_put_contents($logFile, "[$timestamp] $message" . PHP_EOL, FILE_APPEND);
}

// Lấy danh sách đơn hàng, gom nhóm theo SalesID
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
        GROUP BY 
            sales_invoice.SalesID
        HAVING 
            Order_status = 'Chưa duyệt'
        ORDER BY 
            sales_invoice.Date DESC";

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
</head>

<body>
    <h2 class="text-center my-4">Quản lý đơn hàng</h2>
    
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

    <!-- Modal Chi tiết đơn hàng -->
    <div class="modal fade" id="orderModal" tabindex="-1" aria-labelledby="orderModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="orderModalLabel">Chi tiết đơn hàng #<span id="modal-order-id-display"></span></h5>
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
                        <button type="submit" name="approve_all_order" class="btn btn-success">Duyệt đơn hàng</button>
                        <button type="submit" name="cancel_all_order" class="btn btn-danger" style="margin-left: 15px;"
                            onclick="return confirmCancelOrder()">Hủy đơn hàng</button>
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
            button.addEventListener('click', function() {
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

        function printInvoice(salesID) {
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
    </script>
</body>

</html>

<?php
// Xử lý khi duyệt tất cả sản phẩm trong đơn hàng
if (isset($_POST['approve_all_order'])) {
    $sales_id = $_POST['sales_id'];
    
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
    
    // Reload lại trang
    echo "<script>window.location.href = 'index.php?page=order_management';</script>";
}

// Xử lý khi hủy tất cả sản phẩm trong đơn hàng
if (isset($_POST['cancel_all_order'])) {
    $sales_id = $_POST['sales_id'];
    
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
    
    // Reload lại trang
    echo "<script>window.location.href = 'index.php?page=order_management';</script>";
}

$conn->close();
?>