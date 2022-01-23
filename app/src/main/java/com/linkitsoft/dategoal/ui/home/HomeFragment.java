package com.linkitsoft.dategoal.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Homescreen;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import FirebaseService.MyFirebaseMessagingService;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FloatingActionButton filter;
    Homescreen homescreen = new Homescreen();
    MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();
    PortnVariable portnVariable;
    Context context;
    String userid, token;
    ImageView badge;
    SharedPreferences sharedpreferences;
    android.app.FragmentManager fragmentManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        context = root.getContext();
        portnVariable = new PortnVariable();
        badge = root.findViewById(R.id.imageView4);
        fragmentManager = ((Activity) context).getFragmentManager();



        ViewPager viewPager = root.findViewById(R.id.viewPagerhome);
        viewPager.setAdapter(new Mainhomeadapter(getContext(), getChildFragmentManager(), 2));
        viewPager.setCurrentItem(0);
        FloatingActionButton fabaddfriend = root.findViewById(R.id.floatingActionButton2);
        fabaddfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet_Filter bottomSheetDialog = BottomSheet_Filter.newInstance();
                bottomSheetDialog.show(getParentFragmentManager(), "Filter");
            }
        });

        FloatingActionButton fabfilter = root.findViewById(R.id.floatingActionButton);
        fabfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
            }
        });


//        String check = myFirebaseMessagingService.FB_Noti.toString();
//
//        if (myFirebaseMessagingService.FB_Noti){
//            NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
//            bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
//            myFirebaseMessagingService.FB_Noti = false;
//        }


        if (homescreen.req_to || homescreen.grp_req) {
            NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
            bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
            homescreen.req_to = false;
            homescreen.grp_req = false;
            myFirebaseMessagingService.FB_Noti = false;

        }
        else if (myFirebaseMessagingService.FB_Noti){
            NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
            bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
            homescreen.req_to = false;
            homescreen.grp_req = false;
            myFirebaseMessagingService.FB_Noti = false;
        }
        getNotificationsStatus();


        return root;
    }

    private void getNotificationsStatus() {
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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

                    if (response.has("status")) {
                    status = response.getString("status");
                        if (status.equals("success")) {
                            message = response.getString("message");
                            if (message.equals("No new notifications")) {
                                badge.setVisibility(View.GONE);

                            } else if (message.equals("new notifications")) {
                                badge.setVisibility(View.VISIBLE);

                            }
                        }

                    }
                    if (response.has("auth")) {
                        auth = response.getString("auth");
                         if (auth != null && auth.equals("authFailed")) {
                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();
                           // showdialogAuth("Error", "Token Expired Kindly Login Again", 1);

                             if(!fragmentManager.isDestroyed()) {
                                 Intent intent = new Intent(getActivity(), Login.class);
                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                 ((AppCompatActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                 startActivity(intent);
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
               // System.out.println("XXX Volly Error :" + error);
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
try {
    sd.show();
}
catch (Exception ex){}
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }
}