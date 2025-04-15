package DAO;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.*;
import DTO.NhanVien_DTO;

public class NhanVien_DAO extends javax.swing.JPanel {
    public void filterByRole(String role, DefaultTableModel tableModel,ArrayList<NhanVien_DTO> accountList) {
        tableModel.setRowCount(0); // Clear bảng
        accountList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = role.equals("Tất cả")
                ? "SELECT * FROM NHANVIEN"
                : "SELECT * FROM NHANVIEN WHERE chucvu = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            if (!role.equals("Tất cả")) {
                pstmt.setString(1, role);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("manv");
                String name = rs.getString("tennv");
                String username = rs.getString("username");
                String phone = rs.getString("sdt");
                NhanVien_DTO nv = new NhanVien_DTO(id, name, username, phone);
                accountList.add(nv);
                tableModel.addRow(new Object[]{id, name, username, phone, "Chi tiết" + "Xoá"});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void loadDataFormDatabase(DefaultTableModel tableModel,ArrayList<Object> accountList) {
        try (Connection conn = DatabaseConnection.getConnection()) {
                String queryfors = "SELECT * FROM NHANVIEN";
                PreparedStatement pstmt = conn.prepareStatement(queryfors);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    String id = rs.getString("manv");
                    String name = rs.getString("tennv");
                    String username = rs.getString("username");
                    String phone = rs.getString("sdt");
                    NhanVien_DTO nv = new NhanVien_DTO(id, name, username, phone);
                    accountList.add(nv);
                    tableModel.addRow(new Object[]{id, name, username, phone, "Chi tiết" + "Xoá"});
                }
        } catch (Exception e) {}
    }
    public void deleteAccount(String id, DefaultTableModel tableModel,ArrayList<Object> accountList) {
        try (Connection conn = DatabaseConnection.getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM NHANVIEN WHERE manv = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, id);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Nếu ID là của nhân viên
                    String deleteQuery = "DELETE FROM NHANVIEN WHERE manv = ?";
                    PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                    pstmt.setString(1, id);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa tài khoản nhân viên thành công!");
                } else {
                    // Nếu không tìm thấy ID trong cả hai bảng
                    JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản với ID: " + id, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            // Cập nhật lại bảng
            loadDataFormDatabase(tableModel, accountList);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa tài khoản: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
