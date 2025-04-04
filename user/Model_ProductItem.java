
package user;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Image;



public class Model_ProductItem {

    /**
     * @return the Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * @param Title the Title to set
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     * @return the Image
     */
    public String getImageicon() {
        return Imageicon;
    }

    /**
     * @param Image the Image to set
     */
    public void setImageicon(String Image) {
        this.Imageicon = Image;
    }

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }


    public Model_ProductItem() {
    }

    public Model_ProductItem(String Title, String Image, String price) {
        this.Title = Title;
        this.Imageicon = Image;
        this.price = price;
    }

    private String Title;
    private String Imageicon;
    private String price;

    public Icon toImage(){
        ImageIcon image = new ImageIcon("user/ProductImage/" +Imageicon+".png");
        Image og = image.getImage();
        int w = 140;
        int h = 180;
        int iw = image.getIconWidth();
        int ih = image.getIconHeight();
        double xscale = (double) w / iw;
        double yscale = (double) h / ih;
        double scale = Math.max(xscale, yscale);
        int width = (int)(scale*iw);
        int height = (int)(scale*ih);
        Image scaledImage = og.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(scaledImage);
    }
    
}
