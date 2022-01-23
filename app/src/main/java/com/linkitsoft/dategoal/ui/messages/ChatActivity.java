package com.linkitsoft.dategoal.ui.messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.linkitsoft.dategoal.App_Socket;
import com.linkitsoft.dategoal.Homescreen;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.MessageModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.Profile.Profile;
//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    chatadapter chatadapter;
    SharedPreferences sharedpreferences;
    public static String frnd_name, frnd_id, frnd_id2;
    TextView friend_name;
    String photo;
    ImageView img;
    EditText editText_msg;
    PortnVariable portnVariable;
    App_Socket app_socket;
    String userid, token;
    ImageButton imageButton;
    List<MessageModel> userModelList;
    String d;
    String other_msg;
    TextView tv;
    private boolean isReached = false;
    public static String senderIDChat;
    private static String recev_ID;
    Homescreen homescreen;
    RecyclerView recyclerView;
    String noti_id;
    Boolean swipe_like;
    //for hi message duplication (when hi messa
    int c = 0;

//    {
//        try {
//            mSocket = IO.socket("https://dategoal.herokuapp.com/");
//        } catch (URISyntaxException e) {
//        }
//
//
//    }

    //    private void attemptSend(){
//        message = editText_msg.getText().toString().trim();
//        if (message.isEmpty()){
//            return;
//        }
//        editText_msg.setText("");
//    }
//    private Emitter.Listener onNewMessage = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            ChatActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    String message;
//                    try {
//                        username = data.getString("username");
//                        message = data.getString("message");
//                    } catch (JSONException e) {
//                        return;
//                    }
//
//                    // add the message to view
////                    addMessage(username, message);
//                }
//            });
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        portnVariable = new PortnVariable();
        app_socket = new App_Socket();
        homescreen = new Homescreen();


        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        friend_name = findViewById(R.id.textView12);
        img = findViewById(R.id.profile_image2);
        editText_msg = findViewById(R.id.edittext_message);
        imageButton = findViewById(R.id.imageButton11);

        editText_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText_msg.getText().length() == 20 && !isReached) {
                    editText_msg.append("\n");
                    isReached = true;
                }
                if (editText_msg.getText().length() < 10 && isReached) isReached = false;

            }
        });

        frnd_name = getIntent().getExtras().getString("fnrdName");
        frnd_id = getIntent().getExtras().getString("FrID");
        frnd_id2 = getIntent().getExtras().getString("myID");
        photo = getIntent().getExtras().getString("frndPhoto");
        swipe_like = getIntent().getExtras().getBoolean("msg_check");



