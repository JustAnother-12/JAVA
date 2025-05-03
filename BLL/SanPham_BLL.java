package BLL;

import DTO.*;
import DAO.*;
import java.util.ArrayList;

public class SanPham_BLL {
    Sach_DAO Sachdao = new Sach_DAO();
    Vo_DAO Vodao = new Vo_DAO();
    But_DAO Butdao = new But_DAO();
    private ArrayList<SanPham_DTO> danhsachsp;


    public Sach_DTO getSachFromID(String id){
        for(Sach_DTO sach:getAllSach()){
            if(sach.getID_SanPham().equals(id)){
                return sach;
            }
        }
        return null;
    }

    public Vo_DTO getVoFromID(String id){
        for(Vo_DTO Vo:getAllVo()){
            if(Vo.getID_SanPham().equals(id)){
                return Vo;
            }
        }
        return null;
    }

    public But_DTO getButFromID(String id){
        for(But_DTO But:getAllBut()){
            if(But.getID_SanPham().equals(id)){
                return But;
            }
        }
        return null;
    }

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

    public ArrayList<Sach_DTO> getAllSach(){
        return Sachdao.getAllSach();
    }

    public ArrayList<Vo_DTO> getAllVo(){
        return Vodao.getAllVo();
    }

    public ArrayList<But_DTO> getAllBut(){
        return Butdao.getAllBut();
    }

    public String getLatestSACHID(){
        return Sachdao.getLastestSACHID();
    }

    public String getLatestVOID(){
        return Vodao.getLastestVOID();
    }

    public String getLatestBUTID(){
        return Butdao.getLastestBUTID();
    }

    public String updateSP(SanPham_DTO sp){
        if(sp instanceof Sach_DTO){
            Sach_DTO sanpham = (Sach_DTO) sp;
            if(!Sachdao.hasSachID(sanpham.getID_SanPham())){
                return "Sản phẩm không tồn tại!";
            }
            if(Sachdao.updateSach(sanpham)){
                return "Cập nhật Sách thành công!";
            }
        }
        else if(sp instanceof Vo_DTO){
            Vo_DTO sanpham = (Vo_DTO) sp;
            if(!Vodao.hasVoID(sanpham.getID_SanPham())){
                return "Sản phẩm không tồn tại!";
            }
            if(Vodao.updateVo(sanpham)){
                return "Cập nhật Vở thành công!";
            }
        }
        else if(sp instanceof But_DTO){
            But_DTO sanpham = (But_DTO) sp;
            if(!Butdao.hasButID(sanpham.getID_SanPham())){
                return "Sản phẩm không tồn tại!";
            }
            if(Butdao.updateBut(sanpham)){
                return "Cập nhật Bút thành công!";
            }     
        }
        return "Cập nhật Sản phẩm thất bại!";    
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
        return "Thêm Sản phẩm thất bại!";    
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
        return "Xóa Sản phẩm thất bại!";    
    }


    
}
