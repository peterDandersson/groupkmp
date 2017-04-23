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

    /**
     * Sets the hours, minutes and seconds in a date to zero.
     * eg Tue Apr 18 23:44:41 CEST 2017 becomes Tue Apr 18 00:00:00 CEST 2017
     * @param date
     * @return
     */
    public static Date truncateDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("y:M:d");
        Date truncatedDate = null;
        try {
            truncatedDate = dateFormatter.parse(dateFormatter.format(date));
        } catch (ParseException e) {
            // No exception will be given since the formatted string is derived from a date instance.
        }
        return truncatedDate;
    }
}
