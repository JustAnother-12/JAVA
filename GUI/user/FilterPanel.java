package GUI.user;

import javax.swing.*;

import DTO.SanPham_DTO;

import java.awt.*;
import java.awt.event.*;


public class FilterPanel extends JPanel{
    protected JPanel headerPanel = new JPanel();
    protected JLabel headerLabel = new JLabel("LỌC");
    protected JPanel FilterPanel = new JPanel();

    protected JPanel PricePanel = new JPanel();
    protected JLabel PriceLabel = new JLabel("Hãy nhập khoảng giá:");
    protected JTextField MinPriceTF = new JTextField();
    protected JTextField MaxPriceTF = new JTextField();

    protected JPanel BtnPanel = new JPanel();
    protected MyButton FilterButton = new MyButton("LỌC");
    
    public FilterPanel(){
        initComponents();
    }

    FocusListener Focus = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            if (MinPriceTF.getText().equals("Giá thấp nhất")) {
                MinPriceTF.setText("");
                MinPriceTF.setForeground(Color.BLACK);
            }
            if (MaxPriceTF.getText().equals("Giá cao nhất")) {
                MaxPriceTF.setText("");
                MaxPriceTF.setForeground(Color.BLACK);
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (MinPriceTF.getText().isEmpty()) {
                MinPriceTF.setForeground(Color.GRAY);
                MinPriceTF.setText("Giá thấp nhất");
            }
            if (MaxPriceTF.getText().isEmpty()) {
                MaxPriceTF.setForeground(Color.GRAY);
                MaxPriceTF.setText("Giá cao nhất");
            }
        }
    };

    public SanPham_DTO Filter(SanPham_DTO sp){
        SanPham_DTO result = null;
        if(Double.parseDouble(MinPriceTF.getText()) <= sp.getGia_SanPham() && sp.getGia_SanPham() <= Double.parseDouble(MaxPriceTF.getText())){
            result = sp;
        }
        return result;
    }

    private void initComponents(){
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(false);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.setBackground(Color.decode("#00B4DB"));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        headerPanel.setPreferredSize(new Dimension(160, 60));
        headerPanel.add(headerLabel);
        headerPanel.add(Box.createVerticalGlue());

        PriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        PriceLabel.setForeground(Color.BLACK);
        PriceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        PriceLabel.setOpaque(false);
        PriceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        MinPriceTF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        MinPriceTF.setPreferredSize(new Dimension(140, 40));
        MinPriceTF.setText("Giá thấp nhất");
        MinPriceTF.setForeground(Color.GRAY);
        // MinPriceTF.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        MinPriceTF.setAlignmentX(CENTER_ALIGNMENT);

        MaxPriceTF.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        MaxPriceTF.setPreferredSize(new Dimension(140, 40));
        MaxPriceTF.setText("Giá cao nhất");
        MaxPriceTF.setForeground(Color.GRAY);
        // MaxPriceTF.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        MaxPriceTF.setAlignmentX(CENTER_ALIGNMENT);

        FilterPanel.setLayout(new BoxLayout(FilterPanel, BoxLayout.Y_AXIS));
        FilterPanel.setPreferredSize(new Dimension(160, 100));
        FilterPanel.setBackground(Color.WHITE);
        FilterPanel.setBorder(BorderFactory.createEmptyBorder(20,0, 0, 0));
        FilterPanel.add(PriceLabel);
        FilterPanel.add(MinPriceTF);
        FilterPanel.add(Box.createVerticalStrut(10));
        FilterPanel.add(MaxPriceTF);

        FilterButton.setBackground(Color.RED);
        FilterButton.setForeground(Color.WHITE);
        FilterButton.setPreferredSize(new Dimension(80, 40));

        BtnPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        BtnPanel.setBackground(Color.WHITE);
        BtnPanel.add(FilterButton);

        MinPriceTF.addFocusListener(Focus);
        MaxPriceTF.addFocusListener(Focus);

        setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        setMaximumSize(new Dimension(160, 300));
        setBackground(Color.decode("#cfdef3"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(TOP_ALIGNMENT);

        add(headerPanel);
        add(FilterPanel);
        add(BtnPanel);
    }
}
