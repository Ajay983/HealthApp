package com.linkitsoft.dategoal.ui.signup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Models.UserModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

import static android.app.Activity.RESULT_OK;

public class EnterPhotos extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    SharedPreferences sharedpreferences;

    ImageButton uploadimage1;
    ImageButton uploadimage2;
    ImageButton uploadimage3;
    ImageButton uploadimage4;
    ImageButton uploadimage5;
    ImageButton uploadimage6;
    ImageButton cancel_image1, cancel_image2, cancel_image3, cancel_image4, cancel_image5, cancel_image6;

    Boolean img1;
    Boolean img2;
    Boolean img3;
    Boolean img4;
    Boolean img5;
    Boolean img6;
    Boolean c1;

    String path1, path2, path3, path4, path5, path6;

    String url1 = "";
    String url2 = "";
    String url3 = "";
    String url4 = "";
    String url5 = "";
    String url6 = "";


    public final int GET_FROM_GALLERY = 1;

    Uri selectedImage;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String aj;

    String phone;
    String pass;
    String name;
    String email;
    String birthdate;
    String ehtnicity;
    String gender;
    String herefor;
    String passion;
    boolean relegion;
    boolean tatto;
    String relegionname;
    String dreamdate;
    String relagoal;
    String sexuality;

    String city;
    String state;
    String country;

    PortnVariable portnVariable;
    SweetAlertDialog pDialog;

    int tasktodo = 0;
    int tasktodone = 0;
    Context context;
    String ar[] = new String[6];
    Date date;
    String googleId,loginType,facebookId,googleEmail;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    EasyImage easyImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_enter_photos, container, false);
        Activity parent = getActivity();
        context = getContext();

        easyImage = new EasyImage.Builder(context)
                .setCopyImagesToPublicGalleryFolder(false)
                .build();


        imagebtnback = parent.findViewById(R.id.imageButton19);
        portnVariable = new PortnVariable();

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


        next = root.findViewById(R.id.imageButton2);
//        next.setVisibility(View.GONE);
//        next.setEnabled(false);

        uploadimage1 = root.findViewById(R.id.imageButton4);
        uploadimage2 = root.findViewById(R.id.imageButton5);


        uploadimage3 = root.findViewById(R.id.imageButton6);
        uploadimage4 = root.findViewById(R.id.imageButton7);
        uploadimage5 = root.findViewById(R.id.imageButton8);
        uploadimage6 = root.findViewById(R.id.imageButton9);
        cancel_image1 = root.findViewById(R.id.image_cancel1);
        cancel_image2 = root.findViewById(R.id.image_cancel2);
        cancel_image3 = root.findViewById(R.id.image_cancel3);
        cancel_image4 = root.findViewById(R.id.image_cancel4);
        cancel_image5 = root.findViewById(R.id.image_cancel5);
        cancel_image6 = root.findViewById(R.id.image_cancel6);

        uploadimage2.setEnabled(false);
        uploadimage3.setEnabled(false);
        uploadimage4.setEnabled(false);
        uploadimage5.setEnabled(false);
        uploadimage6.setEnabled(false);

        googleId = sharedpreferences.getString("googleId", null);
        loginType = sharedpreferences.getString("loginType",null);
        googleEmail = sharedpreferences.getString("googleEmail",null);
        facebookId = sharedpreferences.getString("facebookId",null);


        cancel_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1 = true;
                Picasso.get().load(R.drawable.addimage).into(uploadimage1);
                path1 = null;
                cancel_image1.setVisibility(View.GONE);
//                next.setVisibility(View.GONE);
//                next.setEnabled(false);
            }
        });

        cancel_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(uploadimage2);
                cancel_image2.setVisibility(View.GONE);
                path2 = null;
            }
        });
        cancel_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(uploadimage3);
                cancel_image3.setVisibility(View.GONE);
                path3 = null;

            }
        });
        cancel_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(uploadimage4);
                cancel_image4.setVisibility(View.GONE);
                path4 = null;

            }
        });
        cancel_image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(uploadimage5);
                cancel_image5.setVisibility(View.GONE);
                path5 = null;

            }
        });
        cancel_image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(R.drawable.addimage).into(uploadimage6);
                cancel_image6.setVisibility(View.GONE);
                path6 = null;

            }
        });


        uploadimage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1 = true;
                aj = "hey";
                img2 = false;
                img3 = false;
                img4 = false;
                img5 = false;
                img6 = false;
                easyImage.openGallery(EnterPhotos.this);
                uploadimage2.setEnabled(true);

