package GUI.Admin.importorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import BLL.PhieuNhap_BLL;
import DTO.PhieuNhap_DTO;
import GUI.Admin.swing.NutGiaoDien_BLL;
import GUI.Admin.swing.NutSuKien_BLL;

public class HistoryTable extends JPanel implements GUI.Admin.component.Header.searchListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<PhieuNhap_DTO> importList = new ArrayList<>();

    public HistoryTable() {
        initComponents();
        setLayout(null);
        setBounds(0, 0, 900, 650);
        setBackground(Color.WHITE);
        this.setName("import");

        JButton btnSortAsc = new JButton("Sắp xếp Tăng");
        btnSortAsc.setBounds(20, 0, 150, 30);
        add(btnSortAsc);

        JButton btnSortDesc = new JButton("Sắp xếp Giảm");
        btnSortDesc.setBounds(200, 0, 150, 30);
        add(btnSortDesc);

        String[] columnNames = {"Mã Phiếu", "Mã NCC", "Mã NV", "Ngày Nhập", "Tác vụ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        PhieuNhap_BLL bll = new PhieuNhap_BLL();
        bll.loadToTable(tableModel, importList);

        table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien_BLL("import"));
        table.getColumn("Tác vụ").setCellEditor(new NutSuKien_BLL(this, tableModel));
        table.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(2, 40, 860, 570);
        add(scrollPane);
        revalidate();
        repaint();

        // Dùng ActionListener theo kiểu truyền thống để tránh warning "unused lambda parameter"
        btnSortAsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTable(true);
            }
        });

        btnSortDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTable(false);
            }
        });
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }

    @Override
    public void onSearch(String text) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 0)); // Tìm theo mã phiếu
        }
    }

    @Override
    public void onFilterByRole(String role) {
        // Không áp dụng
    }

    public void sortTable(boolean ascending) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        SortOrder order = ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING;
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0, order)));
        sorter.sort();
    }

    public ArrayList<PhieuNhap_DTO> getImportList() {
        return importList;
    }
}


