package BLL;

import DTO.KhachHang_DTO;
import GUI.Admin.swing.CheckFailInput_BLL;
import DAO.KhachHang_DAO;
import DAO.NhanVien_DAO;

import java.util.ArrayList;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.table.DefaultTableModel;

public class KhachHang_BLL extends JDialog{
    KhachHang_DAO khachhangDao = new KhachHang_DAO();

    public ArrayList<KhachHang_DTO> getAllKhachHang(){
        return khachhangDao.getAllKhachHang();
    }

    public KhachHang_DTO getKhachHangFromAccount(String username, String password){
        KhachHang_DTO kh = khachhangDao.getKhachHangFromAccount(username, password);
        if (kh != null)
            return kh;
        return null;
    }

    public String addKhachHang(KhachHang_DTO kh){
        if(khachhangDao.hasKhachHangID(kh.getId_KhachHang())){
            return "khách hàng đã tồn tại!";
        }
        if(khachhangDao.hasKhachHangUsername(kh.getUsername())){
            return "Username đã tồn tại!";
        }
        if(khachhangDao.addKhachHang(kh)){
            return "Thêm khách hàng thành công!";
        }
        return "Thêm khách hàng thất bại!";
    }

    public String removeKhachHang(KhachHang_DTO kh){
        if(!khachhangDao.hasKhachHangID(kh.getId_KhachHang())){
            return "khách hàng không tồn tại!";
        }
        if(khachhangDao.removeKhachHang(kh.getId_KhachHang())){
            return "Xoá khách hàng thành công!";
        }

        return "Xóa Khách hàng thất bại!";
    }
    public void LoadDataToTabel(DefaultTableModel tableModel, ArrayList<KhachHang_DTO> customerList) {
        KhachHang_DAO KhachHang_DAO = new KhachHang_DAO();
        KhachHang_DAO.loadDataFormDatabase(customerList, tableModel);
       }
    public boolean updateCustomer(JTextField txtName,JTextField txtPhone,JTextField txtUsername,JTextField txtAddress,JTextField txtBirthday, JTextField txtEmail,JComboBox<String> cbGender, boolean isCustomer, KhachHang_DTO kh) {
        KhachHang_DAO KhachHang_DAO = new KhachHang_DAO();
        CheckFailInput_BLL checkFailInput_BLL = new CheckFailInput_BLL();
        if (checkFailInput_BLL.validateFields(isCustomer, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, txtEmail, null, null)) {
            try {
                KhachHang_DAO.updateCustomer(kh, txtName, txtPhone, txtUsername, txtBirthday, txtAddress, txtEmail, cbGender);
                return true;
            } catch (ParseException ex) {
                Logger.getLogger(KhachHang_DAO.class.getName()).log(Level.SEVERE, "Error updating customer", ex);
            }
        }
        return false;
    }
    public void deleteCustomer(String id, DefaultTableModel tableModel, ArrayList<KhachHang_DTO> staffList) throws SQLException {
    try {
        KhachHang_DAO temp = new KhachHang_DAO();
        temp.deleteCustomer(id, tableModel, staffList);
    } catch (Exception ex) {
        Logger.getLogger(NhanVien_DAO.class.getName()).log(Level.SEVERE, "General Error deleting staff", ex);
    }
   }
}
