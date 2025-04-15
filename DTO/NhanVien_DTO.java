package DTO;

public class NhanVien_DTO {
    private String manv;
    private String tennv;
    private String chucvu;
    private String sdt;
    private String username;
    private String password;
    private String diachi;
    private String CCCD;
    private String gioitinh;
    private String ngaysinh;

    // Phương thức khởi tạo không tham số
    public NhanVien_DTO(String manv, String tennv, String username, String sdt) {
        this.manv = manv;
        this.tennv = tennv;
        this.sdt = sdt;
        this.username = username;
    }

    // Phương thức khởi tạo có tham số
    public NhanVien_DTO(String manv, String tennv, String chucvu, String sdt, String username, 
                    String password, String diachi, String CCCD, String gioitinh, String ngaysinh) {
        this.manv = manv;
        this.tennv = tennv;
        this.chucvu = chucvu;
        this.sdt = sdt;
        this.username = username;
        this.password = password;
        this.diachi = diachi;
        this.CCCD = CCCD;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
    }

    // Getter và Setter
    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getTennv() {
        return tennv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }

    public String getChucvu() {
        return chucvu;
    }

    public void setChucvu(String chucvu) {
        this.chucvu = chucvu;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }
}
