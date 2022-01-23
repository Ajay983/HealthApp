package com.linkitsoft.dategoal.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.PeopleModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyfishjy.library.RippleBackground;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_Swipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Swipe extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_Swipe() {
        // Required empty public constructor
    }
    ColorStateList def;
    TextView item1;
    TextView item2;
    TextView item3;

    TextView select;


    public void initcustomtab(View root)
    {
        item1 = root.findViewById(R.id.item1);
        item2 = root.findViewById(R.id.item2);
        item3 = root.findViewById(R.id.item3);
        item1.setOnClickListener(myoc);
        item2.setOnClickListener(myoc);
        item3.setOnClickListener(myoc);
        select = root.findViewById(R.id.select);
        def = item2.getTextColors();
    }



    View.OnClickListener myoc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.item1){
                select.animate().x(0).setDuration(100);
                item1.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
                item2.setTextColor(def);
                item3.setTextColor(def);
                getActivity().finish();
            } else if (view.getId() == R.id.item2){
                item1.setTextColor(def);
                item2.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
                item3.setTextColor(def);
                int size = item2.getWidth();
                select.animate().x(size).setDuration(100);
                getActivity().finish();
            } else if (view.getId() == R.id.item3){
                item1.setTextColor(def);
                item3.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
                item2.setTextColor(def);
                int size = item2.getWidth() * 2;
                select.animate().x(size).setDuration(100);
                //viewPager.setCurrentItem(2);

            }
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Swipe.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Swipe newInstance(String param1, String param2) {
        Home_Swipe fragment = new Home_Swipe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//              window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            // window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
    }


    Context context;
    SharedPreferences sharedpreferences;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    public String userid;
    List<PeopleModel> userModelList;
    CardStackView cardStackView;

    RippleBackground rippleBaackground;
    Swipe_Profile_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home__swipe, container, false);
        userModelList = new ArrayList<PeopleModel>();
        portnVariable = new PortnVariable();
        context = root.getContext();

      /*  String[] data = {"Simran Jakranda,24,New York,https://i.pinimg.com/564x/b8/03/78/b80378993da7282e58b35bdd3adbce89.jpg",
                "Wasif Ali,20,Karachi,https://wallpaperaccess.com/full/2213424.jpg",
                "Paul Rudd,20,London,https://media1.popsugar-assets.com/files/thumbor/DRKaReuIoQvFSthlzcbVnfj9hhQ/fit-in/728xorig/filters:format_auto-!!-:strip_icc-!!-/2020/03/16/237/n/1922398/d2a2fc375e7055358f6460.62104801_/i/post-malone-best-performance-pictures.jpg",
                "Rameez Asghar,24,Karachi,https://s3.amazonaws.com/pixpa.com/com/articles/1562328087-653328-rodrigo-soares-8bfwbuksqqo-unsplashjpg.png", "Jessie,19,Toronto,https://media.istockphoto.com/photos/angry-lion-roaring-picture-id1184240863?k=6&m=1184240863&s=612x612&w=0&h=rUmWeSiYspBwgfsgPBH8-4fyWJvMiylYDA2KIxlWpx0="};
       */

        rippleBaackground = root.findViewById(R.id.content);


        rippleBaackground.startRippleAnimation();


        initcustomtab(root);
        item1.setTextColor(def);
        item3.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
        item2.setTextColor(def);
        int size = item2.getWidth() * 2;
        select.animate().x(size).setDuration(100);

        cardStackView = root.findViewById(R.id.cardstack);
        CardStackLayoutManager cardStackLayoutManager = new CardStackLayoutManager(getContext());
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();

        RewindAnimationSetting revsetting = new RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new DecelerateInterpolator())
                .build();
        cardStackLayoutManager.setRewindAnimationSetting(revsetting);
        cardStackLayoutManager.setSwipeAnimationSetting(setting);
        cardStackLayoutManager.setStackFrom(StackFrom.Left);

        cardStackLayoutManager.setVisibleCount(1);
        cardStackView.setLayoutManager(cardStackLayoutManager);

        getfSwipelistapi();
        return root;
    }

    private void getfSwipelistapi() {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Fetching people...");
        pDialog.show();

        userid = sharedpreferences.getString("userid","0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com+"user/UserView";

        JSONObject jsonParam =new JSONObject();
        try {
            jsonParam.put("userID",userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri,requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();


                String status = null;
                JSONArray message = null;
                String auth = null;
                try {

                    if (response.has("auth")) {
                        auth = response.getString("auth");
                        if (auth != null && auth.equals("authFailed")) {
                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();
                            showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                        }
                    }
                    if (response.has("status")) {

                    status = response.getString("status");

                    if (status.equals("success")) {
                        message = response.getJSONArray("message");

                        if (message.length() != 0) {

                            Gson gson = new Gson();
                            Type type = new TypeToken<List<PeopleModel>>() {
                            }.getType();

                            userModelList = gson.fromJson(String.valueOf(message), type);

                            // adapter = new Swipe_Profile_Adapter(context, userModelList);

                            //  cardStackView.setAdapter(adapter);
                        }

                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                // System.out.println("XXX Volly Error :"+error);

                pDialog.dismissWithAnimation();
                //showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
            }
        }
        );
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);

    }

    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }

    public void showdialogAuth(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(context, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }


}