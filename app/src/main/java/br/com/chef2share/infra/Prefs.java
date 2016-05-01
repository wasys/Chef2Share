package br.com.chef2share.infra;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.StringUtils;

/**
 * Created by Jonas on 17/11/2015.
 */
public class Prefs{

    private static String PREF_ID = "chef2share_prefs";

    public static void setString(Context context, String flag, String valor) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(flag, valor);
        editor.commit();
    }


    public static boolean getBoolean(Context context, String flag) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        boolean b = pref.getBoolean(flag, false);
        return b;
    }

    public static void setBoolean(Context context, String flag, boolean b) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(flag, b);
        editor.commit();
    }

    public static String getString(Context context, String flag) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        String s = pref.getString(flag, "");
        return s;
    }
}
