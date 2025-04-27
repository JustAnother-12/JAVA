<?php
session_start();
require_once "../../database/connectDB.php";
$conn = connectDB::getConnection();

header('Content-Type: application/json');

$productID = $_POST['productID'];
$response = [
    'status' => 'success',
    'message' => '',
    'loggedIn' => isset($_SESSION['CustomerID']),
    'alreadyExists' => false
];

if (!isset($_SESSION['CustomerID'])) {
    // Nếu chưa đăng nhập, xử lý giỏ hàng bằng session
    if (!isset($_SESSION['cart'])) {
        $_SESSION['cart'] = [];
    }

    if (isset($_SESSION['cart'][$productID])) {
        $_SESSION['cart'][$productID]['quantity'] += 1;
        $response['alreadyExists'] = true;
    } else {
        $_SESSION['cart'][$productID] = [
            'productID' => $productID,
            'quantity' => 1
        ];
    }

    $response['message'] = 'Đã thêm vào giỏ hàng (tạm thời)';
    echo json_encode($response);
    exit;
}

// Đã đăng nhập => thao tác với database
$customerID = $_SESSION['CustomerID'];

// Lấy CartID
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

// Kiểm tra sản phẩm đã có chưa
$sqlCheckItem = "SELECT * FROM cart_item WHERE CartID = ? AND ProductID = ?";
$stmtCheck = $conn->prepare($sqlCheckItem);
$stmtCheck->bind_param("ss", $cartID, $productID);
$stmtCheck->execute();
$resultItem = $stmtCheck->get_result();

if ($resultItem->num_rows > 0) {
    $sqlUpdate = "UPDATE cart_item SET Quantity = Quantity + 1 WHERE CartID = ? AND ProductID = ?";
    $stmtUpdate = $conn->prepare($sqlUpdate);
    $stmtUpdate->bind_param("ss", $cartID, $productID);
    $stmtUpdate->execute();
    $response['alreadyExists'] = true;
} else {
    $sqlInsert = "INSERT INTO cart_item (CartID, ProductID, Quantity) VALUES (?, ?, 1)";
    $stmtInsert = $conn->prepare($sqlInsert);
    $stmtInsert->bind_param("ss", $cartID, $productID);
    $stmtInsert->execute();
}

$response['message'] = 'Đã thêm vào giỏ hàng.';
echo json_encode($response);
exit;
?>
