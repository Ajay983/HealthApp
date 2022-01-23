package com.linkitsoft.dategoal.ui.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.GroupModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.RecyclerAdpaters.DeleteGroupRecycler;
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DeleteGroup extends AppCompatActivity {
    String userid, token;
    SharedPreferences sharedpreferences;
    ImageButton back;
    PortnVariable portnVariable;
    ImageView badge;
    FloatingActionButton noti;
    DeleteGroupRecycler adapter;
    List<GroupModel> groupModelList;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_group);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        portnVariable = new PortnVariable();
        groupModelList = new ArrayList<GroupModel>();
        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        back = findViewById(R.id.imageButton20);
        badge = findViewById(R.id.imageView4);
        noti = findViewById(R.id.floatingActionButton3);
        recyclerView = findViewById(R.id.rcyclr1);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getSupportFragmentManager(), "Notifications");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getNotificationsStatus();
        getMyGroups();

    }

    private void getMyGroups() {
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String uri = portnVariable.com + "group/userGroups";

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("PageNo", "1");
            jsonParam.put("Count", "1000");
            jsonParam.put("userID", userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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
                    if(response.has("status")) {

                        status = response.getString("status");

                    if (status.equals("success")) {

                        message = response.getJSONArray("message");

                        if (message.length() == 0) {
//                            lablenodata.setVisibility(View.VISIBLE);
                            adapter = new DeleteGroupRecycler(groupModelList, userid, token);
                            recyclerView.setLayoutManager(new LinearLayoutManager(DeleteGroup.this, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);


                        } else {

                            Gson gson = new Gson();
                            Type type = new TypeToken<List<GroupModel>>() {
                            }.getType();
                            groupModelList = gson.fromJson(String.valueOf(message), type);
                            adapter = new DeleteGroupRecycler(groupModelList, userid, token);
                            recyclerView.setLayoutManager(new LinearLayoutManager(DeleteGroup.this, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);

                        }
                    }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                mainshimmer.showShimmer(false);
//                mainshimmer.hideShimmer();
//                mainshimmer.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                //  System.out.println("XXX Volly Error :" + error);


//                mainshimmer.showShimmer(false);
//                mainshimmer.hideShimmer();
//                mainshimmer.setVisibility(View.GONE);

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
                    if(response.has("status")) {
                    status = response.getString("status");

                    if (status.equals("success")) {
                        message = response.getString("message");
                        if (message.equals("No new notifications")) {
                            badge.setVisibility(View.GONE);

                        } else if (message.equals("new notifications")) {
                            badge.setVisibility(View.VISIBLE);
                        }
                    } }
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

    public void showdialogAuth(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(DeleteGroup.this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(DeleteGroup.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }


    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }
}