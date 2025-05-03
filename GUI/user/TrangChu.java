package GUI.user;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


public class TrangChu extends JFrame{
    private HeaderPanel header;
    private JPanel ContentPanel;
    private CatalogPanel catalogPanel;
    private ProductPanel productPanel;
    private UserInfoPanel UserInfo;
    private FilterPanel Filter;

    public TrangChu(){
        initComponents();
        setTitle("Trang Chủ");
        setResizable(false);
    }

    KeyListener KeyListener = new KeyListener() {
        @Override
        public void keyPressed(KeyEvent e){
            if (e.getKeyCode() == KeyEvent.VK_ENTER && !header.searchBox.getText().equals("")){
                SwitchToFilter();
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    };

    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == catalogPanel.headerLabel) {
                for ( JLabel label : catalogPanel.list){
                    label.setOpaque(false);
                }
                ContentPanel.remove(productPanel);
                productPanel = new ProductPanel("ALL", null, null);
                ContentPanel.add(productPanel);
            }
            else if (e.getSource() == catalogPanel.sachLabel) {
                catalogPanel.paintLabel("SÁCH");
                ContentPanel.remove(productPanel);
                productPanel = new ProductPanel("SACH", null, null);
                ContentPanel.add(productPanel);
            } 
            else if (e.getSource() == catalogPanel.voLabel) {
                catalogPanel.paintLabel("VỞ");
                ContentPanel.remove(productPanel);
                productPanel = new ProductPanel("VO", null, null);
                ContentPanel.add(productPanel);
            } 
            else if (e.getSource() == catalogPanel.butLabel) {
                catalogPanel.paintLabel("BÚT");
                ContentPanel.remove(productPanel);
                productPanel = new ProductPanel("BUT", null, null);
                ContentPanel.add(productPanel);
            }
            else if (e.getSource() == header.accountLabel  && !header.accountLabel.getText().equals("")){
                SwitchToUserMenu();
            }
            else if (e.getSource() == header.logoIcon){
                SwitchToShop();
            }
            else if(e.getSource() == header.searchIcon && !header.searchBox.getText().equals("")){
                SwitchToFilter();
            }
            else if(e.getSource() == Filter.FilterButton && !Filter.MinPriceTF.getText().equals("Giá thấp nhất") && !Filter.MaxPriceTF.getText().equals("Giá cao nhất")){
                ContentPanel.remove(productPanel);
                // ContentPanel.setLayout(new BoxLayout(ContentPanel, BoxLayout.X_AXIS));
                if(header.searchBox.getText().equals("Search...")){
                    productPanel = new ProductPanel("ALL", null, Filter);
                }
                else{
                    productPanel = new ProductPanel("SEARCH", header.searchBox.getText(), Filter);
                    
                }
                ContentPanel.add(productPanel);
            }

            // Revalidate and repaint the ContentPanel to reflect changes
            ContentPanel.revalidate();
            ContentPanel.repaint();
            }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}
            
        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == catalogPanel.sachLabel) {
                catalogPanel.paintLabel("SÁCH");
            } 
            else if (e.getSource() == catalogPanel.voLabel) {
                catalogPanel.paintLabel("VỞ");
            } 
            else if (e.getSource() == catalogPanel.butLabel) {
                catalogPanel.paintLabel("BÚT");
            }
            
            // Revalidate and repaint the ContentPanel to reflect changes
            ContentPanel.revalidate();
            ContentPanel.repaint();
        }
        @Override
        public void mouseExited(MouseEvent e) {
            for (JLabel label : catalogPanel.list){
                label.setOpaque(false);
            }
            ContentPanel.revalidate();
            ContentPanel.repaint();
        }
    };

    public void SwitchToFilter(){
        ContentPanel.removeAll();;
        ContentPanel.setLayout(new BoxLayout(ContentPanel, BoxLayout.X_AXIS));

        Filter = new FilterPanel();
        Filter.FilterButton.addMouseListener(mouseListener);
        productPanel = new ProductPanel("SEARCH", header.searchBox.getText(), null);

        // ContentPanel.add(Filter);
        ContentPanel.add(Filter);
        ContentPanel.add(productPanel);

        ContentPanel.revalidate();
        ContentPanel.repaint();
    }
    
    public void SwitchToShop(){
        ContentPanel.removeAll();
        ContentPanel.setLayout(new BoxLayout(ContentPanel, BoxLayout.X_AXIS));
        productPanel = new ProductPanel("ALL", null, null);
        catalogPanel = new CatalogPanel();
        catalogPanel.headerLabel.addMouseListener(mouseListener);
        for ( JLabel label : catalogPanel.list){
            label.addMouseListener(mouseListener);
        }
        ContentPanel.add(catalogPanel);
        ContentPanel.add(productPanel);
        ContentPanel.revalidate();
        ContentPanel.repaint();
    }

    public void SwitchToUserMenu(){
        ContentPanel.removeAll();

        // UserMenu.UsernameLabel.setText(header.accountLabel.getText().toUpperCase());
        // for(JLabel lb : UserMenu.OptionList){
        //     lb.addMouseListener(mouseListener);
        // }
        ContentPanel.setLayout(new BoxLayout(ContentPanel, BoxLayout.Y_AXIS));

        UserInfo = new UserInfoPanel();
        UserInfo.NameInfo.setText(header.khachhang.getTen_KhachHang());
        UserInfo.PhoneInfo.setText(header.khachhang.getSdt_KhachHang());
        UserInfo.GioiTinhInfo.setText(header.khachhang.getGioiTinh_KhachHang());
        UserInfo.EmailInfo.setText(header.khachhang.getEmail());
        UserInfo.AddressInfo.setText(header.khachhang.getDiaChi_KhachHang());
        UserInfo.NgaySinhInfo.setText((header.khachhang.getNgaySinh_KhachHang()).replace("-", "/"));
        UserInfo.UsernameInfo.setText(header.khachhang.getUsername());
        UserInfo.PasswordInfo.setText(header.khachhang.getPass_KhachHang());

        // ContentPanel.add(UserMenu);
        // ContentPanel.add(Box.createHorizontalStrut(20));
        ContentPanel.add(UserInfo);
        ContentPanel.revalidate();
        ContentPanel.repaint();
    }

    public void initComponents(){
        JPanel spacer = new JPanel();
        header = new HeaderPanel();
        header.accountLabel.addMouseListener(mouseListener);
        header.logoIcon.addMouseListener(mouseListener);
        header.searchIcon.addMouseListener(mouseListener);
        header.searchBox.addKeyListener(KeyListener);

        ContentPanel = new JPanel();
        catalogPanel = new CatalogPanel();
        catalogPanel.headerLabel.addMouseListener(mouseListener);
        for ( JLabel label : catalogPanel.list){
            label.addMouseListener(mouseListener);
        }
        productPanel = new ProductPanel("ALL", null, null);
        spacer.setPreferredSize(new Dimension(20, 1));
        spacer.setOpaque(false);

        ContentPanel.setLayout(new BoxLayout(ContentPanel, BoxLayout.X_AXIS));
        ContentPanel.setBackground(Color.decode("#cfdef3"));
        ContentPanel.add(catalogPanel);
        ContentPanel.add(spacer);
        ContentPanel.add(productPanel);
        ContentPanel.setPreferredSize(new Dimension(960,540));
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#cfdef3"));
        add(header, BorderLayout.NORTH);
        add(ContentPanel, BorderLayout.CENTER);
        setBounds(0, 0, 1000, 730);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String args[]){
        new TrangChu();
    }
}