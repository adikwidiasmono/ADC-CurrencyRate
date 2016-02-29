package adc.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Adik Widiasmono on 2/10/2016.
 */
public class MyAppUtils {
    public static String safeDoubleToCurrency(Double val) {
        BigDecimal value = BigDecimal.valueOf(val);
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat
                .getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setGroupingSeparator(',');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        kursIndonesia.setParseBigDecimal(true);
        if (val < 1)
            kursIndonesia.setMaximumFractionDigits(8);
        else if (val < 10)
            kursIndonesia.setMaximumFractionDigits(6);
        else if (val < 100)
            kursIndonesia.setMaximumFractionDigits(4);

        return kursIndonesia.format(value);
    }

    public static String getCurrentDate(String format) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(dt);
    }

    public static Date parseStringToDate(String formatDate, String inputDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        try {
            Date date = formatter.parse(inputDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
