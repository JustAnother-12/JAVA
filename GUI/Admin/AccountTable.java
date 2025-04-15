package GUI.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.*;
import BLL.SapXepTangGiam;
import DTO.NhanVien_DTO.NhanVien_DTO;
import DAO.NhanVien_DAO;
import DAO.NhanVien_DAO.DatabaseConnection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AccountTable extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Object> accountList = new ArrayList<>();
    public AccountTable() {
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
                new AddAccount(tableModel); // Mở form thêm nhân viên
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
        nhanVien_DAO.loadDataFormDatabase(tableModel, accountList);
        // Cột "Tác vụ" có 2 nút "Chi tiết" và "Xóa"
        table.getColumn("Tác vụ").setCellRenderer(new ButtonRenderer("staff"));
        table.getColumn("Tác vụ").setCellEditor(new ButtonEditor(this));

        // ScrollPane chứa bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(2, 40 , 770, 570);
        add(scrollPane);
        revalidate();
        repaint();
    }
    
    
    public ArrayList<Object> getAccountList() {
        return this.accountList;
    }
    
    private void initComponents() {

        setName(""); 
    }

    @Override
    public void onSearch(String text) {
        searchById(text);
    }

    @Override
    public void onFilterByRole(String role) {
        filterByRole(role);
    }

}