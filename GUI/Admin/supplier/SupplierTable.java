package GUI.Admin.supplier;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import BLL.NhaCungCap_BLL;
import DTO.NhaCungCap_DTO;
import GUI.Admin.swing.NutGiaoDien_BLL;
import GUI.Admin.swing.NutSuKien_BLL;

public class SupplierTable extends javax.swing.JPanel implements GUI.Admin.component.Header.searchListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<NhaCungCap_DTO> supplierList = new ArrayList<>();

    public SupplierTable() {
        initComponents();
        setLayout(null);
        setBounds(0, 0, 900, 650);
        setBackground(Color.WHITE);
        this.setName("supplier");

        JButton btnSortAsc = new JButton("Sắp xếp Tăng");
        btnSortAsc.setBounds(20, 0, 150, 30);
        add(btnSortAsc);

        JButton btnSortDesc = new JButton("Sắp xếp Giảm");
        btnSortDesc.setBounds(200, 0, 150, 30);
        add(btnSortDesc);
        
        JButton btnThemNCC = new JButton("Thêm NCC");
        btnThemNCC.setBounds(380, 0, 150, 30);
        add(btnThemNCC);
        btnThemNCC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themNhaCungCap();
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
        table.getColumn("Tác vụ").setCellEditor(new NutSuKien_BLL(this, tableModel));
        table.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(table);
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
        javax.swing.JTextField txtMa = new javax.swing.JTextField();
        javax.swing.JTextField txtTen = new javax.swing.JTextField();
        javax.swing.JTextField txtSdt = new javax.swing.JTextField();
        javax.swing.JTextField txtEmail = new javax.swing.JTextField();
    
        Object[] message = {
            "Mã NCC:", txtMa,
            "Tên NCC:", txtTen,
            "SĐT:", txtSdt,
            "Email:", txtEmail
        };
    
        int option = javax.swing.JOptionPane.showConfirmDialog(this, message, "Thêm Nhà Cung Cấp", javax.swing.JOptionPane.OK_CANCEL_OPTION);
        if (option == javax.swing.JOptionPane.OK_OPTION) {
            String ma = txtMa.getText().trim();
            String ten = txtTen.getText().trim();
            String sdt = txtSdt.getText().trim();
            String email = txtEmail.getText().trim();
    
            if (!ma.isEmpty() && !ten.isEmpty()) {
                NhaCungCap_DTO ncc = new NhaCungCap_DTO(ma, ten, sdt, email);
                NhaCungCap_BLL bll = new NhaCungCap_BLL();
                if (bll.themNCC(ncc)) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    supplierList.add(ncc);
                    tableModel.addRow(new Object[]{ma, ten, sdt, email, ""});
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            }
        }
    }
    
}

