package BLL;
import DTO.*;
import DAO.*;
import java.util.ArrayList;

public class Order_BLL {
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
    
}
