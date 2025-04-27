<?php
// get_order_details.php
$servername = "localhost:3306";
$username = "root";
$password = "";
$database = "htttgame";

$conn = new mysqli($servername, $username, $password, $database);
if ($conn->connect_error) {
    die(json_encode(['error' => "Kết nối thất bại: " . $conn->connect_error]));
}

// Viết log ra file
function writeDebugLog($message) {
    $logFile = "debug_orders.log";
    $timestamp = date('Y-m-d H:i:s');
    file_put_contents($logFile, "[$timestamp] $message" . PHP_EOL, FILE_APPEND);
}

// Đảm bảo có SalesID được gửi đến
if (!isset($_GET['sales_id'])) {
    echo json_encode(['error' => 'Thiếu mã đơn hàng']);
    exit;
}

$sales_id = $_GET['sales_id'];

// Truy vấn chi tiết sản phẩm trong đơn hàng
$sql = "SELECT 
            p.ProductID,
            p.ProductName,
            p.ProductImg,
            d.Quantity,
            d.TotalPrice / d.Quantity as UnitPrice,
            d.TotalPrice,
            d.Order_status
        FROM 
            detail_sales_invoice d
        INNER JOIN 
            Product p ON d.ProductID = p.ProductID
        WHERE 
            d.SalesID = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $sales_id);
$stmt->execute();
$result = $stmt->get_result();

$products = [];

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        // Định dạng giá tiền
        $row['UnitPrice'] = number_format($row['UnitPrice'], 0, ',', '.') . " VND";
        $row['TotalPrice'] = number_format($row['TotalPrice'], 0, ',', '.') . " VND";
        
        // Thêm vào mảng kết quả
        $products[] = $row;
    }
}

// Trả về dữ liệu dạng JSON
header('Content-Type: application/json');
echo json_encode($products);

$stmt->close();
$conn->close();
?>