<?php
    class connectDB{
        public static function getConnection(){
            $servername = "localhost:3306";  
            $username = "root";        
            $password = "";             
            $database = "htttgame"; 

            try{
                // Kết nối MySQL
                $conn = mysqli_connect($servername, $username, $password, $database);
            
                // Kiểm tra lỗi kết nối
                if ($conn->connect_error) 
                    die("Kết nối thất bại: " . $conn->connect_error);
            } catch(mysqli_sql_exception $e){
                die("Lỗi kết nối: " . $e->getMessage());
            }
            
            return $conn;
        }

        public static function closeConnection($conn){
            if($conn!=null)
                mysqli_close($conn);
        }
    }
?>