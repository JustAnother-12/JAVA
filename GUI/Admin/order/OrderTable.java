package GUI.Admin.order;

import DAO.DatabaseConnection;
import DTO.Order_DTO;
import DTO.OrderDetail_DTO;
import GUI.Admin.component.Header;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.table.*;
import BLL.NutGiaoDien_BLL;
import BLL.NutSuKien_BLL;
import BLL.Order_BLL;


public class OrderTable extends javax.swing.JPanel implements Header.searchListener{
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Order_DTO> orderList = new ArrayList<>();
    private ArrayList<OrderDetail_DTO> orderDetailList = new ArrayList<>();
    private Order_BLL orderbll = new Order_BLL();

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
        table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien_BLL("order"));
        table.getColumn("Tác vụ").setCellEditor(new NutSuKien_BLL(this, tableModel));
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




    private void loadDataFormDatabase() {
//         try (Connection conn = DatabaseConnection.getConnection()) {
//         String query = "SELECT d.madonhang, d.diachidat, d.ngaydat, d.tinhtrang, d.tongtien, d.manv, d.makh, ct.masp, ct.soluong, ct.dongia, ct.thanhtien FROM DONHANG d JOIN CHITIETDONHANG ct ON d.madonhang = ct.madonhang";
//         PreparedStatement pstmt = conn.prepareStatement(query);
//         ResultSet rs = pstmt.executeQuery();

//         while (rs.next()) {
//             // Lấy dữ liệu đơn hàng
//             String madonhang = rs.getString("madonhang");
//             String diachidat = rs.getString("diachidat");
//             String ngaydat = rs.getString("ngaydat");
//             String tinhtrang = rs.getString("tinhtrang");
//             double tongtien = rs.getDouble("tongtien");
//             String manv = rs.getString("manv");
//             String makh = rs.getString("makh");
//             String tenkh = "";
//             String queryFindUser = "SELECT tenkh FROM KHACHHANG WHERE makh = ?";
//             try (PreparedStatement findName = conn.prepareStatement(queryFindUser)) {
//                 findName.setString(1, makh);
//                 ResultSet rsTenKH = findName.executeQuery();
//                 if (rsTenKH.next()) {
//                     tenkh = rsTenKH.getString("tenkh");
//                 }
//             }
//             // Tạo đối tượng Order
//             Order_DTO order = new Order_DTO(madonhang, diachidat, ngaydat, tinhtrang, tongtien, manv, makh);
//             orderList.add(order);
//             String Date = convertDateFormat(ngaydat);
//             tableModel.addRow(new Object[] {
//                 madonhang,tenkh,tinhtrang,Date,tongtien,"Xác nhận" + "Chi tiết" + "Xoá"
//             });
//             // Lấy dữ liệu chi tiết đơn hàng
//             String masp = rs.getString("masp");
//             int soluong = rs.getInt("soluong");
//             double dongia = rs.getDouble("dongia");
//             double thanhtien = rs.getDouble("thanhtien");

//             // Tạo đối tượng OrderDetail
//             OrderDetail_DTO detail = new OrderDetail_DTO(madonhang, masp, soluong, dongia, thanhtien);
//             orderDetailList.add(detail);

//             // Hiển thị lên bảng (JTable)
// //            tableModel.addRow(new Object[]{
// //                madonhang, ngaydat, tinhtrang, masp, soluong, dongia, thanhtien, "Xóa"
// //            });
//         }
//     } catch (Exception e) {
//         e.printStackTrace();
//     }
        orderList = orderbll.getAllOrder();
        for(Order_DTO order : orderList){
            OrderDetail_DTO detail = orderbll.getDetails(order.getMadonhang());
            orderDetailList.add(detail);
        }
    }



    public Order_DTO getOrderAt(int rowIndex) {
        Order_DTO temp = this.orderList.get(rowIndex);
        return temp;
    }
    
    public ArrayList<OrderDetail_DTO> getOrderDetailList() {
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


    // public void cofirmOrder(String id) {
    //     String query = "Update donhang set tinhtrang=Đã xử lý where madonhang = ?";
    //     try (Connection conn = DatabaseConnection.getConnection()){
    //         PreparedStatement stmt = conn.prepareStatement(query);
    //         stmt.setString(1,id);
    //         stmt.executeUpdate();
    //         loadDataFormDatabase();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }


    // public void deleteOrder(String id) {
    //     String deleteChiTietSql = "DELETE FROM CHITETDONHANG WHERE madonhang = ?";
    //     String deleteDonHangSql = "DELETE FROM DONHANG WHERE madonhang = ?";
        
    //     try (Connection conn = DatabaseConnection.getConnection()) {
    //          PreparedStatement deleteChiTietStmt = conn.prepareStatement(deleteChiTietSql);
    //          PreparedStatement deleteDonHangStmt = conn.prepareStatement(deleteDonHangSql);
             
    //         // Xóa chi tiết đơn hàng
    //         deleteChiTietStmt.setString(1, id);
    //         deleteChiTietStmt.executeUpdate();

    //         // Xóa đơn hàng
    //         deleteDonHangStmt.setString(1, id);
    //         deleteDonHangStmt.executeUpdate();
    //         loadDataFormDatabase();
    //         System.out.println("Đơn hàng và chi tiết đơn hàng đã được xóa thành công.");
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
    
    
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.BorderLayout());

    }
}
