<?php
include_once __DIR__ . '/../functions/db.php';

if (!$conn) {
    die("Kết nối DB thất bại.");
}

// Thêm sản phẩm
if ($_SERVER['REQUEST_METHOD'] === 'POST' && $_POST['action'] === 'add') {
    $id = $_POST['ProductID'];
    $name = $_POST['ProductName'];
    $img = $_POST['ProductImg'];
    $author = $_POST['Author'];
    $publisher = $_POST['Publisher'];
    $qty = isset($_POST['Quantity']) && $_POST['Quantity'] !== '' ? $_POST['Quantity'] : 0;
    $price = $_POST['Price'];
    $desc = $_POST['Description'];
    $supplier = $_POST['SupplierID'];
    $status = $_POST['Status'];
    $typeID = $_POST['TypeID'];

    $stmt = $conn->prepare("INSERT INTO product 
        (ProductID, ProductName, ProductImg, Author, Publisher, Quantity, Price, Description, SupplierID, Status) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssssidsss", $id, $name, $img, $author, $publisher, $qty, $price, $desc, $supplier, $status);
    $stmt->execute();

    if (!empty($typeID)) {
        $stmt2 = $conn->prepare("INSERT INTO type_product (ProductID, TypeID) VALUES (?, ?)");
        $stmt2->bind_param("ss", $id, $typeID);
        $stmt2->execute();
    }

    exit;
}

// Xóa sản phẩm
if ($_SERVER['REQUEST_METHOD'] === 'POST' && $_POST['action'] === 'delete') {
    $id = $_POST['ProductID'];
    $stmt = $conn->prepare("DELETE FROM product WHERE ProductID = ?");
    $stmt->bind_param("s", $id);
    $stmt->execute();
    exit;
}

// Cập nhật sản phẩm
if ($_SERVER['REQUEST_METHOD'] === 'POST' && $_POST['action'] === 'edit') {
    $id = $_POST['ProductID'];
    $name = $_POST['ProductName'];
    $img = $_POST['ProductImg'];
    $author = $_POST['Author'];
    $publisher = $_POST['Publisher'];
    $qty = isset($_POST['Quantity']) && $_POST['Quantity'] !== '' ? $_POST['Quantity'] : 0;
    $price = $_POST['Price'];
    $desc = $_POST['Description'];
    $supplier = $_POST['SupplierID'];
    $status = $_POST['Status'];
    $typeID = $_POST['TypeID'];

    $stmt = $conn->prepare("UPDATE product SET ProductName=?, ProductImg=?, Author=?, Publisher=?, Quantity=?, Price=?, Description=?, SupplierID=?, Status=? WHERE ProductID=?");
    $stmt->bind_param("sssssdssss", $name, $img, $author, $publisher, $qty, $price, $desc, $supplier, $status, $id);
    $stmt->execute();

    $checkType = $conn->prepare("SELECT * FROM type_product WHERE ProductID=?");
    $checkType->bind_param("s", $id);
    $checkType->execute();
    $resultType = $checkType->get_result();

    if ($resultType->num_rows > 0) {
        $stmt2 = $conn->prepare("UPDATE type_product SET TypeID=? WHERE ProductID=?");
        $stmt2->bind_param("ss", $typeID, $id);
        $stmt2->execute();
    } else {
        $stmt2 = $conn->prepare("INSERT INTO type_product (ProductID, TypeID) VALUES (?, ?)");
        $stmt2->bind_param("ss", $id, $typeID);
        $stmt2->execute();
    }

    exit;
}

// Phân trang + Tìm kiếm
$page = isset($_GET['page']) ? max(1, (int)$_GET['page']) : 1;
$limit = 5;
$offset = ($page - 1) * $limit;
$keyword = isset($_GET['keyword']) ? trim($_GET['keyword']) : '';
$keywordLike = '%' . $conn->real_escape_string($keyword) . '%';
$sortField = $_GET['sortField'] ?? '';
$sortOrder = $_GET['sortOrder'] ?? '';

$validFields = ['TypeID', 'SupplierID'];
$validOrders = ['asc', 'desc'];

if (in_array($sortField, $validFields) && in_array($sortOrder, $validOrders)) {
    $orderClause = "ORDER BY $sortField $sortOrder";
} else {
    $orderClause = "ORDER BY p.ProductID ASC";
}


if (!empty($keyword)) {
    $countStmt = $conn->prepare("SELECT COUNT(*) AS total FROM product WHERE ProductID LIKE ? OR ProductName LIKE ?");
    $countStmt->bind_param("ss", $keywordLike,$keywordLike);
    $countStmt->execute();
    $totalResult = $countStmt->get_result();
} else {
    $totalResult = $conn->query("SELECT COUNT(*) AS total FROM product");
}

$totalRows = $totalResult->fetch_assoc()['total'];
$totalPages = ceil($totalRows / $limit);

if (!empty($keyword)) {
    $stmt = $conn->prepare("
        SELECT p.*, tp.TypeID
        FROM product p
        LEFT JOIN type_product tp ON p.ProductID = tp.ProductID
        WHERE p.ProductID LIKE ? OR p.ProductName LIKE ?
        $orderClause
        LIMIT ? OFFSET ?
    ");
    $stmt->bind_param("ssii", $keywordLike,$keywordLike, $limit, $offset);
    $stmt->execute();
    $result = $stmt->get_result();
} else {
    $result = $conn->query("
        SELECT p.*, tp.TypeID
        FROM product p
        LEFT JOIN type_product tp ON p.ProductID = tp.ProductID
        $orderClause
        LIMIT $limit OFFSET $offset
    ");
}
?>

<table border="1" cellpadding="8" cellspacing="0" >
    <thead>
        <tr>
            <th>Mã SP</th>
            <th> <span style="display: inline-flex; align-items: center;margin-left: 6px;">Loại <button class="sort-btn" style="margin-left: 6px;" data-field="TypeID" data-order="asc">⇅</button></span> </th>
            <th>Tên SP</th>
            <th>Ảnh</th>
            <th>Tác giả</th>
            <th>NXB</th>
            <th>SL</th>
            <th>Giá</th>
            <th>Mô tả</th>
            <th><span style="display: inline-flex; align-items: center;margin-left: 8px;">NCC <button class="sort-btn" data-field="SupplierID" data-order="asc">⇅</button> </span></th>
            <th>Trạng thái</th>
            <th>Hành động</th>
        </tr>
    </thead>
    <tbody>
    <?php while ($row = $result->fetch_assoc()): ?>
    <tr>
        <td><?= htmlspecialchars($row['ProductID']) ?></td>
        <td><?= htmlspecialchars($row['TypeID']) ?></td>
        <td><?= htmlspecialchars($row['ProductName']) ?></td>
        <td><img src="<?= htmlspecialchars($row['ProductImg']) ?>" width="60" height="60" onerror="this.src='https://via.placeholder.com/60'"></td>
        <td><?= htmlspecialchars($row['Author']) ?></td>
        <td><?= htmlspecialchars($row['Publisher']) ?></td>
        <td><?= $row['Quantity'] ?></td>
        <td><?= $row['Price'] ?></td>
        <td><?= htmlspecialchars($row['Description']) ?></td>
        <td><?= htmlspecialchars($row['SupplierID']) ?></td>
        <td>
        <?= ($row['Status'] == 1) ? 'Hoạt động' : 'Ngừng hoạt động'; ?>
        </td>
        <td style="text-align: center;">
            <a href="#" class="edit-btn" data-id="<?= $row['ProductID'] ?>" title="Chỉnh sửa">✏️</a>
            &nbsp;
            <a href="#" class="delete-btn" data-id="<?= $row['ProductID'] ?>" title="Xóa">🗑️</a>
        </td>
    </tr>
    <?php endwhile; ?>
    </tbody>
</table>

<?php if ($totalPages > 1): ?>
<div style="margin-top: 20px;">
    <?php if ($page > 1): ?>
        <a href="#" class="page-link" data-page="1" data-keyword="<?= htmlspecialchars($keyword) ?>">«</a>
        <a href="#" class="page-link" data-page="<?= $page - 1 ?>" data-keyword="<?= htmlspecialchars($keyword) ?>">‹</a>
    <?php endif; ?>

    <?php for ($i = 1; $i <= $totalPages; $i++): ?>
        <a href="#" class="page-link"
   data-page="<?= $i ?>"
   data-keyword="<?= htmlspecialchars($keyword) ?>"
   data-sortfield="<?= htmlspecialchars($sortField) ?>"
   data-sortorder="<?= htmlspecialchars($sortOrder) ?>"
   style="margin:0 5px; <?= $i === $page ? 'font-weight:bold; color:blue;' : '' ?>">
   <?= $i ?>
</a>

    <?php endfor; ?>

    <?php if ($page < $totalPages): ?>
        <a href="#" class="page-link" data-page="<?= $page + 1 ?>" data-keyword="<?= htmlspecialchars($keyword) ?>">›</a>
        <a href="#" class="page-link" data-page="<?= $totalPages ?>" data-keyword="<?= htmlspecialchars($keyword) ?>">»</a>
    <?php endif; ?>
</div>
<input type="hidden" id="totalPages" value="<?= $totalPages ?>">
<?php endif; ?>






