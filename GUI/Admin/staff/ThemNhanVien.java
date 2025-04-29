package GUI.Admin.staff;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import javax.swing.table.DefaultTableModel;

import BLL.NhanVien_BLL;
public class ThemNhanVien extends javax.swing.JDialog{
        private JTextField txtName, txtPhone, txtUsername, txtPassword, txtAddress, txtCCCD, txtBirthday;
        private JButton btnSave, btnCancel;
        private JComboBox<String> cbPosition;
        private JComboBox<String> cbGender;
        private DefaultTableModel tableModel;
        
        private HashSet<String> existingIDs; 
        public ThemNhanVien(DefaultTableModel tableModel) {
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
            cbPosition = new JComboBox<>(new String[]{"Quản lý kho", "Quản lý khách hàng", "Quản lý nhân viên", "Quản lý đơn hàng"});
            add(cbPosition);
    
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
                    NhanVien_BLL NhanVien_BLL = new NhanVien_BLL();
                    boolean nhanvien = NhanVien_BLL.addStaff(txtName, txtPhone, txtUsername, txtAddress, txtBirthday, cbPosition, cbGender, txtCCCD, txtPassword, tableModel, existingIDs);
                    if (nhanvien == true)
                        dispose();
                }
            });
    
            // Thêm sự kiện cho nút Hủy
            btnCancel.addActionListener(e -> dispose());
    
            setLocationRelativeTo(null);
            setModal(true);
            setVisible(true);
        }
        private void initComponents() {//GEN-BEGIN:initComponents
            setLayout(new java.awt.BorderLayout()); 
        }
}