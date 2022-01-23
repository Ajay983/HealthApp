package com.linkitsoft.dategoal.ui.settings;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class statedata {
    public static String readJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static HashMap<String, List<String>> getData(String cntry,Context context) {

        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List<String> cricket = new ArrayList<String>();
        JSONArray cityarr;
        JSONArray statesarr;
        try {
            statesarr = new JSONArray(readJSONFromAsset(context,"countries"));
            cityarr = new JSONArray(readJSONFromAsset(context,"states"));

            String Selected_state_code = null;

            for(int i=0;i<statesarr.length();i++)
            {
                if(statesarr.getJSONObject(i).getString("name").equals(cntry))
                {
                    Selected_state_code = statesarr.getJSONObject(i).getString("iso2");
                }
            }

            for(int i=0;i<cityarr.length();i++)
            {
                if(cityarr.getJSONObject(i).getString("country_code").equals(Selected_state_code))
                {
                    cricket.add(cityarr.getJSONObject(i).getString("name"));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        expandableListDetail.put("State", cricket);

        return expandableListDetail;
    }
}
