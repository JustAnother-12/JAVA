<?php
include_once 'data.php';
$data = new Data();
session_start(); 
$currentUser = $_SESSION["username"]; 

// AJAX xử lý top khách hàng
if (isset($_GET['month'])) {
    $month = $_GET['month'];
    $top5User = $data->getTop5CustomersByMonth($month);
    echo json_encode($top5User);
    exit();
}

// AJAX xử lý top nhân viên
if (isset($_GET['topStaffMonth'])) {
    $month = $_GET['topStaffMonth'];
    $topStaff = $data->getTop5StaffByMonth($month);
    echo json_encode($topStaff);
    exit();
}

// AJAX xử lý top sản phẩm
if (isset($_GET['topProductMonth'])) {
    $month = $_GET['topProductMonth'];
    $topProducts = $data->getTop5ProductsByMonth($month);
    echo json_encode($topProducts);
    exit();
}

// Mặc định khi không có AJAX
$currentMonth = date('Y-m');
$top5User = $data->getTop5CustomersByMonth($currentMonth);
$top5Staff = $data->getTop5StaffByMonth($currentMonth);
$games = $data->getAllGame();
$users = $data->getAllUser();
$allHoaDon = $data->getAllHoaDon();
$daban = $data->getAllChiTietHoaDon();
$last6MonthSales = $data->getOrderStatsLast6Months();
$last6MonthImports = $data->getImportStatsLast6Months();
$last6MonthCount = $data->getLast6MonthsTotal();
?>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thống kê - Quản lí doanh nghiệp</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="style.css" rel="stylesheet">
</head>
<body class="bg-gray-100 min-h-screen">
<header class="header-container">
    <h1 class="header-title">
        <span class="header-label">Quản lí</span>
        <span class="header-username"><?php echo htmlspecialchars($currentUser); ?></span>
    </h1>
    <div class="menu-container">
        <div id="menuToggle" class="menu-toggle"><i class="fa-solid fa-bolt"></i></div>
        <div id="sideMenu" class="side-menu">
            <ul class="menu-list">
                <li><a href="../salesStaff/index.php" class="menu-item">Nhân viên</a></li>
                <li><a href="../stock/index.php" class="menu-item">Kho</a></li>
                <li><a href="../index.php" class="menu-item">Cửa hàng</a></li>
            </ul>
            <hr class="menu-divider">
            <a href="logout.php" class="logout-btn">Đăng xuất</a>
        </div>
    </div>
</header>
<div class="max-w-7xl mx-auto px-4" style="margin-bottom: 100px;">
    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <?php
        $stats = [
            'Game' => count($games),
            'Đơn hàng' => count($allHoaDon),
            'Khách hàng' => count($users),
            'Đã bán' => count($daban),
        ];
        foreach ($stats as $title => $count) {
            echo "
            <div class='bg-white shadow-md rounded-lg p-6 text-center'>
                <h3 class='text-lg font-semibold text-gray-700'>{$title}</h3>
                <div class='text-4xl font-bold text-blue-500 mt-2'>{$count}</div>
            </div>";
        }
        ?>
    </div>
    <div class="mb-4">
        <label for="ChartSelect" class="block text-gray-700 font-medium mb-2">Chọn bảng</label>
        <select id="ChartSelect" class="border border-gray-300 rounded-md p-2 w-full sm:w-1/2" style="width: 250px;">
            <option value="salesCount">Đơn hàng 6 tháng qua</option>
            <option value="salesTotal">Doanh thu 6 tháng qua</option>
            <option value="importTotal">Chi phí nhập kho 6 tháng qua</option>
            <option value="topCustomer">Top khách hàng</option>
            <option value="topStaff">Top nhân viên duyệt đơn</option>
            <option value="topProduct">Top sản phẩm bán chạy</option>
        </select>
        <div id="monthPickerWrapper" class="mb-4" style="display:none">
            <label for="TimeSelect" class="block text-gray-700 font-medium mb-2">Chọn tháng/năm</label>
            <input type="month" id="TimeSelect" class="border border-gray-300 rounded-md p-2 w-full sm:w-1/2" style="width: 250px;">
        </div>
    </div>
    <div class="bg-white p-6 rounded shadow">
        <canvas id="salesCountChart"></canvas>
        <canvas id="topCustomerChart" style="display: none;"></canvas>
        <canvas id="salesTotalChart" style="display: none;"></canvas>
        <canvas id="importTotalChart" style="display: none;"></canvas>
        <canvas id="topStaffChart" style="display: none;"></canvas>
        <canvas id="topProductChart" style="display: none;"></canvas>
    </div>
