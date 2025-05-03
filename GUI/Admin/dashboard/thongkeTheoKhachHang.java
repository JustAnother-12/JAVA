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
    private JComboBox<String> optionComboBox;
    private ThongKe_BLL bll = new ThongKe_BLL();
    private JScrollPane tableScrollPane;

    public thongkeTheoKhachHang() {
        table = new JTable();
        tableScrollPane = new JScrollPane(table);
        optionComboBox = new JComboBox<>(new String[] {"Doanh số", "Doanh thu"});

        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(optionComboBox);
        add(top, BorderLayout.NORTH);

        tableScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        add(tableScrollPane, BorderLayout.CENTER);

        optionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                loadData((String)optionComboBox.getSelectedItem());
            }
        });

        setSize(600, 400);
        setVisible(true);
    }

    private void loadData(String option) {
        ArrayList<thongketheoKhachHang_DTO> list = new ArrayList<>();
        list = bll.getThongKeTheoKhachHang(option);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Tên khách hàng");
        model.addColumn("Tổng doanh số");
        model.addColumn("Tổng doanh thu");

        for (thongketheoKhachHang_DTO dto : list) {
            model.addRow(new Object[]{dto.getTenkh(), dto.getTongsodon(), dto.getTongdoanhthu()});
        }

        table.setModel(model);
    }
}
