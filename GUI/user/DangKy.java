package GUI.user;

import java.awt.*;
import javax.swing.*;

public class DangKy extends JFrame {
    private JPanel panel = new JPanel();
    private JLabel lblTitle = new JLabel("ĐĂNG KÝ", JLabel.CENTER);
    protected JTextField txtHoTen = new JTextField();
    protected JTextField txtSDT = new JTextField();
    protected JTextField txtEmail = new JTextField();
    protected JTextField txtUsername = new JTextField();
    protected JPasswordField txtMatKhau = new JPasswordField();
    protected JComboBox<String> cbDiaChi;
    protected JRadioButton rbNam = new JRadioButton("Nam");
    protected JRadioButton rbNu = new JRadioButton("Nữ");
    protected ButtonGroup genderGroup = new ButtonGroup();
    protected JComboBox<Integer> cbNgay = new JComboBox<>();
    protected JComboBox<Integer> cbThang = new JComboBox<>();
    protected JComboBox<Integer> cbNam = new JComboBox<>();
    protected MyButton btnDangKy = new MyButton("ĐĂNG KÝ");

    private String[] diaChiList = {
        "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu",
        "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước",
        "Bình Thuận", "Cà Mau", "Cần Thơ", "Cao Bằng", "Đà Nẵng",
        "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp",
        "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh",
        "Hải Dương", "Hải Phòng", "Hậu Giang", "TP.HCM", "Hòa Bình",
        "Hưng Yên", "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu",
        "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định",
        "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên",
        "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị",
        "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên",
        "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "Trà Vinh", "Tuyên Quang",
        "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
    };


    public DangKy() {
        initComponents();
    }

    private void initComponents() {
        setTitle("ĐĂNG KÝ");
        setSize(400, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        txtHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtHoTen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtHoTen.setBorder(BorderFactory.createTitledBorder("Họ và tên"));
        panel.add(txtHoTen);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtSDT.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtSDT.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtSDT.setBorder(BorderFactory.createTitledBorder("Số điện thoại"));
        panel.add(txtSDT);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtEmail.setBorder(BorderFactory.createTitledBorder("Email"));
        panel.add(txtEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsername.setBorder(BorderFactory.createTitledBorder("Tài khoản"));
        panel.add(txtUsername);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMatKhau.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));
        panel.add(txtMatKhau);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Phần chọn giới tính
        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        genderPanel.setBorder(BorderFactory.createTitledBorder("Giới tính"));
        genderPanel.setBackground(Color.WHITE);
        genderGroup.add(rbNam);
        genderGroup.add(rbNu);
        rbNam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbNam.setBackground(Color.WHITE);
        rbNam.setActionCommand(rbNam.getText());
        rbNu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rbNu.setBackground(Color.WHITE);
        rbNu.setActionCommand(rbNu.getText());
        rbNam.setSelected(true);
        genderPanel.add(rbNam);
        genderPanel.add(rbNu);
        panel.add(genderPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Phần chọn ngày tháng năm sinh
        JPanel birthPanel = new JPanel();
        birthPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        birthPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        birthPanel.setBorder(BorderFactory.createTitledBorder("Ngày sinh"));
        birthPanel.setBackground(Color.WHITE);

        // Khởi tạo tháng
        for (int i = 1; i <= 12; i++) {
            cbThang.addItem(i);
        }
        cbThang.setPreferredSize(new Dimension(60, 30));
        cbThang.setBackground(Color.WHITE);

        // Khởi tạo năm
        int currentYear = java.time.Year.now().getValue();
        for (int i = 1900; i <= currentYear; i++) {
            cbNam.addItem(i);
        }
        cbNam.setPreferredSize(new Dimension(80, 30));
        cbNam.setSelectedItem(currentYear - 20);
        cbNam.setBackground(Color.WHITE);

        // Khởi tạo ngày (ban đầu dựa trên tháng 1 và năm được chọn)
        updateDays();
        cbNgay.setPreferredSize(new Dimension(60, 30));
        cbNgay.setBackground(Color.WHITE);

        // Thêm listener để cập nhật số ngày khi thay đổi tháng hoặc năm
        cbThang.addActionListener(e -> updateDays());
        cbNam.addActionListener(e -> updateDays());
        

        birthPanel.add(cbNgay);
        birthPanel.add(cbThang);
        birthPanel.add(cbNam);
        panel.add(birthPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        cbDiaChi = new JComboBox<>(diaChiList);
        cbDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cbDiaChi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbDiaChi.setBorder(BorderFactory.createTitledBorder("Địa chỉ"));
        cbDiaChi.setBackground(Color.WHITE);
        panel.add(cbDiaChi);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDangKy.setBackground(Color.decode("#00B4DB"));
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnDangKy);

        setContentPane(panel);
        setVisible(true);
    }

    // Phương thức cập nhật số ngày dựa trên tháng và năm
    private void updateDays() {
        int selectedMonth = (int) cbThang.getSelectedItem();
        int selectedYear = (int) cbNam.getSelectedItem();
        int daysInMonth = getDaysInMonth(selectedMonth, selectedYear);

        // Lưu ngày hiện tại nếu có
        Integer selectedDay = (Integer) cbNgay.getSelectedItem();

        // Xóa và cập nhật danh sách ngày
        cbNgay.removeAllItems();
        for (int i = 1; i <= daysInMonth; i++) {
            cbNgay.addItem(i);
        }

        // Khôi phục ngày đã chọn nếu nó hợp lệ với tháng mới
        if (selectedDay != null && selectedDay <= daysInMonth) {
            cbNgay.setSelectedItem(selectedDay);
        } else {
            cbNgay.setSelectedIndex(0); // Chọn ngày 1 nếu ngày cũ không hợp lệ
        }
    }

    // Phương thức tính số ngày trong tháng, có kiểm tra năm nhuận
    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return isLeapYear(year) ? 29 : 28;
            default:
                return 31;
        }
    }

    // Phương thức kiểm tra năm nhuận
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}


