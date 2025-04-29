package GUI.Admin.swing;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import DAO.Customer_DAO;
import DAO.NhanVien_DAO;
import DTO.KhachHang_DTO;
import DTO.NhanVien_DTO;
import DTO.Order_DTO;
import DAO.Order_DAO;
import GUI.Admin.customer.CustomerTable;
import GUI.Admin.staff.ChiTietNhanVien;
import GUI.Admin.staff.NhanVienTable;
import GUI.Admin.order.*;
import java.awt.*;
import java.text.ParseException;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class NutSuKien_BLL implements TableCellEditor {
    private JPanel panel;
    private JButton btnDetail, btnDelete, btnConfirm;
    private int selectedRow;
    private JTable table;
    private NhanVienTable staffList;
    private CustomerTable customerList;
    private OrderTable orderList;
    public enum FormType { STAFF, CUSTOMER ,ORDER}
    private FormType formType;

    public NutSuKien_BLL(NhanVienTable staffList,DefaultTableModel tableModel) {
        this(staffList, null, null, FormType.STAFF,tableModel);
    }

    public NutSuKien_BLL(CustomerTable customerList,DefaultTableModel tableModel) {
        this(null, customerList, null, FormType.CUSTOMER,tableModel);
    }
    public NutSuKien_BLL(OrderTable orderList,DefaultTableModel tableModel) {
        this(null, null, orderList, FormType.ORDER,tableModel);
    }
    public NutSuKien_BLL(NhanVienTable accountList, CustomerTable customerList,OrderTable orderList, FormType type,DefaultTableModel tableModel) {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnDetail = new JButton("Chi tiết");
        btnDelete = new JButton("Xóa");
        btnConfirm = new JButton("Xác nhận");
        this.staffList = accountList;
        this.customerList = customerList;
        this.orderList = orderList;
        this.formType = type;

        btnDetail.addActionListener(e -> {
            try {
                showDetail();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });

        btnDelete.addActionListener(e -> deleteRow(tableModel));
    }
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
    @Override
    public void cancelCellEditing() {
        if (table != null) {
            table.getCellEditor().stopCellEditing();
        }
    }
    @Override
    public void addCellEditorListener(javax.swing.event.CellEditorListener l) {
        // Not implemented
    }
    @Override
    public void removeCellEditorListener(javax.swing.event.CellEditorListener l) {
        // Not implemented
    }
    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean stopCellEditing() {
        if (table != null) {
            table.getModel();
        }
        return true;
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.selectedRow = row;
        if (formType == FormType.STAFF || formType == FormType.CUSTOMER) {
            panel.add(btnDetail);
            panel.add(btnDelete);
        } else if (formType == FormType.ORDER) {
            if(orderList != null && row < orderList.getOrderList().size()) {
                Order_DTO temp = orderList.getOrderAt(row);
                if("Chưa xử lý".equalsIgnoreCase(temp.getTinhtrang())) 
                panel.add(btnConfirm);    
            }
            panel.add(btnDetail);
            panel.add(btnDelete);
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        String tableName = table.getName();
        if ("customer".equals(tableName) || "staff".equals(tableName)) 
            return "Chi tiết   Xóa";
        return "Xác nhận   Chi tiết   Xóa";
    }

    private void showDetailDialog(String title, String message) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDetail() throws ParseException {
        if (table != null) {
            String id = table.getValueAt(selectedRow, 0).toString();
            Object obj = null;
            if (formType == FormType.STAFF) {
                obj = findAccountById(id);
            } else if (formType == FormType.CUSTOMER) {
                obj = findCustomerById(id);
            } else if (formType == FormType.ORDER) {
                obj = findOrderById(id);
            }
            
            if (obj instanceof KhachHang_DTO) {
                new ChiTietNhanVien((KhachHang_DTO) obj); 
            } else if (obj instanceof NhanVien_DTO) {
                new ChiTietNhanVien((NhanVien_DTO) obj); 
            } else if(obj instanceof Order_DTO) {
                new OrderDetailForm((Order_DTO)obj, orderList.getOrderDetailList());
            } else {
                // Nếu không tìm thấy account hợp lệ, hiển thị thông báo
                showDetailDialog("Lỗi", "Không tìm thấy thông tin tài khoản.");
            }
        }
    } 
    private Object findAccountById(String id) {
        for (Object account : staffList.getAccountList()) {
            if (account instanceof NhanVien_DTO && ((NhanVien_DTO) account).getManv().equals(id)) {
                return account;
            }
        }
        return null;
    }
    private Object findCustomerById(String id) {
        for (Object account : customerList.getCustomerList()) {
            if (account instanceof KhachHang_DTO && ((KhachHang_DTO) account).getId_KhachHang().equals(id)) {
                return account;
            }
        }
        return null;
    }
    private Object findOrderById(String id) {
        for (Object order : orderList.getOrderList()) {
            if (order instanceof Order_DTO && ((Order_DTO) order).getMadonhang().equals(id)) {
                return order;
            }
        }
        return null;
    }
    private void deleteRow(DefaultTableModel tableModel) {
        if (table != null) {
            String id = table.getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (formType == FormType.STAFF) {
                    NhanVien_DAO temp = new NhanVien_DAO();
                    temp.deleteStaff(id, tableModel, staffList.getAccountList());
                } else if (formType == FormType.CUSTOMER){
                    Customer_DAO temp = new Customer_DAO();
                    temp.deleteCustomer(id, tableModel, customerList.getCustomerList());
                } else if (formType == FormType.ORDER) {
                    Order_DAO temp = new Order_DAO();
                    temp.DeleteOrder(id);
                }
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                stopCellEditing();
            }
        }
    }
}
