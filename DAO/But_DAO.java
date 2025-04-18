package DAO;

import java.sql.*;
import java.util.ArrayList;

import DTO.But_DTO;

public class But_DAO {
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

    public ArrayList<But_DTO> getAllBut(){
        ArrayList<But_DTO> arr = new  ArrayList<>();
        if (OpenConnection()){
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
                closeConnection();
            }
        }
        return arr;
    }

    public boolean addBut(But_DTO but){
        boolean result = false;
        if (OpenConnection()) {
            try {                    
                String query1 = "INSERT INTO SANPHAM VALUES(?,?,?,?)";
                String query2 = "INSERT INTO BUT VALUES(?,?,?,?,?)";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt1.setString(1, but.getID_SanPham());
                stmt1.setString(2, but.getTen_SanPham());
                stmt1.setDouble(3, but.getGia_SanPham());
                stmt1.setInt(4, but.getSoLuong_SanPham());

                stmt2.setString(1, but.getID_SanPham());
                stmt2.setString(2, but.getMau());
                stmt2.setString(3, but.getLoai_But());
                stmt2.setString(4, but.getHang());

                if (stmt1.executeUpdate()>=1 && stmt2.executeUpdate()>=1)
                    result = true;
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally{
                closeConnection();  
            } 
        }
        return result;
    }

    public boolean removeBut(String id){
        boolean result = false;
        if (OpenConnection()) {
            try {                    
                String query1 = "DELETE FROM SANPHAM WHERE SANPHAM.masp = ?";
                String query2 = "DELETE FROM BUT WHERE BUT.masp = ?";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                PreparedStatement stmt2 = con.prepareStatement(query2);

                stmt1.setString(1, id);
                stmt2.setString(1, id);
                if (stmt1.executeUpdate()>=1 && stmt2.executeUpdate()>=1)
                    result = true;
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally{
                closeConnection();  
            } 
        }
        return result;
    }

    public boolean hasButID(String id){                        
        if (OpenConnection()) {
            try {            
            String sql = "SELECT * FROM BUT WHERE BUT.masp="+id;
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
