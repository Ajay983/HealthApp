package com.linkitsoft.dategoal.ui.group;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.GroupModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Calendar;
import java.util.Date;
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
 * Use the {@link Creategroupfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Creategroupfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Creategroupfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Creategroupfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Creategroupfragment newInstance(String param1, String param2) {
        Creategroupfragment fragment = new Creategroupfragment();
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

    Context context;
    SharedPreferences sharedpreferences;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    public String userid, token;

    EditText grpname;
    EditText grpsmry;
    Button save;
    ImageButton grpimg;
    String path = "";
    String url = "";
    String namegrp = "";
    String smrygrp = "";

    EasyImage easyImage;
    ImageView badge;

    Boolean for_group_img;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_creategroupfragment, container, false);

        context = root.getContext();
        portnVariable = new PortnVariable();
        for_group_img = false;

        grpimg = root.findViewById(R.id.imgbtn_expand_lookingfor);
        grpname = root.findViewById(R.id.editTextTextPersonName2);
        grpsmry = root.findViewById(R.id.editTextTextPersonName3);
        save = root.findViewById(R.id.button14);
        badge = root.findViewById(R.id.imageView4);
        userid = sharedpreferences.getString("userid", "0");
        FloatingActionButton fabfilter = root.findViewById(R.id.floatingActionButton3);
        fabfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
            }
        });


        easyImage = new EasyImage.Builder(context)
// Setting to true will cause taken pictures to show up in the device gallery, DEFAULT false
                .setCopyImagesToPublicGalleryFolder(false)
                .build();

        grpimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for_group_img = true;
                easyImage.openGallery(Creategroupfragment.this);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!grpname.getText().toString().isEmpty() && !grpsmry.getText().toString().isEmpty() && !path.equals("")) {

                    save.setEnabled(false);
                    pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitle("Processing");
                    pDialog.show();

                    namegrp = grpname.getText().toString();
                    smrygrp = grpsmry.getText().toString();
                    Date currentTime = Calendar.getInstance().getTime();

                    uploadblob(grpname.getText().toString(), currentTime.toString());

                } else {
                    showdialog("Error", "Must fill all required fields", 1);
                }
            }
        });
        getNotificationsStatus();


        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                    Picasso.get().load(resultUri).into(grpimg);
                    path = resultUri.getPath();

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
                        .start(getContext(), Creategroupfragment.this);

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

    void uploadblob(String grppnm, String timestamp) {

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
                    String storageContainer = "groupdisplayimg";
                    CloudBlobContainer container = blobClient.getContainerReference(storageContainer);

                    // Create or overwrite the blob (with the name "example.jpeg") with contents from a local file.
                    CloudBlockBlob blob = container.getBlockBlobReference(grppnm + "_" + timestamp + ".jpg");
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
                            postgrpapi();
                        }
                    }
                });
            }
        });
    }


    private void postgrpapi() {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonParam = null;
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");

        String urll = portnVariable.com + "group";

        GroupModel groupModel = new GroupModel();
        groupModel.setTitle(namegrp);
        groupModel.setSummary(smrygrp);
        groupModel.setProfilePic(url);
        groupModel.setUserID(userid);

        try {
            jsonParam = new JSONObject(new Gson().toJson(groupModel));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urll, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismissWithAnimation();
                save.setEnabled(true);
                String status = null;
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
                    if(response.has("status")) {

                        status = response.getString("status");

                        if (status.equals("success")) {

                            showdialog("Successful", "Group is Created", 2);
                            ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();
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
              //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
                save.setEnabled(true);
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

                        }else if (auth != null && auth.equals("authFailed")) {
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

    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }

}