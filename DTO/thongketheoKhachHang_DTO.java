package DTO;

public class thongketheoKhachHang_DTO {
    private String tenkh;
    private int tongsodon;
    private double tongdoanhthu;

    public thongketheoKhachHang_DTO(String tenkh, int tongsodon, double tongdoanhthu) {
        this.tenkh = tenkh;
        this.tongsodon = tongsodon;
        this.tongdoanhthu = tongdoanhthu;
    }

    public String getTenkh() {
        return tenkh;
    }
    public int getTongsodon() {
        return tongsodon;
    }
    public double getTongdoanhthu() {
        return tongdoanhthu;
    }
}
