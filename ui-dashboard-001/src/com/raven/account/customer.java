/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.account;

/**
 *
 * @author ADMIN
 */
public class customer {
    private String makh;
    private String tenkh;
    private String sdt;
    private String gioi;
    private String email;
    private String ngaysinh;
    private String diachi;
    private String username;
    private String password;

    // Phương thức khởi tạo không tham số
    public customer() {
    }

    // Phương thức khởi tạo có tham số
    public customer(String makh, String tenkh, String sdt, String gioi, String email, 
                     String ngaysinh, String diachi, String username, String password) {
        this.makh = makh;
        this.tenkh = tenkh;
        this.sdt = sdt;
        this.gioi = gioi;
        this.email = email;
        this.ngaysinh = ngaysinh;
        this.diachi = diachi;
        this.username = username;
        this.password = password;
    }
    public customer(String makh, String tenkh, String username, String sdt) {
        this.makh = makh;
        this.tenkh = tenkh;
        this.sdt = sdt;
        this.username = username;
    }
    // Getter và Setter
    public String getMakh() {
        return makh;
    }

    public void setMakh(String makh) {
        this.makh = makh;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getGioi() {
        return gioi;
    }

    public void setGioi(String gioi) {
        this.gioi = gioi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
