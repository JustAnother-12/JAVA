package GUI.Admin.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import BLL.ThongKe_BLL;
import DTO.ThongKeDoanhThu_DTO;
import utils.MyScrollBarUI;

import java.awt.*;
import java.util.ArrayList;

public class thongkeDoanhThu extends JPanel{
    private JTable table;
    private JButton btnThongKe;
    private JComboBox<String> cboThang;
    private JScrollPane tableScrollPane;

    private ThongKe_BLL tkBLL = new ThongKe_BLL();

    public thongkeDoanhThu() {
        initComponents();
    }

    private void initComponents(){
        table = new JTable();
        tableScrollPane = new JScrollPane(table);
        cboThang = new JComboBox<>();
        btnThongKe = new JButton("Thống kê");

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem("Tháng " + i);
        }

        topPanel.add(cboThang);
        topPanel.add(btnThongKe);
        add(topPanel, BorderLayout.NORTH);

        tableScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        add(tableScrollPane, BorderLayout.CENTER);

        btnThongKe.addActionListener(e -> loadData());

        setSize(600, 400);
        setVisible(true);
    }

    private void loadData() {
        int thang = cboThang.getSelectedIndex() + 1;
        ArrayList<ThongKeDoanhThu_DTO> doanhthulist = tkBLL.getDoanhThuTheoThang(thang);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Ngày");
        model.addColumn("Danh mục");
        model.addColumn("Tổng Doanh Thu");

        for (ThongKeDoanhThu_DTO dto : doanhthulist) {
            model.addRow(new Object[]{dto.getNgay(), dto.getDanhMuc(), dto.getTongDoanhThu()});
        }

        table.setModel(model);
    }
}
