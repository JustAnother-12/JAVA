package GUI.Admin;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import GUI.Admin.component.Header;
import GUI.Admin.component.Menu;
import GUI.Admin.customer.CustomerTable;
import GUI.Admin.order.OrderTable;
import GUI.Admin.product.ProdmaFrame;
import GUI.Admin.staff.NhanVienTable;
import GUI.Admin.swing.MenuSelectedListener;

public class Main extends JFrame {
    NhanVienTable accountForm;
    CustomerTable customerForm;
    OrderTable orderForm;
    ProdmaFrame productForm;

    private JPanel contentPanel;
    private Header header;
    private JPanel mainPanel;
    private Menu menu;
    private GUI.Admin.swing.PanelBorder panelBorder1;
  
    private final JLabel placeholder = new JLabel("");
    private JToggleButton bt1;


    public Main() {
        initComponents();
        myinit();
        menu.initMoving(Main.this);
        accountForm = new NhanVienTable();
        customerForm = new CustomerTable();
        orderForm = new OrderTable();
        productForm = new ProdmaFrame();
        menu.addEventMenuSelected(new MenuSelectedListener() {
            @Override
            public void menuSelected(int index) {
                switch (index) {
                    case 0:
                        setForm("DashBoard", dashboard);
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
    // private void setForm(JComponent com) {
    //     mainPanel.removeAll();
    //     if (com != null) {
    //         mainPanel.add(com);
    //     }
    //     mainPanel.repaint();
    //     mainPanel.revalidate();
    // }

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
   
    // private void initComponents() {

    //     panelBorder1 = new GUI.Admin.swing.PanelBorder();
    //     menu = new GUI.Admin.component.Menu();
    //     header = new GUI.Admin.component.Header();
    //     mainPanel = new javax.swing.JPanel();

    //     panelBorder1.setPreferredSize(new java.awt.Dimension(1100, 600));

    //     header.setFont(new java.awt.Font("sansserif", 0, 14)); 
    //     // header.addInputMethodListener(new java.awt.event.InputMethodListener() {
    //     //     public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
    //     //     }
    //     //     public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
    //     //         inputSearchEvent(evt);
    //     //     }
    //     // });

    //     javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
    //     mainPanel.setLayout(mainPanelLayout);
    //     mainPanelLayout.setHorizontalGroup(
    //         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    //         .addGap(0, 776, Short.MAX_VALUE)
    //     );
    //     mainPanelLayout.setVerticalGroup(
    //         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    //         .addGap(0, 0, Short.MAX_VALUE)
    //     );

    //     javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
    //     panelBorder1.setLayout(panelBorder1Layout);
    //     panelBorder1Layout.setHorizontalGroup(
    //         panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    //         .addGroup(panelBorder1Layout.createSequentialGroup()
    //             .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    //             .addGap(0, 0, 0)
    //             .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    //                 .addGroup(panelBorder1Layout.createSequentialGroup()
    //                     .addGap(6, 6, 6)
    //                     .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    //                     .addGap(0, 0, Short.MAX_VALUE))
    //                 .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)))
    //     );
    //     panelBorder1Layout.setVerticalGroup(
    //         panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    //         .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
    //         .addGroup(panelBorder1Layout.createSequentialGroup()
    //             .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    //             .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
    //             .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    //             .addContainerGap())
    //     );

    //     javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    //     getContentPane().setLayout(layout);
    //     layout.setHorizontalGroup(
    //         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    //         .addGroup(layout.createSequentialGroup()
    //             .addContainerGap()
    //             .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, 1168, Short.MAX_VALUE)
    //             .addContainerGap())
    //     );
    //     layout.setVerticalGroup(
    //         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    //         .addGroup(layout.createSequentialGroup()
    //             .addContainerGap()
    //             .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
    //             .addContainerGap())
    //     );

    //     setSize(1100,800);
    //     setLocationRelativeTo(null);
    //     setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    //     setUndecorated(true);
    //     setVisible(true);
    // }

private void initComponents() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // setUndecorated(true);
    // setBackground(new Color(0, 0, 0, 0));
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

    public void setMainPanel(JPanel panel) {
        contentPanel.removeAll(); // ✅ XÓA HẾT nội dung trước đó
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER); // ✅ chỉ add panel mới
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    

}

