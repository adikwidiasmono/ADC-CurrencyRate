package adc.api;

import adc.api.model.CurrencyExchange;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Adik Widiasmono on 2/8/2016.
 */
public interface CurrencyURL {
    public static String BASE_URL = "https://api.fixer.io/";

    @GET("latest")
    Call<CurrencyExchange> getCurrencyLatest(@Query("base") String currencySymbol);

    @GET("{date}")
    Call<CurrencyExchange> getCurrencyByDate(@Path("date") String date, @Query("base") String currencySymbol);

}
