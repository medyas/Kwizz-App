package ml.medyas.kwizzapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Categories implements Parcelable {
    private String index;
    private String status;

    public Categories(String index, String status) {
        this.index = index;
        this.status = status;
    }

    public Categories() {
    }

    private Categories(Parcel in) {
        index = in.readString();
        status = in.readString();
    }

    public static final Creator<Categories> CREATOR = new Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(index);
        parcel.writeString(status);
    }
}
