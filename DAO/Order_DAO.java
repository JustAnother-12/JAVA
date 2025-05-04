package DAO;

import DTO.*;
import utils.FormatDate;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

public class Order_DAO {
    private Connection con;

    public void loadDataFormDatabase(DefaultTableModel tableModel, ArrayList<Order_DTO> orderList, ArrayList<OrderDetail_DTO> orderDetailList) {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String query = "SELECT d.madonhang, d.diachidat, d.ngaydat, d.tinhtrang, d.tongtien, d.manv, d.makh, ct.masp, ct.soluong, ct.dongia, ct.thanhtien FROM DONHANG d JOIN CHITIETDONHANG ct ON d.madonhang = ct.madonhang";
                PreparedStatement pstmt = con.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();

                Set<String> addedOrderIds = new HashSet<>();

                while (rs.next()) {
                    // Lấy dữ liệu đơn hàng
                    String madonhang = rs.getString("madonhang");
                    String diachidat = rs.getString("diachidat");
                    String ngaydat = rs.getString("ngaydat");
                    String tinhtrang = rs.getString("tinhtrang");
                    double tongtien = rs.getDouble("tongtien");
                    String manv = rs.getString("manv");
                    String makh = rs.getString("makh");

                    // Chỉ thêm vào orderList 1 lần
                    if (!addedOrderIds.contains(madonhang)) {
                        String tenkh = "";
                        String queryFindUser = "SELECT tenkh FROM KHACHHANG WHERE makh = ?";
                        try (PreparedStatement findName = con.prepareStatement(queryFindUser)) {
                            findName.setString(1, makh);
                            ResultSet rsTenKH = findName.executeQuery();
                            if (rsTenKH.next()) {
                                tenkh = rsTenKH.getString("tenkh");
                            }
                        }

                        // Tạo đối tượng Order và thêm vào danh sách
                        Order_DTO order = new Order_DTO(madonhang, diachidat, ngaydat, tinhtrang, tongtien, manv, makh);
                        orderList.add(order);

                        // Hiển thị lên bảng (chỉ 1 lần)
                        FormatDate temp = new FormatDate();
                        String Date = temp.convertDateFormat(ngaydat);
                        tableModel.addRow(new Object[]{madonhang, tenkh, tinhtrang, Date, tongtien, "Tác vụ"});

                        addedOrderIds.add(madonhang);
                    }

                    // Dữ liệu chi tiết đơn hàng
                    String masp = rs.getString("masp");
                    int soluong = rs.getInt("soluong");
                    double dongia = rs.getDouble("dongia");
                    double thanhtien = rs.getDouble("thanhtien");

                    // Thêm chi tiết
                    OrderDetail_DTO detail = new OrderDetail_DTO(madonhang, masp, soluong, dongia, thanhtien);
                    orderDetailList.add(detail);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally{
                DatabaseConnection.closeConnection(con);
            }
        }
    }


    public ArrayList<Order_DTO> getAllOrder(){
        ArrayList<Order_DTO> arr = new  ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if (con != null){
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
                    // Tạo đối tượng Order
                    Order_DTO order = new Order_DTO(madonhang, diachidat, ngaydat, tinhtrang, tongtien, manv, makh);
                    arr.add(order);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return arr;
    }

    public OrderDetail_DTO getDetailForOrder(String id){
        con = DatabaseConnection.OpenConnection();
        if (con != null){
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
                DatabaseConnection.closeConnection(con);
            }
        }
        return null;
    }

