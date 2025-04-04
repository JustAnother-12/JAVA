package user;
import javax.swing.*;
import java.awt.*;

public class ProductDescription extends JFrame {
    private JLabel soLuongLabel;
    private JLabel xuatXuLabel;
    private JLabel trongLuongLabel;
    private JLabel kichThuocManHinhLabel;
    private JLabel dungLuongLabel;
    private JLabel ramLabel;
    private JLabel thuongHieuLabel;
    private JLabel baoHanhLabel;

    public ProductDescription(Model_Product_Description data) {
        initComponents(data);
    }

    private void initComponents(Model_Product_Description data) {
        // Set up the JFrame
        setTitle(data.getname());
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(400, 500));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create a main panel to hold all components
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title label for the product description
        JLabel titleLabel = new JLabel("Chi tiết Sản phẩm");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.decode("#00B4DB"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Initialize labels for each field
        soLuongLabel = new JLabel("Tồn kho: " + data.getSoLuong());
        xuatXuLabel = new JLabel("Xuất xứ: " + data.getXuatXu());
        trongLuongLabel = new JLabel("Trọng luọng: " + data.getTrongLuong() + "g");
        kichThuocManHinhLabel = new JLabel("Kích thước màn hình: " + data.getKichThuocManHinh() + " inch");
        dungLuongLabel = new JLabel("Dung lượng: " + data.getDungLuong()+" GB");
        ramLabel = new JLabel("RAM: " + data.getRAM() + " GB");
        thuongHieuLabel = new JLabel("Thương hiệu: " + (data.getThuongHieu()).split(" ")[0]);
        baoHanhLabel = new JLabel("Bảo hành: " + data.getBaoHanh() + " tháng");

        // Style the labels
        JLabel[] labels = {soLuongLabel, xuatXuLabel, trongLuongLabel, kichThuocManHinhLabel, 
                           dungLuongLabel, ramLabel, thuongHieuLabel, baoHanhLabel};
        for (JLabel label : labels) {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Add components to the main panel with spacing
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        for (JLabel label : labels) {
            mainPanel.add(label);
            mainPanel.add(Box.createVerticalStrut(10));
        }
        mainPanel.add(Box.createVerticalGlue()); // Push content upwards

        // Add the main panel to the JFrame
        add(mainPanel, BorderLayout.CENTER);

        // Pack and display the frame
        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    // // Main method for testing
    // public static void main(String[] args) {
    //     // Create a sample Model_Product_Description object
    //     Model_Product_Description data = new Model_Product_Description(
    //         10,           // soLuong (Quantity)
    //         "China",      // XuatXu (Origin)
    //         "150g",       // TrongLuong (Weight)
    //         "6.5 inches", // KichThuocManHinh (Screen Size)
    //         "128GB",      // DungLuong (Storage)
    //         "8GB",        // RAM
    //         "Samsung",    // ThuongHieu (Brand)
    //         12            // BaoHanh (Warranty in months)
    //     );

    //     // Create and display the ProductDescription frame
    //     SwingUtilities.invokeLater(() -> new ProductDescription(data));
    // }
}
