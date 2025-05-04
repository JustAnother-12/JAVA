package BLL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import DTO.CartItemDTO;

public class Cart_BLL {
    private static ArrayList<CartItemDTO> gioHang = new ArrayList<>();

    public static void themVaoGio(CartItemDTO item) {
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

    public static void xoaKhoiGio(String maSanPham) {
        gioHang.removeIf(sp -> sp.getMaSanPham().equals(maSanPham));
    }

    public static void capNhatSoLuong(String maSanPham, int soLuong) {
        for (CartItemDTO sp : gioHang) {
            if (sp.getMaSanPham().equals(maSanPham)) {
                sp.setSoLuong(soLuong);
                break;
            }
        }
    }

    public static ArrayList<CartItemDTO> layDanhSach() {
        return gioHang;
    }

    public static BigDecimal tinhTongTien() {
        return gioHang.stream()
                .map(CartItemDTO::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static void xoaTatCa() {
        gioHang.clear();
    }
}
