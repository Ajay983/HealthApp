package com.linkitsoft.dategoal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class ReportUser extends AppCompatActivity {
    ImageButton back;
    EditText other_reason;
    RadioButton r0, r1, r2, r3, r4, r5, r6, other_radio;
    String reporting_userId;
    String userid, token;
    String report_reason;
    String ProofImage = "abc.jpg";
    RadioGroup radioGroup;
    Button ignore, submit;
    PortnVariable portnVariable;
    Boolean b;
    SharedPreferences sharedpreferences;
    ImageButton img_gallery;
    EasyImage easyImage;
    String url1 = "no Image";
    String path;
    SweetAlertDialog pDialog;
    String myTime, myDay;
    FloatingActionButton noti;
    ImageView badge;
    Boolean img1, img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);
        portnVariable = new PortnVariable();
        back = findViewById(R.id.imageButton20);
        other_reason = findViewById(R.id.textReason);
        r0 = findViewById(R.id.radio0);
        r1 = findViewById(R.id.radio1);
        r2 = findViewById(R.id.radio2);
        r3 = findViewById(R.id.radio3);
        r4 = findViewById(R.id.radio4);
        r5 = findViewById(R.id.radio5);
        r6 = findViewById(R.id.radio6);
        badge = findViewById(R.id.imageView4);
        noti = findViewById(R.id.floatingActionButton3);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getSupportFragmentManager(), "Notifications");
            }
        });
        ignore = findViewById(R.id.ignore_report);
        submit = findViewById(R.id.submit_report);
        other_radio = findViewById(R.id.radio7);
        radioGroup = findViewById(R.id.radioGroup1);
        img_gallery = findViewById(R.id.imageGallery);
        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        reporting_userId = getIntent().getStringExtra("reportingUserId");
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        easyImage = new EasyImage.Builder(ReportUser.this).setCopyImagesToPublicGalleryFolder(false).build();
        b = false;
        myTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        myDay = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        r0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report_reason = r0.getText().toString();
                b = false;
            }
        });
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report_reason = r1.getText().toString();
                b = false;
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report_reason = r2.getText().toString();
                b = false;
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report_reason = r3.getText().toString();
                b = false;
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report_reason = r4.getText().toString();
                b = false;
            }
        });
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report_reason = r5.getText().toString();
                b = false;
            }
        });
        r6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                report_reason = r6.getText().toString();
                b = false;
            }
        });
        other_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_reason.setVisibility(View.VISIBLE);
                b = true;

            }
        });
        img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyImage.openGallery(ReportUser.this);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    showdialog("Error", "Kindly Provide Reason For Report", 1);

                } else {
                    pDialog = new SweetAlertDialog(ReportUser.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitle("Processing");
                    pDialog.show();
                    uploadBolob(userid);
                }

            }
        });
        getNotificationsStatus();


    }

    private void getNotificationsStatus() {
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
               // System.out.println("XXX Volly Error :" + error);
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

    public void showdialogAuth(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(ReportUser.this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(ReportUser.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.get().load(resultUri).into(img_gallery);
                path = resultUri.getPath();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        easyImage.handleActivityResult(requestCode, resultCode, data, ReportUser.this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                CropImage.activity(Uri.fromFile(imageFiles[0].getFile()))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .setAspectRatio(4, 3)
                        .start(ReportUser.this);
//                Picasso.get().load(imageFiles[0].getFile()).fit().into(img_gallery);
//                path = imageFiles[0].getFile().getPath();
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }

    void uploadBolob(String userid) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work here
                try {
                    CloudStorageAccount storageAccount = CloudStorageAccount.parse(portnVariable.storageConnectionString);

                    // Create the blob client.
                    CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

                    // Retrieve reference to a previously created container.
                    String storageContainer = "userprofileimg";
                    CloudBlobContainer container = blobClient.getContainerReference(storageContainer);

                    // Create or overwrite the blob (with the name "example.jpeg") with contents from a local file.
                    CloudBlockBlob blob = container.getBlockBlobReference(userid + "Day: " + myDay + " Time:" + myTime + "_" + ".jpg");
                    File source = new File(path);
                    blob.upload(new FileInputStream(source), source.length());
                    url1 = blob.getUri().toURL().toString();


                } catch (Exception ex) {

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        if (!url1.equals("")) {
                            reportUser();
                        } else {

                            showdialog("Error", "Reselect Image", 1);
                        }
                    }
                });
            }
        });

    }

    private void reportUser() {
        if (b) {
            report_reason = other_reason.getText().toString();
        }

        if (report_reason.equals("")) {
            pDialog.dismissWithAnimation();
            showdialog("Error", "Kindly Provide Reason For Report", 1);

        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonParam = null;
            String url = portnVariable.com + "report";
            try {
                jsonParam = new JSONObject();
                jsonParam.put("ReportedBy", userid);
                jsonParam.put("userID", reporting_userId);
                jsonParam.put("ProofImage", url1);
                jsonParam.put("Title", "Report User");
                jsonParam.put("Reason", report_reason);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            final JSONObject requestBody = jsonParam;
            JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog.dismissWithAnimation();
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
                            message = response.getString("message");

                            if (status.equals("success") && message.equals("User Repoted Sucessfully")) {
                                showdialog("Success", "User Reported ", 2);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                  //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);

                }
            }) {
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

    }

    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(ReportUser.this, type)
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
}