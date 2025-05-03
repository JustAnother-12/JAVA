package GUI.Admin.supplier;

import javax.swing.*;
import DTO.NhaCungCap_DTO;
import java.awt.*;

public class ChiTietNhaCungCap extends JFrame {
    public ChiTietNhaCungCap(NhaCungCap_DTO ncc) {
        setTitle("Chi tiết nhà cung cấp");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("ID: " + ncc.getMaNCC()));
        panel.add(new JLabel("Tên: " + ncc.getTenNCC()));
        panel.add(new JLabel("SĐT: " + ncc.getSdtNCC()));
        panel.add(new JLabel("Email: " + ncc.getEmailNCC()));
        // Thêm các trường khác nếu có

        add(panel);
        setVisible(true);
    }
}



