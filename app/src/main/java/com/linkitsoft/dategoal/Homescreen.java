package com.linkitsoft.dategoal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.ui.group.Groupchat;
import com.linkitsoft.dategoal.ui.messages.ChatActivity;
import com.linkitsoft.dategoal.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import FirebaseService.MyFirebaseMessagingService;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.socket.emitter.Emitter;

public class Homescreen extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public String userid,token;
    App_Socket app_socket;
    BottomNavigationView navView;
    public String other_msg;
    String type, groupID;
    NotificationManager notificationManager;
    PendingIntent resultPendingIntent;
    PortnVariable portnVariable;
    String userID1;
    NotificationCompat.Builder notificationBuilder;
    String name;
    String senderID;
    Uri notificationSoundUri;
    String desc;
    ChatActivity chatActivity;
    Groupchat groupchat;
    String check, grp_check, grp_check_name, grp_check_img;
    Intent intent;
    String photo1;
    Intent resultIntent;
    NavController navController;
    public static boolean to;
    public static boolean req_to, grp_req;
    String group_detail_name, group_detail_image;
    SettingsFragment settingsFragment;

    MyFirebaseMessagingService myFire;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        app_socket = new App_Socket();
        portnVariable = new PortnVariable();
        chatActivity = new ChatActivity();
        groupchat = new Groupchat();
        myFire = new MyFirebaseMessagingService();



        settingsFragment = new SettingsFragment();

//        intent = new Intent(this, MyService.class);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

//        stopService(intent);



//        resultIntent = new Intent(this, Homescreen.class);
//        resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }




        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }




        navView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_friends, R.id.navigation_messages, R.id.navigation_groups, R.id.navigation_settings)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        userid = sharedpreferences.getString("userID", "0");
        token = sharedpreferences.getString("user_token","0");
        userID1 = sharedpreferences.getString("userid", "0");
        app_socket.userid = userID1;
        app_socket.socket.connect();

        app_socket.socket.on(userID1, onNewMessage);
        assert userid != null;


        if (!userid.equals("0")) {
            Intent main = new Intent(Homescreen.this, Login.class);
            startActivity(main);
            finish();
        } else {


        }
//        if (to || req_to || grp_req) {
//            navView.setSelectedItemId(R.id.navigation_home);
//        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("MyNotifications","MyNotifications",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

//        String check = getIntent().getStringExtra("CheckFB");





//        FirebaseMessaging.getInstance().subscribeToTopic("general")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Successfull";
//                        if (!task.isSuccessful()) {
//                            msg = "failed";
//
//                        }
//                        Toast.makeText(Homescreen.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });







    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent);
//        } else {
//            startService(intent);
//        }
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app_socket.socket.disconnect();
        app_socket.socket.off();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent);
