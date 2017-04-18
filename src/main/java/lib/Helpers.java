package lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
