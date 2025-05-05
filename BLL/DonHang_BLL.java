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
import DTO.SanPham_DTO;

public class DonHang_BLL {
    Order_DAO orderDao = new Order_DAO();
    SanPham_BLL spBLL = new SanPham_BLL();


    public void LoadDataToTabel(DefaultTableModel tableModel, ArrayList<Order_DTO> orderList,ArrayList<OrderDetail_DTO> orderDetailList) {
        try {
            Order_DAO order_DAO = new Order_DAO();
            order_DAO.loadDataFormDatabase(tableModel,orderList,orderDetailList);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public ArrayList<Order_DTO> getAllOrder(){
        return orderDao.getAllOrder();
    }

    public ArrayList<OrderDetail_DTO> getDetails(String id){
        return orderDao.getDetailForOrder(id);
    }

    public ArrayList<OrderDetail_DTO> getAllDetail(){
        return orderDao.getAllDetail();
    }

    public String addOrder(Order_DTO order, ArrayList<OrderDetail_DTO> details){
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

    public String cancelOrder(String id, String nvid){
        if(!orderDao.hasOrderID(id)){
            return "Đơn hàng không tồn tại!";
        }
        if(orderDao.CancelOrder(id, nvid)){
            return "Hủy đơn hàng thành công!";
        }

        return "Hủy đơn hàng thất bại!";
    }

    public String confirmOrder(String id, String nvid){
        if(!orderDao.hasOrderID(id)){
            return "Đơn hàng không tồn tại!";
        }
        if(orderDao.ConfirmOrder(id, nvid)){
            ArrayList<OrderDetail_DTO> details = getDetails(id);
            for (OrderDetail_DTO detail : details){
                String idsp = detail.getMasp();
                SanPham_DTO sp = null;
                if (idsp.contains("S")){
                    sp = spBLL.getSachFromID(idsp);
                }else if (idsp.contains("V")){
                    sp = spBLL.getVoFromID(idsp);
                }else if (idsp.contains("B")){
                    sp = spBLL.getButFromID(idsp);
                }
                System.out.println(sp.getTen_SanPham());
                if(sp != null){
                    sp.setSoLuong_SanPham(sp.getSoLuong_SanPham() - detail.getSoluong()); // Giảm số lượng sau khi xác nhận
                    spBLL.updateSP(sp);
                }
            }
            return "Duyệt đơn hàng thành công!";
        }

        return "Duyệt đơn hàng thất bại!";
    }
    
    public boolean DeleteOrder(String id) throws SQLException {
        try {
            Order_DAO temp = new Order_DAO();
            boolean result =  temp.DeleteOrder(id);
            if (result == true) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            Logger.getLogger(NhanVien_DAO.class.getName()).log(Level.SEVERE, "General Error deleting staff", ex);
        }
        return false;
    }
    public String getCustomerName(String makh) {
        try {
            Order_DAO temp = new Order_DAO();
            return temp.getCustomerName(makh);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public String getEmployeeInfo(String manv) {
        try {
            Order_DAO temp = new Order_DAO();
            return temp.getEmployeeInfo(manv);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public String getProductName(String masp) {
        try {
            Order_DAO temp = new Order_DAO();
            return temp.getProductName(masp);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
