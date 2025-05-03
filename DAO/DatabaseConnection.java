package DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ban_van_phong_pham"; // Địa chỉ cơ sở dữ liệu
    private static final String USER = "root"; // Tên người dùng
    private static final String PASS = "123456789";
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