<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nhân viên bán hàng</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/main.css">
</head>

<body>
    <?php
        include_once("gui/header.php");
        include_once("gui/navbar.php");
        $page = isset($_GET['page']) ? $_GET['page'] : "home";
        if($page =='order_management'){
            include_once("gui/order_management.php");
        }elseif ($page == 'print_invoice') {
            include_once("gui/print_invoice.php");
        }elseif($page == 'view_invoice'){
            include_once("gui/view_invoice.php");
        }elseif($page == 'view_statistics'){
            include_once("gui/report.php");
        }
    ?>

</body>

</html>