package zw.co.nm.movies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {



    public static JSONArray getJsonArray(String response) {
        try {
            JSONArray obj_temp_arr = new JSONArray(response);
            return obj_temp_arr;
        } catch (JSONException e) {
//            e.printStackTrace();
            return null;
        }

    }

    public static JSONObject getJsonObject(JSONArray arr, int position) {
        try {
            JSONObject obj_temp = arr.getJSONObject(position);
            return obj_temp;
        } catch (JSONException e) {
//            e.printStackTrace();
            return null;
        }

    }


}
