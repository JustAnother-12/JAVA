package DTO;

public class ChiTietPhieuNhap_DTO {
    String MaPN;
    SanPham_DTO thongtinSP;
    int soluongNhap;
    double dongiaNhap;

    public ChiTietPhieuNhap_DTO(String maPN, SanPham_DTO thongtinSP, int soluongNhap, double dongiaNhap) {
        MaPN = maPN;
        this.thongtinSP = thongtinSP;
        this.soluongNhap = soluongNhap;
        this.dongiaNhap = dongiaNhap;
    }

    public String getMaPN() {
        return MaPN;
    }

    public void setMaPN(String maPN) {
        MaPN = maPN;
    }

    public SanPham_DTO getThongtinSP() {
        return thongtinSP;
    }

    public void setThongtinSP(SanPham_DTO thongtinSP) {
        this.thongtinSP = thongtinSP;
    }

    public int getSoluongNhap() {
        return soluongNhap;
    }

    public void setSoluongNhap(int soluongNhap) {
        this.soluongNhap = soluongNhap;
    }

    public double getDongiaNhap() {
        return dongiaNhap;
    }

}
