package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;


public class Sach_DAO{
    private Connection con;

    public String getLastestSACHID(){
        String latestID = "";
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try {
                String sql = "SELECT masp FROM SACH ORDER BY masp DESC LIMIT 1";
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    latestID = rs.getString("masp");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return latestID;
    }

    public String getNextSACHID(Connection con){
        String latestID = "";
        try {
            String sql = "SELECT masp FROM SACH ORDER BY masp DESC LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                latestID = rs.getString("masp");
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

    public Sach_DTO getSACHfromID(String id){
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {            
                String sql ="SELECT * " +
                            "FROM SACH s, SANPHAM sp " + 
                            "WHERE sp.masp ='"+id+"' " +
                            "AND s.masp = sp.masp ";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()){
                    String idsach = rs.getString("masp");
                    String ten = rs.getString("tensp");
                    double gia = rs.getDouble("dongiasp");
                    int soLuong = rs.getInt("soluongsp");
                    String tentg = rs.getString("tentacgia");
                    String theloai = rs.getString("theloai");
                    String nhaxuatban = rs.getString("nhaxuatban");
                    int namxuatban = rs.getInt("namxuatban");
                    Sach_DTO sp = new Sach_DTO(idsach, ten, gia, soLuong, tentg, theloai, nhaxuatban, namxuatban);
                    return sp;
                }
            } catch (SQLException ex) {
                System.out.println(ex);            
            } 
            finally {     
                DatabaseConnection.closeConnection(con); 
            }   
        }
        return null;
    }

    public ArrayList<Sach_DTO> getAllSach(){
        ArrayList<Sach_DTO> arr = new  ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT * " +
                    "FROM SACH " +
                    "JOIN SANPHAM ON SANPHAM.masp = SACH.masp "
                    );
                while (rs.next()) {
                    
                    String id = rs.getString("masp");
                    String ten = rs.getString("tensp");
                    double gia = rs.getDouble("dongiasp");
                    int soLuong = rs.getInt("soluongsp");
                    String tentg = rs.getString("tentacgia");
                    String theloai = rs.getString("theloai");
                    String nhaxuatban = rs.getString("nhaxuatban");
                    int namxuatban = rs.getInt("namxuatban");
                    Sach_DTO sp = new Sach_DTO(id, ten, gia, soLuong, tentg, theloai, nhaxuatban, namxuatban);
                    arr.add(sp);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return arr;
    }

    public boolean updateSach(Sach_DTO sach){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try{
                con.setAutoCommit(false);
                
                String query1 = "UPDATE SANPHAM "+
                                "SET tensp = ?, dongiasp = ?, soluongsp = ? "+
                                "WHERE masp = ?";
                String query2 = "UPDATE SACH "+
                                "SET tentacgia = ?, theloai = ?, nhaxuatban = ?, namxuatban= ? "+
                                "WHERE masp = ?";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt1.setString(1, sach.getTen_SanPham());
                stmt1.setDouble(2, sach.getGia_SanPham());
                stmt1.setInt(3, sach.getSoLuong_SanPham());
                stmt1.setString(4, sach.getID_SanPham());

                stmt2.setString(1,sach.getTenTacGia());
                stmt2.setString(2, sach.getTheLoai());
                stmt2.setString(3, sach.getNhaXuatBan());
                stmt2.setInt(4, sach.getNamXuatBan());
                stmt2.setString(5, sach.getID_SanPham());

                if(stmt1.executeUpdate() >=1 && stmt2.executeUpdate() >=1){
                    con.commit();
                    result = true;
                }
                else{
                    con.rollback();
                }
            }
            catch(SQLException e){
                System.out.println(e);
            } finally{
                try{
                    con.setAutoCommit(true);
                }catch(SQLException e){
                    System.out.println(e);
                }
                DatabaseConnection.closeConnection(con);
            }
        }
        return result;
    }

    public boolean addSach(Sach_DTO sach){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {              
                con.setAutoCommit(false);   

                String query1 = "INSERT INTO SANPHAM VALUES(?,?,?,?)";
                String query2 = "INSERT INTO SACH VALUES(?,?,?,?,?)";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt1.setString(1, getNextSACHID(con));
                stmt1.setString(2, sach.getTen_SanPham());
                stmt1.setDouble(3, sach.getGia_SanPham());
                stmt1.setInt(4, sach.getSoLuong_SanPham());

                stmt2.setString(1, getNextSACHID(con));
                stmt2.setString(2, sach.getTenTacGia());
                stmt2.setString(3, sach.getTheLoai());
                stmt2.setString(4, sach.getNhaXuatBan());
                stmt2.setInt(5, sach.getNamXuatBan());

                if(stmt1.executeUpdate()>=1 && stmt2.executeUpdate()>=1){
                    con.commit();
                    result = true;
                }
                else{
                    con.rollback();
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

    public boolean removeSach(String id){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {                   
                con.setAutoCommit(false); 

                String query1 = "DELETE FROM SANPHAM WHERE SANPHAM.masp = ?";
                String query2 = "DELETE FROM SACH WHERE SACH.masp = ?";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);

                stmt1.setString(1, id);
                stmt2.setString(1, id);

                if (stmt2.executeUpdate()>=1 && stmt1.executeUpdate()>=1){
                    con.commit();
                    result = true;
                }
                else{
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally{
                try{
                    con.setAutoCommit(true);
                }catch(SQLException e){
                    System.out.println(e);
                }
                DatabaseConnection.closeConnection(con);  
            } 
        }
        return result;
    }

    public boolean hasSachID(String id){      
        con = DatabaseConnection.OpenConnection();                  
        if (con != null) {
            try {            
            String sql = "SELECT * FROM SACH WHERE SACH.masp='"+id+"'";
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
        
}
