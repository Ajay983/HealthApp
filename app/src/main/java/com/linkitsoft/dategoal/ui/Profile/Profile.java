package com.linkitsoft.dategoal.ui.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.teresaholfeld.stories.StoriesProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
//import jp.shts.android.storiesprogressview.StoriesProgressView;

public class Profile extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    public String profileid = "";
    public String userid, token;
    String name = null;
    TextView username;
    TextView infoname;
    TextView ycc;
    TextView matchperc;
    TextView friendscount;
    TextView ethinicitytxt;
    ImageView verify;
    Boolean isFriend;
    Boolean isFavorite;
    Boolean isBlocked;
    Boolean isRequest;
    int matching = 0;
    private int i = 0;
    private int DELAY = 5000;
    FloatingActionButton fabh;
    final boolean[] flag = {false};
    ShimmerFrameLayout mainshimmer;

    RecyclerView passionsRecylview;
    StoriesProgressView storiesProgressView;
    ImageView img;
    Button back_img, forw_img;
    int counter = 0;
    String profileurl = null;

    long pressTime = 0L;
    long limit = 500L;
    List<String> list = new ArrayList<String>();
    JSONArray passion = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        username = findViewById(R.id.textView21);
        infoname = findViewById(R.id.textView25);
        ycc = findViewById(R.id.textView24);
        matchperc = findViewById(R.id.textView26);
        friendscount = findViewById(R.id.numberoffriends);
        ethinicitytxt = findViewById(R.id.textView29);
//        profileimg = findViewById(R.id.imageView3);
        verify = findViewById(R.id.imageView5);
        verify.setVisibility(View.GONE);


        portnVariable = new PortnVariable();
        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        profileid = getIntent().getStringExtra("FrID");

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");

        passionsRecylview = findViewById(R.id.grii);


        storiesProgressView = findViewById(R.id.stories);
        img = findViewById(R.id.image);
        back_img = findViewById(R.id.prev);
        forw_img = findViewById(R.id.forward);

        token = sharedpreferences.getString("user_token", "0");


        mainshimmer = findViewById(R.id.mainshimmer);
        mainshimmer.showShimmer(true); // If auto-start is set to false

        FloatingActionButton btn = findViewById(R.id.floatingActionButton12);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bottomsheeet_Profile_more mbottomSheetDialog = Bottomsheeet_Profile_more.newInstance(profileid, isBlocked, isFavorite,
                        isFriend, Profile.this, true, profileurl, name, isRequest);
                mbottomSheetDialog.show(getSupportFragmentManager(), "mybtommore");

            }
        });


        FloatingActionButton back = findViewById(R.id.floatingActionButton10);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CardView friendscard = findViewById(R.id.cardView2);
        friendscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bottomsheet_profile_friends bottomSheetDialog = Bottomsheet_profile_friends.newInstance(profileid, name, token);
                bottomSheetDialog.show(getSupportFragmentManager(), "Bottomsheet_profile_friends");

            }
        });
        fabh = findViewById(R.id.floatingActionButton13);
        fabh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag[0]) {
                    //remove fvt
                    fvrtapi("remove");
                } else {
                    //add fvt
                    fvrtapi("Add");

                }

            }
        });
        CardView comparecard = findViewById(R.id.cardView3);
        comparecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bottomsheet_profile_compare bottomSheetDialog = Bottomsheet_profile_compare.newInstance(matching, passion);
                bottomSheetDialog.show(getSupportFragmentManager(), "Bottomsheet_profile_compare");
            }
        });

       if (profileid != null) {
            getprofileapi();

        }


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storiesProgressView.pause();
            }
        });
//        img.setOnTouchListener(onTouchListener);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storiesProgressView.reverse();
            }
        });
//        back_img.setOnTouchListener(onTouchListener);

        forw_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storiesProgressView.skip();
            }
        });
//        forw_img.setOnTouchListener(onTouchListener);


    }


