/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Beans/Customizer.java to edit this template
 */
package com.raven.account;

import com.raven.account.database.DatabaseConnection;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;
import javax.swing.table.TableRowSorter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import com.raven.account.customer;
import com.raven.account.staff;
import com.raven.component.Header;
/**
 *
 * @author ADMIN
 */
public class AccountTable extends javax.swing.JPanel implements Header.searchListener{
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Object> accountList = new ArrayList<>();
    public AccountTable() {
        initComponents();
        setLayout(null);
        setBounds(0,0, 800, 650);
        setBackground(Color.WHITE);
        this.setName("staff");
        // Nút "Thêm tài khoản" ở góc trên bên phải
        JButton btnAdd = new JButton("Thêm tài khoản");
        btnAdd.setBounds(620, 5, 150, 30);
        add(btnAdd);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddAccount(tableModel); // Mở form thêm nhân viên
            }
        });
        // Nút sắp xếp tăng
        JButton btnSortAsc = new JButton("Sắp xếp Tăng");
        btnSortAsc.setBounds(20, 0, 150, 30);
        add(btnSortAsc);

        // Nút sắp xếp giảm
        JButton btnSortDesc = new JButton("Sắp xếp Giảm");
        btnSortDesc.setBounds(200, 0, 150, 30);
        add(btnSortDesc);
        
        btnSortAsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTable(true); // Sắp xếp tăng
            }
        });

        btnSortDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortTable(false); // Sắp xếp giảm
            }
        });
        // Cấu hình bảng
        String[] columnNames = {"ID", "Họ tên", "Username", "Số Điện thoại", "Tác vụ"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);

        // Căn giữa dữ liệu trong bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Thiết lập renderer cho từng cột
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Thêm dữ liệu mẫu
        loadDataFormDatabase();
        // Cột "Tác vụ" có 2 nút "Chi tiết" và "Xóa"
        table.getColumn("Tác vụ").setCellRenderer(new ButtonRenderer("staff"));
        table.getColumn("Tác vụ").setCellEditor(new ButtonEditor(this));

        // ScrollPane chứa bảng
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(2, 40 , 770, 570);
        add(scrollPane);
        revalidate();
        repaint();
    }
    public void searchById(String text) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null); // Hiện toàn bộ
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text.trim(), 0)); // Theo cột ID
        }
    }

    public void filterByRole(String role) {
        tableModel.setRowCount(0); // Clear bảng
        accountList.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = role.equals("Tất cả")
                ? "SELECT * FROM NHANVIEN"
                : "SELECT * FROM NHANVIEN WHERE chucvu = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            if (!role.equals("Tất cả")) {
                pstmt.setString(1, role);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("manv");
                String name = rs.getString("tennv");
                String username = rs.getString("username");
                String phone = rs.getString("sdt");
                staff nv = new staff(id, name, username, phone);
                accountList.add(nv);
                tableModel.addRow(new Object[]{id, name, username, phone, "Chi tiết" + "Xoá"});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadDataFormDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
                String queryfors = "SELECT * FROM NHANVIEN";
                PreparedStatement pstmt = conn.prepareStatement(queryfors);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    String id = rs.getString("manv");
                    String name = rs.getString("tennv");
                    String username = rs.getString("username");
                    String phone = rs.getString("sdt");
                    staff nv = new staff(id, name, username, phone);
                    accountList.add(nv);
                    tableModel.addRow(new Object[]{id, name, username, phone, "Chi tiết" + "Xoá"});
                }
        } catch (Exception e) {}
    }
    @Override
    public void setName(String name) {
        super.setName(name); 
    }

    @Override
    public String getName() {
        return super.getName(); 
    }
    private void sortTable(boolean ascending) {
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
    public ArrayList<Object> getAccountList() {
        return this.accountList;
    }
    public void deleteAccount(String id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
                String checkQuery = "SELECT COUNT(*) FROM NHANVIEN WHERE manv = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, id);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Nếu ID là của nhân viên
                    String deleteQuery = "DELETE FROM NHANVIEN WHERE manv = ?";
                    PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                    pstmt.setString(1, id);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Xóa tài khoản nhân viên thành công!");
                } else {
                    // Nếu không tìm thấy ID trong cả hai bảng
                    JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản với ID: " + id, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            // Cập nhật lại bảng
            loadDataFormDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa tài khoản: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setName(""); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void onSearch(String text) {
        searchById(text);
    }

    @Override
    public void onFilterByRole(String role) {
        filterByRole(role);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
