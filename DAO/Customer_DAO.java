package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import DTO.KhachHang_DTO;

public class Customer_DAO extends JDialog{
    public void deleteCustomer(String id,DefaultTableModel tableModel,ArrayList<KhachHang_DTO> customerList) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkQuery = "SELECT COUNT(*) FROM KHACHHANG WHERE makh = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                String deleteQuery = "DELETE FROM KHACHHANG WHERE makh = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                pstmt.setString(1, id);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Xóa tài khoản khách hàng thành công!");
            }
            // Cập nhật lại bảng
            loadDataFormDatabase(customerList,tableModel);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa khách hàng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void loadDataFormDatabase(ArrayList<KhachHang_DTO> customerList,DefaultTableModel tableModel) {
        try (Connection conn = DatabaseConnection.getConnection()) {
                String queryforcs = "SELECT * FROM KHACHHANG";
                PreparedStatement pstmt = conn.prepareStatement(queryforcs);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    String id = rs.getString("makh");
                    String name = rs.getString("tenkh");
                    String username = rs.getString("username");
                    String phone = rs.getString("sdt");
                    KhachHang_DTO kh = new KhachHang_DTO(id, name, username, phone);
                    customerList.add(kh);
                    tableModel.addRow(new Object[]{id, name, username, phone,"Chi tiết" + "Xóa"});
                }
        } catch (Exception e) {}
    }
}