//                next.setVisibility(View.VISIBLE);
//                next.setVisibility(View.VISIBLE);
//
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent , GET_FROM_GALLERY);
            }
        });

        uploadimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img2 = true;

                img1 = false;
                img3 = false;
                img4 = false;
                img5 = false;
                img6 = false;
                easyImage.openGallery(EnterPhotos.this);

                uploadimage3.setEnabled(true);


                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);*/
            }
        });

        uploadimage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img3 = true;
                img1 = false;
                img2 = false;
                img4 = false;
                img5 = false;
                img6 = false;

                easyImage.openGallery(EnterPhotos.this);

                uploadimage4.setEnabled(true);
                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);*/
            }
        });

        uploadimage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img4 = true;
                img1 = false;
                img2 = false;
                img3 = false;
                img5 = false;
                img6 = false;

                easyImage.openGallery(EnterPhotos.this);

                uploadimage5.setEnabled(true);
                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);*/
            }
        });

        uploadimage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img5 = true;
                img1 = false;
                img2 = false;
                img3 = false;
                img4 = false;
                img6 = false;
                easyImage.openGallery(EnterPhotos.this);
                uploadimage6.setEnabled(true);

               /* Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);*/
            }
        });

        uploadimage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img6 = true;
                img1 = false;
                img2 = false;
                img3 = false;
                img4 = false;
                img5 = false;
                easyImage.openGallery(EnterPhotos.this);

                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);*/
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (path1 != null && uploadimage1.getDrawable() != null || path2 != null || path3 != null || path4 != null || path5 != null || path6 != null) {
                    next.setEnabled(false);
                    pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setTitle("Creating Account. Please Wait");
                    pDialog.show();
                    phone = sharedpreferences.getString("mobnum", null);
                    ar[0] = path1;
                    ar[1] = path2;
                    ar[2] = path3;
                    ar[3] = path4;
                    ar[4] = path5;
                    ar[5] = path6;

                    for (int i = 0; i <= 5; i++) {
                        if (ar[i] != null) {
                            tasktodo++;
                        }
                    }


                    for (int i = 0; i < tasktodo; i++) {

                        uploadblob(ar[i], String.valueOf(i), phone);
                        pDialog.dismiss();
                    }
                }
                //else please upload

                else {
                    tasktodo = 0;
                    showdialog("Warning", "Please upload an image", 3);
                }

            }
        });
        return root;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (img1) {
                    Picasso.get().load(resultUri).into(uploadimage1);
                    path1 = resultUri.getPath();
                    cancel_image1.setVisibility(View.VISIBLE);
                    // System.out.println("path == "+path1);
                }
                if (img2) {
                    Picasso.get().load(resultUri).into(uploadimage2);
                    path2 = resultUri.getPath();
                    cancel_image2.setVisibility(View.VISIBLE);

                    //path2 = selectedImage.getPath();

                }
                if (img3) {
                    Picasso.get().load(resultUri).into(uploadimage3);
                    path3 = resultUri.getPath();
                    cancel_image3.setVisibility(View.VISIBLE);

                }

                if (img4) {
                    Picasso.get().load(resultUri).into(uploadimage4);
                    path4 = resultUri.getPath();
                    cancel_image4.setVisibility(View.VISIBLE);
                }

                if (img5) {
                    Picasso.get().load(resultUri).into(uploadimage5);
                    path5 = resultUri.getPath();
                    cancel_image5.setVisibility(View.VISIBLE);

                }
                if (img6) {
                    Picasso.get().load(resultUri).into(uploadimage6);
                    path6 = resultUri.getPath();
                    cancel_image6.setVisibility(View.VISIBLE);

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
                        .setAspectRatio(4,3)
                        .start(getContext(),EnterPhotos.this);

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

                            if ("google".equalsIgnoreCase(loginType)){
                                postGoogleUSer();
                            }else if ("facebook".equalsIgnoreCase(loginType)){
                                postFacebookUser();
                            }
                            else if ("email".equalsIgnoreCase(loginType)){
                                postuser();
                            }else {
                                showdialog("Error", "Try Again",1);
                            }



                        }
