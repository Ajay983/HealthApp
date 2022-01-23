package com.linkitsoft.dategoal.ui.group;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.App_Socket;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.GrupMessageModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.ui.home.NotificationBottomSheetFragment;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.messages.Group_Sendmessage_adapter;
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
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.emitter.Emitter;

public class Groupchat extends AppCompatActivity {

    public static String grpid;
    public static String gname;
    public static String url;
    public String userid,token;
    CircleImageView grpimg;
    TextView grpname;
    ImageButton img_gallery, img_camera, send_grp_msg_btn;
    String temp;
    EditText edittext_message;
    ImageView img;
    PortnVariable portnVariable;
    SharedPreferences sharedpreferences;
    Group_Sendmessage_adapter group_sendmessage_adapter;
    List<GrupMessageModel> userModelList;
    String other_msg;
    private boolean isReached = false;
    Boolean isAdmin = false;

    App_Socket app_socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        portnVariable = new PortnVariable();
        app_socket = new App_Socket();

        grpname = findViewById(R.id.textView12);
        grpimg = findViewById(R.id.profile_image2);
        img_gallery = findViewById(R.id.image_gallery);
        img_camera = findViewById(R.id.image_camera);
        edittext_message = findViewById(R.id.edittext_message);
        img = findViewById(R.id.image_pic);
        send_grp_msg_btn = findViewById(R.id.imageButton12);




        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token","0");

        grpid = getIntent().getStringExtra("GrID");
        gname = getIntent().getStringExtra("GrName");
        url = getIntent().getStringExtra("GrImg");
        String adminId = getIntent().getStringExtra("adminId");


        FloatingActionButton fabaddfriend = findViewById(R.id.floatingActionButton7);


        if (userid.equals(adminId)){
            fabaddfriend.setVisibility(View.VISIBLE);
            isAdmin = true;

        }
        else {
            fabaddfriend.setVisibility(View.GONE);

        }

        fabaddfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationBottomSheetFragment bottomSheetDialog = NotificationBottomSheetFragment.newInstance(grpid);
                bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });




        grpname.setText(gname);
        Picasso.get().load(url).into(grpimg);


        userModelList = new ArrayList<GrupMessageModel>();
        group_sendmessage_adapter = new Group_Sendmessage_adapter(Groupchat.this, userModelList, userid);


        get_group_messages();

        edittext_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edittext_message.getText().length() == 20 && !isReached) {
                    edittext_message.append("\n");
                    isReached = true;
                }
                if (edittext_message.getText().length() < 10 && isReached) isReached = false;

            }
        });

        send_grp_msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (!TextUtils.isEmpty(edittext_message.getText().toString().trim()))
                    {
                        send_grp_msg_btn.setEnabled(false);
                        send_group_message();
                    }
            }
        });






        FloatingActionButton fabgoback = findViewById(R.id.floatingActionButton4);
        fabgoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grpid = null;
                grpimg = null;
                url = null;
                finish();

            }
        });




        grpimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetGroupMembers bottomSheetDialog = BottomSheetGroupMembers.newInstance(grpid,token,isAdmin);
                bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });






        app_socket.userid = userid;
        app_socket.socket.connect();
        app_socket.socket.on(userid, onNewMessage);

    }


    public static String send_grp_id(){
        return grpid;
    }

    public static String send_group_image(){
        return url;
    }

    public static String send_grp_name(){
        return gname;
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* grpid = null;
        grpimg = null;
        url = null;*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* grpid = null;
        grpimg = null;
        url = null;*/
    }



    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String sender_name;
                    try {

                        other_msg = data.getString("message");
                        get_group_messages();


                    } catch (JSONException e) {
                        return;
                    }



                }
            });
        }
    };


    private void send_group_message() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "groupmessages/send";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            jsonParam.put("groupID", grpid);
            jsonParam.put("message", edittext_message.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject status = null;
                JSONArray msg;
                String message;
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

                    message = response.getString("message");

                    if (message.equals("sent Successfully!")) {
                        edittext_message.setText("");
//                        send_grp_msg_btn.setEnabled(true);
                        get_group_messages();


//                        Toast.makeText(Groupchat.this, "Message Send", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                //System.out.println("XXX Volly Error :" + error);
                //   pDialog.dismissWithAnimation();
//                showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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


    private void get_group_messages() {

//        userModelList.clear();
//        group_sendmessage_adapter.notifyDataSetChanged();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "groupMessages";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("start", "1");
            jsonParam.put("count", "20000");
            jsonParam.put("groupID", grpid);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String status = null;
                JSONArray message = null;
                String extras;
                String auth = null;
                try {
                    if (response.has("auth")) {
                        auth = response.getString("auth");
                    }

                    status = response.getString("status");

                    if (status.equals("failed")) {
//                        showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
                    } else if (auth != null && auth.equals("authFailed")) {
                        sharedpreferences.edit().remove("userid").commit();
                        sharedpreferences.edit().remove("user_token").commit();
                        showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                    }
                    else if (status.equals("success")) {
                        send_grp_msg_btn.setEnabled(true);
                        message = response.getJSONArray("message");
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<GrupMessageModel>>() {
                        }.getType();


                        userModelList = gson.fromJson(String.valueOf(message), type);

                        group_sendmessage_adapter = new Group_Sendmessage_adapter(Groupchat.this, userModelList, userid);

                        RecyclerView recyclerView = findViewById(R.id.rcyclwer1);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Groupchat.this, LinearLayoutManager.VERTICAL, true));
                        recyclerView.setAdapter(group_sendmessage_adapter);

                        group_sendmessage_adapter.notifyItemInserted((userModelList.size() - userModelList.size()) );
                        group_sendmessage_adapter.notifyItemChanged(userModelList.size() - userModelList.size());
                        group_sendmessage_adapter.notifyDataSetChanged();
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

        final SweetAlertDialog sd = new SweetAlertDialog(Groupchat.this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(Groupchat.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }





//    public void showdialog(String title, String content, int type) {
//
//        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
//                .setTitleText(title)
//                .setContentText(content);
//        sd.show();
//    }

}

