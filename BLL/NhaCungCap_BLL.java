package BLL;

import DTO.NhaCungCap_DTO;
import DAO.NhaCungCap_DAO;
import java.util.ArrayList;

public class NhaCungCap_BLL {
    NhaCungCap_DAO nccDAO = new NhaCungCap_DAO();

    public ArrayList<NhaCungCap_DTO> getAllNCC(){
        return nccDAO.getAllNCC();
    }
}
