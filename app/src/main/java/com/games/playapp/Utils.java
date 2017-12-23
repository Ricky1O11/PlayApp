package com.games.playapp;

import android.util.Log;
import android.view.View;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.games.playapp.SignedInActivity.favouritesSnap;

/**
 * Created by benjy on 21/10/2017.
 */

public class Utils {
    public static String changeLetter(String i) {
        String result = "";
        if (122 == i.charAt(0)) {
            result = "zz";
            // handle "Z"
        } else if (90 == i.charAt(0)) {
            result += "zz";
            // handle all other letter characters
        } else if ((65 <= i.charAt(0) && i.charAt(0) <= 89) ||
                (97 <= i.charAt(0) && i.charAt(0) <= 121)) {
            result = "" + (char) (i.charAt(0) + 1);
            // append all other characters unchanged
        } else {
            result = i;
        }
        return result;
    }


    public static void onFavclick(View view, BgModel bgm, boolean isFav) {
        if (isFav) {
            Log.i("isFav", "fav");
            favouritesSnap.getRef().child("" + bgm.getBggId()).removeValue();
        } else {
            Log.i("isFav", "NON fav");
            Map<String, Object> favBg = new HashMap<String, Object>();
            favBg.put("image", bgm.getImage());
            favBg.put("name", bgm.getName());
            favBg.put("key", bgm.getBggId());
            favBg.put("bggId", bgm.getBggId());
            favBg.put("inserted_at", new Date().getTime());
            favouritesSnap.getRef().child("" + bgm.getBggId()).setValue(favBg);
        }
    }
}
