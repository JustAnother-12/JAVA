package GUI.Admin.swing;


import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.util.*;
public class CheckFailInput_BLL extends javax.swing.JDialog {
        public void loadExistingIDs(DefaultTableModel tableModel,HashSet<String> existingIDs) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                existingIDs.add(tableModel.getValueAt(i, 0).toString()); // Giả sử ID ở cột 0
            }
        }
        
        public boolean validateFields(boolean isCustomer,JTextField txtName,JTextField txtPhone,JTextField txtUsername,JTextField txtAddress, JTextField txtBirthday ,JTextField txtEmail,JTextField txtPosition,JTextField txtCCCD) {
        if (!isValidName(txtName.getText())) {
            JOptionPane.showMessageDialog(this, "Họ tên phải là chữ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isNumeric(txtPhone.getText()) || txtPhone.getText().length() > 12) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là số và chỉ tối đa 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (isCustomer && !isValidEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isValidDate(txtBirthday.getText())) {
            JOptionPane.showMessageDialog(this, "Ngày sinh phải theo định dạng DD/MM/YYYY!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!isCustomer) {
            if (!isValidName(txtPosition.getText())) {
                JOptionPane.showMessageDialog(this, "Chức vụ phải là chữ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!isNumeric(txtCCCD.getText()) || txtCCCD.getText().length() != 12) {
                JOptionPane.showMessageDialog(this, "Căn cước phải là 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true; 
    }
    
    public boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]+"); // Kiểm tra chỉ chứa chữ và khoảng trắng
    }

    public boolean isNumeric(String str) {
        return str.matches("\\d+"); // Kiểm tra chỉ chứa số
    }

    public boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$"); // Kiểm tra định dạng email
    }

    public boolean isValidDate(String date) {
        return date.matches("\\d{2}/\\d{2}/\\d{4}"); // Kiểm tra định dạng DD/MM/YYYY
    }
    
    public String generateRandomID() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Số ngẫu nhiên từ 0 đến 999
        return String.format("NV%03d", randomNumber); // Định dạng ID
    }
}
