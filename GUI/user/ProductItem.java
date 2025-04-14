package GUI.user;

import javax.swing.*;

import DTO.Product_Item_DTO;

import java.awt.*;

public class ProductItem extends JPanel {
    private MyButton addCart_btn;
    private String ID;
    private JLabel Imagelb;
    private JLabel Namelb;
    private JLabel PriceTaglb;
    private boolean selected;

    public String getName(){
        return Namelb.getText();
    }
    public String getID(){
        return ID;
    }

    public ProductItem(Product_Item_DTO data) {
        initComponents(data);
    }

    private void initComponents(Product_Item_DTO data) {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(200, 300)); 
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        ID = data.getID();

        //new ImageIcon("user/ProductImage/LTG.png")
        Imagelb = new JLabel();
        Imagelb.setPreferredSize(new Dimension(180,180));
        Imagelb.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Imagelb.setAlignmentX(Component.CENTER_ALIGNMENT);
        Imagelb.setIcon(data.toImage());
        // Imagelb.setBackground(new Color(205,205,205));

        Namelb = new JLabel("Product Name");
        Namelb.setText(data.getTitle());
        Namelb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        Namelb.setAlignmentX(Component.CENTER_ALIGNMENT);

        PriceTaglb = new JLabel("price");
        PriceTaglb.setText(data.getPrice());
        PriceTaglb.setFont(new Font("Segoe UI", Font.BOLD, 16));
        PriceTaglb.setForeground(new Color(255, 102, 102));
        PriceTaglb.setAlignmentX(Component.CENTER_ALIGNMENT);

        addCart_btn = new MyButton("Thêm vào giỏ");
        addCart_btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        addCart_btn.setBackground(Color.decode("#00B4DB"));
        addCart_btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add spacing and components
        add(Box.createVerticalStrut(10)); // Space at the top
        add(Imagelb);
        add(Box.createVerticalStrut(10));
        add(Namelb);
        add(Box.createVerticalStrut(5));
        add(PriceTaglb);
        add(Box.createVerticalStrut(10));
        add(addCart_btn);
        add(Box.createVerticalStrut(10));
        add(Box.createVerticalGlue()); // Push everything upwards
    }
}

