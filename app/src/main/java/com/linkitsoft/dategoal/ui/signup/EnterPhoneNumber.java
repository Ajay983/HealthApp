package com.linkitsoft.dategoal.ui.signup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnterPhoneNumber extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    EditText phnum;
    String phonenumber;
    String mobilenum;

    PortnVariable portnVariable;
    SweetAlertDialog pDialog;
    SharedPreferences sharedpreferences;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context;
    Activity parent;
    String selected_country_code = null;
    String selected_country_name = null;

    CountryCodePicker country;


    public EnterPhoneNumber() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_enter_phone_number, container, false);

        context = getContext();
        portnVariable = new PortnVariable();
        parent = getActivity();
        imagebtnback = parent.findViewById(R.id.imageButton19);
        phnum = root.findViewById(R.id.editTextPhone);
        next = root.findViewById(R.id.imageButton2);
        country = root.findViewById(R.id.country);
        country.detectLocaleCountry(true);
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_country_name = country.getSelectedCountryNameCode();
                selected_country_code = country.getSelectedCountryCode();
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_country_name = country.getSelectedCountryNameCode();
                selected_country_code = country.getSelectedCountryCode();
                if(!phnum.getText().toString().isEmpty() && !selected_country_code.isEmpty() && !selected_country_name.isEmpty()) {
                    phonenumber = "+"+selected_country_code + phnum.getText().toString();

                   // next.setEnabled(false);
                    pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//                    pDialog.setTitle("Processing");
//                    pDialog.show();
                    gettaccode(phonenumber);



                }
                else{
                    showdialog("Error","Must fill all required fields",1);
                }
            }
        });

        return root;
    }

    private void gettaccode(final String mobilenumber) {
//
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("mobnum", mobilenumber);
        editor.apply();

//        if(!sharedpreferences.getString("mobnum", null).equals(null)){
//            ViewPager m = parent.findViewById(R.id.viewPagerhome);
//            m.setCurrentItem(1);
//        }
//        else {
//            showdialog("Error", "Please check if the storage permission is allowed", 1);
//        }


//
//
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonParam = null;
        String url = portnVariable.com+"user/Verify";

        try {
            jsonParam = new JSONObject();
            jsonParam.put("phoneNumber",mobilenumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                pDialog.dismissWithAnimation();
                next.setEnabled(true);

                try {
                    String status =  response.getString("status");
                    String message =  response.getString("message");

                    if(status.equals("Fail")){

                        if(message.equals("Number already exists")){
                            showdialog("Warning", "Phone number already exist", 3);

                        }
                        else if(message.equals("Invalid code Or Phone no!")){
                            showdialog("Warning", "Phone number is invalid", 3);
                        }
                        else if(message.equals("Limit Exceeded! Try again in a while.")){
//                            showdialog("Warning", "Tac code limit exceeded, please try again later in a while", 3);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("mobnum", mobilenumber);
                            editor.apply();

                            if(!sharedpreferences.getString("mobnum", null).equals(null)){
                                ViewPager m = parent.findViewById(R.id.viewPagerhome);
                                m.setCurrentItem(1);
                            }
                            else {
                                showdialog("Error", "Please check if the storage permission is allowed", 1);
                            }
                        }
                        else
                        {
                            showdialog("Error", "There was an error while sending code please try again later.", 3);
                        }

                    }
                   else if(status.equals("Success")){

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("mobnum", mobilenumber);
                        editor.apply();

                        if(!sharedpreferences.getString("mobnum", null).equals(null)){
                            ViewPager m = parent.findViewById(R.id.viewPagerhome);
                            m.setCurrentItem(1);
                        }
                        else {
                            showdialog("Error", "Please check if the storage permission is allowed", 1);
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
              //  System.out.println("XXX Volly Error :"+error);

                pDialog.dismissWithAnimation();
               // showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
                next.setEnabled(true);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(jsonObjectRequest);
    }


    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }
}