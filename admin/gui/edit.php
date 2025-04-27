<?php
// Kết nối đến cơ sở dữ liệu
$servername = "localhost:3306";
$username = "root";
$password = "";
$dbname = "htttgame";

$conn = new mysqli($servername, $username, $password, $dbname);

// Kiểm tra kết nối
if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
}

// Xử lý cập nhật sản phẩm
if (isset($_POST['update'])) {
    $productID = $_POST['ProductID'];
    $productName = $_POST['ProductName'];
    $author = $_POST['Author'];
    $publisher = $_POST['Publisher'];
    $quantity = $_POST['Quantity'];
    $price = $_POST['Price'];
    $description = $_POST['Description'];
    $supplierID = $_POST['SupplierID'];
    $status = $_POST['Status'];
    
    // Xử lý tải lên hình ảnh
    $productImg = $_POST['current_image']; // Giữ ảnh cũ nếu không tải lên ảnh mới
    
    if(isset($_FILES['ProductImg']) && $_FILES['ProductImg']['error'] == 0) {
        // Thay đổi đường dẫn thư mục lưu trữ
        $target_dir = "../../Assets/Images/Game/";
        
        // Đảm bảo thư mục tồn tại
        if (!file_exists($target_dir)) {
            mkdir($target_dir, 0777, true);
        }
        
        $imageFileType = strtolower(pathinfo($_FILES["ProductImg"]["name"], PATHINFO_EXTENSION));
        
        // Kiểm tra nếu file ảnh thực sự là ảnh
        $check = getimagesize($_FILES["ProductImg"]["tmp_name"]);
        if($check !== false) {
            // Tạo tên file duy nhất để tránh trùng lặp
            $filename = uniqid() . "." . $imageFileType;
            $target_file = $target_dir . $filename;
            
            if (move_uploaded_file($_FILES["ProductImg"]["tmp_name"], $target_file)) {
                // Lưu đường dẫn tương đối vào DB
                $productImg = "/Assets/Images/Game/" . $filename;
                echo "<div class='alert alert-success'>File ". htmlspecialchars(basename($_FILES["ProductImg"]["name"])). " đã được tải lên.</div>";
            } else {
                echo "<div class='alert alert-danger'>Xin lỗi, có lỗi khi tải file của bạn.</div>";
            }
        } else {
            echo "<div class='alert alert-danger'>File không phải là ảnh.</div>";
        }
    }
    
    // Cập nhật dữ liệu vào cơ sở dữ liệu với ProductID được đặt trong dấu nháy đơn
    $sql = "UPDATE product SET 
            ProductName = '$productName', 
            ProductImg = '$productImg', 
            Author = '$author', 
            Publisher = '$publisher', 
            Quantity = $quantity, 
            Price = $price, 
            Description = '$description', 
            SupplierID = '$supplierID', 
            Status = $status 
            WHERE ProductID = '$productID'";
    
    if ($conn->query($sql) === TRUE) {
        echo "<div class='alert alert-success'>Cập nhật sản phẩm thành công!</div>";
    } else {
        echo "<div class='alert alert-danger'>Lỗi: " . $conn->error . "</div>";
    }
}

// Xử lý khi nhấn nút Sửa để hiển thị form chỉnh sửa
$editID = isset($_GET['id']) ? $_GET['id'] : '';
$productToEdit = null;

if ($editID) {
    // Đặt ID trong dấu nháy đơn vì nó là chuỗi
    $sql = "SELECT * FROM product WHERE ProductID = '$editID'";
    $result = $conn->query($sql);
    
    if ($result && $result->num_rows > 0) {
        $productToEdit = $result->fetch_assoc();
    } else {
        echo "<div class='alert alert-warning'>Không tìm thấy sản phẩm với ID: " . htmlspecialchars($editID) . "</div>";
    }
}

// Lấy danh sách tất cả sản phẩm
$sql = "SELECT * FROM product";
$result = $conn->query($sql);