</div>
<script>
    const chartSelect = document.getElementById('ChartSelect');
    const charts = {
        salesCount: document.getElementById('salesCountChart'),
        topCustomer: document.getElementById('topCustomerChart'),
        salesTotal: document.getElementById('salesTotalChart'),
        importTotal: document.getElementById('importTotalChart'),
        topStaff: document.getElementById('topStaffChart'),
        topProduct: document.getElementById('topProductChart'),
    };
    const monthPickerWrapper = document.getElementById('monthPickerWrapper');
    const timeSelect = document.getElementById('TimeSelect');
    function hideAllCharts() {
        Object.values(charts).forEach(chart => chart.style.display = 'none');
    }
    chartSelect.addEventListener('change', function () {
        hideAllCharts();
        const selected = this.value;
        charts[selected].style.display = 'block';
        monthPickerWrapper.style.display = (['topCustomer', 'topStaff', 'topProduct'].includes(selected)) ? 'block' : 'none';
    });

    let topCustomerChart, topStaffChart, topProductChart;
    document.addEventListener('DOMContentLoaded', function () {
        const currentMonth = new Date().toISOString().slice(0, 7);
        timeSelect.value = currentMonth;
        hideAllCharts();
        charts.salesCount.style.display = 'block';

        new Chart(charts.salesCount, {
            type: 'bar',
            data: {
                labels: <?= json_encode(array_column($last6MonthCount, 'month')) ?>,
                datasets: [{
                    label: 'Số lượng đơn hàng theo tháng',
                    data: <?= json_encode(array_column($last6MonthCount, 'total_bill')) ?>,
                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            }
        });

        topCustomerChart = new Chart(charts.topCustomer, {
            type: 'bar',
            data: {
                labels: <?= json_encode(array_column($top5User, 'Fullname')) ?>,
                datasets: [{
                    label: 'Top khách hàng',
                    data: <?= json_encode(array_column($top5User, 'totalSpent')) ?>,
                    backgroundColor: 'rgba(255, 99, 132, 0.5)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1
                }]
            }
        });

        topStaffChart = new Chart(charts.topStaff, {
            type: 'bar',
            data: {
                labels: <?= json_encode(array_column($top5Staff, 'Fullname')) ?>,
                datasets: [{
                    label: 'Top nhân viên duyệt đơn',
                    data: <?= json_encode(array_column($top5Staff, 'totalApproved')) ?>,
                    backgroundColor: 'rgba(153, 102, 255, 0.5)',
                    borderColor: 'rgba(153, 102, 255, 1)',
                    borderWidth: 1
                }]
            }
        });

        topProductChart = new Chart(charts.topProduct, {
            type: 'bar',
            data: {
                labels: <?= json_encode(array_column($data->getTop5ProductsByMonth($currentMonth), 'ProductName')) ?>,
                datasets: [{
                    label: 'Top sản phẩm bán chạy',
                    data: <?= json_encode(array_column($data->getTop5ProductsByMonth($currentMonth), 'totalSold')) ?>,
                    backgroundColor: 'rgba(255, 159, 64, 0.5)',
                    borderColor: 'rgba(255, 159, 64, 1)',
                    borderWidth: 1
                }]
            }
        });

        new Chart(charts.salesTotal, {
            type: 'line',
            data: {
                labels: <?= json_encode(array_column($last6MonthSales, 'month')) ?>,
                datasets: [{
                    label: 'Doanh thu theo tháng',
                    data: <?= json_encode(array_column($last6MonthSales, 'total_sales')) ?>,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderWidth: 2,
                    tension: 0.4
                }]
            }
        });

        new Chart(charts.importTotal, {
            type: 'line',
            data: {
                labels: <?= json_encode(array_column($last6MonthImports, 'month')) ?>,
                datasets: [{
                    label: 'Chi phí nhập kho theo tháng',
                    data: <?= json_encode(array_column($last6MonthImports, 'total_import')) ?>,
                    borderColor: 'rgba(255, 206, 86, 1)',
                    backgroundColor: 'rgba(255, 206, 86, 0.2)',
                    borderWidth: 2,
                    tension: 0.4
                }]
            }
        });
    });

    timeSelect.addEventListener('change', function () {
        const selectedMonth = this.value;
        if (chartSelect.value === 'topCustomer') {
            fetch(`DN.php?month=${selectedMonth}`)
                .then(res => res.json())
                .then(data => {
                    topCustomerChart.data.labels = data.map(item => item.Fullname);
                    topCustomerChart.data.datasets[0].data = data.map(item => item.totalSpent);
                    topCustomerChart.update();
                });
        } else if (chartSelect.value === 'topStaff') {
            fetch(`DN.php?topStaffMonth=${selectedMonth}`)
                .then(res => res.json())
                .then(data => {
                    topStaffChart.data.labels = data.map(item => item.Fullname);
                    topStaffChart.data.datasets[0].data = data.map(item => item.totalApproved);
                    topStaffChart.update();
                });
        } else if (chartSelect.value === 'topProduct') {
            fetch(`DN.php?topProductMonth=${selectedMonth}`)
                .then(res => res.json())
                .then(data => {
                    topProductChart.data.labels = data.map(item => item.ProductName);
                    topProductChart.data.datasets[0].data = data.map(item => item.totalSold);
                    topProductChart.update();
                });
        }
    });
</script>
<script src="dn.js"></script>
</body>
</html>
