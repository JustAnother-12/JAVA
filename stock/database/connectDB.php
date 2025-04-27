<?php
class connectDB {
    public static function getConnection() {
        $servername = "localhost";  
        $username = "root";
        $password = "";
        $database = "htttgame";

        // Kết nối MySQL
        $conn = mysqli_connect($servername, $username, $password, $database);
        
        // Kiểm tra lỗi kết nối
        if (!$conn) {
            die("Kết nối thất bại: " . mysqli_connect_error());
        }

        return $conn;
    }

    public static function closeConnection($conn) {
        if ($conn) {
            mysqli_close($conn);
        }
    }
}
?>
