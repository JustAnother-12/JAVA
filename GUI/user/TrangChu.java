package GUI.user;
import javax.swing.*;

// import DTO.KhachHang_DTO;
// import BLL.KhachHang_BLL;

import java.awt.*;

public class TrangChu extends JFrame{
    private HeaderPanel header;
    private JPanel ContentPanel;
    private CatalogPanel catalogPanel;
    private ProductPanel productPanel;

    public TrangChu(){
        initComponents();
        setTitle("Trang Chủ");
        setResizable(false);
    }

    public void initComponents(){
        JPanel spacer = new JPanel();
        header = new HeaderPanel();
        ContentPanel = new JPanel();
        catalogPanel = new CatalogPanel();
        productPanel = new ProductPanel();
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
