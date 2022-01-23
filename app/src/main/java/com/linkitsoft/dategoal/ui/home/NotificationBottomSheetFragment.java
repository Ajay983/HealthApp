package com.linkitsoft.dategoal.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.FriendsModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.RecyclerAdpaters.FreindsRecycler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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


public class NotificationBottomSheetFragment extends BottomSheetDialogFragment {

    private static String grpid;

    public static NotificationBottomSheetFragment newInstance(String grid) {
        NotificationBottomSheetFragment fragment = new NotificationBottomSheetFragment();
        grpid = grid;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FreindsRecycler adapter;
    RecyclerView recyclerView;
    Context context;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    public String userid,token;
    List<FriendsModel> friendsModelList;
    SharedPreferences sharedpreferences;


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottomsheet_addfriends, null);
        dialog.setContentView(contentView);

        friendsModelList = new ArrayList<FriendsModel>();
        context = contentView.getContext();
        portnVariable = new PortnVariable();
        recyclerView = contentView.findViewById(R.id.profilebotomrecycler);


        getfriendsapi();

        FloatingActionButton fab = contentView.findViewById(R.id.floatingActionButton8);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void getfriendsapi() {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Fetching friends...");
        pDialog.show();

        userid = sharedpreferences.getString("userid","0");
        token = sharedpreferences.getString("user_token","0");


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com+"group/members/addList";

        JSONObject jsonParam =new JSONObject();
        try {
            jsonParam.put("userID",userid);
            jsonParam.put("groupID",grpid);

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
                    status = response.getString("status");

                     if(status.equals("success")) {

                        message = response.getJSONArray("message");

                        if(message.length() != 0)
                        {
                            Gson gson = new Gson();
                            Type type = new TypeToken<FriendsModel>(){}.getType();
                            JSONObject result;
                            String id;
                            for(int i=0; i<message.length(); i++ ){

                                id =  ((JSONObject)message.get(i)).getString("_id");

                                result =  ((JSONObject)message.get(i)).getJSONObject("Details").put("_id", id)  ;

                                friendsModelList.add(gson.fromJson(String.valueOf(result), type));
                            }

                            adapter = new FreindsRecycler(friendsModelList, true, grpid,token);
                            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);

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
                //   System.out.println("XXX Volly Error :"+error);

                pDialog.dismissWithAnimation();
             //   showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
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