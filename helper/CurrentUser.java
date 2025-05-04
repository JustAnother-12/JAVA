package helper;

import DTO.NhanVien_DTO;

public class CurrentUser {
    public static NhanVien_DTO nhanVien = null;
    public static void setNhanVien(NhanVien_DTO nv) {
        nhanVien = nv;
    }

    public static NhanVien_DTO getNhanVien() {
        return nhanVien;
    }


    public static boolean isNhanVien() {
        return nhanVien != null;
    }

    public static void logout() {
        nhanVien = null;
    }

    
} 
