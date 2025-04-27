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

// Lấy danh sách nhà cung cấp cho dropdown
$supplier_query = "SELECT SupplierID, SupplierName FROM supplier";
$supplier_result = $conn->query($supplier_query);

// Kiểm tra xem có dữ liệu nhà cung cấp không
if (!$supplier_result || $supplier_result->num_rows == 0) {
    // Tạo nhà cung cấp mặc định nếu chưa có
    $insert_supplier = "INSERT INTO supplier (SupplierID, SupplierName, Email, Phone, Address) VALUES ('SUP001', 'Nhà cung cấp mặc định', 'supplier@example.com', '0123456789', 'Địa chỉ mặc định')";
    $conn->query($insert_supplier);
    // Lấy lại danh sách nhà cung cấp
    $supplier_result = $conn->query($supplier_query);
}

// Xử lý thêm sản phẩm mới
if(isset($_POST['add_product'])) {
    $productName = $conn->real_escape_string($_POST['productName']);
    $author = $conn->real_escape_string($_POST['author']);
    $publisher = $conn->real_escape_string($_POST['publisher']);
    $quantity = intval($_POST['quantity']);
    $price = floatval($_POST['price']);
    $description = $conn->real_escape_string($_POST['description']);
    $supplierID = $conn->real_escape_string($_POST['supplierID']);
    $status = intval($_POST['status']);
    
    // Debug - hiển thị giá trị supplierID
    echo "<script>console.log('SupplierID: " . $supplierID . "');</script>";
    
    // Tìm ID sản phẩm tiếp theo
    $result_id = $conn->query("SELECT ProductID FROM product ORDER BY ProductID DESC LIMIT 1");
    if($result_id && $result_id->num_rows > 0) {
        $row_id = $result_id->fetch_assoc();
        $last_id = $row_id["ProductID"];
        $numeric_part = intval(substr($last_id, 4)); // Lấy phần số từ GAME040
        $new_numeric = $numeric_part + 1;
        $productID = "GAME" . str_pad($new_numeric, 3, "0", STR_PAD_LEFT); // Tạo ID mới như GAME041
    } else {
        $productID = "GAME001"; // ID mặc định nếu chưa có sản phẩm nào
    }
    
    // Kiểm tra xem file ảnh đã được tải lên chưa
    if(isset($_FILES["productImg"]) && $_FILES["productImg"]["error"] == 0) {
        // Xử lý upload hình ảnh
        $target_dir = "../../Assets/Images/Game/";
        
        // Debug - hiển thị đường dẫn tuyệt đối
        echo "<script>console.log('Absolute path: " . realpath($target_dir) . "');</script>";
        
        $imageFileType = strtolower(pathinfo($_FILES["productImg"]["name"], PATHINFO_EXTENSION));
        $newFileName = uniqid() . "." . $imageFileType; // Tạo tên file mới để tránh trùng lặp
        $target_file = $target_dir . $newFileName;
        $uploadOk = 1;
        
        // Debug thông tin thư mục
        echo "<script>console.log('Target directory: " . $target_dir . "');</script>";
        echo "<script>console.log('Target file: " . $target_file . "');</script>";
        
        // Tạo thư mục nếu chưa tồn tại
        if (!file_exists($target_dir)) {
            if(!mkdir($target_dir, 0777, true)) {
                $error_message = "Không thể tạo thư mục để lưu ảnh. Vui lòng kiểm tra quyền truy cập.";
                $uploadOk = 0;
                echo "<script>console.log('Failed to create directory: " . $target_dir . "');</script>";
                echo "<script>console.log('Error: " . error_get_last()['message'] . "');</script>";
            } else {
                // Set proper permissions after creating
                chmod($target_dir, 0777);
                echo "<script>console.log('Directory created successfully');</script>";
            }
        } else {
            echo "<script>console.log('Directory already exists');</script>";
            // Make sure it's writable
            if (!is_writable($target_dir)) {
                chmod($target_dir, 0777);
                echo "<script>console.log('Changed directory permissions');</script>";
            }
        }
        
        // Kiểm tra file có phải là ảnh thật không
        $check = getimagesize($_FILES["productImg"]["tmp_name"]);
        if($check === false) {
            $error_message = "File không phải là ảnh.";
            $uploadOk = 0;
        }
        
        // Kiểm tra kích thước file
        if ($_FILES["productImg"]["size"] > 5000000) { // 5MB
            $error_message = "File quá lớn, vui lòng chọn file nhỏ hơn 5MB.";
            $uploadOk = 0;
        }
        
        // Chỉ cho phép một số định dạng file
        if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg" && $imageFileType != "gif") {
            $error_message = "Chỉ chấp nhận file JPG, JPEG, PNG & GIF.";
            $uploadOk = 0;
        }
        
        // Kiểm tra nhà cung cấp có tồn tại
        $check_supplier = "SELECT SupplierID FROM supplier WHERE SupplierID = '$supplierID'";
        $supplier_check = $conn->query($check_supplier);
        
        if (!$supplier_check || $supplier_check->num_rows == 0) {
            $error_message = "Mã nhà cung cấp không tồn tại. Vui lòng chọn mã nhà cung cấp hợp lệ.";
            $uploadOk = 0;
        }
        
        // Nếu không có lỗi, tiến hành upload và thêm sản phẩm
        if ($uploadOk == 1) {
            // Debug thông tin trước khi upload
            echo "<script>console.log('Attempting to move uploaded file from: " . $_FILES["productImg"]["tmp_name"] . " to: " . $target_file . "');</script>";
            echo "<script>console.log('Temp file exists: " . file_exists($_FILES["productImg"]["tmp_name"]) . "');</script>";
            echo "<script>console.log('Temp file readable: " . is_readable($_FILES["productImg"]["tmp_name"]) . "');</script>";
            
            if (move_uploaded_file($_FILES["productImg"]["tmp_name"], $target_file)) {
                // Đường dẫn tương đối từ gốc web
                $productImg = "/Assets/Images/Game/" . $newFileName;
                
                // Debug - hiển thị đường dẫn hình ảnh
                echo "<script>console.log('Image path: " . $productImg . "');</script>";
                
                $sql = "INSERT INTO product (ProductID, ProductName, ProductImg, Author, Publisher, Quantity, Price, Description, SupplierID, Status) 
                        VALUES ('$productID', '$productName', '$productImg', '$author', '$publisher', $quantity, $price, '$description', '$supplierID', $status)";
                
                if ($conn->query($sql) === TRUE) {
                    $success_message = "Sản phẩm đã được thêm thành công";
                    // Tự động refresh trang sau khi thêm
                    echo "<meta http-equiv='refresh' content='1'>";
                } else {
                    $error_message = "Lỗi khi thêm sản phẩm: " . $conn->error;
                    echo "<script>console.log('SQL Error: " . $conn->error . "');</script>";
                }
            } else {
                $error_message = "Đã xảy ra lỗi khi tải lên file ảnh. Kiểm tra quyền ghi vào thư mục.";
                echo "<script>console.log('Upload failed: " . error_get_last()['message'] . "');</script>";
                
                // Thử một cách khác nếu thất bại
                if (!copy($_FILES["productImg"]["tmp_name"], $target_file)) {
                    echo "<script>console.log('Copy also failed: " . error_get_last()['message'] . "');</script>";
                } else {
                    // Nếu copy thành công
                    $productImg = "/Assets/Images/Game/" . $newFileName;
                    
                    $sql = "INSERT INTO product (ProductID, ProductName, ProductImg, Author, Publisher, Quantity, Price, Description, SupplierID, Status) 
                            VALUES ('$productID', '$productName', '$productImg', '$author', '$publisher', $quantity, $price, '$description', '$supplierID', $status)";
                    
                    if ($conn->query($sql) === TRUE) {
                        $success_message = "Sản phẩm đã được thêm thành công (sử dụng phương thức copy)";
                        // Tự động refresh trang sau khi thêm
                        echo "<meta http-equiv='refresh' content='1'>";
                    } else {
                        $error_message = "Lỗi khi thêm sản phẩm: " . $conn->error;
                    }
                }
            }
        }
    } else {
        $error_message = "Vui lòng chọn hình ảnh cho sản phẩm.";
        echo "<script>console.log('File upload error code: " . $_FILES["productImg"]["error"] . "');</script>";
    }
}

