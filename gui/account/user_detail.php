<?php 
session_start();
include('../../database/connectDB.php');
include('../header_footer/header.php');

// Giả sử đã lưu session username sau khi đăng nhập
$username = $_SESSION['username'] ?? '';

// Lấy kết nối từ lớp connectDB
$conn = connectDB::getConnection();

// Kiểm tra nếu kết nối thành công
if ($conn) {
    $sql = "SELECT * FROM customer WHERE Username = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();
    $customer = $result->fetch_assoc();

    // Đóng kết nối sau khi sử dụng
    connectDB::closeConnection($conn);
} else {
    die("Không thể kết nối đến cơ sở dữ liệu.");
}
?>
<div id="user-detail" class="grid-col col-l-12 col-m-12 col-s-12 " data-username="<?= htmlspecialchars($username) ?>">
                                        <div class="account-container">
                                             <div class="flex align-center margin-bottom-16">
                                                  <div class="font-size-20 uppercase font-bold">tài khoản của bạn</div>
                                                  
                                             </div>
                                             <p class="user-orders margin-bottom-16 font-size-14 font-light"></p>

                                             <div class="account-history-bill grid-col col-l-3 no-gutter">
                                             <div >
                                             <a href="/index.php" class="category-btn button margin-left-12 js-signout exit-color">Thoát</a></div>


                                             <a href="/gui/history.php">
                                                  <article class="history-order-link button">
                                                       <div class="history-order-btn font-bold capitalize text-center">xem lịch sử mua hàng</div>
                                                  </article>
                                             </a>
                                             </div>

                                             <div class="account-info padding-top-16">
                                                  <div class="font-bold uppercase margin-bottom-16 font-size-20">thông tin khách hàng</div>
                                                  <div class="user-card padding-16 grid-col col-l-6 col-m-12 col-s-12">
                                        
                                                       <div class="info full-name">
                                                            <div class="font-bold grid-col col-l-2 col-m-4 col-s-4 no-gutter">Họ Và Tên</div>
                                                            <input type="text" name="full-name" id="full-name" value="<?= htmlspecialchars($customer['Fullname'] ?? '') ?>"
                                                                 class="grid-col col-l-10 col-m-8 col-s-8" value="Kenneth Valdez" disabled>
                                                       </div>                                                     
                                                       
                                                       <div class="info user-email">
                                                            <div
                                                                 class="font-bold grid-col col-l-2 col-m-4 col-s-4 no-gutter">Email</div>
                                                            <input type="text" name="user-email" id="user-email" value="<?= htmlspecialchars($customer['Email'] ?? '') ?>"
                                                                 class="grid-col col-l-10 col-m-8 col-s-8" value="fip@jukmuh.al" disabled>
                                                       </div>
                                                       
                                                       <div class="info user-phone">
                                                            <div
                                                                 class="font-bold grid-col col-l-2 col-m-4 col-s-4 no-gutter">Điện Thoại</div>
                                                            <input type="text" name="user-phone" id="user-phone" value="<?= htmlspecialchars($customer['Phone'] ?? '') ?>" class="grid-col col-l-10 col-m-8 col-s-8" 
                                                                 placeholder="chưa có số điện thoại" value="(239) 816-9029" disabled>
                                                       </div>
                                                       <div class="info user-address">
                                                            <div class="font-bold grid-col col-l-2 col-m-4 col-s-4 no-gutter"> Địa Chỉ</div>
                                                            <input type="text" name="user-address" id="user-address" value="<?= htmlspecialchars($customer['Address'] ?? '') ?>" class="grid-col col-l-10 col-m-8 col-s-8" 
                                                                 placeholder="chưa có địa chỉ" value="Bay Area, San Francisco, CA" disabled>
                                                       </div>
                                                        <!--  <div class="info user-totalspending">
  <div class="font-bold grid-col col-l-3 col-m-4 col-s-4 no-gutter">Tổng chi tiêu</div>
     <input type="text" disabled
            value="<?= number_format($TotalSpending, 0, ',', '.') ?> đ"
            class="grid-col col-l-10 col-m-8 col-s-8">
</div>-->

                                                       <div class="full-width flex margin-top-12">
                                                            <button class="js-edit-btn button capitalize">sửa thông tin</button>
                                                            <button class="js-submit-btn button margin-left-16 capitalize">xác nhận</button>
                                                       </div>
                                                  </div>
                                                  <!-- <a href="#" class="view-address">Xem địa chỉ</a> -->
                                             </div>
                                        </div>
                                   </div>
<?php include('../header_footer/footer.php') ?>