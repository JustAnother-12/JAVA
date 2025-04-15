package BLL;

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import DTO.NhanVien_DTO;

public class NhanVien_BLL {
    static void searchById(String text,DefaultTableModel tableModel,JTable table) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null); // Hiện toàn bộ
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 0)); // Theo cột ID
        }
    }
    // @Override
    // public void setName(String name) {
    //     super.setName(name); 
    // }

    // @Override
    // public String getName() {
    //     return super.getName(); 
    // }
}
