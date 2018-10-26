package ml.medyas.kwizzapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionClass implements Parcelable {

    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correct_answer;
    private String[] incorrect_answers;

    public QuestionClass(String category, String type, String difficulty, String question, String correct_answer, String[] incorrect_answers) {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answers;
    }

    protected QuestionClass(Parcel in) {
        category = in.readString();
        type = in.readString();
        difficulty = in.readString();
        question = in.readString();
        correct_answer = in.readString();
        incorrect_answers = in.createStringArray();
    }

    public static final Creator<QuestionClass> CREATOR = new Creator<QuestionClass>() {
        @Override
        public QuestionClass createFromParcel(Parcel in) {
            return new QuestionClass(in);
        }

        @Override
        public QuestionClass[] newArray(int size) {
            return new QuestionClass[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String[] getIncorrect_answers() {
        return incorrect_answers;
    }

    public void setIncorrect_answers(String[] incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeString(type);
        parcel.writeString(difficulty);
        parcel.writeString(question);
        parcel.writeString(correct_answer);
        parcel.writeStringArray(incorrect_answers);
    }
}
