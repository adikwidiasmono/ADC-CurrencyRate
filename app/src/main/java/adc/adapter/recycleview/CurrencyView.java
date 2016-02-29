package adc.adapter.recycleview;

/**
 * Created by Adik Widiasmono on 2/10/2016.
 */
public class CurrencyView {
    private String currencyName;
    private Double todayCurrency;
    private Double yesterdayCurrency;
    private int countryImgResource;

    public CurrencyView(String currencyName, Double todayCurrency, Double yesterdayCurrency, int countryImgResource) {
        this.currencyName = currencyName;
        if (todayCurrency != null)
            this.todayCurrency = Double.valueOf(1) / todayCurrency;
        else
            this.todayCurrency = null;
        if (yesterdayCurrency != null)
            this.yesterdayCurrency = Double.valueOf(1) / yesterdayCurrency;
        else
            this.yesterdayCurrency = null;
        this.countryImgResource = countryImgResource;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getTodayCurrency() {
        return todayCurrency;
    }

    public void setTodayCurrency(Double todayCurrency) {
        this.todayCurrency = todayCurrency;
    }

    public Double getYesterdayCurrency() {
        return yesterdayCurrency;
    }

    public void setYesterdayCurrency(Double yesterdayCurrency) {
        this.yesterdayCurrency = yesterdayCurrency;
    }

    public int getCountryImgResource() {
        return countryImgResource;
    }

    public void setCountryImgResource(int countryImgResource) {
        this.countryImgResource = countryImgResource;
    }
}
