package ml.medyas.kwizzapp.classes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpentDBService {

    @GET("api.php")
    Call<OpentDBClass> getQuizAll(@Query("amount") String amount,
                                  @Query("category") String category,
                                  @Query("difficulty") String difficulty,
                                  @Query("type") String type);

    @GET("api.php")
    Call<OpentDBClass> getQuizCategory(@Query("amount") String amount,
                               @Query("category") String category,
                               @Query("type") String type);

    @GET("api.php")
    Call<OpentDBClass> getQuizDiff(@Query("amount") String amount,
                                   @Query("difficulty") String difficulty,
                                   @Query("type") String type);

    @GET("api.php")
    Call<OpentDBClass> getQuiz(@Query("amount") String amount,
                                   @Query("type") String type);
}
