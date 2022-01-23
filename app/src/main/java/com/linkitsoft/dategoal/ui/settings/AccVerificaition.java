package com.linkitsoft.dategoal.ui.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Homescreen;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.VerificationModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class AccVerificaition extends AppCompatActivity {

    ImageButton imagebtnback;

    ImageButton uploadimage1;
    ImageButton uploadimage2;
    ImageView img_uploaded;
    ConstraintLayout first;
    ConstraintLayout second;
    static Boolean a;
    static Boolean b;
    int temp = 0;
    Button upload_data;


//    String check;
//
//    public final int GET_FROM_GALLERY = 1;
//
//    private final int CODE_IMG_GALLERY = 1;
//    private final int CODE_MULTIPLE_IMG_GALLERY = 2;

    EasyImage easyImage;
    int tasktodo = 0;
    int tasktodone = 0;
    String path;
    PortnVariable portnVariable;
    String userid, token;
    SharedPreferences sharedpreferences;
    String url;
    SweetAlertDialog pDialog;
    Boolean img1, img2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_verificaition);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        img2 = false;
        img1 = false;

        portnVariable = new PortnVariable();
        sharedpreferences = getSharedPreferences("MyPrefs", 0);


        easyImage = new EasyImage.Builder(AccVerificaition.this).setCopyImagesToPublicGalleryFolder(false).build();

        imagebtnback = findViewById(R.id.imageButton20);

        first = findViewById(R.id.takeimglayout);
        second = findViewById(R.id.constraintLayout8);

        first.setVisibility(View.VISIBLE);
        second.setVisibility(View.GONE);

        uploadimage1 = findViewById(R.id.imageButton24);
        uploadimage2 = findViewById(R.id.imageButton23);

        Button first_upload = findViewById(R.id.button17);
        first_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog("Error", "Image Is Required", 1);
            }
        });


        upload_data = findViewById(R.id.button18);
        img_uploaded = findViewById(R.id.imageView3);


        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");


        uploadimage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1 = true;
                img2 = false;
                easyImage.openGallery(AccVerificaition.this);


            }
        });

        uploadimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1 = false;
                img2 = true;
                easyImage.openCameraForImage(AccVerificaition.this);

            }
        });


        imagebtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        upload_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog = new SweetAlertDialog(AccVerificaition.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitle("Sending Request. Please Wait");
                pDialog.show();
                uploadBolob(userid);
            }
        });


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (img1) {
                    Picasso.get().load(resultUri).into(img_uploaded);
                    path = resultUri.getPath();
                    second.setVisibility(View.VISIBLE);
                    first.setVisibility(View.GONE);
                }
                if (img2) {
                    Picasso.get().load(resultUri).into(img_uploaded);
                    path = resultUri.getPath();
                    second.setVisibility(View.VISIBLE);
                    first.setVisibility(View.GONE);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        easyImage.handleActivityResult(requestCode, resultCode, data, AccVerificaition.this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                CropImage.activity(Uri.fromFile(imageFiles[0].getFile()))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .setAspectRatio(4, 3)
                        .start(AccVerificaition.this);
//                    System.out.println("hellhellohellohellohellohellohellohellohelloo" + a);
//                    Picasso.get().load(imageFiles[0].getFile()).resize(600, 200).centerInside().into(img_uploaded);
//                    path = imageFiles[0].getFile().getPath();
//                    second.setVisibility(View.VISIBLE);
//                    first.setVisibility(View.GONE);

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
                    CloudBlockBlob blob = container.getBlockBlobReference(userid + "_" + ".jpg");
                    File source = new File(path);
                    blob.upload(new FileInputStream(source), source.length());
                    url = blob.getUri().toURL().toString();


                } catch (Exception ex) {

                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        if (!url.equals("")) {
                            postVerificationReq();
                        } else {
                            showdialog("Error", "Reselect Image", 1);
                        }
                    }
                });
            }
        });

    }

    private void postVerificationReq() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonParam = null;
        String urll = portnVariable.com + "verified";
        VerificationModel verificationModel = new VerificationModel();
        verificationModel.setUserID(userid);
        verificationModel.setImage1(url);
        verificationModel.setImage2(url);
//        verificationModel.setStatus(4);
        try {
            jsonParam = new JSONObject(new Gson().toJson(verificationModel));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urll, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                String message = null;
                pDialog.dismiss();
                String auth = null;

                try {


                    if (response.has("auth")) {
                        auth = response.getString("auth");
                    }

                    status = response.getString("status");
                    message = response.getString("message");
                    if (status.equals("failed")) {
                        if (message.equals("Request already submitted!")) {
                            showdialog("Oops", "Your Request Is Already Submitted", 3);

                        }
                    }else if (auth != null && auth.equals("authFailed")) {
                        sharedpreferences.edit().remove("userid").commit();
                        sharedpreferences.edit().remove("user_token").commit();
                        showdialogAuth("Error", "Token Expired Kindly Login Again", 1);

                    } else if (status.equals("success")) {
                        if (message.equals("Request Submitted Successfully!")) {
                            showdialog1("Done", "Request Submitted", 2);

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
              //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
//                save.setEnabled(true);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("authorization", "bearer " + token);
                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(jsonObjectRequest);


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

    public void showdialogAuth(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(AccVerificaition.this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(AccVerificaition.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }



    public void showdialog1(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent main = new Intent(AccVerificaition.this, Homescreen.class);
                        startActivity(main);
                    }
                })
                .setContentText(content);
        sd.show();
    }


}