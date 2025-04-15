/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Beans/Customizer.java to edit this template
 */
package com.raven.account;
import javax.swing.*;
import com.raven.account.database.DatabaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.sql.ResultSet;
import java.util.*;
import java.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ADMIN
 */
public class AddAccount extends javax.swing.JDialog {
    private JTextField txtName, txtPosition, txtPhone, txtUsername, txtPassword, txtAddress, txtCCCD, txtBirthday;
    private JButton btnSave, btnCancel;
    private JComboBox<String> cbGender;
    private DefaultTableModel tableModel;
    
    private HashSet<String> existingIDs; 
    public AddAccount(DefaultTableModel tableModel) {
        initComponents();
        this.tableModel = tableModel;
        existingIDs = new HashSet<>();
        setTitle("Thêm Nhân Viên");
        setSize(400, 400);
        setLayout(new GridLayout(10, 2));

        // Tạo các trường nhập liệu
        add(new JLabel("Họ tên:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("Chức vụ:"));
        txtPosition = new JTextField();
        txtPosition.setText("Chọn chức vụ");
        txtPosition.setEditable(false);
        JPopupMenu popupMenu = new JPopupMenu();
        String[] positions = {
            "Quản lý kho", "Quản lý khách hàng", "Quản lý nhân viên", "Quản lý đơn hàng"
        };

        for (String pos : positions) {
            JMenuItem item = new JMenuItem(pos);
            item.addActionListener(e -> txtPosition.setText(pos));
            popupMenu.add(item);
        }
        txtPosition.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                popupMenu.show(txtPosition, 0, txtPosition.getHeight());
            }
        });
        add(txtPosition);

        add(new JLabel("Số Điện thoại:"));
        txtPhone = new JTextField();
        add(txtPhone);

        add(new JLabel("Username:"));
        txtUsername = new JTextField();
        add(txtUsername);

        add(new JLabel("Password:"));
        txtPassword = new JTextField();
        add(txtPassword);

        add(new JLabel("Địa chỉ:"));
        txtAddress = new JTextField();
        add(txtAddress);

        add(new JLabel("CCCD:"));
        txtCCCD = new JTextField();
        add(txtCCCD);

        add(new JLabel("Giới tính:"));
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ"}); // JComboBox cho giới tính
        add(cbGender);

        add(new JLabel("Ngày sinh:"));
        txtBirthday = new JTextField();
        add(txtBirthday);

        // Tạo nút lưu và hủy
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        add(btnSave);
        add(btnCancel);

        // Thêm sự kiện cho nút Lưu
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStaff();
            }
        });

        // Thêm sự kiện cho nút Hủy
        btnCancel.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
    }
    private void loadExistingIDs() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            existingIDs.add(tableModel.getValueAt(i, 0).toString()); // Giả sử ID ở cột 0
        }
    }
    private void addStaff() {
        String name = txtName.getText();
        String position = txtPosition.getText();
        if (position.equals("Chọn chức vụ")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String phone = txtPhone.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String address = txtAddress.getText();
        String cccd = txtCCCD.getText();
        String gender = (String) cbGender.getSelectedItem();
        String birthday = txtBirthday.getText();
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
        if (!isValidName(name)) {
            JOptionPane.showMessageDialog(this, "Họ tên phải là chữ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidName(position)) {
            JOptionPane.showMessageDialog(this, "Chức vụ phải là chữ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (gender == null) {
            JOptionPane.showMessageDialog(this, "Giới tính phải chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isNumeric(phone) || phone.length() > 12) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là số và chỉ tối đa 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isNumeric(cccd) || cccd.length() != 12) {
            JOptionPane.showMessageDialog(this, "Căn cước phải là 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidDate(birthday)) {
            JOptionPane.showMessageDialog(this, "Ngày sinh phải theo định dạng DD/MM/YYYY!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Tạo ID ngẫu nhiên
        String id;
        do {
            id = generateRandomID();
        } while (isIDExists(id));
        try (Connection conn = DatabaseConnection.getConnection()) {
            String insertQuery = "INSERT INTO NHANVIEN (manv,tennv, chucvu, sdt, username, password, diachinv, CCCD, gioitinh, ngaysinh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]+"); // Kiểm tra chỉ chứa chữ và khoảng trắng
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+"); // Kiểm tra chỉ chứa số
    }

    private boolean isValidDate(String date) {
        return date.matches("\\d{2}/\\d{2}/\\d{4}"); // Kiểm tra định dạng DD/MM/YYYY
    }

    private String generateRandomID() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Số ngẫu nhiên từ 0 đến 999
        return String.format("NV%03d", randomNumber); // Định dạng ID
    }
    private boolean isIDExists(String id) {
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
