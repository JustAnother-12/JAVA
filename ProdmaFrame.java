
// import com.mycompany.prodma.DB;
// import com.mycompany.prodma.Product;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class ProdmaFrame extends JFrame {

    private JButton Button_add;
    private JButton Button_browse;
    private JButton Button_edit;
    private JButton Button_first;
    private JButton Button_last;
    private JButton Button_next;
    private JButton Button_previous;
    private JButton Button_remove;
    private JButton Button_search;
    private JComboBox<String> jComboBox_cate;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel_image;
    private JPanel jPanel_container;
    private JScrollPane jScrollPane1;
    private JSpinner jSpinner_id;
    private JTable jTable1;
    private JTextField jTextField_Quantity;
    private JTextField jTextField_imgPath;
    private JTextField jTextField_name;
    private JTextField jTextField_price;

    Border panel_border = BorderFactory.createMatteBorder(1,1,1,1, Color.darkGray);
    Border textField_border = BorderFactory.createMatteBorder(0,0,2,0, Color.darkGray);
    ArrayList<Product> productArray = new ArrayList<>();
    int position =0;
    
    public ProdmaFrame() {
        initComponents();
        jPanel_container.setBorder(panel_border);
        jTextField_name.setBorder(textField_border);    
        jTextField_Quantity.setBorder(textField_border);
        jTextField_price.setBorder(textField_border);
        jTable1.setRowHeight(35);
        showProduct();
    }

    // đọc csdl
    public ArrayList<Product> getProductList()
    {
        ArrayList<Product> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM `products`";
        Statement st;
        ResultSet rs;  
        try {
            st = DB.getConnection().createStatement();
            rs = st.executeQuery(selectQuery);
            Product product;
            while(rs.next()){
                product = new Product(rs.getInt("id"),rs.getString("name"),rs.getString("category"),rs.getInt("quantity"),rs.getDouble("price"),rs.getString("image"));
                list.add(product);
            }    
        } catch (SQLException ex) {
            Logger.getLogger(ProdmaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        productArray = list;
        return list;
    }

    //show product
    public void showProduct()
    {
        ArrayList<Product> productlist = getProductList();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        Object[] row = new Object[6];
        
        for(int i=0; i< productlist.size();i++)
        {
            row[0]=productlist.get(i).getId();
            row[1]=productlist.get(i).getName();
            row[2]=productlist.get(i).getCategory();
            row[3]=productlist.get(i).getQuantity();
            row[4]=productlist.get(i).getPrice();
            row[5]=productlist.get(i).getImage();
            model.addRow(row);
        }
    }

    //display product data by index
    public void showProductindex(int index)
    {
        jSpinner_id.setValue(productArray.get(index).getId());
        jTextField_name.setText(productArray.get(index).getName());
        jComboBox_cate.setSelectedItem(productArray.get(index).getCategory());
        jTextField_Quantity.setText(productArray.get(index).getQuantity().toString());
        jTextField_price.setText(productArray.get(index).getPrice().toString());
        jTextField_imgPath.setText(productArray.get(index).getImage());
        displayImage(productArray.get(index).getImage(),jLabel_image);
    }

    public boolean checkEmptyFields()
    {
        String name = jTextField_name.getText().trim();
        String quantity = jTextField_Quantity.getText().trim();
        String price = jTextField_price.getText().trim();
        String img = jTextField_imgPath.getText().trim();
        if(name.equals("")||quantity.equals("")||price.equals("")||img.equals(""))
        {
            return false;
        }
        else{
            return true;
        }
    }

    // create a function to display image into jlabel
    public void displayImage(String imgPath, JLabel label)
    {
        ImageIcon imgIco = new ImageIcon(imgPath);
        Image img = imgIco.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(img));
    }


    public void clearfield()
    {
        jSpinner_id.setValue(0);
        jTextField_name.setText("");
        jComboBox_cate.setSelectedIndex(0);
        jTextField_Quantity.setText("");
        jTextField_price.setText("");
        jTextField_imgPath.setText("");
        jLabel_image.setIcon(null);
    }


    private void initComponents() {

        jPanel_container = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSpinner_id = new javax.swing.JSpinner();
        jTextField_name = new javax.swing.JTextField();
        jComboBox_cate = new javax.swing.JComboBox<>();
        jTextField_Quantity = new javax.swing.JTextField();
        jTextField_price = new javax.swing.JTextField();
        jLabel_image = new javax.swing.JLabel();
        Button_add = new javax.swing.JButton();
        Button_edit = new javax.swing.JButton();
        Button_remove = new javax.swing.JButton();
        Button_search = new javax.swing.JButton();
        Button_browse = new javax.swing.JButton();
        jTextField_imgPath = new javax.swing.JTextField();
        Button_first = new javax.swing.JButton();
        Button_previous = new javax.swing.JButton();
        Button_next = new javax.swing.JButton();
        Button_last = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);

        jPanel_container.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_container.setPreferredSize(new java.awt.Dimension(962, 600));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Category", "Quantity", "Price", "Image"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(51, 51, 51));
        jTable1.setShowGrid(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 20));
        jLabel1.setText("ID:");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 20));
        jLabel2.setText("Name:");

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 20));
        jLabel3.setText("Category:");

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 20));
        jLabel4.setText("Quantity:");

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 20));
        jLabel5.setText("Price:");

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 20));
        jLabel6.setText("Image:");

        jSpinner_id.setFont(new java.awt.Font("Tahoma", 0, 20));

        jTextField_name.setFont(new java.awt.Font("Tahoma", 0, 20));

        jTextField_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // jTextField_nameActionPerformed(evt);
            }
        });

        jComboBox_cate.setBackground(new java.awt.Color(204, 204, 204));
        jComboBox_cate.setFont(new java.awt.Font("Tahoma", 0, 20));
        jComboBox_cate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_cate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jComboBox_cate.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jComboBox_cate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // jComboBox_cateActionPerformed(evt);
            }
        });

        jTextField_Quantity.setFont(new java.awt.Font("Tahoma", 0, 20));
        jTextField_Quantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // jTextField_QuantityActionPerformed(evt);
            }
        });
        jTextField_Quantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_QuantityKeyTyped(evt);
            }
        });

        jTextField_price.setFont(new java.awt.Font("Tahoma", 0, 20));

        jLabel_image.setBackground(new java.awt.Color(102, 102, 102));
        jLabel_image.setOpaque(true);

        Button_add.setBackground(new java.awt.Color(51, 51, 51));
        Button_add.setFont(new java.awt.Font("Tahoma", 0, 14));
        Button_add.setForeground(new java.awt.Color(255, 255, 255));
        Button_add.setText("Add");
        Button_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_addActionPerformed(evt);
            }
        });

        Button_edit.setBackground(new java.awt.Color(0, 0, 0));
        Button_edit.setFont(new java.awt.Font("Tahoma", 0, 14));
        Button_edit.setForeground(new java.awt.Color(255, 255, 255));
        Button_edit.setText("Edit");
        Button_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_editActionPerformed(evt);
            }
        });

        Button_remove.setBackground(new java.awt.Color(0, 0, 0));
        Button_remove.setFont(new java.awt.Font("Tahoma", 0, 14));
        Button_remove.setForeground(new java.awt.Color(255, 255, 255));
        Button_remove.setText("Remove");
        Button_remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_removeActionPerformed(evt);
            }
        });

        Button_search.setBackground(new java.awt.Color(255, 102, 0));
        Button_search.setFont(new java.awt.Font("Tahoma", 0, 14));
        Button_search.setForeground(new java.awt.Color(255, 255, 255));
        Button_search.setText("Search");
        Button_search.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Button_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_searchActionPerformed(evt);
            }
        });

        Button_browse.setBackground(new java.awt.Color(255, 102, 0));
        Button_browse.setFont(new java.awt.Font("Tahoma", 0, 14));
        Button_browse.setForeground(new java.awt.Color(255, 255, 255));
        Button_browse.setText("Browse");
        Button_browse.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Button_browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_browseActionPerformed(evt);
            }
        });

        jTextField_imgPath.setFont(new java.awt.Font("Tahoma", 0, 10));
        jTextField_imgPath.setEnabled(false);

        Button_first.setBackground(new java.awt.Color(204, 204, 204));
        Button_first.setFont(new java.awt.Font("Tahoma", 0, 14));
        Button_first.setForeground(new java.awt.Color(255, 255, 255));
        Button_first.setText("<<");
        Button_first.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_firstActionPerformed(evt);
            }
        });

        Button_previous.setBackground(new java.awt.Color(204, 204, 204));
        Button_previous.setFont(new java.awt.Font("Tahoma", 0, 14));
        Button_previous.setForeground(new java.awt.Color(255, 255, 255));
        Button_previous.setText("<");
        Button_previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_previousActionPerformed(evt);
            }
        });

        Button_next.setBackground(new java.awt.Color(204, 204, 204));
        Button_next.setFont(new java.awt.Font("Tahoma", 0, 14));
        Button_next.setForeground(new java.awt.Color(255, 255, 255));
        Button_next.setText(">");
        Button_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_nextActionPerformed(evt);
            }
        });

        Button_last.setBackground(new java.awt.Color(204, 204, 204));
        Button_last.setFont(new java.awt.Font("Tahoma", 0, 14));
        Button_last.setForeground(new java.awt.Color(255, 255, 255));
        Button_last.setText(">>");
        Button_last.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_lastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_containerLayout = new javax.swing.GroupLayout(jPanel_container);
        jPanel_container.setLayout(jPanel_containerLayout);
        jPanel_containerLayout.setHorizontalGroup(
            jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_containerLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_containerLayout.createSequentialGroup()
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_name)
                            .addGroup(jPanel_containerLayout.createSequentialGroup()
                                .addComponent(jSpinner_id, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Button_search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel_containerLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel_image, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Button_browse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField_imgPath)))
                            .addComponent(jTextField_price, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel_containerLayout.createSequentialGroup()
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addGroup(jPanel_containerLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(Button_add, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_containerLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(Button_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(Button_remove, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel_containerLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_Quantity)
                                    .addComponent(jComboBox_cate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_containerLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_containerLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Button_first, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Button_previous, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Button_next, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Button_last, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51))))
        );
        jPanel_containerLayout.setVerticalGroup(
            jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_containerLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel_containerLayout.createSequentialGroup()
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jSpinner_id, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Button_search, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox_cate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField_Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_containerLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel_image, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)))
                            .addGroup(jPanel_containerLayout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(Button_browse, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField_imgPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_first, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Button_previous, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Button_next, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Button_last, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Button_add, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Button_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Button_remove, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(57, 57, 57))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

    }// </editor-fold>//GEN-END:initComponents

    private void Button_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_addActionPerformed
        // TODO add your handling code here:
        if(checkEmptyFields())
        {
        String name = jTextField_name.getText();
        String Cate = jComboBox_cate.getSelectedItem().toString();
        Integer Quanti = Integer.valueOf(jTextField_Quantity.getText());
        Double Price = Double.valueOf(jTextField_price.getText());
        String img = jTextField_imgPath.getText();
        
        String insert = "INSERT INTO `products`(`name`,`category`,`quantity`,`price`,`image`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement ps =DB.getConnection().prepareStatement(insert);
            ps.setString(1, name);
            ps.setString(2, Cate);
            ps.setInt(3, Quanti);
            ps.setDouble(4, Price);
            ps.setString(5, img);
            if(ps.executeUpdate()>0){
                System.out.println("Item Added");
                showProduct();
            }else{
                System.out.println("Error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdmaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        else
        {
            System.out.println("More than one field are empty");
        }
    }//GEN-LAST:event_Button_addActionPerformed

    private void Button_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_editActionPerformed
        Integer id = Integer.valueOf(jSpinner_id.getValue().toString());
        String name = jTextField_name.getText();
        String Cate = jComboBox_cate.getSelectedItem().toString();
        Integer Quanti = Integer.valueOf(jTextField_Quantity.getText());
        Double Price = Double.valueOf(jTextField_price.getText());
        String img = jTextField_imgPath.getText();
        
        String update = "UPDATE `products` SET `name`=?,`category`=?,`quantity`=?,`price`=?,`image`=? WHERE `id`=?";
        try {
            PreparedStatement ps =DB.getConnection().prepareStatement(update);
            ps.setString(1, name);
            ps.setString(2, Cate);
            ps.setInt(3, Quanti);
            ps.setDouble(4, Price);
            ps.setString(5, img);
            ps.setInt(6, id);
            if(ps.executeUpdate()>0){
                System.out.println("Item Updated");
            }else{
                System.out.println("Error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdmaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        showProduct();
    }//GEN-LAST:event_Button_editActionPerformed

    private void Button_removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_removeActionPerformed
        // TODO add your handling code here:
        
        Integer id = Integer.valueOf(jSpinner_id.getValue().toString());
        
        String delete = "DELETE FROM `products` WHERE `id`=?";
        try {
            PreparedStatement ps =DB.getConnection().prepareStatement(delete);
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                System.out.println("Item Deleted");
                clearfield();
            }else{
                System.out.println("Error");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdmaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        showProduct();
    }//GEN-LAST:event_Button_removeActionPerformed

    private void Button_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_searchActionPerformed
        // TODO add your handling code here:
        //search by id
        Integer id = Integer.valueOf(jSpinner_id.getValue().toString());
        String select = "SELECT * FROM `products` WHERE `id`="+id;
        try {
            Statement st = DB.getConnection().createStatement();
            ResultSet rs = st.executeQuery(select);
            if(rs.next())
            {
                jTextField_name.setText(rs.getString("name"));
                jComboBox_cate.setSelectedItem(rs.getString("category"));
                jTextField_Quantity.setText(rs.getString("quantity"));
                jTextField_price.setText(String.valueOf(rs.getDouble("price")));
                jTextField_imgPath.setText(rs.getString("image"));
                displayImage(rs.getString("image"),jLabel_image);
            }
            else
            {
                System.out.println("No product");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdmaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_Button_searchActionPerformed

    private void Button_browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_browseActionPerformed
        // TODO add your handling code here:
        JFileChooser filechooser = new JFileChooser();
        filechooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*images",".png","jpg","jpeg");
        filechooser.addChoosableFileFilter(filter);
        
        if(filechooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File selectedImage = filechooser.getSelectedFile();
            String image = selectedImage.getAbsolutePath();
            displayImage(image, jLabel_image);
            jTextField_imgPath.setText(image);
             System.out.println(image);
        }
        else
        {
            System.out.println("no selected image");
        }
    }//GEN-LAST:event_Button_browseActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // display the selected product info
        int index = jTable1.getSelectedRow();
        showProductindex(index);
        position = index;
    }//GEN-LAST:event_jTable1MouseClicked

    private void Button_firstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_firstActionPerformed
        // TODO add your handling code here:
        position = 0;
        showProductindex(position);
        jTable1.setRowSelectionInterval(position, position);
    }//GEN-LAST:event_Button_firstActionPerformed

    private void Button_previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_previousActionPerformed
        // TODO add your handling code here:
        position --;
        if(position < 0)
        {
            position = 0;
        }
        showProductindex(position);
        jTable1.setRowSelectionInterval(position, position);
    }//GEN-LAST:event_Button_previousActionPerformed

    private void Button_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_nextActionPerformed
        // TODO add your handling code here:
        position ++;
        if(position > productArray.size()-1)
        {
            position = productArray.size()-1;
        }
        showProductindex(position);
        jTable1.setRowSelectionInterval(position, position);
    }//GEN-LAST:event_Button_nextActionPerformed

    private void Button_lastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button_lastActionPerformed
        // TODO add your handling code here:
        position = productArray.size()-1;
        showProductindex(position);
        jTable1.setRowSelectionInterval(position, position);
    }//GEN-LAST:event_Button_lastActionPerformed

    private void jTextField_QuantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_QuantityKeyTyped
        // TODO add your handling code here:
        if(!Character.isDigit(evt.getKeyChar()))
        {
            evt.consume();
        }
    }

    public static void main(String args[]){
        new ProdmaFrame();
    }

}


