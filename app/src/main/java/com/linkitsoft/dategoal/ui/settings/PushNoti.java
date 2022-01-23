package com.linkitsoft.dategoal.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PushNoti extends AppCompatActivity {

    ImageButton imagebtnback;
    ImageView badge;
    SharedPreferences sharedpreferences;
    String userid, token;
    PortnVariable portnVariable;
    FloatingActionButton notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_noti);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        portnVariable = new PortnVariable();


        String notifsallowed = sharedpreferences.getString("allownotifs", "null");
        String notifsallowedmessages = sharedpreferences.getString("allownotifsmessages", "null");
        String notifsallowedlikes = sharedpreferences.getString("allownotifslikes", "null");
        String notifsallowedfriendrequests = sharedpreferences.getString("allownotifsfriendrequests", "null");
        String notifsallowedgrouprequest = sharedpreferences.getString("allownotifsgrouprequest", "null");


        Switch messages = findViewById(R.id.switch1);
        Switch likes = findViewById(R.id.switch4);
        Switch friendrequests = findViewById(R.id.switch6);
        Switch grouprequest = findViewById(R.id.switch7);
        badge = findViewById(R.id.imageView4);
        notification = findViewById(R.id.floatingActionButton3);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getSupportFragmentManager(), "Notifications");
            }
        });

        if (notifsallowed.equals("true") || notifsallowed.equals("null")) {
            if (notifsallowedmessages.equals("true") || notifsallowedmessages.equals("null")) {
                messages.setChecked(true);
            }
            if (notifsallowedlikes.equals("true") || notifsallowedlikes.equals("null")) {
                likes.setChecked(true);
            }
            if (notifsallowedfriendrequests.equals("true") || notifsallowedfriendrequests.equals("null")) {
                friendrequests.setChecked(true);
            }
            if (notifsallowedgrouprequest.equals("true") || notifsallowedgrouprequest.equals("null")) {
                grouprequest.setChecked(true);
            }

        }


        messages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putString("allownotifs", "true");
                    editor.putString("allownotifsmessages", "true");
                    editor.commit();
                } else {
                    editor.putString("allownotifsmessages", "false");
                    editor.commit();
                }
            }
        });
        likes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putString("allownotifs", "true");
                    editor.putString("allownotifslikes", "true");
                    editor.commit();
                } else {
                    editor.putString("allownotifslikes", "false");
                    editor.commit();
                }
            }
        });
        friendrequests.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putString("allownotifs", "true");
                    editor.putString("allownotifsfriendrequests", "true");
                    editor.commit();
                } else {
                    editor.putString("allownotifsfriendrequests", "false");
                    editor.commit();
                }
            }
        });
        grouprequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editor.putString("allownotifs", "true");
                    editor.putString("allownotifsgrouprequest", "true");
                    editor.commit();
                } else {
                    editor.putString("allownotifsgrouprequest", "false");
                    editor.commit();
                }
            }
        });


        imagebtnback = findViewById(R.id.imageButton20);

        imagebtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getNotificationsStatus();

    }

    private void getNotificationsStatus() {
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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

                try {
                    status = response.getString("status");

                    if (status.equals("success")) {
                        message = response.getString("message");
                        if (message.equals("No new notifications")) {
                            badge.setVisibility(View.GONE);

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
              //  System.out.println("XXX Volly Error :" + error);
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

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }

}
