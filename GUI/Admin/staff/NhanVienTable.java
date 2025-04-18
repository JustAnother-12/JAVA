package GUI.Admin.staff;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.*;

import BLL.NhanVien_BLL;
import BLL.NutGiaoDien_BLL;
import BLL.NutSuKien_BLL;
import BLL.SapXepTangGiam;
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
        setBounds(0,0, 800, 650);
        setBackground(Color.WHITE);
        this.setName("staff");
        // Nút "Thêm tài khoản" ở góc trên bên phải
        JButton btnAdd = new JButton("Thêm tài khoản");
        btnAdd.setBounds(620, 5, 150, 30);
        add(btnAdd);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ThemNhanVien(tableModel); // Mở form thêm nhân viên
            }
        });
        // Nút sắp xếp tăng
        JButton btnSortAsc = new JButton("Sắp xếp Tăng");
        btnSortAsc.setBounds(20, 0, 150, 30);
        add(btnSortAsc);

        // Nút sắp xếp giảm
        JButton btnSortDesc = new JButton("Sắp xếp Giảm");
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
        NhanVien_DAO nhanVien_DAO = new NhanVien_DAO(); 
        nhanVien_DAO.loadDataFormDatabase(tableModel, staffList);
        // Cột "Tác vụ" có 2 nút "Chi tiết" và "Xóa"
        table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien_BLL("staff"));
        table.getColumn("Tác vụ").setCellEditor(new NutSuKien_BLL(this,tableModel));

        // ScrollPane chứa bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(2, 40 , 770, 570);
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
        NhanVien_BLL temp = new NhanVien_BLL();
        temp.searchById(text, tableModel, table);
    }

    @Override
    public void onFilterByRole(String role) {
        NhanVien_DAO temp = new NhanVien_DAO();
        temp.filterByRole(role, tableModel, staffList);
    }

}