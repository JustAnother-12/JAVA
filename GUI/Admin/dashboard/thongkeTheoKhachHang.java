package GUI.Admin.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import BLL.ThongKe_BLL;
import DTO.thongketheoKhachHang_DTO;
import utils.MyScrollBarUI;

public class thongkeTheoKhachHang extends JPanel{
    private JTable table;
    private JLabel filterLabel;
    private JComboBox<String> optionComboBox;
    private ThongKe_BLL bll = new ThongKe_BLL();
    private ArrayList<thongketheoKhachHang_DTO> list;
    private JScrollPane tableScrollPane;
    private JLabel totalLabel;
    private JTextField totalTextField;
    private double total;

    public thongkeTheoKhachHang() {
        initComponents();
        loadData("Doanh số");
    }

    private void initComponents(){
        table = new JTable();
        tableScrollPane = new JScrollPane();
        filterLabel = new JLabel("Lọc theo:");
        optionComboBox = new JComboBox<>(new String[] {"Doanh số", "Doanh thu"});
        totalLabel = new JLabel();
        totalTextField = new JTextField();
        list = new ArrayList<>();

        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        top.add(filterLabel);
        top.add(Box.createHorizontalStrut(5));
        optionComboBox.setFont(new Font("Segoe UI", 0, 16));
        top.add(optionComboBox);
        add(top, BorderLayout.NORTH);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setGridColor(new Color(51, 51, 51));
        table.setDefaultEditor(Object.class, null);

        tableScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        tableScrollPane.setViewportView(table);
        tableScrollPane.setPreferredSize(new Dimension(850, 570));
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        totalLabel.setText("Tổng doanh thu:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        bottomPanel.add(totalLabel);
        bottomPanel.add(Box.createHorizontalStrut(5));

        totalTextField.setFont(new Font("Segoe UI", 0, 20));
        totalTextField.setText("0");
        totalTextField.setForeground(Color.RED);
        totalTextField.setEditable(false);
        totalTextField.setBackground(Color.WHITE);
        totalTextField.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(totalTextField);
        bottomPanel.add(Box.createHorizontalGlue());

        add(bottomPanel, BorderLayout.SOUTH);

        optionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                loadData((String)optionComboBox.getSelectedItem());
            }
        });

        setSize(850, 570);
        setVisible(true);
    }

    private Double getTotalValue(){
        total = 0.0;
        for (thongketheoKhachHang_DTO tk:list){
            total+=tk.getTongdoanhthu();
        }
        return total;
    }

    private void loadData(String option) {
        list = new ArrayList<>();
        list = bll.getThongKeTheoKhachHang(option);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Tên khách hàng");
        model.addColumn("Tổng doanh số");
        model.addColumn("Tổng doanh thu");

        for (thongketheoKhachHang_DTO dto : list) {
            model.addRow(new Object[]{dto.getTenkh(), dto.getTongsodon(), dto.getTongdoanhthu()});
        }

        table.setModel(model);
        totalTextField.setText(String.valueOf(getTotalValue()));
    }
}
