package BLL;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import DTO.CartItemDTO;
import DTO.Order_DTO;
import DTO.OrderDetail_DTO;

public class Cart_BLL {
    private static ArrayList<CartItemDTO> gioHang = new ArrayList<>();

    public void themVaoGio(CartItemDTO item) {
        Optional<CartItemDTO> tonTai = gioHang.stream()
            .filter(sp -> sp.getMaSanPham().equals(item.getMaSanPham()))
            .findFirst();

        if (tonTai.isPresent()) {
            CartItemDTO sp = tonTai.get();
            sp.setSoLuong(sp.getSoLuong() + item.getSoLuong());
        } else {
            gioHang.add(item);
        }
    }

    public void xoaKhoiGio(String maSanPham) {
        gioHang.removeIf(sp -> sp.getMaSanPham().equals(maSanPham));
    }

    public void capNhatSoLuong(String maSanPham, int soLuong) {
        for (CartItemDTO sp : gioHang) {
            if (sp.getMaSanPham().equals(maSanPham)) {
                sp.setSoLuong(soLuong);
                break;
            }
        }
    }

    public ArrayList<CartItemDTO> layDanhSach() {
        return gioHang;
    }

    public BigDecimal tinhTongTien() {
        return gioHang.stream()
                .map(CartItemDTO::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void xoaTatCa() {
        gioHang.clear();
    }

    public Order_DTO createOrder(String diachi, String idkh){
        Order_DTO order = new Order_DTO("", diachi, getCurrentDay(), "Chưa xử lý", tinhTongTien().doubleValue(), null, idkh);
        return order;
    }

    public ArrayList<OrderDetail_DTO> createDetails(){
        ArrayList<OrderDetail_DTO> details = new ArrayList<>();
        for (CartItemDTO item : gioHang){
            Double thanhtien = item.getSoLuong()*item.getDonGia().doubleValue();
            OrderDetail_DTO dt = new OrderDetail_DTO("", item.getMaSanPham(), item.getSoLuong(),item.getDonGia().doubleValue(), thanhtien);
            details.add(dt);
        }
        return details;
    }

    private String getCurrentDay() {
        LocalDate ngayHienTai = LocalDate.now();
        DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ngayHienTai.format(dinhDang);
    }
}
