package in.awarespot.awarespot.Models;

/**
 * Created by saikiran on 6/21/17.
 */

public class AwareSpotModel {

    public String uid;
    public double lat;
    public double lng;
    public String city;
    public String titleOfAwareSpot;
    public String contentOfAwareSpot;
    public int upVote;
    public int downVote;


    public AwareSpotModel(String uid, double lat, double lng, String city, String titleOfAwareSpot, String contentOfAwareSpot, int upVote, int downVote) {
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.titleOfAwareSpot = titleOfAwareSpot;
        this.contentOfAwareSpot = contentOfAwareSpot;
        this.upVote = upVote;
        this.downVote = downVote;
    }

    public AwareSpotModel() { //Default Const
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTitleOfAwareSpot() {
        return titleOfAwareSpot;
    }

    public void setTitleOfAwareSpot(String titleOfAwareSpot) {
        this.titleOfAwareSpot = titleOfAwareSpot;
    }

    public String getContentOfAwareSpot() {
        return contentOfAwareSpot;
    }

    public void setContentOfAwareSpot(String contentOfAwareSpot) {
        this.contentOfAwareSpot = contentOfAwareSpot;
    }

    public int getUpVote() {
        return upVote;
    }

    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }

    public int getDownVote() {
        return downVote;
    }

    public void setDownVote(int downVote) {
        this.downVote = downVote;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AwareSpotModel)) return false;

        AwareSpotModel that = (AwareSpotModel) o;
        return getUid() != null ? getUid().equals(that.getUid()) : that.getUid() == null;
    }

    @Override
    public int hashCode() {
        return getUid() != null ? getUid().hashCode() : 0;
    }
}
