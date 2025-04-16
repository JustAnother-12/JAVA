package BLL;

import java.util.Collections;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Customer_BLL {
    public void searchById(String text,DefaultTableModel tableModel,JTable table) {
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
    public void sortTable(boolean ascending,DefaultTableModel tableModel,JTable table) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (ascending) {
            sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
        }
        else {
            sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0 , SortOrder.DESCENDING)));
        }
        sorter.sort();
    }
}
