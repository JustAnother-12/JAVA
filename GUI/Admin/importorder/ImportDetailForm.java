package GUI.Admin.importorder;

import DTO.PhieuNhap_DTO;
import DTO.ChiTietPhieuNhap_DTO;
import DTO.SanPham_DTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ImportDetailForm extends JDialog {
    public ImportDetailForm(PhieuNhap_DTO phieuNhapDTO) {
        setTitle("Chi tiết phiếu nhập");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setModal(true);

        JPanel panel = new JPanel(new BorderLayout());

        // Hiển thị thông tin chung
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setText("Mã phiếu: " + phieuNhapDTO.getMaPN() + "\n"
                       + "Mã nhà cung cấp: " + phieuNhapDTO.getMaNCC() + "\n"
                       + "Mã nhân viên: " + phieuNhapDTO.getMaNV() + "\n"
                       + "Ngày nhập: " + phieuNhapDTO.getNgayNhap() + "\n");

        panel.add(infoArea, BorderLayout.NORTH);

        // Hiển thị bảng chi tiết
        String[] columnNames = {"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"};
        ArrayList<ChiTietPhieuNhap_DTO> list = phieuNhapDTO.getChiTietPhieuNhap();
        Object[][] data = new Object[list.size()][5];

        for (int i = 0; i < list.size(); i++) {
            ChiTietPhieuNhap_DTO item = list.get(i);
            SanPham_DTO sp = item.getThongtinSP();

            data[i][0] = sp.getID_SanPham();
            data[i][1] = sp.getTen_SanPham();
            data[i][2] = item.getSoluongNhap();
            data[i][3] = item.getDongiaNhap();
            data[i][4] = item.getSoluongNhap() * item.getDongiaNhap();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }
}

