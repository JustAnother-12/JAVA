package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import DTO.KhachHang_DTO;
import DTO.NhanVien_DTO;

public class ChiTietThongTinTaiKhoan_DAO extends javax.swing.JDialog {
    public void updateCustomer(KhachHang_DTO kh,JTextField txtName,JTextField txtPhone,JTextField txtUsername,JTextField txtAddress,JTextField txtBirthday,JTextField txtEmail) throws ParseException{
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE KHACHHANG SET tenkh = ?, sdt = ?, username = ?, diachikh = ?, ngaysinh = ?, email = ? WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, txtPhone.getText());
            pstmt.setString(3, txtUsername.getText());
            pstmt.setString(4, txtAddress.getText());
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String birthday = txtBirthday.getText();
            Date date = inputFormat.parse(birthday);
            String formattedDate = outputFormat.format(date);
            pstmt.setString(5, formattedDate);
            pstmt.setString(6, txtEmail.getText());
            pstmt.setString(7, kh.getUsername()); 
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công!");      
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void updateStaff(NhanVien_DTO nv,JTextField txtName,JTextField txtPhone,JTextField txtUsername,JTextField txtAddress,JTextField txtBirthday,JTextField txtPosition,JTextField txtCCCD,JComboBox<String> cbGender) throws ParseException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE NHANVIEN SET tennv = ?, sdt = ?, username = ?, diachinv = ?,gioitinh = ?, ngaysinh = ?, chucvu = ?, CCCD = ? WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, txtPhone.getText());
            pstmt.setString(3, txtUsername.getText());
            pstmt.setString(4, txtAddress.getText());
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String birthday = txtBirthday.getText();
            Date date = inputFormat.parse(birthday);
            String formattedDate = outputFormat.format(date);
            pstmt.setString(5, formattedDate);
            pstmt.setString(6, (String) cbGender.getSelectedItem());
            pstmt.setString(7, txtPosition.getText());
            pstmt.setString(8, txtCCCD.getText());
            pstmt.setString(9, nv.getUsername()); // Sử dụng username để xác định bản ghi
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhân viên thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
