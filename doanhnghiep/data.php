<?php
class Data {
    private $conn;

    public function __construct() {
        $servername = "localhost";
        $username = "root";
        $password = "";
        $dbname = "htttgame";
        $this->conn = new mysqli($servername, $username, $password, $dbname);
        if ($this->conn->connect_error) {
            die("Kết nối thất bại: " . $this->conn->connect_error);
        }
    }

    public function getAllGame() {
        $sql = "SELECT * FROM product";
        $result = $this->conn->query($sql);
        $products = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $products[] = $row;
            }
        }
        return $products;
    }

    public function getAllUser() {
        $sql = "SELECT * FROM account";
        $result = $this->conn->query($sql);
        $users = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $users[] = $row;
            }
        }
        return $users;
    }

    public function getAllHoaDon() {
        $sql = "SELECT * FROM sales_invoice";
        $result = $this->conn->query($sql);
        $hoadon = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $hoadon[] = $row;
            }
        }
        return $hoadon;
    }

    public function getAllChiTietHoaDon() {
        $sql = "SELECT * FROM detail_sales_invoice";
        $result = $this->conn->query($sql);
        $data = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
        return $data;
    }

    public function getLast6MonthsTotal() {
        $sql = "
            SELECT DATE_FORMAT(Date, '%Y-%m') AS month, COUNT(*) AS total_bill
            FROM sales_invoice
            WHERE Date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
            GROUP BY month
            ORDER BY month ASC
        ";
        $result = $this->conn->query($sql);
        $data = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
        return $data;
    }

    public function getTop5Customers() {
        $sql = "
            SELECT u.Fullname, SUM(h.TotalPrice) AS totalSpent
            FROM customer u
            JOIN sales_invoice h ON u.CustomerID = h.CustomerID
            GROUP BY u.CustomerID
            ORDER BY totalSpent DESC
            LIMIT 5
        ";
        $result = $this->conn->query($sql);
        $data = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
        return $data;
    }

    public function getTop5CustomersByMonth($month) {
        $sql = "
            SELECT u.Fullname, SUM(h.TotalPrice) AS totalSpent
            FROM customer u
            JOIN sales_invoice h ON u.CustomerID = h.CustomerID
            WHERE DATE_FORMAT(h.Date, '%Y-%m') = ?  
            GROUP BY u.CustomerID
            ORDER BY totalSpent DESC
            LIMIT 5
        ";
        $stmt = $this->conn->prepare($sql);
        $stmt->bind_param("s", $month); 
        $stmt->execute();
        $result = $stmt->get_result();
        $data = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
        return $data;
    }

    public function getImportStatsLast6Months() {
        $sql = "
            SELECT DATE_FORMAT(Date, '%Y-%m') AS month, 
                   SUM(TotalPrice) AS total_import
            FROM import_invoice
            WHERE Date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
            GROUP BY month
            ORDER BY month ASC
        ";
        $result = $this->conn->query($sql);
        $data = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
        return $data;
    }

    public function getOrderStatsLast6Months() {
        $sql = "
            SELECT DATE_FORMAT(Date, '%Y-%m') AS month, 
                   SUM(TotalPrice) AS total_sales
            FROM sales_invoice
            WHERE Date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
            GROUP BY month
            ORDER BY month ASC
        ";
        $result = $this->conn->query($sql);
        $data = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
        return $data;
    }

    public function getTop5StaffByMonth($month) {
        $sql = "
            SELECT u.Fullname, COUNT(h.EmployeeID) AS totalApproved
            FROM employee u
            JOIN sales_invoice h ON u.EmployeeID = h.EmployeeID 
            WHERE DATE_FORMAT(h.Date, '%Y-%m') = ?   
            GROUP BY u.EmployeeID
            ORDER BY totalApproved DESC
            LIMIT 5
        ";
        $stmt = $this->conn->prepare($sql);
        $stmt->bind_param("s", $month); 
        $stmt->execute();
        $result = $stmt->get_result();
        $data = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
        return $data;
    }

    public function getTop5ProductsByMonth($month) {
        $sql = "
            SELECT g.ProductName AS ProductName, SUM(cthd.Quantity) AS totalSold
            FROM sales_invoice h
            JOIN detail_sales_invoice cthd ON h.SalesID = cthd.SalesID
            JOIN product g ON cthd.ProductID = g.ProductID
            WHERE DATE_FORMAT(h.Date, '%Y-%m') = ?
            GROUP BY g.ProductID
            ORDER BY totalSold DESC
            LIMIT 5
        ";
        $stmt = $this->conn->prepare($sql);
        $stmt->bind_param("s", $month);
        $stmt->execute();
        $result = $stmt->get_result();
        $data = [];
        if ($result && $result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                $data[] = $row;
            }
        }
        return $data;
    }

    public function __destruct() {
        $this->conn->close();
    }
}
?>
