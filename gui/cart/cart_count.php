<?php
session_start();
require_once "../../database/connectDB.php";
$conn = connectDB::getConnection();

header('Content-Type: application/json');

$count = 0;

if (!isset($_SESSION['CustomerID'])) {
    if (isset($_SESSION['cart'])) {
        $count = count($_SESSION['cart']);
    }
} else {
    $customerID = $_SESSION['CustomerID'];
    $sql = "SELECT COUNT(*) AS productCount
            FROM cart_item ci
            JOIN cart c ON ci.CartID = c.CartID
            WHERE c.CustomerID = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $customerID);
    $stmt->execute();
    $result = $stmt->get_result();
    if ($row = $result->fetch_assoc()) {
        $count = $row['productCount'];
    }
}

echo json_encode(['count' => $count]);
?>
