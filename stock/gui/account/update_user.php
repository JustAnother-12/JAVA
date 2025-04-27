<?php
session_start();
include('../../database/connectDB.php');
header("Content-Type: application/json");

// Lấy dữ liệu JSON gửi từ JS
$data = json_decode(file_get_contents("php://input"), true);

// Kiểm tra dữ liệu đầu vào và session
if (!isset($_SESSION['username']) || !$data) {
    echo json_encode(["success" => false, "message" => "Thiếu dữ liệu hoặc chưa đăng nhập."]);
    exit;
}

$username = $_SESSION['username'];
$fullName = $data['fullName'] ?? '';
$email = $data['email'] ?? '';
$phone = $data['phone'] ?? '';
$address = $data['address'] ?? '';

// Kết nối CSDL
$conn = connectDB::getConnection();
if (!$conn) {
    echo json_encode(["success" => false, "message" => "Không thể kết nối CSDL."]);
    exit;
}

$sql = "UPDATE customer SET Fullname = ?, Email = ?, Phone = ?, Address = ? WHERE Username = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("sssss", $fullName, $email, $phone, $address, $username);

if ($stmt->execute()) {
    echo json_encode(["success" => true]);
} else {
    echo json_encode(["success" => false, "message" => "Lỗi khi cập nhật thông tin."]);
}

connectDB::closeConnection($conn);
?>
