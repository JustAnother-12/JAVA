<?php
session_start();
include('header_footer/header.php');
include('../database/connectDB.php');

// Lấy mã đơn hàng từ session
$order_id = $_SESSION['last_order_id'] ?? null;
$orderData = null;

if ($order_id) {
    $conn = connectDB::getConnection();

    // Truy vấn thông tin đơn hàng dựa vào SalesID
    $stmt = $conn->prepare("SELECT SalesID, Date, TotalPrice FROM sales_invoice WHERE SalesID = ?");
    $stmt->bind_param("i", $order_id); // SalesID là chuỗi, không phải số nguyên
    $stmt->execute();
    $result = $stmt->get_result();
    $orderData = $result->fetch_assoc();

    $stmt->close();
    connectDB::closeConnection($conn);
}
?>

<div class="order-success-wrapper">
    <div class="order-success-container">
        <h2>Xác Nhận Đơn Hàng</h2>
        <div class="order-summaryD">
            <h3>Tóm tắt đơn hàng</h3>
            <?php if ($orderData): ?>
                <p>Mã đơn hàng: <span>#<?= htmlspecialchars($orderData['SalesID']) ?></span></p>
                <p>Ngày đặt hàng: <span><?= date("d/m/Y - H:i", strtotime($orderData['Date'])) ?></span></p>
                <p>Tổng thanh toán: <span><?= number_format($orderData['TotalPrice'], 0, ',', '.') ?>đ</span></p>
            <?php else: ?>
                <p>Không tìm thấy thông tin đơn hàng.</p>
            <?php endif; ?>
        </div>
        <button class="order-button" onclick="window.location.href='/index.php'">Tiếp tục mua sắm</button>
    </div>
</div>

<?php include('header_footer/footer.php') ?>
