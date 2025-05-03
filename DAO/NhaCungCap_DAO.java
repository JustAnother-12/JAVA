package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;

public class NhaCungCap_DAO {
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

    public ArrayList<NhaCungCap_DTO> getAllNCC(){
        ArrayList<NhaCungCap_DTO> arr = new  ArrayList<>();
        if (OpenConnection()){
            try{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM NHACUNGCAP");
                while (rs.next()) {
                    String id = rs.getString("mancc");
                    String ten = rs.getString("tenncc");
                    String sdt = rs.getString("sdt");
                    String emailkh = rs.getString("email");
                    NhaCungCap_DTO ncc = new NhaCungCap_DTO(id, ten, sdt, emailkh);
                    arr.add(ncc);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                closeConnection();
            }
        }
        return arr;
    }
}
