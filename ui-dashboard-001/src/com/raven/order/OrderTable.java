/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Beans/Customizer.java to edit this template
 */
package com.raven.order;

import com.raven.account.AccountTable;
import com.raven.account.AddAccount;
import com.raven.account.ButtonEditor;
import com.raven.account.ButtonRenderer;
import com.raven.account.customer;
import com.raven.account.database.DatabaseConnection;
import com.raven.account.staff;
import com.raven.component.Header;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.sql.SQLException;
import javax.swing.RowFilter;

/**
 *
 * @author ADMIN
 */
public class OrderTable extends javax.swing.JPanel implements Header.searchListener{
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<order> orderList = new ArrayList<>();
    private ArrayList<orderDetail> orderDetailList = new ArrayList<>();
    public OrderTable() {
        initComponents();
        setLayout(null);
        setBounds(0,0, 800, 650);
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
        loadDataFormDatabase();
        // Cột "Tác vụ" có 3 nút "Xác nhận" "Chi tiết" và "Xóa"
        table.getColumn("Tác vụ").setCellRenderer(new ButtonRenderer("order",orderList));
        table.getColumn("Tác vụ").setCellEditor(new ButtonEditor(this));
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Mã đơn hàng
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên khách hàng
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Tình trạng
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Ngày đặt
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Tổng tiền
        table.getColumnModel().getColumn(5).setPreferredWidth(230); // Tác vụ 
        // ScrollPane chứa bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(2, 40 , 770, 570);
        add(scrollPane);
        revalidate();
        repaint();
    }
    @Override
    public void setName(String name) {
        super.setName(name); 
    }

    @Override
    public String getName() {
        return super.getName(); 
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
    @Override
    public void onSearch(String text) {
        searchById(text);
    }
    @Override
    public void onFilterByRole(String role) {
        
    }
    public ArrayList<order> getOrderList() {
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
    private void loadDataFormDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
        String query = "SELECT d.madonhang, d.diachidat, d.ngaydat, d.tinhtrang, d.tongtien, d.manv, d.makh, ct.masp, ct.soluong, ct.dongia, ct.thanhtien FROM DONHANG d JOIN CHITIETDONHANG ct ON d.madonhang = ct.madonhang";
        
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            // Lấy dữ liệu đơn hàng
            String madonhang = rs.getString("madonhang");
            String diachidat = rs.getString("diachidat");
            String ngaydat = rs.getString("ngaydat");
            String tinhtrang = rs.getString("tinhtrang");
            double tongtien = rs.getDouble("tongtien");
            String manv = rs.getString("manv");
            String makh = rs.getString("makh");
            String tenkh = "";
            String queryFindUser = "SELECT tenkh FROM KHACHHANG WHERE makh = ?";
            try (PreparedStatement findName = conn.prepareStatement(queryFindUser)) {
                findName.setString(1, makh);
                ResultSet rsTenKH = findName.executeQuery();
                if (rsTenKH.next()) {
                    tenkh = rsTenKH.getString("tenkh");
                }
            }
            // Tạo đối tượng Order
            order order = new order(madonhang, diachidat, ngaydat, tinhtrang, tongtien, manv, makh);
            orderList.add(order);
            String Date = convertDateFormat(ngaydat);
            tableModel.addRow(new Object[] {
                madonhang,tenkh,tinhtrang,Date,tongtien,"Xác nhận" + "Chi tiết" + "Xoá"
            });
            // Lấy dữ liệu chi tiết đơn hàng
            String masp = rs.getString("masp");
            int soluong = rs.getInt("soluong");
            double dongia = rs.getDouble("dongia");
            double thanhtien = rs.getDouble("thanhtien");

            // Tạo đối tượng OrderDetail
            orderDetail detail = new orderDetail(madonhang, masp, soluong, dongia, thanhtien);
            orderDetailList.add(detail);

            // Hiển thị lên bảng (JTable)
//            tableModel.addRow(new Object[]{
//                madonhang, ngaydat, tinhtrang, masp, soluong, dongia, thanhtien, "Xóa"
//            });
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    public order getOrderAt(int rowIndex) {
        order temp = this.orderList.get(rowIndex);
        return temp;
    }
    public ArrayList<orderDetail> getOrderDetailList() {
        return orderDetailList;
    }
    
    public static String convertDateFormat(String inputDate) {
        java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return inputDate; // Nếu xảy ra lỗi, trả về định dạng ban đầu.
        }
    }
    public void cofirmOrder(String id) {
        String query = "Update donhang set tinhtrang=Đã xử lý where madonhang = ?";
        try (Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,id);
            stmt.executeUpdate();
            loadDataFormDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteOrder(String id) {
        String deleteChiTietSql = "DELETE FROM CHITETDONHANG WHERE madonhang = ?";
        String deleteDonHangSql = "DELETE FROM DONHANG WHERE madonhang = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
             PreparedStatement deleteChiTietStmt = conn.prepareStatement(deleteChiTietSql);
             PreparedStatement deleteDonHangStmt = conn.prepareStatement(deleteDonHangSql);
             
            // Xóa chi tiết đơn hàng
            deleteChiTietStmt.setString(1, id);
            deleteChiTietStmt.executeUpdate();

            // Xóa đơn hàng
            deleteDonHangStmt.setString(1, id);
            deleteDonHangStmt.executeUpdate();
            loadDataFormDatabase();
            System.out.println("Đơn hàng và chi tiết đơn hàng đã được xóa thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.BorderLayout());

    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
