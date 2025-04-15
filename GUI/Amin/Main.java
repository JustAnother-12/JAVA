package GUI.Amin;
import javax.swing.*;
public class Main extends JFrame {
    AccountTable accountForm;
    CustomerTable customerForm;
    OrderTable ordeForm;
    ProdmaFrame productForm;
    /**
     * Creates new form Main
     */
    private final JLabel placeholder = new JLabel("");
    private JToggleButton bt1;
    public Main() {
        initComponents();
        myinit();
        setBackground(new Color(0, 0, 0, 0));
        menu1.initMoving(Main.this);
        setSize(1000,800);
        accountForm = new AccountTable();
        customerForm = new CustomerTable();
        ordeForm = new OrderTable();
        productForm = new ProdmaFrame();
        menu1.addEventMenuSelected(new MenuSelectedListener() {
            @Override
            public void menuSelected(int index) {
                if (index == 1) {
                    setForm(productForm);
                    header2.setSearchListener(customerForm);
                    header2.getjLabel2().setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/none.jpg")));
                }
                else if (index == 2) {
                    setForm(accountForm);
                    header2.getjLabel2().setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/menu.png")));
                    header2.setSearchListener(accountForm);
                } else if (index == 3) {
                    setForm(customerForm);
                    header2.setSearchListener(customerForm);
                    header2.getjLabel2().setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/none.jpg")));
                } else if (index == 4) {
                    setForm(ordeForm);
                    header2.setSearchListener(ordeForm);
                    header2.getjLabel2().setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/none.jpg")));
                } else if (index == 1) {
                    setForm(new JPanel());
                }
            }
        });
        
    }
    private void setForm(JComponent com) {
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
    private void myinit() {
        placeholder.setPreferredSize(header2.getjLabel2().getPreferredSize());
        placeholder.setMinimumSize(header2.getjLabel2().getMinimumSize());
        placeholder.setMaximumSize(header2.getjLabel2().getMaximumSize());
    }
   
    private void initComponents() {

        panelBorder1 = new com.raven.swing.PanelBorder();
        menu1 = new com.raven.component.Menu();
        header2 = new com.raven.component.Header();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelBorder1.setPreferredSize(new java.awt.Dimension(1000, 600));

        header2.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        header2.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                inputSearchEvent(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 776, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addComponent(menu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBorder1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(header2, javax.swing.GroupLayout.DEFAULT_SIZE, 953, Short.MAX_VALUE)))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addComponent(header2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, 1168, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                .addContainerGap())
        );

        setLocationRelativeTo(null);
    }



    public static void main(String args[]) {
        new Main().setVisible(true);
    }

    private com.raven.component.Header header2;
    private javax.swing.JPanel mainPanel;
    private com.raven.component.Menu menu1;
    private com.raven.swing.PanelBorder panelBorder1;
}

