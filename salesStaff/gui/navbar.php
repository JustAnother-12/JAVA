<?php
    $currentPage = isset($_GET['page']) ? $_GET['page'] : "home";

    echo '<div class="row mt-4 non-printable">
            <!-- Quản lý đơn hàng -->
            <div class="col-md-3">
                <a href="index.php?page=order_management" class="text-decoration-none">
                    <div class="card text-center p-4 ' . ($currentPage == "order_management" ? "active" : "") . '">
                        <div class="icon">📦</div>
                        <h5 class="mt-3">Quản lý đơn hàng</h5>
                    </div>
                </a>
            </div>

            <!-- In hóa đơn -->
            <div class="col-md-3">
                <a href="index.php?page=print_invoice" class="text-decoration-none">
                    <div class="card text-center p-4 ' . ($currentPage == "print_invoice" ? "active" : "") . '">
                        <div class="icon">🖨️</div>
                        <h5 class="mt-3">In hóa đơn</h5>
                    </div>
                </a>
            </div>

            <!-- Xem hóa đơn -->
            <div class="col-md-3">
                <a href="index.php?page=view_invoice" class="text-decoration-none">
                    <div class="card text-center p-4 ' . ($currentPage == "view_invoice" ? "active" : "") . '">
                        <div class="icon">📜</div>
                        <h5 class="mt-3">Xem hóa đơn</h5>
                    </div>
                </a>
            </div>

            <!-- Xem thống kê -->
            <div class="col-md-3">
                <a href="index.php?page=view_statistics" class="text-decoration-none">
                    <div class="card text-center p-4 ' . ($currentPage == "view_statistics" ? "active" : "") . '">
                        <div class="icon">📊</div>
                        <h5 class="mt-3">Xem thống kê</h5>
                    </div>
                </a>
            </div>
        </div>';
?>