package BLL;

import DTO.NhaCungCap_DTO;
import DAO.NhaCungCap_DAO;
import java.util.ArrayList;

public class NhaCungCap_BLL {
    NhaCungCap_DAO nccDAO = new NhaCungCap_DAO();

    public ArrayList<NhaCungCap_DTO> getAllNCC(){
        return nccDAO.getAllNCC();
    }

    public NhaCungCap_DTO getNCCFromID(String id){
        NhaCungCap_DTO ncc = nccDAO.getNCCfromID(id);
        if (ncc != null)
            return ncc;
        return null;
    }

    public String updateNCC(NhaCungCap_DTO ncc){
        if(!nccDAO.hasNCCID(ncc.getMaNCC())){
            return "nhà cung cấp không tồn tại!";
        }
        if(nccDAO.updateNCC(ncc)){
            return "Cập nhật nhà cung cấp thành công!";
        }
        return "Cập nhật nhà cung cấp thất bại!";
    }

    public String addNCC(NhaCungCap_DTO ncc){
        if(nccDAO.hasNCCID(ncc.getMaNCC())){
            return "nhà cung cấp đã tồn tại!";
        }
        if(nccDAO.addNCC(ncc)){
            return "Thêm nhà cung cấp thành công!";
        }
        return "Thêm nhà cung cấp thất bại!";
    }

    public String removeNCC(NhaCungCap_DTO ncc){
        if(!nccDAO.hasNCCID(ncc.getMaNCC())){
            return "nhà cung cấp không tồn tại!";
        }
        if(nccDAO.removeNCC(ncc.getMaNCC())){
            return "Xoá nhà cung cấp thành công!";
        }

        return "Xóa nhà cung cấp thất bại!";
    }
}
