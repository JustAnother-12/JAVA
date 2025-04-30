package DTO;

import java.util.ArrayList;

public class PhieuNhap_DTO {
    String MaPN;
    String MaNCC;
    String MaNV;
    String NgayNhap;
    ArrayList<ChiTietPhieuNhap_DTO> ChiTietPhieuNhap;
    
    public PhieuNhap_DTO(String maPN, String maNCC, String maNV, String ngayNhap, ArrayList<ChiTietPhieuNhap_DTO> chiTietPhieuNhap) {
        MaPN = maPN;
        MaNCC = maNCC;
        MaNV = maNV;
        NgayNhap = ngayNhap;
        ChiTietPhieuNhap = chiTietPhieuNhap;
    }
    public String getMaPN() {
        return MaPN;
    }
    public void setMaPN(String maPN) {
        MaPN = maPN;
    }
    public String getMaNCC() {
        return MaNCC;
    }
    public void setMaNCC(String maNCC) {
        MaNCC = maNCC;
    }
    public String getMaNV() {
        return MaNV;
    }
    public void setMaNV(String maNV) {
        MaNV = maNV;
    }
    public String getNgayNhap() {
        return NgayNhap;
    }
    public void setNgayNhap(String ngayNhap) {
        NgayNhap = ngayNhap;
    }
    public ArrayList<ChiTietPhieuNhap_DTO> getChiTietPhieuNhap() {
        return ChiTietPhieuNhap;
    }
    public void setChiTietPhieuNhap(ArrayList<ChiTietPhieuNhap_DTO> chiTietPhieuNhap) {
        ChiTietPhieuNhap = chiTietPhieuNhap;
    }

    
}
