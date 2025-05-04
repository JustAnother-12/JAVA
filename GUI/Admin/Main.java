package GUI.Admin;
import GUI.Admin.staff.NhanVienTable;

import javax.swing.*;

import java.awt.*;
import GUI.Admin.customer.CustomerTable;
import GUI.Admin.dashboard.thongkeTongHop;
import GUI.Admin.order.OrderTable;
import GUI.Admin.product.ProdmaFrame;
import GUI.Admin.supplier.SupplierTable;
import GUI.Admin.importorder.HistoryTable;
import GUI.Admin.component.Header;
import GUI.Admin.component.Menu;
import GUI.Admin.swing.*;

public class Main extends JFrame {
    NhanVienTable accountForm;
    CustomerTable customerForm;
    OrderTable orderForm;
    ProdmaFrame productForm;
    SupplierTable supplierForm;
    HistoryTable historyForm;
    thongkeTongHop dashboard;
    
    private JPanel contentPanel;
    private Header header;
    private JPanel mainPanel;
    private Menu menu;
    
    private final JLabel placeholder = new JLabel("");
    private JToggleButton bt1;


    public Main() {
        initComponents();
        myinit();
        menu.initMoving(Main.this);
        dashboard = new thongkeTongHop();
        accountForm = new NhanVienTable();
        customerForm = new CustomerTable();
        orderForm = new OrderTable();
        productForm = new ProdmaFrame();
        supplierForm = new SupplierTable();
        historyForm =new HistoryTable();

        setForm("DashBoard", dashboard);
        header.setVisible(false);

        menu.addEventMenuSelected(new MenuSelectedListener() {
            @Override
            public void menuSelected(int index) {
                System.out.println(index);
                switch (index) {
                    case 0:
                        setForm("DashBoard", dashboard);
                        // header.setSearchListener(dashboard);
                        // header.getjLabel2().setIcon(new ImageIcon(getClass().getResource("/GUI/Admin/icon/none.jpg")));
                        header.setVisible(false);
                        break;
                    case 1:
                        setForm("Product", productForm);
                        header.setSearchListener(productForm);
                        header.getjLabel2().setIcon(new ImageIcon(getClass().getResource("/GUI/Admin/icon/none.jpg")));
                        header.setVisible(true);
                        break;
                    case 2:
                        setForm("Account", accountForm);
                        header.setSearchListener(accountForm);
                        header.getjLabel2().setIcon(new ImageIcon(getClass().getResource("/GUI/Admin/icon/menu.png")));
                        header.setVisible(true);
                        break;
                    case 3:
                        setForm("Customer", customerForm);
                        header.setSearchListener(customerForm);
                        header.getjLabel2().setIcon(new ImageIcon(getClass().getResource("/GUI/Admin/icon/none.jpg")));
                        header.setVisible(true);
                        break;
                    case 4:
                        setForm("Order", orderForm);
                        header.setSearchListener(orderForm);
                        header.getjLabel2().setIcon(new ImageIcon(getClass().getResource("/GUI/Admin/icon/none.jpg")));
                        header.setVisible(true);
                        break;
                    case 5:
                        setForm("Suppliers", supplierForm);
                        header.setSearchListener(supplierForm);
                        header.getjLabel2().setIcon(new ImageIcon(getClass().getResource("/GUI/Admin/icon/none.jpg")));
                        header.setVisible(true);
                        break; 
                    case 6:
                        setForm("Import", historyForm);
                        header.setSearchListener(historyForm);
                        header.getjLabel2().setIcon(new ImageIcon(getClass().getResource("/GUI/Admin/icon/none.jpg")));
                        header.setVisible(true);
                        break;          
                }
            }
        });
        
    }

    private void setForm(String name, JComponent com) {
        if (contentPanel.getComponentCount() == 0 || !componentExists(name)) {
            contentPanel.add(com, name);
        }
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }
    
    private boolean componentExists(String name) {
        for (Component comp : contentPanel.getComponents()) {
            if (name.equals(comp.getName())) {
                return true;
            }
        }
        return false;
    }
    
    private void myinit() {
        placeholder.setPreferredSize(header.getjLabel2().getPreferredSize());
        placeholder.setMinimumSize(header.getjLabel2().getMinimumSize());
        placeholder.setMaximumSize(header.getjLabel2().getMaximumSize());
    }
   
    
private void initComponents() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setSize(1100, 800);
    setLocationRelativeTo(null);
    getContentPane().setLayout(new BorderLayout());

    // Khởi tạo menu và add vào WEST
    menu = new GUI.Admin.component.Menu();
    getContentPane().add(menu, BorderLayout.WEST);

    // Khởi tạo mainPanel với BorderLayout
    mainPanel = new JPanel(new BorderLayout());
    getContentPane().add(mainPanel, BorderLayout.CENTER);

    // Tạo header và add vào NORTH của mainPanel
    header = new GUI.Admin.component.Header();
    mainPanel.add(header, BorderLayout.NORTH);

    // Tạo contentPanel để hiển thị các form, dùng CardLayout
    contentPanel = new JPanel(new CardLayout());
    mainPanel.add(contentPanel, BorderLayout.CENTER);
}
    

    public static void main(String args[]) {
        new Main().setVisible(true);;
    }  
}

