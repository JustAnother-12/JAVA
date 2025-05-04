package GUI.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import BLL.SanPham_BLL;
import DTO.But_DTO;
import DTO.Product_Item_DTO;
import DTO.Sach_DTO;
import DTO.SanPham_DTO;
import DTO.Vo_DTO;
import utils.MyScrollBarUI;
import utils.WrapLayout;

public class ProductPanel extends JScrollPane {
    private JPanel ItemPanel;
    private ArrayList<ProductItem> ProductList;
    private ArrayList<Sach_DTO> SachDescriptionList;
    private ArrayList<Vo_DTO> VoDescriptionList;
    private ArrayList<But_DTO> ButDescriptionList;
    private SachDescription CurrentSachDescription;
    private VoDescription CurrentVoDescription;
    private ButDescription CurrentButDescription;
    protected SanPham_BLL spBLL;

    public ProductPanel(String typename, String searchtext, FilterPanel filter) { 
        initComponents();
        if (typename.equals("ALL")){
            getAllSP(filter);
        }
        else if(typename.equals("SACH")){
            getAllSach();
        } 
        else if(typename.equals("VO")){
            getAllVo();
        }
        else if(typename.equals("BUT")){
            getAllBut();
        }
        else if(typename.equals("SEARCH")){
            getSearchedItem(searchtext, filter);
        }
        addEvent();
    }

    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(CurrentSachDescription != null)
                CurrentSachDescription.dispose();
            else if(CurrentVoDescription != null)
                CurrentVoDescription.dispose();
            else if(CurrentButDescription != null)
                CurrentButDescription.dispose();
            ProductItem item = (ProductItem) e.getSource();
            if (item.getID().contains("S")){
                for(Sach_DTO description : SachDescriptionList){
                    if(description.getTen_SanPham().equals(item.getName())){
                        CurrentSachDescription = new SachDescription(description);
                    }
                }
            }
            else if(item.getID().contains("V")){
                for(Vo_DTO description : VoDescriptionList){
                    if(description.getTen_SanPham().equals(item.getName())){
                        CurrentVoDescription = new VoDescription(description);
                    }
                }
            }
            else{
                for(But_DTO description : ButDescriptionList){
                    if(description.getTen_SanPham().equals(item.getName())){
                        CurrentButDescription = new ButDescription(description);
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    };

    private void getAllSach(){
        SachDescriptionList = new ArrayList<>();
        VoDescriptionList = new ArrayList<>();
        ButDescriptionList = new ArrayList<>();
        ProductList = new ArrayList<>();
        for(Sach_DTO sach : spBLL.getAllSach()){
            String id = sach.getID_SanPham();
            String ten = sach.getTen_SanPham();
            double gia = sach.getGia_SanPham();

        ProductItem item = new ProductItem(
            new Product_Item_DTO(id, ten, id, String.valueOf(new BigDecimal(gia)))
        );
            ProductList.add(item);
            ItemPanel.add(item);
            SachDescriptionList.add(sach);
        }
    }

    private void getAllVo(){
        SachDescriptionList = new ArrayList<>();
        VoDescriptionList = new ArrayList<>();
        ButDescriptionList = new ArrayList<>();
        ProductList = new ArrayList<>();
        for(Vo_DTO Vo : spBLL.getAllVo()){
            String id = Vo.getID_SanPham();
            String ten = Vo.getTen_SanPham();
            double gia = Vo.getGia_SanPham();

        ProductItem item = new ProductItem(
            new Product_Item_DTO(id, ten, id, String.valueOf(new BigDecimal(gia)))
        );
            ProductList.add(item);
            ItemPanel.add(item);
            VoDescriptionList.add(Vo);
        }
    }

    private void getAllBut(){
        SachDescriptionList = new ArrayList<>();
        VoDescriptionList = new ArrayList<>();
        ButDescriptionList = new ArrayList<>();
        ProductList = new ArrayList<>();
        for(But_DTO But : spBLL.getAllBut()){
            String id = But.getID_SanPham();
            String ten = But.getTen_SanPham();
            double gia = But.getGia_SanPham();

        ProductItem item = new ProductItem(
            new Product_Item_DTO(id, ten, id, String.valueOf(new BigDecimal(gia)))
        );
            ProductList.add(item);
            ItemPanel.add(item);
            ButDescriptionList.add(But);
        }
    }

    private void getAllSP(FilterPanel filter){
        SachDescriptionList = new ArrayList<>();
        VoDescriptionList = new ArrayList<>();
        ButDescriptionList = new ArrayList<>();
        ProductList = new ArrayList<>();
        for(SanPham_DTO sp : spBLL.getAllSanPham()){
            String id = null;
            String ten = null;
            double gia = 0.0;
            if (filter == null){
                id = sp.getID_SanPham();
                ten = sp.getTen_SanPham();
                gia = sp.getGia_SanPham();

            ProductItem item = new ProductItem(
            new Product_Item_DTO(id, ten, id, String.valueOf(new BigDecimal(gia)))
        );
                ProductList.add(item);
                ItemPanel.add(item);

                if (sp instanceof Sach_DTO){
                    Sach_DTO sach = (Sach_DTO)sp;
                    SachDescriptionList.add(sach);
                }
                else if (sp instanceof Vo_DTO){
                    Vo_DTO vo = (Vo_DTO)sp;
                    VoDescriptionList.add(vo);
                }
                else{
                    But_DTO but = (But_DTO)sp;
                    ButDescriptionList.add(but);
                }
            }
            else{
                SanPham_DTO sph = filter.Filter(sp);
                    if(sph != null){
                        id = sph.getID_SanPham();
                        ten = sph.getTen_SanPham();
                        gia = sph.getGia_SanPham();

                    ProductItem item = new ProductItem(
            new Product_Item_DTO(id, ten, id, String.valueOf(new BigDecimal(gia)))
        );
                        ProductList.add(item);
                        ItemPanel.add(item);

                        if (sph instanceof Sach_DTO){
                            Sach_DTO sach = (Sach_DTO)sph;
                            SachDescriptionList.add(sach);
                        }
                        else if (sph instanceof Vo_DTO){
                            Vo_DTO vo = (Vo_DTO)sph;
                            VoDescriptionList.add(vo);
                        }
                        else{
                            But_DTO but = (But_DTO)sph;
                            ButDescriptionList.add(but);
                        }
                    }
            }
        }
    }

    private void getSearchedItem(String searchtext, FilterPanel Filter){
        SachDescriptionList = new ArrayList<>();
        VoDescriptionList = new ArrayList<>();
        ButDescriptionList = new ArrayList<>();
        ProductList = new ArrayList<>();
        for (SanPham_DTO sp : spBLL.getAllSanPham()){
            if(sp.getTen_SanPham().toLowerCase().contains(searchtext.toLowerCase())){
                String id = null;
                String ten = null;
                double gia = 0.0;
                if(Filter == null){
                    id = sp.getID_SanPham();
                    ten = sp.getTen_SanPham();
                    gia = sp.getGia_SanPham();

                ProductItem item = new ProductItem(
            new Product_Item_DTO(id, ten, id, String.valueOf(new BigDecimal(gia)))
        );
                    ProductList.add(item);
                    ItemPanel.add(item);

                    if (sp instanceof Sach_DTO){
                        Sach_DTO sach = (Sach_DTO)sp;
                        SachDescriptionList.add(sach);
                    }
                    else if (sp instanceof Vo_DTO){
                        Vo_DTO vo = (Vo_DTO)sp;
                        VoDescriptionList.add(vo);
                    }
                    else{
                        But_DTO but = (But_DTO)sp;
                        ButDescriptionList.add(but);
                    }
                }
                else{
                    SanPham_DTO sph = Filter.Filter(sp);
                    if(sph != null){
                        id = sph.getID_SanPham();
                        ten = sph.getTen_SanPham();
                        gia = sph.getGia_SanPham();

                    ProductItem item = new ProductItem(
            new Product_Item_DTO(id, ten, id, String.valueOf(new BigDecimal(gia)))
        );
                        ProductList.add(item);
                        ItemPanel.add(item);

                        if (sph instanceof Sach_DTO){
                            Sach_DTO sach = (Sach_DTO)sph;
                            SachDescriptionList.add(sach);
                        }
                        else if (sph instanceof Vo_DTO){
                            Vo_DTO vo = (Vo_DTO)sph;
                            VoDescriptionList.add(vo);
                        }
                        else{
                            But_DTO but = (But_DTO)sph;
                            ButDescriptionList.add(but);
                        }
                    }
                }

                
            }
        }
    }

    private void addEvent(){
        for(ProductItem item : ProductList) {
            item.addMouseListener(mouseListener);
        
            item.getAddCartButton().addActionListener(e -> {
                if (HeaderPanel.khachhang == null) {
                    JOptionPane.showMessageDialog(null, "Bạn cần đăng nhập để thêm vào giỏ hàng.");
                    return;
                }
        
                DTO.CartItemDTO cartItem = new DTO.CartItemDTO(
                    item.getID(),
                    item.getName(),
                    item.getID(), // sửa tại đây để lấy đúng tên ảnh
                    1,
                    new BigDecimal(item.PriceTaglb.getText())
                );
                
        
                BLL.Cart_BLL.themVaoGio(cartItem);
                JOptionPane.showMessageDialog(null, "Đã thêm \"" + item.getName() + "\" vào giỏ hàng!");
            });
        }
        
    }

    private void initComponents() {
        spBLL = new SanPham_BLL();
        ItemPanel = new JPanel(new WrapLayout(WrapLayout.LEADING, 35, 35));
        ItemPanel.setBackground(Color.decode("#cfdef3"));
        ItemPanel.setBorder(BorderFactory.createEmptyBorder(-15, -15, 0, 20));
        ItemPanel.revalidate();
        ItemPanel.repaint();

        setViewportView(ItemPanel);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        getVerticalScrollBar().setUI(new MyScrollBarUI());
        getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setPreferredSize(new Dimension(740, 540));
    }


}

