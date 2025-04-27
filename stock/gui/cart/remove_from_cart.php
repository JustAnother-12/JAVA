<?php
session_start();
require_once "../../database/connectDB.php";
$conn = connectDB::getConnection();

if ($_SERVER["REQUEST_METHOD"] === "POST" && isset($_POST["ProductID"])) {
    $productID = $_POST["ProductID"];
    $isLoggedIn = isset($_SESSION["CustomerID"]);

    if ($isLoggedIn) {
        $customerID = $_SESSION["CustomerID"];

        // Lấy CartID từ DB
        $sqlCart = "SELECT CartID FROM cart WHERE CustomerID = ?";
        $stmtCart = $conn->prepare($sqlCart);
        $stmtCart->bind_param("s", $customerID);
        $stmtCart->execute();
        $resultCart = $stmtCart->get_result();

        if ($resultCart->num_rows > 0) {
            $cartID = $resultCart->fetch_assoc()["CartID"];

            // Xóa sản phẩm khỏi cart_item
            $sqlDelete = "DELETE FROM cart_item WHERE CartID = ? AND ProductID = ?";
            $stmtDelete = $conn->prepare($sqlDelete);
            $stmtDelete->bind_param("ss", $cartID, $productID);
            $stmtDelete->execute();
        }

    } else {
        // Chưa đăng nhập -> dùng session
        if (isset($_SESSION["cart"][$productID])) {
            unset($_SESSION["cart"][$productID]);
        }
    }
}

// Sau khi xóa xong, chuyển hướng về lại giỏ hàng
header("Location: ../cart.php");
exit;
?>
