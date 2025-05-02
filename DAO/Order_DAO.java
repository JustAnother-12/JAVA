package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import GUI.Admin.swing.FormatDate_BLL;

public class Order_DAO {
    private Connection con;

    public boolean OpenConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ban_van_phong_pham", "root", "");
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (con != null){
                con.close();
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void loadDataFormDatabase(DefaultTableModel tableModel,ArrayList<Order_DTO> orderList,ArrayList<OrderDetail_DTO> orderDetailList) {
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
                Order_DTO order = new Order_DTO(madonhang, diachidat, ngaydat, tinhtrang, tongtien, manv, makh);
                orderList.add(order);
                FormatDate_BLL temp = new FormatDate_BLL();
                String Date = temp.convertDateFormat(ngaydat);
                tableModel.addRow(new Object[] {
                madonhang,tenkh,tinhtrang,Date,tongtien,"Tác vụ"
                //Hiển thị lên bảng (JTable)
            });
            // Lấy dữ liệu chi tiết đơn hàng
            String masp = rs.getString("masp");
            int soluong = rs.getInt("soluong");
            double dongia = rs.getDouble("dongia");
            double thanhtien = rs.getDouble("thanhtien");

            // Tạo đối tượng OrderDetail
            OrderDetail_DTO detail = new OrderDetail_DTO(madonhang, masp, soluong, dongia, thanhtien);
            orderDetailList.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // orderList = orderbll.getAllOrder();
        // for(Order_DTO order : orderList){
        //     OrderDetail_DTO detail = orderbll.getDetails(order.getMadonhang());
        //     orderDetailList.add(detail);
        // }
    }

    public ArrayList<Order_DTO> getAllOrder(){
        ArrayList<Order_DTO> arr = new  ArrayList<>();
        if (OpenConnection()){
            try{
                String query = "SELECT * FROM DONHANG";
        
                PreparedStatement pstmt = con.prepareStatement(query);
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
                    // String tenkh = "";

                    // // Tìm tên khách hanngf trong csdl
                    // String queryFindUser = "SELECT tenkh FROM KHACHHANG WHERE makh = ?";
                    // try (PreparedStatement findName = con.prepareStatement(queryFindUser)) {
                    //     findName.setString(1, makh);
                    //     ResultSet rsTenKH = findName.executeQuery();
                    //     if (rsTenKH.next()) {
                    //         tenkh = rsTenKH.getString("tenkh");
                    //     }
                    // }

                    // Tạo đối tượng Order
                    Order_DTO order = new Order_DTO(madonhang, diachidat, ngaydat, tinhtrang, tongtien, manv, makh);
                    arr.add(order);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return arr;
    }

    public OrderDetail_DTO getDetailForOrder(String id){
        if (OpenConnection()){
            try{
                String query = "SELECT * FROM CHITIETDONHANG WHERE CHITIETDONHANG.madonhang=?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1,id);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String madonhang = rs.getString("madonhang");
                    String masp = rs.getString("masp");
                    int soluong = rs.getInt("soluong");
                    double dongia = rs.getDouble("dongia");
                    double thanhtien = rs.getDouble("thanhtien");

                    // Tạo đối tượng OrderDetail
                    OrderDetail_DTO detail = new OrderDetail_DTO(madonhang, masp, soluong, dongia, thanhtien);
                    return detail;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return null;
    }

    public ArrayList<OrderDetail_DTO> getAllDetail(){
        ArrayList<OrderDetail_DTO> arr = new  ArrayList<>();
        if (OpenConnection()){
            try{
                String query = "SELECT * FROM CHITIETDONHANG";
                Statement pstmt = con.createStatement();
                ResultSet rs = pstmt.executeQuery(query);
                while (rs.next()) {
                    String madonhang = rs.getString("madonhang");
                    String masp = rs.getString("masp");
                    int soluong = rs.getInt("soluong");
                    double dongia = rs.getDouble("dongia");
                    double thanhtien = rs.getDouble("thanhtien");

                    // Tạo đối tượng OrderDetail
                    OrderDetail_DTO detail = new OrderDetail_DTO(madonhang, masp, soluong, dongia, thanhtien);
                    arr.add(detail);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return arr;
    }

    public boolean ConfirmOrder(String id){
        if (OpenConnection()){
            try{
                String query = "Update donhang set tinhtrang = 'Đã xử lý' where madonhang = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1,id);
                if (stmt.executeUpdate()>=1)
                    return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return false;
    }

    public boolean AddOrder(Order_DTO order, OrderDetail_DTO details){
        if (OpenConnection()) {
            try {                    
                String query1 = "INSERT INTO DONHANG VALUES(?,?,?,?,?,?,?)";
                String query2 = "INSERT INTO CHITIETDONHANG VALUES(?,?,?,?,?)";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt1.setString(1, order.getMadonhang());
                stmt1.setString(2, order.getDiachidat());
                stmt1.setString(3, order.getNgaydat());
                stmt1.setString(4, order.getTinhtrang());
                stmt1.setDouble(5, order.getTongtien());
                stmt1.setString(6, order.getManv());
                stmt1.setString(7, order.getMakh());

                stmt2.setString(1, details.getMadonhang());
                stmt2.setString(2, details.getMasp());
                stmt2.setInt(3, details.getSoluong());
                stmt2.setDouble(4, details.getDongia());
                stmt2.setDouble(5, details.getThanhtien());

                if (stmt1.executeUpdate()>=1 && stmt2.executeUpdate()>=1)
                    return true;
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally{
                closeConnection();  
            } 
        }
        return false;
    }

    public boolean DeleteOrder(String id){
        if (OpenConnection()){
            try{
                String deleteChiTietSql = "DELETE FROM CHITETDONHANG WHERE madonhang = ?";
                String deleteDonHangSql = "DELETE FROM DONHANG WHERE madonhang = ?";
                PreparedStatement deleteChiTietStmt = con.prepareStatement(deleteChiTietSql);
                PreparedStatement deleteDonHangStmt = con.prepareStatement(deleteDonHangSql);
                
                // Xóa chi tiết đơn hàng
                deleteChiTietStmt.setString(1, id);
                // Xóa đơn hàng
                deleteDonHangStmt.setString(1, id);
                if (deleteDonHangStmt.executeUpdate() >=1 &&  deleteChiTietStmt.executeUpdate() >= 1){
                    return true;
                }
                System.out.println("Đơn hàng và chi tiết đơn hàng đã được xóa thành công.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return false;
    }

    public boolean removeDetailBySP(String id){
        boolean result = false;
        if (OpenConnection()) {
            try {                   
                con.setAutoCommit(false); 
    
                // Xóa chi tiết đơn hàng
                String query = "DELETE FROM CHITIETDONHANG WHERE madonhang = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, id);
                int rowsAffected = stmt.executeUpdate();
    
                if (rowsAffected >= 1) {
                    // Kiểm tra nếu không còn chi tiết nào nữa
                    String checkQuery = "SELECT COUNT(*) AS total FROM CHITIETDONHANG WHERE madonhang = ?";
                    PreparedStatement checkStmt = con.prepareStatement(checkQuery);
                    checkStmt.setString(1, id);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt("total") == 0) {
                        // Xóa luôn phiếu nhập
                        String deletePN = "DELETE FROM DONHANG WHERE madonhang = ?";
                        PreparedStatement delStmt = con.prepareStatement(deletePN);
                        delStmt.setString(1, id);
                        delStmt.executeUpdate(); // không cần kiểm tra số dòng vì COUNT trước đó đã xác nhận
                    }
    
                    con.commit();
                    result = true;
                } else {
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally{
                try{
                    con.setAutoCommit(true);
                } catch(SQLException e){
                    System.out.println(e);
                }
                closeConnection();  
            } 
        }
        return result;
    }

    public boolean hasDetailID(String id){
        if (OpenConnection()) {
            try {            
                String sql = "SELECT * FROM CHITIETDONHANG WHERE CHITIETDONHANG.madonhang='"+id+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next())
                    return true;
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                closeConnection(); 
            }   
        }
        return false;
    }

    public boolean hasOrderID(String id){
        if (OpenConnection()) {
            try {            
                String sql = "SELECT * FROM DONHANG WHERE DONHANG.madonhang='"+id+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next())
                    return true;
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                closeConnection(); 
            }   
        }
        return false;
    }
    
}
