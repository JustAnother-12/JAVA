package BLL;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import DAO.Order_DAO;
import DTO.OrderDetail_DTO;
import DTO.Order_DTO;

public class DonHang_BLL {
    public void LoadDataToTabel(DefaultTableModel tableModel, ArrayList<Order_DTO> orderList,ArrayList<OrderDetail_DTO> orderDetailList) {
        Order_DAO order_DAO = new Order_DAO();
        order_DAO.loadDataFormDatabase(tableModel,orderList,orderDetailList);
    }
}
