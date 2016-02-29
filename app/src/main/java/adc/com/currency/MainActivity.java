package adc.com.currency;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import adc.adapter.recycleview.CurrencyView;
import adc.adapter.recycleview.CurrencyViewAdapter;
import adc.api.CurrencyIMPL;
import adc.api.model.CurrencyExchange;
import adc.utils.MyAppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Call<CurrencyExchange> call;
    private RecyclerView recyclerViewList;
    private CurrencyViewAdapter recyclerViewAdapter;
    private EditText etBaseCurrency, etBaseDate;
    private Calendar myCalendar;
    private View pgBar;
    private TextView tvFailedHeader, tvFailedAction;

    private String selectedCountryCode = "IDR - Indonesia Rupiah";
    private CurrencyExchange selectedExchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupFloatingButton();
        setupRecycleView();
        setupEditText();
        setupProgressBar();
        setupTextView();

        String baseCode = selectedCountryCode.split(Pattern.quote("-"))[0].trim();
        fetchCurrencyExchange(myCalendar, baseCode);
    }

    private void setupProgressBar() {
        pgBar = findViewById(R.id.progress_bar_layout);
        pgBar.setVisibility(View.VISIBLE);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFloatingButton() {
        FloatingActionButton fabRefresh = (FloatingActionButton) findViewById(R.id.fab);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String baseCode = selectedCountryCode.split(Pattern.quote("-"))[0].trim();
                fetchCurrencyExchange(myCalendar, baseCode);
            }
        });
    }

    private void setupRecycleView() {
        recyclerViewList = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerViewList.setHasFixedSize(true);

        recyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new CurrencyViewAdapter();
        recyclerViewList.setAdapter(recyclerViewAdapter);
    }

    private void setupEditText() {
        etBaseDate = (EditText) findViewById(R.id.base_date);
        etBaseCurrency = (EditText) findViewById(R.id.base_currency);

        myCalendar = Calendar.getInstance();
        updateDateLabel();
        updateCurrencyLabel();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };
        etBaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Country Code");
        final String[] types = getResources().getStringArray(R.array.currency_aray);
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                selectedCountryCode = types[which];
                updateCurrencyLabel();
            }

        });
        etBaseCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.show();
            }
        });
    }

    private void updateCurrencyLabel() {
        etBaseCurrency.setText(selectedCountryCode);
    }

    private void updateDateLabel() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etBaseDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void setupTextView() {
        tvFailedHeader = (TextView) findViewById(R.id.failed_then_reload_header);
        tvFailedAction = (TextView) findViewById(R.id.failed_then_reload_action);
        tvFailedHeader.setVisibility(View.GONE);
        tvFailedAction.setVisibility(View.GONE);
        tvFailedAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSuccessLoad();
                String baseCode = selectedCountryCode.split(Pattern.quote("-"))[0].trim();
                fetchCurrencyExchange(myCalendar, baseCode);
            }
        });
    }

    private void onSuccessLoad() {
        recyclerViewList.setVisibility(View.VISIBLE);
        tvFailedHeader.setVisibility(View.GONE);
        tvFailedAction.setVisibility(View.GONE);
    }

    private void onFailedLoad() {
        pgBar.setVisibility(View.GONE);
        recyclerViewList.setVisibility(View.GONE);
        tvFailedHeader.setVisibility(View.VISIBLE);
        tvFailedAction.setVisibility(View.VISIBLE);
    }

    private void fetchCurrencyExchange(final Calendar selectedDate, final String selectedCurrencyCode) {
        pgBar.setVisibility(View.VISIBLE);

        // GET SELECTED DATE DATA
        call = CurrencyIMPL.getInstance().getCurrencyByDate(selectedDate.getTime(), selectedCurrencyCode);
        call.enqueue(new Callback<CurrencyExchange>() {
            @Override
            public void onResponse(Call<CurrencyExchange> call, Response<CurrencyExchange> response) {
                // response.isSuccess() is true if the response code is 2xx
                if (response.isSuccess()) {
                    onSuccessLoad();
                    selectedExchange = response.body();
//                    Log.e("SELECTED", "|" + selectedExchange.getBase() + "|");
//                    Log.e("SELECTED", "|" + selectedExchange.getDate() + "|");
                    Calendar serverBaseDate = Calendar.getInstance();
                    serverBaseDate.setTime(MyAppUtils.parseStringToDate("yyyy-MM-dd", selectedExchange.getDate()));
                    fetchYesterdayCurrencyExchange(serverBaseDate, selectedCurrencyCode);
                } else {
                    onFailedLoad();
                    int statusCode = response.code();
//                    Log.e("SELECTED", "|" + statusCode + "|");
                    return;
                }
            }

            @Override
            public void onFailure(Call<CurrencyExchange> call, Throwable t) {
                onFailedLoad();
                t.printStackTrace();
                return;
            }
        });
    }

    private void fetchYesterdayCurrencyExchange(Calendar selectedDate, String selectedCurrencyCode) {
        // GET SELECTED (DATE -1) DATA
        selectedDate.add(Calendar.DATE, -1);
        call = CurrencyIMPL.getInstance().getCurrencyByDate(selectedDate.getTime(), selectedCurrencyCode);
        call.enqueue(new Callback<CurrencyExchange>() {
            @Override
            public void onResponse(Call<CurrencyExchange> call, Response<CurrencyExchange> response) {
                // response.isSuccess() is true if the response code is 2xx
                if (response.isSuccess()) {
                    pgBar.setVisibility(View.GONE);
                    onSuccessLoad();
                    CurrencyExchange yesterdayExchange = response.body();
//                    Log.e("SELECTED - 1", "|" + yesterdayExchange.getBase() + "|");
//                    Log.e("SELECTED - 1", "|" + yesterdayExchange.getDate() + "|");

                    List<CurrencyView> lCurrencyView = new LinkedList<>();
                    lCurrencyView.add(new CurrencyView("IDR - Indonesia Rupiah", selectedExchange.getRates().getIDR(), yesterdayExchange.getRates().getIDR(), R.drawable.indonesia));
                    lCurrencyView.add(new CurrencyView("AUD - Australia Dollar", selectedExchange.getRates().getAUD(), yesterdayExchange.getRates().getAUD(), R.drawable.australia));
                    lCurrencyView.add(new CurrencyView("BGN - Bulgaria Lev", selectedExchange.getRates().getBGN(), yesterdayExchange.getRates().getBGN(), R.drawable.bulgaria));
                    lCurrencyView.add(new CurrencyView("BRL - Brazil Real", selectedExchange.getRates().getBRL(), yesterdayExchange.getRates().getBRL(), R.drawable.brazil));
                    lCurrencyView.add(new CurrencyView("CAD - Canada Dollar", selectedExchange.getRates().getCAD(), yesterdayExchange.getRates().getCAD(), R.drawable.canada));
                    lCurrencyView.add(new CurrencyView("CHF - Switzerland Franc", selectedExchange.getRates().getCHF(), yesterdayExchange.getRates().getCHF(), R.drawable.switzerland));
                    lCurrencyView.add(new CurrencyView("CNY - China Yuan Renminbi", selectedExchange.getRates().getCNY(), yesterdayExchange.getRates().getCNY(), R.drawable.china));
                    lCurrencyView.add(new CurrencyView("CZK - Czech Republic Koruna", selectedExchange.getRates().getCZK(), yesterdayExchange.getRates().getCZK(), R.drawable.czech_republic));
                    lCurrencyView.add(new CurrencyView("DKK - Denmark Krone", selectedExchange.getRates().getDKK(), yesterdayExchange.getRates().getDKK(), R.drawable.denmark));
                    lCurrencyView.add(new CurrencyView("EUR - Euro Member Countries", selectedExchange.getRates().getEUR(), yesterdayExchange.getRates().getEUR(), R.drawable.european_union));
                    lCurrencyView.add(new CurrencyView("GBP - United Kingdom Pound", selectedExchange.getRates().getGBP(), yesterdayExchange.getRates().getGBP(), R.drawable.united_kingdom));
                    lCurrencyView.add(new CurrencyView("HKD - Hong Kong Dollar", selectedExchange.getRates().getHKD(), yesterdayExchange.getRates().getHKD(), R.drawable.hong_kong));
                    lCurrencyView.add(new CurrencyView("HRK - Croatia Kuna", selectedExchange.getRates().getHRK(), yesterdayExchange.getRates().getHRK(), R.drawable.croatia));
                    lCurrencyView.add(new CurrencyView("HUF - Hungary Forint", selectedExchange.getRates().getHUF(), yesterdayExchange.getRates().getHUF(), R.drawable.hungary));
                    lCurrencyView.add(new CurrencyView("ILS - Israel Shekel", selectedExchange.getRates().getILS(), yesterdayExchange.getRates().getILS(), R.drawable.israel));
                    lCurrencyView.add(new CurrencyView("INR - India Rupee", selectedExchange.getRates().getINR(), yesterdayExchange.getRates().getINR(), R.drawable.india));
                    lCurrencyView.add(new CurrencyView("JPY - Japan Yen", selectedExchange.getRates().getJPY(), yesterdayExchange.getRates().getJPY(), R.drawable.japan));
                    lCurrencyView.add(new CurrencyView("KRW - Korea (South) Won", selectedExchange.getRates().getKRW(), yesterdayExchange.getRates().getKRW(), R.drawable.south_korea));
                    lCurrencyView.add(new CurrencyView("MXN - Mexico Peso", selectedExchange.getRates().getMXN(), yesterdayExchange.getRates().getMXN(), R.drawable.mexico));
                    lCurrencyView.add(new CurrencyView("MYR - Malaysia Ringgit", selectedExchange.getRates().getMYR(), yesterdayExchange.getRates().getMYR(), R.drawable.malaysia));
                    lCurrencyView.add(new CurrencyView("NOK - Norway Krone", selectedExchange.getRates().getNOK(), yesterdayExchange.getRates().getNOK(), R.drawable.norway));
                    lCurrencyView.add(new CurrencyView("NZD - New Zealand Dollar", selectedExchange.getRates().getNZD(), yesterdayExchange.getRates().getNZD(), R.drawable.new_zealand));
                    lCurrencyView.add(new CurrencyView("PHP - Philippines Peso", selectedExchange.getRates().getPHP(), yesterdayExchange.getRates().getPHP(), R.drawable.philippines));
                    lCurrencyView.add(new CurrencyView("PLN - Poland Zloty", selectedExchange.getRates().getPLN(), yesterdayExchange.getRates().getPLN(), R.drawable.poland));
                    lCurrencyView.add(new CurrencyView("RON - Romania New Leu", selectedExchange.getRates().getRON(), yesterdayExchange.getRates().getRON(), R.drawable.romania));
                    lCurrencyView.add(new CurrencyView("RUB - Russia Ruble", selectedExchange.getRates().getRUB(), yesterdayExchange.getRates().getRUB(), R.drawable.russian_federation));
                    lCurrencyView.add(new CurrencyView("SEK - Sweden Krona", selectedExchange.getRates().getSEK(), yesterdayExchange.getRates().getSEK(), R.drawable.sweden));
                    lCurrencyView.add(new CurrencyView("SGD - Singapore Dollar", selectedExchange.getRates().getSGD(), yesterdayExchange.getRates().getSGD(), R.drawable.singapore));
                    lCurrencyView.add(new CurrencyView("THB - Thailand Baht", selectedExchange.getRates().getTHB(), yesterdayExchange.getRates().getTHB(), R.drawable.thailand));
                    lCurrencyView.add(new CurrencyView("TRY - Turkey Lira", selectedExchange.getRates().getTRY(), yesterdayExchange.getRates().getTRY(), R.drawable.turkey));
                    lCurrencyView.add(new CurrencyView("USD - United States Dollar", selectedExchange.getRates().getUSD(), yesterdayExchange.getRates().getUSD(), R.drawable.usa));
                    lCurrencyView.add(new CurrencyView("ZAR - South Africa Rand", selectedExchange.getRates().getZAR(), yesterdayExchange.getRates().getZAR(), R.drawable.south_africa));

                    // REMOVE BASE CURRENCY FROM LIST
                    for (CurrencyView curr : lCurrencyView) {
                        if (curr.getTodayCurrency() == null || curr.getYesterdayCurrency() == null) {
                            lCurrencyView.remove(curr);
                            break;
                        }
                    }
                    recyclerViewAdapter.replaceCurrencys(lCurrencyView, yesterdayExchange.getBase());
                } else {
                    onFailedLoad();
                    int statusCode = response.code();
//                    Log.e("SELECTED - 1", "|" + statusCode + "|");
                    return;
                }
            }

            @Override
            public void onFailure(Call<CurrencyExchange> call, Throwable t) {
                onFailedLoad();
                t.printStackTrace();
                return;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//         Cancel retrofit process
        call.cancel();
    }

}
