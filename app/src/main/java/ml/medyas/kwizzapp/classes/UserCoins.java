package ml.medyas.kwizzapp.classes;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserCoins {
    private String uid;
    private long userCoins;

    public UserCoins() {
    }

    public UserCoins(String uid, long userCoins) {
        this.uid = uid;
        this.userCoins = userCoins;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getUserCoins() {
        return userCoins;
    }

    public void setUserCoins(long userCoins) {
        this.userCoins = userCoins;
    }
}
