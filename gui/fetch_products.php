<?php
include '../database/connectDB.php';
$conn = connectDB::getConnection();

// Cấu hình
$productsPerPage = 20;

// Lấy các tham số từ URL
$page = isset($_GET['page']) ? (int)$_GET['page'] : 1;
$offset = ($page - 1) * $productsPerPage;

$keyword = isset($_GET['query']) ? trim($_GET['query']) : "";
$type = isset($_GET['type']) ? trim($_GET['type']) : "";
$minPrice = isset($_GET['minPrice']) ? (int)$_GET['minPrice'] : 0;
$maxPrice = isset($_GET['maxPrice']) ? (int)$_GET['maxPrice'] : PHP_INT_MAX;
$isFeatured = isset($_GET['featured']) && $_GET['featured'] === 'true';

// Truy vấn sản phẩm
$sql = "SELECT p.ProductID, p.ProductName, p.ProductImg, p.Price, p.Quantity 
        FROM product p 
        LEFT JOIN type_product t ON p.ProductID = t.ProductID 
        WHERE 1";

$params = [];
$types = "";

// Tìm kiếm theo từ khóa
if (!empty($keyword)) {
    $sql .= " AND p.ProductName LIKE ?";
    $params[] = "%" . $keyword . "%";
    $types .= "s";
}

// Lọc theo thể loại
if (!empty($type)) {
    $sql .= " AND t.TypeID = ?";
    $params[] = $type;
    $types .= "s";
}

// Lọc theo giá tiền
$sql .= " AND p.Price BETWEEN ? AND ?";
$params[] = $minPrice;
$params[] = $maxPrice;
$types .= "ii";

// Trường hợp FEATURED: không phân trang, giới hạn 15 kết quả theo Quantity
if ($isFeatured) {
    $sql .= " ORDER BY p.Quantity DESC LIMIT 15";
    $totalPages = 1;
} else {
    // Đếm tổng số sản phẩm
    $countSql = "SELECT COUNT(*) as total FROM ($sql) as subquery";
    $stmt = mysqli_prepare($conn, $countSql);
    if (!empty($params)) {
        mysqli_stmt_bind_param($stmt, $types, ...$params);
    }
    mysqli_stmt_execute($stmt);
    $countResult = mysqli_stmt_get_result($stmt);
    $totalProducts = mysqli_fetch_assoc($countResult)['total'];
    $totalPages = ceil($totalProducts / $productsPerPage);

    // Thêm phân trang
    $sql .= " LIMIT ? OFFSET ?";
    $params[] = $productsPerPage;
    $params[] = $offset;
    $types .= "ii";
}

// Thực thi truy vấn chính
$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, $types, ...$params);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);

// Lấy dữ liệu
$products = [];
while ($row = mysqli_fetch_assoc($result)) {
    $products[] = $row;
}

connectDB::closeConnection($conn);

// Trả về JSON
echo json_encode(["products" => $products, "totalPages" => $totalPages]);
?>
