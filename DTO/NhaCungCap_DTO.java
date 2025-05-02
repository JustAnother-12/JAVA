package DTO;

public class NhaCungCap_DTO {
    private String MaNCC;
    private String TenNCC;
    private String sdtNCC;
    private String emailNCC;

    public NhaCungCap_DTO() {
        MaNCC = "";
        TenNCC = "";
        sdtNCC = "";
        emailNCC = "";
    }

    public NhaCungCap_DTO(String maNCC, String tenNCC, String sdtNCC, String emailNCC) {
        MaNCC = maNCC;
        TenNCC = tenNCC;
        this.sdtNCC = sdtNCC;
        this.emailNCC = emailNCC;
    }
    
    public String getMaNCC() {
        return MaNCC;
    }
    public void setMaNCC(String maNCC) {
        MaNCC = maNCC;
    }
    public String getTenNCC() {
        return TenNCC;
    }
    public void setTenNCC(String tenNCC) {
        TenNCC = tenNCC;
    }
    public String getSdtNCC() {
        return sdtNCC;
    }
    public void setSdtNCC(String sdtNCC) {
        this.sdtNCC = sdtNCC;
    }
    public String getEmailNCC() {
        return emailNCC;
    }
    public void setEmailNCC(String emailNCC) {
        this.emailNCC = emailNCC;
    }
}
