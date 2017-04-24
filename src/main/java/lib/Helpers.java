package lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Helpers {

    public static String englishPluralise(String singularWord, String pluralWord, int quantity) {
        if (quantity == 0) {
            return "no " + pluralWord;
        }
        if (quantity == 1) {
            return "one " + singularWord;
        }
        else {
            return String.format("%d %s", quantity, pluralWord);
        }
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Sets the hours, minutes and seconds in a date to zero.
     * eg Tue Apr 18 23:44:41 CEST 2017 becomes Tue Apr 18 00:00:00 CEST 2017
     * @param date
     * @return
     */
    public static Date truncateDate(Date date) {
        return stringToDate(dateToString(date));
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public static Date stringToDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
