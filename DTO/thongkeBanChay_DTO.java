package DTO;

public class thongkeBanChay_DTO {
    private String tenSP;
    private String masp;
    private int tongBan;

    public thongkeBanChay_DTO(String tenSP, String masp, int tongBan) {
        this.tenSP = tenSP;
        this.masp = masp;
        this.tongBan = tongBan;
    }
    

    public String getTenSP() { 
        return tenSP; 
    }
    public int getTongBan() { 
        return tongBan; 
    }

    public String getmasp() {
        return masp;
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
