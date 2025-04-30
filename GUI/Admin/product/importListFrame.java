package GUI.Admin.product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import BLL.NhaCungCap_BLL;
import DTO.ChiTietPhieuNhap_DTO;
import DTO.NhaCungCap_DTO;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class importListFrame extends JFrame{
    private JPanel mainPanel;
    // private JLabel headerLabel;
    private JScrollPane tableScrollPane;
    private JTable importTable;
    private JLabel TotalLabel;
    private JLabel TotalValueLabel;
    private JButton NhapHangButton;
    private JLabel providerLabel;
    private JComboBox<String> providerComboBox;
    private JLabel quantityLabel; 
    private JTextField quantityTextField;
    private JButton deleteButton;

    private NhaCungCap_BLL nccBLL;
    private ArrayList<ChiTietPhieuNhap_DTO> importList;
    private double TotalValue;
    private int position = -1;

    public importListFrame(ArrayList<ChiTietPhieuNhap_DTO> importList){
        this.importList = importList;
        initComponents();
        showImports(importList);
    }

    private void showImports(ArrayList<ChiTietPhieuNhap_DTO> importList) {
        DefaultTableModel model = (DefaultTableModel) importTable.getModel();
        model.setRowCount(0);
        Object[] row = new Object[3];
        for (int i = 0; i < importList.size(); i++) {
            row[0] = importList.get(i).getThongtinSP().getTen_SanPham();
            row[1] = importList.get(i).getSoluongNhap();
            row[2] = importList.get(i).getDongiaNhap();
            model.addRow(row);
        }
    }

    private void initComponents(){
        mainPanel = new JPanel();
        // headerLabel = new JLabel();
        tableScrollPane = new JScrollPane();
        importTable = new JTable();
        TotalLabel = new JLabel();
        TotalValueLabel = new JLabel();
        NhapHangButton = new JButton();
        quantityLabel = new JLabel();
        quantityTextField = new JTextField();
        deleteButton = new JButton();

        providerLabel = new JLabel();
        nccBLL = new NhaCungCap_BLL();
        ArrayList<NhaCungCap_DTO> danhSachNCC = nccBLL.getAllNCC();
        String[] listTenNCC = new String[danhSachNCC.size()];
        for (int i = 0; i < danhSachNCC.size(); i++) {
            listTenNCC[i] = danhSachNCC.get(i).getTenNCC();
        }
        providerComboBox = new JComboBox<>(listTenNCC);


        importTable.setModel(new DefaultTableModel(
            new Object[][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String[] {
                "Tên sản phẩm", "Số lượng", "Đơn giá"
            }
        ));

        importTable.setRowHeight(35);
        importTable.setGridColor(new Color(51, 51, 51));
        importTable.setShowGrid(true);
        importTable.setDefaultEditor(Object.class, null);
        importTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                importTableMouseClicked(evt);
            }
        });

        // chỉnh độ rộng cột
        TableColumnModel columnModel = importTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200); 
        columnModel.getColumn(1).setPreferredWidth(100); 
        columnModel.getColumn(2).setPreferredWidth(100); 


        tableScrollPane.setViewportView(importTable);
        tableScrollPane.setPreferredSize(new Dimension(531, 466));

        // toppabel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 255, 255));

        // Left side: Search
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setBackground(Color.WHITE);

        providerLabel.setText("Chọn nhà cung cấp:");
        providerLabel.setFont(new Font("Segoe UI", 0, 14));
        leftPanel.add(providerLabel);
        
        providerComboBox.setBackground(Color.WHITE);
        providerComboBox.setFont(new Font("Segoe UI", 0, 14));
        providerComboBox.setPreferredSize(new Dimension(150, 30));
        providerComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                
            }
        });
        leftPanel.add(providerComboBox);

        // Right side: Buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(Color.WHITE);

        quantityLabel.setFont(new Font("Segoe UI", 0, 16));
        quantityLabel.setText("Số lượng:");
        rightPanel.add(quantityLabel);

        quantityTextField.setBackground(Color.WHITE);
        quantityTextField.setFont(new Font("Segoe UI", 0, 16));
        quantityTextField.setPreferredSize(new Dimension(200,30));
        quantityTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // chặn nếu không phải số
                }
            }
        });
        rightPanel.add(quantityTextField);

        deleteButton.setBackground(new Color(51, 51, 51));
        deleteButton.setFont(new Font("Segoe UI", 0, 14));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setText("Xóa");
        deleteButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        rightPanel.add(deleteButton);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);


        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(255, 255, 255));
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JPanel TotalPanel = new JPanel();
        TotalPanel.setBackground(new Color(255, 255, 255));
        TotalPanel.setLayout(new FlowLayout());
        TotalPanel.setPreferredSize(new Dimension(400, 30));

        TotalLabel.setFont(new Font("Segoe UI", 0, 14));
        TotalLabel.setText("Tổng cộng:");
        TotalPanel.add(TotalLabel);

        TotalValueLabel.setFont(new Font("Segoe UI", 0, 14));
        TotalValueLabel.setPreferredSize(new Dimension(200,30));
        TotalValueLabel.setText("0");
        TotalValueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        TotalPanel.add(TotalValueLabel);

        footerPanel.add(TotalPanel);

        NhapHangButton.setBackground(new Color(204, 204, 204));
        NhapHangButton.setFont(new Font("Segoe UI", 0, 14));
        NhapHangButton.setForeground(new Color(255, 255, 255));
        NhapHangButton.setText("Xác nhận nhập hàng");
        NhapHangButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                NhapHangButtonActionPerformed(evt);
            }
        });

        footerPanel.add(NhapHangButton);

        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setPreferredSize(new Dimension(962, 600));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // setup layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(1000, 600);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void importTableMouseClicked(MouseEvent evt) {
        position = importTable.getSelectedRow();
        importTable.setRowSelectionInterval(position, position);
    }

    private void deleteButtonActionPerformed(ActionEvent evt){

    }

    private void NhapHangButtonActionPerformed(ActionEvent evt){

    }
}
