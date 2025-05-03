package DAO;

import java.sql.*;
import java.util.ArrayList;

import DTO.*;

public class But_DAO {
    private Connection con;

    // public boolean OpenConnection() {
    //     try {
    //         Class.forName("com.mysql.cj.jdbc.Driver");
    //         con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ban_van_phong_pham", "root", "123456789");
    //         return true;
    //     } catch (ClassNotFoundException | SQLException e) {
    //         System.out.println(e.getMessage());
    //         return false;
    //     }
    // }

    // public void closeConnection() {
    //     try {
    //         if (con != null){
    //             con.close();
    //         }
    //     }catch (SQLException e){
    //         System.out.println(e.getMessage());
    //     }
    // }

    public String getLastestBUTID(){
        String latestID = "";
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try {
                String sql = "SELECT masp FROM BUT ORDER BY masp DESC LIMIT 1";
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

    public String getNextBUTID(Connection con){
        String latestID = "";
        try {
            String sql = "SELECT masp FROM BUT ORDER BY masp DESC LIMIT 1";
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

    public But_DTO getBUTfromID(String id){
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {            
                String sql ="SELECT * " +
                            "FROM BUT, SANPHAM" +
                            "WHERE SANPHAM.masp = BUT.masp " +
                            "AND SANPHAM.masp ='"+id+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()){
                    String idbut = rs.getString("masp");
                    String ten = rs.getString("tensp");
                    double gia = rs.getDouble("dongiasp");
                    int soLuong = rs.getInt("soluongsp");
                    String mau = rs.getString("mau");
                    String loaibut = rs.getString("loaibut");
                    String nhasanxuat = rs.getString("nhasanxuat");
                    But_DTO sp = new But_DTO(idbut, ten, gia, soLuong, mau, loaibut, nhasanxuat);
                    return sp;
                }
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                DatabaseConnection.closeConnection(con); 
            }   
        }
        return null;
    }


    public ArrayList<But_DTO> getAllBut(){
        ArrayList<But_DTO> arr = new  ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT * " +
                    "FROM BUT " +
                    "JOIN SANPHAM ON SANPHAM.masp = BUT.masp "
                    );
                while (rs.next()) {
                    
                    String id = rs.getString("masp");
                    String ten = rs.getString("tensp");
                    double gia = rs.getDouble("dongiasp");
                    int soLuong = rs.getInt("soluongsp");
                    String mau = rs.getString("mau");
                    String loaibut = rs.getString("loaibut");
                    String nhasanxuat = rs.getString("nhasanxuat");
                    But_DTO sp = new But_DTO(id, ten, gia, soLuong, mau, loaibut, nhasanxuat);
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

    public boolean updateBut(But_DTO but){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try{
                con.setAutoCommit(false);
                String query1 = "UPDATE SANPHAM "+
                                "SET tensp = ?, dongiasp = ?, soluongsp = ? "+
                                "WHERE masp = ?";
                String query2 = "UPDATE VO "+
                                "SET mau = ?, loaibut = ?, nhasanxuat = ? "+
                                "WHERE masp = ?";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt1.setString(1, but.getTen_SanPham());
                stmt1.setDouble(2, but.getGia_SanPham());
                stmt1.setInt(3, but.getSoLuong_SanPham());
                stmt1.setString(4, but.getID_SanPham());

                stmt2.setString(1,but.getMau());
                stmt2.setString(2, but.getLoai_But());
                stmt2.setString(3, but.getHang());
                stmt2.setString(4, but.getID_SanPham());

                if(stmt1.executeUpdate() >=1 && stmt2.executeUpdate() >=1){
                    con.commit();
                    result = true;
                }else{
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

    public boolean addBut(But_DTO but){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {               
                con.setAutoCommit(false);     
                String query1 = "INSERT INTO SANPHAM VALUES(?,?,?,?)";
                String query2 = "INSERT INTO BUT VALUES(?,?,?,?)";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt1.setString(1, getNextBUTID(con));
                stmt1.setString(2, but.getTen_SanPham());
                stmt1.setDouble(3, but.getGia_SanPham());
                stmt1.setInt(4, but.getSoLuong_SanPham());

                stmt2.setString(1, getNextBUTID(con));
                stmt2.setString(2, but.getMau());
                stmt2.setString(3, but.getLoai_But());
                stmt2.setString(4, but.getHang());

                if(stmt2.executeUpdate()>=1 && stmt1.executeUpdate()>=1){
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

    public boolean removeBut(String id){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {       
                con.setAutoCommit(false);             
                String query1 = "DELETE FROM SANPHAM WHERE SANPHAM.masp = ?";
                String query2 = "DELETE FROM BUT WHERE BUT.masp = ?";
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

    public boolean hasButID(String id){   
        con = DatabaseConnection.OpenConnection();                     
        if (con != null) {
            try {            
            String sql = "SELECT * FROM BUT WHERE BUT.masp='"+id+"'";
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
