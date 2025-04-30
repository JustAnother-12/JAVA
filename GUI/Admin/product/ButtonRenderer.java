package GUI.Admin.product;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.TableCellRenderer;

class ButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton detailButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton importButton;

    public ButtonRenderer() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(true);

        importButton = new JButton("Nhập hàng");
        importButton.setFont(new Font("Segoe UI", 0, 12));
        importButton.setBackground(new Color(255, 153, 0));
        importButton.setForeground(Color.WHITE);
        importButton.setPreferredSize(new Dimension(200, 25));

        detailButton = new JButton("Chi tiết");
        detailButton.setFont(new Font("Segoe UI", 0, 12));
        detailButton.setBackground(new Color(255, 153, 0));
        detailButton.setForeground(Color.WHITE);
        detailButton.setPreferredSize(new Dimension(200, 25));
        
        editButton = new JButton("Sửa");
        editButton.setFont(new Font("Segoe UI", 0, 12));
        editButton.setBackground(new Color(255, 153, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setPreferredSize(new Dimension(150, 25));
        
        deleteButton = new JButton("Xóa");
        deleteButton.setFont(new Font("Segoe UI", 0, 12));
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(150, 25));

        
        add(importButton);
        add(Box.createHorizontalStrut(5));
        add(detailButton);
        add(Box.createHorizontalStrut(5));
        add(editButton);
        add(Box.createHorizontalStrut(5));
        add(deleteButton);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}
