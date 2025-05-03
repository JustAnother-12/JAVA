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
                list.remove(i);
                model.removeRow(i);
                nccDAO.deleteNCC(id); // gọi DAO để xóa trong CSDL
                break;
            }
        }
    }    
}
