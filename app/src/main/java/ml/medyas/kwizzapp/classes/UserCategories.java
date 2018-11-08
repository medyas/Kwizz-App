package ml.medyas.kwizzapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class UserCategories implements Parcelable {

    private String uid;
    private List<Categories> categories;

    public UserCategories() {
    }

    public UserCategories(String uid, List<Categories> categories) {
        this.uid = uid;
        this.categories = categories;
    }

    private UserCategories(Parcel in) {
        uid = in.readString();
        categories = in.createTypedArrayList(Categories.CREATOR);
    }

    public static final Creator<UserCategories> CREATOR = new Creator<UserCategories>() {
        @Override
        public UserCategories createFromParcel(Parcel in) {
            return new UserCategories(in);
        }

        @Override
        public UserCategories[] newArray(int size) {
            return new UserCategories[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeTypedList(categories);
    }
}


/* ---------------------------------------------------------------------------------------------- */

