package GUI.Admin.product;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import java.util.ArrayList;
import javax.swing.border.*;
import javax.swing.table.*;

import BLL.SanPham_BLL;
import DTO.But_DTO;
import DTO.Sach_DTO;
import DTO.SanPham_DTO;
import DTO.Vo_DTO;

public class ProdmaFrame extends JFrame {

    private JButton importQuantityButton;
    private JButton restartButton;
    private JButton addButton;
    private JButton editButton;
    private JButton firstButton;
    private JButton lastButton;
    private JButton nextButton;
    private JButton previousButton;
    private JButton removeButton;
    private JButton searchButton;
    private JPanel mainPanel;
    private JScrollPane productTableScrollPane;
    private JTable productTable;
    private JTextField searchBar;
    private ProductForm productForm;

    Border mainPanelBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.darkGray);
    ArrayList<SanPham_DTO> products = new ArrayList<>();
    int position = -1;

    private SanPham_BLL productBLL = new SanPham_BLL();

    public ProdmaFrame() {
        initComponents();
        products = productBLL.getAllSanPham();
        showProducts(products);
        System.out.println(productTable.getSelectedRow());
    }

    public void showProducts(ArrayList<SanPham_DTO> productList) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);
        Object[] row = new Object[6];
        for (int i = 0; i < productList.size(); i++) {
            row[0] = productList.get(i).getID_SanPham();
            row[1] = productList.get(i).getTen_SanPham();
            row[2] = productList.get(i).getDanhMuc();
            row[3] = productList.get(i).getSoLuong_SanPham();
            row[4] = productList.get(i).getGia_SanPham();
            row[5] = productList.get(i).getID_SanPham();
            model.addRow(row);
        }
    }

    // public void showProductIndex(int index) {
    //     searchBar.setText(products.get(index).getID_SanPham());
    // }

    private void initComponents() {
        mainPanel = new JPanel();
        productTableScrollPane = new JScrollPane();
        productTable = new JTable();
        searchBar = new JTextField();
        restartButton = new JButton();
        searchButton = new JButton();
        importQuantityButton = new JButton();
        addButton = new JButton();
        editButton = new JButton();
        removeButton = new JButton();
        firstButton = new JButton();
        previousButton = new JButton();
        nextButton = new JButton();
        lastButton = new JButton();


        productTable.setModel(new DefaultTableModel(
            new Object[][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String[] {
                "ID", "Name", "Category", "Quantity", "Price", "Image"
            }
        ));

        productTable.setRowHeight(35);
        productTable.setGridColor(new Color(51, 51, 51));
        productTable.setShowGrid(true);
        productTable.setDefaultEditor(Object.class, null);
        productTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                productTableMouseClicked(evt);
            }
        });


        productTableScrollPane.setViewportView(productTable);
        productTableScrollPane.setPreferredSize(new Dimension(531, 466));


        // toppanel components
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(255, 255, 255));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel searchLabel = new JLabel();
        searchLabel.setFont(new Font("Segoe UI", 0, 20));
        searchLabel.setText("Search:");
        topPanel.add(searchLabel);

        searchBar.setFont(new Font("Segoe UI", 0, 20));
        searchBar.setPreferredSize(new Dimension(300, 30));
        topPanel.add(searchBar);

        searchButton.setBackground(new Color(255, 102, 0));
        searchButton.setFont(new Font("Segoe UI", 0, 14));
        searchButton.setForeground(new Color(255, 255, 255));
        searchButton.setText("Search");
        searchButton.setPreferredSize(new Dimension(50,30));
        searchButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        topPanel.add(searchButton);


        // buttonPanel components
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        restartButton.setBackground(new Color(51, 51, 51));
        restartButton.setFont(new Font("Segoe UI", 0, 14));
        restartButton.setForeground(new Color(255, 255, 255));
        restartButton.setText("Refresh");
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                restartButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(restartButton);

        addButton.setBackground(new Color(51, 51, 51));
        addButton.setFont(new Font("Segoe UI", 0, 14));
        addButton.setForeground(new Color(255, 255, 255));
        addButton.setText("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(addButton);

        importQuantityButton.setBackground(new Color(0, 0, 0));
        importQuantityButton.setFont(new Font("Segoe UI", 0, 14));
        importQuantityButton.setForeground(new Color(255, 255, 255));
        importQuantityButton.setText("Nhập hàng");
        importQuantityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                importQuantityButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(importQuantityButton);

        editButton.setBackground(new Color(0, 0, 0));
        editButton.setFont(new Font("Segoe UI", 0, 14));
        editButton.setForeground(new Color(255, 255, 255));
        editButton.setText("Edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(editButton);

        removeButton.setBackground(new Color(0, 0, 0));
        removeButton.setFont(new Font("Segoe UI", 0, 14));
        removeButton.setForeground(new Color(255, 255, 255));
        removeButton.setText("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(removeButton);

        firstButton.setBackground(new Color(204, 204, 204));
        firstButton.setFont(new Font("Segoe UI", 0, 14));
        firstButton.setForeground(new Color(255, 255, 255));
        firstButton.setText("<<");
        firstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                firstButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(firstButton);

        previousButton.setBackground(new Color(204, 204, 204));
        previousButton.setFont(new Font("Segoe UI", 0, 14));
        previousButton.setForeground(new Color(255, 255, 255));
        previousButton.setText("<");
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(previousButton);

        nextButton.setBackground(new Color(204, 204, 204));
        nextButton.setFont(new Font("Segoe UI", 0, 14));
        nextButton.setForeground(new Color(255, 255, 255));
        nextButton.setText(">");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(nextButton);

        lastButton.setBackground(new Color(204, 204, 204));
        lastButton.setFont(new Font("Segoe UI", 0, 14));
        lastButton.setForeground(new Color(255, 255, 255));
        lastButton.setText(">>");
        lastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                lastButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(lastButton);


        mainPanel.setBorder(mainPanelBorder);
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setPreferredSize(new Dimension(962, 600));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(productTableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


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


        setSize(962, 600);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void productTableMouseClicked(MouseEvent evt) {
        System.out.println("test");
        position = productTable.getSelectedRow();
        productTable.setRowSelectionInterval(position, position);
        
        System.out.println(position);
    }

    private void restartButtonActionPerformed(ActionEvent evt){
        refreshProducts();
    }

    private void searchButtonActionPerformed(ActionEvent evt) {
        String query = searchBar.getText().trim().toLowerCase();
        ArrayList<SanPham_DTO> Searchproducts = new ArrayList<>();
        for(SanPham_DTO sp:products){
            if(sp.getTen_SanPham().toLowerCase().contains(query)){
                Searchproducts.add(sp);
            }
        }
        showProducts(Searchproducts);
    }

    private void importQuantityButtonActionPerformed(ActionEvent evt) {
        if(position>=0){
            new importQuantityFrame();
        }else{
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addButtonActionPerformed(ActionEvent evt) {
        productForm = new ProductForm(this,null);
    }

    private void editButtonActionPerformed(ActionEvent evt) {
        if(position>=0){
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
        else{
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeButtonActionPerformed(ActionEvent evt) {
        if (position>=0){
            int choice = JOptionPane.showOptionDialog(this, 
                                        "Bạn muốn xóa sản phẩm '"+products.get(position).getTen_SanPham()+"' ?", 
                                        "Xóa sản phẩm", 
                                        JOptionPane.YES_NO_OPTION, 
                                        JOptionPane.INFORMATION_MESSAGE, 
                                        null, 
                                        new Object[] {"OK","Cancel"}, 
                                        "Cancel");
            if (choice == 0){
                if(productBLL.removeSP(products.get(position)).contains("thành công")){
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    if(deleteProductImageById(products.get(position).getID_SanPham())){
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
        else{
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        products = productBLL.getAllSanPham();
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

    public static void main(String args[]) {
        new ProdmaFrame();
    }
}