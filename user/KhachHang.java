package user;

public class KhachHang {
   private String Id_KhachHang ;
   private String Ten_KhachHang ;
   private String Sdt_KhachHang ;
   private String GioiTinh_KhachHang;
   private String DiaChi_KhachHang;
   private String Email;
   private String Pass_KhachHang;
   private String NgaySinh_KhachHang;
   public KhachHang ()
   {
      this.Id_KhachHang = "";
      this.Ten_KhachHang = "";
      this.Sdt_KhachHang = "";
      this.GioiTinh_KhachHang = "";
      this.DiaChi_KhachHang = "";
      this.Pass_KhachHang = "";
   }
   public KhachHang(String Id,String Ten, String Sdt,String gioiTinh, String DiaChi, String Email, String Pass, String NgaySinh)

   {
      this.Id_KhachHang = Id;
      this.Ten_KhachHang = Ten;
      this.Sdt_KhachHang = Sdt;
      this.GioiTinh_KhachHang = gioiTinh;
      this.DiaChi_KhachHang = DiaChi;
      this.Pass_KhachHang = Pass;
      this.NgaySinh_KhachHang = NgaySinh;
      this.Email = Email;
   }

   public String getEmail(){
      return Email;
   }

   public void setEmail(String email){
      Email = email;
   }

   // Getter and Setter Id_KhachHang
   public String getId_KhachHang() {
      return Id_KhachHang;
   }

   public void setId_KhachHang(String id_KhachHang) {
      Id_KhachHang = id_KhachHang;
   }
   
   // Getter and Setter for GioTinh_KhachHang
   public String getGioiTinh_KhachHang() {
      return GioiTinh_KhachHang;
   }

   public void setGioiTinh_KhachHang(String gioiTinh_KhachHang) {
      GioiTinh_KhachHang = gioiTinh_KhachHang;
   }

   // Getter and Setter for NgaySinh_KhachHang
   public String getNgaySinh_KhachHang() {
      return NgaySinh_KhachHang;
   }

   public void setNgaySinh_KhachHang(String ngaySinh_KhachHang) {
      NgaySinh_KhachHang = ngaySinh_KhachHang;
   }

   // Getter and Setter for Ten_KhachHang
   public String getTen_KhachHang() {
      return Ten_KhachHang;
   }

   public void setTen_KhachHang(String ten_KhachHang) {
      Ten_KhachHang = ten_KhachHang;
   }

   // Getter and Setter for Sdt_KhachHang
   public String getSdt_KhachHang() {
      return Sdt_KhachHang;
   }

   public void setSdt_KhachHang(String sdt_KhachHang) {
      Sdt_KhachHang = sdt_KhachHang;
   }

   // Getter and Setter for DiaChi_KhachHang
   public String getDiaChi_KhachHang() {
      return DiaChi_KhachHang;
   }

   public void setDiaChi_KhachHang(String diaChi_KhachHang) {
      DiaChi_KhachHang = diaChi_KhachHang;
   }

   // Getter and Setter for Pass_KhachHang
   public String getPass_KhachHang() {
      return Pass_KhachHang;
   }

   public void setPass_KhachHang(String pass_KhachHang) {
      Pass_KhachHang = pass_KhachHang;
   }
}
