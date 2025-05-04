package GUI.Admin.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import BLL.ThongKe_BLL;
import DTO.thongkeBanChay_DTO;
import utils.MyScrollBarUI;

public class thongkeBanChay extends JPanel {
    private JTable table;
    private JLabel optionLabel;
    private JComboBox<String> optionComboBox;
    private ThongKe_BLL bll = new ThongKe_BLL();
    private JScrollPane tableScrollPane;

    public thongkeBanChay() {
        initComponents();
        loadData("Tất cả");
    }

    private void initComponents(){
        table = new JTable();
        optionLabel = new JLabel("Chọn:");
        optionComboBox = new JComboBox<>(new String[] {"Tất cả", "Bán chạy", "Bán ế"});
        tableScrollPane = new JScrollPane();

        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        optionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        top.add(optionLabel);
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

        optionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                loadData((String)optionComboBox.getSelectedItem());
            }
        });

        setSize(850, 570);
        setVisible(true);
    }

    private void loadData(String option) {
        ArrayList<thongkeBanChay_DTO> list = new ArrayList<>();
        switch (option) {
            case "Tất cả":
                list = bll.getDoanhSoBanHang();
                break;
            case "Bán chạy":
                list = bll.getBestSellers();
                break;
            case "Bán ế":
                list = bll.getWorstSellers();
                break;
            default:
                break;
        }

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Tên SP");
        model.addColumn("Danh mục");
        model.addColumn("Tổng Bán");

        for (thongkeBanChay_DTO dto : list) {
            model.addRow(new Object[]{dto.getTenSP(), dto.getDanhMuc(), dto.getTongBan()});
        }

        table.setModel(model);
    }
}