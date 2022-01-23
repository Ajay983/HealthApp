
package com.linkitsoft.dategoal.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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
import com.linkitsoft.dategoal.Models.PeopleModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.messages.ChatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class Swipe extends AppCompatActivity {

    ColorStateList def;
    TextView item1;
    TextView item2;
    TextView item3;

    TextView select;


    public void initcustomtab() {
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        item1.setOnClickListener(myoc);
        item2.setOnClickListener(myoc);
        item3.setOnClickListener(myoc);
        select = findViewById(R.id.select);
        def = item2.getTextColors();
    }


    View.OnClickListener myoc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.item1) {
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    try {
                        window.setNavigationBarColor(Color.parseColor("#fff"));
                    } catch (Exception ex) {
                    }
                    window.setStatusBarColor(getResources().getColor(R.color.white));
                }
                finish();

                overridePendingTransition(R.anim.lefttrigh,
                        R.anim.righttoleft);
            } else if (view.getId() == R.id.item2) {
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(getResources().getColor(R.color.white));
                }
                finish();

                overridePendingTransition(R.anim.righttoleft,
                        R.anim.righttoleft);
            } else if (view.getId() == R.id.item3) {

                //viewPager.setCurrentItem(2);

            }
        }
    };


    SharedPreferences sharedpreferences;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    public String userid, token;
    public String other_id;
    public String other_user_id;
    public String photos;
    public String other_name;
    List<PeopleModel> userModelList;
    String pid;
    Swipe_Profile_Adapter adapter;
    CircleImageView circleImageView;
    RippleBackground rippleBaackground;
    CardStackView cardStackView;
    SwipeAnimationSetting setting;
    RewindAnimationSetting revsetting;
    CardStackLayoutManager cardStackLayoutManager;
    ConstraintLayout swipe_layout;
    String other_ids;
    ConstraintLayout nobodyfound;
    String other_user_photo;
    String for_like_id;


    //ShimmerFrameLayout mainshimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
        sharedpreferences = getSharedPreferences("MyPrefs", 0);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        circleImageView = findViewById(R.id.centerImage);
        userModelList = new ArrayList<PeopleModel>();
        portnVariable = new PortnVariable();
        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        initcustomtab();


        item1.setTextColor(def);
        item3.setTextColor(getResources().getColor(R.color.orangeasli, this.getTheme()));
        item2.setTextColor(def);

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        photos = sharedpreferences.getString("photo1", "0");


        Picasso.get().load(photos).into(circleImageView);
        cardStackView = findViewById(R.id.cardstack);


        nobodyfound = findViewById(R.id.nodody);

        rippleBaackground = findViewById(R.id.content);
        rippleBaackground.startRippleAnimation();

        swipe_layout = findViewById(R.id.swipe_layout);

        adapter = new Swipe_Profile_Adapter(Swipe.this, userModelList, userid, token);


        cardStackLayoutManager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {
                if (direction == Direction.Right) {
//                    getfSwipelistapi();
                    likeUser(other_user_id);


                }

                //will implement dislike in end

                else if (direction == Direction.Left) {
                    adapter.b = 0;
                    getfSwipelistapi();


                }

            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {

                other_user_id = adapter.userModelList.get(cardStackLayoutManager.getTopPosition())._id;

            }

            @Override
            public void onCardDisappeared(View view, int position) {


            }
        });


        setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();

        revsetting = new RewindAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new DecelerateInterpolator())
                .build();

        cardStackLayoutManager.setRewindAnimationSetting(revsetting);
        cardStackLayoutManager.setSwipeAnimationSetting(setting);
        cardStackLayoutManager.setStackFrom(StackFrom.None);
        cardStackLayoutManager.setCanScrollVertical(false);
        cardStackView.setLayoutManager(cardStackLayoutManager);
        getfSwipelistapi();


    }


    private void getfSwipelistapi() {
        adapter.b = 0;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "user/UserView";
        String latitude = sharedpreferences.getString("latitude", null);
        String longitude = sharedpreferences.getString("longitude", null);
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
//            jsonParam.put("lat", 67.0167);
//            jsonParam.put("lng", 24.8532);
            jsonParam.put("lng", longitude);
            jsonParam.put("lat", latitude);
//            jsonParam.put("lng", 23.8426943813556);
//            jsonParam.put("lat", 61.4644750214197);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //  pDialog.dismissWithAnimation();
                System.out.println("Check Response line 198  "+response);

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

                    if (status.equals("success")) {
                        message = response.getJSONArray("message");
                        //  String p1 = message.getString(0);

                        if (message.length() == 0) {
                            rippleBaackground.stopRippleAnimation();
                            rippleBaackground.setVisibility(View.GONE);
                            circleImageView.setVisibility(View.GONE);
                            nobodyfound.setVisibility(View.VISIBLE);


                        } else {

                            Gson gson = new Gson();
                            Type type = new TypeToken<List<PeopleModel>>() {
                            }.getType();
                            userModelList = gson.fromJson(String.valueOf(message), type);
                            adapter = new Swipe_Profile_Adapter(Swipe.this, userModelList, userid, token);
                            cardStackView.setAdapter(adapter);
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
                //    System.out.println("XXX Volly Error :" + error);
                //   pDialog.dismissWithAnimation();
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

    private void likeUser(String oid) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "user/like/user";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            jsonParam.put("LikeUserID", oid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String status = null;
                String msg;
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
                    if (response.has("message")) {
                        msg = response.getString("message");

                    if (msg.equals("Matched Successfully!")) {
//                        showdialog1("Matched", "User Matched", 2);

                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(Swipe.this, SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Yay! You have a match");
                        sweetAlertDialog.setContentText("Now you can connect with your match");
                        sweetAlertDialog.setConfirmButton("Say Hi", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent(Swipe.this, ChatActivity.class);
                                intent.putExtra("noti_id", oid);
                                startActivity(intent);
                                finish();


                            }
                        });

                        sweetAlertDialog.setCancelButton("Ignore", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.dismissWithAnimation();

                            }
                        });

                        sweetAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                        sweetAlertDialog.show();


                    } else if (msg.equals("User Liked Successfully!")) {


                    } else if (msg.equals("Already Matched!")) {
                        showdialog1("Matched", "User Matched", 2);

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
                //  System.out.println("XXX Volly Error :" + error);
                //   pDialog.dismissWithAnimation();
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


    public void showdialog1(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setConfirmText("Say Hi")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = new Intent(Swipe.this, ChatActivity.class);
                        intent.putExtra("noti_id", other_user_id);
                        startActivity(intent);
                        finish();
//                        startActivity(new Intent(Swipe.this, ChatActivity.class));
//                        finish();
                    }
                })
                .setCancelText("Ignore")
                .setContentText(content);
        onWindowFocusChanged(true);
        sd.show();


    }

    public void showdialogAuth(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(Swipe.this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(Swipe.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }


}