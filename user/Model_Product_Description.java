package user;

public class Model_Product_Description {
    private String name;
    private int soLuong;           // Quantity
    private String XuatXu;         // Origin
    private String TrongLuong;      // Weight
    private String KichThuocManHinh; // Screen size
    private String DungLuong;      // Storage capacity
    private String RAM;            // RAM
    private String ThuongHieu;     // Brand
    private int BaoHanh;           // Warranty (in months or years, presumably)

    // No-argument constructor
    public Model_Product_Description() {
        this.name = "";
        this.soLuong = 0;
        this.XuatXu = "";
        this.TrongLuong = "";
        this.KichThuocManHinh = "";
        this.DungLuong = "";
        this.RAM = "";
        this.ThuongHieu = "";
        this.BaoHanh = 0;
    }

    // Parameterized constructor
    public Model_Product_Description(String name, int soLuong, String XuatXu, String TrongLuong, 
                                     String KichThuocManHinh, String DungLuong, 
                                     String RAM, String ThuongHieu, int BaoHanh) {
        this.name = name;
        this.soLuong = soLuong;
        this.XuatXu = XuatXu;
        this.TrongLuong = TrongLuong;
        this.KichThuocManHinh = KichThuocManHinh;
        this.DungLuong = DungLuong;
        this.RAM = RAM;
        this.ThuongHieu = ThuongHieu;
        this.BaoHanh = BaoHanh;
    }

    public String getname(){
        return name;
    }

    // Getters and Setters
    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getXuatXu() {
        return XuatXu;
    }

    public void setXuatXu(String XuatXu) {
        this.XuatXu = XuatXu;
    }

    public String getTrongLuong() {
        return TrongLuong;
    }

    public void setTrongLuong(String TrongLuong) {
        this.TrongLuong = TrongLuong;
    }

    public String getKichThuocManHinh() {
        return KichThuocManHinh;
    }

    public void setKichThuocManHinh(String KichThuocManHinh) {
        this.KichThuocManHinh = KichThuocManHinh;
    }

    public String getDungLuong() {
        return DungLuong;
    }

    public void setDungLuong(String DungLuong) {
        this.DungLuong = DungLuong;
    }

    public String getRAM() {
        return RAM;
    }

    public void setRAM(String RAM) {
        this.RAM = RAM;
    }

    public String getThuongHieu() {
        return ThuongHieu;
    }

    public void setThuongHieu(String ThuongHieu) {
        this.ThuongHieu = ThuongHieu;
    }

    public int getBaoHanh() {
        return BaoHanh;
    }

    public void setBaoHanh(int BaoHanh) {
        this.BaoHanh = BaoHanh;
    }
}