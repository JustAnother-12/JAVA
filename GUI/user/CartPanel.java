package GUI.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import BLL.Cart_BLL;
import BLL.DonHang_BLL;
import BLL.SanPham_BLL;
import DTO.CartItemDTO;
import DTO.KhachHang_DTO;
import DTO.SanPham_DTO;

public class CartPanel extends JPanel {
    private JPanel contentPanel;
    private JLabel tongTienLabel;
    private KhachHang_DTO khachHang;
    private DonHang_BLL orderBLL = new DonHang_BLL();
    private Cart_BLL cartBLL = new Cart_BLL();
    private SanPham_BLL spBLL = new SanPham_BLL();
    private ArrayList<CartItemDTO> items;

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
        
        tongTienLabel = new JLabel("Tổng tiền: " + cartBLL.tinhTongTien() + "₫");
        tongTienLabel.setFont(new Font("Arial", Font.BOLD, 16));
        thongTinPanel.add(tongTienLabel);

        JButton muaHangBtn = new JButton("Mua Hàng");
        thongTinPanel.add(muaHangBtn);
        muaHangBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                muaHangActionPerform();
            }
        });
        

        add(thongTinPanel, BorderLayout.EAST);
    }

    private void loadCartItems() {
        contentPanel.removeAll();
        items = cartBLL.layDanhSach();

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
                    cartBLL.capNhatSoLuong(item.getMaSanPham(), sl - 1);
                    refresh();
                }
            });
            plusBtn.addActionListener(e -> {
                String id = item.getMaSanPham();
                SanPham_DTO sp = null;
                if (id.contains("S")){
                    sp = spBLL.getSachFromID(id);
                }else if (id.contains("V")){
                    sp = spBLL.getVoFromID(id);
                }else if (id.contains("B")){
                    sp = spBLL.getButFromID(id);
                }
                if(item.getSoLuong() >= sp.getSoLuong_SanPham()){
                    JOptionPane.showMessageDialog(this, "Đạt tối đa số lượng của sản phẩm!");
                }else{
                    cartBLL.capNhatSoLuong(item.getMaSanPham(), item.getSoLuong() + 1);
                    refresh();
                }
            });
            xoaBtn.addActionListener(e -> {
                cartBLL.xoaKhoiGio(item.getMaSanPham());
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

    private void muaHangActionPerform(){
        if(!items.isEmpty()){
            String message = orderBLL.addOrder(cartBLL.createOrder(khachHang.getDiaChi_KhachHang(), khachHang.getId_KhachHang()), cartBLL.createDetails());
            if(message.contains("thành công!")){
                JOptionPane.showMessageDialog(this, "Đặt hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                cartBLL.xoaTatCa();
                refresh();
                contentPanel.revalidate();
                contentPanel.repaint();
            }else{
                JOptionPane.showMessageDialog(this, "Đặt hàng thất bại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refresh() {
        loadCartItems();
        tongTienLabel.setText("Tổng tiền: " + cartBLL.tinhTongTien() + "₫");
    }
}
