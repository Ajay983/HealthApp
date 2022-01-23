package com.linkitsoft.dategoal.ui.settings;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class citydata {



    public static String readJSONFromAsset(Context context,String filename) {
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



    public static HashMap<String, List<String>> getData(String states, Context context) {

        String cutnrystates = "";


        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();


        List<String> cricket = new ArrayList<String>();

        JSONArray cityarr;
        JSONArray statesarr;
        try {
            statesarr = new JSONArray(readJSONFromAsset(context,"states"));
            cityarr = new JSONArray(readJSONFromAsset(context,"cities"));

            String Selected_state_code = null;

            for(int i=0;i<statesarr.length();i++)
            {
                if(statesarr.getJSONObject(i).getString("name").equals(states))
                {
                    Selected_state_code = statesarr.getJSONObject(i).getString("id");
                }
            }

             for(int i=0;i<cityarr.length();i++)
            {
                if(cityarr.getJSONObject(i).getString("state_id").equals(Selected_state_code))
                {
                    cricket.add(cityarr.getJSONObject(i).getString("name"));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        expandableListDetail.put("City", cricket);

        return expandableListDetail;
    }
}
