<?php
include_once __DIR__ . '/../functions/db.php';

if (!isset($_GET['id'])) {
    echo "Không có mã phiếu nhập.";
    exit;
}

$importID = $_GET['id'];

$query = mysqli_query($conn, "SELECT d.*, p.ProductName 
FROM detail_import_invoice d 
JOIN product p ON d.ProductID = p.ProductID 
WHERE d.ImportID = '$importID'"
);

if (mysqli_num_rows($query) === 0) {
    echo "Không tìm thấy chi tiết.";
    exit;
}

echo "<h3>Chi tiết phiếu nhập: $importID</h3>";
echo "<table border='1' cellspacing='0' cellpadding='5' style='width:100%;'>";
echo "<tr><th>Mã chi tiết</th><th>Sản phẩm</th><th>Số lượng</th><th>Đơn giá</th><th>Thành tiền</th></tr>";

while ($row = mysqli_fetch_assoc($query)) {
    echo "<tr>
        <td>{$row['DetailImportID']}</td>
        <td>{$row['ProductName']}</td>
        <td>{$row['Quantity']}</td>
        <td>" . number_format($row['Price'], 0, ',', '.') . " VND</td>
        <td>" . number_format($row['TotalPrice'], 0, ',', '.') . " VND</td>
    </tr>";
}
echo "</table>";
?>