    public ArrayList<OrderDetail_DTO> getAllDetail(){
        ArrayList<OrderDetail_DTO> arr = new  ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if (con != null){
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
                DatabaseConnection.closeConnection(con);
            }
        }
        return arr;
    }

    public boolean CancelOrder(String id, String nvid){
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try{
                String query = "UPDATE DONHANG "+
                                "SET tinhtrang = 'Đã hủy', manv = ? "+
                                "WHERE madonhang = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1,nvid);
                stmt.setString(2, id);
                if (stmt.executeUpdate()>=1)
                    return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return false;
    }

    public boolean ConfirmOrder(String id, String nvid){
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try{
                String query = "UPDATE DONHANG "+
                                "SET tinhtrang = 'Đã xử lý', manv = ? "+
                                "WHERE madonhang = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1,nvid);
                stmt.setString(2, id);
                if (stmt.executeUpdate()>=1)
                    return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return false;
    }

    // public boolean AddOrder(Order_DTO order, ArrayList<OrderDetail_DTO> details){
    //     con = DatabaseConnection.OpenConnection();
    //     if (con != null) {
    //         try {                    
    //             String query1 = "INSERT INTO DONHANG VALUES(?,?,?,?,?,?,?)";
    //             String query2 = "INSERT INTO CHITIETDONHANG VALUES(?,?,?,?,?)";
    //             PreparedStatement stmt1 = con.prepareStatement(query1);
    //             PreparedStatement stmt2 = con.prepareStatement(query2);
    //             stmt1.setString(1, order.getMadonhang());
    //             stmt1.setString(2, order.getDiachidat());
    //             stmt1.setString(3, order.getNgaydat());
    //             stmt1.setString(4, order.getTinhtrang());
    //             stmt1.setDouble(5, order.getTongtien());
    //             stmt1.setString(6, order.getManv());
    //             stmt1.setString(7, order.getMakh());

    //             stmt2.setString(1, details.getMadonhang());
    //             stmt2.setString(2, details.getMasp());
    //             stmt2.setInt(3, details.getSoluong());
    //             stmt2.setDouble(4, details.getDongia());
    //             stmt2.setDouble(5, details.getThanhtien());

    //             if (stmt1.executeUpdate()>=1 && stmt2.executeUpdate()>=1)
    //                 return true;
    //         } catch (SQLException ex) {
    //             System.out.println(ex);            
    //         } finally{
    //             DatabaseConnection.closeConnection(con);  
    //         } 
    //     }
    //     return false;
    // }

    public String getNextORDERID(Connection con){
        String latestID = "";
        try {
            String sql = "SELECT madonhang FROM DONHANG ORDER BY madonhang DESC LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                latestID = rs.getString("madonhang");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String prefix = latestID.replaceAll("\\d+", "");
        String numberic = latestID.replaceAll("[^\\d]", "");

        int number = Integer.parseInt(numberic);
        number++;
        String nextnumberic = String.format("%03d", number);
        return (prefix+nextnumberic).replace(" ", ""); 
    }

    public boolean AddOrder(Order_DTO order, ArrayList<OrderDetail_DTO> details){
        boolean result = false;
        int success_count = 0;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {      
                con.setAutoCommit(false);

                String query1 = "INSERT INTO DONHANG VALUES(?,?,?,?,?,?,?)";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                String DonHangID = getNextORDERID(con);
                stmt1.setString(1, DonHangID);
                stmt1.setString(2, order.getDiachidat());
                stmt1.setString(3, order.getNgaydat());
                stmt1.setString(4, order.getTinhtrang());
                stmt1.setDouble(5, order.getTongtien());
                stmt1.setString(6, order.getManv());
                stmt1.setString(7, order.getMakh());

                if(stmt1.executeUpdate()>=1){
                    for(OrderDetail_DTO detail : details){
                        try{
                            String query2 = "INSERT INTO CHITIETDONHANG VALUES(?,?,?,?,?)";
                            PreparedStatement stmt2 = con.prepareStatement(query2);

                            stmt2.setString(1, DonHangID);
                            stmt2.setString(2, detail.getMasp());
                            stmt2.setInt(3, detail.getSoluong());
                            stmt2.setDouble(4, detail.getDongia());
                            stmt2.setDouble(5, detail.getThanhtien());

                            if(stmt2.executeUpdate()>=1)
                                success_count++;
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        } 
                    }
                    if (success_count == details.size()){
                        con.commit();
                        result = true;
                    }else{
                        con.rollback();
                    }
                }
                else{
                    con.rollback();
                    result = false;
                }
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally{
                try{
                    con.setAutoCommit(true);
                }
                catch(SQLException e){
                    System.out.println(e);
                }
                DatabaseConnection.closeConnection(con);   
            } 
        }
        return result;
    }

    public boolean DeleteOrder(String id){
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try{
                String deleteChiTietSql = "DELETE FROM CHITIETDONHANG WHERE madonhang = ?";
                String deleteDonHangSql = "DELETE FROM DONHANG WHERE madonhang = ?";
                PreparedStatement deleteChiTietStmt = con.prepareStatement(deleteChiTietSql);
                PreparedStatement deleteDonHangStmt = con.prepareStatement(deleteDonHangSql);
    
                
                deleteChiTietStmt.setString(1, id);
                deleteChiTietStmt.executeUpdate();
    
                deleteDonHangStmt.setString(1, id);
                deleteDonHangStmt.executeUpdate();
    
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return false;
    }
    
    public boolean removeDetailBySP(String id){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
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
                try {
                    con.rollback();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } finally{
                try{
                    con.setAutoCommit(true);
                } catch(SQLException e){
                    System.out.println(e);
                }
                DatabaseConnection.closeConnection(con);  
            } 
        }
        return result;
    }

    public boolean hasDetailID(String id){
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {            
                String sql = "SELECT * FROM CHITIETDONHANG WHERE CHITIETDONHANG.madonhang='"+id+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next())
                    return true;
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                DatabaseConnection.closeConnection(con); 
            }   
        }
        return false;
    }

    public boolean hasOrderID(String id){
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {            
                String sql = "SELECT * FROM DONHANG WHERE DONHANG.madonhang='"+id+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next())
                    return true;
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                DatabaseConnection.closeConnection(con); 
            }   
        }
        return false;
    }

    public String getCustomerName(String makh) {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                PreparedStatement stmt = con.prepareStatement("SELECT tenkh FROM KHACHHANG WHERE makh = ?");
                stmt.setString(1, makh);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return rs.getString("tenkh");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return "Không rõ";
    }

    public String getEmployeeInfo(String manv) {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                PreparedStatement stmt = con.prepareStatement("SELECT tennv FROM NHANVIEN WHERE manv = ?");
                stmt.setString(1, manv);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return manv + " - " + rs.getString("tennv");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return "";
    }

    public String getProductName(String masp) {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                PreparedStatement stmt = con.prepareStatement("SELECT tensp FROM SANPHAM WHERE masp = ?");
                stmt.setString(1, masp);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("tensp");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return "Không rõ";
    }
}
