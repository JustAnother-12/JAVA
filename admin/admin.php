<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Admin</title>
    <link rel="stylesheet" href="assets/css/main.css">
</head>

<body>
    <?php
    include_once("gui/header.php");
    $page = isset($_GET['page']) ? $_GET['page'] : "admin.php";
    if($page == 'add'){
        include_once("gui/add.php");
    }elseif ($page == 'edit') {
        include_once("gui/edit.php");
    }elseif($page == 'delete'){
        include_once("gui/delete.php");
    }elseif($page == 'permission'){
        include_once("gui/permission.php");
    }else{
        include_once("gui/content.php");
    }
    ?>
</body>

</html>