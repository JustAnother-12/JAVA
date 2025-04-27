<?php
// Kết nối database
$servername = "localhost:3306";
$username = "root";
$password = "";
$dbname = "htttgame";

$conn = new mysqli($servername, $username, $password, $dbname);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// Xử lý yêu cầu xóa (thay đổi Status thành 0)
if(isset($_GET['delete_id']) && !empty($_GET['delete_id'])) {
    $id = $_GET['delete_id'];
    
    // Cập nhật Status thành 0 thay vì xóa hoàn toàn
    // Sửa từ "i" thành "s" để xử lý ProductID dạng chuỗi (GAME001)
    $sql = "UPDATE product SET Status = 0 WHERE ProductID = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $id);
    
    if($stmt->execute()) {
        echo "<script>alert('Xóa sản phẩm thành công!'); window.location.href='admin.php?page=delete';</script>";
    } else {
        echo "<script>alert('Có lỗi xảy ra: " . $stmt->error . "');</script>";
    }
    
    $stmt->close();
}

// Truy vấn lấy danh sách sản phẩm có Status = 1
$sql = "SELECT ProductID, ProductName, ProductImg, Author, Publisher, Quantity, Price, Description, SupplierID, Status 
        FROM product 
        WHERE Status = 1";
$result = $conn->query($sql);
?>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Sản phẩm</title>
    <link rel="stylesheet" href="../assets/css/delete.css">
</head>
<body>
    <h1>Danh sách Sản phẩm</h1>
    
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên sản phẩm</th>
                <th>Hình ảnh</th>
                <th>Tác giả</th>
                <th>Nhà xuất bản</th>
                <th>Số lượng</th>
                <th>Giá</th>
                <th>Mô tả</th>
                <th>ID Nhà cung cấp</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <?php
            if ($result->num_rows > 0) {
                while($row = $result->fetch_assoc()) {
                    echo "<tr>";
                    echo "<td>" . $row["ProductID"] . "</td>";
                    echo "<td>" . $row["ProductName"] . "</td>";
                    echo "<td><img src='" . $row["ProductImg"] . "' class='product-img' alt='Product Image'></td>";
                    echo "<td>" . $row["Author"] . "</td>";
                    echo "<td>" . $row["Publisher"] . "</td>";
                    echo "<td>" . $row["Quantity"] . "</td>";
                    echo "<td>" . number_format($row["Price"]) . " VND</td>";
                    echo "<td>" . substr($row["Description"], 0, 50) . (strlen($row["Description"]) > 50 ? "..." : "") . "</td>";
                    echo "<td>" . $row["SupplierID"] . "</td>";
                    echo "<td>
                            <button class='delete-btn' onclick='confirmDelete(\"" . $row["ProductID"] . "\")'>Xóa</button>
                          </td>";
                    echo "</tr>";
                }
            } else {
                echo "<tr><td colspan='10'>Không có sản phẩm nào.</td></tr>";
            }
            ?>
        </tbody>
    </table>

    <script>
        function confirmDelete(id) {
            if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này không?")) {
                // Sử dụng encodeURIComponent để xử lý đúng ID chứa kí tự đặc biệt
                window.location.href = "admin.php?page=delete&delete_id=" + encodeURIComponent(id);
            }
        }
    </script>
</body>
</html>

<?php
// Đóng kết nối
$conn->close();
?>