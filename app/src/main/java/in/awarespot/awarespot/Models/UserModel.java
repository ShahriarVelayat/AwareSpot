package in.awarespot.awarespot.Models;

import java.util.List;

/**
 * Created by saikiran on 6/21/17.
 */

public class UserModel {

    public String uid;
    public String nameOfUser;
    public String notifyToken;
    public List<String> citiesKnown;

    public UserModel(String uid, String nameOfUser, String notifyToken, List<String> citiesKnown) {
        this.uid = uid;
        this.nameOfUser = nameOfUser;
        this.notifyToken = notifyToken;
        this.citiesKnown = citiesKnown;
    }

    public UserModel() {
    }

    public String getNotifyToken() {
        return notifyToken;
    }

    public void setNotifyToken(String notifyToken) {
        this.notifyToken = notifyToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public List<String> getCitiesKnown() {
        return citiesKnown;
    }

    public void setCitiesKnown(List<String> citiesKnown) {
        this.citiesKnown = citiesKnown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;

        UserModel userModel = (UserModel) o;

        return getUid() != null ? getUid().equals(userModel.getUid()) : userModel.getUid() == null;

    }

    @Override
    public int hashCode() {
        return getUid() != null ? getUid().hashCode() : 0;
    }
}
