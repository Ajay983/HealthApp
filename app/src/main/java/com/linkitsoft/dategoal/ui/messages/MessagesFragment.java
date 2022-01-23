package com.linkitsoft.dategoal.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.ChatListModel;
import com.linkitsoft.dategoal.Models.FriendsModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.RecyclerAdpaters.MessageRecycler;
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
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

public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;
    RecyclerView recyclerView, recyclerView2;
    MessageRecycler adapter;
    last_message_adapter lastMessageAdapter;
    List<ChatListModel> userModelList;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView badge;
    TextView lablenodata;
    TextView labenomsg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        messagesViewModel =
                new ViewModelProvider(this).get(MessagesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        context = root.getContext();
        portnVariable = new PortnVariable();

        userModelList = new ArrayList<ChatListModel>();


        mainshimmer = root.findViewById(R.id.mainshimmer);
        mainshimmer2 = root.findViewById(R.id.mainshimmer2);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        badge = root.findViewById(R.id.imageView4);
        lablenodata = root.findViewById(R.id.no_frnds);
        labenomsg = root.findViewById(R.id.no_msg);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
        mainshimmer2.showShimmer(true);
        mainshimmer.showShimmer(true);
        mainshimmer.startShimmer();
        mainshimmer2.startShimmer();


        FloatingActionButton fabfilter = root.findViewById(R.id.floatingActionButton3);
        fabfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
            }
        });


        // set up the RecyclerView

        recyclerView = root.findViewById(R.id.recycler1);
        recyclerView2 = root.findViewById(R.id.RecyclerviewMessages);
        messagesrecyclerView = root.findViewById(R.id.RecyclerviewMessages);

        getfriendsapi();
        getmessageapi();
        getNotificationsStatus();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
               if(userModelList.size() != 0){
                   userModelList.clear();
                   lastMessageAdapter.notifyDataSetChanged();

               }
                getmessageapi();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    RecyclerView messagesrecyclerView;
    Context context;
    SharedPreferences sharedpreferences;
    PortnVariable portnVariable;
    public String userid,token;

    List<FriendsModel> friendsModelList = new ArrayList<>();

    ShimmerFrameLayout mainshimmer;
    ShimmerFrameLayout mainshimmer2;


    private void getmessageapi() {

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token","0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "userMessages/chatlist";

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

                    if (status.equals("success")) {
                        message = response.getJSONArray("message");
                        if (message.length() == 0){
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ChatListModel>>() {
                            }.getType();
                            labenomsg.setVisibility(View.VISIBLE);
                            userModelList = gson.fromJson(String.valueOf(message), type);
                            lastMessageAdapter = new last_message_adapter(context, userModelList, userid);
                            recyclerView2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            recyclerView2.setAdapter(lastMessageAdapter);


                        }else {
                            labenomsg.setVisibility(View.GONE);
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ChatListModel>>() {
                            }.getType();
                            userModelList = gson.fromJson(String.valueOf(message), type);
                            lastMessageAdapter = new last_message_adapter(context, userModelList, userid);
                            recyclerView2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            recyclerView2.setAdapter(lastMessageAdapter);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mainshimmer2.showShimmer(false);
                mainshimmer2.hideShimmer();
                mainshimmer2.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
              //  System.out.println("XXX Volly Error :" + error);
                mainshimmer2.showShimmer(false);
                mainshimmer2.hideShimmer();
                mainshimmer2.setVisibility(View.GONE);
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


    private void getfriendsapi() {

        mainshimmer.showShimmer(true);
        mainshimmer.startShimmer();

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token","0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "friend/list/friends";


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

                    status = response.getString("status");
                     if (status.equals("success")) {

                        message = response.getJSONArray("message");

                        if (message.length() == 0) {
                            //label to show you have no friends
                               lablenodata.setVisibility(View.VISIBLE);
                        } else {
                            lablenodata.setVisibility(View.GONE);

                            Gson gson = new Gson();
                            Type type = new TypeToken<FriendsModel>() {
                            }.getType();
                            JSONObject result;
                            String id;
                            for (int i = 0; i < message.length(); i++) {

                                id = ((JSONObject) message.get(i)).getString("_id");

                                result = ((JSONObject) message.get(i)).getJSONObject("Details").put("_id", id);

                                friendsModelList.add(gson.fromJson(String.valueOf(result), type));
                            }

                            adapter = new MessageRecycler(friendsModelList, false, "", R.layout.item_messagefriend);
                            recyclerView.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });

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
              //  System.out.println("XXX Volly Error :" + error);

                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();
                mainshimmer.setVisibility(View.GONE);
              //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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

    private void getNotificationsStatus(){
        userid = sharedpreferences.getString("userid","0");
        token = sharedpreferences.getString("user_token","0");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = portnVariable.com + "user/Notification/UnRead";
        JSONObject jsonParam =new JSONObject();
        try {
            jsonParam.put("userID",userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, url,requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                String message = null;
                String auth = null;

                try {
                    if (response.has("auth")) {
                        auth = response.getString("auth");
                    }

                    status = response.getString("status");

                    if(status.equals("success")){
                        message = response.getString("message");
                        if (message.equals("No new notifications")){
                            badge.setVisibility(View.GONE);

                        }else if (auth != null && auth.equals("authFailed")) {
                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();
                            showdialogAuth("Error", "Token Expired Kindly Login Again", 1);

                        }
                        else if (message.equals("new notifications")){
                            badge.setVisibility(View.VISIBLE);

                        }


                    }
                    else if(status.equals("success")) {


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                System.out.println("XXX Volly Error :"+error);
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



}