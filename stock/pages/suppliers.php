<!-- supplier.php -->
<div id="supplierContainer">
    <h2>Quản lý nhà cung cấp</h2>
    <div style="margin-bottom: 10px;">
        <button id="showAddRow">➕ Thêm nhà cung cấp</button>
    </div>
    <div id="supplierTable">Đang tải dữ liệu...</div>
</div>


<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
$(document).on('input', '.only-number', function () {
    this.value = this.value.replace(/[^0-9]/g, '');
});    
function loadSuppliers(page = 1) {
    $.get("pages/supplier_load.php", { page }, function (data) {
        $("#supplierTable").html(data);
    });
}

 $(document).ready(function () {
    loadSuppliers();

    $('#supplierContainer').on('click', '#showAddRow', function () {
        if ($('#addRow').length) return;

        const newRow = `
<tr id="addRow">
    <td><input type="text" name="SupplierID" placeholder="Mã NCC"></td>
    <td><input type="text" name="SupplierName" placeholder="Tên nhà cung cấp"></td>
    <td><input type="text" name="Address" placeholder="Địa chỉ"></td>
    <td><input type="text" name="Phone" placeholder="SĐT" class="only-number"></td>
    <td><input type="text" name="Email" placeholder="Email"></td>
    <td>
        <button class="save-supplier">💾</button>
        <button class="cancel-add">❌</button>
    </td>
</tr>`;

        $("#supplierTable table tbody").append(newRow);
        $('#showAddRow').hide();
    });

    $('#supplierContainer').on('click', '.cancel-add', function () {
        $('#addRow').remove();
        $('#showAddRow').show();
    });

    $('#supplierContainer').on('click', '.save-supplier', function () {
        const row = $('#addRow');
        const data = {
    action: 'add',
    SupplierID: row.find('input[name="SupplierID"]').val(),
    SupplierName: row.find('input[name="SupplierName"]').val(),
    Address: row.find('input[name="Address"]').val(),
    Phone: row.find('input[name="Phone"]').val(),
    Email: row.find('input[name="Email"]').val()
};


        $.post('pages/supplier_load.php', data, function () {
            loadSuppliers();
            $('#showAddRow').show();
        });
    });

    $('#supplierContainer').on('click', '.delete-btn', function () {
        if (!confirm('Xóa nhà cung cấp này?')) return;
        const id = $(this).data('id');
        $.post('pages/supplier_load.php', { action: 'delete', SupplierID: id }, function () {
            loadSuppliers();
        });
    });

    $('#supplierContainer').on('click', '.edit-btn', function () {
    const row = $(this).closest('tr');
    const tds = row.find('td');

    // 👉 Lưu HTML gốc trước khi chuyển sang input
    row.data('originalHTML', row.html());

    const data = {
        SupplierID: tds.eq(0).text().trim(),
        SupplierName: tds.eq(1).text().trim(),
        Address: tds.eq(2).text().trim(),
        Phone: tds.eq(3).text().trim(),
        Email: tds.eq(4).text().trim()
    };

    row.html(`
        <td>${data.SupplierID}</td>
        <td><input type="text" name="SupplierName" value="${data.SupplierName}"></td>
        <td><input type="text" name="Address" value="${data.Address}"></td>
        <td><input type="text" name="Phone" value="${data.Phone}" class="only-number"></td>
        <td><input type="text" name="Email" value="${data.Email}"></td>
        <td>
            <button class="save-edit">💾</button>
            <button class="cancel-edit">❌</button>
        </td>
    `);
});


    $('#supplierContainer').on('click', '.cancel-edit', function () {
        const row = $(this).closest('tr');
        row.html(row.data('originalHTML'));
    });

    $('#supplierContainer').on('click', '.save-edit', function () {
        const row = $(this).closest('tr');
        const data = {
    action: 'edit',
    SupplierID: row.find('td').eq(0).text().trim(),
    SupplierName: row.find('input[name="SupplierName"]').val(),
    Address: row.find('input[name="Address"]').val(),
    Phone: row.find('input[name="Phone"]').val(),
    Email: row.find('input[name="Email"]').val()
};


        $.post('pages/supplier_load.php', data, function () {
            loadSuppliers();
        });
    });

    $('#supplierContainer').on('click', '.page-link', function () {
        const page = $(this).data('page');
        loadSuppliers(page);
    });
});
</script>






