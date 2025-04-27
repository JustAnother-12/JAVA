<?php
include_once "../../database/connectDB.php";
$conn = connectDB::getConnection();

header("Content-Type: application/json");
session_start();

// Nhận dữ liệu từ frontend
$data = json_decode(file_get_contents("php://input"), true);

// Kiểm tra dữ liệu đầu vào
if (!$data || !isset($data['items']) || empty($data['items'])) {
    echo json_encode(["success" => false, "message" => "Dữ liệu không hợp lệ hoặc danh sách sản phẩm rỗng"]);
    exit;
}

// Kiểm tra đăng nhập
$customerID = $_SESSION['CustomerID'] ?? null;
if (!$customerID) {
    echo json_encode(["success" => false, "message" => "Bạn cần đăng nhập để đặt hàng"]);
    exit;
}

$items = $data['items'];
$paymentMethod = $data['paymentMethod'] ?? 'COD';
$address = trim($data['address'] ?? '');
$note = trim($data['note'] ?? '');
$date = date('Y-m-d H:i:s');

// Lấy giảm giá từ voucher và phí ship
$shippingFee = isset($data['shippingFee']) ? (int)$data['shippingFee'] : 0;
$shippingDiscount = isset($data['shippingDiscount']) ? (int)$data['shippingDiscount'] : 0;

// Kiểm tra địa chỉ
if (empty($address)) {
    echo json_encode(["success" => false, "message" => "Vui lòng nhập địa chỉ giao hàng"]);
    exit;
}

// Tính tổng tiền sản phẩm
$productTotal = 0;
$productPrices = [];

foreach ($items as $item) {
    $productID = $item['productID'] ?? '';
    $quantity = $item['quantity'] ?? 0;

    if (empty($productID) || !is_numeric($quantity) || $quantity <= 0) {
        echo json_encode(["success" => false, "message" => "Thông tin sản phẩm không hợp lệ"]);
        exit;
    }

    // Lấy giá sản phẩm nếu chưa có
    if (!isset($productPrices[$productID])) {
        $stmt = $conn->prepare("SELECT Price FROM product WHERE ProductID = ?");
        $stmt->bind_param("s", $productID);
        $stmt->execute();
        $result = $stmt->get_result()->fetch_assoc();

        if (!$result) {
            echo json_encode(["success" => false, "message" => "Sản phẩm không tồn tại: $productID"]);
            exit;
        }

        $productPrices[$productID] = $result['Price'];
    }

    $productTotal += $productPrices[$productID] * $quantity;
}

// Trừ voucher và phí ship (có thể = 0)
$finalTotal = max(0, $productTotal + $shippingFee - $shippingDiscount);

// Bắt đầu giao dịch
$conn->begin_transaction();

try {
    // Thêm vào bảng hóa đơn (SalesID sẽ được tự động tăng)
    $stmt = $conn->prepare("INSERT INTO sales_invoice (CustomerID, PaymentMethod, ShippingAddress, TotalPrice, Note, Date) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("ssssss", $customerID, $paymentMethod, $address, $finalTotal, $note, $date);

    if (!$stmt->execute()) {
        throw new Exception("Không thể tạo hóa đơn");
    }

    // Lấy SalesID vừa tạo
    $salesID = $conn->insert_id;

    // Thêm chi tiết hóa đơn
    foreach ($items as $item) {
        $productID = $item['productID'];
        $quantity = $item['quantity'];
        $price = $productPrices[$productID];
        $totalItem = $price * $quantity;
        $orderStatus = "Chưa duyệt";

        $stmt = $conn->prepare("INSERT INTO detail_sales_invoice (SalesID, ProductID, Order_status, Quantity, Price, TotalPrice) VALUES (?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("issidd", $salesID, $productID, $orderStatus, $quantity, $price, $totalItem);

        if (!$stmt->execute()) {
            throw new Exception("Không thể thêm chi tiết đơn hàng cho sản phẩm $productID");
        }

        // Xóa sản phẩm khỏi giỏ hàng
        $stmt = $conn->prepare("DELETE FROM cart_item WHERE CartID = (SELECT CartID FROM cart WHERE CustomerID = ?) AND ProductID = ?");
        $stmt->bind_param("ss", $customerID, $productID);
        $stmt->execute();
    }

    // Commit nếu thành công
    $conn->commit();

    // Gán vào session và trả về đường dẫn chuyển hướng
    $_SESSION['last_order_id'] = $salesID;
    echo json_encode([
        "success" => true,
        "message" => "Đặt hàng thành công",
        "redirect" => "order_success.php"
    ]);
    exit;
} catch (Exception $e) {
    $conn->rollback();
    echo json_encode(["success" => false, "message" => "Lỗi: " . $e->getMessage()]);
}
?>
