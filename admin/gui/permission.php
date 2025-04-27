<?php
    // Kết nối đến cơ sở dữ liệu
    $servername = "localhost:3306";
    $username = "root";
    $password = "";
    $dbname = "htttgame";

    // Tạo kết nối
    $conn = new mysqli($servername, $username, $password, $dbname);

    // Kiểm tra kết nối
    if ($conn->connect_error) {
        die("Kết nối thất bại: " . $conn->connect_error);
    }

    // Hàm tạo CustomerID mới
    function generateCustomerID($conn)
    {
        // Truy vấn CustomerID lớn nhất hiện tại
        $sql = "SELECT CustomerID FROM customer ORDER BY CustomerID DESC LIMIT 1";
        $result = $conn->query($sql);

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $lastID = $row['CustomerID'];

            // Lấy phần số từ CustomerID cuối cùng
            $numPart = intval(substr($lastID, 4)); // Bỏ qua tiền tố MT3H
            $newNumPart = $numPart + 1;

            // Tạo CustomerID mới với định dạng MT3Hxxxxx (x là số)
            $newID = 'MT3H' . str_pad($newNumPart, 5, '0', STR_PAD_LEFT);

            return $newID;
        } else {
            // Nếu chưa có CustomerID nào, bắt đầu từ MT3H00001
            return 'MT3H00001';
        }
    }

    // Hàm kiểm tra username đã tồn tại hay chưa
    function checkUsernameExists($conn, $username) {
        $sql = "SELECT Username FROM account WHERE Username = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $result = $stmt->get_result();
        $exists = $result->num_rows > 0;
        $stmt->close();
        return $exists;
    }

    // Xử lý thêm tài khoản mới
    if (isset($_POST['add'])) {
        $username = $_POST['username'];
        
        // Kiểm tra username đã tồn tại chưa
        if (checkUsernameExists($conn, $username)) {
            echo "<script>alert('Username đã tồn tại. Vui lòng chọn Username khác!');</script>";
        } else {
            $password = password_hash($_POST['password'], PASSWORD_DEFAULT); // Mã hóa mật khẩu
            $roleID = $_POST['roleID'];
            $status = $_POST['status'];
            $lock = $_POST['lock']; // Thêm trường Lock
            $fullname = $_POST['fullname'];
            $email = $_POST['email'];
            $address = $_POST['address'];
            $phone = $_POST['phone'];

            // Tạo CustomerID mới
            $customerID = generateCustomerID($conn);

            // Sử dụng prepared statement để tránh SQL injection
            $sql1 = "INSERT INTO account (Username, Password, RoleID, Status, `Lock`) VALUES (?, ?, ?, ?, ?)";
            $stmt1 = $conn->prepare($sql1);
            $stmt1->bind_param("sssii", $username, $password, $roleID, $status, $lock);

            if ($stmt1->execute()) {
                // Thêm vào bảng customer với CustomerID mới
                $sql2 = "INSERT INTO customer (CustomerID, Fullname, Username, Email, Address, Phone, TotalSpending) 
                        VALUES (?, ?, ?, ?, ?, ?, 0)";
                $stmt2 = $conn->prepare($sql2);
                $stmt2->bind_param("ssssss", $customerID, $fullname, $username, $email, $address, $phone);

                if ($stmt2->execute()) {
                    echo "<script>alert('Thêm tài khoản thành công!');</script>";
                } else {
                    echo "<script>alert('Lỗi khi thêm thông tin khách hàng: " . $stmt2->error . "');</script>";
                }
                $stmt2->close();
            } else {
                echo "<script>alert('Lỗi khi thêm tài khoản: " . $stmt1->error . "');</script>";
            }
            $stmt1->close();
        }
    }

    // Xử lý sửa tài khoản
    if (isset($_POST['edit'])) {
        $old_username = $_POST['old_username']; // Username cũ
        $new_username = $_POST['username']; // Username mới
        $roleID = $_POST['roleID'];
        $fullname = $_POST['fullname'];
        $email = $_POST['email'];
        $address = $_POST['address'];
        $phone = $_POST['phone'];

        // Nếu username bị thay đổi, kiểm tra xem username mới đã tồn tại chưa
        if ($old_username != $new_username) {
            if (checkUsernameExists($conn, $new_username)) {
                echo "<script>alert('Username mới đã tồn tại. Vui lòng chọn Username khác!');</script>";
            } else {
                // Bắt đầu transaction để đảm bảo tính nhất quán dữ liệu
                $conn->begin_transaction();
                try {
                    // CẢI TIẾN: Thêm bản ghi mới vào account với username mới
                    $sql_get_account = "SELECT Password, RoleID, Status, `Lock` FROM account WHERE Username = ?";
                    $stmt_get = $conn->prepare($sql_get_account);
                    $stmt_get->bind_param("s", $old_username);
                    $stmt_get->execute();
                    $result_account = $stmt_get->get_result();
                    $account_data = $result_account->fetch_assoc();
                    $stmt_get->close();
                    
                    // Thêm bản ghi mới cho username mới
                    $sql_new_account = "INSERT INTO account (Username, Password, RoleID, Status, `Lock`) VALUES (?, ?, ?, ?, ?)";
                    $stmt_new = $conn->prepare($sql_new_account);
                    $stmt_new->bind_param("sssii", $new_username, $account_data['Password'], $roleID, $account_data['Status'], $account_data['Lock']);
                    $stmt_new->execute();
                    $stmt_new->close();
                    
                    // Cập nhật username trong bảng customer
                    $sql_update_customer = "UPDATE customer SET Username = ?, Fullname = ?, Email = ?, Address = ?, Phone = ? WHERE Username = ?";
                    $stmt_customer = $conn->prepare($sql_update_customer);
                    $stmt_customer->bind_param("ssssss", $new_username, $fullname, $email, $address, $phone, $old_username);
                    $stmt_customer->execute();
                    $stmt_customer->close();

                      // Xóa bản ghi cũ trong account
                      $sql_delete_old = "DELETE FROM account WHERE Username = ?";
                      $stmt_delete = $conn->prepare($sql_delete_old);
                      $stmt_delete->bind_param("s", $old_username);
                      $stmt_delete->execute();
                      $stmt_delete->close();

                    // Commit transaction nếu không có lỗi
                    $conn->commit();
                    echo "<script>alert('Cập nhật tài khoản thành công!');</script>";
                    echo "<script>window.location.href = 'admin.php?page=permission';</script>"; // Tải lại trang
                } catch (Exception $e) {
                    // Rollback nếu có lỗi
                    $conn->rollback();
                    echo "<script>alert('Lỗi khi cập nhật tài khoản: " . $e->getMessage() . "');</script>";
                }
            }
        } else {
            // Nếu username không thay đổi, chỉ cập nhật thông tin khác
            // Cập nhật bảng account
            $sql3 = "UPDATE account SET RoleID = ? WHERE Username = ?";
            $stmt3 = $conn->prepare($sql3);
            $stmt3->bind_param("ss", $roleID, $old_username);

            if ($stmt3->execute()) {
                // Cập nhật bảng customer
                $sql4 = "UPDATE customer SET Fullname = ?, Email = ?, Address = ?, Phone = ? WHERE Username = ?";
                $stmt4 = $conn->prepare($sql4);
                $stmt4->bind_param("sssss", $fullname, $email, $address, $phone, $old_username);

                if ($stmt4->execute()) {
                    echo "<script>alert('Cập nhật tài khoản thành công!');</script>";
                } else {
                    echo "<script>alert('Lỗi khi cập nhật thông tin khách hàng: " . $stmt4->error . "');</script>";
                }
                $stmt4->close();
            } else {
                echo "<script>alert('Lỗi khi cập nhật tài khoản: " . $stmt3->error . "');</script>";
            }
            $stmt3->close();
        }
    }

    // Xử lý vô hiệu hóa tài khoản (thay vì xóa)
    if (isset($_GET['delete'])) {
        $username = $_GET['delete'];

        // Cập nhật trạng thái tài khoản thành không hoạt động (0)
        $sql5 = "UPDATE account SET Status = 0 WHERE Username = ?";
        $stmt5 = $conn->prepare($sql5);
        $stmt5->bind_param("s", $username);

        if ($stmt5->execute()) {
            echo "<script>alert('Đã vô hiệu hóa tài khoản thành công!');</script>";
            echo "<script>window.location.href = 'admin.php?page=permission';</script>"; // Tải lại trang
        } else {
            echo "<script>alert('Lỗi khi vô hiệu hóa tài khoản: " . $stmt5->error . "');</script>";
        }
        $stmt5->close();
    }

    // Xử lý khóa/mở tài khoản
    if (isset($_GET['toggle_lock'])) {
        $username = $_GET['username'];
        $lock_status = $_GET['toggle_lock'];

        // Cập nhật trạng thái khóa - thêm backticks cho Lock
        $sql7 = "UPDATE account SET `Lock` = ? WHERE Username = ?";
        $stmt7 = $conn->prepare($sql7);
        $stmt7->bind_param("is", $lock_status, $username);

        if ($stmt7->execute()) {
            $message = $lock_status == 0 ? "Khóa tài khoản thành công!" : "Mở khóa tài khoản thành công!";
            echo "<script>alert('$message');</script>";
            echo "<script>window.location.href = 'admin.php?page=permission';</script>"; // Tải lại trang
        } else {
            echo "<script>alert('Lỗi khi cập nhật trạng thái khóa: " . $stmt7->error . "');</script>";
        }
        $stmt7->close();
    }

    // Lấy danh sách các vai trò từ DB
    $sql_roles = "SELECT DISTINCT RoleID FROM account ORDER BY RoleID";
    $result_roles = $conn->query($sql_roles);
    $roles = array();
    
    if ($result_roles->num_rows > 0) {
        while($row = $result_roles->fetch_assoc()) {
            $roles[] = $row["RoleID"];
        }
    }

    // Xử lý tìm kiếm và lọc
    $search = isset($_GET['search']) ? $_GET['search'] : '';
    $role_filter = isset($_GET['role_filter']) ? $_GET['role_filter'] : '';

    // Xử lý phân trang
    $items_per_page = 5; // Số tài khoản trên mỗi trang
    $current_page = isset($_GET['pg']) ? intval($_GET['pg']) : 1;
    $offset = ($current_page - 1) * $items_per_page;

    // Tạo câu truy vấn SQL với điều kiện tìm kiếm và lọc
    $sql_count = "SELECT COUNT(*) as total FROM account a 
                  JOIN customer c ON a.Username = c.Username 
                  WHERE a.Status = 1";
    
    $sql = "SELECT a.Username, a.RoleID, a.Status, a.`Lock`, c.CustomerID, c.Fullname, c.Email, c.Address, c.Phone, c.TotalSpending 
            FROM account a 
            JOIN customer c ON a.Username = c.Username 
            WHERE a.Status = 1";

    // Thêm điều kiện tìm kiếm nếu có
    if (!empty($search)) {
        $search_param = "%$search%";
        $sql_count .= " AND a.Username LIKE ?";
        $sql .= " AND a.Username LIKE ?";
    }

    // Thêm điều kiện lọc theo vai trò nếu có
    if (!empty($role_filter)) {
        $sql_count .= " AND a.RoleID = ?";
        $sql .= " AND a.RoleID = ?";
    }

    // Thêm LIMIT và OFFSET cho phân trang
    $sql .= " ORDER BY a.Username LIMIT ? OFFSET ?";

    // Prepared statement cho đếm tổng số bản ghi
    $stmt_count = $conn->prepare($sql_count);
    
    // Bind các tham số
    if (!empty($search) && !empty($role_filter)) {
        $stmt_count->bind_param("ss", $search_param, $role_filter);
    } elseif (!empty($search)) {
        $stmt_count->bind_param("s", $search_param);
    } elseif (!empty($role_filter)) {
        $stmt_count->bind_param("s", $role_filter);
    }

    // Thực thi truy vấn đếm
    $stmt_count->execute();
    $result_count = $stmt_count->get_result();
    $row_count = $result_count->fetch_assoc();
    $total_items = $row_count['total'];
    $total_pages = ceil($total_items / $items_per_page);
    $stmt_count->close();

    // Prepared statement cho lấy dữ liệu
    $stmt = $conn->prepare($sql);
    
    // Bind các tham số tùy theo điều kiện
    if (!empty($search) && !empty($role_filter)) {
        $stmt->bind_param("ssii", $search_param, $role_filter, $items_per_page, $offset);
    } elseif (!empty($search)) {
        $stmt->bind_param("sii", $search_param, $items_per_page, $offset);
    } elseif (!empty($role_filter)) {
        $stmt->bind_param("sii", $role_filter, $items_per_page, $offset);
    } else {
        $stmt->bind_param("ii", $items_per_page, $offset);
    }
    
    // Thực thi truy vấn
    $stmt->execute();
    $result = $stmt->get_result();

    // Hàm chuyển đổi RoleID sang tên vai trò - Đã sửa theo yêu cầu mới
    function getRoleName($roleID)
    {
        switch ($roleID) {
            case 'R1':
                return 'Admin';
            case 'R0':
                return 'Quản lý doanh nghiệp';
            case 'R2':
                return 'Quản lý kho';
            case 'R3':
                return 'Nhân viên bán hàng';
            case 'R4':
                return 'Người mua hàng';
            default:
                return 'Không xác định';
        }
    }
    ?>

    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý tài khoản</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
        <link rel="stylesheet" href="assets/css/permission.css">
        <style>
            /* Thêm CSS cho phân trang */
            .pagination {
                margin-top: 20px;
                justify-content: center;
            }
            .search-filter-container {
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 5px;
                margin-bottom: 20px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            .btn-search {
                height: 38px;
            }
            .form-select, .form-control {
                border-radius: 5px;
            }
            .page-info {
                text-align: center;
                margin-top: 10px;
                color: #6c757d;
            }
        </style>
    </head>

    <body>
        <div class="container mt-5">
            <div class="d-flex justify-content-between align-items-center">
                <h2 class="text-primary mb-4"><i class="fas fa-users me-2"></i>Quản lý người dùng</h2>

                <!-- Nút thêm tài khoản -->
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addModal">
                    <i class="fas fa-user-plus me-1"></i> Thêm tài khoản mới
                </button>
            </div>

            <!-- Form tìm kiếm và lọc -->
            <div class="search-filter-container">
                <form action="admin.php" method="GET" class="row g-3">
                    <input type="hidden" name="page" value="permission">
                    <div class="col-md-5">
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-search"></i></span>
                            <input type="text" class="form-control" placeholder="Tìm kiếm theo username" name="search" value="<?php echo htmlspecialchars($search); ?>">
                        </div>
                    </div>
                    <div class="col-md-5">
                        <select class="form-select" name="role_filter">
                            <option value="">-- Tất cả vai trò --</option>
                            <?php foreach ($roles as $role): ?>
                                <option value="<?php echo $role; ?>" <?php echo ($role_filter == $role) ? 'selected' : ''; ?>>
                                    <?php echo getRoleName($role); ?>
                                </option>
                            <?php endforeach; ?>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary w-100 btn-search">
                            <i class="fas fa-filter me-1"></i> Lọc
                        </button>
                    </div>
                </form>
            </div>

            <!-- Bảng hiển thị tài khoản -->
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>CustomerID</th>
                            <th>Username</th>
                            <th>Vai trò</th>
                            <th>Họ và tên</th>
                            <th>Email</th>
                            <th>Địa chỉ</th>
                            <th>Số điện thoại</th>
                            <th class="action-column-header">Chỉnh sửa</th>
                            <th class="action-column-header">Xóa</th>
                            <th class="action-column-header">Khóa</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php
                        if ($result->num_rows > 0) {
                            while ($row = $result->fetch_assoc()) {
                                echo "<tr>";
                                echo "<td>" . $row["CustomerID"] . "</td>";
                                echo "<td>" . $row["Username"] . "</td>";
                                echo "<td>" . getRoleName($row["RoleID"]) . "</td>";
                                echo "<td>" . $row["Fullname"] . "</td>";
                                echo "<td>" . $row["Email"] . "</td>";
                                echo "<td>" . $row["Address"] . "</td>";
                                echo "<td>" . $row["Phone"] . "</td>";

                                // Cột Chỉnh sửa
                                echo "<td>
                                    <button class='btn btn-sm btn-warning edit-btn btn-action' data-bs-toggle='modal' data-bs-target='#editModal' 
                                        data-username='" . $row["Username"] . "' 
                                        data-roleid='" . $row["RoleID"] . "' 
                                        data-status='" . $row["Status"] . "' 
                                        data-fullname='" . $row["Fullname"] . "' 
                                        data-email='" . $row["Email"] . "' 
                                        data-address='" . $row["Address"] . "' 
                                        data-phone='" . $row["Phone"] . "'>
                                        <i class='fas fa-edit'></i> Sửa
                                    </button>
                                </td>";

                                // Cột Xóa
                                echo "<td>
                                    <button class='btn btn-sm btn-danger delete-btn btn-action' onclick='confirmDelete(\"" . $row["Username"] . "\")'>
                                        <i class='fas fa-trash-alt'></i> Xóa
                                    </button>
                                </td>";

                                // Cột Khóa/Mở khóa
                                echo "<td class='text-center'>";
                                if ($row["Lock"] == 1) {
                                    echo "<i class='fas fa-lock-open fa-lg text-success lock-icon' title='Đã mở - Nhấn để khóa' onclick='confirmLock(\"" . $row["Username"] . "\", 0)'></i>";
                                } else {
                                    echo "<i class='fas fa-lock fa-lg text-danger lock-icon' title='Đã khóa - Nhấn để mở' onclick='confirmLock(\"" . $row["Username"] . "\", 1)'></i>";
                                }
                                echo "</td>";
                                echo "</tr>";
                            }
                        } else {
                            echo "<tr><td colspan='10' class='text-center'>Không có dữ liệu</td></tr>";
                        }
                        ?>
                    </tbody>
                </table>
            </div>

           

            <!-- Phân trang -->
            <?php if ($total_pages > 1): ?>
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <!-- Nút Trang trước -->
                    <li class="page-item <?php echo $current_page <= 1 ? 'disabled' : ''; ?>">
                        <a class="page-link" href="<?php echo "admin.php?page=permission&pg=" . ($current_page - 1) . (!empty($search) ? "&search=$search" : "") . (!empty($role_filter) ? "&role_filter=$role_filter" : ""); ?>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    
                    <!-- Các nút số trang -->
                    <?php for ($i = 1; $i <= $total_pages; $i++): ?>
                        <li class="page-item <?php echo $i == $current_page ? 'active' : ''; ?>">
                            <a class="page-link" href="<?php echo "admin.php?page=permission&pg=$i" . (!empty($search) ? "&search=$search" : "") . (!empty($role_filter) ? "&role_filter=$role_filter" : ""); ?>">
                                <?php echo $i; ?>
                            </a>
                        </li>
                    <?php endfor; ?>
                    
                    <!-- Nút Trang sau -->
                    <li class="page-item <?php echo $current_page >= $total_pages ? 'disabled' : ''; ?>">
                        <a class="page-link" href="<?php echo "admin.php?page=permission&pg=" . ($current_page + 1) . (!empty($search) ? "&search=$search" : "") . (!empty($role_filter) ? "&role_filter=$role_filter" : ""); ?>" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
            <?php endif; ?>
        </div>

        <!-- Modal thêm tài khoản -->
        <div class="modal fade" id="addModal" tabindex="-1" aria-labelledby="addModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addModalLabel"><i class="fas fa-user-plus me-2"></i>Thêm tài khoản mới
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form method="post" action="admin.php?page=permission" id="addAccountForm"
                            onsubmit="return validateAddForm()">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="username" class="form-label">Username <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="username" name="username" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="password" class="form-label">Password <span
                                            class="text-danger">*</span></label>
                                    <div class="password-container">
                                        <input type="password" class="form-control" id="password" name="password" required>
                                        <span class="password-toggle" onclick="togglePassword('password')">
                                            <i class="fas fa-eye"></i>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="roleID" class="form-label">Vai trò <span
                                            class="text-danger">*</span></label>
                                    <select class="form-select" id="roleID" name="roleID" required>
                                        <?php foreach ($roles as $role): ?>
                                            <option value="<?php echo $role; ?>" <?php echo ($role == "R4") ? 'selected' : ''; ?>>
                                                <?php echo getRoleName($role); ?>
                                            </option>
                                        <?php endforeach; ?>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label for="lock" class="form-label">Trạng thái khóa <span
                                            class="text-danger">*</span></label>
                                    <select class="form-select" id="lock" name="lock" required>
                                        <option value="1">Mở</option>
                                        <option value="0">Khóa</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="fullname" class="form-label">Họ và tên <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="fullname" name="fullname" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                                    <input type="email" class="form-control" id="email" name="email" required>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="address" class="form-label">Địa chỉ <span
                                            class="text-danger">*</span>
                                    <input type="text" class="form-control" id="address" name="address" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="phone" class="form-label">Số điện thoại <span
                                            class="text-danger">*</span></label>
                                    <input type="tel" class="form-control" id="phone" name="phone" required>
                                    <small class="text-muted">Số điện thoại phải có 10 số</small>
                                </div>
                            </div>
                            <input type="hidden" name="status" value="1"> <!-- Mặc định Status là 1 (Kích hoạt) -->
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                <button type="submit" name="add" class="btn btn-primary">Lưu</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal sửa tài khoản -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel"><i class="fas fa-user-edit me-2"></i>Sửa thông tin tài
                            khoản</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form method="post" action="admin.php?page=permission" id="editAccountForm"
                            onsubmit="return validateEditForm()">
                            <input type="hidden" id="old_username" name="old_username">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="edit_username" class="form-label">Username <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="edit_username" name="username" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="edit_roleid" class="form-label">Vai trò <span
                                            class="text-danger">*</span></label>
                                    <select class="form-select" id="edit_roleid" name="roleID" required>
                                        <option value="R0">Quản lý doanh nghiệp</option>
                                        <option value="R1">Admin</option>
                                        <option value="R2">Quản lý kho</option>
                                        <option value="R3">Nhân viên bán hàng</option>
                                        <option value="R4">Người mua hàng</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="edit_fullname" class="form-label">Họ và tên <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="edit_fullname" name="fullname" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="edit_email" class="form-label">Email <span
                                            class="text-danger">*</span></label>
                                    <input type="email" class="form-control" id="edit_email" name="email" required>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label for="edit_address" class="form-label">Địa chỉ <span
                                            class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="edit_address" name="address" required>
                                </div>
                                <div class="col-md-6">
                                    <label for="edit_phone" class="form-label">Số điện thoại <span
                                            class="text-danger">*</span></label>
                                    <input type="tel" class="form-control" id="edit_phone" name="phone" required>
                                    <small class="text-muted">Số điện thoại phải có 10 số</small>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                <button type="submit" name="edit" class="btn btn-primary">Lưu thay đổi</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script>
            // Xử lý khi nhấn nút sửa để lấy dữ liệu hiển thị lên form
            $('.edit-btn').click(function () {
                var username = $(this).data('username');
                var roleid = $(this).data('roleid');
                var fullname = $(this).data('fullname');
                var email = $(this).data('email');
                var address = $(this).data('address');
                var phone = $(this).data('phone');

                $('#old_username').val(username); // Lưu username cũ
                $('#edit_username').val(username); // Hiển thị username hiện tại
                $('#edit_roleid').val(roleid);
                $('#edit_fullname').val(fullname);
                $('#edit_email').val(email);
                $('#edit_address').val(address);
                $('#edit_phone').val(phone);
            });

            // Xác nhận vô hiệu hóa tài khoản
            function confirmDelete(username) {
                if (confirm("Bạn có chắc chắn muốn vô hiệu hóa tài khoản này không?")) {
                    window.location.href = "admin.php?page=permission&delete=" + username;
                }
            }

            // Xác nhận khóa/mở khóa tài khoản
            function confirmLock(username, lockStatus) {
                const message = lockStatus === 1 ? "mở khóa" : "khóa";
                if (confirm(`Bạn có chắc chắn muốn ${message} tài khoản này không?`)) {
                    window.location.href = `admin.php?page=permission&toggle_lock=${lockStatus}&username=${username}`;
                }
            }

            // Hàm chuyển đổi hiển thị mật khẩu
            function togglePassword(inputId) {
                var passwordInput = document.getElementById(inputId);
                var icon = document.querySelector(`#${inputId} + .password-toggle i`);

                if (passwordInput.type === "password") {
                    passwordInput.type = "text";
                    icon.classList.remove("fa-eye");
                    icon.classList.add("fa-eye-slash");
                } else {
                    passwordInput.type = "password";
                    icon.classList.remove("fa-eye-slash");
                    icon.classList.add("fa-eye");
                }
            }

        // Kiểm tra form thêm tài khoản
        function validateAddForm() {
            var phone = document.getElementById('phone').value;
            var phonePattern = /^\d{10}$/;

            // Kiểm tra số điện thoại có đúng 10 số
            if (!phonePattern.test(phone)) {
                alert("Số điện thoại phải có đúng 10 số!");
                return false;
            }

            // Kiểm tra các trường không được trống
            var fields = ['username', 'password', 'fullname', 'email', 'address'];
            for (var i = 0; i < fields.length; i++) {
                var field = document.getElementById(fields[i]);
                if (field.value.trim() === '') {
                    alert("Vui lòng điền đầy đủ thông tin!");
                    field.focus();
                    return false;
                }
            }

            return true;
        }

        // Kiểm tra form sửa tài khoản
        function validateEditForm() {
            var phone = document.getElementById('edit_phone').value;
            var phonePattern = /^\d{10}$/;

            // Kiểm tra số điện thoại có đúng 10 số
            if (!phonePattern.test(phone)) {
                alert("Số điện thoại phải có đúng 10 số!");
                return false;
            }

            // Kiểm tra các trường không được trống
            var fields = ['edit_fullname', 'edit_email', 'edit_address'];
            for (var i = 0; i < fields.length; i++) {
                var field = document.getElementById(fields[i]);
                if (field.value.trim() === '') {
                    alert("Vui lòng điền đầy đủ thông tin!");
                    field.focus();
                    return false;
                }
            }

            return true;
        }
    </script>
</body>

</html>

<?php
// Đóng kết nối
$conn->close();
?>