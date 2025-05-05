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
