package com.linkitsoft.dategoal.ui.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Agreement.SettingsPrivacyPolicy;
import com.linkitsoft.dategoal.Agreement.SettingsTermsOfUse;
import com.linkitsoft.dategoal.BuildConfig;
import com.linkitsoft.dategoal.CustomExpandableListAdapter;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.MyProfile;
import com.linkitsoft.dategoal.NoScrollExListView;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.home.EditMyProfile;
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {


    private SettingsViewModel mViewModel;
    private Button connect;
    private Button pushnotif;
    private Button privicy;
    private Button term_of_services;
    private Button share_app;
    private Button logout;
    private Button deleteacc;
    private ImageButton editProfile;
    private ImageView badge;
    private TextView phonenumber;
    private CircleImageView img;
    private TextView name;
    private TextView emial;
    private ToggleButton lookingfor_dating;
    private ToggleButton lookingfor_Frend;
    private ToggleButton lookingfor_both;
    private ToggleButton interstdin_male;
    private ToggleButton interstdin_female;
    private ToggleButton interstdin_both;
    private RangeSlider agerange;
    private ToggleButton ethnicity_american;
    private ToggleButton ethnicity_aisan;
    private ToggleButton ethnicity_blakc;
    private ToggleButton ethnicity_latino;
    private ToggleButton ethnicity_hawai;
    private ToggleButton ethnicity_white;
    private ToggleButton seXual_straight;
    private ToggleButton seXual_lesbain;
    private ToggleButton seXual_bisexual;
    private ToggleButton seXual_gay;
    private ToggleButton seXual_queer;
    private ToggleButton seXual_lgbtq;
    private ToggleButton seXual_comingout;
    private ToggleButton seXual_others;
    private NoScrollExListView loc_coutnry;
    private NoScrollExListView loc_city;
    private NoScrollExListView loc_state;
    private Button verify;
    private boolean loading;
    public String set;
    public TextView onlyName;
    public ImageButton eidt;

    Context context;

    NoScrollExListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    HashMap<String, List<String>> satesdatamap = new HashMap<String, List<String>>();
    HashMap<String, List<String>> citydatamap = new HashMap<String, List<String>>();
    ArrayList<String> countries = new ArrayList<String>();


    SharedPreferences sharedpreferences;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;

    String user_name;
    Boolean user_verified;
    String user_phonenumber;
    String user_email, user_img;
    String user_lookingfor;
    String user_interstedin;
    String user_agePreferenceFrom;
    String user_herefor;
    String user_agePreferenceto;
    String user_ethnicity;
    String user_sexuality;
    String user_location_city;
    String user_location_state;
    String user_connectedAccounts;
    String user_location_cntry;
    String googleId, facebookId;
    JSONObject userdata;
    SweetAlertDialog sweetAlertDialog;


    public String userid, token;


    private void getUser_data() {


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "user/" + userid;

        // System.out.println(uri);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET, uri, null /*requestBody*/, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                String message = null;
                String auth = null;

                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();
                loading = false;

                try {
                    if (response.has("auth")) {
                            auth = response.getString("auth");
                        if (auth != null && auth.equals("authFailed")) {
                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();
                            showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                        }
                        }

                    status = response.getString("status");

                    if (status.equals("success")) {

                        message = response.getString("message");

                        if (!message.equals("user has been found")) {

                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();
                            showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                        } else {

                            setProfileobj(response);

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
                //    System.out.println("XXX Volly Error :" + error);
//
//                pDialog.dismissWithAnimation();
                mainshimmer.stopShimmer();
                loading = false;
                //  showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("authorization", "bearer " + token);
                return header;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);

    }


    private void setProfileobj(JSONObject response) throws JSONException {

        userdata = response.getJSONObject("Data");
        user_name = userdata.getString("name");
        user_verified = userdata.getInt("verfiedStatus") == 1;
        user_connectedAccounts = userdata.getInt("connectedAccounts") + "";
        user_phonenumber = userdata.getString("phone");
        facebookId = userdata.getString("facebookId");
        user_img = userdata.getString("Photo1");


        googleId = userdata.getString("googleId");
        user_email = userdata.getString("email").toLowerCase();
        user_lookingfor = userdata.getString("lookingFor").toLowerCase();
        if (user_lookingfor.equals("dating")) {
            lookingfor_dating.toggle();
        } else if (user_lookingfor.equals("both")) {
            lookingfor_both.toggle();
        } else {
            lookingfor_Frend.toggle();
        }


        user_interstedin = userdata.getString("interestedIn").toLowerCase();
        user_agePreferenceFrom = userdata.getString("agePreferenceFrom");
        user_herefor = userdata.getString("herefor");
        user_agePreferenceto = userdata.getString("agePreferenceto");
        user_sexuality = userdata.getString("sexuality").toLowerCase();
        user_ethnicity = userdata.getString("ethinicity").replace(" ", "").toLowerCase();


        if (user_ethnicity.contains("/")) user_ethnicity = user_ethnicity.split("/")[1];
        user_location_city = userdata.getString("city");
        user_location_state = userdata.getString("state");
        user_location_cntry = userdata.getString("country");
        expandableListView.setFilterText(user_location_cntry);


        //user_pushnotifs = userdata.getString("country");

        phonenumber.setText(user_phonenumber);
        name.setText(user_name);
        onlyName.setText(user_name);
        emial.setText(user_email);

        Picasso.get().load(user_img).fit().networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(img);


//        if(user_herefor.equals(lookingfor_dating.getTextOn().toString().toLowerCase()))lookingfor_dating.toggle();
//        else if(user_herefor.equals(lookingfor_both.getTextOn().toString().toLowerCase()))lookingfor_both.toggle();
//        else if(user_herefor.equals(lookingfor_Frend.getTextOn().toString().toLowerCase()))lookingfor_Frend.toggle();

        if (user_interstedin.equals(interstdin_female.getTextOn().toString().toLowerCase()))
            interstdin_female.toggle();
        else if (user_interstedin.equals(interstdin_male.getTextOn().toString().toLowerCase()))
            interstdin_male.toggle();
        else if (user_interstedin.equals(interstdin_both.getTextOn().toString().toLowerCase()))
            interstdin_both.toggle();

    if(Float.valueOf(Math.round(Float.valueOf(user_agePreferenceFrom))) >= 18 && Float.valueOf(Math.round(Float.valueOf(user_agePreferenceto))) <= 90) {

        agerange.setValues(Float.valueOf(Math.round(Float.valueOf(user_agePreferenceFrom))), Float.valueOf(Math.round(Float.valueOf(user_agePreferenceto))));

    }
        if (user_ethnicity.contains(ethnicity_american.getTextOn().toString().split("/")[1].toLowerCase().replace(" ", "")))
            ethnicity_american.toggle();
        else if (user_ethnicity.contains(ethnicity_aisan.getTextOn().toString().toLowerCase()))
            ethnicity_aisan.toggle();
        else if (user_ethnicity.contains(ethnicity_blakc.getTextOn().toString().split("/")[1].toLowerCase().replace(" ", "")))
            ethnicity_blakc.toggle();
        else if (user_ethnicity.contains(ethnicity_latino.getTextOn().toString().split("/")[1].toLowerCase().replace(" ", "")))
            ethnicity_latino.toggle();
        else if (user_ethnicity.contains(ethnicity_hawai.getTextOn().toString().split("/")[1].toLowerCase().replace(" ", "")))
            ethnicity_hawai.toggle();
        else if (user_ethnicity.contains(ethnicity_white.getTextOn().toString().toLowerCase()))
            ethnicity_white.toggle();

        if (user_sexuality.equals(seXual_straight.getTextOn().toString().toLowerCase()))
            seXual_straight.toggle();
        else if (user_sexuality.equals(seXual_lesbain.getTextOn().toString().toLowerCase()))
            seXual_lesbain.toggle();
        else if (user_sexuality.equals(seXual_bisexual.getTextOn().toString().toLowerCase()))
            seXual_bisexual.toggle();
        else if (user_sexuality.equals(seXual_gay.getTextOn().toString().toLowerCase()))
            seXual_gay.toggle();
        else if (user_sexuality.equals(seXual_queer.getTextOn().toString().toLowerCase()))
            seXual_queer.toggle();
        else if (user_sexuality.equals(seXual_lgbtq.getTextOn().toString().toLowerCase()))
            seXual_lgbtq.toggle();
        else if (user_sexuality.equals(seXual_comingout.getTextOn().toString().toLowerCase()))
            seXual_comingout.toggle();
        else if (user_sexuality.equals(seXual_others.getTextOn().toString().toLowerCase()))
            seXual_others.toggle();

        else seXual_others.toggle();
        mainshimmer.stopShimmer();
        loading = false;

      /*  agerange.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {

            }
        });*/

        agerange.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                putfriendsback("agePreferenceFrom", agerange.getValues().get(0) + "");
                putfriendsback("agePreferenceto", agerange.getValues().get(1) + "");
            }
        });


        CompoundButton.OnCheckedChangeListener chhec = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    seXual_straight.setChecked(false);
                    seXual_lesbain.setChecked(false);
                    seXual_bisexual.setChecked(false);
                    seXual_gay.setChecked(false);
                    seXual_queer.setChecked(false);
                    seXual_lgbtq.setChecked(false);
                    seXual_comingout.setChecked(false);
                    seXual_others.setChecked(false);
                    compoundButton.setChecked(true);
                    putfriendsback("sexuality", compoundButton.getText().toString());
                }
            }
        };


        CompoundButton.OnCheckedChangeListener chhethni = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ethnicity_american.setChecked(false);
                    ethnicity_aisan.setChecked(false);
                    ethnicity_blakc.setChecked(false);
                    ethnicity_latino.setChecked(false);
                    ethnicity_hawai.setChecked(false);
                    ethnicity_white.setChecked(false);
                    compoundButton.setChecked(true);
                    putfriendsback("ethinicity", compoundButton.getText().toString());

                }
            }
        };

        CompoundButton.OnCheckedChangeListener checkgender = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    interstdin_female.setChecked(false);
                    interstdin_male.setChecked(false);
                    interstdin_both.setChecked(false);
                    compoundButton.setChecked(true);
                    putfriendsback("interestedIn", compoundButton.getText().toString());
                }
            }
        };


        CompoundButton.OnCheckedChangeListener checklokingfor = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    lookingfor_Frend.setChecked(false);
                    lookingfor_both.setChecked(false);
                    lookingfor_dating.setChecked(false);
                    compoundButton.setChecked(true);
                    putfriendsback("lookingFor", compoundButton.getText().toString());
                }
            }
        };


        seXual_lesbain.setOnCheckedChangeListener(chhec);
        seXual_bisexual.setOnCheckedChangeListener(chhec);
        seXual_gay.setOnCheckedChangeListener(chhec);
        seXual_queer.setOnCheckedChangeListener(chhec);
        seXual_lgbtq.setOnCheckedChangeListener(chhec);
        seXual_comingout.setOnCheckedChangeListener(chhec);
        seXual_others.setOnCheckedChangeListener(chhec);
        seXual_straight.setOnCheckedChangeListener(chhec);

        ethnicity_american.setOnCheckedChangeListener(chhethni);
        ethnicity_aisan.setOnCheckedChangeListener(chhethni);
        ethnicity_blakc.setOnCheckedChangeListener(chhethni);
        ethnicity_latino.setOnCheckedChangeListener(chhethni);
        ethnicity_hawai.setOnCheckedChangeListener(chhethni);
        ethnicity_white.setOnCheckedChangeListener(chhethni);

        interstdin_female.setOnCheckedChangeListener(checkgender);
        interstdin_male.setOnCheckedChangeListener(checkgender);
        interstdin_both.setOnCheckedChangeListener(checkgender);

        lookingfor_dating.setOnCheckedChangeListener(checklokingfor);
        lookingfor_Frend.setOnCheckedChangeListener(checklokingfor);
        lookingfor_both.setOnCheckedChangeListener(checklokingfor);


    }


    private void getVerifiedStatus() {
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonParam = new JSONObject();
        String uri = portnVariable.com + "verified/status";
        try {
            jsonParam.put("userID", userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                String message = null;
                String auth = null;
                try {
                    if (response.has("auth")) {
                        auth = response.getString("auth");
                    }
                    status = response.getString("status");

                    if (status.equals("success")) {
                        message = response.getString("message");
                        if (message.equals("Pending")) {
                            verify.setBackgroundResource(R.drawable.roundbtnpending);
                            verify.setText("Status Pending");
                            verify.setEnabled(false);

                        } else if (message.equals("Verified")) {
                            verify.setBackgroundResource(R.drawable.roundbtnverified);
                            verify.setText("Account Verified");
                            verify.setEnabled(false);

                        } else if (message.equals("Start") || message.equals("Fail")){
                            verify.setEnabled(true);
                        }
                    }else if (auth != null && auth.equals("authFailed")) {
                        sharedpreferences.edit().remove("userid").commit();
                        sharedpreferences.edit().remove("user_token").commit();
                        showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("authorization", "bearer " + token);
                return header;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);


    }

    private void getNotificationsStatus() {
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = portnVariable.com + "user/Notification/UnRead";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                String message = null;
                String auth = null;

                try {
                    if (response.has("auth")) {
                        auth = response.getString("auth");
                    }

                    status = response.getString("status");

                    if (status.equals("success")) {
                        message = response.getString("message");
                        if (message.equals("No new notifications")) {
                            badge.setVisibility(View.GONE);

                        }else if (auth != null && auth.equals("authFailed")) {
                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();
                            showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                        } else if (message.equals("new notifications")) {
                            badge.setVisibility(View.VISIBLE);

                        }


                    } else if (status.equals("success")) {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                // System.out.println("XXX Volly Error :" + error);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("authorization", "bearer " + token);
                return header;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);
    }

    private void putfriendsback(String feild, String value) {

        mainshimmer.showShimmer(true);
        mainshimmer.startShimmer();
        loading = true;

//        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.setTitle("Fetching friends...");
//        pDialog.show();

        userid = sharedpreferences.getString("userid", "0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "user/field/Update";

        JSONObject jsonParam = new JSONObject();
        try {
            //   jsonParam = remakeiserobj();
            jsonParam.put("userID", userid);
            jsonParam.put("fieldName", feild);
            jsonParam.put("fieldValue", value);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  System.out.println(jsonParam);

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  System.out.println(response.toString()+"\n jsonres");
//                pDialog.dismissWithAnimation();
//                loading = false;
                String status = null;
                String message = null;
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
                    status = response.getString("status");
                      if (status.equals("success")) {

                        message = response.getString("message");

                        if (!message.equals("user has been found")) {

                            //showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
                        } else {
                            expandableListView.setFilterText(selectedcountry);
                            expandableListDetail.put(selectedcountry, countries);

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                //  System.out.println("XXX Volly Error :" + error);

//                pDialog.dismissWithAnimation();
              //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("authorization", "bearer " + token);
                return header;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);
    }

    String selectedcountry = "";
    String seeleltedste = "";
    String seeleltedcity = "";


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    ShimmerFrameLayout mainshimmer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        context = getContext();
        mainshimmer = root.findViewById(R.id.mainshimmer);
        mainshimmer.showShimmer(true); // If auto-start is set to false
        loading = true;
        verify = root.findViewById(R.id.button13);
        connect = root.findViewById(R.id.editTextTextEmailAddress4);
        pushnotif = root.findViewById(R.id.editTextTextEmailAddress7);
        share_app = root.findViewById(R.id.button37);
        privicy = root.findViewById(R.id.editTextTextEmailAddress8);
        term_of_services = root.findViewById(R.id.editTextTextEmailAddress9);
        logout = root.findViewById(R.id.button35);
        deleteacc = root.findViewById(R.id.button36);
        eidt = root.findViewById(R.id.imageButton);

        badge = root.findViewById(R.id.imageView4);

        portnVariable = new PortnVariable();

        phonenumber = root.findViewById(R.id.textView19);
        name = root.findViewById(R.id.editTextTextEmailAddress2);
        onlyName = root.findViewById(R.id.textView56);
        emial = root.findViewById(R.id.textView23);
        img = root.findViewById(R.id.circleImageView);

        lookingfor_dating = root.findViewById(R.id.toggleButton18);
        lookingfor_Frend = root.findViewById(R.id.toggleButton19);
        lookingfor_both = root.findViewById(R.id.toggleButton20);

        interstdin_female = root.findViewById(R.id.button14);
        interstdin_male = root.findViewById(R.id.button15);
        interstdin_both = root.findViewById(R.id.button16);

        agerange = root.findViewById(R.id.agerange);

        ethnicity_american = root.findViewById(R.id.button20);
        ethnicity_aisan = root.findViewById(R.id.button21);
        ethnicity_blakc = root.findViewById(R.id.button22);
        ethnicity_latino = root.findViewById(R.id.button23);
        ethnicity_hawai = root.findViewById(R.id.button24);
        ethnicity_white = root.findViewById(R.id.button25);

        seXual_straight = root.findViewById(R.id.button26);
        seXual_lesbain = root.findViewById(R.id.button27);
        seXual_bisexual = root.findViewById(R.id.button28);
        seXual_gay = root.findViewById(R.id.button29);
        seXual_queer = root.findViewById(R.id.button30);
        seXual_lgbtq = root.findViewById(R.id.button31);
        seXual_comingout = root.findViewById(R.id.button19);
        seXual_others = root.findViewById(R.id.button33);

        userid = sharedpreferences.getString("userid", "0");
        getVerifiedStatus();
        getUser_data();
        getNotificationsStatus();

        FloatingActionButton fabfilter = root.findViewById(R.id.floatingActionButton3);
        fabfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutapi();
            }
        });
        deleteacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("Delete account permanently");
                sweetAlertDialog.setContentText("This account will not be able to restore");
                sweetAlertDialog.setConfirmButton("Delete", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismissWithAnimation();
                        deleteaccapi();
                    }
                });

                sweetAlertDialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                                 }
                });

                sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.show();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getActivity(), AccVerificaition.class);
                main.putExtra(user_phonenumber, "phoneNo");
                startActivity(main);

            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getActivity(), ConnectedAccs.class);
                main.putExtra("facebookId", facebookId);
                main.putExtra("googleId", googleId);
                startActivity(main);
            }
        });
        pushnotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getActivity(), PushNoti.class);
                startActivity(main);
            }
        });
        privicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getActivity(), SettingsPrivacyPolicy.class);
                startActivity(main);
            }
        });
        term_of_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getActivity(), SettingsTermsOfUse.class);
                startActivity(main);

            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getActivity(), MyProfile.class);
                main.putExtra("Hello", "SettingsDirect");
                startActivity(main);
            }
        });

        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    share_app.setTextColor(ContextCompat.getColor(context, R.color.orangeasli));
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Dategoal");
                    String shareMessage = "\nAre you looking to find a true connection for love relationship and marriage? I recommend DateGoal App. Check it out\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        eidt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getActivity(), EditMyProfile.class);
                startActivity(main);

