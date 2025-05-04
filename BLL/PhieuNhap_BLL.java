package BLL;

import DTO.PhieuNhap_DTO;
import DTO.ChiTietPhieuNhap_DTO;
import DAO.NhaCungCap_DAO;
import DAO.NhanVien_DAO;
import DAO.PhieuNhap_DAO;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;


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
    
    public void loadToTable(DefaultTableModel model, ArrayList<PhieuNhap_DTO> list) {
        list.clear();
        list.addAll(pnDAO.getAllPhieuNhap());
        NhanVien_DAO nvDAO = new NhanVien_DAO();
        NhaCungCap_DAO nccDAO = new NhaCungCap_DAO();
        model.setRowCount(0);
        for (PhieuNhap_DTO pn : list) {
            String tenNV = nvDAO.getTenNhanVien(pn.getMaNV());
            String tenNCC = nccDAO.getTenNCC(pn.getMaNCC());
            model.addRow(new Object[]{
                pn.getMaPN(),
                tenNCC,
                tenNV,
                pn.getNgayNhap(),
                "Chi tiết | Sửa | Xoá"
            });
        }
    }
    public void deleteImport(String id, DefaultTableModel model, ArrayList<PhieuNhap_DTO> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaPN().equals(id)) {
                pnDAO.removePhieuNhap(id); 
                break;
            }
        }
    }
}
