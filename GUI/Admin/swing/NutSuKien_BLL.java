package GUI.Admin.swing;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

import BLL.DonHang_BLL;
import BLL.KhachHang_BLL;
import BLL.NhanVien_BLL;
import DTO.KhachHang_DTO;
import DTO.NhanVien_DTO;
import DTO.Order_DTO;
import GUI.Admin.customer.CustomerTable;
import GUI.Admin.staff.ChiTietNhanVien;
import GUI.Admin.staff.NhanVienTable;
import GUI.Admin.order.*;
import java.awt.*;
import java.sql.SQLException;
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

        btnDelete.addActionListener(e -> {
            try {
                deleteRow(tableModel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        btnConfirm.addActionListener(e -> {
            try {
                confirmOrder(tableModel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
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
        return true;
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        panel.removeAll();
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
    private void deleteRow(DefaultTableModel tableModel) throws SQLException{
        if (table != null) {
            String id = table.getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (formType == FormType.STAFF) {
                    NhanVien_BLL temp = new NhanVien_BLL();
                    temp.deleteStaff(id, tableModel, staffList.getAccountList());
                } else if (formType == FormType.CUSTOMER){
                    KhachHang_BLL temp = new KhachHang_BLL();
                    temp.deleteCustomer(id, tableModel, customerList.getCustomerList());
                } else if (formType == FormType.ORDER) {
                    DonHang_BLL temp = new DonHang_BLL();
                    temp.DeleteOrder(id);
                }
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                stopCellEditing();
            }
        }
    }
    private void confirmOrder(DefaultTableModel tableModel) throws SQLException {
        if (table == null || selectedRow < 0 || formType != FormType.ORDER || orderList == null) {
            JOptionPane.showMessageDialog(panel, "Không thể xác nhận đơn hàng. Dữ liệu không hợp lệ.");
            return;
        }
    
        String id = table.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có chắc chắn muốn xác nhận đơn hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            DonHang_BLL temp = new DonHang_BLL();
            String isConfirmed = temp.confirmOrder(id);
    
            if ("Duyệt đơn hàng thành công!".equals(isConfirmed)) {
                orderList.getOrderList().clear();
                orderList.getOrderDetailList().clear();
    
                int columnIndex = -1;
                try {
                    columnIndex = table.getColumnModel().getColumnIndex("Tác vụ");
                } catch (IllegalArgumentException e) {
                    columnIndex = -1;
                }
    
                DefaultTableModel newTableModel = new DefaultTableModel();
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    if (i != columnIndex) {
                        newTableModel.addColumn(tableModel.getColumnName(i));
                    }
                }
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    Object[] rowData = new Object[newTableModel.getColumnCount()];
                    int newColIndex = 0;
                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        if (i != columnIndex) {
                            rowData[newColIndex++] = tableModel.getValueAt(row, i);
                        }
                    }
                    newTableModel.addRow(rowData);
                }
    
                table.setModel(newTableModel);
                tableModel = newTableModel;
    
                // ❗️ Xoá toàn bộ dữ liệu bảng cũ
                tableModel.setRowCount(0);
    
                // Load lại dữ liệu
                temp.LoadDataToTabel(tableModel, orderList.getOrderList(), orderList.getOrderDetailList());
    
                // Thêm lại cột "Tác vụ"
                tableModel.addColumn("Tác vụ");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt("Tác vụ", i, tableModel.getColumnCount() - 1);
                }
    
                // Cập nhật lại renderer & editor
                table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien_BLL("order", orderList.getOrderList()));
                table.getColumn("Tác vụ").setCellEditor(new NutSuKien_BLL(orderList, tableModel));
    
                // Cập nhật lại kích thước cột
                table.getColumnModel().getColumn(0).setPreferredWidth(100);
                table.getColumnModel().getColumn(1).setPreferredWidth(150);
                table.getColumnModel().getColumn(2).setPreferredWidth(100);
                table.getColumnModel().getColumn(3).setPreferredWidth(100);
                table.getColumnModel().getColumn(4).setPreferredWidth(100);
                table.getColumnModel().getColumn(5).setPreferredWidth(250);
    
                table.setDefaultEditor(Object.class, null);
    
                if (table.getCellEditor() != null) {
                    table.getCellEditor().stopCellEditing();
                }
    
                JOptionPane.showMessageDialog(panel, "Xác nhận đơn hàng thành công!");
            } else {
                JOptionPane.showMessageDialog(panel, "Xác nhận đơn hàng không thành công.");
            }
        }
    }
    
    
}
