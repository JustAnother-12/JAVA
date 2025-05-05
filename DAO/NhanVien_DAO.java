package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import DTO.NhanVien_DTO;

public class NhanVien_DAO extends javax.swing.JPanel {
    Connection con;

    public void filterByRole(String role, DefaultTableModel tableModel,ArrayList<NhanVien_DTO> accountList) {
        tableModel.setRowCount(0); // Clear bảng
        accountList.clear();
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String query = role.equals("Tất cả")
                    ? "SELECT * FROM NHANVIEN"
                    : "SELECT * FROM NHANVIEN WHERE chucvu = ?";
                PreparedStatement pstmt = con.prepareStatement(query);
                if (!role.equals("Tất cả")) {
                    pstmt.setString(1, role);
                }
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String id = rs.getString("manv");
                    String name = rs.getString("tennv");
                    String username = rs.getString("username");
                    String phone = rs.getString("sdt");
                    NhanVien_DTO nv = new NhanVien_DTO(id, name, username, phone);
                    accountList.add(nv);
                    tableModel.addRow(new Object[]{id, name, username, phone, "Chi tiết" + "Xoá"});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
    }
    public void loadDataFormDatabase(DefaultTableModel tableModel,ArrayList<NhanVien_DTO> accountList) {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                    String queryfors = "SELECT * FROM NHANVIEN";
                    PreparedStatement pstmt = con.prepareStatement(queryfors);
                    ResultSet rs = pstmt.executeQuery();
                    
                    while (rs.next()) {
                        String id = rs.getString("manv");
                        String name = rs.getString("tennv");
                        String username = rs.getString("username");
                        String phone = rs.getString("sdt");
                        NhanVien_DTO nv = new NhanVien_DTO(id, name, username, phone);
                        accountList.add(nv);
                        tableModel.addRow(new Object[]{id, name, username, phone, "Chi tiết" + "Xoá"});
                    }
            } catch (Exception e) {

            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
    }
    public boolean deleteStaff(String id, DefaultTableModel tableModel,ArrayList<NhanVien_DTO> staffList) {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                    String checkQuery = "SELECT COUNT(*) FROM NHANVIEN WHERE manv = ?";
                    PreparedStatement checkStmt = con.prepareStatement(checkQuery);
                    checkStmt.setString(1, id);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Nếu ID là của nhân viên
                        String deleteQuery = "DELETE FROM NHANVIEN WHERE manv = ?";
                        PreparedStatement pstmt = con.prepareStatement(deleteQuery);
                        pstmt.setString(1, id);
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Xóa tài khoản nhân viên thành công!");
                    } else {
                        // Nếu không tìm thấy ID trong cả hai bảng
                        JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản với ID: " + id, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                // Cập nhật lại bảng
                //loadDataFormDatabase(tableModel, staffList);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa tài khoản: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return false;
    }
    public NhanVien_DTO getDataOfStaff(NhanVien_DTO nv) {
        NhanVien_DTO a = null; // Khởi tạo a với giá trị null
        String query = "SELECT * FROM NHANVIEN WHERE manv = ?"; // Truy vấn để lấy dữ liệu
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
            
                pstmt.setString(1, nv.getManv()); // Thiết lập ID của khách hàng
                ResultSet rs = pstmt.executeQuery(); // Thực thi truy vấn

                if (rs.next()) { // Nếu có kết quả
                    // Tạo đối tượng customer từ kết quả truy vấn
                    a = new NhanVien_DTO(
                        rs.getString("manv"),
                        rs.getString("tennv"),
                        rs.getString("chucvu"),
                        rs.getString("sdt"),
                        rs.getString("username"),
                        rs.getString("passwordnv"),
                        rs.getString("diachinv"),
                        rs.getString("CCCD"),
                        rs.getString("gioitinh"),
                        rs.getString("ngaysinh")
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace(); // In ra lỗi nếu có
            }   finally{
                DatabaseConnection.closeConnection(con);
            }
        }

        return a; // Trả về đối tượng customer hoặc null nếu không tìm thấy
    }
    // Kiểm tra SDT đã tồn tại (ngoại trừ username hiện tại)
    public boolean isPhoneExist(String phone, String currentUsername) throws SQLException {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String query = "SELECT * FROM NHANVIEN WHERE sdt = ? AND username <> ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, phone);
                stmt.setString(2, currentUsername);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace(); // In ra lỗi nếu có
            }  finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return false;
    }

    // Kiểm tra CCCD đã tồn tại (ngoại trừ username hiện tại)
    public boolean isCCCDExist(String cccd, String currentUsername) throws SQLException {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String query = "SELECT * FROM NHANVIEN WHERE CCCD = ? AND username <> ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, cccd);
                stmt.setString(2, currentUsername);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace(); // In ra lỗi nếu có
            }  finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return false;
    }

    // Kiểm tra username mới có bị trùng không
    public boolean isUsernameExist(String username) throws SQLException {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {  
                String query = "SELECT * FROM NHANVIEN WHERE username = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace(); // In ra lỗi nếu có
            }  finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return false;
    }
    public void updateStaff(NhanVien_DTO nv, JTextField txtName, JTextField txtPhone, JTextField txtUsername,
                        JTextField txtAddress, JTextField txtBirthday, JComboBox<String> cbPosition,
                        JTextField txtCCCD, JComboBox<String> cbGender) throws ParseException {
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String newPhone = txtPhone.getText();
                String newCCCD = txtCCCD.getText();
                String newUsername = txtUsername.getText();
                String currentUsername = nv.getUsername();

                String query = "UPDATE NHANVIEN SET tennv = ?, sdt = ?, username = ?, diachinv = ?, gioitinh = ?, ngaysinh = ?, chucvu = ?, CCCD = ? WHERE username = ?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1, txtName.getText());
                pstmt.setString(2, newPhone);
                pstmt.setString(3, newUsername);
                pstmt.setString(4, txtAddress.getText());

                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = inputFormat.parse(txtBirthday.getText());
                String formattedDate = outputFormat.format(date);

                pstmt.setString(5, (String) cbGender.getSelectedItem());
                pstmt.setString(6, formattedDate);
                pstmt.setString(7, (String) cbPosition.getSelectedItem());
                pstmt.setString(8, newCCCD);
                pstmt.setString(9, currentUsername);

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cập nhật thông tin nhân viên thành công!");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
    }

    public NhanVien_DTO checkLogin(String username, String password) {
        NhanVien_DTO nv = null;
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String sql = "SELECT * FROM NHANVIEN WHERE username = ? AND passwordnv = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    nv = new NhanVien_DTO(
                        rs.getString("manv"),
                        rs.getString("tennv"),
                        rs.getString("chucvu"),
                        rs.getString("sdt"),
                        rs.getString("username"),
                        rs.getString("passwordnv"),
                        rs.getString("diachinv"),
                        rs.getString("CCCD"),
                        rs.getString("gioitinh"),
                        rs.getString("ngaysinh") // đúng kiểu String như DTO yêu cầu
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return nv;
    }
    public String getTenNhanVien(String manv) {
        String tenNV = "";
        String sql = "SELECT tennv FROM nhanvien WHERE manv = ?";
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, manv);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    tenNV = rs.getString("tennv");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return tenNV;
    }

}

