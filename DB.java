/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prodma;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class DB {
    public static String servername = "127.0.0.1"; 
    public static String username = "root"; 
    public static String dbname = "product_management";
    public static Integer portnumber = 3307 ; 
    public static String password = "1234567890123wQ";
    
    
    
    public static Connection getConnection(){
     MysqlDataSource datasource = new MysqlDataSource();
     datasource.setServerName(servername);
     datasource.setUser(username);
     datasource.setDatabaseName(dbname);
     datasource.setPortNumber(portnumber);
     datasource.setPassword(password);
     
     try {
         return datasource.getConnection();
     } catch (SQLException ex){
         Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
         return null;
     }
    }
    
}

