package BLL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import DAO.NhanVien_DAO;
import DAO.Order_DAO;
import DTO.OrderDetail_DTO;
import DTO.Order_DTO;

public class DonHang_BLL {
    public void LoadDataToTabel(DefaultTableModel tableModel, ArrayList<Order_DTO> orderList,ArrayList<OrderDetail_DTO> orderDetailList) {
        Order_DAO order_DAO = new Order_DAO();
        order_DAO.loadDataFormDatabase(tableModel,orderList,orderDetailList);
    }
    Order_DAO orderDao = new Order_DAO();

    public ArrayList<Order_DTO> getAllOrder(){
        return orderDao.getAllOrder();
    }

    public OrderDetail_DTO getDetails(String id){
        return orderDao.getDetailForOrder(id);
    }

    public String addOrder(Order_DTO order, OrderDetail_DTO details){
        if(orderDao.hasOrderID(order.getMadonhang())){
            return "Đơn hàng đã tồn tại!";
        }
        if(orderDao.AddOrder(order, details)){
            return "Thêm đơn hàng thành công!";
        }
        return "Thêm đơn hàng thất bại!";
    }
    public String removeDetailBySP(String id){
        if(!orderDao.hasDetailID(id)){
            return "Chi tiết đơn hàng không tồn tại!";
        }
        if(orderDao.removeDetailBySP(id)){
            return "Xoá chi tiết đơn hàng thành công!";
        }

        return "Xóa chi tiết đơn hàng thất bại!";
    }
    public String removeOrder(String id){
        if(!orderDao.hasOrderID(id)){
            return "Đơn hàng không tồn tại!";
        }
        if(orderDao.DeleteOrder(id)){
            return "Xoá đơn hàng thành công!";
        }

        return "Xóa đơn hàng thất bại!";
    }

    public String confirmOrder(String id){
        if(!orderDao.hasOrderID(id)){
            return "Đơn hàng không tồn tại!";
        }
        if(orderDao.ConfirmOrder(id)){
            return "Duyệt đơn hàng thành công!";
        }

        return "Duyệt đơn hàng thất bại!";
    }
    public void DeleteOrder(String id) throws SQLException {
        try {
            Order_DAO temp = new Order_DAO();
            temp.DeleteOrder(id);
        } catch (Exception ex) {
            Logger.getLogger(NhanVien_DAO.class.getName()).log(Level.SEVERE, "General Error deleting staff", ex);
        }
    }
    
}
