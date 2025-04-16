package GUI.Admin.customer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import DAO.Customer_DAO;
import BLL.Customer_BLL;
import BLL.NutGiaoDien_BLL;
import BLL.NutSuKien_BLL;
import DTO.KhachHang_DTO;

public class CustomerTable extends javax.swing.JPanel implements GUI.Admin.component.Header.searchListener{
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<KhachHang_DTO> customerList = new ArrayList<>();
    public CustomerTable() {
        initComponents();
        setLayout(null);
        setBounds(0,0, 800, 650);
        setBackground(Color.WHITE);
        JButton btnSortAsc = new JButton("Sắp xếp Tăng");
        btnSortAsc.setBounds(20, 0, 150, 30);
        add(btnSortAsc);
        this.setName("customer");
        // Nút sắp xếp giảm
        JButton btnSortDesc = new JButton("Sắp xếp Giảm");
        btnSortDesc.setBounds(200, 0, 150, 30);
        add(btnSortDesc);
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
        Customer_DAO customerDAO = new Customer_DAO();
        customerDAO.loadDataFormDatabase(customerList, tableModel);
        // Cột "Tác vụ" có 2 nút "Chi tiết" và "Xóa"
        table.getColumn("Tác vụ").setCellRenderer(new NutGiaoDien_BLL("customer"));
        table.getColumn("Tác vụ").setCellEditor(new NutSuKien_BLL(this,tableModel));

        // ScrollPane chứa bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(2, 40 , 770, 570);
        add(scrollPane);
        revalidate();
        repaint();
        btnSortAsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer_BLL customerBLL = new Customer_BLL();
                customerBLL.sortTable(true,tableModel,table); // Sắp xếp tăng
            }
        });

        btnSortDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer_BLL customerBLL = new Customer_BLL();
                customerBLL.sortTable(false,tableModel,table); // Sắp xếp giảm
            }
        });
    }
    public ArrayList<KhachHang_DTO> getCustomerList() {
        return this.customerList;
    }
    @Override
    public void onSearch(String text) {
        Customer_BLL customerBLL = new Customer_BLL();
        customerBLL.searchById(text,tableModel,table);
    }
    @Override
    public void onFilterByRole(String role) {
        
    }
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.BorderLayout());
    }
}
