package BLL;

import java.util.ArrayList;
import DAO.ThongKe_DAO;
import DTO.ThongKeDoanhThu_DTO;
import DTO.thongkeBanChay_DTO;
import DTO.thongketheoKhachHang_DTO;

public class ThongKe_BLL {
    ThongKe_DAO tkDAO = new ThongKe_DAO();

    public ArrayList<ThongKeDoanhThu_DTO> getDoanhThuTheoThang(int month){
        return tkDAO.getDoanhThuTheoThang(month);
    }

    public ArrayList<thongkeBanChay_DTO> getDoanhSoBanHang(){
        return tkDAO.getDoanhSoBanHang();
    }

    public ArrayList<thongkeBanChay_DTO> getBestSellers(){
        return tkDAO.getBestSellers();
    }

    public ArrayList<thongkeBanChay_DTO> getWorstSellers(){
        return tkDAO.getWorstSellers();
    }

    public ArrayList<thongketheoKhachHang_DTO> getThongKeTheoKhachHang(String sortBy){
        return tkDAO.getThongKeTheoKhachHang(sortBy);
    }
}
