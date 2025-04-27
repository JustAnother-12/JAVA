<?php
include_once __DIR__ . '/../functions/db.php';

$result = $conn->query("SELECT SupplierID, SupplierName FROM supplier");
$suppliers = [];

while ($row = $result->fetch_assoc()) {
    $suppliers[] = $row;
}

echo json_encode($suppliers);
?>
