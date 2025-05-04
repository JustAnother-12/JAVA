package BLL;

import DTO.NhaCungCap_DTO;
import DAO.NhaCungCap_DAO;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class NhaCungCap_BLL {
    NhaCungCap_DAO nccDAO = new NhaCungCap_DAO();

    public ArrayList<NhaCungCap_DTO> getAllNCC(){
        return nccDAO.getAllNCC();
    }

    public String getLastestNCCID(){
        return nccDAO.getLastestNCCID();
    }

    public void LoadDataToTabel(DefaultTableModel model, ArrayList<NhaCungCap_DTO> list) {
        list.clear();
        list.addAll(getAllNCC());
        model.setRowCount(0); // Xóa dữ liệu cũ

        for (NhaCungCap_DTO ncc : list) {
            model.addRow(new Object[]{
                ncc.getMaNCC(),
                ncc.getTenNCC(),
                ncc.getSdtNCC(),
                ncc.getEmailNCC(),
                "Chi tiết / Xóa"
            });
        }
    }

    public boolean themNCC(NhaCungCap_DTO ncc) {
        return nccDAO.insertNCC(ncc);
    }

    public void deleteSupplier(String id, DefaultTableModel model, ArrayList<NhaCungCap_DTO> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMaNCC().equals(id)) {
                nccDAO.deleteNCC(id);
                break;
            }
        }
    }    

    public NhaCungCap_DTO getNCCFromID(String id){
        NhaCungCap_DTO ncc = nccDAO.getNCCfromID(id);
        if (ncc != null)
            return ncc;
        return null;
    }
    public boolean capNhatNCC(NhaCungCap_DTO ncc) {
    NhaCungCap_DAO dao = new NhaCungCap_DAO();
    return dao.updateNCC(ncc);
    }

    // public String updateNCC(NhaCungCap_DTO ncc){
    //     if(!nccDAO.hasNCCID(ncc.getMaNCC())){
    //         return "nhà cung cấp không tồn tại!";
    //     }
    //     if(nccDAO.updateNCC(ncc)){
    //         return "Cập nhật nhà cung cấp thành công!";
    //     }
    //     return "Cập nhật nhà cung cấp thất bại!";
    // }

    // public String addNCC(NhaCungCap_DTO ncc){
    //     if(nccDAO.hasNCCID(ncc.getMaNCC())){
    //         return "nhà cung cấp đã tồn tại!";
    //     }
    //     if(nccDAO.addNCC(ncc)){
    //         return "Thêm nhà cung cấp thành công!";
    //     }
    //     return "Thêm nhà cung cấp thất bại!";
    // }

    // public String removeNCC(NhaCungCap_DTO ncc){
    //     if(!nccDAO.hasNCCID(ncc.getMaNCC())){
    //         return "nhà cung cấp không tồn tại!";
    //     }
    //     if(nccDAO.removeNCC(ncc.getMaNCC())){
    //         return "Xoá nhà cung cấp thành công!";
    //     }

    //     return "Xóa nhà cung cấp thất bại!";
    // }
}