//                FragmentManager fragmentManager = getChildFragmentManager();
//                UpdateMyProfile updateMyProfile = new UpdateMyProfile();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_settings, updateMyProfile);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                fragmentTransaction.commit();


            }
        });


        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
      /*  for (String country : countries) {
            System.out.println(country);
        }
        System.out.println( "# countries found: " + countries.size());
*/
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        expandableListDetail.put("Country", countries);
        expandableListView = root.findViewById(R.id.spinner2);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        NoScrollExListView expandableListView2 = root.findViewById(R.id.spinner3);
        expandableListView2.setVisibility(View.GONE);
        NoScrollExListView expandableListView1 = root.findViewById(R.id.spinner1);
        expandableListView1.setVisibility(View.GONE);

        expandableListView.setOnChildClickListener(new NoScrollExListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                parent.collapseGroup(groupPosition);
                parent.setSelectedChild(groupPosition, childPosition, false);
                selectedcountry = countries.get(childPosition);

                setstate(selectedcountry, expandableListView2);

                return false;
            }
        });

        expandableListView2.setOnChildClickListener(new NoScrollExListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                parent.collapseGroup(groupPosition);
                parent.setSelectedChild(groupPosition, childPosition, false);

                seeleltedste = satesdatamap.get("State").get(childPosition);

                setcitis(seeleltedste, expandableListView1);
                return false;
            }
        });

        expandableListView1.setOnChildClickListener(new NoScrollExListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                parent.collapseGroup(groupPosition);
                parent.setSelectedChild(groupPosition, childPosition, false);

                seeleltedcity = citydatamap.get("City").get(childPosition);

                putfriendsback("city", seeleltedcity);

                return false;
            }
        });


        ImageButton imgbtn_expand_agerange = root.findViewById(R.id.imgbtn_expand_agerange);
        ImageButton imgbtn_expand_ethnicity = root.findViewById(R.id.imgbtn_expand_ethnicity);
        ImageButton imgbtn_expand_location = root.findViewById(R.id.imgbtn_expand_location);
        ImageButton imgbtn_expand_lookingfor = root.findViewById(R.id.imgbtn_expand_lookingfor);
        ImageButton imgbtn_expand_gender = root.findViewById(R.id.imgbtn_expand_gender);
        ImageButton imgbtn_expand_sexulpreference = root.findViewById(R.id.imgbtn_expand_sexulpreference);

        ExpandableLayout expandable_layout_age = root.findViewById(R.id.expandable_layout_age);
        ExpandableLayout expandable_layout_city = root.findViewById(R.id.expandable_layout_city);
        ExpandableLayout expandable_layout_ethnicity = root.findViewById(R.id.expandable_layout_ethnicity);
        ExpandableLayout expandable_layout_gender = root.findViewById(R.id.expandable_layout_gender);
        ExpandableLayout expandable_layout_lookingfor = root.findViewById(R.id.expandable_layout_lookingfor);
        ExpandableLayout expandable_layout_sexual = root.findViewById(R.id.expandable_layout_sexual);


        imgbtn_expand_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getRotation() == 0) {
                    view.setRotation(180);
                } else {
                    view.setRotation(0);
                }
                expandable_layout_gender.toggle();

            }
        });

        imgbtn_expand_agerange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getRotation() == 0) {
                    view.setRotation(180);
                } else {
                    view.setRotation(0);
                }
                expandable_layout_age.toggle();

            }
        });

        imgbtn_expand_ethnicity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getRotation() == 0) {
                    view.setRotation(180);
                } else {
                    view.setRotation(0);
                }
                expandable_layout_ethnicity.toggle();

            }
        });

        imgbtn_expand_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getRotation() == 0) {
                    view.setRotation(180);
                } else {
                    view.setRotation(0);
                }
                expandable_layout_city.toggle();

            }
        });

        imgbtn_expand_lookingfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getRotation() == 0) {
                    view.setRotation(180);
                } else {
                    view.setRotation(0);
                }
                expandable_layout_lookingfor.toggle();

            }
        });

        imgbtn_expand_sexulpreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getRotation() == 0) {
                    view.setRotation(180);
                } else {
                    view.setRotation(0);
                }
                expandable_layout_sexual.toggle();

            }
        });


        return root;
    }

    private void deleteaccapi() {

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Processing...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com+"user/remove";

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

                    status = response.getString("status");
                    if(status.equals("success")) {

                        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(getActivity(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ((AppCompatActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        startActivity(intent);
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
               // showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("authorization","bearer " +token);
                return header;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);

    }

    private void logoutapi() {

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Logging out...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String uri = portnVariable.com + "user/logoutUser/" + userid;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
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
                    status = response.getString("status");

                    if (status.equals("success")) {
                        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();


                        Intent intent = new Intent(getActivity(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);


//                        Intent serviceIntent = new Intent(context, MyService.class);
//                        context.stopService(serviceIntent);


                        ((AppCompatActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        startActivity(intent);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                //  System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();
               // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);


            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("authorization", "bearer " + token);
                return header;
            }
        };
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    public void setstate(String cntry, NoScrollExListView expandableListView1) {

        mainshimmer.startShimmer();
        mainshimmer.showShimmer(true);
        loading = true;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {


                satesdatamap = statedata.getData(cntry, getContext());
                List<String> expandableListTitle2 = new ArrayList<String>(satesdatamap.keySet());
                ExpandableListAdapter expandableListAdapter1 = new CustomExpandableListAdapter(getContext(), expandableListTitle2, satesdatamap);


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        expandableListView1.setVisibility(View.VISIBLE);
                        expandableListView1.setAdapter(expandableListAdapter1);
                        putfriendsback("country", selectedcountry);
                        loading = false;

                    }
                });
            }

        });
    }


    public void setcitis(String statename, NoScrollExListView expandableListView1) {

        mainshimmer.startShimmer();
        mainshimmer.showShimmer(true);
        loading = true;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {


                citydatamap = citydata.getData(statename, getContext());
                List<String> expandableListTitle1 = new ArrayList<String>(citydatamap.keySet());
                ExpandableListAdapter expandableListAdapter1 = new CustomExpandableListAdapter(getContext(), expandableListTitle1, citydatamap);


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        expandableListView1.setVisibility(View.VISIBLE);
                        expandableListView1.setAdapter(expandableListAdapter1);


                        putfriendsback("state", seeleltedste);
                        loading = true;
                    }
                });
            }

        });
    }

}