<?php
session_start();
require_once "../../database/connectDB.php";
$conn = connectDB::getConnection();
header('Content-Type: application/json');
$productID = $_POST['productID'];
$quantity = isset($_POST['quantity']) ? max(1, intval($_POST['quantity'])) : 1;
$response = [
    'status' => 'success',
    'message' => '',
    'loggedIn' => isset($_SESSION['CustomerID']),
    'alreadyExists' => false
];

if (!isset($_SESSION['CustomerID'])) {
    if (!isset($_SESSION['cart'])) {
        $_SESSION['cart'] = [];
    }
    if (isset($_SESSION['cart'][$productID])) {
        $_SESSION['cart'][$productID]['quantity'] += $quantity;
        $response['alreadyExists'] = true;
    } else {
        $_SESSION['cart'][$productID] = [
            'productID' => $productID,
            'quantity' => $quantity
        ];
    }    
    
    echo json_encode($response);
    exit;
}

$customerID = $_SESSION['CustomerID'];

$sqlCart = "SELECT CartID FROM cart WHERE CustomerID = ?";
$stmtCart = $conn->prepare($sqlCart);
$stmtCart->bind_param("s", $customerID);
$stmtCart->execute();
$result = $stmtCart->get_result();

if ($result->num_rows > 0) {
    $cartID = $result->fetch_assoc()['CartID'];
} else {
    $cartID = uniqid("CART");
    $sqlCreateCart = "INSERT INTO cart (CartID, CustomerID) VALUES (?, ?)";
    $stmtCreate = $conn->prepare($sqlCreateCart);
    $stmtCreate->bind_param("ss", $cartID, $customerID);
    $stmtCreate->execute();
}

$sqlCheckItem = "SELECT * FROM cart_item WHERE CartID = ? AND ProductID = ?";
$stmtCheck = $conn->prepare($sqlCheckItem);
$stmtCheck->bind_param("ss", $cartID, $productID);
$stmtCheck->execute();
$resultItem = $stmtCheck->get_result();

if ($resultItem->num_rows > 0) {
    $sqlUpdate = "UPDATE cart_item SET Quantity = Quantity + ? WHERE CartID = ? AND ProductID = ?";
    $stmtUpdate = $conn->prepare($sqlUpdate);
    $stmtUpdate->bind_param("iss", $quantity, $cartID, $productID);
    $stmtUpdate->execute();
    $response['alreadyExists'] = true;
} else {
    $sqlInsert = "INSERT INTO cart_item (CartID, ProductID, Quantity) VALUES (?, ?, ?)";
    $stmtInsert = $conn->prepare($sqlInsert);
    $stmtInsert->bind_param("ssi", $cartID, $productID, $quantity);
    $stmtInsert->execute();
}


echo json_encode($response);
exit;
?>
