package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;

import BLL.SanPham_BLL;

public class PhieuNhap_DAO {
    private Connection con;
    private SanPham_BLL spbll = new SanPham_BLL();

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

    public String getNextPHIEUID(Connection con){
        String latestID = "";
        try {
            String sql = "SELECT maphieu FROM PHIEUNHAP ORDER BY maphieu DESC LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                latestID = rs.getString("maphieu");
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

    public ArrayList<PhieuNhap_DTO> getAllPhieuNhap(){
        ArrayList<PhieuNhap_DTO> arr = new  ArrayList<>();
        if (OpenConnection()){
            try{
                Statement stmt1 = con.createStatement();
                ResultSet rs = stmt1.executeQuery("SELECT * FROM PHIEUNHAP");
                while (rs.next()) {
                    String id = rs.getString("maphieu");
                    String idncc = rs.getString("mancc");
                    String idnv = rs.getString("manv");
                    String ngayNhap = rs.getString("ngaynhap");
                    ArrayList<ChiTietPhieuNhap_DTO> dsctpn = getAllChiTietForPN(id);
                    PhieuNhap_DTO pn = new PhieuNhap_DTO(id, idncc, idnv, ngayNhap, dsctpn);
                    arr.add(pn);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return arr;
    }

    public ArrayList<ChiTietPhieuNhap_DTO> getAllChiTietForPN(String id){
        ArrayList<ChiTietPhieuNhap_DTO> arr = new  ArrayList<>();
        if (OpenConnection()){
            try{
                String query = "SELECT * FROM CHITIETPHIEUNHAP WHERE CHITIETPHIEUNHAP.maphieu = ?";
                PreparedStatement prstmt = con.prepareStatement(query);
                prstmt.setString(1, id);
                ResultSet rs = prstmt.executeQuery();
                while (rs.next()) {
                    String idpn = rs.getString("maphieu");
                    String idsp = rs.getString("masp");
                    int soluong = rs.getInt("soluongnhap");
                    double dongianhap = rs.getDouble("dongianhap");
                    SanPham_DTO sp = null; 
                    if(idsp.contains("S")){
                        sp = spbll.getSachFromID(idsp);
                    } else if(idsp.contains("V")){
                        sp = spbll.getVoFromID(idsp);
                    } else{
                        sp = spbll.getButFromID(idsp);
                    }
                    ChiTietPhieuNhap_DTO ctpn = new ChiTietPhieuNhap_DTO(idpn, sp, soluong, dongianhap);
                    arr.add(ctpn);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return arr;
    }

    public ArrayList<ChiTietPhieuNhap_DTO> getAllChiTiet(){
        ArrayList<ChiTietPhieuNhap_DTO> arr = new  ArrayList<>();
        if (OpenConnection()){
            try{
                String query = "SELECT * FROM CHITIETPHIEUNHAP";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String idpn = rs.getString("maphieu");
                    String idsp = rs.getString("masp");
                    int soluong = rs.getInt("soluongnhap");
                    double dongianhap = rs.getDouble("dongianhap");
                    SanPham_DTO sp = null; 
                    if(idsp.contains("S")){
                        sp = spbll.getSachFromID(idsp);
                    } else if(idsp.contains("V")){
                        sp = spbll.getVoFromID(idsp);
                    } else{
                        sp = spbll.getButFromID(idsp);
                    }
                    ChiTietPhieuNhap_DTO ctpn = new ChiTietPhieuNhap_DTO(idpn, sp, soluong, dongianhap);
                    arr.add(ctpn);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return arr;
    }

    public boolean addPhieuNhap(PhieuNhap_DTO pn){
        boolean result = false;
        int success_count = 0;
        if (OpenConnection()) {
            try {
                con.setAutoCommit(false);

                String query1 = "INSERT INTO PHIEUNHAP VALUES(?,?,?,?)";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                String PhieuNhapID = getNextPHIEUID(con);
                stmt1.setString(1, PhieuNhapID);
                stmt1.setString(2, pn.getMaNCC());
                stmt1.setString(3, pn.getMaNV());
                stmt1.setString(4, pn.getNgayNhap());

                
                if(stmt1.executeUpdate()>=1){
                    for(ChiTietPhieuNhap_DTO ct:pn.getChiTietPhieuNhap()){
                        try{
                            String query2 = "INSERT INTO CHITIETPHIEUNHAP VALUES(?,?,?,?)";
                            PreparedStatement stmt2 = con.prepareStatement(query2);
    
                            stmt2.setString(1, PhieuNhapID);
                            stmt2.setString(2, ct.getThongtinSP().getID_SanPham());
                            stmt2.setInt(3, ct.getSoluongNhap());
                            stmt2.setDouble(4, ct.getDongiaNhap());
    
                            if(stmt2.executeUpdate()>=1)
                                success_count++;
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        } 
                    }

                    if (success_count == pn.getChiTietPhieuNhap().size()){
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
                closeConnection(); 
            } 
        }
        return result;
    }

    public boolean removeCTPhieuNhapBySP(String id){
        boolean result = false;
        if (OpenConnection()) {
            try {                   
                con.setAutoCommit(false); 
    
                // Xóa chi tiết phiếu nhập
                String query = "DELETE FROM CHITIETPHIEUNHAP WHERE maphieu = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, id);
                int rowsAffected = stmt.executeUpdate();
    
                if (rowsAffected >= 1) {
                    // Kiểm tra nếu không còn chi tiết nào nữa
                    String checkQuery = "SELECT COUNT(*) AS total FROM CHITIETPHIEUNHAP WHERE maphieu = ?";
                    PreparedStatement checkStmt = con.prepareStatement(checkQuery);
                    checkStmt.setString(1, id);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt("total") == 0) {
                        // Xóa luôn phiếu nhập
                        String deletePN = "DELETE FROM PHIEUNHAP WHERE maphieu = ?";
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
                closeConnection();  
            } 
        }
        return result;
    }


    public boolean hasCTPhieuNhapID(String id){                        
        if (OpenConnection()) {
            try {            
                String sql = "SELECT * FROM CHITIETPHIEUNHAP WHERE CHITIETPHIEUNHAP.maphieu='"+id+"'";
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

    public boolean hasPhieuNhapID(String id){                        
        if (OpenConnection()) {
            try {            
                String sql = "SELECT * FROM PHIEUNHAP WHERE PHIEUNHAP.maphieu='"+id+"'";
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
