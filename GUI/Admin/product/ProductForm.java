package GUI.Admin.product;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import BLL.SanPham_BLL;
import DTO.SanPham_DTO;

public class ProductForm extends JFrame {

    protected CardLayout cardLayout;
    protected JPanel cardPanel;
    protected SanPham_BLL productBLL = new SanPham_BLL();
    protected ProdmaFrame parentFrame;

    // General info components
    public JTextField nameTextField;
    public JComboBox<String> categoryComboBox;
    public JTextField priceTextField;
    public JTextField imagePathTextField;
    private JLabel productImageLabel;
    private Border inputFieldBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.darkGray);


    protected SachForm sachForm;
    protected VoForm voForm;
    protected ButForm butForm;

    protected File selectedImage;

    public ProductForm(ProdmaFrame parent, SanPham_DTO sp) {
        this.parentFrame = parent;
        if(sp == null){
            setTitle("Add product");
        }
        else{
            setTitle("Edit product");
        }
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        sachForm = new SachForm(this,sp);
        voForm = new VoForm(this,sp);
        butForm = new ButForm(this,sp);

        // Initialize CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(initComponents(), "GeneralForm");
        cardPanel.add(sachForm.createAdditionalInfoPanel(), "SachForm");
        cardPanel.add(voForm.createAdditionalInfoPanel(), "VoForm");
        cardPanel.add(butForm.createAdditionalInfoPanel(), "ButForm");

        getContentPane().add(cardPanel);
        setVisible(true);
    }

    private JPanel initComponents() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(new Color(255, 255, 255));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Name field
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.setBackground(new Color(255, 255, 255));
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", 0, 20));
        nameTextField = new JTextField();
        nameTextField.setFont(new Font("Segoe UI", 0, 20));
        nameTextField.setBorder(inputFieldBorder);
        namePanel.add(nameLabel);
        namePanel.add(Box.createHorizontalStrut(10));
        namePanel.add(nameTextField);
        formPanel.add(namePanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Category field
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.X_AXIS));
        categoryPanel.setBackground(new Color(255, 255, 255));
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", 0, 20));
        categoryComboBox = new JComboBox<>(new String[] { "Sách", "Vở", "Bút" });
        categoryComboBox.setBackground(Color.WHITE);
        categoryComboBox.setFont(new Font("Segoe UI", 0, 20));
        categoryComboBox.setBorder(BorderFactory.createEtchedBorder());
        categoryPanel.add(categoryLabel);
        categoryPanel.add(Box.createHorizontalStrut(10));
        categoryPanel.add(categoryComboBox);
        formPanel.add(categoryPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Price field
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.X_AXIS));
        pricePanel.setBackground(new Color(255, 255, 255));
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Segoe UI", 0, 20));
        priceTextField = new JTextField();
        priceTextField.setFont(new Font("Segoe UI", 0, 20));
        priceTextField.setBorder(inputFieldBorder);
        priceTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // chặn nếu không phải số
                }
            }
        });

        pricePanel.add(priceLabel);
        pricePanel.add(Box.createHorizontalStrut(10));
        pricePanel.add(priceTextField);
        formPanel.add(pricePanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Image field
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        imagePanel.setBackground(new Color(255, 255, 255));
        JLabel imageLabel = new JLabel("Image:");
        imageLabel.setFont(new Font("Segoe UI", 0, 20));

        productImageLabel = new JLabel();
        productImageLabel.setBackground(new Color(102, 102, 102));
        productImageLabel.setOpaque(true);
        productImageLabel.setPreferredSize(new Dimension(140, 180));
        displayPlaceHolder(productImageLabel);

        JButton browseButton = new JButton("Browse");
        browseButton.setBackground(new Color(255, 102, 0));
        browseButton.setFont(new Font("Segoe UI", 0, 14));
        browseButton.setForeground(new Color(255, 255, 255));
        browseButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser filechooser = new JFileChooser();
                filechooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "png", "jpg", "jpeg");
                filechooser.addChoosableFileFilter(filter);
                if (filechooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    selectedImage = filechooser.getSelectedFile();
                    String imagePath = selectedImage.getAbsolutePath();
                    displayImage(imagePath, productImageLabel);
                    imagePathTextField.setText(imagePath);
                }
            }
        });

        imagePanel.add(imageLabel);
        imagePanel.add(Box.createHorizontalStrut(10));
        imagePanel.add(productImageLabel);
        imagePanel.add(Box.createHorizontalStrut(10));
        imagePanel.add(browseButton);
        formPanel.add(imagePanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Image path field
        JPanel imagePathPanel = new JPanel();
        imagePathPanel.setLayout(new BoxLayout(imagePathPanel, BoxLayout.X_AXIS));
        imagePathPanel.setBackground(new Color(255, 255, 255));
        imagePathTextField = new JTextField();
        imagePathTextField.setFont(new Font("Segoe UI", 0, 14));
        imagePathTextField.setForeground(Color.GRAY);
        imagePathTextField.setEnabled(false);
        imagePathPanel.add(imagePathTextField);
        formPanel.add(imagePathPanel);
        formPanel.add(Box.createVerticalStrut(10));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton continueButton = new JButton("Continue");
        continueButton.setBackground(new Color(51, 51, 51));
        continueButton.setFont(new Font("Segoe UI", 0, 14));
        continueButton.setForeground(new Color(255, 255, 255));
        continueButton.addActionListener(e -> {
            if (productBLL.checkEmptyFields(this)) {
                switch ((String)categoryComboBox.getSelectedItem()) {
                    case "Sách":
                        cardLayout.show(cardPanel, "SachForm");
                        break;
                    case "Vở":
                        cardLayout.show(cardPanel, "VoForm");
                        break;
                    case "Bút":
                        cardLayout.show(cardPanel, "ButForm");
                        break;
                    default:
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(204, 204, 204));
        cancelButton.setFont(new Font("Segoe UI", 0, 14));
        cancelButton.setForeground(new Color(255, 255, 255));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(continueButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        formPanel.add(buttonPanel);

        return formPanel;
    }


    private void displayImage(String imgPath, JLabel label) {
        int width = 140;
        int height = 180;
        ImageIcon imgIcon = new ImageIcon(imgPath);
        Image img = imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(img));
    }

    private void displayPlaceHolder(JLabel label){
        ImageIcon image = new ImageIcon("GUI/user/ProductImage/placeholder.png");
        Image og = image.getImage();
        int w = 140;
        int h = 180;
        int iw = image.getIconWidth();
        int ih = image.getIconHeight();
        double xscale = (double) w / iw;
        double yscale = (double) h / ih;
        double scale = Math.max(xscale, yscale);
        int width = (int)(scale*iw);
        int height = (int)(scale*ih);
        Image scaledImage = og.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledImage));
    }

    protected void saveImageToProductFolder(String id){
        if(selectedImage != null){
            try {
                String folderPath = "GUI/user/ProductImage/";
                File destinationDir = new File(folderPath);
                if (!destinationDir.exists()) {
                    return;
                }
        
                // Tạo file đích với tên ID.png
                File destFile = new File(destinationDir, id + ".png");
        
                // Đọc ảnh từ file gốc
                BufferedImage bufferedImage = ImageIO.read(selectedImage);
        
                // Ghi ảnh mới với đuôi PNG
                ImageIO.write(bufferedImage, "png", destFile);
        
                // Cập nhật đường dẫn
                imagePathTextField.setText(destFile.getAbsolutePath());
        
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu ảnh: " + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}