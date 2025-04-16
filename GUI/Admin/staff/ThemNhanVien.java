package GUI.Admin.staff;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashSet;
import javax.swing.table.DefaultTableModel;
import DAO.ThemNhanVien_DAO;
public class ThemNhanVien extends javax.swing.JDialog{
        private JTextField txtName, txtPosition, txtPhone, txtUsername, txtPassword, txtAddress, txtCCCD, txtBirthday;
        private JButton btnSave, btnCancel;
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
                    ThemNhanVien_DAO addStaff = new ThemNhanVien_DAO();
                    addStaff.addStaff(txtName, txtPosition, txtPhone, txtUsername, txtPassword, txtAddress, txtCCCD, txtBirthday,cbGender,tableModel,existingIDs);
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