//                        } else {
//                            showdialog("Error", "Atleast One Image Is Required", 1);
//                        }
                    }
                });
            }
        });
    }

    private void postFacebookUser(){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Creating Account. Please Wait");
        pDialog.show();
        phone = sharedpreferences.getString("mobnum", null);
        name = sharedpreferences.getString("name", null);
        birthdate = sharedpreferences.getString("dob", null);
        ehtnicity = sharedpreferences.getString("ethnicity", null);
        gender = sharedpreferences.getString("gender", null);
        herefor = sharedpreferences.getString("herefor", null);
        passion = sharedpreferences.getString("passionlist", "none");
        relegion = sharedpreferences.getBoolean("religionyes", true);
        tatto = sharedpreferences.getBoolean("tatto", false);
        relegionname = sharedpreferences.getString("religion", "none");
        dreamdate = sharedpreferences.getString("dreamdt", null);
        relagoal = sharedpreferences.getString("relgoal", null);
        sexuality = sharedpreferences.getString("sexuality", null);
        city = "none";
        state = "none";
        country = "none";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonParam = null;
        String url = portnVariable.com + "user";
        UserModel userModel = new UserModel();
        userModel.setFacebookId(facebookId);
        userModel.setLoginType(loginType);
        userModel.setEmail(facebookId);
        userModel.setName(name);

        userModel.setDOB(date);
        userModel.setGender(gender);
        userModel.setEthinicity(ehtnicity);
        userModel.setSexuality(sexuality);
        userModel.setHerefor(herefor);
        userModel.setPhone(phone);
        userModel.setPassions(passion);
        userModel.setCity(city);
        userModel.setState(state);
        userModel.setCountry(country);

        userModel.setPhoto1(url1);
        userModel.setPhoto2(url2);
        userModel.setPhoto3(url3);
        userModel.setPhoto4(url4);
        userModel.setPhoto5(url5);
        userModel.setPhoto6(url6);

        userModel.setRelegion(String.valueOf(relegion));
        userModel.setRelegious(relegionname);
        userModel.setTatto(String.valueOf(tatto));
        userModel.setDreamDate(dreamdate);
        userModel.setRelationShipGoals(relagoal);

        try {
            jsonParam = new JSONObject(new Gson().toJson(userModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                next.setEnabled(true);

                try {
                    String status = response.getString("message");
                    //   System.out.println("Check Return value from api " + status);
                    if (status.equals("ok")) {
                        showdialog1("Success", "Your Account Has Been Created", 2);


//                            getActivity().finish();
//                            startActivity(new Intent(getActivity(),AllowNoti.class));

                    } else showdialog("unsuccessful", "One or more information is not correct", 1);

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
                next.setEnabled(true);


            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(jsonObjectRequest);

    }

    private void postGoogleUSer() {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Creating Account. Please Wait");
        pDialog.show();
        phone = sharedpreferences.getString("mobnum", null);
        name = sharedpreferences.getString("name", null);
        birthdate = sharedpreferences.getString("dob", null);
        ehtnicity = sharedpreferences.getString("ethnicity", null);
        gender = sharedpreferences.getString("gender", null);
        herefor = sharedpreferences.getString("herefor", null);
        passion = sharedpreferences.getString("passionlist", "none");
        relegion = sharedpreferences.getBoolean("religionyes", true);
        tatto = sharedpreferences.getBoolean("tatto", false);
        relegionname = sharedpreferences.getString("religion", "none");
        dreamdate = sharedpreferences.getString("dreamdt", null);
        relagoal = sharedpreferences.getString("relgoal", null);
        sexuality = sharedpreferences.getString("sexuality", null);
        city = "none";
        state = "none";
        country = "none";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonParam = null;
        String url = portnVariable.com + "user";
        UserModel userModel = new UserModel();

        userModel.setGoogleId(googleId);
        userModel.setLoginType(loginType);
        userModel.setName(name);
        userModel.setEmail(googleEmail);

        userModel.setDOB(date);
        userModel.setGender(gender);
        userModel.setEthinicity(ehtnicity);
        userModel.setSexuality(sexuality);
        userModel.setHerefor(herefor);
        userModel.setPhone(phone);
        userModel.setPassions(passion);
        userModel.setCity(city);
        userModel.setState(state);
        userModel.setCountry(country);

        userModel.setPhoto1(url1);
        userModel.setPhoto2(url2);
        userModel.setPhoto3(url3);
        userModel.setPhoto4(url4);
        userModel.setPhoto5(url5);
        userModel.setPhoto6(url6);

        userModel.setRelegion(String.valueOf(relegion));
        userModel.setRelegious(relegionname);
        userModel.setTatto(String.valueOf(tatto));
        userModel.setDreamDate(dreamdate);
        userModel.setRelationShipGoals(relagoal);

        try {
            jsonParam = new JSONObject(new Gson().toJson(userModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                next.setEnabled(true);

                try {
                    String status = response.getString("message");
                    //   System.out.println("Check Return value from api " + status);
                    if (status.equals("ok")) {
                        showdialog1("Success", "Your Account Has Been Created", 2);


//                            getActivity().finish();
//                            startActivity(new Intent(getActivity(),AllowNoti.class));

                    } else showdialog("unsuccessful", "One or more information is not correct", 1);

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
                next.setEnabled(true);


            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(jsonObjectRequest);

    }


    private void postuser() {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Creating Account. Please Wait");
        pDialog.show();
        //get all values from shared pref
        email = sharedpreferences.getString("email", null);
        pass = sharedpreferences.getString("password", null);
        phone = sharedpreferences.getString("mobnum", null);
        name = sharedpreferences.getString("name", null);
        birthdate = sharedpreferences.getString("dob", null);
        ehtnicity = sharedpreferences.getString("ethnicity", null);
        gender = sharedpreferences.getString("gender", null);
        herefor = sharedpreferences.getString("herefor", null);
        passion = sharedpreferences.getString("passionlist", "none");
        relegion = sharedpreferences.getBoolean("religionyes", true);
        tatto = sharedpreferences.getBoolean("tatto", false);
        relegionname = sharedpreferences.getString("religion", "none");
        dreamdate = sharedpreferences.getString("dreamdt", null);
        relagoal = sharedpreferences.getString("relgoal", null);
        sexuality = sharedpreferences.getString("sexuality", null);

        city = "none";
        state = "none";
        country = "none";


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(birthdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonParam = null;
        String url = portnVariable.com + "user";


        UserModel userModel = new UserModel();
        userModel.setLoginType(loginType);
        userModel.setEmail(email);
        userModel.setPassword(pass);
        userModel.setName(name);

        userModel.setDOB(date);
        userModel.setGender(gender);
        userModel.setEthinicity(ehtnicity);
        userModel.setSexuality(sexuality);
        userModel.setHerefor(herefor);
        userModel.setPhone(phone);
        userModel.setPassions(passion);
        userModel.setCity(city);
        userModel.setState(state);
        userModel.setCountry(country);

        userModel.setPhoto1(url1);
        userModel.setPhoto2(url2);
        userModel.setPhoto3(url3);
        userModel.setPhoto4(url4);
        userModel.setPhoto5(url5);
        userModel.setPhoto6(url6);

        userModel.setRelegion(String.valueOf(relegion));
        userModel.setRelegious(relegionname);
        userModel.setTatto(String.valueOf(tatto));
        userModel.setDreamDate(dreamdate);
        userModel.setRelationShipGoals(relagoal);


        try {
            jsonParam = new JSONObject(new Gson().toJson(userModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                next.setEnabled(true);

                try {
                    String status = response.getString("message");
                    //    System.out.println("Check Return value from api " + status);
                    if (status.equals("ok")) {
                        showdialog1("Success", "Your Account Has Been Created", 2);
//                            getActivity().finish();
//                            startActivity(new Intent(getActivity(),AllowNoti.class));

                    } else showdialog("unsuccessful", "One or more information is not correct", 1);

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
               // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
                next.setEnabled(true);


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

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)

                .setContentText(content);
        sd.show();
    }

    public void showdialog1(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), AllowNoti.class));

                    }
                })

                .setContentText(content);
        sd.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }
}

//        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
//
//            selectedImage = data.getData();
//
//            if (img1) {
//                Picasso.get().load(selectedImage).into(uploadimage1);
//                path1 =  getRealPathFromUriforimage(context, selectedImage);
//                System.out.println("path == "+path1);
//
//
//            }
//            if (img2) {
//                Picasso.get().load(selectedImage).into(uploadimage2);
//                path2 = selectedImage.getPath();
//
//            }
//            if (img3) {
//                Picasso.get().load(selectedImage).into(uploadimage3);
//                path3 = selectedImage.getPath();
//
//            }
//
//            if (img4) {
//                Picasso.get().load(selectedImage).into(uploadimage4);
//                path4 =  selectedImage.getPath();
//
//            }
//
//            if (img5) {
//                Picasso.get().load(selectedImage).into(uploadimage5);
//                path5 =  selectedImage.getPath();
//
//            }
//            if (img6) {
//                Picasso.get().load(selectedImage).into(uploadimage6);
//                path6 =  selectedImage.getPath();
//
//            }
//
//
//        }
//****************************************upload blob via jason task depriciated****************************************

  /*
   public static String getRealPathFromUriforimage(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

   private class JsonTask2  extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {

            try {
                // Retrieve storage account from connection-string.
                CloudStorageAccount storageAccount = CloudStorageAccount.parse(portnVariable.storageConnectionString);

                // Create the blob client.
                CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

                // Retrieve reference to a previously created container.
                String storageContainer = "userprofileimg";
                CloudBlobContainer container = blobClient.getContainerReference(storageContainer);

                // Create or overwrite the blob (with the name "example.jpeg") with contents from a local file.
                CloudBlockBlob blob = container.getBlockBlobReference(params[2]+"_"+params[1]+".jpg");
                File source = new File(params[0]);
                blob.upload(new FileInputStream(source), source.length());
                switch (params[1]) {
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


            } catch (Exception e) {
                // Output the stack trace.
                e.printStackTrace();


            }
            return null;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);




        }
    }*/

//****************************************option 1 to get real image path from uri****************************************


   /* public static String getRealPathFromUriforimage(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
*/
//****************************************option 2 to get real image path from uri****************************************

   /* public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    *//**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 *//*
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    *//**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 *//*
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    *//**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 *//*
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    *//**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 *//*
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }*/



