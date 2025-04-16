package GUI.Amin;

import DAO.ChiTietThongTinTaiKhoan_DAO;
import DAO.DatabaseConnection;
import DAO.KhachHang_DAO;
import DAO.NhanVien_DAO;

import javax.swing.*;

import BLL.CheckFailInput_BLL;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import DTO.KhachHang_DTO;
import DTO.NhanVien_DTO;

public class ChiTietNhanVien extends JDialog {
    private JTextField txtName, txtPhone, txtUsername, txtAddress, txtBirthday;
    private JTextField txtEmail; // Chỉ dành cho customer
    private JTextField txtPosition; // Chỉ dành cho staff
    private JComboBox<String> cbGender;
    private JTextField txtCCCD; // Chỉ dành cho staff
    private JButton btnEdit, btnSave;
    private boolean isCustomer;
    public ChiTietNhanVien(KhachHang_DTO kh) throws ParseException {
        isCustomer = true;
        setTitle("Chi tiết Khách Hàng");
        setSize(400, 400);
        setLayout(new GridLayout(8, 2));
        KhachHang_DAO a = new KhachHang_DAO();
        KhachHang_DTO b = new KhachHang_DTO();
        b = a.getKhachHangfromID(kh.getId_KhachHang());
        // Tạo các trường nhập liệu cho khách hàng
        txtName = new JTextField(b.getTen_KhachHang());
        txtPhone = new JTextField(b.getSdt_KhachHang());
        txtUsername = new JTextField(b.getUsername());
        txtAddress = new JTextField(b.getDiaChi_KhachHang());
        txtBirthday = new JTextField(b.getNgaySinh_KhachHang());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String birthday = txtBirthday.getText();
        Date date = inputFormat.parse(birthday);
        String formattedDate = outputFormat.format(date);
        txtBirthday.setText(formattedDate);
        txtEmail = new JTextField(b.getEmail()); // Thêm trường email
        // Tạo JComboBox cho giới tính
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ"}); // Các giá trị có thể có
        cbGender.setSelectedItem(b.getGioiTinh_KhachHang());
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
            ChiTietThongTinTaiKhoan_DAO chiTietNhanVien_DAO = new ChiTietThongTinTaiKhoan_DAO();
            CheckFailInput_BLL checkFailInput_BLL = new CheckFailInput_BLL();
            if (checkFailInput_BLL.validateFields(isCustomer, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, txtEmail, null, null)) {
                try {
                    chiTietNhanVien_DAO.updateCustomer(kh, txtName, txtPhone, txtUsername, txtAddress, txtBirthday,txtEmail);
                    dispose();
                } catch (ParseException ex) {
                    Logger.getLogger(ChiTietNhanVien.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
        dispose();
    }
    // Constructor cho nhân viên
    public ChiTietNhanVien(NhanVien_DTO nv) throws ParseException {
        isCustomer = false;
        setTitle("Chi tiết Nhân Viên");
        setSize(400, 400);
        setLayout(new GridLayout(9, 2));
        NhanVien_DAO a = new NhanVien_DAO();
        NhanVien_DTO b = a.getDataOfStaff(nv);
        // Tạo các trường nhập liệu cho nhân viên
        txtName = new JTextField(b.getTennv());
        txtPhone = new JTextField(b.getSdt());
        txtUsername = new JTextField(b.getUsername());
        txtAddress = new JTextField(b.getDiachi());
        txtBirthday = new JTextField(b.getNgaysinh());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String birthday = txtBirthday.getText();
        Date date = inputFormat.parse(birthday);
        String formattedDate = outputFormat.format(date);
        txtBirthday.setText(formattedDate);
        txtPosition = new JTextField(b.getChucvu()); // Thêm trường chức vụ
        txtCCCD = new JTextField(b.getCCCD()); // Thêm trường CCCD
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ"}); // Các giá trị có thể có
        cbGender.setSelectedItem(b.getGioitinh());
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
            ChiTietThongTinTaiKhoan_DAO chiTietNhanVien_DAO = new ChiTietThongTinTaiKhoan_DAO();
            CheckFailInput_BLL checkFailInput_BLL = new CheckFailInput_BLL();
            // Cập nhật thông tin
            if (checkFailInput_BLL.validateFields(isCustomer, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, null, txtPosition, txtCCCD)) {
                try {
                    chiTietNhanVien_DAO.updateStaff(nv, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, txtPosition, txtCCCD, cbGender);;
                    dispose();
                } catch (ParseException ex) {
                    Logger.getLogger(ChiTietNhanVien.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
        dispose();
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
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.BorderLayout());

    }
}
