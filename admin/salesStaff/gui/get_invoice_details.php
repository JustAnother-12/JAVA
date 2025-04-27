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

// Kiểm tra nếu có salesId được gửi
if(isset($_GET['salesId'])) {
    $salesId = $_GET['salesId'];
    
    // Chuẩn bị câu truy vấn với prepared statement để tránh SQL injection
    $sql = "SELECT 
                Product.ProductName,
                Product.Author,
                Product.Price,
                detail_sales_invoice.Quantity,
                detail_sales_invoice.TotalPrice
            FROM 
                detail_sales_invoice
            INNER JOIN 
                Product ON detail_sales_invoice.ProductID = Product.ProductID
            WHERE 
                detail_sales_invoice.SalesID = ? AND
                detail_sales_invoice.Order_status = 'Đã duyệt'";
    
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $salesId);
    $stmt->execute();
    $result = $stmt->get_result();
    
    $details = [];
    while($row = $result->fetch_assoc()) {
        $details[] = $row;
    }
    
    // Trả về kết quả dưới dạng JSON
    header('Content-Type: application/json');
    echo json_encode($details);
} else {
    // Nếu không có salesId, trả về lỗi
    header('HTTP/1.1 400 Bad Request');
    echo json_encode(['error' => 'Missing salesId parameter']);
}

$conn->close();
?>