package GUI.Admin.dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class thongkeFrame extends JFrame{
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JComboBox<String> optionComboBox;


    public thongkeFrame(){
        initComponents();
    }

    private void initComponents(){
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        optionComboBox = new JComboBox<>(new String[] { "Doanh thu theo tháng", "Bán hàng", "Khách hàng" });

        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BorderLayout());
        optionPanel.setPreferredSize(new Dimension(150, 30));

        optionComboBox.setPreferredSize(new Dimension(150,30));
        optionPanel.add(optionComboBox, BorderLayout.EAST);
        add(optionPanel, BorderLayout.NORTH);

        // Các panel thống kê
        JPanel doanhThuPanel = new thongkeDoanhThu();
        JPanel banChayPanel = new thongkeBanChay();
        JPanel khachangPanel = new thongkeTheoKhachHang();

        cardPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        cardPanel.add(doanhThuPanel, "Doanh thu theo tháng");
        cardPanel.add(banChayPanel, "Bán hàng");
        cardPanel.add(khachangPanel, "Khách hàng");

        add(cardPanel, BorderLayout.CENTER);

        optionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cardLayout.show(cardPanel, (String)optionComboBox.getSelectedItem());
            }    
        });

        setTitle("Thống kê tổng hợp");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String args[]){
        new thongkeFrame();
    }
}
