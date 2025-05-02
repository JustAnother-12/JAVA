package GUI.Admin.product;

import javax.swing.*;

import DTO.ChiTietPhieuNhap_DTO;
import DTO.SanPham_DTO;
import utils.*;

import java.awt.*;
import java.awt.event.*;

public class importQuantityFrame extends JFrame{
    private JPanel formPanel;
    private JLabel formLabel;
    private JLabel quantityLabel;
    private JTextField quantityTextfield;

    private MyButton confirmButton;
    private MyButton cancelButton;


    private SanPham_DTO sp;
    private ProdmaFrame parent;

    public importQuantityFrame(ProdmaFrame parent, SanPham_DTO sp){
        this.parent = parent;
        this.sp = sp;
        initComponents();
    }

    private void initComponents(){
        formPanel = new JPanel();
        formLabel = new JLabel(sp.getTen_SanPham().toUpperCase());
        quantityLabel = new JLabel("Số lượng:");
        quantityTextfield = new JTextField();


        // providerLabel = new JLabel("Nhà cung cấp:");
        // ArrayList<NhaCungCap_DTO> danhSachNCC = nccBLL.getAllNCC();
        // String[] listTenNCC = new String[danhSachNCC.size()];
        // for (int i = 0; i < danhSachNCC.size(); i++) {
        //     listTenNCC[i] = danhSachNCC.get(i).getTenNCC();
        // }
        // providerComboBox = new JComboBox<>(listTenNCC);
        confirmButton = new MyButton("Xác nhận");
        cancelButton = new MyButton("Hủy"); 

        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new Dimension(400,300));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(Color.WHITE);

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(Color.WHITE);
        formLabel.setFont(new Font("Segoe UI", 0, 20));
        labelPanel.add(formLabel);

        formPanel.add(labelPanel);
        formPanel.add(Box.createVerticalStrut(20));


        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new FlowLayout());
        quantityPanel.setBackground(Color.WHITE);
        
        quantityLabel.setFont(new Font("Segoe UI", 0, 16));

        quantityTextfield.setBackground(Color.WHITE);
        quantityTextfield.setFont(new Font("Segoe UI", 0, 16));
        quantityTextfield.setPreferredSize(new Dimension(200,30));
        quantityTextfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // chặn nếu không phải số
                }
            }
        });
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityTextfield);

        formPanel.add(quantityPanel);
        formPanel.add(Box.createVerticalStrut(10));


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        
        confirmButton.setFont(new Font("Segoe UI", 0, 16));
        confirmButton.setBackground(Color.decode("#00B4DB"));
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });
        cancelButton.setFont(new Font("Segoe UI", 0, 16));
        cancelButton.setBackground(new Color(190,190,190));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);

        formPanel.add(buttonPanel);

        setTitle("Nhập hàng");
        setResizable(false);
        setSize(450,300);
        getContentPane().add(formPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void confirmButtonActionPerformed(ActionEvent evt){
        int quantity = 0;
        try{
            quantity = Integer.parseInt(quantityTextfield.getText());
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, 
                    "Nhập không đúng định dạng!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
        if(quantity >=1){
            ChiTietPhieuNhap_DTO hangnhap = new ChiTietPhieuNhap_DTO("", sp, quantity, sp.getGia_SanPham()*0.8);
            int index = 0;
            boolean existed = false;
            for(ChiTietPhieuNhap_DTO ct:parent.importList){
                if(ct.getThongtinSP().getID_SanPham().equals(hangnhap.getThongtinSP().getID_SanPham()) ){ // trùng thì tăng số lượng
                    int OldQuantity = ct.getSoluongNhap();
                    parent.importList.get(index).setSoluongNhap(OldQuantity+hangnhap.getSoluongNhap());
                    existed = true;
                }
                index++;
            }
            if(!existed) // không trùng thì thêm vào
                parent.importList.add(hangnhap);

            JOptionPane.showMessageDialog(this, 
                    "Đã thêm "+ quantityTextfield.getText() +" sản phẩm '" + sp.getTen_SanPham() + "' vào danh sách nhập!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }else{
            JOptionPane.showMessageDialog(this, 
                    "Số lượng nhập phải lớn hơn 0!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
