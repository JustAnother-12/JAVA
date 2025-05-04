package BLL;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import DAO.KhachHang_DAO;
import DAO.NhanVien_DAO;
import DTO.KhachHang_DTO;
import GUI.Admin.swing.CheckFailInput;

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

    public KhachHang_DTO getKhachHangFromID(String id){
        KhachHang_DTO kh = khachhangDao.getKhachHangfromID(id);
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
        CheckFailInput checkFailInput_BLL = new CheckFailInput();
        if (checkFailInput_BLL.validateFields(isCustomer, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, txtEmail, null, null)) {
            try {
                
                if (KhachHang_DAO.isCustomerPhoneExist(txtPhone.getText(), kh.getUsername())) {
                    JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return false;
                }

                if (KhachHang_DAO.isCustomerEmailExist(txtEmail.getText(), kh.getUsername())) {
                    JOptionPane.showMessageDialog(null, "Email đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return false;
                }

                if (!txtUsername.getText().equals(kh.getUsername()) && KhachHang_DAO.isCustomerUsernameExist(txtUsername.getText())) {
                    JOptionPane.showMessageDialog(null, "Username đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                KhachHang_DAO.updateCustomer(kh, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, txtEmail, cbGender);
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(KhachHang_DAO.class.getName()).log(Level.SEVERE, "Error updating customer", ex);
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
