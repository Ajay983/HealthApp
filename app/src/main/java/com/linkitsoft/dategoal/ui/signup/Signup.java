package com.linkitsoft.dategoal.ui.signup;

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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import com.linkitsoft.dategoal.Agreement.CookiePolicy;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Signup extends AppCompatActivity {
    Button signup;
    TextInputEditText email;
    TextView login;
    TextInputEditText password;
    String pass;
    String emailadd;
    SweetAlertDialog pDialog;
    SharedPreferences sharedpreferences;
    PortnVariable portnVariable;
    RadioButton agreed;
    GoogleSignInClient mGoogleSignInClient;
    Button googleSignUp;
    Button facebookSignUp;
    LoginButton fb;
    private static int RC_SIGN_IN = 100;
    private static int FB_SIGN_IN = 64206;
    CallbackManager callbackManager;
    String loginType;
    Boolean for_agree = false;
    String googleId, facebookId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        signup = findViewById(R.id.button);
        login = findViewById(R.id.textView5);
        email = findViewById(R.id.editTextTextEmailA2ddress);
        password = findViewById(R.id.editTextTextPassword);
        googleSignUp = findViewById(R.id.button2);
        facebookSignUp = findViewById(R.id.fbBtn);
        fb = findViewById(R.id.button3);
        agreed = findViewById(R.id.radioButton);
        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        portnVariable = new PortnVariable();

        String check_agree = getIntent().getStringExtra("done");

        if (check_agree != null && "done".equalsIgnoreCase(check_agree)) {
            for_agree = true;
            agreed.setChecked(true);
            agreed.setClickable(false);
        }


        agreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agreed.isChecked() && for_agree.equals(false)) {
                    Intent intent = new Intent(Signup.this, CookiePolicy.class);
                    startActivity(intent);
                }


            }
        });

        facebookSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agreed.isChecked() && for_agree) {
                    callbackManager = CallbackManager.Factory.create();
                    fb.performClick();
                    fb.setReadPermissions("email", "public_profile", "user_friends");
                    fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            pDialog = new SweetAlertDialog(Signup.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.setTitle("Processing");
                            pDialog.show();
                            loginType = "facebook";
                            facebookId = loginResult.getAccessToken().getUserId();
                            checkFacebookAccount(facebookId, loginType);
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(FacebookException error) {

                        }
                    });


                } else {
                    showdialog("Terms and conditions", "You must agree to our terms and conditions to signup with DateGoal.", 3);
                }


            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (agreed.isChecked() && for_agree) {
                    signIn();
                } else {
                    showdialog("Terms and conditions", "You must agree to our terms and conditions to signup with DateGoal.", 3);
                }

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_check = email.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (agreed.isChecked() && for_agree) {

                    if (password.length() >= 8 && email_check.matches(emailPattern)) {

                        signup.setEnabled(false);
                        pDialog = new SweetAlertDialog(Signup.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.setTitle("Processing");
                        pDialog.show();

                        emailadd = email.getText().toString();
                        pass = password.getText().toString();

                        checkemailapi(emailadd);

                    } else if (password.length() < 8 && !email_check.matches(emailPattern)) {
                        showdialog("Error", "Email Should be Valid & Password Should Be Atleast 8 Characters", 1);

                    } else if (password.length() < 8) {
                        showdialog("Error", "Password Should be of atleast 8 characters", 1);

                    } else if (!email_check.matches(emailPattern)) {
                        showdialog("Error", "Email Should be Valid", 1);

                    } else if (!email.getText().toString().isEmpty() && !Objects.requireNonNull(password.getText()).toString().isEmpty()) {
                        showdialog("Error", "Must fill all required fields", 1);


                    } else {

                        showdialog("Error", "Provide Verified Email & Password", 1);
                        //asdasd
                    }
                } else {
                    showdialog("Terms and conditions", "You must agree to our terms and conditions to signup with DateGoal.", 3);
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Signup.this, Login.class);
                startActivity(main);
                finish();
            }
        });


        signOutGoogle();
        signOutFacebook();


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                pDialog = new SweetAlertDialog(Signup.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitle("Processing");
                pDialog.show();

                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                googleId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                loginType = "google";
                checkGoogleAccount(googleId, loginType, personEmail);
            }

            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Message", e.toString());

        }
    }

