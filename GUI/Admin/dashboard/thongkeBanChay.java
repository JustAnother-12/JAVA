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
    private JComboBox<String> optionComboBox;
    private JButton btnThongKe;
    private ThongKe_BLL bll = new ThongKe_BLL();
    private JScrollPane tableScrollPane;

    public thongkeBanChay() {
        initComponents();
    }

    private void initComponents(){
        table = new JTable();
        btnThongKe = new JButton("Thống kê");
        optionComboBox = new JComboBox<>(new String[] {"Tất cả", "Bán chạy", "Bán ế"});
        tableScrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(optionComboBox);
        top.add(btnThongKe);

        add(top, BorderLayout.NORTH);

        tableScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        add(tableScrollPane, BorderLayout.CENTER);

        btnThongKe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                loadData((String)optionComboBox.getSelectedItem());
            }
        });

        setSize(600, 400);
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