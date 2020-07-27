package com.example.kelseyhaydenc196.Utilities;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Dates {
    public static String Date(int year, int month, int day) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            month += 1;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.US);
        String dateString = month + "/" + day + "/" + year;
        Date dateFormattedLong = null;
        try {
            dateFormattedLong = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert dateFormattedLong != null;
        return sdf.format(dateFormattedLong);

    }

    ///// Takes String format and converts to date format /////
    @TypeConverter
    public static Date stringToDateConverter(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date dateFormattedLong = null;
        try {
            dateFormattedLong = sdf.parse(dateString);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return dateFormattedLong;
    }

    ///// takes date format and converts to string format /////
    @TypeConverter
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return sdf.format(date);
    }
}
