package GUI.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import BLL.Cart_BLL;
import DTO.CartItemDTO;
import DTO.KhachHang_DTO;

public class CartPanel extends JPanel {
    private JPanel contentPanel;
    private JLabel tongTienLabel;
    private KhachHang_DTO khachHang;

    public CartPanel(KhachHang_DTO khachHang) {
        this.khachHang = khachHang;
        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        add(scrollPane, BorderLayout.CENTER);

        loadCartItems();

        JPanel thongTinPanel = new JPanel(new GridLayout(0, 1));
        thongTinPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        thongTinPanel.add(new JLabel("Họ tên: " + khachHang.getTen_KhachHang()));
        thongTinPanel.add(new JLabel("SĐT: " + khachHang.getSdt_KhachHang()));
        thongTinPanel.add(new JLabel("Địa chỉ: " + khachHang.getDiaChi_KhachHang()));
        
        tongTienLabel = new JLabel("Tổng tiền: " + Cart_BLL.tinhTongTien() + "₫");
        tongTienLabel.setFont(new Font("Arial", Font.BOLD, 16));
        thongTinPanel.add(tongTienLabel);

        JButton muaHangBtn = new JButton("Mua Hàng");
        thongTinPanel.add(muaHangBtn);
        

        add(thongTinPanel, BorderLayout.EAST);
    }

    private void loadCartItems() {
        contentPanel.removeAll();
        ArrayList<CartItemDTO> items = Cart_BLL.layDanhSach();

        for (CartItemDTO item : items) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            // Hình ảnh
            ImageIcon icon = new ImageIcon("GUI/user/ProductImage/" + item.getHinhAnh() + ".png");

            JLabel imgLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
            itemPanel.add(imgLabel, BorderLayout.WEST);

            // Thông tin và nút
            JPanel centerPanel = new JPanel(new GridLayout(3, 1));
            centerPanel.add(new JLabel(item.getTenSanPham()));
            centerPanel.add(new JLabel("Đơn giá: " + item.getDonGia() + "₫"));
            centerPanel.add(new JLabel("Thành tiền: " + item.getThanhTien() + "₫"));
            itemPanel.add(centerPanel, BorderLayout.CENTER);

            // Nút tăng/giảm
            JPanel buttonPanel = new JPanel();
            JButton minusBtn = new JButton("-");
            JButton plusBtn = new JButton("+");
            JButton xoaBtn = new JButton("Xoá");
            JLabel soLuongLabel = new JLabel(String.valueOf(item.getSoLuong()));

            minusBtn.addActionListener(e -> {
                int sl = item.getSoLuong();
                if (sl > 1) {
                    Cart_BLL.capNhatSoLuong(item.getMaSanPham(), sl - 1);
                    refresh();
                }
            });
            plusBtn.addActionListener(e -> {
                Cart_BLL.capNhatSoLuong(item.getMaSanPham(), item.getSoLuong() + 1);
                refresh();
            });
            xoaBtn.addActionListener(e -> {
                Cart_BLL.xoaKhoiGio(item.getMaSanPham());
                refresh();
            });
            buttonPanel.add(minusBtn);
            buttonPanel.add(soLuongLabel);
            buttonPanel.add(plusBtn);
            buttonPanel.add(xoaBtn);
            itemPanel.add(buttonPanel, BorderLayout.EAST);

            contentPanel.add(itemPanel);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void refresh() {
        loadCartItems();
        tongTienLabel.setText("Tổng tiền: " + Cart_BLL.tinhTongTien() + "₫");
    }
}
