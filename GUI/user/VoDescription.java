package GUI.user;

import javax.swing.*;
import java.awt.*;

import DTO.Vo_DTO;

public class VoDescription extends JFrame{
    private JLabel soluongLabel;
    private JLabel loaivoLabel;
    private JLabel chatLieuLabel;
    private JLabel nhaSanXuatLabel;
    private JLabel soTrangLabel;

    public VoDescription(Vo_DTO data) {
        initComponents(data);
    }

    private void initComponents(Vo_DTO data) {
        // Set up the JFrame
        setTitle(data.getTen_SanPham());
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 500));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create a main panel to hold all components
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title label for the product description
        JLabel titleLabel = new JLabel("Chi tiết Sản phẩm");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.decode("#00B4DB"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize labels for each field
        soluongLabel = new JLabel("Số lượng: " + String.valueOf(data.getSoLuong_SanPham()));
        loaivoLabel = new JLabel("Loại vở: " + data.getLoai_Vo());
        chatLieuLabel = new JLabel("Chất liệu: " + data.getChatLieu());
        nhaSanXuatLabel = new JLabel("Nhà sản xuất: " + data.getNhaSanXuat());
        soTrangLabel = new JLabel("Số trang: " + String.valueOf(data.getSoTrang()));

        // Style the labels
        JLabel[] labels = {soluongLabel, loaivoLabel,chatLieuLabel,nhaSanXuatLabel,soTrangLabel};
        for (JLabel label : labels) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Add components to the main panel with spacing
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        for (JLabel label : labels) {
            mainPanel.add(label);
            mainPanel.add(Box.createVerticalStrut(10));
        }
        mainPanel.add(Box.createVerticalGlue()); // Push content upwards

        // Add the main panel to the JFrame
        add(mainPanel, BorderLayout.CENTER);

        // Pack and display the frame
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
}
