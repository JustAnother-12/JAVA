package GUI.Admin.product;

import javax.swing.*;
import javax.swing.border.Border;

import DTO.But_DTO;
import DTO.SanPham_DTO;

import java.awt.*;

public class ButForm extends JPanel{
    public JTextField colorTextField;
    public JTextField typeTextField;
    public JTextField publisherTextField;

    private Border inputFieldBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.darkGray);
    private ProductForm parent;
    private SanPham_DTO sp=null;

    public ButForm(ProductForm parent, SanPham_DTO sp){
        this.parent = parent;
        this.sp = sp;
    }

    private void saveProduct() {
        String name = parent.nameTextField.getText().trim();
        int quantity = 0;
        double price = Double.parseDouble(parent.priceTextField.getText().trim());

        String type = typeTextField.getText().trim();
        String color = colorTextField.getText().trim();
        String publisher = publisherTextField.getText().trim();

        SanPham_DTO product = new But_DTO("", name, price, quantity, color, type, publisher);
        if(parent.productBLL.addSP(product).contains("thành công")){
            JOptionPane.showMessageDialog(this, "Thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            parent.saveImageToProductFolder(parent.productBLL.getLatestBUTID());
            parent.parentFrame.refreshProducts();
        }else{
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editProduct(But_DTO but) {
        but.setTen_SanPham(parent.nameTextField.getText().trim());
        but.setGia_SanPham(Double.parseDouble(parent.priceTextField.getText().trim()));

        but.setLoai_But(typeTextField.getText().trim());
        but.setMau(colorTextField.getText().trim());
        but.setHang(publisherTextField.getText().trim());

        if(parent.productBLL.updateSP(but).contains("thành công")){
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            parent.saveImageToProductFolder(but.getID_SanPham());
            parent.parentFrame.refreshProducts();
        }else{
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public JPanel createAdditionalInfoPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // type field
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));
        typePanel.setBackground(new Color(255, 255, 255));
        JLabel typeLabel = new JLabel("Loại bút:");
        typeLabel.setFont(new Font("Segoe UI", 0, 20));
        colorTextField = new JTextField();
        colorTextField.setFont(new Font("Segoe UI", 0, 20));
        colorTextField.setBorder(inputFieldBorder);
        typePanel.add(typeLabel);
        typePanel.add(Box.createHorizontalStrut(10));
        typePanel.add(colorTextField);
        formPanel.add(typePanel);
        formPanel.add(Box.createVerticalStrut(10));

        // color field
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
        colorPanel.setBackground(new Color(255, 255, 255));
        JLabel colorLabel = new JLabel("Màu:");
        colorLabel.setFont(new Font("Segoe UI", 0, 20));
        typeTextField = new JTextField();
        typeTextField.setFont(new Font("Segoe UI", 0, 20));
        typeTextField.setBorder(inputFieldBorder);
        colorPanel.add(colorLabel);
        colorPanel.add(Box.createHorizontalStrut(10));
        colorPanel.add(typeTextField);
        formPanel.add(colorPanel);
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

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBackground(new Color(51, 51, 51));
        confirmButton.setFont(new Font("Segoe UI", 0, 14));
        confirmButton.setForeground(new Color(255, 255, 255));
        confirmButton.addActionListener(e -> {
            if (parent.productBLL.checkBUTAdditionalFields(this)) {
            
                if(sp==null){
                    saveProduct();
                }else{
                    But_DTO but = (But_DTO)sp;
                    editProduct(but);
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

