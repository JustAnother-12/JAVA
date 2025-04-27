<?php
include_once __DIR__ . '/../functions/db.php';

if (!$conn) {
    die("Kết nối DB thất bại.");
}

$action = $_POST['action'] ?? null;

if ($action === 'add') {
    $stmt = $conn->prepare("INSERT INTO supplier (SupplierID, SupplierName, Address, Phone, Email) VALUES (?, ?, ?, ?, ?)");
    $stmt->bind_param("sssss", $_POST['SupplierID'], $_POST['SupplierName'], $_POST['Address'], $_POST['Phone'], $_POST['Email']);
    $stmt->execute();
    exit;
}

if ($action === 'edit') {
    $stmt = $conn->prepare("UPDATE supplier SET SupplierName = ?, Address = ?, Phone = ?, Email = ? WHERE SupplierID = ?");
    $stmt->bind_param("sssss", $_POST['SupplierName'], $_POST['Address'], $_POST['Phone'], $_POST['Email'], $_POST['SupplierID']);
    $stmt->execute();
    exit;
}

if ($action === 'delete') {
    $stmt = $conn->prepare("DELETE FROM supplier WHERE SupplierID = ?");
    $stmt->bind_param("s", $_POST['SupplierID']);
    $stmt->execute();
    exit;
}

// Load data (GET)
$page = isset($_GET['page']) ? max(1, intval($_GET['page'])) : 1;
$limit = 5;
$offset = ($page - 1) * $limit;

// Get total count
$totalStmt = $conn->query("SELECT COUNT(*) FROM supplier");
$row = $totalStmt->fetch_row();
$total = $row[0];
$totalPages = ceil($total / $limit);

// Get current page data
$stmt = $conn->prepare("SELECT * FROM supplier LIMIT ? OFFSET ?");
$stmt->bind_param("ii", $limit, $offset);
$stmt->execute();
$result = $stmt->get_result();

$suppliers = [];
while ($row = $result->fetch_assoc()) {
    $suppliers[] = $row;
}

// Display table
echo "<table border='1' cellpadding='5' cellspacing='0'><thead>
<tr>
    <th>Mã NCC</th>
    <th>Tên</th>
    <th>Địa chỉ</th>
    <th>Điện thoại</th>
    <th>Email</th>
    <th>Hành động</th>
</tr></thead><tbody>";

foreach ($suppliers as $s) {
    echo "<tr>
        <td>{$s['SupplierID']}</td>
        <td>{$s['SupplierName']}</td>
        <td>{$s['Address']}</td>
        <td>{$s['Phone']}</td>
        <td>{$s['Email']}</td>
        <td>
            <button class='edit-btn'>✏️</button>
            <button class='delete-btn' data-id='{$s['SupplierID']}'>🗑️</button>
        </td>
    </tr>";
}
echo "</tbody></table>";

// Pagination
echo "<div style='margin-top:10px;'>";
for ($i = 1; $i <= $totalPages; $i++) {
    echo "<button class='page-link' data-page='$i'>$i</button> ";
}
echo "</div>";
?>



