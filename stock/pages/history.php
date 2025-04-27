<?php
include_once __DIR__ . '/../functions/db.php';
$sort = $_GET['sort'] ?? 'none';

switch ($sort) {
    case 'asc':
        $orderBy = "ORDER BY Date ASC";
        $nextSort = 'desc';
        break;
    case 'desc':
        $orderBy = "ORDER BY Date DESC";
        $nextSort = 'none';
        break;
    default:
        $orderBy = "";
        $nextSort = 'asc';
}


$query = mysqli_query($conn, "SELECT * FROM import_invoice $orderBy");

?>

<h2>Lịch sử phiếu nhập</h2>
<div class="history-box">
    <table class="history-table">
        <thead>
            <tr>
                <th>Import ID</th>
                <th>Employee ID</th>
                <th>Supplier ID</th>
                <th>
                <a href="?section=history<?= $sort !== 'none' ? '&sort=' . $nextSort : '&sort=asc' ?>" style="color: white; text-decoration: none;">
    Date <?= $sort == 'asc' ? '↑' : ($sort == 'desc' ? '↓' : '') ?>
</a>

</th>


                <th>Total Price</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <?php while ($row = mysqli_fetch_assoc($query)) : ?>
                <tr>
                    <td><?= $row['ImportID'] ?></td>
                    <td><?= $row['EmployeeID'] ?></td>
                    <td><?= $row['SupplierID'] ?></td>
                    <td><?= $row['Date'] ?></td>
                    <td><?= number_format($row['TotalPrice'], 0, ',', '.') ?> VND</td>
                    <td>
                    <a href="javascript:void(0);" class="btn-view" onclick="openDetail('<?= $row['ImportID'] ?>')">Xem chi tiết</a>

                    </td>
                </tr>
            <?php endwhile; ?>
        </tbody>
    </table>
</div>
<div id="detailModal" class="modal-overlay" onclick="closeModal(event)">
    <div class="modal-content" onclick="event.stopPropagation()">
        <span class="close-btn" onclick="closeModal()">×</span>
        <div id="detailContent">Loading...</div>
        <button onclick="printModal()">In</button>
    </div>
</div>

<script>
function openDetail(id) {
    const modal = document.getElementById('detailModal');
    const content = document.getElementById('detailContent');
    content.innerHTML = 'Đang tải...';

    fetch(`pages/detail_import.php?id=${id}`)
        .then(res => res.text())
        .then(html => content.innerHTML = html);

    modal.style.display = 'flex';
}

function closeModal(e) {
    document.getElementById('detailModal').style.display = 'none';
}
function printModal() {
    const content = document.getElementById('detailContent').innerHTML;
    const iframe = document.createElement('iframe');
    iframe.style.display = 'none';
    document.body.appendChild(iframe);

    const doc = iframe.contentWindow.document;
    doc.open();
    doc.write(`
        <html>
            <head>
                <title>In phiếu nhập</title>
                <style>
                    body { font-family: Arial; padding: 20px; }
                    table { width: 100%; border-collapse: collapse; }
                    table, th, td { border: 1px solid black; }
                    th, td { padding: 8px; text-align: left; }
                </style>
            </head>
            <body>
                ${content}
            </body>
        </html>
    `);
    doc.close();

    iframe.contentWindow.focus();
    iframe.contentWindow.print();

    // Xóa iframe sau in
    setTimeout(() => document.body.removeChild(iframe), 1000);
}

</script>