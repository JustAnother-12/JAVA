package DTO;

public class Vo_DTO extends SanPham_DTO{
    private String Loai_Vo;
    private String NhaSanXuat;
    private String ChatLieu;
    private int SoTrang;

    public Vo_DTO(){
        super();
        this.Loai_Vo="";
        this.NhaSanXuat="";
        this.ChatLieu="";
        this.SoTrang = 0;
    }

    public Vo_DTO(String id, String ten, double gia, int soluong, String loai, String nsx, String chatlieu, int sotrang){
        super(id, ten, gia, soluong);
        this.Loai_Vo=loai;
        this.NhaSanXuat=nsx;
        this.ChatLieu=chatlieu;
        this.SoTrang = sotrang;
    }

    // Getter và Setter cho Loai_Vo
    public String getLoai_Vo() {
        return Loai_Vo;
    }

    public void setLoai_Vo(String Loai_Vo) {
        this.Loai_Vo = Loai_Vo;
    }

    // Getter và Setter cho NhaSanXuat
    public String getNhaSanXuat() {
        return NhaSanXuat;
    }

    public void setNhaSanXuat(String NhaSanXuat) {
        this.NhaSanXuat = NhaSanXuat;
    }

    // Getter và Setter cho ChatLieu
    public String getChatLieu() {
        return ChatLieu;
    }

    public void setChatLieu(String ChatLieu) {
        this.ChatLieu = ChatLieu;
    }

    public int getSoTrang() {
        return SoTrang;
    }

    public void setSoTrang(int soTrang) {
        SoTrang = soTrang;
    }
    
    
}