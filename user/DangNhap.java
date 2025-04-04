package user;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;


public class DangNhap extends JFrame{
    private JPanel panel = new JPanel();
    private JLabel lblTitle = new JLabel("ĐĂNG NHẬP", JLabel.CENTER);
    protected JTextField txtEmail = new JTextField();
    protected JPasswordField txtMatKhau = new JPasswordField();
    protected MyButton btnDangNhap = new MyButton("ĐĂNG NHẬP");
    protected MyButton btnDangKy = new MyButton("Chưa có tài khoản? Đăng ký");

    public DangNhap() {
        initComponents();
    }


    public void initComponents() {
        setResizable(false);
        setTitle("ĐĂNG NHẬP");
        setSize(400, 400);
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

        
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtEmail.setBorder(BorderFactory.createTitledBorder("Email"));
        panel.add(txtEmail);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMatKhau.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));
        panel.add(txtMatKhau);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDangNhap.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setBackground(Color.decode("#00B4DB"));
        panel.add(btnDangNhap);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        
        btnDangKy.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDangKy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangKy.setForeground(Color.BLUE);
        btnDangKy.setBorderPainted(false);
        btnDangKy.setContentAreaFilled(false);
        panel.add(btnDangKy);

        setContentPane(panel);
        setVisible(true);
    }
}
