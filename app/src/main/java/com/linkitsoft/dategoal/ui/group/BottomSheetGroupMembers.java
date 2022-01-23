package com.linkitsoft.dategoal.ui.group;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.GroupMembersModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
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

public class BottomSheetGroupMembers extends BottomSheetDialogFragment {

    private static String grpid,token;
    public static Boolean isAdmin;

    public static BottomSheetGroupMembers newInstance(String grid,String tok,Boolean isad) {
        BottomSheetGroupMembers fragment = new BottomSheetGroupMembers();
        grpid = grid;
        token = tok;
        isAdmin = isad;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    GroupMembersRecycler adapter;
    RecyclerView recyclerView;
    Context context;
    PortnVariable portnVariable;
    public String userid;
    List<GroupMembersModel> friendsModelList;
    SharedPreferences sharedpreferences;


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottomsheet_group_members, null);
        dialog.setContentView(contentView);

        friendsModelList = new ArrayList<GroupMembersModel>();
        context = contentView.getContext();
        portnVariable = new PortnVariable();
        recyclerView = contentView.findViewById(R.id.profilebotomrecycler);
        getGroupMembers(dialog);
        FloatingActionButton fab = contentView.findViewById(R.id.floatingActionButton8);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));


    }


    private void getGroupMembers(Dialog dialog) {
        userid = sharedpreferences.getString("userid", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String uri = portnVariable.com + "group/members/" + grpid;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                JSONObject data;
                JSONArray message = null;
                String auth = null;
                try {

                    if (response.has("status")){
                        status = response.getString("status");
                    message = response.getJSONArray("message");

                     if (status.equals("success")) {


                        if (message.length() != 0) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<GroupMembersModel>() {
                            }.getType();
                            JSONObject result;
                            String id;
                            for (int i = 0; i < message.length(); i++) {

                                result = ((JSONObject) message.get(i)).getJSONObject("Details");

                                friendsModelList.add(gson.fromJson(String.valueOf(result), type));
                            }


                            adapter = new GroupMembersRecycler(friendsModelList, grpid, userid, isAdmin, token, dialog);
                            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);

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
             //   System.out.println("XXX Volly Error :" + error);


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
