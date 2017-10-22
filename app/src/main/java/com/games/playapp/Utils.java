package com.games.playapp;

/**
 * Created by benjy on 21/10/2017.
 */

public class Utils {
    public static String changeLetter(String i){
        String result = "";
        if (122 == i.charAt(0)) {
            result = "zz";
            // handle "Z"
        } else if (90 == i.charAt(0)) {
            result += "zz";
            // handle all other letter characters
        } else if ((65 <= i.charAt(0) && i.charAt(0) <= 89) ||
                (97 <= i.charAt(0) && i.charAt(0) <= 121)) {
            result = ""+(char)(i.charAt(0) + 1);
            // append all other characters unchanged
        } else {
            result = i;
        }
        return result;
    }
}
