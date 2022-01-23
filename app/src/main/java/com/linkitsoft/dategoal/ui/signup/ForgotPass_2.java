package com.linkitsoft.dategoal.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPass_2 extends AppCompatActivity {

    ImageButton next;
    ImageButton back;
    PortnVariable portnVariable;
    EditText val1;
    EditText val2;
    EditText val3;
    EditText val4;
    EditText val5;
    EditText val6;
    SharedPreferences sharedpreferences;

    SweetAlertDialog pDialog;



    Button resend;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_2);

        portnVariable = new PortnVariable();


        sharedpreferences = getSharedPreferences("MyPrefs", 0);

        next = findViewById(R.id.imageButton2);
        back = findViewById(R.id.imageButton15);

        resend = findViewById(R.id.resedbtn);

        setResend();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phnum = sharedpreferences.getString("mobnum", null);

                pDialog = new SweetAlertDialog(ForgotPass_2.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitle("Processing");
                pDialog.show();
                gettaccode(phnum);
            }
        });


        val1 = findViewById(R.id.editTextPhone);
        val2 = findViewById(R.id.editTextPhone2);
        val3 = findViewById(R.id.editTextPhone3);
        val4 = findViewById(R.id.editTextPhone4);

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
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }});

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!val1.getText().toString().isEmpty() && !val2.getText().toString().isEmpty() &&
                        !val3.getText().toString().isEmpty() && !val4.getText().toString().isEmpty()){

                    String code = val1.getText().toString() + val2.getText().toString() + val3.getText().toString() + val4.getText().toString(); /*+ val5.getText().toString() + val6.getText().toString()*/
                    next.setEnabled(false);
                    pDialog = new SweetAlertDialog(ForgotPass_2.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitle("Processing");
                    pDialog.show();
                    verifytaccode(code);

                }
                else{
                    showdialog("Error","Must fill all required fields",1);
                }
            }
        });
    }
    public String phnum;
    private void verifytaccode(String code) {

        phnum = sharedpreferences.getString("mobnum", null);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                pDialog.dismissWithAnimation();
                next.setEnabled(true);

                try {

                    String status =  response.getString("status");
                    String message =  response.getString("message");

                    //   System.out.println("GUJRXXX "+response);
                    if(status.equals("Fail"))
                    {

                        if(message.equals("Invalid code!")){

                            showdialog("Warning", "Invalid code", 3);
                            val1.setText("");
                            val2.setText("");
                            val3.setText("");
                            val4.setText("");
                            val5.setText("");
                            val6.setText("");
                            val1.requestFocus();
                        }
                        else
                        {
                            showdialog("Error", "There was an error while verifying code please try again later.", 3);
                        }
                    }
                    else if(status.equals("Success")){

                       // finish();
                        startActivity(new Intent(ForgotPass_2.this, ForgotPass_3.class));
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
            //    showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonParam = null;
        String url = portnVariable.com+"user/VerifyForgotPass";

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
                pDialog.dismissWithAnimation();
                next.setEnabled(true);

                try {
                    String status =  response.getString("status");
                    String message =  response.getString("message");

                    if(status.equals("Fail")){

                        if(message.equals("Number already exists")){
                            setResend();
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
                //  System.out.println("XXX Volly Error :"+error);

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

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }

}