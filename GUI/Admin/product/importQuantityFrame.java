package GUI.Admin.product;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import BLL.NhaCungCap_BLL;
import DTO.NhaCungCap_DTO;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class importQuantityFrame extends JFrame{
    private JPanel formPanel;
    private JLabel formLabel;
    private JLabel quantityLabel;
    private JSpinner quantitySpinner;
    private JLabel providerLabel;
    private JComboBox<String> providerComboBox; 

    private JButton confirmButton;
    private JButton cancelButton;


    private NhaCungCap_BLL nccBLL;

    public importQuantityFrame(){
        initComponents();
    }

    private void initComponents(){
        nccBLL = new NhaCungCap_BLL();
        formPanel = new JPanel();
        formLabel = new JLabel("NHẬP HÀNG");
        quantityLabel = new JLabel("Số lượng:");
        quantitySpinner = new JSpinner();
        providerLabel = new JLabel("Nhà cung cấp:");
        ArrayList<NhaCungCap_DTO> danhSachNCC = nccBLL.getAllNCC();
        String[] listTenNCC = new String[danhSachNCC.size()];
        for (int i = 0; i < danhSachNCC.size(); i++) {
            listTenNCC[i] = danhSachNCC.get(i).getTenNCC();
        }
        providerComboBox = new JComboBox<>(listTenNCC);
        confirmButton = new JButton("Xác nhận");
        cancelButton = new JButton("Hủy"); 

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

        quantitySpinner.setBackground(Color.WHITE);
        quantitySpinner.setFont(new Font("Segoe UI", 0, 16));
        quantitySpinner.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantitySpinner);

        formPanel.add(quantityPanel);
        formPanel.add(Box.createVerticalStrut(10));


        JPanel providerPanel = new JPanel();
        providerPanel.setLayout(new FlowLayout());
        providerPanel.setBackground(Color.WHITE);

        providerLabel.setFont(new Font("Segoe UI", 0, 16));

        providerComboBox.setBackground(Color.WHITE);
        providerComboBox.setFont(new Font("Segoe UI", 0, 16));
        
        providerPanel.add(providerLabel);
        providerPanel.add(providerComboBox);

        formPanel.add(providerPanel);
        formPanel.add(Box.createHorizontalStrut(20));


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        
        confirmButton.setFont(new Font("Segoe UI", 0, 16));
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });
        cancelButton.setFont(new Font("Segoe UI", 0, 16));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);

        formPanel.add(buttonPanel);

        setResizable(false);
        setSize(450,400);
        getContentPane().add(formPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void confirmButtonActionPerformed(ActionEvent evt){

    }
}
