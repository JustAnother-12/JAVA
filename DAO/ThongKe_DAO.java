package DAO;

import java.sql.*;
import java.util.ArrayList;

import DTO.*;

public class ThongKe_DAO {
    private Connection con;

    public ArrayList<ThongKeDoanhThu_DTO> getDoanhThuTheoThang(int thang) {
        ArrayList<ThongKeDoanhThu_DTO> arr = new ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String query = """
                    SELECT ct.masp, DATE(dh.ngaydat) AS Ngay,
                        SUM(dh.tongtien) AS DoanhThu
                    FROM DONHANG dh
                    JOIN CHITIETDONHANG ct ON ct.madonhang = dh.madonhang
                    WHERE MONTH(dh.ngaydat) = ?
                    AND dh.tinhtrang = 'Đã xử lý'
                    GROUP BY DATE(dh.ngaydat), ct.masp
                    ORDER BY Ngay
                """;
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, thang);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String masp = rs.getString("masp");
                    String ngay = rs.getString("Ngay");
                    double doanhThu = rs.getDouble("DoanhThu");
                    ThongKeDoanhThu_DTO dt = new ThongKeDoanhThu_DTO(ngay, masp, doanhThu);
                    arr.add(dt);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return arr;
    }

    public ArrayList<thongkeBanChay_DTO> getDoanhSoBanHang() {
        ArrayList<thongkeBanChay_DTO> arr = new ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String query = """
                    SELECT sp.masp, sp.tensp, SUM(ct.soluong) AS doanhso
                    FROM SANPHAM sp, CHITIETDONHANG ct
                    JOIN DONHANG dh ON dh.madonhang = ct.madonhang
                    WHERE sp.masp = ct.masp
                    AND dh.tinhtrang = 'Đã xử lý'
                    GROUP BY sp.tensp, sp.masp
                    ORDER BY doanhso DESC
                """;

                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String masp = rs.getString("masp");
                    String tenSP = rs.getString("tensp");
                    int tongBan = rs.getInt("doanhso");

                    thongkeBanChay_DTO tk = new thongkeBanChay_DTO(tenSP, masp, tongBan);
                    arr.add(tk);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return arr;
    }

    public ArrayList<thongkeBanChay_DTO> getBestSellers() {
        ArrayList<thongkeBanChay_DTO> arr = new ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String query = """
                    SELECT sp.masp, sp.tensp, SUM(ct.soluong) AS doanhso
                    FROM SANPHAM sp, CHITIETDONHANG ct
                    JOIN DONHANG dh ON dh.madonhang = ct.madonhang
                    WHERE sp.masp = ct.masp
                    AND dh.tinhtrang = 'Đã xử lý'
                    GROUP BY sp.tensp, sp.masp
                    ORDER BY doanhso DESC
                    LIMIT 3
                """;

                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String masp = rs.getString("masp");
                    String tenSP = rs.getString("tensp");
                    int tongBan = rs.getInt("doanhso");

                    thongkeBanChay_DTO tk = new thongkeBanChay_DTO(tenSP, masp, tongBan);
                    arr.add(tk);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return arr;
    }

    public ArrayList<thongkeBanChay_DTO> getWorstSellers() {
        ArrayList<thongkeBanChay_DTO> arr = new ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String query = """
                    SELECT sp.masp, sp.tensp, COALESCE(SUM(ct.soluong), 0) AS doanhso
                    FROM SANPHAM sp, CHITIETDONHANG ct
                    LEFT JOIN DONHANG dh ON ct.madonhang = dh.madonhang AND dh.tinhtrang = 'Đã xử lý'
                    WHERE sp.masp = ct.masp
                    GROUP BY sp.tensp, sp.masp
                    ORDER BY doanhso ASC
                    LIMIT 3
                """;

                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String masp = rs.getString("masp");
                    String tenSP = rs.getString("tensp");
                    int tongBan = rs.getInt("doanhso");

                    thongkeBanChay_DTO tk = new thongkeBanChay_DTO(tenSP, masp, tongBan);
                    arr.add(tk);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return arr;
    }

    public ArrayList<thongketheoKhachHang_DTO> getThongKeTheoKhachHang(String sortBy) {
        ArrayList<thongketheoKhachHang_DTO> arr = new ArrayList<>();
        con = DatabaseConnection.OpenConnection();
        if(con != null){
            try {
                String baseQuery = """
                    SELECT kh.tenkh, COUNT(dh.madonhang) AS doanhso, SUM(dh.tongtien) AS doanhthu
                    FROM KHACHHANG kh
                    JOIN DONHANG dh ON kh.makh = dh.makh
                    WHERE dh.tinhtrang = 'Đã xử lý'
                    GROUP BY kh.tenkh
                """;
                String orderByQuery = "ORDER BY ";

                switch (sortBy) {
                    case "Doanh thu":
                        orderByQuery += "doanhthu DESC";
                        break;
                    case "Doanh số":
                        orderByQuery += "doanhso DESC";
                        break;
                    default:
                        orderByQuery += "doanhthu DESC"; // Default
                }
                String finalQuery = baseQuery + orderByQuery;

                PreparedStatement stmt = con.prepareStatement(finalQuery);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String makh = rs.getString("tenkh");
                    int doanhso = rs.getInt("doanhso");
                    double doanhthu = rs.getDouble("doanhthu");

                    thongketheoKhachHang_DTO tk = new thongketheoKhachHang_DTO(makh, doanhso, doanhthu);
                    arr.add(tk);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                DatabaseConnection.closeConnection(con);
            }
        }
        return arr;
    }

}
