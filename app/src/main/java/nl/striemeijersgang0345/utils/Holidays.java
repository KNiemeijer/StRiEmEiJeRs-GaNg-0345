package nl.striemeijersgang0345.utils;

import android.graphics.Color;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;
import nl.striemeijersgang0345.R;

public class Holidays {

    private GifImageView feestView;

    public void checkHolidays(GifImageView feestView) {
        this.feestView = feestView;

        // Get today's date
        Date now = new Date();
        int curYear = Calendar.getInstance().get(Calendar.YEAR);

        // Christmas
        Date christmasStart = stringToDate(curYear + "-12-21");
        Date christmasEnd = stringToDate(curYear + "-12-28");
        if(now.after(christmasStart) && now.before(christmasEnd))
            displayHolidayFestivities(R.drawable.dancing_santa);

        // New year
        Date newyearStart = stringToDate(curYear + "-12-28");
        Date newyearEnd = stringToDate((curYear + 1) + "-01-03");
        if(now.after(newyearStart) && now.before(newyearEnd))
            displayHolidayFestivities(R.drawable.new_year);

        // Carnaval
        Date carnavalStart = stringToDate(curYear + "-" +
                getEasterMonth(curYear) + "-" +
                (getEasterDay(curYear) - 51));
        Date carnavalEnd = stringToDate(curYear + "-" +
                getEasterMonth(curYear) + "-" +
                (getEasterDay(curYear) - 47));
        if(now.after(carnavalStart) && now.before(carnavalEnd))
            displayHolidayFestivities(R.drawable.carnaval);

                // Easter
        Date easterStart = stringToDate(curYear + "-" +
                getEasterMonth(curYear) + "-" +
                (getEasterDay(curYear) - 2));
        Date easterEnd  = stringToDate(curYear + "-" +
                getEasterMonth(curYear) + "-" +
                (getEasterDay(curYear) + 1));
        if(now.after(easterStart) && now.before(easterEnd))
            displayHolidayFestivities(R.drawable.easter_bunny);

        // Halloween
        Date halloweenStart = stringToDate(curYear + "-10-30");
        Date halloweenEnd = stringToDate(curYear + "-11-01");
        if(now.after(halloweenStart) && now.before(halloweenEnd))
            displayHolidayFestivities(R.drawable.halloween_spook);

        // Valentine's Day
        Date valentinesStart = stringToDate(curYear + "-02-14");
        Date valentinesEnd = stringToDate(curYear + "-02-15");
        if(now.after(valentinesStart) && now.before(valentinesEnd))
            displayHolidayFestivities(R.drawable.valentines_day);

        // Sinterklaas
        Date sinterklaasStart = stringToDate(curYear + "-12-01");
        Date sinterklaasEnd = stringToDate(curYear + "-12-07");
        if(now.after(sinterklaasStart) && now.before(sinterklaasEnd))
            displayHolidayFestivities(R.drawable.sinterklaas);
    }

    private void displayHolidayFestivities(int cartoon) {
        feestView.setVisibility(View.VISIBLE);

        feestView.setImageResource(cartoon);
        feestView.setBackgroundColor(Color.TRANSPARENT);
    }

    private static Date stringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    private static int getEasterMonth(int year)
    {
        int a = year % 19,
                b = year / 100,
                c = year % 100,
                d = b / 4,
                e = b % 4,
                g = (8 * b + 13) / 25,
                h = (19 * a + b - d - g + 15) % 30,
                j = c / 4,
                k = c % 4,
                m = (a + 11 * h) / 319,
                r = (2 * e + 2 * j - k - h + m + 32) % 7; // month

        return (h - m + r + 90) / 25;
    }

    private static int getEasterDay(int year) {
        int a = year % 19,
                b = year / 100,
                c = year % 100,
                d = b / 4,
                e = b % 4,
                g = (8 * b + 13) / 25,
                h = (19 * a + b - d - g + 15) % 30,
                j = c / 4,
                k = c % 4,
                m = (a + 11 * h) / 319,
                r = (2 * e + 2 * j - k - h + m + 32) % 7,
                n = (h - m + r + 90) / 25; // month

        return (h - m + r + n + 19) % 32; // day
    }
}
