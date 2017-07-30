package in.awarespot.awarespot.Models;

/**
 * Created by sai on 30/7/17.
 */

public class StoriesModel {

    public String uid;
    public String shopId;
    public String shopName;
    public String address;
    public String imageUrl;
    public String captionText;
    public int totalViews;

    public StoriesModel(String uid, String shopId, String shopName, String address, String imageUrl, String captionText, int totalViews) {
        this.uid = uid;
        this.shopId = shopId;
        this.shopName = shopName;
        this.address = address;
        this.imageUrl = imageUrl;
        this.captionText = captionText;
        this.totalViews = totalViews;
    }

    public StoriesModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaptionText() {
        return captionText;
    }

    public void setCaptionText(String captionText) {
        this.captionText = captionText;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

}
