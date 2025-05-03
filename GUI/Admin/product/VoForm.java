package GUI.Admin.product;

import javax.swing.*;
import javax.swing.border.Border;

import DTO.SanPham_DTO;
import DTO.Vo_DTO;

import java.awt.*;
import java.awt.event.*;

public class VoForm extends JPanel{
    public JTextField typeTextField;
    public JTextField materialTextField;
    public JTextField publisherTextField;
    public JTextField pagenumberTextField;

    private Border inputFieldBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.darkGray);
    private ProductForm parent;
    private SanPham_DTO sp=null;

    public VoForm(ProductForm parent, SanPham_DTO sp){
        this.parent = parent;
        this.sp = sp;
    }

    private void saveProduct() {
        String name = parent.nameTextField.getText().trim();
        int quantity = 0;
        double price = Double.parseDouble(parent.priceTextField.getText().trim());

        String type = typeTextField.getText().trim();
        String material = materialTextField.getText().trim();
        String publisher = publisherTextField.getText().trim();
        int pageNumber = Integer.parseInt(pagenumberTextField.getText().trim());

        SanPham_DTO product = new Vo_DTO("", name, price, quantity, type,  publisher, material, pageNumber);
        if(parent.productBLL.addSP(product).contains("thành công")){
            JOptionPane.showMessageDialog(this, "Thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            parent.saveImageToProductFolder(parent.productBLL.getLatestVOID());
            parent.parentFrame.refreshProducts();
        }else{
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editProduct(Vo_DTO vo) {

        vo.setTen_SanPham(parent.nameTextField.getText().trim());
        vo.setGia_SanPham(Double.parseDouble(parent.priceTextField.getText().trim()));

        vo.setLoai_Vo(typeTextField.getText().trim());
        vo.setChatLieu(materialTextField.getText().trim());
        vo.setNhaSanXuat(publisherTextField.getText().trim());
        vo.setSoTrang(Integer.parseInt(pagenumberTextField.getText().trim()));

        if(parent.productBLL.updateSP(vo).contains("thành công")){
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            parent.saveImageToProductFolder(vo.getID_SanPham());
            parent.parentFrame.refreshProducts();
        }else{
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }

    // private boolean checkAdditionalFields() {
    //     String type = typeTextField.getText().trim();
    //     String material = materialTextField.getText().trim();
    //     String publisher = publisherTextField.getText().trim();
    //     String pageNumber = pagenumberTextField.getText().trim();
    //     return !(type.isEmpty() || material.isEmpty() || publisher.isEmpty() || pageNumber.isEmpty());
    // }

    public JPanel createAdditionalInfoPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // type field
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));
        typePanel.setBackground(new Color(255, 255, 255));
        JLabel typeLabel = new JLabel("Loại vở:");
        typeLabel.setFont(new Font("Segoe UI", 0, 20));
        typeTextField = new JTextField();
        typeTextField.setFont(new Font("Segoe UI", 0, 20));
        typeTextField.setBorder(inputFieldBorder);
        typePanel.add(typeLabel);
        typePanel.add(Box.createHorizontalStrut(10));
        typePanel.add(typeTextField);
        formPanel.add(typePanel);
        formPanel.add(Box.createVerticalStrut(10));

        // material field
        JPanel materialPanel = new JPanel();
        materialPanel.setLayout(new BoxLayout(materialPanel, BoxLayout.X_AXIS));
        materialPanel.setBackground(new Color(255, 255, 255));
        JLabel materialLabel = new JLabel("Chất liệu:");
        materialLabel.setFont(new Font("Segoe UI", 0, 20));
        materialTextField = new JTextField();
        materialTextField.setFont(new Font("Segoe UI", 0, 20));
        materialTextField.setBorder(inputFieldBorder);
        materialPanel.add(materialLabel);
        materialPanel.add(Box.createHorizontalStrut(10));
        materialPanel.add(materialTextField);
        formPanel.add(materialPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Publisher field
        JPanel publisherPanel = new JPanel();
        publisherPanel.setLayout(new BoxLayout(publisherPanel, BoxLayout.X_AXIS));
        publisherPanel.setBackground(new Color(255, 255, 255));
        JLabel publisherLabel = new JLabel("Nhà sản xuất:");
        publisherLabel.setFont(new Font("Segoe UI", 0, 20));
        publisherTextField = new JTextField();
        publisherTextField.setFont(new Font("Segoe UI", 0, 20));
        publisherTextField.setBorder(inputFieldBorder);
        publisherPanel.add(publisherLabel);
        publisherPanel.add(Box.createHorizontalStrut(10));
        publisherPanel.add(publisherTextField);
        formPanel.add(publisherPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Page number field
        JPanel pageNumberPanel = new JPanel();
        pageNumberPanel.setLayout(new BoxLayout(pageNumberPanel, BoxLayout.X_AXIS));
        pageNumberPanel.setBackground(new Color(255, 255, 255));
        JLabel pageNumberLabel = new JLabel("Số trang:");
        pageNumberLabel.setFont(new Font("Segoe UI", 0, 20));
        pagenumberTextField = new JTextField();
        pagenumberTextField.setFont(new Font("Segoe UI", 0, 20));
        pagenumberTextField.setBorder(inputFieldBorder);
        pagenumberTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                if (!Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                }
            }
        });
        pageNumberPanel.add(pageNumberLabel);
        pageNumberPanel.add(Box.createHorizontalStrut(10));
        pageNumberPanel.add(pagenumberTextField);
        formPanel.add(pageNumberPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBackground(new Color(51, 51, 51));
        confirmButton.setFont(new Font("Segoe UI", 0, 14));
        confirmButton.setForeground(new Color(255, 255, 255));
        confirmButton.addActionListener(e -> {
            if (parent.productBLL.checkVOAdditionalFields(this)) {
                
                if(sp==null){
                    saveProduct();
                }else{
                    Vo_DTO vo = (Vo_DTO)sp;
                    editProduct(vo);
                }
                parent.dispose();
                
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(102, 102, 102));
        backButton.setFont(new Font("Segoe UI", 0, 14));
        backButton.setForeground(new Color(255, 255, 255));
        backButton.addActionListener(e -> parent.cardLayout.show(parent.cardPanel, "GeneralForm"));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(204, 204, 204));
        cancelButton.setFont(new Font("Segoe UI", 0, 14));
        cancelButton.setForeground(new Color(255, 255, 255));
        cancelButton.addActionListener(e -> parent.dispose());
        buttonPanel.add(confirmButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);

        return formPanel;
    }
}
