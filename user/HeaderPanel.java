package user;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class HeaderPanel extends JPanel {
    private JPanel logoPanel, searchPanel, btnPanel;
    protected JLabel logoIcon, searchIcon, accountIcon, cartIcon, accountLabel;
    private JTextField searchBox;

    public HeaderPanel() {
        initComponents();
    }

    

    private void initComponents() {
        // Header panel settings
        setPreferredSize(new Dimension(1000, 100));
        setBackground(Color.decode("#0083B0"));
        setLayout(new BorderLayout());

        // ==== LOGO PANEL ====
        logoPanel = new JPanel();
        logoPanel.setPreferredSize(new Dimension(200, 100));
        logoPanel.setOpaque(false);

        logoIcon = new JLabel(new ImageIcon("user/Icon/logo.png")); 
        logoPanel.add(logoIcon);

        // ==== SEARCH PANEL ====
        searchPanel = new JPanel();
        searchPanel.setLayout(null);
        searchPanel.setPreferredSize(new Dimension(500, 100));
        searchPanel.setOpaque(false);

        searchIcon = new JLabel(new ImageIcon("user/Icon/search.png"));
        searchIcon.setBounds(10, 30, 30, 40);

        searchBox = new JTextField();
        searchBox.setBounds(50, 30, 400, 40);
        searchBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        searchPanel.add(searchIcon);
        searchPanel.add(searchBox);

        // ==== BUTTON PANEL ====
        btnPanel = new JPanel();
        btnPanel.setPreferredSize(new Dimension(300, 100));
        btnPanel.setOpaque(false);
        btnPanel.setLayout(null);

        accountIcon = new JLabel(new ImageIcon("user/Icon/user.png"));
        accountIcon.setBounds(20, 30, 30, 40);

        cartIcon = new JLabel(new ImageIcon("user/Icon/shopping-cart.png"));
        cartIcon.setBounds(180, 30, 40, 40);

        accountLabel = new JLabel("");
        accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        accountLabel.setForeground(Color.WHITE);
        accountLabel.setBounds(60, 40, 100, 20);

        btnPanel.add(accountIcon);
        btnPanel.add(accountLabel);
        btnPanel.add(cartIcon);

        
        // ==== ADD TO HEADER ====
        add(logoPanel, BorderLayout.WEST);
        add(searchPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.EAST);
    }
}

