package com.linkitsoft.dategoal.ui.signup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnterOtp extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    Context context;
    PortnVariable portnVariable;
    Button resend;
    EditText val1;
    EditText val2;
    EditText val3;
    EditText val4;
    EditText val5;
    EditText val6;
    SharedPreferences sharedpreferences;
    TextView aj;

    SweetAlertDialog pDialog;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String phnum;
    Activity parent;
    public String verify;


    public EnterOtp() {
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



    void setResend()
    {

        resend.setEnabled(false);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                resend.setText("Wait " + millisUntilFinished / 1000+" Sec");

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                resend.setText("Resend Code");
                resend.setEnabled(true);

            }

        }.start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_enter_otp, container, false);

        context = getContext();
        parent = getActivity();
        portnVariable = new PortnVariable();

//        phnum = sharedpreferences.getString("mobnum", null);
//        if (phnum == null){
//            Toast.makeText(context, "phone number null", Toast.LENGTH_SHORT).show();
//        }else
//        {
//            Toast.makeText(context, "not null", Toast.LENGTH_SHORT).show();
//        }



        resend = root.findViewById(R.id.resedbtn);

        setResend();

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phnum = sharedpreferences.getString("mobnum", null);
                gettaccode(phnum);
            }
        });
//        if (verify == null){
//
//        }else
//        {
//            ViewPager m = parent.findViewById(R.id.viewPagerhome);
//            m.setCurrentItem(2);
//
//        }





        imagebtnback = parent.findViewById(R.id.imageButton19);

        next = root.findViewById(R.id.imageButton2);

        val1 = root.findViewById(R.id.editTextPhone);
        val2 = root.findViewById(R.id.editTextPhone2);
        val3 = root.findViewById(R.id.editTextPhone3);
        val4 = root.findViewById(R.id.editTextPhone4);

        aj = root.findViewById(R.id.text_code);

        //****************************VAL1********************************
        val1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                { val2.requestFocus(); } }});

        //****************************VAL2********************************
        val2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                { val3.requestFocus(); } }});

        //****************************VAL3********************************
        val3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                { val4.requestFocus(); } }});

        //****************************VAL4********************************
        //****************************VAL5********************************
        val4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()>0)
                {
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }});

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!val1.getText().toString().isEmpty() && !val2.getText().toString().isEmpty() &&
                        !val3.getText().toString().isEmpty() && !val4.getText().toString().isEmpty()){

                    String code = val1.getText().toString() + val2.getText().toString() + val3.getText().toString() + val4.getText().toString();
//                    String code2 = code.replace("\", "");


                    /*+ val5.getText().toString() + val6.getText().toString()*/
                  //  next.setEnabled(false);
//                    pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//                    pDialog.setTitle("Processing");
//                    pDialog.show();
                    verifytaccode(code);

                }
                else{
                    showdialog("Error","Must fill all required fields",1);
                }
            }
        });

        return root;
    }

    private void verifytaccode(String code) {


//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString("verify", "true");
//        editor.apply();
//
//
//        ViewPager m = parent.findViewById(R.id.viewPagerhome);
//        m.setCurrentItem(2);
//
        phnum = sharedpreferences.getString("mobnum", null);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonParam = null;
        String url = portnVariable.com+"user/VerifyCheck";



        try {
            jsonParam = new JSONObject();
            jsonParam.put("phoneNumber",phnum);
            jsonParam.put("VerificationCode",code);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                pDialog.dismissWithAnimation();
                next.setEnabled(true);

                //   System.out.println("GUJRXXX "+response);

                try {

                    String status =  response.getString("status");
                    String message =  response.getString("message");

                    if(status.equals("Fail"))
                    {

                        if(message.contains("Invalid code")){

                            showdialog("Warning", "Invalid code", 3);
                            val1.setText("");
                            val2.setText("");
                            val3.setText("");
                            val4.setText("");
                            val5.setText("");
                            val6.setText("");
                            val1.requestFocus();

                        }
                        else {
                            showdialog("Error", "There was an error while verifying code please try again later.", 3);
//                            verify = "verified";
//                            SharedPreferences.Editor editor = sharedpreferences.edit();
//                            editor.putString("verify",verify);
//                            editor.apply();
//
//
//                            ViewPager m = parent.findViewById(R.id.viewPagerhome);
//                            m.setCurrentItem(2);
                        }
                    }
                    else if(status.equals("Success")){
                        verify = "verified";
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("verify",verify);
                        editor.apply();
                        ViewPager m = parent.findViewById(R.id.viewPagerhome);
                        m.setCurrentItem(2);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // error report
                //   System.out.println("XXX Volly Error :"+error);

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

    private void gettaccode(final String mobilenumber) {

//pDialog.show();

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

                setResend();

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
                            showdialog("Warning", "Tac code limit exceeded, please try again later in a while", 3);
                        }

                    }
                   else if(status.equals("Success")){

                      setResend();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // error report
                //   System.out.println("XXX Volly Error :"+error);

                pDialog.dismissWithAnimation();
              //  showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
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