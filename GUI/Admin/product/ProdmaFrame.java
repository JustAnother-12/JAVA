package GUI.Admin.product;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import java.util.ArrayList;
import javax.swing.border.*;
import javax.swing.table.*;
import BLL.DonHang_BLL;
import BLL.PhieuNhap_BLL;
import BLL.SanPham_BLL;
import DTO.But_DTO;
import DTO.Sach_DTO;
import DTO.SanPham_DTO;
import DTO.Vo_DTO;
import DTO.ChiTietPhieuNhap_DTO;
import DTO.OrderDetail_DTO;
import GUI.user.ButDescription;
import GUI.user.SachDescription;
import GUI.user.VoDescription;
import GUI.Admin.component.Header;
import GUI.Admin.swing.SapXepTangGiam;
import utils.*;

public class ProdmaFrame extends JPanel implements Header.searchListener{

    private JComboBox<String> FilterComboBox;
    private MyButton importListButton;
    private MyButton restartButton;
    private MyButton addButton;
    private MyButton firstButton;
    private MyButton lastButton;
    private MyButton nextButton;
    private MyButton previousButton;
    private JPanel mainPanel;
    private JScrollPane productTableScrollPane;
    private JTable productTable;
    private ProductForm productForm;

    Border mainPanelBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.darkGray);
    ArrayList<SanPham_DTO> products = new ArrayList<>();
    int position = -1;

    private SanPham_BLL productBLL = new SanPham_BLL();
    private PhieuNhap_BLL pnBLL = new PhieuNhap_BLL();
    private DonHang_BLL orderBLL = new DonHang_BLL();
    private ArrayList<Sach_DTO> SachDescriptionList;
    private ArrayList<Vo_DTO> VoDescriptionList;
    private ArrayList<But_DTO> ButDescriptionList;
    private SachDescription CurrentSachDescription;
    private VoDescription CurrentVoDescription;
    private ButDescription CurrentButDescription;

    protected ArrayList<ChiTietPhieuNhap_DTO> importList = new ArrayList<>();

    public ProdmaFrame() {
        initComponents();
        getSPandDetails("Tất cả");
        showProducts(products);
        System.out.println(productTable.getSelectedRow());
    }

    public void showProducts(ArrayList<SanPham_DTO> productList) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);
        Object[] row = new Object[5];
        for (int i = 0; i < productList.size(); i++) {
            row[0] = productList.get(i).getID_SanPham();
            row[1] = productList.get(i).getTen_SanPham();
            row[2] = productList.get(i).getDanhMuc();
            row[3] = productList.get(i).getSoLuong_SanPham();
            row[4] = productList.get(i).getGia_SanPham();
            model.addRow(row);
        }
    }

    private void getSPandDetails(String choice){
        SachDescriptionList = new ArrayList<>();
        VoDescriptionList = new ArrayList<>();
        ButDescriptionList = new ArrayList<>();
        if(choice.equals("Tất cả")){
            products = productBLL.getAllSanPham();
        }else if(choice.equals("Sách")){
            products = new ArrayList<>(productBLL.getAllSach()); 
        }else if(choice.equals("Vở")){
            products = new ArrayList<>(productBLL.getAllVo());
        }else if(choice.equals("Bút")){
            products = new ArrayList<>(productBLL.getAllBut());
        }
        
        for (SanPham_DTO sp:products){
            if (sp instanceof Sach_DTO) {
                Sach_DTO sach = (Sach_DTO) sp;
                SachDescriptionList.add(sach);
            } else if (sp instanceof Vo_DTO) {
                Vo_DTO vo = (Vo_DTO) sp;
                VoDescriptionList.add(vo);
            } else if (sp instanceof But_DTO) {
                But_DTO but = (But_DTO) sp;
                ButDescriptionList.add(but);
            }
        }

    }

    private void initComponents() {
        mainPanel = new JPanel();
        productTableScrollPane = new JScrollPane();
        productTable = new JTable();
        restartButton = new MyButton();
        addButton = new MyButton();
        firstButton = new MyButton();
        previousButton = new MyButton();
        nextButton = new MyButton();
        lastButton = new MyButton();
        importListButton = new MyButton();


        productTable.setModel(new DefaultTableModel(
            new Object[][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String[] {
                "ID", "Tên", "Phân loại", "Số lượng", "Đơn giá", "Tác vụ"
            }
        ){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Chỉ cột "Tác vụ" mới có thể chỉnh sửa
            }
        });

        productTable.setRowHeight(35);
        productTable.setGridColor(new Color(51, 51, 51));
        productTable.setShowGrid(true);
        productTable.setDefaultEditor(Object.class, null);
        productTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                productTableMouseClicked(evt);
            }
        });

        // cài custom renderer và editor cho cột Tác vụ
        productTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        productTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), this));

        // chỉnh độ rộng cột
        TableColumnModel columnModel = productTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50); 
        columnModel.getColumn(1).setPreferredWidth(200); 
        columnModel.getColumn(2).setPreferredWidth(80); 
        columnModel.getColumn(3).setPreferredWidth(80); 
        columnModel.getColumn(4).setPreferredWidth(80); 
        columnModel.getColumn(5).setPreferredWidth(300); 

        productTableScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        productTableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        productTableScrollPane.setViewportView(productTable);
        productTableScrollPane.setPreferredSize(new Dimension(850, 570));


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 255, 255));

        // Left side:
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setBackground(Color.WHITE);

        // Nút sắp xếp tăng
        MyButton btnSortAsc = new MyButton("Sắp xếp Tăng");
        btnSortAsc.setFont(new Font("Segoe UI", 0, 14));
        btnSortAsc.setBackground(Color.decode("#00B4DB"));
        btnSortAsc.setPreferredSize(new Dimension(150,30));
        leftPanel.add(btnSortAsc);

        // Nút sắp xếp giảm
        MyButton btnSortDesc = new MyButton("Sắp xếp Giảm");
        btnSortDesc.setFont(new Font("Segoe UI", 0, 14));
        btnSortDesc.setBackground(Color.decode("#00B4DB"));
        btnSortDesc.setPreferredSize(new Dimension(150,30));
        leftPanel.add(btnSortDesc);
        
        btnSortAsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SapXepTangGiam sortTable = new SapXepTangGiam();
                sortTable.sortTable(true,(DefaultTableModel)productTable.getModel(),productTable); // Sắp xếp tăng
            }
        });

        btnSortDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SapXepTangGiam sortTable = new SapXepTangGiam();
                sortTable.sortTable(false,(DefaultTableModel)productTable.getModel(),productTable); // Sắp xếp giảm
            }
        });

        FilterComboBox = new JComboBox<>(new String[] { "Tất cả", "Sách", "Vở", "Bút" });
        FilterComboBox.setBackground(Color.WHITE);
        FilterComboBox.setFont(new Font("Segoe UI", 0, 14));
        FilterComboBox.setPreferredSize(new Dimension(80, 30));
        FilterComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                position = -1;
                getSPandDetails((String)FilterComboBox.getSelectedItem());
                showProducts(products);
            }
        });
        leftPanel.add(FilterComboBox);

        // Right side: Buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(Color.WHITE);

        addButton.setBackground(new Color(51, 51, 51));
        addButton.setFont(new Font("Segoe UI", 0, 14));
        addButton.setForeground(Color.WHITE);
        addButton.setText("Thêm");
        addButton.setPreferredSize(new Dimension(80, 30));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        rightPanel.add(addButton);

        restartButton.setBackground(new Color(51, 51, 51));
        restartButton.setFont(new Font("Segoe UI", 0, 14));
        restartButton.setForeground(Color.WHITE);
        restartButton.setText("Làm mới");
        restartButton.setPreferredSize(new Dimension(100, 30));
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                restartButtonActionPerformed(evt);
            }
        });
        rightPanel.add(restartButton);

        importListButton.setBackground(new Color(51, 51, 51));
        importListButton.setFont(new Font("Segoe UI", 0, 14));
        importListButton.setForeground(Color.WHITE);
        importListButton.setText("Danh sách nhập");
        importListButton.setPreferredSize(new Dimension(140, 30));
        importListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new importListFrame(importList);
            }
        });
        rightPanel.add(importListButton);


        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);


        // buttonPanel components
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        firstButton.setBackground(new Color(51, 51, 51));
        firstButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        firstButton.setForeground(new Color(255, 255, 255));
        firstButton.setText("<<");
        firstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                firstButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(firstButton);

        previousButton.setBackground(new Color(51, 51, 51));
        previousButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        previousButton.setForeground(new Color(255, 255, 255));
        previousButton.setText("<");
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(previousButton);

        nextButton.setBackground(new Color(51, 51, 51));
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nextButton.setForeground(new Color(255, 255, 255));
        nextButton.setText(">");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(nextButton);

        lastButton.setBackground(new Color(51, 51, 51));
        lastButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lastButton.setForeground(new Color(255, 255, 255));
        lastButton.setText(">>");
        lastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                lastButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(lastButton);


        
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setPreferredSize(new Dimension(900, 650));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(productTableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        // setup layout
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );


        setSize(900, 650);
        setMinimumSize(new Dimension(800, 500));
        setVisible(true);
    }

    private void productTableMouseClicked(MouseEvent evt) {
        position = productTable.getSelectedRow();
        productTable.setRowSelectionInterval(position, position);
    }

    private void restartButtonActionPerformed(ActionEvent evt){
        refreshProducts();
    }

    @Override
    public void onSearch(String text) {
        searchByName(text.trim().toLowerCase());
    }
    @Override
    public void onFilterByRole(String role) {
        
    }

    private void searchByName(String txt) {
        ArrayList<SanPham_DTO> Searchproducts = new ArrayList<>();
        for(SanPham_DTO sp:products){
            if(sp.getTen_SanPham().toLowerCase().contains(txt)){
                Searchproducts.add(sp);
            }
        }
        showProducts(Searchproducts);
    }


    private void addButtonActionPerformed(ActionEvent evt) {
        productForm = new ProductForm(this,null);
    }

    protected void editButtonActionPerformed(int row) {
        if (row >= 0 && row < products.size()) {
            SanPham_DTO product = products.get(position);
            productForm = new ProductForm(this, product);

            productForm.nameTextField.setText(product.getTen_SanPham());
            productForm.categoryComboBox.setSelectedItem(product.getDanhMuc());
            productForm.categoryComboBox.setEnabled(false);
            productForm.priceTextField.setText(String.valueOf(product.getGia_SanPham()));

            if(product instanceof Sach_DTO){
                Sach_DTO sach = (Sach_DTO)product;
                productForm.sachForm.authorTextField.setText(sach.getTenTacGia());
                productForm.sachForm.genreTextField.setText(sach.getTheLoai());
                productForm.sachForm.publisherTextField.setText(sach.getNhaXuatBan());
                productForm.sachForm.publicationYearTextField.setText(String.valueOf(sach.getNamXuatBan()));
            }
            else if(product instanceof Vo_DTO){
                Vo_DTO vo = (Vo_DTO)product;
                productForm.voForm.typeTextField.setText(vo.getLoai_Vo());
                productForm.voForm.materialTextField.setText(vo.getChatLieu());
                productForm.voForm.publisherTextField.setText(vo.getNhaSanXuat());
                productForm.voForm.pagenumberTextField.setText(String.valueOf(vo.getSoTrang()));
            }
            else if(product instanceof But_DTO){
                But_DTO but = (But_DTO)product;
                productForm.butForm.colorTextField.setText(but.getMau());
                productForm.butForm.typeTextField.setText(but.getLoai_But());
                productForm.butForm.publisherTextField.setText(but.getHang());
            }
        }
    }

    protected void removeButtonActionPerformed(int row) {
        if (row >= 0 && row < products.size()) {
            SanPham_DTO product = products.get(position);
            int choice = JOptionPane.showOptionDialog(this, 
                                        "Bạn muốn xóa sản phẩm '"+product.getTen_SanPham()+"' ?", 
                                        "Xóa sản phẩm", 
                                        JOptionPane.YES_NO_OPTION, 
                                        JOptionPane.INFORMATION_MESSAGE, 
                                        null, 
                                        new Object[] {"OK","Cancel"}, 
                                        "Cancel");
            if (choice == 0){
                for(ChiTietPhieuNhap_DTO ct:pnBLL.getAllChiTiet()){
                    if(ct.getThongtinSP().getID_SanPham().equals(product.getID_SanPham())){
                        pnBLL.removeCTPhieuNhapBySP(ct.getMaPN());
                        break;
                    }
                }
                for(OrderDetail_DTO detail:orderBLL.getAllDetail()){
                    if(detail.getMasp().equals(product.getID_SanPham())){
                        orderBLL.removeDetailBySP(product.getID_SanPham());
                        break;
                    }
                }
                if(productBLL.removeSP(product).contains("thành công")){
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    if(deleteProductImageById(product.getID_SanPham())){
                        JOptionPane.showMessageDialog(this, "Xóa ảnh thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(this, "Xóa ảnh thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                    refreshProducts();
                }else{
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    protected void detailButtonActionPerformed(int row) {
        if (row >= 0 && row < products.size()) {
            if(CurrentSachDescription != null)
                CurrentSachDescription.dispose();
            else if(CurrentVoDescription != null)
                CurrentVoDescription.dispose();
            else if(CurrentButDescription != null)
                CurrentButDescription.dispose();
            SanPham_DTO sp = products.get(position);
            if (sp.getID_SanPham().contains("S")){
                for(Sach_DTO description : SachDescriptionList){
                    if(description.getTen_SanPham().equals(sp.getTen_SanPham())){
                        CurrentSachDescription = new SachDescription(description);
                    }
                }
            }
            else if(sp.getID_SanPham().contains("V")){
                for(Vo_DTO description : VoDescriptionList){
                    if(description.getTen_SanPham().equals(sp.getTen_SanPham())){
                        CurrentVoDescription = new VoDescription(description);
                    }
                }
            }
            else{
                for(But_DTO description : ButDescriptionList){
                    if(description.getTen_SanPham().equals(sp.getTen_SanPham())){
                        CurrentButDescription = new ButDescription(description);
                    }
                }
            }
        }
    }

    private void firstButtonActionPerformed(ActionEvent evt) {
        position = 0;
        productTable.setRowSelectionInterval(position, position);
        moveScrollBar();
    }

    private void previousButtonActionPerformed(ActionEvent evt) {
        position--;
        if (position < 0)
            position = 0;
        productTable.setRowSelectionInterval(position, position);
        moveScrollBar();
    }

    private void nextButtonActionPerformed(ActionEvent evt) {
        position++;
        if (position > products.size() - 1)
            position = products.size() - 1;
        productTable.setRowSelectionInterval(position, position);
        moveScrollBar();
    }

    private void lastButtonActionPerformed(ActionEvent evt) {
        position = products.size() - 1;
        productTable.setRowSelectionInterval(position, position);
        moveScrollBar();
    }

    private void moveScrollBar(){
        SwingUtilities.invokeLater(()->{
            productTable.scrollRectToVisible(productTable.getCellRect(position,0,true));
        });
    }

    public void refreshProducts() {
        position = -1;
        FilterComboBox.setSelectedItem("Tất cả");
        getSPandDetails("Tất cả");
        showProducts(products);
    }

    protected boolean deleteProductImageById(String id) {
        String folderPath = "GUI/user/ProductImage/";
        File imageFile = new File(folderPath + id + ".png");
        boolean result = false;

        if (imageFile.exists()) {
            boolean deleted = imageFile.delete();
            if (deleted) {
                result = true;
            }
        }
        return result;
    }

    protected void addToImportList(int row) {
        if (row >= 0 && row < products.size()) {
            SanPham_DTO sp = products.get(row);
            new importQuantityFrame(this,sp);
        }
    }
}