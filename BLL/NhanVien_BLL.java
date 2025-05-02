package BLL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import DAO.NhanVien_DAO;
import DAO.ThemNhanVien_DAO;
import DTO.NhanVien_DTO;
import GUI.Admin.staff.ChiTietNhanVien;
import GUI.Admin.swing.CheckFailInput_BLL;
import java.util.HashSet;

public class NhanVien_BLL {
    public void loadDataToTable(DefaultTableModel tableModel,ArrayList<NhanVien_DTO> staffList){
        NhanVien_DAO nhanVien_DAO = new NhanVien_DAO(); 
        nhanVien_DAO.loadDataFormDatabase(tableModel, staffList);
   }
   public boolean updateStaff(JTextField txtName,JTextField txtPhone,JTextField txtUsername,JTextField txtAddress,JTextField txtBirthday, JComboBox<String> txtPosition, JComboBox<String> cbGender, JTextField txtCCCD, boolean isCustomer, NhanVien_DTO nv) {
    NhanVien_DAO NhanVien_DAO = new NhanVien_DAO();
    CheckFailInput_BLL checkFailInput_BLL = new CheckFailInput_BLL();
    // Cập nhật thông tin
    if (checkFailInput_BLL.validateFields(isCustomer, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, null, txtPosition, txtCCCD)) {
        try {
            NhanVien_DAO.updateStaff(nv, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, txtPosition, txtCCCD, cbGender);;
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ChiTietNhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return false;
   }
   public boolean addStaff(JTextField txtName,JTextField txtPhone,JTextField txtUsername,JTextField txtAddress,JTextField txtBirthday, JComboBox<String> txtPosition, JComboBox<String> cbGender, JTextField txtCCCD, JTextField txtPassword, DefaultTableModel tableModel, HashSet<String> existingIDs) {
    try {
        ThemNhanVien_DAO addStaff = new ThemNhanVien_DAO();
        addStaff.addStaff(txtName, txtPosition, txtPhone, txtUsername, txtPassword, txtAddress, txtCCCD, txtBirthday,cbGender,tableModel,existingIDs);
        return true;
    } catch (Exception ex) {
        Logger.getLogger(ChiTietNhanVien.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
   }
   public void deleteStaff(String id, DefaultTableModel tableModel, ArrayList<NhanVien_DTO> staffList) throws SQLException {
    try {
        NhanVien_DAO temp = new NhanVien_DAO();
        temp.deleteStaff(id, tableModel, staffList);
    } catch (Exception ex) {
        Logger.getLogger(NhanVien_DAO.class.getName()).log(Level.SEVERE, "General Error deleting staff", ex);
    }
   }
}