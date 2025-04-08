package GUI.user;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import BLL.SanPham_BLL;
import DTO.Product_Item_DTO;
import DTO.SanPham_DTO;
import DTO.Product_DescriptionDTO;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class ProductPanel extends JScrollPane {
    private JPanel ItemPanel;
    private ArrayList<ProductItem> ProductList;
    private ArrayList<Product_DescriptionDTO> DescriptionList;
    private ProductDescription CurrentDescription;
    protected SanPham_BLL spBLL;

    public ProductPanel() { 
        initComponents();
    }

    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(CurrentDescription != null){
                CurrentDescription.dispose();
            }
            ProductItem item = (ProductItem) e.getSource();
            for(Product_DescriptionDTO description:DescriptionList){
                if (description.getname().equals(item.getName())) {
                    CurrentDescription = new ProductDescription(description);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    };

    private Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/ban_van_phong_pham", "root", "123456789");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // private void getAllSp() {
    //     DescriptionList = new ArrayList<>();
    //     try (Connection con = connectDB()) {
    //         if (con != null) {
    //             Statement stmt = con.createStatement();
    //             ResultSet rs = stmt.executeQuery("SELECT * FROM SANPHAM");
    //             while (rs.next()) {
    //                 String id = rs.getString("masp");
    //                 String ten = rs.getString("tensp");
    //                 double gia = rs.getDouble("dongiasp");
    //                 int soLuong = rs.getInt("soluongsp");

    //                 ProductItem item = new ProductItem(new Product_Item_DTO(ten, "LTG", String.valueOf(new BigDecimal(gia))));
    //                 ProductList.add(item);
    //                 ItemPanel.add(item);
    //             }
    //         }
    //     } catch (SQLException e) {
    //         System.out.println(e.getMessage());
    //     }
    // }

    private void addAllSP(){
        DescriptionList = new ArrayList<>();
        ProductList = new ArrayList<>();
        for( SanPham_DTO sp : spBLL.getAllSanPham()){
            String id = sp.getID_SanPham();
            String ten = sp.getTen_SanPham();
            double gia = sp.getGia_SanPham();
            int soLuong = sp.getSoLuong_SanPham();

            ProductItem item = new ProductItem(new Product_Item_DTO(ten, "LTG", String.valueOf(new BigDecimal(gia))));
            ProductList.add(item);
            ItemPanel.add(item);
        }
    }

    private void addEvent(){
        for(ProductItem item:ProductList){
            item.addMouseListener(mouseListener);
        }
    }

    private void initComponents() {
        spBLL = new SanPham_BLL();
        ItemPanel = new JPanel(new GridLayout(0, 3, 35, 35));
        ItemPanel.setBackground(Color.decode("#cfdef3"));
        ItemPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));

        // getAllSp(); // Fetch and populate items from DB
        addAllSP();
        ItemPanel.revalidate();
        ItemPanel.repaint();
        addEvent();

        setViewportView(ItemPanel);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getVerticalScrollBar().setUI(new MyScrollBarUI());
        getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setPreferredSize(new Dimension(740, 540));
    }
}

