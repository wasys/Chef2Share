package br.com.chef2share.infra;

import com.android.utils.lib.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Jonas on 20/11/2015.
 */
public class SuperGson {

    public static String toJson(Object object){
        Gson gson = new Gson();
        String s = gson.toJson(object);
        return s;
    }

    public static <T> T   fromJson(String json, Type typeOfT){
        Gson gson = new Gson();
        T target = (T) gson.fromJson(json, typeOfT);
        return target;
    }

    public static JSONObject toJSONObject(Object object) throws JSONException {
        String gson = toJson(object);
        JSONObject json = new JSONObject(gson);
        return json;
    }
}
