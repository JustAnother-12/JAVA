package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import GUI.Admin.swing.CheckFailInput_BLL;

public class ThemNhanVien_DAO extends javax.swing.JDialog {
    public void addStaff(JTextField txtName,JComboBox<String> txtPosition,JTextField txtPhone,JTextField txtUsername,JTextField txtPassword,JTextField txtAddress,JTextField txtCCCD,JTextField txtBirthday,JComboBox<String> cbGender,DefaultTableModel tableModel,HashSet<String> existingIDs) {
            String name = txtName.getText();
            String position = (String) txtPosition.getSelectedItem();
            // if (position.equals("Chọn chức vụ")) {
            //     JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            //     return;
            // }
            String phone = txtPhone.getText();
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String address = txtAddress.getText();
            String cccd = txtCCCD.getText();
            String gender = (String) cbGender.getSelectedItem();
            String birthday = txtBirthday.getText();
            CheckFailInput_BLL themnv = new CheckFailInput_BLL();
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = inputFormat.parse(birthday);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ! Vui lòng nhập theo định dạng dd/MM/yyyy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String formattedDate = outputFormat.format(date);
            // Kiểm tra ràng buộc
            if (themnv.isValidName(name)) {
                JOptionPane.showMessageDialog(this, "Họ tên phải là chữ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (gender == null) {
                JOptionPane.showMessageDialog(this, "Giới tính phải chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!themnv.isNumeric(phone) || phone.length() > 12) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải là số và chỉ tối đa 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!themnv.isNumeric(cccd) || cccd.length() != 12) {
                JOptionPane.showMessageDialog(this, "Căn cước phải là 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!themnv.isValidDate(birthday)) {
                JOptionPane.showMessageDialog(this, "Ngày sinh phải theo định dạng DD/MM/YYYY!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Tạo ID ngẫu nhiên
            String id;
            do {
                id = themnv.generateRandomID();
            } while (isIDExists(id));
            if (isSDTExists(phone)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại đã tòn tại. Vui lòng nhập lại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isCCCDExists(cccd)) {
                JOptionPane.showMessageDialog(this, "CCCD đã tòn tại. Vui lòng nhập lại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (isUserNameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username đã tòn tại. Vui lòng nhập lại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                String insertQuery = "INSERT INTO NHANVIEN (manv,tennv, chucvu, sdt, username, passwordnv, diachinv, CCCD, gioitinh, ngaysinh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1, id); // Thêm ID ngẫu nhiên vào cơ sở dữ liệu
                pstmt.setString(2, name);
                pstmt.setString(3, position);
                pstmt.setString(4, phone);
                pstmt.setString(5, username);
                pstmt.setString(6, password);
                pstmt.setString(7, address);
                pstmt.setString(8, cccd);
                pstmt.setString(9, gender);
                pstmt.setString(10, formattedDate);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
    
            // Thêm dữ liệu vào bảng
            tableModel.addRow(new Object[]{id, name, username, phone, "Chi tiết", "Xóa"});
            existingIDs.add(id); // Thêm ID vào set
    
            // Đóng form
            dispose();
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
