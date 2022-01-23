package com.linkitsoft.dategoal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateMyProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateMyProfile extends Fragment {
    Boolean img1;
    Boolean img2;
    Boolean img3;
    Boolean img4;
    Boolean img5;
    Boolean img6;
    String phoneNo;

    static int check = 0;

    String path1, path2, path3, path4, path5, path6;

    String url1 = "";
    String url2 = "";
    String url3 = "";
    String url4 = "";
    String url5 = "";
    String url6 = "";
    int tasktodone = 0;
    int tasktodo = 0;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateMyProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateMyProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateMyProfile newInstance(String param1, String param2) {
        UpdateMyProfile fragment = new UpdateMyProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_update_my_profile, container, false);

        context = root.getContext();
        portnVariable = new PortnVariable();
        easyImage = new EasyImage.Builder(context).setCopyImagesToPublicGalleryFolder(false).build();

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

        p1 = root.findViewById(R.id.imageButton4);
        p2 = root.findViewById(R.id.imageButton5);
        p3 = root.findViewById(R.id.imageButton6);
        p4 = root.findViewById(R.id.imageButton7);
        p5 = root.findViewById(R.id.imageButton8);
        p6 = root.findViewById(R.id.imageButton9);
        c1 = root.findViewById(R.id.image_cancel1);
        c2 = root.findViewById(R.id.image_cancel2);
        c3 = root.findViewById(R.id.image_cancel3);
        c4 = root.findViewById(R.id.image_cancel4);
        c5 = root.findViewById(R.id.image_cancel5);
        c6 = root.findViewById(R.id.image_cancel6);
        badge = root.findViewById(R.id.imageView4);
        notification = root.findViewById(R.id.floatingActionButton3);
        next = root.findViewById(R.id.save);
        myName = root.findViewById(R.id.myName);
        myEmail = root.findViewById(R.id.myEmail);
        myDOB = root.findViewById(R.id.myDOB);
        myPhone = root.findViewById(R.id.myPhone);
        save = root.findViewById(R.id.save);

        connect = root.findViewById(R.id.connectAcc);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getActivity(), ConnectedAccs.class);
                main.putExtra("facebookId",facebookId);
                main.putExtra("googleId",googleId);
                startActivity(main);
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p1);
                c1.setVisibility(View.GONE);
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p2);
                c2.setVisibility(View.GONE);

            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p3);
                c3.setVisibility(View.GONE);
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p4);
                c4.setVisibility(View.GONE);
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p5);
                c5.setVisibility(View.GONE);
            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(p6);
                c6.setVisibility(View.GONE);
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
                easyImage.openGallery(UpdateMyProfile.this);
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
                easyImage.openGallery(UpdateMyProfile.this);
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
                easyImage.openGallery(UpdateMyProfile.this);
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
                easyImage.openGallery(UpdateMyProfile.this);
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
                easyImage.openGallery(UpdateMyProfile.this);
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
                easyImage.openGallery(UpdateMyProfile.this);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (path1 != null || path2 != null || path3 != null || path4 != null || path5 != null || path6 != null) {
                    next.setEnabled(false);
                    pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitle("Uploading Images... Please Wait");
                    pDialog.show();
                    ar[0] = path1;
                    ar[1] = path2;
                    ar[2] = path3;
                    ar[3] = path4;
                    ar[4] = path5;
                    ar[5] = path6;

                   /* for (int i = 0; i <= 5; i++) {
                        if (ar[i] != null) {
                            tasktodo++;
                        }
                    }*/


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


        getMyProfile();
        getNotificationsStatus();



        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }
    private void getNotificationsStatus() {
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                    }

                    status = response.getString("status");

                    if (status.equals("success")) {
                        message = response.getString("message");
                        if (message.equals("No new notifications")) {
                            badge.setVisibility(View.GONE);

                        } else if (auth != null && auth.equals("authFailed")) {
                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();
                            showdialogAuth("Error", "Token Expired Kindly Login Again", 1);

                        }
                        else if (message.equals("new notifications")) {
                            badge.setVisibility(View.VISIBLE);

                        }


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

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(context, Login.class);
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
                    c1.setVisibility(View.VISIBLE);
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
        easyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {

                CropImage.activity(Uri.fromFile(imageFiles[0].getFile()))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .setAspectRatio(4, 3)
                        .start(getContext(), UpdateMyProfile.this);

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
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Uploading Images... Please Wait");
        pDialog.show();
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                next.setEnabled(true);

                String status = null;
                String message = null;

                try {
                    status = response.getString("status");

                  if (status.equals("success")) {
                        message = response.getString("message");
                        if (message.equals("images updated successfully")) {
                            showdialog1("Success", "Images Changed", 2);

                            getActivity().getFragmentManager().popBackStack();
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

                pDialog.dismissWithAnimation();
                next.setEnabled(true);


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




    private void getMyProfile() {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Loading...");
        pDialog.show();
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                pDialog.dismissWithAnimation();
                String status = null;
                JSONObject message = null;
                int age = 0;
                String profileurl;
                String profileurl6 = null;
                String profileurl2 = null;
                String profileurl3 = null;
                String profileurl4 = null;
                String profileurl5 = null;
                String email = null;
                phoneNo = null;
                String name = null;
                String birthday = null;
                String auth = null;

                try {
                    check = 1;

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

                        message = response.getJSONObject("message");
                        profileurl = message.getString("Photo1");
                        profileurl2 = message.getString("Photo2");
                        profileurl3 = message.getString("Photo3");
                        profileurl4 = message.getString("Photo4");
                        profileurl5 = message.getString("Photo5");
                        profileurl6 = message.getString("Photo6");
                        url1 = profileurl;
                        url2 = profileurl2;
                        url3 = profileurl3;
                        url4 = profileurl4;
                        url5 = profileurl5;
                        url6 = profileurl6;

                        googleId = message.getString("googleId");
                        facebookId = message.getString("facebookId");

                        if (profileurl.length() > 0 && profileurl2.length() > 0 && profileurl3.length() > 0 && profileurl4.length() > 0
                                && profileurl5.length() > 0 && profileurl6.length() > 0) {
                            Picasso.get().load(profileurl).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p1);
                            Picasso.get().load(profileurl2).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p2);
                            Picasso.get().load(profileurl3).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p3);
                            Picasso.get().load(profileurl4).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p4);
                            Picasso.get().load(profileurl5).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p5);
                            Picasso.get().load(profileurl6).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p6);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c5.setVisibility(View.VISIBLE);
                            c6.setVisibility(View.VISIBLE);
                        } else if (profileurl.length() > 0 && profileurl2.length() > 0 && profileurl3.length() > 0 && profileurl4.length() > 0
                                && profileurl5.length() > 0) {
                            Picasso.get().load(profileurl).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p1);
                            Picasso.get().load(profileurl2).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p2);
                            Picasso.get().load(profileurl3).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p3);
                            Picasso.get().load(profileurl4).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p4);
                            Picasso.get().load(profileurl5).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p5);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c5.setVisibility(View.VISIBLE);
                        } else if (profileurl.length() > 0 && profileurl2.length() < 0 && profileurl3.length() > 0 && profileurl4.length() > 0) {
                            Picasso.get().load(profileurl).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p1);
                            Picasso.get().load(profileurl2).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p2);
                            Picasso.get().load(profileurl3).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p3);
                            Picasso.get().load(profileurl4).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p4);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                        } else if (profileurl.length() > 0 && profileurl2.length() > 0 && profileurl3.length() > 0) {
                            Picasso.get().load(profileurl).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p1);
                            Picasso.get().load(profileurl2).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p2);
                            Picasso.get().load(profileurl3).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p3);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                        } else if (profileurl.length() > 0 && profileurl2.length() > 0) {
                            Picasso.get().load(profileurl).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p1);
                            Picasso.get().load(profileurl2).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(p2);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                        } else {
                            c1.setVisibility(View.VISIBLE);
                            Picasso.get().load(profileurl).into(p1);
                        }

                        name = message.getString("name");
                        myName.setText(name);

                        email = message.getString("email");
                        phoneNo = message.getString("phoneNo");
                        birthday = message.getString("DOB");
                        String birthday_split = birthday.substring(0, 10);

                        myEmail.setText("Email:  " + email);
                        myPhone.setText("Phone number:   " + phoneNo);
                        myDOB.setText(birthday_split);
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
              //  System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();

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

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }
    public void showdialog1(String title, String content, int type) {
        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent main = new Intent(getActivity(), Homescreen.class);
                        startActivity(main);
                    }
                })
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }


}