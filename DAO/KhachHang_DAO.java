package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import DTO.KhachHang_DTO;

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
                    String gioitinh = rs.getString("gioi");
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
                    String gioitinh = rs.getString("gioitinh");
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
    public boolean deleteCustomer(String id, DefaultTableModel tableModel, ArrayList<KhachHang_DTO> customerList) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Lấy tất cả mã đơn hàng của khách hàng
            String selectOrders = "SELECT madonhang FROM donhang WHERE makh = ?";
            try (PreparedStatement selectOrderStmt = conn.prepareStatement(selectOrders)) {
                selectOrderStmt.setString(1, id);
                ResultSet rs = selectOrderStmt.executeQuery();
                ArrayList<String> idArray = new ArrayList<>();
                while (rs.next())  {
                    String madh = rs.getString("madonhang");
                    idArray.add(madh);
                }
                if (idArray.isEmpty()) {
                    String deleteCustomer = "DELETE FROM khachhang WHERE makh = ?";
                    try (PreparedStatement delCustomerStmt = conn.prepareStatement(deleteCustomer)) {
                        delCustomerStmt.setString(1, id);
                        int affected = delCustomerStmt.executeUpdate();
    
                        if (affected > 0) {
                            JOptionPane.showMessageDialog(null, "Đã xóa khách hàng, đơn hàng và chi tiết liên quan.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng để xóa.");
                        }
                        loadDataFormDatabase(customerList, tableModel);
                        return true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                            "Lỗi khi xóa khách hàng: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Lỗi: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    
    
    public void loadDataFormDatabase(ArrayList<KhachHang_DTO> customerList,DefaultTableModel tableModel) {
        try (Connection conn = DatabaseConnection.getConnection()) {
                String queryforcs = "SELECT * FROM KHACHHANG";
                PreparedStatement pstmt = conn.prepareStatement(queryforcs);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    String id = rs.getString("makh");
                    String name = rs.getString("tenkh");
                    String username = rs.getString("username");
                    String phone = rs.getString("sdt");
                    KhachHang_DTO kh = new KhachHang_DTO(id, name, username, phone);
                    customerList.add(kh);
                    tableModel.addRow(new Object[]{id, name, username, phone,"Chi tiết" + "Xóa"});
                }
        } catch (Exception e) {}
    }
    // Kiểm tra SDT đã tồn tại (ngoại trừ khách hàng hiện tại)
    public boolean isCustomerPhoneExist(String phone, String currentUsername) throws SQLException {
        if(OpenConnection()){
            try {
                String query = "SELECT * FROM KHACHHANG WHERE sdt = ? AND username <> ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, phone);
                stmt.setString(2, currentUsername);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                closeConnection(); 
            }    
        }
        return false;
    }

    // Kiểm tra email đã tồn tại (ngoại trừ khách hàng hiện tại)
    public boolean isCustomerEmailExist(String email, String currentUsername) throws SQLException {
        if(OpenConnection()){
            try {
                String query = "SELECT * FROM KHACHHANG WHERE email = ? AND username <> ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, email);
                stmt.setString(2, currentUsername);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                closeConnection(); 
            }    
        }
        return false;
    }

    // Kiểm tra username đã tồn tại nếu đổi
    public boolean isCustomerUsernameExist(String username) throws SQLException {
        if(OpenConnection()){
            try {
                String query = "SELECT * FROM KHACHHANG WHERE username = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            } catch (SQLException ex) {
                System.out.println(ex);            
            } finally {     
                closeConnection(); 
            }    
        }
        return false;
    }

    public void updateCustomer(KhachHang_DTO kh, JTextField txtName, JTextField txtPhone, JTextField txtUsername,
                           JTextField txtAddress, JTextField txtBirthday, JTextField txtEmail,
                           JComboBox<String> cbGender) throws ParseException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String newPhone = txtPhone.getText();
            String newEmail = txtEmail.getText();
            String newUsername = txtUsername.getText();
            String currentUsername = kh.getUsername();  // Lưu ý: bạn phải có getUsername() trong DTO

            // Nếu không trùng thì cập nhật
            String query = "UPDATE KHACHHANG SET tenkh = ?, sdt = ?, username = ?, diachikh = ?, ngaysinh = ?, email = ?, gioi = ? WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, newPhone);
            pstmt.setString(3, newUsername);
            pstmt.setString(4, txtAddress.getText());

            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(txtBirthday.getText());
            String formattedDate = outputFormat.format(date);

            pstmt.setString(5, formattedDate);
            pstmt.setString(6, newEmail);
            pstmt.setString(7, (String) cbGender.getSelectedItem());
            pstmt.setString(8, currentUsername);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cập nhật thông tin khách hàng thành công!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== BỔ SUNG: Hàm checkLogin dùng cho phân quyền đăng nhập (LoginForm.java) =====
    public static KhachHang_DTO checkLogin(String username, String password) {
        KhachHang_DTO kh = null;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM KHACHHANG WHERE username = ? AND passwordkh = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                kh = new KhachHang_DTO();
                kh.setId_KhachHang(rs.getString("makh"));
                kh.setTen_KhachHang(rs.getString("tenkh"));
                kh.setSdt_KhachHang(rs.getString("sdt"));
                kh.setGioiTinh_KhachHang(rs.getString("gioitinh")); // hoặc \"gioi\" tùy DB
                kh.setEmail(rs.getString("email"));
                kh.setNgaySinh_KhachHang(rs.getString("ngaysinh"));
                kh.setDiaChi_KhachHang(rs.getString("diachikh"));
                kh.setUsername(rs.getString("username"));
                kh.setPass_KhachHang(rs.getString("passwordkh"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kh;
    }


}
