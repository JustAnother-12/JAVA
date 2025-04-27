<?php
$activeSection = 'home'; // mặc định là Trang chủ

// Nếu có POST (submit form), thì lấy theo form ẩn
if (isset($_POST['active_section'])) {
    $activeSection = $_POST['active_section'];
}

// Nếu không có POST mà người dùng nhấn qua URL ?section=abc (khi click menu), thì cũng lấy từ đó
if (isset($_GET['section'])) {
    $activeSection = $_GET['section'];
}
?>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý kho hàng</title>
    <link rel="stylesheet" href="Assets/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .mainBlock { display: flex; height: 100vh; }
        .sideMenu {
            width: 220px;
            background-color: #2c3e50;
            color: white;
            padding: 20px 0;
        }
        .sideMenu h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .sideList {
            list-style: none;
            padding: 0;
        }
        .sideList li {
            padding: 10px 20px;
            cursor: pointer;
        }
        .sideList li:hover {
            background-color: #34495e;
        }
        .container {
            flex: 1;
            padding: 20px;
        }
        .section {
            display: none;
        }
        .section.active {
            display: block;
        }
    </style>
</head>
<body>

<div class="mainBlock">
    <div  class="sideMenu">
        <h2>ADMIN</h2>
        <ul class="sideList">
    <li><a href="?section=home">Trang chủ</a></li>
    <li><a href="?section=products">Quản lý sản phẩm</a></li>
    <li><a href="?section=suppliers">Nhà cung cấp</a></li>
    <li><a href="?section=import">Lập phiếu nhập</a></li>
    <li><a href="?section=history">Lịch sử nhập</a></li>
    <br><br><br><br><br><br><br><br><br><br><br><br><br><br>
    <li class="logout"><a href="logout.php"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a></li>
    
</ul>
</div>
    

    <div class="container">
        <!-- Trang chủ -->
        <div id="home" class="section <?= $activeSection == 'home' ? 'active' : '' ?>">
    <h2>Chào mừng Admin!</h2>
    <p>Chọn chức năng từ menu bên trái để quản lý kho hàng.</p>
</div>

<div id="products" class="section <?= $activeSection == 'products' ? 'active' : '' ?>">
    <?php include 'pages/products.php'; ?>
</div>

<div id="suppliers" class="section <?= $activeSection == 'suppliers' ? 'active' : '' ?>">
    <?php include 'pages/suppliers.php'; ?>
</div>

<div id="import" class="section <?= $activeSection == 'import' ? 'active' : '' ?>">
    <?php include 'pages/import_form.php'; ?>
</div>

<div id="history" class="section <?= $activeSection == 'history' ? 'active' : '' ?>">
    <?php include 'pages/history.php'; ?>
</div>

</div>

<script>
    function showSection(id) {const sections = document.querySelectorAll('.section');sections.forEach(section => section.classList.remove('active'));document.getElementById(id).classList.add('active');}
</script>

</body>
</html>




