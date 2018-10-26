package ml.medyas.kwizzapp.classes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpentDBService {

    //Get Currency Exchange rates
    @GET("api.php")
    Call<OpentDBClass> getLatestExchangeRates(@Query("amount") int amount,
                                              @Query("category") int category,
                                              @Query("type") String type);
}
