package GUI.Admin.customer;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import BLL.KhachHang_BLL;
import GUI.Admin.swing.NutGiaoDien;
import GUI.Admin.swing.NutSuKien;
import utils.*;
import DTO.KhachHang_DTO;

public class CustomerTable extends javax.swing.JPanel implements GUI.Admin.component.Header.searchListener{
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<KhachHang_DTO> customerList = new ArrayList<>();
    public CustomerTable() {
        initComponents();
        setLayout(null);
        setBounds(0,0, 900, 650);
        setBackground(Color.WHITE);
        
        // Nút sắp xếp tăng
        MyButton btnSortAsc = new MyButton("Sắp xếp Tăng");
        btnSortAsc.setFont(new Font("Segoe UI", 0, 14));
        btnSortAsc.setBackground(Color.decode("#00B4DB"));
        btnSortAsc.setBounds(20, 0, 150, 30);
        add(btnSortAsc);
        this.setName("customer");
    
        // Nút sắp xếp giảm
        MyButton btnSortDesc = new MyButton("Sắp xếp Giảm");
        btnSortDesc.setFont(new Font("Segoe UI", 0, 14));
        btnSortDesc.setBackground(Color.decode("#00B4DB"));
        btnSortDesc.setBounds(200, 0, 150, 30);
        add(btnSortDesc);
        // Cấu hình bảng
        String[] columnNames = {"ID", "Họ tên", "Username", "Số Điện thoại", "Tác vụ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);

        // Căn giữa dữ liệu trong bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Thiết lập renderer cho từng cột
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Thêm dữ liệu mẫu
        KhachHang_BLL KhachHang_BLL = new KhachHang_BLL();
        KhachHang_BLL.LoadDataToTabel(tableModel, customerList);
        KhachHang_BLL.LoadDataToTabel(null, customerList);
        // Cột "Tác vụ" có 2 nút "Chi tiết" và "Xóa"
        table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien("customer"));
        table.getColumn("Tác vụ").setCellEditor(new NutSuKien(this,tableModel));
        table.setDefaultEditor(Object.class, null);

        // ScrollPane chứa bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        scrollPane.setBounds(2, 40 , 860, 570);
        add(scrollPane);
        revalidate();
        repaint();
        btnSortAsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTable(true,tableModel,table); // Sắp xếp tăng
            }
        });

        btnSortDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTable(false,tableModel,table); // Sắp xếp giảm
            }
        });
    }
    public ArrayList<KhachHang_DTO> getCustomerList() {
        return this.customerList;
    }
    @Override
    public void onSearch(String text) {
        searchById(text,tableModel,table);
        searchByName(text, tableModel, table);
    }
    @Override
    public void onFilterByRole(String role) {
        
    }
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.BorderLayout());
    }
     public void searchById(String text,DefaultTableModel tableModel,JTable table) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null); // Hiện toàn bộ
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 0)); // Theo cột ID
        }
    }
    public void searchByName(String text,DefaultTableModel tableModel,JTable table) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null); // Hiện toàn bộ
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 1)); // Theo cột Name
        }
    }
    public void sortTable(boolean ascending,DefaultTableModel tableModel,JTable table) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (ascending) {
            sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        }
        else {
            sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0 , SortOrder.DESCENDING)));
        }
        sorter.sort();
    }
}
