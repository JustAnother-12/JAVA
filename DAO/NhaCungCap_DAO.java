package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;

public class NhaCungCap_DAO {
    private Connection con;

    public String getLastestNCCID(){
        String latestID = "";
        con = DatabaseConnection.OpenConnection();
        if (con != null){
            try {
                String sql = "SELECT mancc FROM NHACUNGCAP ORDER BY mancc DESC LIMIT 1";
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    latestID = rs.getString("mancc");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return latestID;
    }

    public String getNextNCCID(Connection con){
        String latestID = "";
        try {
            String sql = "SELECT mancc FROM NHACUNGCAP ORDER BY mancc DESC LIMIT 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                latestID = rs.getString("mancc");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String prefix = latestID.replaceAll("\\d+", "");
        String numberic = latestID.replaceAll("[^\\d]", "");

        int number = Integer.parseInt(numberic);
        number++;
        String nextnumberic = String.format("%02d", number);
        return (prefix+nextnumberic).replace(" ", ""); 
    }

    public ArrayList<NhaCungCap_DTO> getAllNCC(){
        ArrayList<NhaCungCap_DTO> arr = new  ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if (con != null){
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
                DatabaseConnection.closeConnection(con);
            }
        }
        return arr;
    }

    public NhaCungCap_DTO getNCCfromID(String id){
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {            
                String sql = "SELECT * FROM NHACUNGCAP WHERE NHACUNGCAP.mancc='"+id+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()){
                    String idncc = rs.getString("mancc");
                    String ten = rs.getString("tenncc");
                    String sdt = rs.getString("sdt");
                    String emailkh = rs.getString("email");
                    NhaCungCap_DTO ncc = new NhaCungCap_DTO(idncc, ten, sdt, emailkh);
                    return ncc;
                }
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                DatabaseConnection.closeConnection(con); 
            }   
        }
        return null;
    }

    public boolean hasNCCID(String id){         
        con = DatabaseConnection.OpenConnection();               
        if (con != null) {
            try {            
                String sql = "SELECT * FROM NHACUNGCAP WHERE NHACUNGCAP.mancc='"+id+"'";
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

    // Thêm nhà cung cấp
    public boolean insertNCC(NhaCungCap_DTO ncc) {
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {
                String sql = "INSERT INTO NHACUNGCAP(mancc, tenncc, sdt, email) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, getNextNCCID(con));
                stmt.setString(2, ncc.getTenNCC());
                stmt.setString(3, ncc.getSdtNCC());
                stmt.setString(4, ncc.getEmailNCC());

                result = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                DatabaseConnection.closeConnection(con);
            }
        }
        return result;
    }

    // Cập nhật nhà cung cấp
    public boolean updateNCC(NhaCungCap_DTO ncc) {
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {
                String sql = "UPDATE NHACUNGCAP SET tenncc = ?, sdt = ?, email = ? WHERE mancc = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, ncc.getTenNCC());
                stmt.setString(2, ncc.getSdtNCC());
                stmt.setString(3, ncc.getEmailNCC());
                stmt.setString(4, ncc.getMaNCC());

                result = stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                DatabaseConnection.closeConnection(con);
            }
        }
        return result;
    }

    // Xóa nhà cung cấp
    public boolean deleteNCC(String maNCC) {
        boolean result = false;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {
                String sql = "DELETE FROM NHACUNGCAP WHERE mancc = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, maNCC);

                if(stmt.executeUpdate() > 0){
                    result = true;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                DatabaseConnection.closeConnection(con);
            }
        }
        return result;
    }
    public String getTenNCC(String id) {
        String tenNCC = null;
        con = DatabaseConnection.OpenConnection();
        if (con != null) {
            try {
                String sql = "SELECT tenncc FROM NHACUNGCAP WHERE mancc = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    tenNCC = rs.getString("tenncc");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                DatabaseConnection.closeConnection(con);
            }
        }
        return tenNCC;
    }
}


