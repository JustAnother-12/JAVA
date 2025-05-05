package GUI.Admin.swing;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

import BLL.DonHang_BLL;
import BLL.KhachHang_BLL;
import BLL.NhanVien_BLL;
import BLL.NhaCungCap_BLL;//
import BLL.PhieuNhap_BLL;
import DTO.NhaCungCap_DTO;//
import DTO.ChiTietPhieuNhap_DTO;
import DTO.KhachHang_DTO;
import DTO.NhanVien_DTO;
import DTO.Order_DTO;
import DTO.PhieuNhap_DTO;
import GUI.Admin.supplier.ChiTietNhaCungCap;//
import GUI.Admin.importorder.ImportDetailForm;//
import GUI.Admin.customer.CustomerTable;
import GUI.Admin.importorder.HistoryTable;
import GUI.Admin.staff.ChiTietNhanVien;
import GUI.Admin.staff.NhanVienTable;
import GUI.Admin.supplier.SupplierTable;
import GUI.Admin.order.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class NutSuKien implements TableCellEditor {
    private JPanel panel;
    private JButton btnDetail, btnDelete, btnConfirm;
    private int selectedRow;
    private JTable table;
    private NhanVienTable staffList;
    private CustomerTable customerList;
    private OrderTable orderList;
    private SupplierTable supplierList;
    private HistoryTable importList;
    public enum FormType { STAFF, CUSTOMER ,ORDER,SUPPLIER,IMPORT}
    private FormType formType;
    private NhanVien_DTO nv;

    public NutSuKien(NhanVienTable staffList,DefaultTableModel tableModel) {
        this(staffList, null, null,null ,null,FormType.STAFF,tableModel);
    }

    public NutSuKien(CustomerTable customerList,DefaultTableModel tableModel) {
        this(null, customerList, null,null, null,FormType.CUSTOMER,tableModel);
    }
    public NutSuKien(OrderTable orderList,DefaultTableModel tableModel, NhanVien_DTO nv) {
        this(null, null, orderList,null, null,FormType.ORDER,tableModel);
        this.nv = nv;
    }
    public NutSuKien(SupplierTable supplierList, DefaultTableModel tableModel) {
    this(null, null, null, supplierList,null, FormType.SUPPLIER, tableModel);
    }

    public NutSuKien(HistoryTable importList, DefaultTableModel tableModel) {
    this(null, null, null, null, importList, FormType.IMPORT, tableModel);
    }

public NutSuKien(NhanVienTable accountList, CustomerTable customerList,OrderTable orderList,SupplierTable supplierList,HistoryTable importList, FormType type,DefaultTableModel tableModel) {
    panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    btnDetail = new JButton("Chi tiết");
    btnDetail.setBackground(new Color(255, 153, 0));
    btnDetail.setForeground(Color.WHITE);
    btnDelete = new JButton("Xóa");
    btnDelete.setBackground(Color.RED);
    btnDelete.setForeground(Color.WHITE);
    btnConfirm = new JButton("Xử lý");
    btnConfirm.setBackground(new Color(255, 153, 0));
    btnConfirm.setForeground(Color.WHITE);
    this.staffList = accountList;
    this.customerList = customerList;
    this.orderList = orderList;
    this.supplierList = supplierList;
    this.importList = importList;
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
       Choice(tableModel);
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
        if (formType == FormType.STAFF || formType == FormType.CUSTOMER || formType == FormType.SUPPLIER || formType == FormType.IMPORT) {
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
        if ("customer".equals(tableName) || "staff".equals(tableName) || "supplier".equals(tableName) || "import".equals(tableName)) 
            return "Chi tiết   Xóa";
        return "Xử lý   Chi tiết   Xóa";
    }

    private void showDetailDialog(String title, String message) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void Choice(DefaultTableModel tableModel){
        int choice = JOptionPane.showOptionDialog(null, 
                                        "Hãy chọn tác vụ", 
                                        "Xử lý", 
                                        JOptionPane.YES_NO_OPTION, 
                                        JOptionPane.INFORMATION_MESSAGE, 
                                        null, 
                                        new Object[] {"Xác nhận","Hủy"}, null);
        if(choice == 0){
            try {
                confirmOrder(tableModel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        else if(choice == 1){
            try {
                cancelOrder(tableModel);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
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
            else if (formType == FormType.SUPPLIER) {
                obj = findSupplierById(id);
            }
            else if (formType == FormType.IMPORT) {
                obj = findImportById(id);
            }
            
            if (obj instanceof KhachHang_DTO) {
                new ChiTietNhanVien((KhachHang_DTO) obj); 
            } else if (obj instanceof NhanVien_DTO) {
                new ChiTietNhanVien((NhanVien_DTO) obj); 
            } else if(obj instanceof Order_DTO) {
                new OrderDetailForm((Order_DTO)obj, orderList.getOrderDetailList());
            }else if (obj instanceof NhaCungCap_DTO) {
                new ChiTietNhaCungCap((NhaCungCap_DTO) obj);
            }else if (obj instanceof PhieuNhap_DTO) {
                new ImportDetailForm((PhieuNhap_DTO) obj);
            }
            else {
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
    private Object findSupplierById(String id) {
        for (Object s : supplierList.getSupplierList()) {
            if (s instanceof NhaCungCap_DTO && ((NhaCungCap_DTO) s).getMaNCC().equals(id)) {
                return s;
            }
        }
        return null;
    }
    private Object findImportById(String id) {
        for (Object s: importList.getImportList()) {
            if (s instanceof PhieuNhap_DTO && ((PhieuNhap_DTO) s).getMaPN().equals(id)) {
                return s;
            }
        }
        return null;
    }
    private void deleteRow(DefaultTableModel tableModel) throws SQLException{
        if (table != null) {
            String id = table.getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean flag = true;
                if (formType == FormType.STAFF) {
                    NhanVien_BLL temp = new NhanVien_BLL();
                    boolean rs = temp.deleteStaff(id, tableModel, staffList.getAccountList());
                    if (rs == true) {
                        JOptionPane.showMessageDialog(null, "Xoá khách tài khoản khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE); 
                    } else {
                        JOptionPane.showMessageDialog(null, "Xoá thất bại! Có đơn hàng liên quan đến khách hàng này!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        flag = false;
                    }
                } else if (formType == FormType.CUSTOMER){
                    KhachHang_BLL temp = new KhachHang_BLL();
                    boolean rs = temp.deleteCustomer(id, tableModel, customerList.getCustomerList());
                    if (rs == true) {
                        JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy nhân viên để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        flag = false;
                    }
                } else if (formType == FormType.ORDER) {
                    DonHang_BLL temp = new DonHang_BLL();
                    boolean rs = temp.DeleteOrder(id);
                    if (rs == true) {
                        JOptionPane.showMessageDialog(null, "Xóa đơn hàng thành công!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy đơn hàng để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        flag = false;
                    }
                }else if (formType == FormType.SUPPLIER) {
                    NhaCungCap_BLL temp = new NhaCungCap_BLL();
                    PhieuNhap_BLL pnBLL = new PhieuNhap_BLL();
                    for(PhieuNhap_DTO pn:pnBLL.getAllPhieuNhap()){
                        if(pn.getMaNCC().equals(id)){
                            flag = false;
                            JOptionPane.showMessageDialog(null, "Xóa thất bại! Có phiếu nhập liên quan đến nhà cung cấp này", "Thông báo", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }
                    if(flag)
                        temp.deleteSupplier(id, tableModel, supplierList.getSupplierList());
                }
                else if (formType == FormType.IMPORT) {
                    PhieuNhap_BLL temp = new PhieuNhap_BLL();
                    temp.deleteImport(id, tableModel, importList.getImportList());
                }
                if (flag) {
                    ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                    stopCellEditing();
                }
            }
        }
    }
    private void cancelOrder(DefaultTableModel tableModel) throws SQLException {
        if (table == null || selectedRow < 0 || formType != FormType.ORDER || orderList == null) {
            JOptionPane.showMessageDialog(panel, "Không thể hủy đơn hàng. Dữ liệu không hợp lệ.");
            return;
        }
    
        String id = table.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có chắc chắn muốn hủy đơn hàng này?", "Hủy", JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            DonHang_BLL temp = new DonHang_BLL();
            String isConfirmed = temp.cancelOrder(id, nv.getManv());
            System.out.println(isConfirmed);
    
            if ("Hủy đơn hàng thành công!".equals(isConfirmed)) {
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
    
                // Xoá toàn bộ dữ liệu bảng cũ
                tableModel.setRowCount(0);
    
                // Load lại dữ liệu
                temp.LoadDataToTabel(tableModel, orderList.getOrderList(), orderList.getOrderDetailList());
    
                // Thêm lại cột "Tác vụ"
                tableModel.addColumn("Tác vụ");
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt("Tác vụ", i, tableModel.getColumnCount() - 1);
                }
    
                // Cập nhật lại renderer & editor
                table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien("order", orderList.getOrderList()));
                table.getColumn("Tác vụ").setCellEditor(new NutSuKien(orderList, tableModel, nv));
    
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
    
                JOptionPane.showMessageDialog(panel, "Hủy đơn hàng thành công!");
            } else {
                JOptionPane.showMessageDialog(panel, "Hủy đơn hàng không thành công.");
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
            String isConfirmed = temp.confirmOrder(id, nv.getManv());
    
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
                table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien("order", orderList.getOrderList()));
                table.getColumn("Tác vụ").setCellEditor(new NutSuKien(orderList, tableModel, nv));
    
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
