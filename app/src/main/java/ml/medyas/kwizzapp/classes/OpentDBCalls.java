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


    public Call<OpentDBClass> getQuestion(int amount, int category, String type) {
        return builder(OPEN_DB_URL).create(OpentDBService.class).
                getLatestExchangeRates(amount, category, type);
    }
}
