package com.linkitsoft.dategoal.ui.Profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Bottomsheet_profile_friends#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Bottomsheet_profile_friends extends BottomSheetDialogFragment {

    private static String prid;
    private static String name;
    private static String token;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    public static Bottomsheet_profile_friends newInstance(String pid, String nam,String tok) {
        Bottomsheet_profile_friends fragment = new Bottomsheet_profile_friends();
        prid =  pid;
        name =  nam;
        token = tok;
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
    public String userid;
    List<FriendsModel> friendsModelList;
    TextView pname;
    TextView no_friend;
    SharedPreferences sharedpreferences;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottomsheet_profile_friends, null);
        dialog.setContentView(contentView);
        setWhiteNavigationBar(dialog);
        friendsModelList = new ArrayList<FriendsModel>();
        context = contentView.getContext();
        portnVariable = new PortnVariable();
        recyclerView = contentView.findViewById(R.id.profilebotomrecycler);
        pname = contentView.findViewById(R.id.textView20);
        no_friend = contentView.findViewById(R.id.textView42);
        pname.setText(name+"'s Friends");

        getfriendsapi();

       /* String[] data = {"Simran Jakranda,https://i.pinimg.com/564x/b8/03/78/b80378993da7282e58b35bdd3adbce89.jpg,0", "Wasif Ali,https://wallpaperaccess.com/full/2213424.jpg,0", "fffk,https://media1.popsugar-assets.com/files/thumbor/DRKaReuIoQvFSthlzcbVnfj9hhQ/fit-in/728xorig/filters:format_auto-!!-:strip_icc-!!-/2020/03/16/237/n/1922398/d2a2fc375e7055358f6460.62104801_/i/post-malone-best-performance-pictures.jpg,0", "Paul Rudd,https://media1.popsugar-assets.com/files/thumbor/DRKaReuIoQvFSthlzcbVnfj9hhQ/fit-in/728xorig/filters:format_auto-!!-:strip_icc-!!-/2020/03/16/237/n/1922398/d2a2fc375e7055358f6460.62104801_/i/post-malone-best-performance-pictures.jpg,0", "Rameez Asghar,https://s3.amazonaws.com/pixpa.com/com/articles/1562328087-653328-rodrigo-soares-8bfwbuksqqo-unsplashjpg.png,1", "New Entry,https://media.istockphoto.com/photos/angry-lion-roaring-picture-id1184240863?k=6&m=1184240863&s=612x612&w=0&h=rUmWeSiYspBwgfsgPBH8-4fyWJvMiylYDA2KIxlWpx0=,1"};
        // set up the RecyclerView
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(contentView.getContext(), numberOfColumns));
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(contentView.getContext(), data);
        adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);*/


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


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com+"friend/list/friends";

        JSONObject jsonParam =new JSONObject();
        try {

            jsonParam.put("PageNo","1");
            jsonParam.put("Count","1000");
            jsonParam.put("userID",prid);

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
                    if (response.has("status")) {

                    status = response.getString("status");

                        if(status.equals("success")) {

                            message = response.getJSONArray("message");

                            if(message.length() == 0){
                                no_friend.setVisibility(View.VISIBLE);

                                //showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
                                //label to show you have no friends
                            }
                            else
                            {
                                Gson gson = new Gson();
                                Type type = new TypeToken<FriendsModel>(){}.getType();
                                JSONObject result;
                                String id;
                                for(int i=0; i<message.length(); i++ ){

                                    id =  ((JSONObject)message.get(i)).getString("_id");

                                    result =  ((JSONObject)message.get(i)).getJSONObject("Details").put("_id", id);

                                    friendsModelList.add(gson.fromJson(String.valueOf(result), type));
                                }

                                adapter = new FreindsRecycler(friendsModelList, false, "",token);
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