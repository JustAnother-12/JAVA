package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DTO.NhanVien_DTO;

public class ThemNhanVien_DAO {
    
    public boolean addStaff(NhanVien_DTO nv) {
        String insertQuery = "INSERT INTO NHANVIEN (manv, tennv, chucvu, sdt, username, passwordnv, diachinv, CCCD, gioitinh, ngaysinh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
             
            pstmt.setString(1, nv.getManv());
            pstmt.setString(2, nv.getTennv());
            pstmt.setString(3, nv.getChucvu());
            pstmt.setString(4, nv.getSdt());
            pstmt.setString(5, nv.getUsername());
            pstmt.setString(6, nv.getPassword());
            pstmt.setString(7, nv.getDiachi());
            pstmt.setString(8, nv.getCCCD());
            pstmt.setString(9, nv.getGioitinh());
            pstmt.setString(10, nv.getNgaysinh());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getNextNVID() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String latestID = "";
            try {
                String sql = "SELECT manv FROM NHANVIEN ORDER BY manv DESC LIMIT 1";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    latestID = rs.getString("manv");
                }
            } catch (SQLException e) {
                System.out.println("Lỗi khi truy vấn mã nhân viên: " + e.getMessage());
            }
    
            String prefix = latestID.replaceAll("\\d+", "");
            String numberic = latestID.replaceAll("[^\\d]", "");
    
            try {
                int number = Integer.parseInt(numberic);
                number++;
                String nextnumberic = String.format("%03d", number);
                return (prefix + nextnumberic).replace(" ", "");
            } catch (NumberFormatException e) {
                System.out.println("Lỗi định dạng mã nhân viên trong CSDL: " + latestID);
                return "NV001"; // fallback nếu định dạng sai
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối database: " + e.getMessage());
        }
        return null;
    }
        public boolean isIDExists(String id) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM NHANVIEN WHERE manv = ?";
                PreparedStatement pstmt = conn.prepareStatement(checkQuery);
                pstmt.setString(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu có ít nhất 1 bản ghi thì ID đã tồn tại
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false; // ID không tồn tại
        }
        public boolean isSDTExists(String sdt) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM NHANVIEN WHERE sdt = ?";
                PreparedStatement pstmt = conn.prepareStatement(checkQuery);
                pstmt.setString(1, sdt);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu có ít nhất 1 bản ghi thì ID đã tồn tại
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false; // ID không tồn tại
        }
        public boolean isUserNameExists(String username) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM NHANVIEN WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(checkQuery);
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu có ít nhất 1 bản ghi thì ID đã tồn tại
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false; // ID không tồn tại
        }
        public boolean isCCCDExists(String cccd) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM NHANVIEN WHERE CCCD = ?";
                PreparedStatement pstmt = conn.prepareStatement(checkQuery);
                pstmt.setString(1, cccd);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu có ít nhất 1 bản ghi thì ID đã tồn tại
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false; // ID không tồn tại
        }
}
