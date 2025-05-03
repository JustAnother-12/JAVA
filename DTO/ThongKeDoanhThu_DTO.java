package DTO;

public class ThongKeDoanhThu_DTO {
    private String ngay;
    private String masp;
    private double tongDoanhThu;

    public ThongKeDoanhThu_DTO(String ngay, String masp, double tongDoanhThu) {
        this.ngay = ngay;
        this.masp = masp;
        this.tongDoanhThu = tongDoanhThu;
    }

    public String getNgay() {
        return ngay;
    }

    public String getmasp(){
        return masp;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public String getDanhMuc(){
        switch (masp.substring(0, 1)) {
            case "S":
                return "Sách";
            case "V":
                return "Vở";
            case "B":
                return "Bút";
            default:
                return "N/A";
        }
    }
}

