package ml.medyas.kwizzapp.classes;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpentDBCalls {
    private static final String OPEN_DB_URL = "https://opentdb.com/";

    private Retrofit builder(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public Call<OpentDBClass> getQuestion(String amount, String type) {
        return builder(OPEN_DB_URL).create(OpentDBService.class).
                getQuiz(amount, type);
    }

    public Call<OpentDBClass> getQuestionAll(String amount, String category, String difficulty, String type) {
        return builder(OPEN_DB_URL).create(OpentDBService.class).
                getQuizAll(amount, category, difficulty,  type);
    }

    public Call<OpentDBClass> getQuestionByCategory(String amount, String category, String type) {
        return builder(OPEN_DB_URL).create(OpentDBService.class).
                getQuizCategory(amount, category,  type);
    }

    public Call<OpentDBClass> getQuestionByDiff(String amount, String difficulty, String type) {
        return builder(OPEN_DB_URL).create(OpentDBService.class).
                getQuizDiff(amount, difficulty,  type);
    }
}
