/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.account.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;
/**
 *
 * @author ADMIN
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vanphongpham"; // Địa chỉ cơ sở dữ liệu
    private static final String USER = "root"; // Tên người dùng
    private static final String PASS = "";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,USER,PASS);
    }
    public static ArrayList<String> getAllRoles() {
    ArrayList<String> chucvu = new ArrayList<>();
    try (Connection conn = getConnection()) {
        String query = "SELECT DISTINCT chucvu FROM NHANVIEN";
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            chucvu.add(rs.getString("chucvu"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return chucvu;
}
}