//    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    pressTime = System.currentTimeMillis();
//                    storiesProgressView.pause();
//                    return false;
//                case MotionEvent.ACTION_UP:
//                    long now = System.currentTimeMillis();
//                    storiesProgressView.resume();
//                    return limit < now - pressTime;
//            }
//            return false;
//        }
//    };

    private void fvrtapi(String urlname) {

        mainshimmer.startShimmer();
        mainshimmer.showShimmer(true);
//        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.setTitle("Updating favorite...");
//        pDialog.show();




        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String uri = portnVariable.com + "favorite/" + urlname;

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("favoriteID", profileid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                pDialog.dismissWithAnimation();

                String status = null;
                String msg = "none";
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

                            fabh.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.heartsel));
                            flag[0] = true;
                            isFavorite = true;

                            msg = "User added to favorite";

                            // showdialog("Success","Successfully added to favorite",2);
                        } else {

                            fabh.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.heart));
                            flag[0] = false;
                            isFavorite = false;

                            msg = "User removed from favorite";


                            // showdialog("Success","Successfully removed from favorite",2);
                        }

                    }

                    Configuration croutonConfiguration = new Configuration.Builder()
                            .setDuration(2000).build();
                    // Define custom styles for crouton
                    Style style = new Style.Builder()
                            .setBackgroundColorValue(getResources().getColor(R.color.orangeasli, Profile.this.getTheme()))
                            .setGravity(Gravity.CENTER)
                            .setConfiguration(croutonConfiguration)
                            .setHeight(300)
                            .setTextColorValue(getResources().getColor(R.color.white, Profile.this.getTheme())).build();
                    // Display notice with custom style and configuration
                    Crouton.showText(Profile.this, msg, style);
                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
              //  System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();
               // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);

                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();
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

    private void getprofileapi() {
        userid = sharedpreferences.getString("userid", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "friend/friendview";
        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("friendID", profileid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                pDialog.dismissWithAnimation();

                String status = null;
                JSONObject message = null;
                int age = 0;
                String city = null;
                String country = null;
                String ethinicity = null;
                int fcount = 0;
                boolean match;
                int veft;
                String profileurl6 = null;
                String profile_pic = null;
                String profileurl2 = null;
                String profileurl3 = null;
                String profileurl4 = null;
                String profileurl5 = null;

                LinkedHashMap<String, Boolean> passionlist = new LinkedHashMap<String, Boolean>();


                try {
                    status = response.getString("status");

                    if (status.equals("success")) {


                        message = response.getJSONObject("message");

                        profile_pic = message.getString("profilePic");


                        profileurl = message.getString("Photo1");
                        profileurl2 = message.getString("Photo2");
                        profileurl3 = message.getString("Photo3");
                        profileurl4 = message.getString("Photo4");
                        profileurl5 = message.getString("Photo5");
                        profileurl6 = message.getString("Photo6");

                        if (profileurl.length() > 0){
                            list.add(profileurl);
                        }
                        if (profileurl2.length() > 0){
                            list.add(profileurl2);
                        }
                        if (profileurl3.length() > 0){
                            list.add(profileurl3);
                        }
                        if (profileurl4.length() > 0){
                            list.add(profileurl4);
                        }
                        if (profileurl5.length() > 0){
                            list.add(profileurl5);
                        }
                        if (profileurl6.length() > 0){
                            list.add(profileurl6);
                        }

                        storiesProgressView.setStoriesCount(list.size());
                        storiesProgressView.setStoryDuration(8000L);
                        story_view();

                       /* if (profileurl.length() > 0 && profileurl2.length() > 0 && profileurl3.length() > 0 && profileurl4.length() > 0
                                && profileurl5.length() > 0 && profileurl6.length() > 0) {


                            list.add(profileurl);
                            list.add(profileurl2);
                            list.add(profileurl3);
                            list.add(profileurl4);
                            list.add(profileurl5);
                            list.add(profileurl6);

                            storiesProgressView.setStoriesCount(list.size());
                            storiesProgressView.setStoryDuration(8000L);
                            story_view();


                        } else if (profileurl.length() > 0 && profileurl2.length() > 0 && profileurl3.length() > 0 && profileurl4.length() > 0
                                && profileurl5.length() > 0) {

                            list.add(profileurl);
                            list.add(profileurl2);
                            list.add(profileurl3);
                            list.add(profileurl4);
                            list.add(profileurl5);
                            storiesProgressView.setStoriesCount(list.size());
                            storiesProgressView.setStoryDuration(8000L);
                            story_view();


                        } else if (profileurl.length() > 0 && profileurl2.length() < 0 && profileurl3.length() > 0 && profileurl4.length() > 0) {
                            list.add(profileurl);
                            list.add(profileurl2);
                            list.add(profileurl3);
                            list.add(profileurl4);
                            storiesProgressView.setStoriesCount(list.size());
                            storiesProgressView.setStoryDuration(8000L);
                            story_view();
                        } else if (profileurl.length() > 0 && profileurl2.length() > 0 && profileurl3.length() > 0) {
                            list.add(profileurl);
                            list.add(profileurl2);
                            list.add(profileurl3);
                            storiesProgressView.setStoriesCount(list.size());
                            storiesProgressView.setStoryDuration(8000L);
                            story_view();

                        } else if (profileurl.length() > 0 && profileurl2.length() > 0) {
                            list.add(profileurl);
                            list.add(profileurl2);
                            storiesProgressView.setStoriesCount(list.size());
                            storiesProgressView.setStoryDuration(8000L);
                            story_view();
                        } else {
                            list.add(profileurl);
                            storiesProgressView.setStoriesCount(list.size());
                            storiesProgressView.setStoryDuration(8000L);
                            story_view();

                        }
*/
                        name = message.getString("name");

                        username.setText(name);
                        infoname.setText(name + "'s Info");

                        age = message.getInt("age");
                        city = message.getString("city");
                        country = message.getString("country");

                        ycc.setText(age + " years • " + city + ", " + country);

                        matching = message.getInt("matching");
                        matchperc.setText(matching + "%");

                        fcount = message.getInt("friends");
                        friendscount.setText(fcount + "");

                        ethinicity = message.getString("ethinicity");
                        ethinicitytxt.setText(ethinicity);

                        veft = message.getInt("isVerifified");
                        if (veft == 0) {
                            verify.setVisibility(View.GONE);
                        } else if (veft == 1) {
                            verify.setVisibility(View.VISIBLE);
                        }


                        isBlocked = message.getBoolean("isBlocked");
                        isFavorite = message.getBoolean("isFavorite");
                        isRequest = message.getBoolean("isRequested");


                        if (isFavorite) {
                            fabh.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.heartsel));
                            flag[0] = true;
                        }

                        isFriend = message.getBoolean("isFriend");

                        passion = message.getJSONArray("passions");

                        if (passion.length() == 0) {


                        } else {


                            for (int i = 0; i < passion.length(); i++) {

                                match = ((JSONObject) passion.get(i)).getBoolean("match");
                                passionlist.put(((JSONObject) passion.get(i)).getString("passion"), match);

                            }

                            FlexboxLayoutManager fblm = new FlexboxLayoutManager(Profile.this, FlexDirection.ROW);
                            fblm.setFlexWrap(FlexWrap.WRAP);
//                            fblm.setAlignContent(AlignContent.STRETCH);
                            fblm.setAlignItems(AlignItems.STRETCH);
                            Passionsadapter adapter = new Passionsadapter(passionlist);
                            passionsRecylview.setLayoutManager(fblm);
                            passionsRecylview.setAdapter(adapter);
                            passionsRecylview.setHasFixedSize(true);


                        }
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
              //  System.out.println("XXX Volly Error :" + error);

                //pDialog.dismissWithAnimation();

                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();

                // showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
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

    public void story_view() {
        Picasso.get().load(list.get(0)).fit().centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(img, new Callback() {
            @Override
            public void onSuccess() {
                storiesProgressView.startStories();
            }

            @Override
            public void onError(Exception e) {

            }
        });

        storiesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
                if (counter < list.size()) {
                    counter++;
                    Picasso.get().load(list.get(counter)).fit().centerCrop().into(img);

                }
            }

            @Override
            public void onPrev() {
                if (counter > 0) {
                    counter--;
                    Picasso.get().load(list.get(counter)).fit().centerCrop().into(img);

                }

            }

            @Override
            public void onComplete() {
                counter = 0;
                Picasso.get().load(list.get(0)).fit().centerCrop().into(img);
                storiesProgressView.startStories();


            }
        });


    }

    public void setfvrtfrombottomsheet(Boolean selected) {
        if (selected) {
            isFavorite = true;
            fabh.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.heartsel));
            flag[0] = true;
        } else {
            fabh.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.heart));
            flag[0] = false;
            isFavorite = false;

        }
    }

    public void setblockfrombottomsheet(Boolean selected) {
        isBlocked = selected;
    }

    public void setfrndfrombottomsheet(Boolean selected, Boolean selectd) {
        isFriend = selected;
        isRequest = selectd;
    }


    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }


    public void showdialogAuth(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(Profile.this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(Profile.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
      //  System.out.println("Resume");

    }


}