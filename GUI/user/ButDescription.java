package GUI.user;

import javax.swing.*;
import DTO.But_DTO;
import java.awt.*;

public class ButDescription extends JFrame{
    private JLabel soluongLabel;
    private JLabel mauLabel;
    private JLabel loaiButLabel;
    private JLabel hangLabel;

    public ButDescription(But_DTO data) {
        initComponents(data);
    }

    private void initComponents(But_DTO data) {
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
        mauLabel = new JLabel("Màu: " + data.getMau());
        loaiButLabel = new JLabel("Loại bút: " + data.getLoai_But());
        hangLabel = new JLabel("Hãng: " + data.getHang());

        // Style the labels
        JLabel[] labels = {soluongLabel, mauLabel, loaiButLabel, hangLabel};
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
