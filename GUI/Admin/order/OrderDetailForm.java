package GUI.Admin.order;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

import DTO.Order_DTO;
import DTO.OrderDetail_DTO;


public class OrderDetailForm extends javax.swing.JDialog {
    private JTextArea txtOrderInfo,txtOrderDetailInfo;
    private JButton btnShowDetailsButton,btnHideDetails;
    private JPanel detailPanel;
    private ArrayList<OrderDetail_DTO> orderDetailList;

    public OrderDetailForm(Order_DTO order,ArrayList<OrderDetail_DTO> orderDetalList) {
        initComponents();
        this.orderDetailList = orderDetalList;
        setTitle("Chi tiết Đơn Hàng");
        setSize(400,400);
        setLayout(new BorderLayout());
        txtOrderInfo = new JTextArea();
        txtOrderInfo.setEditable(false);
        txtOrderInfo.setText("Thông tin đơn hàng:\n" + 
                "Mã đơn hàng: " + order.getMadonhang() + "\n" +
                "Ngày đặt hàng: " + order.getNgaydat() + "\n" +
                "Tổng tiền: " + order.getTongtien() + "\n" +
                "Trạng thái: " + order.getTinhtrang());
        txtOrderInfo.setLineWrap(true);
        txtOrderInfo.setWrapStyleWord(true);
        add(new JScrollPane(txtOrderInfo), BorderLayout.CENTER);
        btnShowDetailsButton = new JButton("Chi tiết");
        btnShowDetailsButton.setVisible(true);
        add(btnShowDetailsButton,BorderLayout.SOUTH);
        detailPanel = new JPanel();
        detailPanel.setLayout(new BorderLayout());
        txtOrderDetailInfo = new JTextArea();
        txtOrderDetailInfo.setEditable(false);
        txtOrderDetailInfo.setLineWrap(true);
        txtOrderDetailInfo.setWrapStyleWord(true);
        detailPanel.add(new JScrollPane(txtOrderDetailInfo),BorderLayout.CENTER);
        btnHideDetails = new JButton("Ẩn bớt");
        btnHideDetails.setVisible(false);
        detailPanel.add(btnHideDetails,BorderLayout.SOUTH);
        add(detailPanel,BorderLayout.SOUTH);
        detailPanel.setVisible(false);
        btnShowDetailsButton.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e) {
               showOrderDetails(order);
           }
        });
        btnHideDetails.addActionListener(new ActionListener() {
            @Override
           public void actionPerformed(ActionEvent e) {
               hideOrderDetails();
           }
        });
        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
    }
    private void showOrderDetails(Order_DTO order) {
        StringBuilder details = new StringBuilder("Chi tiết đơn hàng:\n");
        for (OrderDetail_DTO detail : orderDetailList) {
            if(order.getMadonhang().equals(detail.getMadonhang())) {
                details.append("Mã sản phẩm: " + detail.getMasp() + "\n" +
                        "Số lượng: " + detail.getSoluong() + "\n" +
                        "Đơn giá: " + detail.getDongia() + "\n" +
                        "Thành tiền: " + detail.getThanhtien() + "\n\n");
            }
        }
        txtOrderDetailInfo.setText(details.toString());
        detailPanel.setVisible(true);
        btnHideDetails.setVisible(true);
        btnShowDetailsButton.setVisible(false);
        pack();
    }   
    private void hideOrderDetails() {
        detailPanel.setVisible(false);
        btnShowDetailsButton.setVisible(true);
        btnHideDetails.setVisible(false);
        pack();
    }
    
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.BorderLayout());

    }
}
