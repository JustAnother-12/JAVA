package BLL;

import DTO.KhachHang_DTO;
import DAO.KhachHang_DAO;
import java.util.ArrayList;

public class KhachHang_BLL {
    KhachHang_DAO khachhangDao = new KhachHang_DAO();

    public ArrayList<KhachHang_DTO> getAllKhachHang(){
        return khachhangDao.getAllKhachHang();
    }

    public KhachHang_DTO getKhachHangFromAccount(String username, String password){
        KhachHang_DTO kh = khachhangDao.getKhachHangFromAccount(username, password);
        if (kh != null)
            return kh;
        return null;
    }

    public String addKhachHang(KhachHang_DTO kh){
        if(khachhangDao.hasKhachHangID(kh.getId_KhachHang())){
            return "khách hàng đã tồn tại!";
        }
        if(khachhangDao.hasKhachHangUsername(kh.getUsername())){
            return "Username đã tồn tại!";
        }
        if(khachhangDao.addKhachHang(kh)){
            return "Thêm khách hàng thành công!";
        }
        return "Thêm khách hàng thất bại!";
    }

    public String removeKhachHang(KhachHang_DTO kh){
        if(!khachhangDao.hasKhachHangID(kh.getId_KhachHang())){
            return "khách hàng không tồn tại!";
        }
        if(khachhangDao.removeKhachHang(kh.getId_KhachHang())){
            return "Xoá khách hàng thành công!";
        }

        return "Xóa Khách hàng thất bại!";
    }
}