//
//        if (swipe_like) {
//            editText_msg.setText("hi");
//            sendMessage();
//            swipe_like = false;
//        }

        recyclerView = findViewById(R.id.rcyclr1);


        friend_name.setText(frnd_name);

        Picasso.get().load(photo).into(img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, Profile.class);
                if (userid.equalsIgnoreCase(frnd_id)) {
                    intent.putExtra("FrID", frnd_id2);
                } else {
                    intent.putExtra("FrID", frnd_id);

                }
                startActivity(intent);
                finish();
               // frnd_id = null;
            }
        });

        userModelList = new ArrayList<MessageModel>();
        chatadapter = new chatadapter(ChatActivity.this, userModelList, userid, d);

        noti_id = getIntent().getExtras().getString("noti_id");

        if (noti_id != null) {
            frnd_id = noti_id;
            getNotiIdDetails();
        }
        else {
            getSendMessages();
            updateStatus();
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  if (editText_msg.getText().length() < 0) {
                    if (!TextUtils.isEmpty(editText_msg.getText().toString().trim()))
                    {
                        imageButton.setEnabled(false);
                        sendMessage();
                    }

            }
        });


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        FloatingActionButton fabgoback = findViewById(R.id.floatingActionButton4);
        fabgoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frnd_id = null;
                noti_id = null;
                finish();
            }
        });

        app_socket.userid = userid;
        app_socket.socket.connect();
        app_socket.socket.on(userid, onNewMessage);

    }

    private void getNotiIdDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String uri = portnVariable.com + "user/" + noti_id;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                JSONObject data;
                String name = null;
                String photo1;
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
                        data = response.getJSONObject("Data");
                        name = data.getString("name");
                        photo1 = data.getString("Photo1");
                        friend_name.setText(name);
                        Picasso.get().load(photo1).into(img);
                        if (c == 0){
                            c = 1;
                            String like_msg = "Hi";
                            sendHi(like_msg);
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

    public Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {

                        senderIDChat = data.getString("userId");
                        getSendMessages();
                        updateStatus();
                        recev_ID = data.getString("recieverId");
                    } catch (JSONException e) {
                        return;
                    }

//
                }
            });
        }
    };

    public static String send_id() {
        return frnd_id;
    }

    public static String rev_id() {
        return recev_ID;
    }


    private void updateStatus() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "userMessages/view";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            if (userid.equalsIgnoreCase(frnd_id)) {
                jsonParam.put("friendID", frnd_id2);
            } else {
                jsonParam.put("friendID", frnd_id);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


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
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                //   System.out.println("XXX Volly Error :" + error);
                //   pDialog.dismissWithAnimation();
//                showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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


    private void getSendMessages() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "userMessages";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("pageNo", "1");
            jsonParam.put("Count", 1000);
            jsonParam.put("userID", userid);
            if (userid.equalsIgnoreCase(frnd_id)) {
                jsonParam.put("requestID", frnd_id2);
            } else {
                jsonParam.put("requestID", frnd_id);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String status = null;
                JSONArray message = null;
                try {
                    if (response.has("status")) {
                        status = response.getString("status");
                        if (status.equals("success")) {
                            imageButton.setEnabled(true);
                            message = response.getJSONArray("message");
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<MessageModel>>() {
                            }.getType();
                            userModelList = gson.fromJson(String.valueOf(message), type);
                            String a = String.valueOf(message.getJSONObject(0).get("createdAt"));
                            String d = a.substring(0, 10);
                            chatadapter = new chatadapter(ChatActivity.this, userModelList, userid, d);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, true));

                            recyclerView.setAdapter(chatadapter);
                            chatadapter.notifyItemInserted((userModelList.size() - userModelList.size()));
                            chatadapter.notifyItemChanged(userModelList.size() - userModelList.size());


                            chatadapter.notifyDataSetChanged();


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
                //   pDialog.dismissWithAnimation();
//                showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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


    private void sendHi(String hi_msg){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "userMessages/send";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
//            jsonParam.put("requestID", "60e06d949fe0be000458df54");
//            jsonParam.put("requestID", frnd_id);
            jsonParam.put("message", hi_msg);
            if (userid.equalsIgnoreCase(frnd_id)) {
                jsonParam.put("requestID", frnd_id2);
            } else {
                jsonParam.put("requestID", frnd_id);

            }
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
                    if (response.has("status")) {
                        status = response.getString("status");

                        msg = response.getString("message");
                        if (status.equals("failed") && msg.equals("User is Blocked")) {
                            showdialog("Error", "User is Blocked", 1);
                        } else if (status.equals("success")) {
                            editText_msg.setText("");
                            getSendMessages();
                            updateStatus();
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
//                showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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


    private void sendMessage() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "userMessages/send";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
//            jsonParam.put("requestID", "60e06d949fe0be000458df54");
//            jsonParam.put("requestID", frnd_id);
            jsonParam.put("message", editText_msg.getText().toString());
            if (userid.equalsIgnoreCase(frnd_id)) {
                jsonParam.put("requestID", frnd_id2);
            } else {
                jsonParam.put("requestID", frnd_id);

            }
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
                    msg = response.getString("message");

                    status = response.getString("status");

                    if (status.equals("failed") && msg.equals("User is Blocked")) {
                        showdialog("Error", "User is Blocked", 1);
                    }
                    else if (status.equals("success")) {
                        editText_msg.setText("");
//                        imageButton.setEnabled(true);
                        getSendMessages();
                        updateStatus();
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
                //   pDialog.dismissWithAnimation();
//                showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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

        final SweetAlertDialog sd = new SweetAlertDialog(ChatActivity.this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(ChatActivity.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }

    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                })
                .setContentText(content);
        sd.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // frnd_id = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // frnd_id = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}