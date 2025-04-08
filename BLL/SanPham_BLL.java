package BLL;

import DTO.*;
import DAO.*;
import java.util.ArrayList;

public class SanPham_BLL {
    Sach_DAO Sachdao = new Sach_DAO();
    Vo_DAO Vodao = new Vo_DAO();
    But_DAO Butdao = new But_DAO();
    private ArrayList<SanPham_DTO> danhsachsp;


    public ArrayList<SanPham_DTO> getAllSanPham(){
        danhsachsp = new ArrayList<>();

        ArrayList<Sach_DTO> SachArr = Sachdao.getAllSach();
        danhsachsp.addAll(SachArr);

        ArrayList<Vo_DTO> VoArr = Vodao.getAllVo();
        danhsachsp.addAll(VoArr);

        ArrayList<But_DTO> ButArr = Butdao.getAllBut();
        danhsachsp.addAll(ButArr);
        return danhsachsp;
    }

    public String addSP(SanPham_DTO sp){
        if(sp instanceof Sach_DTO){
            Sach_DTO sanpham = (Sach_DTO) sp;
            if(Sachdao.hasSachID(sanpham.getID_SanPham())){
                return "Sản phẩm đã tồn tại!";
            }
            if(Sachdao.addSach(sanpham)){
                return "Thêm Sách thành công!";
            }
        }
        else if(sp instanceof Vo_DTO){
            Vo_DTO sanpham = (Vo_DTO) sp;
            if(Vodao.hasVoID(sanpham.getID_SanPham())){
                return "Sản phẩm đã tồn tại!";
            }
            if(Vodao.addVo(sanpham)){
                return "Thêm Vở thành công!";
            }
        }
        else if(sp instanceof But_DTO){
            But_DTO sanpham = (But_DTO) sp;
            if(Butdao.hasButID(sanpham.getID_SanPham())){
                return "Sản phẩm đã tồn tại!";
            }
            if(Butdao.addBut(sanpham)){
                return "Thêm Bút thành công!";
            }     
        }
        return "Thêm Bút thất bại!";    
    }

    public String removeSP(SanPham_DTO sp){
        if(sp instanceof Sach_DTO){
            Sach_DTO sanpham = (Sach_DTO) sp;
            if(!Sachdao.hasSachID(sanpham.getID_SanPham())){
                return "Sản phẩm đã không tồn tại!";
            }
            if(Sachdao.removeSach(sanpham.getID_SanPham())){
                return "Xóa Sách thành công!";
            }
        }
        else if(sp instanceof Vo_DTO){
            Vo_DTO sanpham = (Vo_DTO) sp;
            if(!Vodao.hasVoID(sanpham.getID_SanPham())){
                return "Sản phẩm đã không tồn tại!";
            }
            if(Vodao.removeVo(sanpham.getID_SanPham())){
                return "Xóa Vở thành công!";
            }
        }
        else if(sp instanceof But_DTO){
            But_DTO sanpham = (But_DTO) sp;
            if(!Butdao.hasButID(sanpham.getID_SanPham())){
                return "Sản phẩm đã không tồn tại!";
            }
            if(Butdao.removeBut(sanpham.getID_SanPham())){
                return "Xóa Bút thành công!";
            }     
        }
        return "Xóa Bút thất bại!";    
    }


    
}
