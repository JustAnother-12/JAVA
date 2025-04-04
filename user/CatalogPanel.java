package user;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class CatalogPanel extends JPanel {
    private JLabel headerLabel;
    private JPanel listPanel;
    private JLabel sachLabel = new JLabel("SÁCH");
    private JLabel voLabel = new JLabel("VỞ");
    private JLabel butLabel = new JLabel("BÚT");
    private JLabel list[] = {sachLabel, voLabel, butLabel};

    public CatalogPanel() {
        initComponents();
    }

    private void initComponents() {
        headerLabel = new JLabel();
        listPanel = new JPanel();

        for (JLabel lb : list) {
            lb.setFont(new Font("Segoe UI", 1, 20));
            lb.setForeground(Color.WHITE);
            // lb.addMouseListener(mouseListener);
            listPanel.add(lb);
            listPanel.add(Box.createVerticalStrut(15));
        }

        headerLabel.setFont(new Font("Segoe UI", 1, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setText("DANH MỤC");
        headerLabel.setBackground(Color.decode("#00B4DB"));
        headerLabel.setOpaque(true);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.decode("#00B4DB"));
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        setPreferredSize(new Dimension(200, 540));
        setBackground(Color.decode("#cfdef3"));
        setLayout(new BorderLayout());

        add(headerLabel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
    }
}
