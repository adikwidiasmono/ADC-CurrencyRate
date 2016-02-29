package adc.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import adc.api.model.CurrencyExchange;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Adik Widiasmono on 2/8/2016.
 */
public class CurrencyIMPL {
    private static CurrencyIMPL impl;
    private CurrencyURL currencyBASE;

    private CurrencyIMPL() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CurrencyURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        currencyBASE = retrofit.create(CurrencyURL.class);
    }

    public static CurrencyIMPL getInstance() {
        if (impl == null)
            impl = new CurrencyIMPL();

        return impl;
    }

    public Call<CurrencyExchange> getCurrencyLatest(String currencySymbol) {
        return currencyBASE.getCurrencyLatest(currencySymbol);
    }

    public Call<CurrencyExchange> getCurrencyByDate(Date date, String currencySymbol) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return currencyBASE.getCurrencyByDate(sdf.format(date), currencySymbol);
    }

}