//    private void google_signup(String gid){
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        String uri = portnVariable.com + "user/checkEmail";
//
//        JSONObject jsonParam = new JSONObject();
//        try {
//            jsonParam.put("googleId", gid);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        final JSONObject requestBody = jsonParam;
//        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                pDialog.dismissWithAnimation();
//                signup.setEnabled(true);
//                String status = null;
//                try {
//                    status = response.getString("status");
//
////                    if (status == 0) {
////                        showdialog("Warning", "Email address already used", 3);
////                    } else if (status == 1) {
////
////                        SharedPreferences.Editor editor = sharedpreferences.edit();
////                        editor.putString("email", emailadd);
////                        editor.putString("password", pass);
////                        editor.apply();
////
////                        if (sharedpreferences.getString("email", null) != null) {
////                            Intent main = new Intent(Signup.this, SignupPager.class);
////                            startActivity(main);
////                        } else {
////                            showdialog("Error", "Please check if the storage permission is allowed", 1);
////                        }
////
////                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error report
//                System.out.println("XXX Volly Error :" + error);
//
//                pDialog.dismissWithAnimation();
//                signup.setEnabled(true);
//
//                showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
//            }
//        }
//        );
//
//        requestQueue.add(myReq);
//
//    }

    private void checkFacebookAccount(String FBId, String type) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "user/checkSocailAccounts";
        JSONObject jsonparam = new JSONObject();
        try {
            jsonparam.put("id", FBId);
            jsonparam.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonparam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismissWithAnimation();

                int status;
                String message = null;
                try {
                    status = response.getInt("status");
                    message = response.getString("message");

                    if (status == 0 && message.equals("account already exists")) {
                        signOutFacebook();
                        showdialog("Error", "Account Already Exist", 1);

                    } else if (status == 1 && message.equals("account does not exists")) {
                        Toast.makeText(Signup.this, "Done", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("facebookId", FBId);
                        editor.putString("loginType", type);
                        editor.apply();
                        if (sharedpreferences.getString("facebookId", null) != null) {
                            pDialog.dismissWithAnimation();
                            Intent main = new Intent(Signup.this, SignupPager.class);
                            startActivity(main);
                        } else {
                            signOutFacebook();
                            showdialog("Error", "Please check if the storage permission is allowed", 1);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();

               // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);

            }
        });
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);
    }

    private void checkGoogleAccount(String mediaID, String type, String googleEmail) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "user/checkSocailAccounts";
        JSONObject jsonparam = new JSONObject();
        try {
            jsonparam.put("id", mediaID);
            jsonparam.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonparam;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismissWithAnimation();

                int status;
                String message = null;
                try {
                    status = response.getInt("status");
                    message = response.getString("message");
                    if (status == 0 && message.equals("account already exists")) {
                        signOutGoogle();
                        showdialog("Error", "Account Already Exist", 1);
                    } else if (status == 1 && message.equals("account does not exists")) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("googleId", mediaID);
                        editor.putString("loginType", type);
                        editor.putString("googleEmail", googleEmail);
                        editor.apply();
                        if (sharedpreferences.getString("googleId", null) != null) {
                            pDialog.dismissWithAnimation();
                            Intent main = new Intent(Signup.this, SignupPager.class);
                            startActivity(main);
                        } else {
                            signOutGoogle();
                            showdialog("Error", "Please check if the storage permission is allowed", 1);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
                pDialog.dismissWithAnimation();

            }
        });
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);
    }

    private void checkemailapi(String em) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String uri = portnVariable.com + "user/checkEmail";

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("email", em);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();
                signup.setEnabled(true);


                int status = 0;

                try {
                    status = response.getInt("status");

                    if (status == 0) {
                        showdialog("Warning", "Email address already used", 3);
                    } else if (status == 1) {


                        loginType = "email";

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("email", emailadd);
                        editor.putString("password", pass);
                        editor.putString("loginType", loginType);
                        editor.apply();

                        if (sharedpreferences.getString("email", null) != null) {
                            Intent main = new Intent(Signup.this, SignupPager.class);
                            startActivity(main);
                        } else {
                            showdialog("Error", "Please check if the storage permission is allowed", 1);
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
                System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();
                signup.setEnabled(true);

               // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
            }
        }
        );
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);

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


    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(Signup.this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }
}