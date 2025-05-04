package GUI.Admin.swing;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import DTO.Order_DTO;
import java.awt.*;
import java.util.ArrayList;

public class NutGiaoDien_BLL extends JPanel implements TableCellRenderer {
    private String formType;
    private ArrayList<Order_DTO> orderList;
    public NutGiaoDien_BLL(String formType) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setOpaque(true);
        this.formType = formType;
    }
    public NutGiaoDien_BLL(String formType,ArrayList<Order_DTO> orderList) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        setOpaque(true);
        this.formType = formType;
        this.orderList = orderList;
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        removeAll();
        JButton btnDetail = new JButton("Chi tiết");
        JButton btnDelete = new JButton("Xóa");
        JButton btnConfirm = new JButton("Xử lý");
        if ("customer".equals(formType) || "staff".equals(formType)) {
            add(btnDetail);
            add(btnDelete);
        } else if ("order".equals(formType)) {
            if(orderList != null && row < orderList.size()) {
                Order_DTO temp = orderList.get(row);
                if("Chưa xử lý".equalsIgnoreCase(temp.getTinhtrang())) 
                add(btnConfirm);    
            }
            add(btnDetail);
            add(btnDelete);
        }
        revalidate();
        repaint();
        return this;
    }
}
