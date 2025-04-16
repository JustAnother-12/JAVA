package GUI.Admin.component;

import java.awt.*;
import java.util.logging.Logger;
import java.util.*;
import javax.swing.*;

import DAO.DatabaseConnection;
import GUI.Admin.swing.SearchText;

public class Header extends JPanel {

    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JToggleButton jToggleButton1;
    private SearchText searchText1;

    public interface searchListener{
        void onSearch(String text);
        void onFilterByRole(String role);
    }
    private searchListener searchListener;

    public void setSearchListener(searchListener listener) {
        this.searchListener = listener;

        // Gán hành động khi nhập liệu
        searchText1.addEvent((txt) -> {
            if (searchListener != null) {
                searchListener.onSearch(txt);
            }
        });
        // Gán hành động khi nhấn jLabel2 (lọc theo vai trò)
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showRolePopup(jLabel2);
            }
        });
    }

    public JLabel getjLabel2() {
        return jLabel2;
    }
    
    private void showRolePopup(JLabel label) {
        JPopupMenu popup = new JPopupMenu();

        ArrayList<String> roles = DatabaseConnection.getAllRoles();
        roles.add(0, "Tất cả");

        for (String role : roles) {
            JMenuItem item = new JMenuItem(role);
            item.addActionListener(e -> {
                if (searchListener != null) {
                    searchListener.onFilterByRole(role);
                }
            });
            popup.add(item);
        }

        popup.show(label, 0, label.getHeight());
    }
    public Header() {
        initComponents();
        myinitcomponent();
        setOpaque(false);
        setSize(800, 150);
    }
    
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        searchText1 = new SearchText();
        jLabel2 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();

        jButton1.setText("jButton1");

        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setName(""); 

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/search.png"))); 

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/menu.png"))); 
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/raven/icon/close.jpg"))); 
        jToggleButton1.setBorder(null);
        jToggleButton1.setBorderPainted(false);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchText1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(searchText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jToggleButton1.getAccessibleContext().setAccessibleParent(null);

        getAccessibleContext().setAccessibleName("");
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private static final Logger LOG = Logger.getLogger(Header.class.getName());
    private void myinitcomponent() {
        JButton closeButton = new JButton("/com/raven/icon/close.jpg");
        closeButton.addActionListener(e ->{
            System.exit(0);
        });
        this.add(closeButton);
    }
    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(0, 0, 25, getHeight());
        g2.fillRect(getWidth() - 25, getHeight() - 25, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }
}
