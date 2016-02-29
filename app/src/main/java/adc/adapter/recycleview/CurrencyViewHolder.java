package adc.adapter.recycleview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import adc.com.currency.R;
import adc.utils.MyAppUtils;

/**
 * Created by Adik Widiasmono on 2/10/2016.
 */
public class CurrencyViewHolder extends RecyclerView.ViewHolder {
    private TextView tvCurrencyName;
    private TextView tvTodayCurrency;
    private TextView tvYesterdayCurrency;
    private ImageView ivCurrencyIcon;
    private ImageView ivCurrencyStatusIncrease;
    private View ivCurrencyStatusFlat;
    private ImageView ivCurrencyStatusDecrease;
    private Context context;

    public CurrencyViewHolder(View view) {
        super(view);
        context = view.getContext();
        tvCurrencyName = (TextView) view.findViewById(R.id.currency_name);
        tvTodayCurrency = (TextView) view.findViewById(R.id.currency_today);
        tvYesterdayCurrency = (TextView) view.findViewById(R.id.currency_yesterday);
        ivCurrencyIcon = (ImageView) view.findViewById(R.id.currecy_icon);
        ivCurrencyStatusIncrease = (ImageView) view.findViewById(R.id.currency_status_increase);
        ivCurrencyStatusFlat = view.findViewById(R.id.currency_status_flat);
        ivCurrencyStatusDecrease = (ImageView) view.findViewById(R.id.currency_status_decrease);
    }

    public void setValue(CurrencyView currency, String baseCurrCode) {
        ivCurrencyStatusFlat.setVisibility(View.GONE);
        ivCurrencyStatusIncrease.setVisibility(View.GONE);
        ivCurrencyStatusDecrease.setVisibility(View.GONE);
        tvTodayCurrency.setTextColor(ContextCompat.getColor(context, R.color.my_black));

        tvCurrencyName.setText(currency.getCurrencyName());
        tvTodayCurrency.setText(baseCurrCode + " " + MyAppUtils.safeDoubleToCurrency(currency.getTodayCurrency()));
        tvYesterdayCurrency.setText(baseCurrCode + " " + MyAppUtils.safeDoubleToCurrency(currency.getYesterdayCurrency()));
        ivCurrencyIcon.setImageDrawable(ContextCompat.getDrawable(context, currency.getCountryImgResource()));
        int res = Double.compare(currency.getTodayCurrency(), currency.getYesterdayCurrency());
        if (res > 0) {
            ivCurrencyStatusDecrease.setVisibility(View.VISIBLE);
            tvTodayCurrency.setTextColor(ContextCompat.getColor(context, R.color.my_red));
        } else if (res < 0) {
            ivCurrencyStatusIncrease.setVisibility(View.VISIBLE);
            tvTodayCurrency.setTextColor(ContextCompat.getColor(context, R.color.my_green));
        } else {
            ivCurrencyStatusFlat.setVisibility(View.VISIBLE);
            tvTodayCurrency.setTextColor(ContextCompat.getColor(context, R.color.my_black));
        }
    }
}
