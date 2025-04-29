package BLL;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import DAO.ChiTietThongTinTaiKhoan_DAO;
import DAO.Customer_DAO;
import DTO.KhachHang_DTO;
import GUI.Admin.staff.ChiTietNhanVien;
import GUI.Admin.swing.CheckFailInput_BLL;

public class Customer_BLL {
   public void LoadDataToTabel(DefaultTableModel tableModel, ArrayList<KhachHang_DTO> customerList) {
    Customer_DAO customerDAO = new Customer_DAO();
    customerDAO.loadDataFormDatabase(customerList, tableModel);
   }
   public void updateCustomer(JTextField txtName,JTextField txtPhone,JTextField txtUsername,JTextField txtAddress,JTextField txtBirthday, JTextField txtEmail,JComboBox<String> cbGender, boolean isCustomer, KhachHang_DTO kh) {
    ChiTietThongTinTaiKhoan_DAO chiTietNhanVien_DAO = new ChiTietThongTinTaiKhoan_DAO();
            CheckFailInput_BLL checkFailInput_BLL = new CheckFailInput_BLL();
            if (checkFailInput_BLL.validateFields(isCustomer, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, txtEmail, null, null)) {
                try {
                    chiTietNhanVien_DAO.updateCustomer(kh, txtName, txtPhone, txtUsername, txtAddress, txtBirthday,txtEmail);
                    dispose();
                } catch (ParseException ex) {
                    Logger.getLogger(ChiTietNhanVien.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
   }

   private void dispose() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'dispose'");
   }
    
}