// Lấy danh sách sản phẩm
$sql = "SELECT p.ProductID, p.ProductName, p.ProductImg, p.Author, p.Publisher, p.Quantity, p.Price, 
        p.Description, p.SupplierID, s.SupplierName, p.Status 
        FROM product p 
        LEFT JOIN supplier s ON p.SupplierID = s.SupplierID";
$result = $conn->query($sql);
?>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Sản phẩm</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="../assets/css/add.css">
</head>
<body>
    <div class="container mt-5">
        
        <!-- Hiển thị thông báo -->
        <?php if(isset($success_message)): ?>
            <div class="alert alert-success"><?php echo $success_message; ?></div>
        <?php endif; ?>
        
        <?php if(isset($error_message)): ?>
            <div class="alert alert-danger"><?php echo $error_message; ?></div>
        <?php endif; ?>
        
        <!-- Bảng danh sách sản phẩm -->
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 style="color:white;">Danh sách Sản phẩm</h5>
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addProductModal">
                    <i class="fas fa-plus"></i> Thêm Sản phẩm
                </button>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Hình ảnh</th>
                                <th>Tên sản phẩm</th>
                                <th>Tác giả</th>
                                <th>Nhà xuất bản</th>
                                <th>Số lượng</th>
                                <th>Giá</th>
                                <th>Mô tả</th>
                                <th>Nhà cung cấp</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            <?php
                            if ($result && $result->num_rows > 0) {
                                while($row = $result->fetch_assoc()) {
                                    echo "<tr>";
                                    echo "<td>" . $row["ProductID"] . "</td>";
                                    echo "<td>
                                            <img src='../../" . $row["ProductImg"] . "' class='product-img' alt='Product Image'>
                                            <span class='d-none'>" . $row["ProductImg"] . "</span>
                                          </td>";
                                    echo "<td>" . $row["ProductName"] . "</td>";
                                    echo "<td>" . $row["Author"] . "</td>";
                                    echo "<td>" . $row["Publisher"] . "</td>";
                                    echo "<td>" . $row["Quantity"] . "</td>";
                                    echo "<td>" . number_format($row["Price"], 0, ',', '.') . " VNĐ</td>";
                                    echo "<td class='description-cell'>" . $row["Description"] . "</td>";
                                    echo "<td>" . $row["SupplierName"] . "</td>";
                                    echo "<td>" . ($row["Status"] == 1 ? '<span class="badge bg-success">Hoạt động</span>' : '<span class="badge bg-danger">Không hoạt động</span>') . "</td>";
                                    echo "</tr>";
                                }
                            } else {
                                echo "<tr><td colspan='10' class='text-center'>Không có sản phẩm nào</td></tr>";
                            }
                            ?>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal thêm sản phẩm -->
    <div class="modal fade" id="addProductModal" tabindex="-1" aria-labelledby="addProductModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addProductModalLabel">Thêm Sản phẩm Mới</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form method="POST" action="" enctype="multipart/form-data" id="addProductForm">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="productName" class="form-label">Tên sản phẩm</label>
                                <input type="text" class="form-control" id="productName" name="productName" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="productImg" class="form-label">Hình ảnh</label>
                                <input type="file" class="form-control" id="productImg" name="productImg" accept="image/*" required>
                                <small class="text-muted">Hình ảnh sẽ được lưu vào thư mục /Assets/Images/Game/</small>
                                <img id="image-preview" src="#" alt="Preview" class="mt-2">
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="author" class="form-label">Tác giả</label>
                                <input type="text" class="form-control" id="author" name="author" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="publisher" class="form-label">Nhà xuất bản</label>
                                <input type="text" class="form-control" id="publisher" name="publisher" required>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="quantity" class="form-label">Số lượng</label>
                                <input type="number" class="form-control" id="quantity" name="quantity" min="0" required>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="price" class="form-label">Giá</label>
                                <input type="number" class="form-control" id="price" name="price" min="0" step="0.01" required>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="supplierID" class="form-label">Nhà cung cấp</label>
                                <select class="form-control" id="supplierID" name="supplierID" required>
                                    <?php
                                    if ($supplier_result && $supplier_result->num_rows > 0) {
                                        // Reset con trỏ kết quả về đầu
                                        $supplier_result->data_seek(0);
                                        while($supplier = $supplier_result->fetch_assoc()) {
                                            echo "<option value='" . $supplier["SupplierID"] . "'>" . $supplier["SupplierName"] . "</option>";
                                        }
                                    } else {
                                        echo "<option value=''>Không có nhà cung cấp</option>";
                                    }
                                    ?>
                                </select>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="description" class="form-label">Mô tả</label>
                            <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label for="status" class="form-label">Trạng thái</label>
                            <select class="form-control" id="status" name="status" required>
                                <option value="1">Hoạt động</option>
                                <option value="0">Không hoạt động</option>
                            </select>
                        </div>
                        
                        <!-- Thêm input ẩn để đảm bảo form được xử lý đúng -->
                        <input type="hidden" name="add_product" value="1">
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="button" class="btn btn-primary" id="submitAddProduct">Thêm sản phẩm</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script>
        // Script để hiển thị tên file đã chọn
        document.getElementById('productImg').addEventListener('change', function() {
            const fileName = this.files[0].name;
            const fileLabel = document.querySelector('label[for="productImg"]');
            fileLabel.textContent = 'Hình ảnh: ' + fileName;
            
            // Hiển thị ảnh xem trước
            const reader = new FileReader();
            reader.onload = function(e) {
                const preview = document.getElementById('image-preview');
                preview.src = e.target.result;
                preview.style.display = 'block';
            };
            reader.readAsDataURL(this.files[0]);
        });
        
        // Script để submit form khi nhấn nút thêm sản phẩm trong modal
        document.getElementById('submitAddProduct').addEventListener('click', function() {
            // Submit form
            document.getElementById('addProductForm').submit();
        });
    </script>
</body>
</html>

<?php
// Đóng kết nối
$conn->close();
?>