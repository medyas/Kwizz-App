package ml.medyas.kwizzapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OpentDBClass implements Parcelable {

    private int response_code;
    private List<QuestionClass> results;

    public OpentDBClass(int response_code, List<QuestionClass> results) {
        this.response_code = response_code;
        this.results = results;
    }

    protected OpentDBClass(Parcel in) {
        response_code = in.readInt();
        results = in.createTypedArrayList(QuestionClass.CREATOR);
    }

    public static final Creator<OpentDBClass> CREATOR = new Creator<OpentDBClass>() {
        @Override
        public OpentDBClass createFromParcel(Parcel in) {
            return new OpentDBClass(in);
        }

        @Override
        public OpentDBClass[] newArray(int size) {
            return new OpentDBClass[size];
        }
    };

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public List<QuestionClass> getResults() {
        return results;
    }

    public void setResults(List<QuestionClass> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(response_code);
        parcel.writeTypedList(results);
    }
}
