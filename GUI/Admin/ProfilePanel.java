package GUI.Admin;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DTO.NhanVien_DTO;
import helper.CurrentUser;

public class ProfilePanel extends JPanel {
    public NhanVien_DTO nv;

    public ProfilePanel() {
        setLayout(new GridLayout(6, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        nv = CurrentUser.nhanVien;

        if (nv != null) {
            add(new JLabel("Mã nhân viên: " + nv.getManv()));
            add(new JLabel("Họ tên: " + nv.getTennv()));
            add(new JLabel("Chức vụ: " + nv.getChucvu()));
            add(new JLabel("Số điện thoại: " + nv.getSdt()));
            add(new JLabel("Giới tính: " + nv.getGioitinh()));
            add(new JLabel("Địa chỉ: " + nv.getDiachi()));
        } else {
            add(new JLabel("Không tìm thấy thông tin nhân viên!"));
        }
    }
}
