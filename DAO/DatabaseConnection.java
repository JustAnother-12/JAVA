package DAO;
import java.util.ArrayList;
import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ban_van_phong_pham"; // Địa chỉ cơ sở dữ liệu
    private static final String USER = "root"; // Tên người dùng
    private static final String PASS = "123456789";

    public static Connection OpenConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void closeConnection(Connection con) {
        try {
            if (con != null){
                con.close();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,USER,PASS);
    }
    public static ArrayList<String> getAllRoles() {
        ArrayList<String> chucvu = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String query = "SELECT DISTINCT chucvu FROM NHANVIEN";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                chucvu.add(rs.getString("chucvu"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    return chucvu;
}
}