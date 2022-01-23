
package com.linkitsoft.dategoal.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
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
import com.linkitsoft.dategoal.Models.GroupModel;
import com.linkitsoft.dategoal.Models.NotificationModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.group.Grouprequestadapter;
import com.facebook.shimmer.ShimmerFrameLayout;
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
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends BottomSheetDialogFragment {

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RecyclerView recyclerView;
    RecyclerView frecyclerView;
    RecyclerView LikerecyclerView;
    RecyclerView MatchrecyclerView;
    RecyclerView FavtrecyclerView;

    Context context;
    SharedPreferences sharedpreferences;
    PortnVariable portnVariable;
    public String userid,token;
    List<GroupModel> groupModelList;
    List<FriendsModel> friendsModelList;
    List<NotificationModel> notificationModelList;
    List<NotificationModel> notificationModelList1;
    List<NotificationModel> notificationModelList2;
    List<NotificationModel> notificationModelList3;
    List<NotificationModel> notificationModelList4;
    Grouprequestadapter adapter;

    TextView grprewuest;
    TextView likerewuest;
    TextView frerewuest;
    TextView mtchrewuest;
    TextView fvtrewuest;

    TextView nothingofund;

    Boolean dismiss1, dismiss2;

    ShimmerFrameLayout mainshimmer;
    String view = "";


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottomsheet_notification, null);
        dialog.setContentView(contentView);


        notificationModelList = new ArrayList<NotificationModel>();
        notificationModelList1 = new ArrayList<NotificationModel>();
        notificationModelList2 = new ArrayList<NotificationModel>();
        notificationModelList3 = new ArrayList<NotificationModel>();
        notificationModelList4 = new ArrayList<NotificationModel>();

        context = contentView.getContext();
        portnVariable = new PortnVariable();

        recyclerView = contentView.findViewById(R.id.friendsbotomrecycler3);
        frecyclerView = contentView.findViewById(R.id.profilebotomrecycler);
        LikerecyclerView = contentView.findViewById(R.id.friendsbotomrecycler23);
        FavtrecyclerView = contentView.findViewById(R.id.friendsbotomrecycler1);
        MatchrecyclerView = contentView.findViewById(R.id.matchrecyler);

        nothingofund = contentView.findViewById(R.id.textView52);


        frerewuest = contentView.findViewById(R.id.textView22);
        grprewuest = contentView.findViewById(R.id.textView23);
        likerewuest = contentView.findViewById(R.id.textView18);
        fvtrewuest = contentView.findViewById(R.id.textView223);
        mtchrewuest = contentView.findViewById(R.id.textView53);

        mainshimmer = contentView.findViewById(R.id.mainshimmer);
        mainshimmer.showShimmer(true);
        mainshimmer.startShimmer();

        getNotification(dialog);

        FloatingActionButton fab = contentView.findViewById(R.id.floatingActionButton8);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public void hiderecycler(String recyclername){

        if(recyclername.equals("grp")){
            recyclerView.setVisibility(View.GONE);
            grprewuest.setVisibility(View.GONE);
        }
        else if (recyclername.equals("frnd")){
            frecyclerView.setVisibility(View.GONE);
            frerewuest.setVisibility(View.GONE);
        }
        else if (recyclername.equals("match")){
            MatchrecyclerView.setVisibility(View.GONE);
            mtchrewuest.setVisibility(View.GONE);
        }

    }

    public void getNotification(Dialog dialog) {

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token","0");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String uri = portnVariable.com + "user/notification";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // pDialog.dismissWithAnimation();

                String status = null;
                JSONObject message;
                JSONArray liked = null;
                JSONArray friend = null;
                JSONArray favorite = null;
                JSONArray match = null;
                JSONArray group = null;
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

                    if (status.equals("success")) {

                        message = response.getJSONObject("message");
                        liked = message.getJSONArray("liked");
                        friend = message.getJSONArray("friend");
                        favorite = message.getJSONArray("favorite");
                        match = message.getJSONArray("match");
                        group = message.getJSONArray("group");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<NotificationModel>>() {
                        }.getType();


                        notificationModelList = gson.fromJson(String.valueOf(liked), type);
                        notificationModelList1 = gson.fromJson(String.valueOf(friend), type);
                        notificationModelList2 = gson.fromJson(String.valueOf(favorite), type);
                        notificationModelList3 = gson.fromJson(String.valueOf(match), type);
                        notificationModelList4 = gson.fromJson(String.valueOf(group), type);


                        if (liked.length() <=0 && friend.length() <= 0 && favorite.length() <= 0 && match.length() <=0
                         && group.length() <= 0 ){
                            nothingofund.setVisibility(View.VISIBLE);
                        }



                        if (notificationModelList1.size() != 0) {
                            frecyclerView.setVisibility(View.VISIBLE);
                            frerewuest.setVisibility(View.VISIBLE);
                            frecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            adapter = new Grouprequestadapter(context, NotificationFragment.this ,notificationModelList1, userid, true, true,dialog, token);
                            adapter.notifyDataSetChanged();
                            frecyclerView.setAdapter(adapter);
                            frecyclerView.setHasFixedSize(true);
                        }
                        if (notificationModelList4.size() != 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            grprewuest.setVisibility(View.VISIBLE);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            adapter = new Grouprequestadapter(context,NotificationFragment.this , notificationModelList4,
                                    userid, true, true, dialog,token);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);
                        }
                        if (notificationModelList.size() != 0) {
                            LikerecyclerView.setVisibility(View.VISIBLE);
                            likerewuest.setVisibility(View.VISIBLE);
                            LikerecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            adapter = new Grouprequestadapter(context,NotificationFragment.this , notificationModelList, userid, true, true,dialog, token);
                            adapter.notifyDataSetChanged();
                            LikerecyclerView.setAdapter(adapter);
                            LikerecyclerView.setHasFixedSize(true);
                        }
                        if (notificationModelList2.size() != 0) {
                            FavtrecyclerView.setVisibility(View.VISIBLE);
                            fvtrewuest.setVisibility(View.VISIBLE);
                            FavtrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            adapter = new Grouprequestadapter(context,NotificationFragment.this , notificationModelList2, userid, true, true, dialog,token);
                            adapter.notifyDataSetChanged();
                            FavtrecyclerView.setAdapter(adapter);
                            FavtrecyclerView.setHasFixedSize(true);
                        }
                        if (notificationModelList3.size() != 0) {
                            MatchrecyclerView.setVisibility(View.VISIBLE);
                            mtchrewuest.setVisibility(View.VISIBLE);
                            MatchrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            adapter = new Grouprequestadapter(context,NotificationFragment.this , notificationModelList3, userid, true, true,dialog, token);
                            adapter.notifyDataSetChanged();
                            MatchrecyclerView.setAdapter(adapter);
                            MatchrecyclerView.setHasFixedSize(true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();
                mainshimmer.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                //   System.out.println("XXX Volly Error :" + error);
                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();
                mainshimmer.setVisibility(View.GONE);
                //  pDialog.dismissWithAnimation();
               // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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