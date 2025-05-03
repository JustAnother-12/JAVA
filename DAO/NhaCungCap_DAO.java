package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;

public class NhaCungCap_DAO {
    private Connection con;

    public boolean OpenConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ban_van_phong_pham", "root", "");
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Lấy danh sách tất cả nhà cung cấp
    public ArrayList<NhaCungCap_DTO> getAllNCC() {
        ArrayList<NhaCungCap_DTO> arr = new ArrayList<>();
        if (OpenConnection()) {
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM NHACUNGCAP");
                while (rs.next()) {
                    String id = rs.getString("mancc");
                    String ten = rs.getString("tenncc");
                    String sdt = rs.getString("sdt");
                    String emailkh = rs.getString("email");
                    NhaCungCap_DTO ncc = new NhaCungCap_DTO(id, ten, sdt, emailkh);
                    arr.add(ncc);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closeConnection();
            }
        }
        return arr;
    }

    // Thêm nhà cung cấp
    public boolean insertNCC(NhaCungCap_DTO ncc) {
        boolean result = false;
        if (OpenConnection()) {
            try {
                String sql = "INSERT INTO NHACUNGCAP(mancc, tenncc, sdt, email) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, ncc.getMaNCC());
                stmt.setString(2, ncc.getTenNCC());
                stmt.setString(3, ncc.getSdtNCC());
                stmt.setString(4, ncc.getEmailNCC());

                result = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closeConnection();
            }
        }
        return result;
    }

    // Cập nhật nhà cung cấp
    public boolean updateNCC(NhaCungCap_DTO ncc) {
        boolean result = false;
        if (OpenConnection()) {
            try {
                String sql = "UPDATE NHACUNGCAP SET tenncc = ?, sdt = ?, email = ? WHERE mancc = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, ncc.getTenNCC());
                stmt.setString(2, ncc.getSdtNCC());
                stmt.setString(3, ncc.getEmailNCC());
                stmt.setString(4, ncc.getMaNCC());

                result = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closeConnection();
            }
        }
        return result;
    }

    // Xóa nhà cung cấp
    public boolean deleteNCC(String maNCC) {
        boolean result = false;
        if (OpenConnection()) {
            try {
                String sql = "DELETE FROM NHACUNGCAP WHERE mancc = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, maNCC);

                result = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closeConnection();
            }
        }
        return result;
    }
}


