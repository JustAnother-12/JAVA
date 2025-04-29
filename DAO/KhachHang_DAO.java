package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;


public class KhachHang_DAO {
    private Connection con;

    public boolean OpenConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ban_van_phong_pham", "root", "123456789");
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

    public ArrayList<KhachHang_DTO> getAllKhachHang(){
        ArrayList<KhachHang_DTO> arr = new  ArrayList<>();
        if (OpenConnection()){
            try{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM KHACHHANG");
                while (rs.next()) {
                    String id = rs.getString("makh");
                    String ten = rs.getString("tenkh");
                    String sdt = rs.getString("sdt");
                    String gioitinh = rs.getString("gioitinh");
                    String emailkh = rs.getString("email");
                    String date = rs.getString("ngaysinh");
                    String diachi = rs.getString("diachikh");
                    String username = rs.getString("username");
                    String passwdkh = rs.getString("passwordkh");
                    KhachHang_DTO kh = new KhachHang_DTO(id, ten, sdt, gioitinh, diachi, emailkh, username, passwdkh, date);
                    arr.add(kh);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return arr;
    }

    public KhachHang_DTO getKhachHangfromID(String id){
        if (OpenConnection()) {
            try {            
                String sql = "SELECT * FROM KHACHHANG WHERE KHACHHANG.makh='"+id+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()){
                    String idkh = rs.getString("makh");
                    String ten = rs.getString("tenkh");
                    String sdt = rs.getString("sdt");
                    String gioitinh = rs.getString("gioi");
                    String emailkh = rs.getString("email");
                    String date = rs.getString("ngaysinh");
                    String diachi = rs.getString("diachikh");
                    String username = rs.getString("username");
                    String passwdkh = rs.getString("passwordkh");
                    KhachHang_DTO kh = new KhachHang_DTO(idkh, ten, sdt, gioitinh, diachi, emailkh, username, passwdkh, date);
                    return kh;
                }
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                closeConnection(); 
            }   
        }
        return null;
    }

    public boolean addKhachHang(KhachHang_DTO kh){
        boolean result = false;
        if (OpenConnection()) {
            try {                    
                String query1 = "INSERT INTO KHACHHANG VALUES(?,?,?,?,?,?,?,?,?)";
                PreparedStatement stmt1 = con.prepareStatement(query1);
                stmt1.setString(1, kh.getId_KhachHang());
                stmt1.setString(2, kh.getTen_KhachHang());
                stmt1.setString(3, kh.getSdt_KhachHang());
                stmt1.setString(4, kh.getGioiTinh_KhachHang());
                stmt1.setString(5, kh.getEmail());
                stmt1.setString(6, kh.getNgaySinh_KhachHang());
                stmt1.setString(7, kh.getDiaChi_KhachHang());
                stmt1.setString(8, kh.getUsername());
                stmt1.setString(9, kh.getPass_KhachHang());

                if (stmt1.executeUpdate()>=1)
                    result = true;
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally{
                closeConnection();  
            } 
        }
        return result;
    }

    public boolean removeKhachHang(String id){
        boolean result = false;
        if (OpenConnection()) {
            try {                    
                String query1 = "DELETE FROM KHACHHANG WHERE KHACHHANG.makh = ?";
                PreparedStatement stmt1 = con.prepareStatement(query1);

                stmt1.setString(1, id);
                if (stmt1.executeUpdate()>=1)
                    result = true;
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally{
                closeConnection();  
            } 
        }
        return result;
    }

    public boolean hasKhachHangUsername(String username){
        if (OpenConnection()) {
            try {            
                String sql = "SELECT * FROM KHACHHANG WHERE KHACHHANG.username="+username;
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

    public boolean hasKhachHangID(String id){                        
        if (OpenConnection()) {
            try {            
                String sql = "SELECT * FROM KHACHHANG WHERE KHACHHANG.makh="+id;
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

    public KhachHang_DTO getKhachHangFromAccount(String username, String password){
        if (OpenConnection()) {
            try {            
                String query = "SELECT * FROM KHACHHANG WHERE username = ? AND passwordkh = ?";
                PreparedStatement prestmt = con.prepareStatement(query);
                prestmt.setString(1, username);
                prestmt.setString(2, password);
                ResultSet rs = prestmt.executeQuery();
                if (rs.next()){
                    String idkh = rs.getString("makh");
                    String ten = rs.getString("tenkh");
                    String sdt = rs.getString("sdt");
                    String gioitinh = rs.getString("gioitinh");
                    String emailkh = rs.getString("email");
                    String date = rs.getString("ngaysinh");
                    String diachi = rs.getString("diachikh");
                    String usernamekh = rs.getString("username");
                    String passwdkh = rs.getString("passwordkh");
                    KhachHang_DTO kh = new KhachHang_DTO(idkh, ten, sdt, gioitinh, diachi, emailkh, usernamekh, passwdkh, date);
                    return kh;
                }
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                closeConnection(); 
            }   
        }
        return null;
    }
}
