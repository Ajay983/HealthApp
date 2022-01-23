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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
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
import com.linkitsoft.dategoal.ReportUser;
import com.linkitsoft.dategoal.ui.home.Swipe_Profile_Adapter;
import com.linkitsoft.dategoal.ui.messages.ChatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class Bottomsheeet_Profile_more extends BottomSheetDialogFragment {

    SharedPreferences sharedpreferences;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    public String userid, token;
    Context context;
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
    private static Boolean isFriend = false;
    private static Boolean isFavorite = false;
    private static Boolean isBlocked = false;
    private static String image;
    private static String nme;
    public static Boolean request_send = false;


    private static String proid;

    public static Profile proflepage;
    public static Swipe_Profile_Adapter swipepage;

    private static Boolean isprof = false;
    private static Boolean isswipe = false;


    public static Bottomsheeet_Profile_more newInstance(String pid, Boolean isBlocke, Boolean isFavorit, Boolean isFrien,Boolean isRequest, Swipe_Profile_Adapter swippage) {
        Bottomsheeet_Profile_more fragment = new Bottomsheeet_Profile_more();
        proid = pid;
        isFriend = isFrien;
        isFavorite = isFavorit;
        isBlocked = isBlocke;
        swipepage = swippage;
        isswipe = true;
        request_send = isRequest;
        return fragment;
    }

    public static Bottomsheeet_Profile_more newInstance(String pid, Boolean isBlocke, Boolean isFavorit, Boolean isFrien, Profile profilepg,
                                                        Boolean isProfile,String ProfileImg,String name,Boolean isRequest) {
        Bottomsheeet_Profile_more fragment = new Bottomsheeet_Profile_more();
        proid = pid;
        isFriend = isFrien;
        isFavorite = isFavorit;
        isBlocked = isBlocke;
        isprof = isProfile;
        proflepage = profilepg;
        image = ProfileImg;
        nme = name;
        request_send = isRequest;

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button fab,directMessage;
    Button adtofavs;
    Button blkusr;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottomsheet_profile_more, null);
        dialog.setContentView(contentView);
        setWhiteNavigationBar(dialog);
        context = contentView.getContext();
        portnVariable = new PortnVariable();

        fab = contentView.findViewById(R.id.addasfrind);
        directMessage = contentView.findViewById(R.id.buttonMessage);


        if (isFriend) {
            directMessage.setVisibility(View.VISIBLE);
            fab.setText("Remove Friend");
        }
        else if (request_send) {
            fab.setText("Pending Friend Request");
        }
        adtofavs = contentView.findViewById(R.id.adtofavs);
        if (isFavorite) {
            adtofavs.setText("Remove Favorites");
        }
        blkusr = contentView.findViewById(R.id.blkusr);
        if (isBlocked) {
            blkusr.setText("Unblock");
        }

        directMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("FrID", proid);
                intent.putExtra("frndPhoto", image);
                intent.putExtra("fnrdName", nme);
                startActivity(intent);
            }
        });

        Button reprtusr = contentView.findViewById(R.id.reprtusr);

        reprtusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReportUser.class);
                intent.putExtra("reportingUserId", proid);
                startActivity(intent);
            }
        });

        adtofavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFavorite) {
                    //remove fvt
                    fvrtapi("remove", dialog);
                } else {
                    //add fvt
                    fvrtapi("Add", dialog);

                }

            }
        });
        blkusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBlocked) {
                    blockapi("BlockUser", dialog);
                } else {

                    blockapi("RemoveBlockedUser", dialog);
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFriend) {
                    friendapi("Add", dialog);
                } else {
                    friendapi("Remove", dialog);
                }
            }
        });

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void fvrtapi(String urlname, Dialog dialog) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Updating favorite...");
        pDialog.show();

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String uri = portnVariable.com + "favorite/" + urlname;

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("favoriteID", proid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String msg = "none";
                Boolean fvvt = false;
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

                        if (status.equals("success")) {

                            if (urlname.equals("Add")) {

                                msg = "User added to favorite";
                                fvvt = true;
                            } else {
                                msg = "User removed from favorite";
                                fvvt = false;
                            }

                        }

                    Configuration croutonConfiguration = new Configuration.Builder()
                            .setDuration(2000).build();
                    // Define custom styles for crouton
                    Style style = new Style.Builder()
                            .setBackgroundColorValue(getResources().getColor(R.color.orangeasli, getContext().getTheme()))
                            .setGravity(Gravity.CENTER)
                            .setConfiguration(croutonConfiguration)
                            .setHeight(300)
                            .setTextColorValue(getResources().getColor(R.color.white, getContext().getTheme())).build();
                    // Display notice with custom style and configuration
                    Crouton.showText(getActivity(), msg, style);

                    dialog.dismiss();
                    if (isprof) {
                        proflepage.setfvrtfrombottomsheet(fvvt);
                    } else if (isswipe) {

                        swipepage.setfvrtfrombottomsheet(fvvt);

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

                pDialog.dismissWithAnimation();
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

    private void friendapi(String rq, Dialog dialog) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Friend status updating...");
        pDialog.show();

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token","0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonParam = new JSONObject();

        String uri;
        //send request
        if (rq.equals("Add")) {

            uri = portnVariable.com + "friend/request/" + proid;
            try {
                jsonParam.put("requestID", userid);
                jsonParam.put("type", "friend");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //remove from friend
        else {
            uri = portnVariable.com + "friend/removefriend";
            try {
                jsonParam.put("userID", userid);
                jsonParam.put("requestID", proid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String message = null;
                String msg = "none";
                Boolean isfr = false;
                Boolean isre = false;
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

                    if (status.equals("exists")) {

                        message = response.getString("message");

                        if (message.equals("Already Friends!")) {
                            msg = "Already a friend";
                            isfr = true;
                        } else if (message.equals("request already sent!")) {
                            msg = "Request is already in pending";
                            isre = true;
                        }
                    } else if (status.equals("success")) {

                        message = response.getString("message");

                        //send request
                        if (rq.equals("Add")) {

                            if (message.equals("Request Sent Sucessfully")) {

                                msg = "Successfully sent friend request";
                                isre = true;

                            }
                        }
                        //remove from friends
                        else {
                            msg = "Successfully removed from friend";
                            isfr = false;

                        }

                    }
                    Configuration croutonConfiguration = new Configuration.Builder()
                            .setDuration(2000).build();
                    // Define custom styles for crouton
                    Style style = new Style.Builder()
                            .setBackgroundColorValue(getResources().getColor(R.color.orangeasli, getContext().getTheme()))
                            .setGravity(Gravity.CENTER)
                            .setConfiguration(croutonConfiguration)
                            .setHeight(300)
                            .setTextColorValue(getResources().getColor(R.color.white, getContext().getTheme())).build();
                    // Display notice with custom style and configuration
                    Crouton.showText(getActivity(), msg, style);

                    dialog.dismiss();
                    if (isprof) {
                        proflepage.setfrndfrombottomsheet(isfr, isre);
                    } else if (isswipe) {
                        swipepage.setfrndfrombottomsheet(isfr, isre);

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

                pDialog.dismissWithAnimation();
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

    private void blockapi(String uriname, Dialog dialog) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Block status updating...");
        pDialog.show();

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token","0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "user/" + uriname;

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("blockUserID", proid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String message = null;
                String msg = "none";
                boolean isbl = false;

                try {
                    status = response.getString("status");

                   if (status.equals("success")) {
                        message = response.getString("message");

                        if (uriname.equals("BlockUser")) {

                            isbl = true;
                            if (message.equals("User Successfully Blocked!")) {

                                msg = "Successfully blocked";

                            } else if (message.equals("User Already Blocked!")) {

                                msg = "Profile is already blocked";
                            }
                        } else {
                            isbl = false;

                            if (message.equals("User removed successfully!")) {

                                msg = "Successfully Unblocked";

                            } else if (message.equals("User already unblocked!")) {

                                msg = "Profile is already Unblocked";
                            }
                        }
                    }

                    Configuration croutonConfiguration = new Configuration.Builder()
                            .setDuration(2000).build();
                    // Define custom styles for crouton
                    Style style = new Style.Builder()
                            .setBackgroundColorValue(getResources().getColor(R.color.orangeasli, getContext().getTheme()))
                            .setGravity(Gravity.CENTER)
                            .setConfiguration(croutonConfiguration)
                            .setHeight(300)
                            .setTextColorValue(getResources().getColor(R.color.white, getContext().getTheme())).build();
                    // Display notice with custom style and configuration
                    Crouton.showText(getActivity(), msg, style);

                    dialog.dismiss();
                    if (isprof) {
                        proflepage.setblockfrombottomsheet(isbl);
                    } else if (isswipe) {
                        swipepage.setblockfrombottomsheet(isbl);
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

                pDialog.dismissWithAnimation();
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