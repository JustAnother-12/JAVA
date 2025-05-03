package GUI.Admin.product;

import javax.swing.*;
import java.awt.*;

import javax.swing.table.TableCellEditor;

import utils.MyButton;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private MyButton detailButton;
    private MyButton editButton;
    private MyButton deleteButton;
    private MyButton importButton;

    private int currentRow;

    public ButtonEditor(JCheckBox checkBox, ProdmaFrame parent) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(true);

        importButton = new MyButton("Nhập hàng");
        importButton.setFont(new Font("Segoe UI", 0, 12));
        importButton.setBackground(new Color(255, 153, 0));
        importButton.setForeground(Color.WHITE);
        importButton.setPreferredSize(new Dimension(200, 25));
        importButton.addActionListener(e -> {
            fireEditingStopped();
            parent.addToImportList(currentRow);
        });

        detailButton = new MyButton("Chi tiết");
        detailButton.setFont(new Font("Segoe UI", 0, 12));
        detailButton.setBackground(new Color(255, 153, 0));
        detailButton.setForeground(Color.WHITE);
        detailButton.setPreferredSize(new Dimension(200, 25));
        detailButton.addActionListener(e -> {
            fireEditingStopped();
            parent.detailButtonActionPerformed(currentRow);
        });

        editButton = new MyButton("Sửa");
        editButton.setFont(new Font("Segoe UI", 0, 12));
        editButton.setBackground(new Color(255, 153, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setPreferredSize(new Dimension(200, 25));
        editButton.addActionListener(e -> {
            fireEditingStopped();
            parent.editButtonActionPerformed(currentRow);
        });

        deleteButton = new MyButton("Xóa");
        deleteButton.setFont(new Font("Segoe UI", 0, 12));
        deleteButton.setBackground(new Color(255, 153, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(200, 25));
        deleteButton.addActionListener(e -> {
            fireEditingStopped();
            parent.removeButtonActionPerformed(currentRow);
        });

        panel.add(importButton);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(detailButton);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(editButton);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(deleteButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentRow = row;
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

}
