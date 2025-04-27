<?php
include_once '../../doanhnghiep/data.php';
$data = new Data();

$last6MonthSales = $data->getOrderStatsLast6Months();
$last6MonthCount = $data->getLast6MonthsTotal();
$allHoaDon = $data->getAllHoaDon();
$daban = $data->getAllChiTietHoaDon();
?>

<div class="chart-container">

    <div class="stat-container">
        <div class="select-container">
            <label for="ChartSelect" class="select-label">Chọn biểu đồ</label>
            <select id="ChartSelect" class="chart-select">
                <option value="salesCount">Đơn hàng 6 tháng qua</option>
                <option value="salesTotal">Doanh thu 6 tháng qua</option>
            </select>
        </div>
        <div class="stat-item">
            <h3 class="stat-title">Đơn hàng</h3>
            <div class="stat-value"><?= count($allHoaDon) ?></div>
        </div>
        <div class="stat-item">
            <h3 class="stat-title">Đã bán</h3>
            <div class="stat-value"><?= count($daban) ?></div>
        </div>

        
    </div>

    <div class="chart-wrapper">
        <canvas id="salesCountChart"></canvas>
        <canvas id="salesTotalChart" style="display: none;"></canvas>
    </div>

</div>


<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
    const chartSelect = document.getElementById('ChartSelect');
    const charts = {
        salesCount: document.getElementById('salesCountChart'),
        salesTotal: document.getElementById('salesTotalChart'),
    };

    function hideAllCharts() {
        Object.values(charts).forEach(chart => chart.style.display = 'none');
    }

    chartSelect.addEventListener('change', function () {
        hideAllCharts();
        const selected = this.value;
        charts[selected].style.display = 'block';
    });

    document.addEventListener('DOMContentLoaded', function () {
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
    });
</script>

<style>
.chart-container {
    padding: 70px 10%;
}
.stat-container {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 20px;
}
.stat-item {
    background-color: white;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    flex: 1; 
    margin: 0 10px;
}
.stat-title {
    font-size: 18px;
    font-weight: 600;
    color: #4A4A4A;
}
.stat-value {
    font-size: 36px;
    font-weight: 700;
    color: #1E40AF; 
    margin-top: 10px;
}
.select-container {
    width: 50%;

}
.select-label {
    font-size: 14px;
    color: #4A4A4A;
    margin-bottom: 8px;
    display: block;
}
.chart-select {
    border: 1px solid #D1D5DB;
    border-radius: 8px;
    padding: 8px 16px;
    width: 250px;
    font-size: 16px;
}
.chart-wrapper {
    background-color: white;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>