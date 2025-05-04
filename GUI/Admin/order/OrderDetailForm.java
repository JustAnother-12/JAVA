package GUI.Admin.order;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import BLL.DonHang_BLL;
import DTO.Order_DTO;
import DTO.OrderDetail_DTO;

public class OrderDetailForm extends JDialog {
    private JButton btnShowDetailsButton, btnHideDetails;
    private JPanel detailPanel, productPanel;
    private ArrayList<OrderDetail_DTO> orderDetailList;
    DonHang_BLL DonHang_BLL = new DonHang_BLL();
    public OrderDetailForm(Order_DTO order, ArrayList<OrderDetail_DTO> orderDetailList) {
        this.orderDetailList = orderDetailList;
        setTitle("Chi tiết Đơn Hàng");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ==== THÔNG TIN ĐƠN HÀNG ====
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addField(infoPanel, "Mã đơn hàng:", order.getMadonhang());
        addField(infoPanel, "Ngày đặt hàng:", order.getNgaydat());
        addField(infoPanel, "Tổng tiền:", order.getTongtien() + " VNĐ");
        String customerName = DonHang_BLL.getCustomerName(order.getMakh());
        addField(infoPanel, "Khách hàng:", customerName);

        addField(infoPanel, "Tình trạng:", order.getTinhtrang());

        if ("Đã xử lý".equalsIgnoreCase(order.getTinhtrang())) {
            String employeeName = DonHang_BLL.getEmployeeInfo(order.getManv());
            addField(infoPanel, "Nhân viên xử lý:", employeeName);
        }

        add(infoPanel, BorderLayout.NORTH);

        // ==== PANEL CHI TIẾT SẢN PHẨM ====
        detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setVisible(false);

        // ==== PANEL NÚT ====
        JPanel buttonPanel = new JPanel();
        btnShowDetailsButton = new JButton("Chi tiết");
        btnHideDetails = new JButton("Ẩn bớt");
        btnHideDetails.setVisible(false);

        buttonPanel.add(btnShowDetailsButton);
        buttonPanel.add(btnHideDetails);

        add(detailPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ==== SỰ KIỆN ====
        btnShowDetailsButton.addActionListener(e -> showOrderDetails(order));
        btnHideDetails.addActionListener(e -> hideOrderDetails());

        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
    }

    private void addField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField(value);
        field.setEditable(false);
        panel.add(field);
    }

    private void showOrderDetails(Order_DTO order) {
        detailPanel.removeAll();

        JLabel title = new JLabel("Danh sách sản phẩm:");
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        detailPanel.add(title);

        for (OrderDetail_DTO detail : orderDetailList) {
            if (order.getMadonhang().equals(detail.getMadonhang())) {
                String tensp = DonHang_BLL.getProductName(detail.getMasp());

                JPanel itemPanel = new JPanel(new GridLayout(0, 2, 5, 5));
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                addField(itemPanel, "Tên sản phẩm:", tensp);
                addField(itemPanel, "Số lượng:", String.valueOf(detail.getSoluong()));
                addField(itemPanel, "Đơn giá:", detail.getDongia() + " VNĐ");
                addField(itemPanel, "Thành tiền:", detail.getThanhtien() + " VNĐ");

                itemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                detailPanel.add(itemPanel);
            }
        }

        detailPanel.setVisible(true);
        btnShowDetailsButton.setVisible(false);
        btnHideDetails.setVisible(true);
        pack();
    }

    private void hideOrderDetails() {
        detailPanel.setVisible(false);
        btnHideDetails.setVisible(false);
        btnShowDetailsButton.setVisible(true);
        pack();
    }
}