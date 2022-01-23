package com.linkitsoft.dategoal.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.TextView;

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
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.settings.ConnectedAccs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
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

public class EditMyProfile extends AppCompatActivity {

    Boolean img1;
    Boolean img2;
    Boolean img3;
    Boolean img4;
    Boolean img5;
    Boolean img6;
    String phoneNo;

    static int check = 0;

    String path1 = null, path2 = null, path3 = null, path4 = null, path5 = null, path6 = null;

    String url1 = null;
    String url2 = null;
    String url3 = null;
    String url4 = null;
    String url5 = null;
    String url6 = null;
    int tasktodone = 0;
    int tasktodo = 0;
    Button save;
    String userid, token;
    SharedPreferences sharedpreferences;
    Context context;
    PortnVariable portnVariable;
    ImageButton p1, p2, p3, p4, p5, p6;
    ImageButton c1, c2, c3, c4, c5, c6;
    TextView myName, myEmail, myDOB, myPhone;
    EasyImage easyImage;
    Button next;
    SweetAlertDialog pDialog;
    ImageView badge;
    FloatingActionButton notification;
    String ar[] = new String[6];
    Button connect;
    String googleId = null;
    String facebookId = null;
    ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        portnVariable = new PortnVariable();
        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        easyImage = new EasyImage.Builder(EditMyProfile.this).setCopyImagesToPublicGalleryFolder(false).build();

        img1 = false;
        img2 = false;
        img3 = false;
        img4 = false;
        img5 = false;
        img6 = false;

        path1 = null;
        path2 = null;
        path3 = null;
        path4 = null;
        path5 = null;
        path6 = null;

        p1 = findViewById(R.id.imageButton4);
        p2 = findViewById(R.id.imageButton5);
        p3 = findViewById(R.id.imageButton6);
        p4 =findViewById(R.id.imageButton7);
        p5 = findViewById(R.id.imageButton8);
        p6 = findViewById(R.id.imageButton9);
        c1 = findViewById(R.id.image_cancel1);
        c2 = findViewById(R.id.image_cancel2);
        c3 = findViewById(R.id.image_cancel3);
        c4 = findViewById(R.id.image_cancel4);
        c5 = findViewById(R.id.image_cancel5);
        c6 = findViewById(R.id.image_cancel6);
        badge = findViewById(R.id.imageView4);
        back = findViewById(R.id.imageButton20);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        notification = findViewById(R.id.floatingActionButton3);
        next = findViewById(R.id.save);
        myName = findViewById(R.id.myName);
        myEmail = findViewById(R.id.myEmail);
        myDOB = findViewById(R.id.myDOB);
        myPhone = findViewById(R.id.myPhone);
        save = findViewById(R.id.save);

