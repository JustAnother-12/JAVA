package GUI.user;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DTO.Product_Item_DTO;
import utils.MyButton;

public class ProductItem extends JPanel {
    private MyButton addCart_btn;
    private String ID;
    private JLabel Imagelb;
    private JLabel Namelb;
    JLabel PriceTaglb;

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
        // addCart_btn.addActionListener(e -> {
        //     if (HeaderPanel.khachhang == null) {
        //         JOptionPane.showMessageDialog(null, "Bạn cần đăng nhập để thêm vào giỏ hàng.");
        //         return;
        //     }
        
        //     DTO.CartItemDTO item = new DTO.CartItemDTO(
        //         data.getID(),
        //         data.getTitle(),
        //         data.getImageicon(),
        //         1,
        //         data.getRawPrice()
        //     );
        
        //     BLL.Cart_BLL.themVaoGio(item);
        //     JOptionPane.showMessageDialog(null, "Đã thêm vào giỏ hàng!");
        // });
        
        

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

    public String getPriceText() {
        return PriceTaglb.getText();
    }
        public JButton getAddCartButton() {
        return addCart_btn;
    }
    
}

