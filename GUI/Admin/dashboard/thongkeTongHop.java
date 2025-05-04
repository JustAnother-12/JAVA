package GUI.Admin.dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class thongkeTongHop extends JPanel{
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private JLabel doanhthuLabel;
    private JLabel banhangLabel;
    private JLabel khachhangLabel;

    private JPanel doanhthuPanel = new JPanel();
    private JPanel banhangPanel = new JPanel();
    private JPanel khachhangPanel = new JPanel();

    private Color bgColor = new Color(200,200,200);

    private JPanel PanelList[] = {doanhthuPanel, banhangPanel, khachhangPanel};

    MouseListener mouseListener = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() == doanhthuPanel || e.getSource() == doanhthuLabel){
                for(JPanel pnl: PanelList){
                    pnl.setBackground(Color.WHITE);
                }
                doanhthuPanel.setBackground(Color.LIGHT_GRAY);
                cardLayout.show(cardPanel, "Doanh thu theo tháng");
            }
            else if(e.getSource() == banhangPanel || e.getSource() == banhangLabel){
                for(JPanel pnl: PanelList){
                    pnl.setBackground(Color.WHITE);
                }
                banhangPanel.setBackground(Color.LIGHT_GRAY);
                cardLayout.show(cardPanel, "Bán hàng");
            }
            else if(e.getSource() == khachhangPanel || e.getSource() == khachhangLabel){
                for(JPanel pnl: PanelList){
                    pnl.setBackground(Color.WHITE);
                }
                khachhangPanel.setBackground(Color.LIGHT_GRAY);
                cardLayout.show(cardPanel, "Khách hàng");
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(e.getSource() == doanhthuPanel || e.getSource() == doanhthuLabel){
                for(JPanel pnl: PanelList){
                    if(pnl.getBackground().equals(bgColor))
                        pnl.setBackground(Color.WHITE);
                }
                if(doanhthuPanel.getBackground().equals(Color.WHITE))
                    doanhthuPanel.setBackground(bgColor);
            }
            else if(e.getSource() == banhangPanel || e.getSource() == banhangLabel){
                for(JPanel pnl: PanelList){
                    if(pnl.getBackground().equals(bgColor))
                        pnl.setBackground(Color.WHITE);
                }
                if(banhangPanel.getBackground().equals(Color.WHITE))
                    banhangPanel.setBackground(bgColor);
            }
            else if(e.getSource() == khachhangPanel || e.getSource() == khachhangLabel){
                for(JPanel pnl: PanelList){
                    if(pnl.getBackground().equals(bgColor))
                        pnl.setBackground(Color.WHITE);
                }
                if(khachhangPanel.getBackground().equals(Color.WHITE))
                    khachhangPanel.setBackground(bgColor);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(e.getSource() == doanhthuPanel || e.getSource() == doanhthuLabel){
                for(JPanel pnl: PanelList){
                    if(pnl.getBackground().equals(bgColor))
                        pnl.setBackground(Color.WHITE);
                }
            }
            else if(e.getSource() == banhangPanel || e.getSource() == banhangLabel){
                for(JPanel pnl: PanelList){
                    if(pnl.getBackground().equals(bgColor))
                        pnl.setBackground(Color.WHITE);
                }
            }
            else if(e.getSource() == khachhangPanel || e.getSource() == khachhangLabel){
                for(JPanel pnl: PanelList){
                    if(pnl.getBackground().equals(bgColor))
                        pnl.setBackground(Color.WHITE);
                }
            }
        }
        
    };

    public thongkeTongHop(){
        initComponents();
        doanhthuPanel.setBackground(Color.LIGHT_GRAY);
        cardLayout.show(cardPanel, "Doanh thu theo tháng");
    }

    private void initComponents(){
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        doanhthuLabel = new JLabel("Doanh thu theo tháng", SwingConstants.CENTER);
        doanhthuLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        banhangLabel = new JLabel("Doanh số bán hàng", SwingConstants.CENTER);
        banhangLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        khachhangLabel = new JLabel("Thống kê theo khách hàng", SwingConstants.CENTER);
        khachhangLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        

        JLabel LabelList[] = {doanhthuLabel, banhangLabel, khachhangLabel};

        for (JLabel lb:LabelList){
            lb.addMouseListener(mouseListener);
        }
        
        for (JPanel pnl:PanelList){
            pnl.setBackground(Color.WHITE);
            pnl.setPreferredSize(new Dimension(250,80));
            pnl.setLayout(new BorderLayout());
            pnl.addMouseListener(mouseListener);
        }

        doanhthuPanel.add(doanhthuLabel, BorderLayout.CENTER);
        banhangPanel.add(banhangLabel, BorderLayout.CENTER);
        khachhangPanel.add(khachhangLabel, BorderLayout.CENTER);

        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
        optionPanel.setPreferredSize(new Dimension(850, 80));
        optionPanel.add(Box.createHorizontalStrut(50));
        optionPanel.setBackground(Color.WHITE);

        
        for (JPanel pnl:PanelList){
            optionPanel.add(pnl);
            // optionPanel.add(Box.createHorizontalStrut(5));
        }
        optionPanel.add(Box.createHorizontalStrut(45));
        optionPanel.add(Box.createHorizontalGlue());

        // optionComboBox.setPreferredSize(new Dimension(150,30));
        // optionPanel.add(optionComboBox, BorderLayout.EAST);
        add(optionPanel, BorderLayout.NORTH);

        // Các panel thống kê
        JPanel doanhThuPanel = new thongkeDoanhThu();
        JPanel banChayPanel = new thongkeBanChay();
        JPanel khachangPanel = new thongkeTheoKhachHang();

        cardPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        cardPanel.setPreferredSize(new Dimension(850,650));
        cardPanel.add(doanhThuPanel, "Doanh thu theo tháng");
        cardPanel.add(banChayPanel, "Bán hàng");
        cardPanel.add(khachangPanel, "Khách hàng");

        add(cardPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(900, 650));
        setVisible(true);
    }
}