// Định nghĩa hàm để chuyển đổi đường dẫn hình ảnh từ cơ sở dữ liệu thành đường dẫn tương đối với trang hiện tại
function getImagePath($dbPath) {
    // Nếu đường dẫn bắt đầu bằng /Assets
    if (strpos($dbPath, '/Assets') === 0) {
        // Đã có / ở đầu, chỉ cần thêm . vào trước
        return "." . $dbPath;
    } else {
        return $dbPath;
    }
}

// Lấy danh sách nhà cung cấp
$supplierQuery = "SELECT SupplierID, SupplierName FROM supplier";
$supplierResult = $conn->query($supplierQuery);
?>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý sản phẩm</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        /* Toàn bộ trang */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }

        h1, h2, h5 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }

        /* Card */
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            background-color: #fff;
            padding: 20px;
        }

        .card-header {
            background-color: #4568dc;
            color: white;
            border-radius: 10px 10px 0 0;
            padding: 15px;
            font-size: 18px;
            font-weight: bold;
        }

        /* Bảng */
        .table-container {
            margin-top: 20px;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #fff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .table th, .table td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
            vertical-align: middle;
        }

        .table th {
            background-color: #4568dc;
            color: white;
            font-weight: bold;
        }

        .table tbody tr:hover {
            background-color: #f1f1f1;
        }

        /* Hình ảnh sản phẩm */
        .product-image {
            width: 80px;
            height: 100px;
            object-fit: cover;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        /* Nút */
        .btn-primary {
            background-color: #4568dc;
            border-color: #4568dc;
            transition: all 0.3s ease;
        }

        .btn-primary:hover {
            background-color: #2a4cb8;
            border-color: #2a4cb8;
        }

        .btn-secondary {
            background-color: #6c757d;
            border-color: #6c757d;
            transition: all 0.3s ease;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
            border-color: #5a6268;
        }

        .btn-warning {
            background-color: #ffc107;
            border-color: #ffc107;
            color: white;
            transition: all 0.3s ease;
        }

        .btn-warning:hover {
            background-color: #e0a800;
            border-color: #d39e00;
        }

        /* Modal */
        .modal-header {
            background-color: #4568dc;
            color: white;
            border-radius: 10px 10px 0 0;
        }

        .modal-footer {
            border-top: none;
        }

        /* Ảnh xem trước */
        #image-preview {
            max-width: 100%;
            max-height: 200px;
            margin-top: 10px;
            display: block;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        /* Thông báo */
        .alert {
            border-radius: 5px;
            padding: 10px 15px;
            margin-bottom: 20px;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .alert-warning {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h2>Danh sách sản phẩm</h2>
        <div class="table-container">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên sản phẩm</th>
                            <th>Hình ảnh</th>
                            <th>Tác giả</th>
                            <th>Nhà xuất bản</th>
                            <th>Số lượng</th>
                            <th>Giá</th>
                            <th>Nhà cung cấp</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php
                        if ($result && $result->num_rows > 0) {
                            while ($row = $result->fetch_assoc()) {
                                echo "<tr>";
                                echo "<td>" . htmlspecialchars($row["ProductID"]) . "</td>";
                                echo "<td>" . htmlspecialchars($row["ProductName"]) . "</td>";
                                echo "<td>";
                                if (!empty($row["ProductImg"])) {
                                    // Chuyển đổi đường dẫn để hiển thị đúng
                                    $imgPath = getImagePath($row["ProductImg"]);
                                    echo "<img src='../../" . htmlspecialchars($imgPath) . "' class='product-image' alt='Hình sản phẩm'>";
                                } else {
                                    echo "Chưa có hình";
                                }
                                echo "</td>";
                                echo "<td>" . htmlspecialchars($row["Author"]) . "</td>";
                                echo "<td>" . htmlspecialchars($row["Publisher"]) . "</td>";
                                echo "<td>" . htmlspecialchars($row["Quantity"]) . "</td>";
                                echo "<td>" . number_format($row["Price"], 0, ',', '.') . " VNĐ</td>";
                                echo "<td>" . htmlspecialchars($row["SupplierID"]) . "</td>";
                                echo "<td>" . ($row["Status"] == 1 ? '<span class="badge bg-success">Kích hoạt</span>' : '<span class="badge bg-secondary">Ẩn</span>') . "</td>";
                                echo "<td><button class='btn btn-warning btn-sm' data-bs-toggle='modal' data-bs-target='#editProductModal' onclick='loadProductData(" . json_encode($row) . ")'>Sửa</button></td>";
                                echo "</tr>";
                            }
                        } else {
                            echo "<tr><td colspan='10' class='text-center'>Không có sản phẩm nào trong cơ sở dữ liệu</td></tr>";
                        }
                        ?>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Modal chỉnh sửa sản phẩm -->
    <div class="modal fade" id="editProductModal" tabindex="-1" aria-labelledby="editProductModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editProductModalLabel">Chỉnh sửa sản phẩm</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="admin.php?page=edit" method="post" enctype="multipart/form-data">
                    <div class="modal-body">
                        <input type="hidden" id="ProductID" name="ProductID">
                        <input type="hidden" id="current_image" name="current_image">
                        
                        <div class="mb-3">
                            <label for="ProductName" class="form-label">Tên sản phẩm</label>
                            <input type="text" class="form-control" id="ProductName" name="ProductName" required>
                        </div>
                        <div class="mb-3">
                            <label for="Author" class="form-label">Tác giả</label>
                            <input type="text" class="form-control" id="Author" name="Author">
                        </div>
                        <div class="mb-3">
                            <label for="Publisher" class="form-label">Nhà xuất bản</label>
                            <input type="text" class="form-control" id="Publisher" name="Publisher">
                        </div>
                        <div class="mb-3">
                            <label for="Quantity" class="form-label">Số lượng</label>
                            <input type="number" class="form-control" id="Quantity" name="Quantity" required>
                        </div>
                        <div class="mb-3">
                            <label for="Price" class="form-label">Giá</label>
                            <input type="number" class="form-control" id="Price" name="Price" required>
                        </div>
                        <div class="mb-3">
                            <label for="SupplierID" class="form-label">Nhà cung cấp</label>
                            <select class="form-select" id="SupplierID" name="SupplierID" required>
                                <?php
                                if ($supplierResult && $supplierResult->num_rows > 0) {
                                    while ($supplier = $supplierResult->fetch_assoc()) {
                                        echo "<option value='" . htmlspecialchars($supplier['SupplierID']) . "'>" . htmlspecialchars($supplier['SupplierName']) . "</option>";
                                    }
                                }
                                ?>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="Status" class="form-label">Trạng thái</label>
                            <select class="form-select" id="Status" name="Status">
                                <option value="1">Kích hoạt</option>
                                <option value="0">Ẩn</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="Description" class="form-label">Mô tả</label>
                            <textarea class="form-control" id="Description" name="Description" rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="ProductImg" class="form-label">Hình ảnh sản phẩm</label>
                            <input type="file" class="form-control" id="ProductImg" name="ProductImg">
                            <div class="mt-2">
                                <img id="currentImagePreview" class="product-image" alt="Ảnh sản phẩm">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" name="update" class="btn btn-primary">Cập nhật sản phẩm</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function loadProductData(product) {
            document.getElementById('ProductID').value = product.ProductID;
            document.getElementById('ProductName').value = product.ProductName;
            document.getElementById('Author').value = product.Author;
            document.getElementById('Publisher').value = product.Publisher;
            document.getElementById('Quantity').value = product.Quantity;
            document.getElementById('Price').value = product.Price;
            document.getElementById('Status').value = product.Status;
            document.getElementById('Description').value = product.Description;
            document.getElementById('current_image').value = product.ProductImg;

            // Đặt giá trị cho dropdown nhà cung cấp
            const supplierDropdown = document.getElementById('SupplierID');
            supplierDropdown.value = product.SupplierID;

            // Hiển thị ảnh xem trước
            let imagePath = product.ProductImg;
            if (imagePath.startsWith('/Assets')) {
                imagePath = '.' + imagePath;
            }
            document.getElementById('currentImagePreview').src = imagePath;
        }
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

<?php
// Đóng kết nối
$conn->close();
?>