        connect = findViewById(R.id.connectAcc);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(EditMyProfile.this, ConnectedAccs.class);
                main.putExtra("facebookId",facebookId);
                main.putExtra("googleId",googleId);
                startActivity(main);
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getSupportFragmentManager(), "Notifications");
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p1);
                c1.setVisibility(View.GONE);
                removeImages("Photo1");
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p2);
                c2.setVisibility(View.GONE);
                removeImages("Photo2");


            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p3);
                c3.setVisibility(View.GONE);
                removeImages("Photo3");

            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p4);
                c4.setVisibility(View.GONE);
                removeImages("Photo4");

            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p5);
                c5.setVisibility(View.GONE);
                removeImages("Photo5");

            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p6);
                c6.setVisibility(View.GONE);
                removeImages("Photo6");
            }
        });

        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img1 = true;
                img2 = false;
                img3 = false;
                img4 = false;
                img5 = false;
                img6 = false;
                easyImage.openGallery(EditMyProfile.this);
            }
        });
        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img2 = true;
                img1 = false;
                img3 = false;
                img4 = false;
                img5 = false;
                img6 = false;
                easyImage.openGallery(EditMyProfile.this);
            }
        });
        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img3 = true;
                img1 = false;
                img2 = false;
                img4 = false;
                img5 = false;
                img6 = false;
                easyImage.openGallery(EditMyProfile.this);
            }
        });
        p4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img4 = true;
                img1 = false;
                img2 = false;
                img3 = false;
                img5 = false;
                img6 = false;
                easyImage.openGallery(EditMyProfile.this);
            }
        });
        p5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img5 = true;
                img1 = false;
                img2 = false;
                img3 = false;
                img4 = false;
                img6 = false;
                easyImage.openGallery(EditMyProfile.this);
            }
        });
        p6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img6 = true;
                img1 = false;
                img2 = false;
                img3 = false;
                img4 = false;
                img5 = false;
                easyImage.openGallery(EditMyProfile.this);
            }
        });
        getMyProfile();
        getNotificationsStatus();



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (path1 != null || path2 != null || path3 != null || path4 != null || path5 != null || path6 != null) {
                    next.setEnabled(false);
                    pDialog = new SweetAlertDialog(EditMyProfile.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitle("Uploading Images... Please Wait");
                    pDialog.show();
                    ar[0] = path1;
                    ar[1] = path2;
                    ar[2] = path3;
                    ar[3] = path4;
                    ar[4] = path5;
                    ar[5] = path6;


                    for (int i = 0; i < 5; i++) {

                        if (ar[i] != null) {
                            tasktodo++;
                            uploadblob(ar[i], String.valueOf(i), phoneNo);
                        }
                    }
                }
                //else please upload

                else {
                    tasktodo = 0;
                    showdialog("Warning", "Please upload an image", 3);
                }

            }
        });

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
                    status = response.getString("status");

                    if (status.equals("success")) {
                        message = response.getString("message");
                        if (message.equals("No new notifications")) {
                            badge.setVisibility(View.GONE);

                        } else if (message.equals("new notifications")) {
                            badge.setVisibility(View.VISIBLE);

                        }


                    }else if (auth != null && auth.equals("authFailed")) {
                        sharedpreferences.edit().remove("userid").commit();
                        sharedpreferences.edit().remove("user_token").commit();
                        showdialogAuth("Error", "Token Expired Kindly Login Again", 1);

                    } else if (status.equals("success")) {


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

        final SweetAlertDialog sd = new SweetAlertDialog(EditMyProfile.this, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(EditMyProfile.this, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (img1) {
                    Picasso.get().load(resultUri).into(p1);
                    path1 = resultUri.getPath();
                  //  c1.setVisibility(View.VISIBLE);
                    // System.out.println("path == "+path1);
                }
                if (img2) {
                    Picasso.get().load(resultUri).into(p2);
                    path2 = resultUri.getPath();
                    c2.setVisibility(View.VISIBLE);

                    //path2 = selectedImage.getPath();

                }
                if (img3) {
                    Picasso.get().load(resultUri).into(p3);
                    path3 = resultUri.getPath();
                    c3.setVisibility(View.VISIBLE);

                }

                if (img4) {
                    Picasso.get().load(resultUri).into(p4);
                    path4 = resultUri.getPath();
                    c4.setVisibility(View.VISIBLE);
                }

                if (img5) {
                    Picasso.get().load(resultUri).into(p5);
                    path5 = resultUri.getPath();
                    c5.setVisibility(View.VISIBLE);

                }
                if (img6) {
                    Picasso.get().load(resultUri).into(p6);
                    path6 = resultUri.getPath();
                    c6.setVisibility(View.VISIBLE);

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {

                CropImage.activity(Uri.fromFile(imageFiles[0].getFile()))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .setAspectRatio(4, 3)
                        .start(EditMyProfile.this);

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

    void uploadblob(String path, String imgno, String phone) {

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
                    CloudBlockBlob blob = container.getBlockBlobReference(phone.replace("+", "") + "_" + imgno + ".jpg");
                    File source = new File(path);
                    blob.upload(new FileInputStream(source), source.length());
                    switch (imgno) {
                        case "0":
                            url1 = blob.getUri().toURL().toString();
                            break;
                        case "1":
                            url2 = blob.getUri().toURL().toString();
                            break;
                        case "2":
                            url3 = blob.getUri().toURL().toString();
                            break;
                        case "3":
                            url4 = blob.getUri().toURL().toString();
                            break;
                        case "4":
                            url5 = blob.getUri().toURL().toString();
                            break;
                        case "5":
                            url6 = blob.getUri().toURL().toString();
                            break;
                    }

                } catch (Exception ex) {


                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here

                        tasktodone++;
                        if (tasktodone == tasktodo) {
                            changeImages();


                        }
//                        } else {
//                            showdialog("Error", "Atleast One Image Is Required", 1);
//                        }
                    }
                });
            }
        });
    }



    private void changeImages() {
        pDialog = new SweetAlertDialog(EditMyProfile.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Uploading Images... Please Wait");
        pDialog.show();
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "user/updateUserMedia";
        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("Photo1", url1);
            jsonParam.put("Photo2", url2);
            jsonParam.put("Photo3", url3);
            jsonParam.put("Photo4", url4);
            jsonParam.put("Photo5", url5);
            jsonParam.put("Photo6", url6);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
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

                    status = response.getString("status");

                   if (status.equals("success")) {
                        message = response.getString("message");
                        if (message.equals("images updated successfully")) {
                            showdialog1("Success", "Images Changed", 2);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("photo1", url1);
                            editor.commit();
                        }
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
//                mainshimmer.showShimmer(false);
//                mainshimmer.hideShimmer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
               // System.out.println("XXX Volly Error :" + error);

                //pDialog.dismissWithAnimation();

//                mainshimmer.showShimmer(false);
//                mainshimmer.hideShimmer();

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

    private void removeImages(String photonum) {

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Removing Image... Please Wait");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        token = sharedpreferences.getString("user_token", "0");
        userid = sharedpreferences.getString("userid", "0");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String uri = portnVariable.com + "user/field/Update";

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            jsonParam.put("fieldName", photonum);
            jsonParam.put("fieldValue", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String status = null;
                String auth = null;
                pDialog.dismissWithAnimation();

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
                        showdialog1("Success", "Image Removed", 2);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismissWithAnimation();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();

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

    private void getMyProfile() {
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = portnVariable.com + "user/userProfile";
        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                JSONObject message = null;
                int age = 0;
                String email = null;
                phoneNo = null;
                String name = null;
                String birthday = null;
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
                        message = response.getJSONObject("message");
                        url1 = message.getString("Photo1");
                        url2 = message.getString("Photo2");
                        url3 = message.getString("Photo3");
                        url4 = message.getString("Photo4");
                        url5 = message.getString("Photo5");
                        url6 = message.getString("Photo6");
                        googleId = message.getString("googleId");
                        facebookId = message.getString("facebookId");

                      if (url1.length() > 0)
                      {
                          Picasso.get().load(url1).networkPolicy(NetworkPolicy.NO_CACHE)
                                  .memoryPolicy(MemoryPolicy.NO_CACHE).into(p1);
                          //c1.setVisibility(View.VISIBLE);

                      }
                      if (url2.length() > 0)
                      {
                          Picasso.get().load(url2).networkPolicy(NetworkPolicy.NO_CACHE)
                                  .memoryPolicy(MemoryPolicy.NO_CACHE).into(p2);
                          c2.setVisibility(View.VISIBLE);

                      }
                      if (url3.length() > 0)
                      {
                          Picasso.get().load(url3).networkPolicy(NetworkPolicy.NO_CACHE)
                                  .memoryPolicy(MemoryPolicy.NO_CACHE).into(p3);
                          c3.setVisibility(View.VISIBLE);

                      }
                      if (url4.length() > 0)
                      {
                          Picasso.get().load(url4).networkPolicy(NetworkPolicy.NO_CACHE)
                                  .memoryPolicy(MemoryPolicy.NO_CACHE).into(p4);
                          c4.setVisibility(View.VISIBLE);

                      }
                      if (url5.length() > 0)
                      {
                          Picasso.get().load(url5).networkPolicy(NetworkPolicy.NO_CACHE)
                                  .memoryPolicy(MemoryPolicy.NO_CACHE).into(p5);
                          c5.setVisibility(View.VISIBLE);

                      }
                      if (url6.length() > 0)
                      {
                          Picasso.get().load(url6).networkPolicy(NetworkPolicy.NO_CACHE)
                                  .memoryPolicy(MemoryPolicy.NO_CACHE).into(p6);
                          c6.setVisibility(View.VISIBLE);

                      }

                        name = message.getString("name");
                        myName.setText(name);

                        email = message.getString("email");
                        phoneNo = message.getString("phoneNo");
                        birthday = message.getString("DOB");
                        String birthday_split = birthday.substring(0, 10);

                        myEmail.setText(email);
                        myPhone.setText(phoneNo);
                        myDOB.setText(birthday_split);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
//                mainshimmer.showShimmer(false);
//                mainshimmer.hideShimmer();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
              //  System.out.println("XXX Volly Error :" + error);

                //pDialog.dismissWithAnimation();

//                mainshimmer.showShimmer(false);
//                mainshimmer.hideShimmer();

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
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent main = new Intent(EditMyProfile.this, Homescreen.class);
                        startActivity(main);
                    }
                })
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }





}