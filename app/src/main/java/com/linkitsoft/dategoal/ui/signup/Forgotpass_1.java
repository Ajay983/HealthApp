package com.linkitsoft.dategoal.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class Forgotpass_1 extends AppCompatActivity {

    ImageButton next;
    ImageButton back;
    EditText phnum;
    String phonenumber;
    String mobilenum;

    PortnVariable portnVariable;
    SweetAlertDialog pDialog;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass_1);
        portnVariable = new PortnVariable();
        phnum = findViewById(R.id.editTextPhone);
        next = findViewById(R.id.imageButton2);
        back = findViewById(R.id.imageButton16);
        sharedpreferences = getSharedPreferences("MyPrefs", 0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phnum.getText().toString().isEmpty() ) {
                    phonenumber = phnum.getText().toString();

                    next.setEnabled(false);
                    pDialog = new SweetAlertDialog(Forgotpass_1.this, SweetAlertDialog.PROGRESS_TYPE);
                   pDialog.setTitle("Processing");
                    pDialog.show();
                    gettaccode(phonenumber);

                }
                else{
                    showdialog("Error","Must fill all required fields",1);
                }
            }
        });

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
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("mobnum", mobilenumber);
                            editor.apply();

                            if(!sharedpreferences.getString("mobnum", null).equals(null)){

                               // finish();
                                startActivity(new Intent(Forgotpass_1.this, ForgotPass_2.class));
//                            ViewPager m = parent.findViewById(R.id.viewPagerhome);
//                            m.setCurrentItem(1);
                            }
                            else {
                                showdialog("Error", "Please check if the storage permission is allowed", 1);
                            }
                        }
                        else if(message.equals("Invalid code Or Phone no!")){
                            showdialog("Warning", "Phone number is invalid", 3);
                        }
                        else if(message.equals("Limit Exceeded! Try again in a while.")){
                            showdialog("Warning", "Tac code limit exceeded, please try again later in a while", 3);
                        }
                        else
                        {
                            showdialog("Error", "There was an error while sending code please try again later.", 3);
                        }

                    }
                    else if(status.equals("Success")){
                      //  showdialog("Warning", "User not found.", 3);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("mobnum", mobilenumber);
                        editor.apply();

                        if(!sharedpreferences.getString("mobnum", null).equals(null)){

                            //finish();
                            startActivity(new Intent(Forgotpass_1.this, ForgotPass_2.class));
//                            ViewPager m = parent.findViewById(R.id.viewPagerhome);
//                            m.setCurrentItem(1);
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