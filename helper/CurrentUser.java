// File: helper/CurrentUser.java
package helper;

import DTO.KhachHang_DTO;
import DTO.NhanVien_DTO;

public class CurrentUser {
    public static NhanVien_DTO nhanVien = null;
    private static KhachHang_DTO khachHang = null;

    public static void setNhanVien(NhanVien_DTO nv) {
        nhanVien = nv;
        khachHang = null;
    }

    public static void setKhachHang(KhachHang_DTO kh) {
        khachHang = kh;
        nhanVien = null;
    }

    public static NhanVien_DTO getNhanVien() {
        return nhanVien;
    }

    public static KhachHang_DTO getKhachHang() {
        return khachHang;
    }

    public static boolean isNhanVien() {
        return nhanVien != null;
    }

    public static boolean isKhachHang() {
        return khachHang != null;
    }

    public static void logout() {
        nhanVien = null;
        khachHang = null;
    }

    
} 
