package BLL;

import DTO.PhieuNhap_DTO;
import DTO.ChiTietPhieuNhap_DTO;
import DAO.PhieuNhap_DAO;
import java.util.ArrayList;


public class PhieuNhap_BLL {
    PhieuNhap_DAO pnDAO = new PhieuNhap_DAO();
    
    public ArrayList<PhieuNhap_DTO> getAllPhieuNhap(){
        return pnDAO.getAllPhieuNhap();
    }

    public ArrayList<ChiTietPhieuNhap_DTO> getAllChiTiet(){
        return pnDAO.getAllChiTiet();
    }

    public String addPhieuNhap(PhieuNhap_DTO pn){
        if(pnDAO.hasPhieuNhapID(pn.getMaPN())){
            return "Phiếu nhập đã tồn tại!";
        }
        if(pnDAO.addPhieuNhap(pn)){
            return "Thêm phiếu nhập thành công!";
        }
        return "Thêm phiếu nhập thất bại!";
    }

    public String removeCTPhieuNhapBySP(String id){
        if(!pnDAO.hasCTPhieuNhapID(id)){
            return "Chi tiết phiếu nhập không tồn tại!";
        }
        if(pnDAO.removeCTPhieuNhapBySP(id)){
            return "Xóa chi tiết phiếu nhập thành công!";
        }
        return "Xóa chi tiết phiếu nhập thất bại!";
    }
}
