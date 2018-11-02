package ml.medyas.kwizzapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryItemClass implements Parcelable {

    private String index;
    private String name;
    private String img;
    private String description;

    public CategoryItemClass() {
    }

    public CategoryItemClass(String index, String name, String img, String description) {
        this.index = index;
        this.name = name;
        this.img = img;
        this.description = description;
    }

    protected CategoryItemClass(Parcel in) {
        index = in.readString();
        name = in.readString();
        img = in.readString();
        description = in.readString();
    }

    public static final Creator<CategoryItemClass> CREATOR = new Creator<CategoryItemClass>() {
        @Override
        public CategoryItemClass createFromParcel(Parcel in) {
            return new CategoryItemClass(in);
        }

        @Override
        public CategoryItemClass[] newArray(int size) {
            return new CategoryItemClass[size];
        }
    };

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(index);
        parcel.writeString(name);
        parcel.writeString(img);
        parcel.writeString(description);
    }
}
