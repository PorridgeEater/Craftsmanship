package leadroyal.porridge;

import com.avos.avoscloud.AVObject;

/**
 * Created by LeadroyaL on 2015/12/4.
 */
public class NoticeEntity {
    private String[] imageSrc = new String[9];
    private String txt = "";
    private int price = 0;
    private AVObject avo = null;

    public void setImageSrc(String imageSrc, int pos) {
        this.imageSrc[pos] = imageSrc;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setAVO(AVObject avo) {
        this.avo = avo;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageSrc(int pos) {
        return imageSrc[pos];
    }

    public String getTxt() {
        return txt;
    }

    public AVObject getAVO() {
        return avo;
    }

    public int getPrice() {
        return price;
    }
}
