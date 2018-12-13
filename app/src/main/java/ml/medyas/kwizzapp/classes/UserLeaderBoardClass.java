package ml.medyas.kwizzapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

public class UserLeaderBoardClass implements Parcelable {
    private String username;
    private long score;
    private String imageUrl;

    public UserLeaderBoardClass() {
    }

    public UserLeaderBoardClass(String username, long score, String imageUrl) {
        this.username = username;
        this.score = score;
        this.imageUrl = imageUrl;
    }

    protected UserLeaderBoardClass(Parcel in) {
        username = in.readString();
        score = in.readLong();
        imageUrl = in.readString();
    }

    public static final Creator<UserLeaderBoardClass> CREATOR = new Creator<UserLeaderBoardClass>() {
        @Override
        public UserLeaderBoardClass createFromParcel(Parcel in) {
            return new UserLeaderBoardClass(in);
        }

        @Override
        public UserLeaderBoardClass[] newArray(int size) {
            return new UserLeaderBoardClass[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeLong(score);
        parcel.writeString(imageUrl);
    }

    public String getFormattedScore() {
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(score);
    }

}
