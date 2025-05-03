package DAO;

import java.sql.*;
import java.util.ArrayList;

import DTO.*;

public class Vo_DAO {
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

    public String getLastestVOID(){
        String latestID = "";
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try {
                String sql = "SELECT masp FROM VO ORDER BY masp DESC LIMIT 1";
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

    public String getNextVOID(Connection con){
        String latestID = "";
        try {
            String sql = "SELECT masp FROM VO ORDER BY masp DESC LIMIT 1";
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

    public Vo_DTO getVOfromID(String id){
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {            
                String sql ="SELECT * " +
                            "FROM VO, SANPHAM" +
                            "WHERE SANPHAM.masp = VO.masp " +
                            "AND SANPHAM.masp ='"+id+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()){
                    String idvo = rs.getString("masp");
                    String ten = rs.getString("tensp");
                    double gia = rs.getDouble("dongiasp");
                    int soLuong = rs.getInt("soluongsp");
                    String loaivo = rs.getString("loaivo");
                    String nhasanxuat = rs.getString("nhasanxuat");
                    String chatlieu = rs.getString("chatlieu");
                    int sotrang = rs.getInt("sotrang");
                    Vo_DTO sp = new Vo_DTO(idvo, ten, gia, soLuong, loaivo, nhasanxuat, chatlieu, sotrang);
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

    public ArrayList<Vo_DTO> getAllVo(){
        ArrayList<Vo_DTO> arr = new  ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(
                    "SELECT * " +
                    "FROM VO " +
                    "JOIN SANPHAM ON SANPHAM.masp = VO.masp "
                    );
                while (rs.next()) {
                    
                    String id = rs.getString("masp");
                    String ten = rs.getString("tensp");
                    double gia = rs.getDouble("dongiasp");
                    int soLuong = rs.getInt("soluongsp");
                    String loaivo = rs.getString("loaivo");
                    String nhasanxuat = rs.getString("nhasanxuat");
                    String chatlieu = rs.getString("chatlieu");
                    int sotrang = rs.getInt("sotrang");
                    Vo_DTO sp = new Vo_DTO(id, ten, gia, soLuong, loaivo, nhasanxuat, chatlieu, sotrang);
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

    public boolean updateVo(Vo_DTO vo){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try{
                con.setAutoCommit(false);
                String query1 = "UPDATE SANPHAM "+
                                "SET tensp = ?, dongiasp = ?, soluongsp = ? "+
                                "WHERE masp = ?";
                String query2 = "UPDATE VO "+
                                "SET loaivo = ?, nhasanxuat = ?, chatlieu = ?, sotrang= ? "+
                                "WHERE masp = ?";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt1.setString(1, vo.getTen_SanPham());
                stmt1.setDouble(2, vo.getGia_SanPham());
                stmt1.setInt(3, vo.getSoLuong_SanPham());
                stmt1.setString(4, vo.getID_SanPham());

                stmt2.setString(1,vo.getLoai_Vo());
                stmt2.setString(2, vo.getNhaSanXuat());
                stmt2.setString(3, vo.getChatLieu());
                stmt2.setInt(4, vo.getSoTrang());
                stmt2.setString(5, vo.getID_SanPham());

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
                }
                catch(SQLException e){
                    System.out.println(e);
                }
                DatabaseConnection.closeConnection(con);
            }
        }
        return result;
    }

    public boolean addVo(Vo_DTO vo){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {                    
                con.setAutoCommit(false);
                String query1 = "INSERT INTO SANPHAM VALUES(?,?,?,?)";
                String query2 = "INSERT INTO VO VALUES(?,?,?,?,?)";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt1.setString(1, getNextVOID(con));
                stmt1.setString(2, vo.getTen_SanPham());
                stmt1.setDouble(3, vo.getGia_SanPham());
                stmt1.setInt(4, vo.getSoLuong_SanPham());

                stmt2.setString(1, getNextVOID(con));
                stmt2.setString(2, vo.getLoai_Vo());
                stmt2.setString(3, vo.getNhaSanXuat());
                stmt2.setString(4, vo.getChatLieu());
                stmt2.setInt(5, vo.getSoTrang());

                if(stmt1.executeUpdate()>=1 && stmt2.executeUpdate()>=1){
                    con.commit();
                    result = true;
                }else{
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

    public boolean removeVo(String id){
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {              
                con.setAutoCommit(false);      
                String query1 = "DELETE FROM SANPHAM WHERE SANPHAM.masp = ?";
                String query2 = "DELETE FROM VO WHERE VO.masp = ?";
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

    public boolean hasVoID(String id){     
        con = DatabaseConnection.OpenConnection();                   
        if (con != null) {
            try {            
            String sql = "SELECT * FROM VO WHERE VO.masp='"+id+"'";
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
