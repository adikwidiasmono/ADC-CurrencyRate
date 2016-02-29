package adc.adapter.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import adc.com.currency.R;

/**
 * Created by Adik Widiasmono on 2/10/2016.
 */
public class CurrencyViewAdapter extends RecyclerView.Adapter {
    private List<CurrencyView> listCurrency;
    private String baseCurrencyCode;

    public CurrencyViewAdapter() {
        this.listCurrency = new LinkedList<>();
        baseCurrencyCode = "";
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.cardview_currency, parent, false);

        return new CurrencyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CurrencyViewHolder) {
            CurrencyView currency = listCurrency.get(position);
            ((CurrencyViewHolder)holder).setValue(currency, baseCurrencyCode);
        }
    }

    @Override
    public int getItemCount() {
        return listCurrency.size();
    }

    public List<CurrencyView> replaceCurrencys(List<CurrencyView> listCurrency, String baseCurrencyCode){
        this.listCurrency = new LinkedList<>(listCurrency);
        this.baseCurrencyCode = baseCurrencyCode;

        notifyDataSetChanged();
        return this.listCurrency;
    }

}
