package BLL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import DAO.NhanVien_DAO;
import DAO.ThemNhanVien_DAO;
import DTO.NhanVien_DTO;
import GUI.Admin.staff.ChiTietNhanVien;
import GUI.Admin.staff.NhanVienTable;
import GUI.Admin.swing.CheckFailInput;
import GUI.Admin.swing.NutGiaoDien;
import GUI.Admin.swing.NutSuKien;

public class NhanVien_BLL extends JDialog{
    public void loadDataToTable(DefaultTableModel tableModel,ArrayList<NhanVien_DTO> staffList){
        NhanVien_DAO nhanVien_DAO = new NhanVien_DAO(); 
        nhanVien_DAO.loadDataFormDatabase(tableModel, staffList);
   }
   private NhanVien_DAO NhanVien_DAO = new NhanVien_DAO();
   private CheckFailInput checkFailInput_BLL = new CheckFailInput();
   private ThemNhanVien_DAO ThemNhanVien_DAO = new ThemNhanVien_DAO();

   public boolean updateStaff(JTextField txtName,JTextField txtPhone,JTextField txtUsername,JTextField txtAddress,JTextField txtBirthday, JComboBox<String> txtPosition, JComboBox<String> cbGender, JTextField txtCCCD, boolean isCustomer, NhanVien_DTO nv) {
    // Cập nhật thông tin
    if (checkFailInput_BLL.validateFields(isCustomer, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, null, txtPosition, txtCCCD)) {
        try {
            // Kiểm tra số điện thoại
            if (NhanVien_DAO.isPhoneExist(txtPhone.getText(), nv.getUsername())) {
                JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // Kiểm tra CCCD
            if (NhanVien_DAO.isCCCDExist(txtCCCD.getText(), nv.getUsername())) {
                JOptionPane.showMessageDialog(null, "CCCD đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // Kiểm tra username mới
            if (!nv.getUsername().equals(txtUsername.getText()) && NhanVien_DAO.isUsernameExist(txtUsername.getText())) {
                JOptionPane.showMessageDialog(null, "Username đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            NhanVien_DAO.updateStaff(nv, txtName, txtPhone, txtUsername, txtAddress, txtBirthday, txtPosition, txtCCCD, cbGender);;
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ChiTietNhanVien.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return false;
   }
   public boolean addStaff(JTextField txtName, JComboBox<String> txtPosition, JTextField txtPhone,
                               JTextField txtUsername, JTextField txtPassword, JTextField txtAddress,
                               JTextField txtCCCD, JTextField txtBirthday, JComboBox<String> cbGender,
                               DefaultTableModel tableModel, HashSet<String> existingIDs,ArrayList<NhanVien_DTO> staffList,JTable table) {
        try {
            String name = txtName.getText().trim();
            String position = (String) txtPosition.getSelectedItem();
            String phone = txtPhone.getText().trim();
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String address = txtAddress.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String gender = (String) cbGender.getSelectedItem();
            String birthday = txtBirthday.getText().trim();

            // Validate
            if (checkFailInput_BLL.isValidName(name)) throw new Exception("Họ tên phải là chữ!");
            if (gender == null) throw new Exception("Giới tính phải chọn!");
            if (!checkFailInput_BLL.isNumeric(phone) || phone.length() > 12) throw new Exception("Số điện thoại không hợp lệ!");
            if (!checkFailInput_BLL.isNumeric(cccd) || cccd.length() != 12) throw new Exception("CCCD phải là 12 số!");
            if (!checkFailInput_BLL.isValidDate(birthday)) throw new Exception("Ngày sinh phải theo định dạng dd/MM/yyyy");

            // Convert ngày
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = outputFormat.format(inputFormat.parse(birthday));

            // Tạo ID duy nhất
            String id;
            do {
                id = ThemNhanVien_DAO.getNextNVID();
            } while (existingIDs.contains(id));

            // Kiểm tra trùng
            validateUniqueFields(txtPhone.getText(), txtCCCD.getText(), txtUsername.getText());
            
            // Tạo DTO
            NhanVien_DTO nv = new NhanVien_DTO(id, name, position, phone, username, password, address, cccd, gender, formattedDate);
            staffList.add(nv);
            // Thêm vào DB
            boolean success = ThemNhanVien_DAO.addStaff(nv);
            if (success) {
                tableModel.addRow(new Object[]{id, name, username, phone, "Chi tiết", "Xóa"});
                existingIDs.add(id);
                JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công!");
                new NhanVienTable();
                return true;
            } else {
                throw new Exception("Không thể thêm nhân viên vào cơ sở dữ liệu.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public NhanVien_DTO getDataOfStaff(NhanVien_DTO nv){
        return NhanVien_DAO.getDataOfStaff(nv);
    }

    public NhanVien_DTO checkLogin(String username, String password){
        return NhanVien_DAO.checkLogin(username,password);
    }

    private void validateUniqueFields(String phone, String cccd, String username) throws Exception {
        if (ThemNhanVien_DAO.isSDTExists(phone)) throw new Exception("Số điện thoại đã tồn tại.");
        if (ThemNhanVien_DAO.isCCCDExists(cccd)) throw new Exception("CCCD đã tồn tại.");
        if (ThemNhanVien_DAO.isUserNameExists(username)) throw new Exception("Username đã tồn tại.");
    }
    public boolean deleteStaff(String id, DefaultTableModel tableModel, ArrayList<NhanVien_DTO> staffList) {
        boolean deleted = NhanVien_DAO.deleteStaff(id, tableModel, staffList);
        if (deleted) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(id)) {
                tableModel.removeRow(i);
                break;
            }
        }
        staffList.removeIf(nv -> nv.getManv().equals(id));
        return true;
    } else {
        return false;
    }
    }
}