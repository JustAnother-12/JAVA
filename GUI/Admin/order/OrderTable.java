package GUI.Admin.order;

import DTO.Order_DTO;
import DTO.OrderDetail_DTO;
import GUI.Admin.component.Header;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.*;
import GUI.Admin.swing.NutGiaoDien_BLL;
import GUI.Admin.swing.NutSuKien_BLL;
import BLL.DonHang_BLL;


public class OrderTable extends javax.swing.JPanel implements Header.searchListener{
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Order_DTO> orderList = new ArrayList<>();
    private ArrayList<OrderDetail_DTO> orderDetailList = new ArrayList<>();
    private DonHang_BLL DonHang_BLL = new DonHang_BLL();

    public OrderTable() {
        initComponents();
        setLayout(null);
        setBounds(0,0, 900, 650);
        setBackground(Color.WHITE);
        this.setName("order");

        // Nút sắp xếp tăng
        JButton btnSortAsc = new JButton("Sắp xếp Tăng");
        btnSortAsc.setBounds(20, 0, 150, 30);
        add(btnSortAsc);

        // Nút sắp xếp giảm
        JButton btnSortDesc = new JButton("Sắp xếp Giảm");
        btnSortDesc.setBounds(200, 0, 150, 30);
        add(btnSortDesc);
        
        btnSortAsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTable(true); // Sắp xếp tăng
            }
        });

        btnSortDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTable(false); // Sắp xếp giảm
            }
        });
        

        // Cấu hình bảng
        String[] columnNames = {"Mã đơn hàng", "Tên khách hàng", "Tình trạng", "Ngày đặt", "Tổng tiền", "Tác vụ"};
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
        DonHang_BLL.LoadDataToTabel(tableModel, orderList, orderDetailList);
        // Cột "Tác vụ" có 3 nút "Xác nhận" "Chi tiết" và "Xóa"
        table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien_BLL("order",orderList));
        table.getColumn("Tác vụ").setCellEditor(new NutSuKien_BLL(this, tableModel));
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Mã đơn hàng
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên khách hàng
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Tình trạng
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Ngày đặt
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Tổng tiền
        table.getColumnModel().getColumn(5).setPreferredWidth(250); // Tác vụ 
        table.setDefaultEditor(Object.class, null);
        // ScrollPane chứa bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(2, 40 , 860, 570);
        add(scrollPane);
        revalidate();
        repaint();
    }
    public void searchById(String text) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null); // Hiện toàn bộ
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 0)); // Theo cột ID
        }
    }
    public void searchByName(String text) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null); // Hiện toàn bộ
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 1)); // Theo cột ID
        }
    }
    @Override
    public void onSearch(String text) {
        searchById(text);
        searchByName(text);
    }
    @Override
    public void onFilterByRole(String role) {
        
    }
    public ArrayList<Order_DTO> getOrderList() {
        return this.orderList;
    }
    private void sortTable(boolean ascending) {
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




    public Order_DTO getOrderAt(int rowIndex) {
        Order_DTO temp = this.orderList.get(rowIndex);
        return temp;
    }
    
    public ArrayList<OrderDetail_DTO> getOrderDetailList() {
        return orderDetailList;
    }
    
    
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.BorderLayout());

    }
}