//
//        } else {
//            startService(intent);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        app_socket.socket.disconnect();
        app_socket.socket.off();


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        app_socket.userid = userID1;
        app_socket.socket.connect();
        app_socket.socket.on(userID1, onNewMessage);
    }

    public Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        userID1 = sharedpreferences.getString("userid", "0");
                        senderID = data.getString("userId");
                        other_msg = data.getString("message");
                        type = data.getString("type");
                        groupID = data.getString("groupId");

                    } catch (JSONException e) {
                        return;
                    }

                    check = chatActivity.send_id();
                    grp_check = groupchat.send_grp_id();
                    grp_check_img = groupchat.send_group_image();
                    grp_check_name = groupchat.send_grp_name();


                    if (userID1.equals("0")) {

                    } else if (!other_msg.isEmpty() && type.equals("chat") && check != null && check.equalsIgnoreCase(senderID)) {


                    } else if (!other_msg.isEmpty() && type.equals("GroupMessage") && grp_check != null && grp_check.equalsIgnoreCase(groupID)) {


                    } else if (!other_msg.isEmpty() && type.equals("GroupMessage")) {
                        getUSerDetails();
                    } else if (!other_msg.isEmpty() && type.equals("chat")) {

                        getUSerDetails();


                    } else if (other_msg.equals("Liked") && type.equals("noti")) {
                        getUSerDetails();

                    } else if (other_msg.equals("Matched") && type.equals("noti")) {
                        getUSerDetails();

                    } else if (other_msg.equals("AddedToFavorite") && type.equals("noti")) {

                        getUSerDetails();
//                        getNotify();


//                        notificationBuilder = new NotificationCompat.Builder(Homescreen.this, "order-requests")
//                                .setSmallIcon(R.mipmap.ic_launcher)
//                                .setContentTitle("Notification")
//                                .setContentText(name + "   Added You To Favourites")
//                                .setAutoCancel(true)
//                                .setColor(Color.parseColor("#D81B60"))
//                                .setSound(notificationSoundUri)
//                                .setAutoCancel(true)
//                                .setContentIntent(resultPendingIntent);
//
//                        notificationManager.notify(count,notificationBuilder.build())
                    } else if (!other_msg.isEmpty() && type.equals("GroupMessage")) {
                        getUSerDetails();
                    } else if (other_msg.equals("GroupRequest") && type.equals("noti")) {
                        getUSerDetails();
                    } else if (other_msg.equals("GroupRequestAccepted") && type.equals("noti")) {
                        getUSerDetails();
                    } else if (other_msg.equals("FriendRequest") && type.equals("noti")) {
                        getUSerDetails();
                    } else if (other_msg.equals("FriendRequestAccepted")) {
                        getUSerDetails();
                    }


                }
            });
        }
    };
    //final String uri = portnVariable.com + "VendingMachine?vendingId=" + vid + "&username=" + uname + "&password=" + pass;


    private void getUSerDetails() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String uri = portnVariable.com + "user/" + senderID;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                JSONObject data;
                name = null;
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

                        if (status.equals("success") && type.equals("GroupMessage")) {
                            data = response.getJSONObject("Data");
                            name = data.getString("name");
                            getGroupDetails();

                        } else if (status.equals("success")) {
                            data = response.getJSONObject("Data");
                            name = data.getString("name");
                            photo1 = data.getString("Photo1");
                            getNotify();
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
                //showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);


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


    private void getGroupDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "group/details";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("groupId", groupID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String status = null;
                JSONObject message = null;
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
                        message = response.getJSONObject("message");

                        if (status.equals("success")) {

                            group_detail_image = message.getString("profilePic");
                            group_detail_name = message.getString("title");
                            getNotify();

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
               // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
                //   pDialog.dismissWithAnimation();
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


    public void getNotify() {
        getIntent().getStringExtra("fragment_friends_request");
        if (other_msg.equals("AddedToFavorite")) {
            req_to = true;
            desc = name + " Added You To Favourites";
            resultIntent = new Intent(this, Homescreen.class);
            resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        } else if (other_msg.equals("GroupRequestAccepted") && type.equals("noti")) {
            grp_req = true;
            desc = name + " Accepted Your Group Request";
            resultIntent = new Intent(this, Homescreen.class);
            resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        } else if (other_msg.equals("GroupRequest") && type.equals("noti")) {
            grp_req = true;
            desc = name + " Sent You Group Request";
            resultIntent = new Intent(this, Homescreen.class);
            resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        } else if (!other_msg.isEmpty() && type.equals("chat")) {
            to = false;
            desc = name + " Sent You A Message : " + other_msg;
            resultIntent = new Intent(this, ChatActivity.class);
            resultIntent.putExtra("fnrdName", name);
            resultIntent.putExtra("FrID", senderID);
            resultIntent.putExtra("frndPhoto", photo1);
            resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else if (other_msg.equals("Liked") && type.equals("noti")) {
            grp_req = true;
            desc = name + " Liked You";
            resultIntent = new Intent(this, Homescreen.class);
            resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        } else if (other_msg.equals("Matched") && type.equals("noti")) {
            grp_req = true;
            desc = name + " Matched";
            resultIntent = new Intent(this, Homescreen.class);
            resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        } else if (!other_msg.isEmpty() && type.equals("GroupMessage")) {

            to = false;
            desc = name + " Sent Group Message  : " + other_msg;
            resultIntent = new Intent(this, Groupchat.class);
            resultIntent.putExtra("GrID", groupID);
            resultIntent.putExtra("GrImg", group_detail_image);
            resultIntent.putExtra("GrName", group_detail_name);
            resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        } else if (other_msg.equals("FriendRequestAccepted")) {
            req_to = true;
            grp_req = true;
            desc = name + " Accepted Your Friend Request ";
            resultIntent = new Intent(this, Homescreen.class);
            resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        } else if (other_msg.equals("FriendRequest")) {
            to = false;
            req_to = true;
            grp_req = true;
            desc = name + " Sent You Friend Request ";
            resultIntent = new Intent(this, Homescreen.class);
            resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        }


        notificationBuilder = new NotificationCompat.Builder(Homescreen.this, "order-requests")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification")
                .setContentText(desc)
                .setAutoCancel(true)
                .setColor(Color.parseColor("#D81B60"))
                .setSound(notificationSoundUri)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);


        notificationManager.notify(m, notificationBuilder.build());


    }

    public void showdialogAuth(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(Homescreen.this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(Homescreen.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }






    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel("order-requests", adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }


    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }


}