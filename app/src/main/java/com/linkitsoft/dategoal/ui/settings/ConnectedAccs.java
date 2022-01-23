package com.linkitsoft.dategoal.ui.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

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
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ConnectedAccs extends AppCompatActivity {


    ImageButton imagebtnback;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch fb, google, apple;
    SharedPreferences sharedpreferences;
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
    private static int FB_SIGN_IN = 64206;
    String userid, token;
    PortnVariable portnVariable;
    String account_type;
    SweetAlertDialog pDialog;
    LoginButton fbLogin;
    CallbackManager callbackManager;
    String check_google, check_facebook;
    FloatingActionButton notification;
    ImageView badge;
    String fb_id, googleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_accs);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        check_facebook = getIntent().getStringExtra("facebookId");
        check_google = getIntent().getStringExtra("googleId");


        imagebtnback = findViewById(R.id.imageButton20);
        badge = findViewById(R.id.imageView4);
        notification = findViewById(R.id.floatingActionButton3);
        fb = findViewById(R.id.switch3);
        google = findViewById(R.id.switch5);
        fbLogin = findViewById(R.id.facebookLogin);
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        portnVariable = new PortnVariable();

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getSupportFragmentManager(), "Notifications");
            }
        });

        if (!"null".equalsIgnoreCase(check_google)) {
            google.setChecked(true);
            google.setClickable(false);
        }
        if (!"null".equalsIgnoreCase(check_facebook)) {
            fb.setChecked(true);
            fb.setClickable(false);
        }

        //for badge
        getNotificationsStatus();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!google.isChecked()) {
                    pDialog = new SweetAlertDialog(ConnectedAccs.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitle("Processing");
                    pDialog.show();
                    googleId = "null";
                    account_type = "googleId";
                    deleteGoogleAcc(googleId, account_type);

                } else {
                    google.setChecked(false);
                    signIn();
                }

            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fb.isChecked()) {
                    pDialog = new SweetAlertDialog(ConnectedAccs.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitle("Processing");
                    pDialog.show();
                    fb_id = "null";
                    account_type = "facebookId";
                    deleteFacebookAcc(fb_id, account_type);

                } else {
                    fb.setChecked(false);
                    callbackManager = CallbackManager.Factory.create();
                    fbLogin.performClick();
                    fbLogin.setReadPermissions("email", "public_profile", "user_friends");
                    fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            pDialog = new SweetAlertDialog(ConnectedAccs.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.setTitle("Processing");
                            pDialog.show();
                            fb.setChecked(false);
                            fb_id = loginResult.getAccessToken().getUserId();
                            account_type = "facebookId";
                            updateFacebookField(fb_id, account_type);

                        }

                        @Override
                        public void onCancel() {


                        }

                        @Override
                        public void onError(FacebookException error) {


                        }
                    });
                }

            }
        });

        signOutGoogle();
        signOutFacebook();


        imagebtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //for facebook login
        //  callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else if (requestCode == FB_SIGN_IN) {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                pDialog = new SweetAlertDialog(ConnectedAccs.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitle("Processing");
                pDialog.show();
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                googleId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                account_type = "googleId";
                if (!googleId.equals(null)) {
                    updateGoogleField(googleId, account_type);

                }


            }

            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Message", e.toString());

        }

    }

    private void signOutGoogle() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void signOutFacebook() {
        LoginManager.getInstance().logOut();
    }

    private void updateGoogleField(String id, String Acctype) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = portnVariable.com + "user/field/Update";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            jsonParam.put("fieldName", Acctype);
            jsonParam.put("fieldValue", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int status;
                String message;
                pDialog.dismissWithAnimation();
                String auth = null;

                try {
                    if (response.has("auth")) {
                        auth = response.getString("auth");
                    }
                    status = response.getInt("status");
                    if (status == 1) {
                        message = response.getString("message");
                        if (message.equals("Field Updated Sucessfully")) ;
                        showdialog("Success", "Google Account Connected", 2);
                        google.setChecked(true);
                        signOutGoogle();

                    } else if (auth != null && auth.equals("authFailed")) {
                        sharedpreferences.edit().remove("userid").commit();
                        sharedpreferences.edit().remove("user_token").commit();
                        showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                     //   showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                    } else {
                        google.setChecked(false);
                        signOutGoogle();
                       // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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
               /// showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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
    private void deleteGoogleAcc(String id, String Acctype) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = portnVariable.com + "user/field/Update";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            jsonParam.put("fieldName", Acctype);
            jsonParam.put("fieldValue", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int status;
                String message;
                pDialog.dismissWithAnimation();
                String auth = null;

                try {
                    if (response.has("auth")) {
                        auth = response.getString("auth");
                    }
                    status = response.getInt("status");

                    if (status == 1) {
                        message = response.getString("message");
                        if (message.equals("Field Updated Sucessfully")) ;
                        showdialog("Success", "Google Account Removed", 2);
                        google.setChecked(false);

                    } else if (auth != null && auth.equals("authFailed")) {
                        sharedpreferences.edit().remove("userid").commit();
                        sharedpreferences.edit().remove("user_token").commit();
                        showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                     //   showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                    }else {
                        signOutGoogle();
                        signOutFacebook();
                     //   showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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
    private void updateFacebookField(String id, String Acctype){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = portnVariable.com + "user/field/Update";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            jsonParam.put("fieldName", Acctype);
            jsonParam.put("fieldValue", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int status;
                String message;
                pDialog.dismissWithAnimation();
                String auth = null;

                try {
                    if (response.has("auth")) {
                        auth = response.getString("auth");
                    }
                    status = response.getInt("status");

                    if (status == 1) {
                        message = response.getString("message");
                        if (message.equals("Field Updated Sucessfully"));
                         showdialog("Success", "Facebook Account Connected", 2);
                        fb.setChecked(true);
                        signOutFacebook();



                    } else if (auth != null && auth.equals("authFailed")) {
                        sharedpreferences.edit().remove("userid").commit();
                        sharedpreferences.edit().remove("user_token").commit();
                        showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                        showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                    }else {
                        fb.setChecked(false);
                        signOutFacebook();
                      //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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
             //   showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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
    private void deleteFacebookAcc(String id, String Acctype){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = portnVariable.com + "user/field/Update";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            jsonParam.put("fieldName", Acctype);
            jsonParam.put("fieldValue", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int status;
                String message;
                pDialog.dismissWithAnimation();
                String auth = null;

                try {
                    status = response.getInt("status");
                    if (status == 1) {
                        message = response.getString("message");
                        if (message.equals("Field Updated Sucessfully")) ;
                        showdialog("Success", "Facebook Account Removed", 2);
                        fb.setChecked(false);

                    } else if (auth != null && auth.equals("authFailed")) {
                        sharedpreferences.edit().remove("userid").commit();
                        sharedpreferences.edit().remove("user_token").commit();
                        showdialogAuth("Error", "Token Expired Kindly Login Again", 1);

                    }else {
                        signOutGoogle();
                        signOutFacebook();
                       // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
           ///     System.out.println("XXX Volly Error :" + error);
           //     showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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

    private void getNotificationsStatus() {
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                            message = response.getString("message");
                            if (message.equals("No new notifications")) {
                                badge.setVisibility(View.GONE);

                            } else if (message.equals("new notifications")) {
                                badge.setVisibility(View.VISIBLE);

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
                //System.out.println("XXX Volly Error :" + error);
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

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }

    public void showdialogAuth(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(ConnectedAccs.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }



}