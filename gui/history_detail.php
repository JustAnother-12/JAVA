<?php
// Bắt đầu output buffering
ob_start();

// Các phần còn lại của mã PHP
session_start();
include('header_footer/header.php');
require_once "../database/connectDB.php";
$conn = connectDB::getConnection();

if (!isset($_GET['SalesID'])) {
  echo "Không có đơn hàng được chọn.";
  exit;
}

$salesID = $_GET['SalesID'];

if (isset($_POST['cancel_order'])) {
  $productID = $_POST['ProductID']; 

  $conn->begin_transaction();
  
  try {
    // Cập nhật Order_status thành 'Đã hủy' cho sản phẩm cụ thể trong đơn hàng
    $updateStatusSql = "UPDATE detail_sales_invoice SET Order_status = 'Đã hủy' WHERE SalesID = ? AND ProductID = ?";
    $stmt = $conn->prepare($updateStatusSql);
    $stmt->bind_param("ss", $salesID, $productID);
    $stmt->execute();
    $stmt->close();

    // Cam kết giao dịch
    $conn->commit();

    // Chuyển hướng lại trang chi tiết đơn hàng
    header("Location: history_detail.php?SalesID=" . $salesID);
    exit;

  } catch (Exception $e) {
    $conn->rollback();
    echo "Có lỗi xảy ra khi hủy sản phẩm: " . $e->getMessage();
  }
}

// Lấy chi tiết đơn hàng
$sql = "
SELECT 
    dsi.Quantity, 
    dsi.Price, 
    dsi.TotalPrice,
    dsi.Order_status,  -- Lấy cột Order_status
    p.ProductName, 
    p.ProductImg,
    p.ProductID  -- Thêm ProductID vào kết quả
FROM detail_sales_invoice dsi
JOIN product p ON dsi.ProductID = p.ProductID
WHERE dsi.SalesID = ?
";

$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $salesID);
$stmt->execute();
$result = $stmt->get_result();
$items = $result->fetch_all(MYSQLI_ASSOC);
$stmt->close();
?>

<!-- HTML và mã tiếp theo của bạn -->
<div class="history-tracking-container margin-y-24 grid-col col-l-12 col-m-12 col-s-12">
  <section id="history-order-container" class="active">
    <div class="order-status-header padding-bottom-8 margin-bottom-16">
      <p class="uppercase font-bold text-center font-size-20">Chi tiết đơn hàng</p>
    </div>

    <div class="history-order-table grid-col col-l-12 col-m-12 col-s-12 no-gutter flex-column gap-16">
      <?php foreach ($items as $item): ?>
        <div class="block-product" style="width: 300px;">
          <div class="cart-content">
            <div class="completed-order-info margin-bottom-8">
              <img src="<?= htmlspecialchars($item['ProductImg']) ?>" alt="<?= htmlspecialchars($item['ProductName']) ?>" style="width: 100px; height: auto;">
              <div class="full-width padding-left-12">
                <p class="capitalize padding-bottom-8"><?= htmlspecialchars($item['ProductName']) ?></p>
                <div class="block-product-price text-end">
                  <div class="new-price price"><?= number_format($item['Price'], 0, ',', '.') ?>&nbsp;₫</div>
                </div>
              </div>
            </div>
            <div class="flex justify-space-between padding-bottom-8 padding-top-8">
              <div class="total-item opacity-0-6"><?= $item['Quantity'] ?> item</div>
              <div class="price total-price font-bold text-end"><?= number_format($item['TotalPrice'], 0, ',', '.') ?>&nbsp;₫</div>
            </div>
            <div class="order-status flex justify-space-between padding-top-8 padding-bottom-8">
              <?php if ($item['Order_status'] == 'Đã hủy'): ?>
                <span class="opacity-0-8 font-size-13 waiting-color">Sản phẩm đã được hủy</span>
              <?php else: ?>
                <span class="opacity-0-8 font-size-13 waiting-color"><?= htmlspecialchars($item['Order_status']) ?></span>
              <?php endif; ?>
            </div>
            <?php if ($item['Order_status'] != 'Đã hủy'): ?>
              <div class="flex align-center justify-space-between padding-top-8">
                <span class="delivered-day flex opacity-0-8">
                  <div><?= date('H:i d/m/Y') ?></div>
                </span>
                <div class="flex">
                  <form method="POST" onsubmit="return confirmCancel()">
                    <input type="hidden" name="ProductID" value="<?= $item['ProductID'] ?>" />
                    <button type="submit" name="cancel_order" class="rmbtn button">
                      <div class="capitalize">Hủy Sản Phẩm</div>
                    </button>
                  </form>
                </div>
              </div>
            <?php endif; ?>
          </div>
        </div>
      <?php endforeach; ?>
    </div>
  </section>
</div>


<script type="text/javascript">
  function confirmCancel() {
    return confirm("Bạn có chắc chắn muốn hủy sản phẩm này?");
  }
</script>

<?php
ob_end_flush();
?>
