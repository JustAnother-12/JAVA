package GUI.Admin.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import BLL.ThongKe_BLL;
import DTO.ThongKeDoanhThu_DTO;
import utils.MyScrollBarUI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class thongkeDoanhThu extends JPanel{
    private JTable table;
    private JLabel optionlLabel;
    private JComboBox<String> cboThang;
    private JScrollPane tableScrollPane;
    private JLabel totalLabel;
    private JTextField totalTextField;
    private double total;

    private ThongKe_BLL tkBLL = new ThongKe_BLL();
    private ArrayList<ThongKeDoanhThu_DTO> doanhthulist;

    public thongkeDoanhThu() {
        initComponents();
        loadData();
    }

    private void initComponents(){
        table = new JTable();
        tableScrollPane = new JScrollPane();
        optionlLabel = new JLabel("Chọn tháng: ");
        cboThang = new JComboBox<>();
        totalLabel = new JLabel("Tổng doanh thu:");
        totalTextField = new JTextField();
        doanhthulist = new ArrayList<>();

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        optionlLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topPanel.add(optionlLabel);
        topPanel.add(Box.createHorizontalStrut(5));

        for (int i = 1; i <= 12; i++) {
            cboThang.addItem("Tháng " + i);
        }
        cboThang.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        topPanel.add(cboThang);
        add(topPanel, BorderLayout.NORTH);

        table.setGridColor(new Color(51, 51, 51));
        table.setDefaultEditor(Object.class, null);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        tableScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        tableScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        tableScrollPane.setViewportView(table);
        tableScrollPane.setPreferredSize(new Dimension(850, 570));
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
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

        cboThang.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                loadData();
            }
        });

        setSize(850, 570);
        setVisible(true);
    }

    private Double getTotalValue(){
        total = 0.0;
        for (ThongKeDoanhThu_DTO tk:doanhthulist){
            total+=tk.getTongDoanhThu();
        }
        return total;
    }

    private void loadData() {
        int thang = cboThang.getSelectedIndex() + 1;
        doanhthulist = tkBLL.getDoanhThuTheoThang(thang);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Ngày");
        model.addColumn("Danh mục");
        model.addColumn("Tổng Doanh Thu");

        for (ThongKeDoanhThu_DTO dto : doanhthulist) {
            model.addRow(new Object[]{dto.getNgay(), dto.getDanhMuc(), dto.getTongDoanhThu()});
        }

        table.setModel(model);
        totalTextField.setText(String.valueOf(getTotalValue()));
        
    }
}
