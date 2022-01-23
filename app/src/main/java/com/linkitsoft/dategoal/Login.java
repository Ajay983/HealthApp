package com.linkitsoft.dategoal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.ui.signup.Forgotpass_1;
import com.linkitsoft.dategoal.ui.signup.Signup;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    TextView signup;
    Button login;
    Button fgpass;
    TextInputEditText email;
    TextInputEditText password;
    String pass;
    String emailadd;
    String type;
    SweetAlertDialog pDialog;
    SharedPreferences sharedpreferences;
    PortnVariable portnVariable;
    GoogleSignInClient mGoogleSignInClient;
    Button googleSignIn;

    Button facebook_btn;
    private static int RC_SIGN_IN = 100;
    private static int FB_SIGN_IN = 64206;

    LoginButton fb;
    CallbackManager callbackManager;
    Intent intent;
    String firebase_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
//        intent = new Intent(this, MyService.class);
//        stopService(intent);

        signup = findViewById(R.id.textView5);
        login = findViewById(R.id.button);
        fgpass = findViewById(R.id.radioButton);
        facebook_btn = findViewById(R.id.btn_fb);
        googleSignIn = findViewById(R.id.button2);

        email = findViewById(R.id.editTextTextEmailA2ddress);
        password = findViewById(R.id.editTextTextPassword);


        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        portnVariable = new PortnVariable();


        //just Check
        String a = sharedpreferences.getString("userid", "0");
        String b = sharedpreferences.getString("user_token", "0");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //google login
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        App_Socket app_socket = new App_Socket();
        app_socket.socket.disconnect();
        app_socket.socket.close();
        app_socket.socket.off();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!email.getText().toString().isEmpty() && !Objects.requireNonNull(password.getText()).toString().isEmpty()) {
                    emailadd = email.getText().toString();
                    pass = password.getText().toString();

                    login.setEnabled(false);

                    pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitle("Processing");
                    pDialog.show();
                    postuser();

                } else {
                    showdialog("Error", "Must fill all required fields", 1);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Login.this, Signup.class);
                startActivity(main);
                finish();
            }
        });

        fgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Login.this, Forgotpass_1.class);
                startActivity(main);
            }
        });

        fb = findViewById(R.id.facebookLogin);

        facebook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackManager = CallbackManager.Factory.create();
                fb.performClick();
                fb.setReadPermissions("email", "public_profile", "user_friends");
                fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.setTitle("Processing");
                        pDialog.show();

                        String fb_id = loginResult.getAccessToken().getUserId();
                        type = "facebook";
                        postFacebook(fb_id, type);
                    }

                    @Override
                    public void onCancel() {


                    }

                    @Override
                    public void onError(FacebookException error) {


                    }
                });


            }
        });


        signOutGoogle();
        signOutFacebook();


      /*  LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.setTitle("Processing");
                        pDialog.show();

                        String fb_id = loginResult.getAccessToken().getUserId();
                        type = "facebook";
                        postFacebook(fb_id, type);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

*/

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new FCM registration token
                        firebase_token = task.getResult();

                        // Log and toast


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
                pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitle("Processing");
                pDialog.show();
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                type = "google";
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("googleId", personId);
                editor.putString("loginType", type);
                editor.apply();

                if (sharedpreferences.getString("googleId", null) != null) {
                    type = "google";
                    postGoogle(personId, type);
                } else {
                    showdialog("Error", "Please check if the storage permission is allowed", 1);
                }
            }

            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Message", e.toString());

        }
    }

    private void postFacebook(String userFbId, String loginType) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonParam = null;
        String url = portnVariable.com + "user/loginuser";
        try {
            jsonParam = new JSONObject();
            jsonParam.put("facebookId", userFbId);
            jsonParam.put("loginType", loginType);
            jsonParam.put("fcmToken", firebase_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismissWithAnimation();
//                login.setEnabled(true);

                try {
                    String auth = null;
                    String status = response.getString("message");
                    // System.out.println("Check Return value from api "+status);
                    switch (status) {

                        case "the user has been found":
                            String userid = response.getString("userID");
                            String Photo1 = response.getJSONObject("Data").getString("Photo1");
                            String auth_token = response.getString("token");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("userid", userid);
                            editor.putString("user_token", auth_token);
                            editor.putString("loginType", loginType);
                            editor.putString("photo1", Photo1);
                            editor.apply();

                            String data = response.getString("Data");

                           /* Gson gson = new Gson();
                            Type type = new TypeToken<UserModel>() {}.getType();
                            UserModel userModel1 = gson.fromJson(data, type);*/

                            Gson gson = new Gson();
                            String myJson = gson.toJson(data);
                            Intent main = new Intent(Login.this, Homescreen.class);
                            main.putExtra("usermodel", myJson);
                            startActivity(main);
                            finish();
                            break;

                        case "incorrect password":
                            signOutFacebook();

                            showdialog("Unsuccessful", "Invalid password", 1);
                            break;

                        case "the user has not been found":
                            signOutFacebook();

                            showdialog("Unsuccessful", "User does not exist, Please sign up", 1);
                            break;
                        default:
                            signOutFacebook();
                            showdialog("Unsuccessful", "One or more information is not correct", 1);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // error report

                pDialog.dismissWithAnimation();
              //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
                login.setEnabled(true);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(jsonObjectRequest);


    }


    private void postGoogle(String userGFId, String loginType) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonParam = null;
        String url = portnVariable.com + "user/loginuser";

        try {
            jsonParam = new JSONObject();
            jsonParam.put("googleId", userGFId);
            jsonParam.put("loginType", loginType);
            jsonParam.put("fcmToken", firebase_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismissWithAnimation();
                login.setEnabled(true);

                try {
                    String status = response.getString("message");
                    // System.out.println("Check Return value from api "+status);
                    switch (status) {

                        case "the user has been found":
                            String userid = response.getString("userID");
                            String Photo1 = response.getJSONObject("Data").getString("Photo1");
                            String auth_token = response.getString("token");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("userid", userid);
                            editor.putString("user_token", auth_token);
                            editor.putString("photo1", Photo1);
                            editor.putString("loginType", loginType);
                            editor.apply();

                            String data = response.getString("Data");

                           /* Gson gson = new Gson();
                            Type type = new TypeToken<UserModel>() {}.getType();
                            UserModel userModel1 = gson.fromJson(data, type);*/

                            Gson gson = new Gson();
                            String myJson = gson.toJson(data);
                            Intent main = new Intent(Login.this, Homescreen.class);
                            main.putExtra("usermodel", myJson);
                            startActivity(main);
                            finish();
                            break;

                        case "the user has not been found":
                            signOutGoogle();
                            showdialog("Unsuccessful", "Account does not exist", 1);

                            break;

                        case "incorrect password":
                            signOutGoogle();

                            showdialog("Unsuccessful", "Invalid password", 1);
                            break;


                        case "the user has not been found with this email/name":
                            signOutGoogle();

                            showdialog("Unsuccessful", "Invalid email", 1);
                            break;
                        default:
                            signOutGoogle();
                            showdialog("Unsuccessful", "One or more information is not correct", 1);
                            break;
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
                login.setEnabled(true);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(jsonObjectRequest);
    }


    //logout from google

    private void signOutGoogle() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }


    //logout from facebook


    private void signOutFacebook() {
        LoginManager.getInstance().logOut();
    }


    private void postuser() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonParam = null;
        String url = portnVariable.com + "user/loginuser";
        try {
            jsonParam = new JSONObject();
            jsonParam.put("email", emailadd);
            jsonParam.put("password", pass);
            jsonParam.put("loginType", "email");
            jsonParam.put("fcmToken", firebase_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismissWithAnimation();
                login.setEnabled(true);


                try {
                    String status = response.getString("message");
                    // System.out.println("Check Return value from api "+status);
                    switch (status) {

                        case "the user has been found":


                            String userid = response.getString("userID");
                            String Photo1 = response.getJSONObject("Data").getString("Photo1");
                            String auth_token = response.getString("token");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("userid", userid);
                            editor.putString("user_token", auth_token);
                            editor.putString("photo1", Photo1);
                            editor.apply();

                            String data = response.getString("Data");

                           /* Gson gson = new Gson();
                            Type type = new TypeToken<UserModel>() {}.getType();
                            UserModel userModel1 = gson.fromJson(data, type);*/

                            Gson gson = new Gson();
                            String myJson = gson.toJson(data);
                            Intent main = new Intent(Login.this, Homescreen.class);
                            main.putExtra("usermodel", myJson);
                            startActivity(main);
                            finish();
                            break;

                        case "User already logged in!":
                            showdialog("Error", "User Already Logged In", 1);
                            break;

                        case "incorrect password":

                            showdialog("Unsuccessful", "Invalid password", 1);
                            break;

                        case "the user has not been found with this email/name":

                            showdialog("Unsuccessful", "Invalid email", 1);
                            break;
                        default:
                            showdialog("Unsuccessful", "One or more information is not correct", 1);
                            break;
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
                login.setEnabled(true);
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(jsonObjectRequest);
    }


    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(Login.this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }
}