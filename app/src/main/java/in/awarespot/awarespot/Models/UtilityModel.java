package in.awarespot.awarespot.Models;

import java.util.List;

/**
 * Created by saikiran on 6/21/17.
 */

public class UtilityModel {

    public String uid;
    public double lat;
    public double lng;
    public String city;
    public String titleOfUtility;
    public String titleOfUtilityHindi;
    public String titleOfUtilityGujarati;
    public String contentOfUtility;
    public String contentOfUtilityHindi;
    public String contentOfutlityGujarati;
    public String tag1;
    public String tag2;
    public String tag3;
    public int upVote;
    public int downVote;


    public UtilityModel(String uid, double lat, double lng, String city, String titleOfUtility, String titleOfUtilityHindi, String titleOfUtilityGujarati, String contentOfUtility, String contentOfUtilityHindi, String contentOfutlityGujarati, int upVote, int downVote) {
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
        this.city = city;
        this.titleOfUtility = titleOfUtility;
        this.titleOfUtilityHindi = titleOfUtilityHindi;
        this.titleOfUtilityGujarati = titleOfUtilityGujarati;
        this.contentOfUtility = contentOfUtility;
        this.contentOfUtilityHindi = contentOfUtilityHindi;
        this.contentOfutlityGujarati = contentOfutlityGujarati;
        this.upVote = upVote;
        this.downVote = downVote;
    }

    public UtilityModel() { //Default Const
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
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

    public String getTitleOfUtility() {
        return titleOfUtility;
    }

    public void setTitleOfUtility(String titleOfUtility) {
        this.titleOfUtility = titleOfUtility;
    }

    public String getContentOfUtility() {
        return contentOfUtility;
    }

    public void setContentOfUtility(String contentOfUtility) {
        this.contentOfUtility = contentOfUtility;
    }

    public String getTitleOfUtilityHindi() {
        return titleOfUtilityHindi;
    }

    public void setTitleOfUtilityHindi(String titleOfUtilityHindi) {
        this.titleOfUtilityHindi = titleOfUtilityHindi;
    }

    public String getTitleOfUtilityGujarati() {
        return titleOfUtilityGujarati;
    }

    public void setTitleOfUtilityGujarati(String titleOfUtilityGujarati) {
        this.titleOfUtilityGujarati = titleOfUtilityGujarati;
    }

    public String getContentOfUtilityHindi() {
        return contentOfUtilityHindi;
    }

    public void setContentOfUtilityHindi(String contentOfUtilityHindi) {
        this.contentOfUtilityHindi = contentOfUtilityHindi;
    }

    public String getContentOfutlityGujarati() {
        return contentOfutlityGujarati;
    }

    public void setContentOfutlityGujarati(String contentOfutlityGujarati) {
        this.contentOfutlityGujarati = contentOfutlityGujarati;
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
        if (!(o instanceof UtilityModel)) return false;

        UtilityModel that = (UtilityModel) o;
        return getUid() != null ? getUid().equals(that.getUid()) : that.getUid() == null;
    }

    @Override
    public int hashCode() {
        return getUid() != null ? getUid().hashCode() : 0;
    }
}
