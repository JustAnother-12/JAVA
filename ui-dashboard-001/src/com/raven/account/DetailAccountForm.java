/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Beans/Customizer.java to edit this template
 */
package com.raven.account;
import com.raven.account.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.raven.account.customer;
import com.raven.account.staff;
import java.sql.*;
/**
 *
 * @author ADMIN
 */

public class DetailAccountForm extends JDialog {
    private JTextField txtName, txtPhone, txtUsername, txtAddress, txtBirthday;
    private JTextField txtEmail; // Chỉ dành cho customer
    private JTextField txtPosition; // Chỉ dành cho staff
    private JComboBox<String> cbGender;
    private JTextField txtCCCD; // Chỉ dành cho staff
    private JButton btnEdit, btnSave;
    private boolean isCustomer;
    public DetailAccountForm(customer kh) throws ParseException {
        isCustomer = true;
        setTitle("Chi tiết Khách Hàng");
        setSize(400, 400);
        setLayout(new GridLayout(8, 2));
        customer a = getDataOfCustomer(kh);
        // Tạo các trường nhập liệu cho khách hàng
        txtName = new JTextField(a.getTenkh());
        txtPhone = new JTextField(a.getSdt());
        txtUsername = new JTextField(a.getUsername());
        txtAddress = new JTextField(a.getDiachi());
        txtBirthday = new JTextField(a.getNgaysinh());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String birthday = txtBirthday.getText();
        Date date = inputFormat.parse(birthday);
        String formattedDate = outputFormat.format(date);
        txtBirthday.setText(formattedDate);
        txtEmail = new JTextField(a.getEmail()); // Thêm trường email
        // Tạo JComboBox cho giới tính
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ"}); // Các giá trị có thể có
        cbGender.setSelectedItem(a.getGioi());
        setEditable(false); // Không cho phép chỉnh sửa ban đầu
// Thêm các trường vào form
        add(new JLabel("Họ tên:"));
        add(txtName);
        add(new JLabel("Số Điện thoại:"));
        add(txtPhone);
        add(new JLabel("Username:"));
        add(txtUsername);
        add(new JLabel("Địa chỉ:"));
        add(txtAddress);
        add(new JLabel("Ngày sinh:"));
        add(txtBirthday);
        add(new JLabel("Email:")); // Thêm nhãn cho email
        add(txtEmail); // Thêm trường email
        add(new JLabel("Gới tính:")); // Thêm nhãn cho email
        add(cbGender);
        // Tạo nút chỉnh sửa và lưu
        btnEdit = new JButton("Chỉnh sửa");
        btnSave = new JButton("Lưu");
        btnSave.setVisible(false); // Ẩn nút lưu ban đầu

        add(btnEdit);
        add(btnSave);

        // Thêm sự kiện cho nút Chỉnh sửa
        btnEdit.addActionListener(e -> {
            setEditable(true);
            btnEdit.setVisible(false);
            btnSave.setVisible(true);
        });

        // Thêm sự kiện cho nút Lưu
        btnSave.addActionListener(e -> {
            // Cập nhật thông tin
            if (validateFields()) {
                try {
                    updateCustomer(kh);
                    dispose();
                } catch (ParseException ex) {
                    Logger.getLogger(DetailAccountForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
        dispose();
    }
    private customer getDataOfCustomer(customer nv) {
        customer a = null; // Khởi tạo a với giá trị null
        String query = "SELECT * FROM KHACHHANG WHERE makh = ?"; // Truy vấn để lấy dữ liệu

        try (Connection conn = DatabaseConnection.getConnection(); // Kết nối đến cơ sở dữ liệu
             PreparedStatement pstmt = conn.prepareStatement(query)) {
         
            pstmt.setString(1, nv.getMakh()); // Thiết lập ID của khách hàng
            ResultSet rs = pstmt.executeQuery(); // Thực thi truy vấn

            if (rs.next()) { // Nếu có kết quả
                // Tạo đối tượng customer từ kết quả truy vấn
                a = new customer(
                    rs.getString("makh"),
                    rs.getString("tenkh"),
                    rs.getString("sdt"),
                    rs.getString("gioi"),
                    rs.getString("email"),
                    rs.getString("ngaysinh"),
                    rs.getString("diachikh"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }   

        return a; // Trả về đối tượng customer hoặc null nếu không tìm thấy
    }
    // Constructor cho nhân viên
    public DetailAccountForm(staff nv) throws ParseException {
        isCustomer = false;
        setTitle("Chi tiết Nhân Viên");
        setSize(400, 400);
        setLayout(new GridLayout(9, 2));
        staff a = getDataOfStaff(nv);
        // Tạo các trường nhập liệu cho nhân viên
        txtName = new JTextField(a.getTennv());
        txtPhone = new JTextField(a.getSdt());
        txtUsername = new JTextField(a.getUsername());
        txtAddress = new JTextField(a.getDiachi());
        txtBirthday = new JTextField(a.getNgaysinh());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String birthday = txtBirthday.getText();
        Date date = inputFormat.parse(birthday);
        String formattedDate = outputFormat.format(date);
        txtBirthday.setText(formattedDate);
        txtPosition = new JTextField(a.getChucvu()); // Thêm trường chức vụ
        txtCCCD = new JTextField(a.getCCCD()); // Thêm trường CCCD
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ"}); // Các giá trị có thể có
        cbGender.setSelectedItem(a.getGioitinh());
        setEditable(false); // Không cho phép chỉnh sửa ban đầu
        // Thêm các trường vào form
        add(new JLabel("Họ tên:"));
        add(txtName);
        add(new JLabel("Số Điện thoại:"));
        add(txtPhone);
        add(new JLabel("Username:"));
        add(txtUsername);
        add(new JLabel("Địa chỉ:"));
        add(txtAddress);
        add(new JLabel("Ngày sinh:"));
        add(txtBirthday);
        add(new JLabel("Chức vụ:")); // Thêm nhãn cho chức vụ
        add(txtPosition); // Thêm trường chức vụ
        add(new JLabel("CCCD:")); // Thêm nhãn cho CCCD
        add(txtCCCD); // Thêm trường CCCD
        add(new JLabel("Giới tính:")); // Thêm nhãn cho CCCD
        add(cbGender);
        // Tạo nút chỉnh sửa và lưu
        btnEdit = new JButton("Chỉnh sửa");
        btnSave = new JButton("Lưu");
        btnSave.setVisible(false); // Ẩn nút lưu ban đầu

        add(btnEdit);
        add(btnSave);

        // Thêm sự kiện cho nút Chỉnh sửa
        btnEdit.addActionListener(e -> {
            setEditable(true);
            btnEdit.setVisible(false);
            btnSave.setVisible(true);
        });

        // Thêm sự kiện cho nút Lưu
        btnSave.addActionListener(e -> {
            // Cập nhật thông tin
            if (validateFields()) {
                try {
                    updateStaff(nv);
                    dispose();
                } catch (ParseException ex) {
                    Logger.getLogger(DetailAccountForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
        dispose();
    }
    private staff getDataOfStaff(staff nv) {
        staff a = null; // Khởi tạo a với giá trị null
        String query = "SELECT * FROM NHANVIEN WHERE manv = ?"; // Truy vấn để lấy dữ liệu

        try (Connection conn = DatabaseConnection.getConnection(); // Kết nối đến cơ sở dữ liệu
             PreparedStatement pstmt = conn.prepareStatement(query)) {
         
            pstmt.setString(1, nv.getManv()); // Thiết lập ID của khách hàng
            ResultSet rs = pstmt.executeQuery(); // Thực thi truy vấn

            if (rs.next()) { // Nếu có kết quả
                // Tạo đối tượng customer từ kết quả truy vấn
                a = new staff(
                    rs.getString("manv"),
                    rs.getString("tennv"),
                    rs.getString("chucvu"),
                    rs.getString("sdt"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("diachinv"),
                    rs.getString("CCCD"),
                    rs.getString("gioitinh"),
                    rs.getString("ngaysinh")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }   

        return a; // Trả về đối tượng customer hoặc null nếu không tìm thấy
    }
    private void setEditable(boolean editable) {
        txtName.setEnabled(editable);
        txtPhone.setEnabled(editable);
        txtUsername.setEnabled(editable);
        txtAddress.setEnabled(editable);
        txtBirthday.setEnabled(editable);
        cbGender.setEnabled(editable);
        if (isCustomer) {
            txtEmail.setEnabled(editable); // Chỉ cho phép chỉnh sửa email nếu là khách hàng
        } else {
            txtPosition.setEnabled(editable); // Chỉ cho phép chỉnh sửa chức vụ nếu là nhân viên
            txtCCCD.setEnabled(editable); // Chỉ cho phép chỉnh sửa CCCD nếu là nhân viên
        }
    }
    private boolean validateFields() {
        if (!isValidName(txtName.getText())) {
            JOptionPane.showMessageDialog(this, "Họ tên phải là chữ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isNumeric(txtPhone.getText()) || txtPhone.getText().length() > 12) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là số và chỉ tối đa 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (isCustomer && !isValidEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidDate(txtBirthday.getText())) {
            JOptionPane.showMessageDialog(this, "Ngày sinh phải theo định dạng DD/MM/YYYY!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isCustomer) {
            if (!isValidName(txtPosition.getText())) {
                JOptionPane.showMessageDialog(this, "Chức vụ phải là chữ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!isNumeric(txtCCCD.getText()) || txtCCCD.getText().length() != 12) {
                JOptionPane.showMessageDialog(this, "Căn cước phải là 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true; 
    }
    private void updateCustomer(customer kh) throws ParseException{
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
    private void updateStaff(staff nv) throws ParseException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE NHANVIEN SET tennv = ?, sdt = ?, username = ?, diachinv = ?, ngaysinh = ?, chucvu = ?, CCCD = ? WHERE username = ?";
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
            pstmt.setString(6, txtPosition.getText());
            pstmt.setString(7, txtCCCD.getText());
            pstmt.setString(8, nv.getUsername()); // Sử dụng username để xác định bản ghi
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin nhân viên thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]+"); // Kiểm tra chỉ chứa chữ và khoảng trắng
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+"); // Kiểm tra chỉ chứa số
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$"); // Kiểm tra định dạng email
    }

    private boolean isValidDate(String date) {
        return date.matches("\\d{2}/\\d{2}/\\d{4}"); // Kiểm tra định dạng DD/MM/YYYY
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.BorderLayout());

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
