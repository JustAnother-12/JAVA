<?php
session_start();
require_once "../database/connectDB.php";
$conn = connectDB::getConnection();
include('header_footer/header.php');

// Kiểm tra đăng nhập
if (!isset($_SESSION['CustomerID'])) {
  echo "<p class='text-center margin-top-24'>Vui lòng <a href='/gui/account/login.php'><span style='color: red;'>đăng nhập</span></a> để xem lịch sử mua hàng.</p>";


    exit;
}

$customerID = $_SESSION['CustomerID'];

// Lấy danh sách đơn hàng của người dùng
$sql = "SELECT SalesID, Date, Status FROM sales_invoice WHERE CustomerID = ? ORDER BY Date DESC";
$stmt = $conn->prepare($sql);
$stmt->bind_param("s", $customerID);
$stmt->execute();
$result = $stmt->get_result();

// Kiểm tra trạng thái tất cả sản phẩm trong mỗi đơn hàng và cập nhật trạng thái đơn hàng nếu tất cả sản phẩm đều đã hủy
while ($row = $result->fetch_assoc()) {
    $salesID = $row['SalesID'];
    $status = $row['Status'];

    // Kiểm tra trạng thái của tất cả sản phẩm trong đơn hàng
    $checkStatusSql = "SELECT COUNT(*) as total, SUM(Order_status = 'Đã hủy') as canceled 
                       FROM detail_sales_invoice 
                       WHERE SalesID = ?";
    $checkStmt = $conn->prepare($checkStatusSql);
    $checkStmt->bind_param("s", $salesID);
    $checkStmt->execute();
    $checkResult = $checkStmt->get_result();
    $checkRow = $checkResult->fetch_assoc();

    // Nếu tất cả sản phẩm trong đơn hàng đã hủy, cập nhật trạng thái đơn hàng thành "Đã hủy"
    if ($checkRow['total'] == $checkRow['canceled'] && $status != 'Đã hủy') {
        $updateStatusSql = "UPDATE sales_invoice SET Status = 'Đã hủy' WHERE SalesID = ?";
        $updateStmt = $conn->prepare($updateStatusSql);
        $updateStmt->bind_param("s", $salesID);
        $updateStmt->execute();
    }
}

?>

<!-- history -->
<div class="history-tracking-container margin-y-24 grid-col col-l-12 col-m-12 col-s-12">
  <section id="history-order-container" class="active">
    <div class="order-status-header padding-bottom-8 margin-bottom-16">
      <p class="uppercase font-bold text-center font-size-20">lịch sử mua hàng</p>
    </div>

    <div class="history-order-table grid-col col-l-12 col-m-12 col-s-12 no-gutter flex-column gap-16">
      <?php
      // Lấy lại danh sách đơn hàng sau khi cập nhật trạng thái
      $stmt->execute();
      $result = $stmt->get_result();

      if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
          $salesID = $row['SalesID'];
          $date = date("F d, Y", strtotime($row['Date']));
          $status = $row['Status'];

          echo "
          <div class='order-card'>
            <div class='order-header'>
              <strong>Đơn #$salesID</strong>
              <span>$date</span>
            </div>
            <p>Trạng thái: $status</p>
            <a href='history_detail.php?SalesID=$salesID' class='detail-btn'>Xem chi tiết</a>
          </div>
          ";
        }
      } else {
        echo "<p class='uppercase font-bold text-center font-size-16'>Bạn chưa có đơn hàng nào.</p>";
      }
      ?>
    </div>
  </section>
</div>

<?php include('header_footer/footer.php') ?>
