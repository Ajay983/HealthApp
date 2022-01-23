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
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPass_3 extends AppCompatActivity {

    ImageButton next;
    ImageButton back;
    PortnVariable portnVariable;
    EditText val1;
    EditText val2;

    SweetAlertDialog pDialog;

    SharedPreferences sharedpreferences;

    String phnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_3);
        portnVariable = new PortnVariable();
        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        phnum = sharedpreferences.getString("mobnum", null);


        next = findViewById(R.id.imageButton2);
        back = findViewById(R.id.imageButton14);

        val1 = findViewById(R.id.editTextTextEmailA2ddress);
        val2 = findViewById(R.id.editTextTextPassword);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((!val1.getText().toString().isEmpty() && !val2.getText().toString().isEmpty())){

                    if(val1.getText().toString().equals(val2.getText().toString())) {

                        next.setEnabled(false);
                        pDialog = new SweetAlertDialog(ForgotPass_3.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.setTitle("Processing");
                        pDialog.show();

                        verifytaccode(val1.getText().toString());
                    }
                     else{
                        showdialog("Error","Confirm password does not match",1);
                    }

                }
                else{
                    showdialog("Error","Must fill all required fields",1);
                }
            }
        });
    }

    private void verifytaccode(String code) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonParam = null;
        String url = portnVariable.com+"user/ChangePasswordNo";

        try {
            jsonParam = new JSONObject();
            jsonParam.put("phoneNumber",phnum);
            jsonParam.put("password",code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismissWithAnimation();
                next.setEnabled(true);

                // System.out.println(response);
                try {

                    String status =  response.getString("status");
                    String message =  response.getString("message");

                    if(status.equals("Fail"))
                    {

                        if(message.equals("Invalid code Or Phone no!")){

                            showdialog("Warning", "Unable to change password", 3);
                            val1.setText("");
                            val2.setText("");

                            val1.requestFocus();
                        }
                    }
                    else if(status.equals("Success")){

                        Intent intent = new Intent(ForgotPass_3.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }
}