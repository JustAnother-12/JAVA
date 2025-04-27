

<?php include("../header_footer/header.php"); ?>
     <?php
     include("../../database/connectDB.php"); // Kết nối CSDL
     $conn = connectDB::getConnection();
     $success_message = "";
     $error_message = "";

     if ($_SERVER["REQUEST_METHOD"] == "POST") {
     $first_name = trim($_POST['customer-first-name']);
     $last_name = trim($_POST['customer-last-name']);
     $email = trim($_POST['customer-email-register']);
     $password = trim($_POST['customer-password-register']);
     $confirm_password = trim($_POST['customer-confirm-password-register']);  
     $fullname = $last_name . " " . $first_name;
     $username = $fullname;

     // Kiểm tra xác nhận mật khẩu
     if ($password !== $confirm_password) {
          $error_message = "Mật khẩu xác nhận không khớp!";
     } else {
          // Kiểm tra email đã tồn tại chưa
          $check_email = "SELECT * FROM customer WHERE Email = ?";
          $stmt = $conn->prepare($check_email);
          $stmt->bind_param("s", $email);
          $stmt->execute();
          $result = $stmt->get_result();

          if ($result->num_rows > 0) {
               $error_message = "Email đã được đăng ký!";
          } else {
               // Tạo tài khoản mới
               $hash_pass = password_hash($password, PASSWORD_DEFAULT);
               $role = "R4"; // Người mua hàng
               $query_max_id = "SELECT MAX(CustomerID) AS max_id FROM customer WHERE CustomerID LIKE 'MT3H%'";
               $result_max = mysqli_query($conn, $query_max_id);
               $row = mysqli_fetch_assoc($result_max);
               if (!$row['max_id']) {
                    $customer_id = "MT3H00001";
               } else {
                    $current_num = intval(substr($row['max_id'], 4));
                    $next_num = $current_num + 1;
                    $customer_id = "MT3H" . str_pad($next_num, 5, "0", STR_PAD_LEFT);
               }

               // Insert vào account
               $insert_account = "INSERT INTO account (Username, Password, RoleID) VALUES (?, ?, ?)";
               $stmt1 = $conn->prepare($insert_account);
               $stmt1->bind_param("sss", $username, $hash_pass, $role);

               // Insert vào customer
               $insert_customer = "INSERT INTO customer (CustomerID, Fullname, Username, Email) VALUES (?, ?, ?, ?)";
               $stmt2 = $conn->prepare($insert_customer);
               $stmt2->bind_param("ssss", $customer_id, $fullname, $username, $email);

               if ($stmt1->execute() && $stmt2->execute()) {
                    $success_message = "Đăng ký thành công! Bạn có thể đăng nhập.";
               } else {
                    $error_message = "Lỗi khi tạo tài khoản!";
               }
          }
     }
     }
     ?>
     <div id="account-content" class="grid-col col-l-12 col-m-12 col-s-12 no-gutter">
          <section id="login-registration-form" class="js-account-form">
               <div class="user-box">
                    <div id="register">
                         <div class="font-size-20 uppercase font-bold text-center">đăng ký</div>
                         <div id="register-layout">
                              
                              <form action="" method="post">
                                   <div class="js-fname-register">
                                        <input type="text" name="customer-last-name" id="customer-last-name" placeholder="Họ" required>
                                        <div class="error-message"></div>
                                   </div>
                                   <div class="js-lname-register">
                                        <input type="text" name="customer-first-name" id="customer-first-name" placeholder="Tên" required>
                                        <div class="error-message"></div>
                                   </div>
                                   <div class="js-email-register">
                                        <input type="email" name="customer-email-register" id="customer-email-register" placeholder="Email" autocomplete="on" required>
                                        <div class="error-message"></div>
                                   </div>
                                   <div class="js-password-register">
                                        <input type="password" name="customer-password-register" id="customer-password-register" autocomplete="off" placeholder="Tạo mật khẩu" minlength="8" required>
                                        <div class="error-message"></div>
                                   </div>
                                   <div class="js-confirm-pass-register">
                                        <input type="password" name="customer-confirm-password-register" id="customer-confirm-password-register" autocomplete="off" placeholder="Xác nhận mật khẩu" minlength="8" required>
                                        <div class="error-message"></div>
                                   </div>
                                   <button type="submit" id="register-btn" class="button">
                                                         <p class="capitalize font-bold">đăng ký</p>
                                   </button>
                              </form>

                              <div class="font-size-14 margin-y-12 js-login text-center">
                                   <span>Đã có tài khoản ?</span>
                                   <a href="login.php">đăng nhập</a>
                              </div>

                              <?php if (!empty($success_message)) : ?>
                                   <div class="success-message" style="color: green; text-align: center;">
                                        <?php echo $success_message; ?>
                                   </div>
                              <?php endif; ?>

                              <?php if (!empty($error_message)) : ?>
                                   <div class="error-message" style="color: red; text-align: center;">
                                        <?php echo $error_message; ?>
                                   </div>
                              <?php endif; ?>
                         </div>
                    </div>
               </div>
          </section>
     </div>

<?php include("../header_footer/footer.php"); ?>
