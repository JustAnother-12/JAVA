<?php

session_start(); // Khởi tạo session
echo '  <div class="header-container">
            <div class="header-text">
                Nhân viên bán hàng <span class="text-warning">' . htmlspecialchars($_SESSION["username"]) . '</span>!
            </div>
            <a href="../../index.php" class="logout-btn">Thoát</a>
       </div>';
?>
<style>
    .header-container {
        display: flex;
        justify-content: space-between; /* Căn đều hai bên */
        align-items: center; /* Căn giữa theo chiều dọc */
        padding: 10px 20px;
        background-color: #0056b3; /* Màu nền */

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
        color: #ffc107; /* Màu vàng cho tên nhân viên */
    }
</style>