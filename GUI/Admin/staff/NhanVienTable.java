package GUI.Admin.staff;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;

import BLL.NhanVien_BLL;
import GUI.Admin.swing.NutGiaoDien;
import GUI.Admin.swing.NutSuKien;
import GUI.Admin.swing.SapXepTangGiam;
import utils.MyButton;
import utils.MyScrollBarUI;
import DTO.NhanVien_DTO;
import DAO.NhanVien_DAO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NhanVienTable extends JPanel  implements GUI.Admin.component.Header.searchListener{
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<NhanVien_DTO> staffList = new ArrayList<>();
    public NhanVienTable() {
        initComponents();
        setLayout(null);
        setBounds(0,0, 900, 650);
        setBackground(Color.WHITE);
        this.setName("staff");
        // Nút "Thêm tài khoản" ở góc trên bên phải
        MyButton btnAdd = new MyButton("Thêm tài khoản");
        btnAdd.setFont(new Font("Segoe UI", 0, 14));
        btnAdd.setBackground(new Color(255, 153, 0));
        btnAdd.setBounds(620, 5, 150, 30);
        add(btnAdd);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ThemNhanVien(tableModel,staffList,table,NhanVienTable.this); // Mở form thêm nhân viên
            }
        });
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
        
        btnSortAsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SapXepTangGiam sortTable = new SapXepTangGiam();
                sortTable.sortTable(true,tableModel,table); // Sắp xếp tăng
            }
        });

        btnSortDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SapXepTangGiam sortTable = new SapXepTangGiam();
                sortTable.sortTable(false,tableModel,table); // Sắp xếp giảm
            }
        });
        // Cấu hình bảng
        String[] columnNames = {"ID", "Họ tên", "Username", "Số Điện thoại", "Tác vụ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);

        // Căn giữa dữ liệu trong bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Thiết lập renderer cho từng cột
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Thêm dữ liệu mẫu
        NhanVien_BLL NhanVien_BLL = new NhanVien_BLL();
        NhanVien_BLL.loadDataToTable(tableModel, staffList);
        // Cột "Tác vụ" có 2 nút "Chi tiết" và "Xóa"
        table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien("staff"));
        table.getColumn("Tác vụ").setCellEditor(new NutSuKien(this,tableModel));
        table.setDefaultEditor(Object.class, null);
        // ScrollPane chứa bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        scrollPane.setBounds(2, 40 , 860, 570);
        add(scrollPane);
        revalidate();
        repaint();
    }
    
    
    public ArrayList<NhanVien_DTO> getAccountList() {
        return this.staffList;
    }
    
    private void initComponents() {

        setName(""); 
    }

    @Override
    public void onSearch(String text) {
        searchById(text, tableModel, table);
        searchByName(text, tableModel, table);
    }

    @Override
    public void onFilterByRole(String role) {
        NhanVien_DAO temp = new NhanVien_DAO();
        temp.filterByRole(role, tableModel, staffList);
    }
    public void searchById(String text,DefaultTableModel tableModel,JTable table) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null); // Hiện toàn bộ
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 0)); // Theo cột ID
        }
    }
    public void searchByName(String text,DefaultTableModel tableModel,JTable table) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null); // Hiện toàn bộ
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 1)); // Theo cột Name
        }
    }
}