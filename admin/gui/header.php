<?php
session_start(); // Khởi tạo session

// Kiểm tra xem session 'username' đã được khởi tạo chưa
if (isset($_SESSION['username'])) {
    echo '<div class="header">
            <span>Xin chào, admin <span class="text-warning">' . htmlspecialchars($_SESSION['username']) . '</span></span>
            <a href="../../index.php" class="logout-btn">Chuyển sang trang mua hàng</a>
          </div>
          
          <div class="container">
            <div class="sidebar">
              <a href="admin.php?page=permission">Quyền</a>
            </div>';
} else {
    // Nếu session chưa tồn tại, chuyển hướng về trang đăng nhập
    header('Location: ../../gui/account/login.php');
    exit();
}
?>
<style>
    .header {
        display: flex;
        justify-content: space-between; /* Căn đều hai bên */
        align-items: center; /* Căn giữa theo chiều dọc */
        padding: 10px 20px;
        background-color: #4568dc; /* Màu nền */
        color: white; /* Màu chữ */
        font-size: 18px;
        font-weight: bold;
    }

    .logout-btn {
        background-color: #ff6b6b;
        color: white;
        border: none;
        padding: 8px 12px;
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
        text-decoration: none; /* Xóa gạch chân */
        transition: background-color 0.3s ease;
    }

    .logout-btn:hover {
        background-color: #ff4444;
    }

    .text-warning {
        color: #ffc107; /* Màu vàng cho tên admin */
    }
</style>