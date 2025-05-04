package GUI.Admin.product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import BLL.NhaCungCap_BLL;
import BLL.PhieuNhap_BLL;
import DTO.ChiTietPhieuNhap_DTO;
import DTO.NhaCungCap_DTO;
import DTO.NhanVien_DTO;
import DTO.PhieuNhap_DTO;
import utils.*;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class importListFrame extends JFrame{
    private JPanel mainPanel;
    private JScrollPane tableScrollPane;
    private JTable importTable;
    private JLabel TotalLabel;
    private JLabel TotalValueLabel;
    private MyButton NhapHangButton;
    private JLabel providerLabel;
    private JComboBox<String> providerComboBox;
    private JLabel quantityLabel; 
    private JSpinner quantitySpinner;
    private MyButton deleteButton;

    private NhaCungCap_BLL nccBLL;
    private PhieuNhap_BLL pnBLL;
    private ArrayList<ChiTietPhieuNhap_DTO> importList;
    private ArrayList<NhaCungCap_DTO> danhSachNCC;
    private double TotalValue;
    private int position = -1;
    private NhanVien_DTO currentNV;

    public importListFrame(ArrayList<ChiTietPhieuNhap_DTO> importList, NhanVien_DTO currentNV){
        this.currentNV = currentNV;
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
        pnBLL = new PhieuNhap_BLL();
        mainPanel = new JPanel();
        tableScrollPane = new JScrollPane();
        importTable = new JTable();
        TotalLabel = new JLabel();
        TotalValueLabel = new JLabel();
        NhapHangButton = new MyButton();
        quantityLabel = new JLabel();
        quantitySpinner = new JSpinner();
        deleteButton = new MyButton();

        providerLabel = new JLabel();
        nccBLL = new NhaCungCap_BLL();
        danhSachNCC = nccBLL.getAllNCC();
        String[] listTenNCC = new String[danhSachNCC.size()];
        for (int i = 0; i < danhSachNCC.size(); i++) {
            listTenNCC[i] = danhSachNCC.get(i).getTenNCC();
        }
        providerComboBox = new JComboBox<>(listTenNCC);


        importTable.setModel(new DefaultTableModel(
            new Object[][] {},
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

        tableScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
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

        quantitySpinner.setBackground(Color.WHITE);
        quantitySpinner.setFont(new Font("Segoe UI", 0, 16));
        quantitySpinner.setPreferredSize(new Dimension(200,30));
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
        quantitySpinner.setModel(model);
        quantitySpinner.addChangeListener(e -> {
            if (position >= 0) {
                int newQuantity = (int) quantitySpinner.getValue();
                if(newQuantity > 0){
                    importList.get(position).setSoluongNhap(newQuantity);
                    importTable.setRowSelectionInterval(position, position); // Giữ lại dòng đang chọn
                }else{
                    importList.remove(position);
                    position = -1;
                }
                showImports(importList); // Cập nhật lại bảng
                TotalValueLabel.setText(String.valueOf(getTotalValue())); // Tính lại tổng tiền
            }
        });
        JComponent editor = quantitySpinner.getEditor();
        JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        rightPanel.add(quantitySpinner);

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

        TotalLabel.setFont(new Font("Segoe UI", 0, 14));
        TotalLabel.setText("Tổng cộng:");
        footerPanel.add(TotalLabel);

        TotalValueLabel.setFont(new Font("Segoe UI", 0, 14));
        TotalValueLabel.setPreferredSize(new Dimension(200,30));
        TotalValueLabel.setText(String.valueOf(getTotalValue()));
        TotalValueLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        footerPanel.add(TotalValueLabel);


        NhapHangButton.setBackground(Color.decode("#00B4DB"));
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

        setSize(800, 400);
        setMinimumSize(new Dimension(800, 400));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private Double getTotalValue(){
        TotalValue = 0.0;
        for (ChiTietPhieuNhap_DTO ct:importList){
            TotalValue+=ct.getDongiaNhap()*ct.getSoluongNhap();
        }
        return TotalValue;
    }

    private void importTableMouseClicked(MouseEvent evt) {
        position = importTable.getSelectedRow();
        importTable.setRowSelectionInterval(position, position);
        ChiTietPhieuNhap_DTO ct = importList.get(position);
        quantitySpinner.setValue(ct.getSoluongNhap());
    }

    private void deleteButtonActionPerformed(ActionEvent evt){
        if(position>=0){
            importList.remove(position);
            showImports(importList);
            TotalValueLabel.setText(String.valueOf(getTotalValue())); // Tính lại tổng tiền
            position = -1;
        }else{
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void NhapHangButtonActionPerformed(ActionEvent evt){
        NhaCungCap_DTO nhacungcap = null;
        for(NhaCungCap_DTO ncc:danhSachNCC){
            if(ncc.getTenNCC().equals(providerComboBox.getSelectedItem().toString())){
                nhacungcap = ncc;
            }
        }
        if (nhacungcap != null && !importList.isEmpty()){
            PhieuNhap_DTO pn = new PhieuNhap_DTO("",nhacungcap.getMaNCC(), currentNV.getManv(), getCurrentDay(), importList);
            JOptionPane.showMessageDialog(this, pnBLL.addPhieuNhap(pn), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            importList.clear();
            showImports(importList);
            TotalValueLabel.setText(String.valueOf(getTotalValue())); // Tính lại tổng tiền
            position = -1;
        }
        else{
            JOptionPane.showMessageDialog(this, "Danh sách rỗng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getCurrentDay() {
        LocalDate ngayHienTai = LocalDate.now();
        DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ngayHienTai.format(dinhDang);
    }
}
