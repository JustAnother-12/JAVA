package GUI.Admin.supplier;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.*;

import BLL.NhaCungCap_BLL;
import DTO.NhaCungCap_DTO;
import GUI.Admin.swing.NutGiaoDien_BLL;
import GUI.Admin.swing.NutSuKien_BLL;
import utils.*;

public class SupplierTable extends javax.swing.JPanel implements GUI.Admin.component.Header.searchListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<NhaCungCap_DTO> supplierList = new ArrayList<>();
    private NutSuKien_BLL nsk;

    // Regex cho email
    private static final String EMAIL_PATTERN = 
    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
 
    // Regex cho số điện thoại Việt Nam (bắt đầu bằng 0, theo sau là 9 số)
    private static final String PHONE_PATTERN = 
        "^0[35789][0-9]{8}$";


    public SupplierTable() {
        initComponents();
        setLayout(null);
        setBounds(0, 0, 900, 650);
        setBackground(Color.WHITE);
        this.setName("supplier");

        // Nút sắp xếp tăng
        MyButton btnSortAsc = new MyButton("Sắp xếp Tăng");
        btnSortAsc.setFont(new Font("Segoe UI", 0, 14));
        btnSortAsc.setBackground(Color.decode("#00B4DB"));
        btnSortAsc.setBounds(20, 0, 150, 30);
        add(btnSortAsc);

        // Nút sắp xếp giảm
        MyButton btnSortDesc = new MyButton("Sắp xếp Giảm");
        btnSortDesc.setFont(new Font("Segoe UI", 0, 14));
        btnSortDesc.setBackground(Color.decode("#00B4DB"));
        btnSortDesc.setBounds(200, 0, 150, 30);
        add(btnSortDesc);
        
        MyButton btnThemNCC = new MyButton("Thêm nhà cung cấp");
        btnThemNCC.setFont(new Font("Segoe UI", 0, 14));
        btnThemNCC.setBackground(new Color(255, 153, 0));
        btnThemNCC.setBounds(380, 0, 150, 30);
        add(btnThemNCC);
        btnThemNCC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themNhaCungCap();
            }
        });
        MyButton btnSuaNCC = new MyButton("Sửa nhà cung cấp");
        btnSuaNCC.setFont(new Font("Segoe UI", 0, 14));
        btnSuaNCC.setBackground(new Color(255, 204, 0)); // màu khác tí
        btnSuaNCC.setBounds(560, 0, 150, 30);
        add(btnSuaNCC);

        btnSuaNCC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suaNhaCungCap();
            }
       });
        String[] columnNames = {"ID", "Tên NCC", "SĐT", "Email", "Tác vụ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        NhaCungCap_BLL nccBLL = new NhaCungCap_BLL();
        nccBLL.LoadDataToTabel(tableModel, supplierList);
        table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien_BLL("supplier"));
        nsk = new NutSuKien_BLL(this, tableModel);
        table.getColumn("Tác vụ").setCellEditor(nsk);
        table.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        scrollPane.setBounds(2, 40, 860, 570);
        add(scrollPane);
        revalidate();
        repaint();

        btnSortAsc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortTable(true);
            }
        });

        btnSortDesc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortTable(false);
            }
        });
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }

    @Override
    public void onSearch(String text) {
        searchByIdOrName(text);
    }

    @Override
    public void onFilterByRole(String role) {
        // Không áp dụng cho NCC
    }

    public void searchByIdOrName(String text) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 0, 1)); // ID hoặc Tên NCC
        }
    }

    public void sortTable(boolean ascending) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        SortOrder order = ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING;
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0, order)));
        sorter.sort();
    }
    public ArrayList<NhaCungCap_DTO> getSupplierList() {
        return supplierList;
    }
    private void themNhaCungCap() {
        javax.swing.JTextField txtTen = new javax.swing.JTextField();
        javax.swing.JTextField txtSdt = new javax.swing.JTextField();
        javax.swing.JTextField txtEmail = new javax.swing.JTextField();

        
    
        Object[] message = {
            "Tên NCC:", txtTen,
            "SĐT:", txtSdt,
            "Email:", txtEmail
        };
    
        int option = javax.swing.JOptionPane.showConfirmDialog(this, message, "Thêm Nhà Cung Cấp", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (option == javax.swing.JOptionPane.OK_OPTION) {
            String ten = txtTen.getText().trim();
            String sdt = txtSdt.getText().trim();
            String email = txtEmail.getText().trim();
    
            if (ten.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            }else{
                // Kiểm tra định dạng số điện thoại
                if (!Pattern.matches(PHONE_PATTERN, sdt)) {
                    JOptionPane.showMessageDialog(this, 
                        "Số điện thoại không đúng định dạng!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra định dạng email
                if (!Pattern.matches(EMAIL_PATTERN, email)) {
                    JOptionPane.showMessageDialog(this, 
                        "Email không đúng định dạng!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                NhaCungCap_DTO ncc = new NhaCungCap_DTO("", ten, sdt, email);
                NhaCungCap_BLL bll = new NhaCungCap_BLL();
                if (bll.themNCC(ncc)) {
                javax.swing.JOptionPane.showMessageDialog(this, "Thêm thành công!");

               // Cập nhật lại dữ liệu từ DB thay vì thêm thủ công
               supplierList.clear();
               tableModel.setRowCount(0);
               bll.LoadDataToTabel(tableModel, supplierList);

               } else {
               javax.swing.JOptionPane.showMessageDialog(this, "Thêm thất bại!");
               }
            }
        }
    }
        private void suaNhaCungCap() {
        JComboBox<String> comboBox = new JComboBox<>();
        for (NhaCungCap_DTO ncc : supplierList) {
            comboBox.addItem(ncc.getMaNCC());
        }
    
        JTextField txtTen = new JTextField();
        JTextField txtSdt = new JTextField();
        JTextField txtEmail = new JTextField();
    
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedId = (String) comboBox.getSelectedItem();
                for (NhaCungCap_DTO ncc : supplierList) {
                    if (ncc.getMaNCC().equals(selectedId)) {
                        txtTen.setText(ncc.getTenNCC());
                        txtSdt.setText(ncc.getSdtNCC());
                        txtEmail.setText(ncc.getEmailNCC());
                        break;
                    }
                }
            }
        });
    
        Object[] message = {
            "Chọn ID NCC:", comboBox,
            "Tên NCC:", txtTen,
            "SĐT:", txtSdt,
            "Email:", txtEmail
        };
    
        int option = JOptionPane.showConfirmDialog(this, message, "Sửa Nhà Cung Cấp", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String ten = txtTen.getText().trim();
            String sdt = txtSdt.getText().trim();
            String email = txtEmail.getText().trim();
            String id = (String) comboBox.getSelectedItem();
    
            if (ten.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            } else {
                if (!Pattern.matches(PHONE_PATTERN, sdt)) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại không đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                if (!Pattern.matches(EMAIL_PATTERN, email)) {
                    JOptionPane.showMessageDialog(this, "Email không đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                NhaCungCap_DTO ncc = new NhaCungCap_DTO(id, ten, sdt, email);
                NhaCungCap_BLL bll = new NhaCungCap_BLL();
                if (bll.capNhatNCC(ncc)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
    
                    // Cập nhật trong supplierList
                    for (int i = 0; i < supplierList.size(); i++) {
                        if (supplierList.get(i).getMaNCC().equals(id)) {
                            supplierList.set(i, ncc);
                            break;
                        }
                    }
    
                    // Refresh lại bảng
                    tableModel.setRowCount(0);
                    for (NhaCungCap_DTO item : supplierList) {
                        tableModel.addRow(new Object[]{item.getMaNCC(), item.getTenNCC(), item.getSdtNCC(), item.getEmailNCC(), ""});
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        }
    }
    
}

