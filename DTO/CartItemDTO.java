package DTO;

import java.math.BigDecimal;

public class CartItemDTO {
    private String maSanPham;
    private String tenSanPham;
    private String hinhAnh;
    private int soLuong;
    private BigDecimal donGia;

    public CartItemDTO(String maSanPham, String tenSanPham, String hinhAnh, int soLuong, BigDecimal donGia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.hinhAnh = hinhAnh;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public BigDecimal getThanhTien() {
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }
}
