package GUI.Admin.product;

import javax.swing.*;
import javax.swing.border.Border;

import DTO.Sach_DTO;
import DTO.SanPham_DTO;

import java.awt.*;
import java.awt.event.*;

public class SachForm extends JPanel{
    public JTextField authorTextField;
    public JTextField genreTextField;
    public JTextField publisherTextField;
    public JTextField publicationYearTextField;

    private Border inputFieldBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.darkGray);
    private ProductForm parent;
    private SanPham_DTO sp=null;


    public SachForm(ProductForm parent, SanPham_DTO sp){
        this.parent = parent;
        this.sp = sp;
    }

    // private boolean checkAdditionalFields() {
    //     String author = authorTextField.getText().trim();
    //     String genre = genreTextField.getText().trim();
    //     String publisher = publisherTextField.getText().trim();
    //     String publicationYear = publicationYearTextField.getText().trim();
    //     return !(author.isEmpty() || genre.isEmpty() || publisher.isEmpty() || publicationYear.isEmpty());
    // }

    private void saveProduct() {
        String name = parent.nameTextField.getText().trim();
        int quantity = 0;
        double price = Double.parseDouble(parent.priceTextField.getText().trim());

        String author = authorTextField.getText().trim();
        String genre = genreTextField.getText().trim();
        String publisher = publisherTextField.getText().trim();
        int publicationYear = Integer.parseInt(publicationYearTextField.getText().trim());

        SanPham_DTO product = new Sach_DTO("",name,price,quantity,author, genre, publisher, publicationYear);
        if(parent.productBLL.addSP(product).contains("thành công")){
            JOptionPane.showMessageDialog(this, "Thêm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            parent.saveImageToProductFolder(parent.productBLL.getLatestSACHID());
            parent.parentFrame.refreshProducts();
        }else{
            JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void editProduct(Sach_DTO sach){

        sach.setTen_SanPham(parent.nameTextField.getText().trim());
        sach.setGia_SanPham(Double.parseDouble(parent.priceTextField.getText().trim()));

        sach.setTenTacGia(authorTextField.getText().trim());
        sach.setTheLoai(genreTextField.getText().trim());
        sach.setNhaXuatBan(publisherTextField.getText().trim());
        sach.setNamXuatBan(Integer.parseInt(publicationYearTextField.getText().trim()));

        if(parent.productBLL.updateSP(sach).contains("thành công")){
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            parent.saveImageToProductFolder(sach.getID_SanPham());
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

        // Author field
        JPanel authorPanel = new JPanel();
        authorPanel.setLayout(new BoxLayout(authorPanel, BoxLayout.X_AXIS));
        authorPanel.setBackground(new Color(255, 255, 255));
        JLabel authorLabel = new JLabel("Tên tác giả:");
        authorLabel.setFont(new Font("Segoe UI", 0, 20));
        authorTextField = new JTextField();
        authorTextField.setFont(new Font("Segoe UI", 0, 20));
        authorTextField.setBorder(inputFieldBorder);
        authorPanel.add(authorLabel);
        authorPanel.add(Box.createHorizontalStrut(10));
        authorPanel.add(authorTextField);
        formPanel.add(authorPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Genre field
        JPanel genrePanel = new JPanel();
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.X_AXIS));
        genrePanel.setBackground(new Color(255, 255, 255));
        JLabel genreLabel = new JLabel("Thể loại:");
        genreLabel.setFont(new Font("Segoe UI", 0, 20));
        genreTextField = new JTextField();
        genreTextField.setFont(new Font("Segoe UI", 0, 20));
        genreTextField.setBorder(inputFieldBorder);
        genrePanel.add(genreLabel);
        genrePanel.add(Box.createHorizontalStrut(10));
        genrePanel.add(genreTextField);
        formPanel.add(genrePanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Publisher field
        JPanel publisherPanel = new JPanel();
        publisherPanel.setLayout(new BoxLayout(publisherPanel, BoxLayout.X_AXIS));
        publisherPanel.setBackground(new Color(255, 255, 255));
        JLabel publisherLabel = new JLabel("Nhà xuất bản:");
        publisherLabel.setFont(new Font("Segoe UI", 0, 20));
        publisherTextField = new JTextField();
        publisherTextField.setFont(new Font("Segoe UI", 0, 20));
        publisherTextField.setBorder(inputFieldBorder);
        publisherPanel.add(publisherLabel);
        publisherPanel.add(Box.createHorizontalStrut(10));
        publisherPanel.add(publisherTextField);
        formPanel.add(publisherPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Publication Year field
        JPanel publicationYearPanel = new JPanel();
        publicationYearPanel.setLayout(new BoxLayout(publicationYearPanel, BoxLayout.X_AXIS));
        publicationYearPanel.setBackground(new Color(255, 255, 255));
        JLabel publicationYearLabel = new JLabel("Năm xuất bản:");
        publicationYearLabel.setFont(new Font("Segoe UI", 0, 20));
        publicationYearTextField = new JTextField();
        publicationYearTextField.setFont(new Font("Segoe UI", 0, 20));
        publicationYearTextField.setBorder(inputFieldBorder);
        publicationYearTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                if (!Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                }
            }
        });
        publicationYearPanel.add(publicationYearLabel);
        publicationYearPanel.add(Box.createHorizontalStrut(10));
        publicationYearPanel.add(publicationYearTextField);
        formPanel.add(publicationYearPanel);
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
            if (parent.productBLL.checkSACHAdditionalFields(this)) {
                if(sp==null){
                    saveProduct();
                }else{
                    Sach_DTO sach = (Sach_DTO)sp;
                    editProduct(sach);
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
