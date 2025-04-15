/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raven.order;

/**
 *
 * @author ADMIN
 */
public class order {
    private String madonhang;
    private String diachidat;
    private String ngaydat;
    private String tinhtrang;
    private double tongtien;
    private String manv;
    private String makh;

    public order() {
    }

    public order(String madonhang, String diachidat, String ngaydat, String tinhtrang,
                 double tongtien, String manv, String makh) {
        this.madonhang = madonhang;
        this.diachidat = diachidat;
        this.ngaydat = ngaydat;
        this.tinhtrang = tinhtrang;
        this.tongtien = tongtien;
        this.manv = manv;
        this.makh = makh;
    }

    public String getMadonhang() {
        return madonhang;
    }

    public void setMadonhang(String madonhang) {
        this.madonhang = madonhang;
    }

    public String getDiachidat() {
        return diachidat;
    }

    public void setDiachidat(String diachidat) {
        this.diachidat = diachidat;
    }

    public String getNgaydat() {
        return ngaydat;
    }

    public void setNgaydat(String ngaydat) {
        this.ngaydat = ngaydat;
    }

    public String getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(String tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public double getTongtien() {
        return tongtien;
    }

    public void setTongtien(double tongtien) {
        this.tongtien = tongtien;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getMakh() {
        return makh;
    }

    public void setMakh(String makh) {
        this.makh = makh;
    }
}
