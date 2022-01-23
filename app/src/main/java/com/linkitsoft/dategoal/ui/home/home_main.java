package com.linkitsoft.dategoal.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.linkitsoft.dategoal.ui.signup.EnableLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_main extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public home_main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_main.
     */
    // TODO: Rename and change types and number of parameters
    public static home_main newInstance(String param1, String param2) {
        home_main fragment = new home_main();
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


    //First Get Longitude And Latitude Then Fetching from Api (Friends List)

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    location = task.getResult();
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        getAddressFromLocation(latitude,longitude,context.getApplicationContext());

                        updateCords(longitude, latitude );

                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Handler handler = new Handler(Looper.getMainLooper());
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        getPeopleapi(m, longitude, latitude);

                                    }
                                });
                            }

                        });
                    } else {
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                Location location1 = locationResult.getLastLocation();
//                                aj_lat = location1.getLatitude();
//                                aj_long = location1.getLongitude();
//                                latLong.setText(location1.toString());
                            }
                        };
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }

                }
            });
        } else {
            startActivity(new Intent(getActivity(), EnableLocation.class));

        }
    }

    String country = "";
    String city = "";
    String state = "";

    public void getAddressFromLocation(final double latitude, final double longitude,
                                       final Context context) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getAdminArea()).append("\n");
                sb.append(address.getCountryName());

                country = address.getCountryName();
                city = address.getLocality();
                state = address.getAdminArea();
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
//        getCurrentLocation();
        getCurrentLocation();
    }


    //updated lat long save in DB:


    private void updateCords(double longi, double lati) {



        double[] coordinates = new double[2];
        coordinates[0] = (longi);
        coordinates[1] = (lati);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String uri = portnVariable.com + "user/Update/CordsLoc";

        JSONObject jsonParam = new JSONObject();
        JSONObject geometry = new JSONObject();
        JSONArray coordinaates = new JSONArray();
        String type = "Point";

        try {
            coordinaates.put(coordinates[0]);
            coordinaates.put(coordinates[1]);
            geometry.put("type", type);
            geometry.put("coordinates", coordinaates);
            jsonParam.put("userID", userid);
            jsonParam.put("geometry", geometry);
            jsonParam.put("country", country);
            jsonParam.put("state",state);
            jsonParam.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject message = null;
                String msg = null;
                JSONObject check = null;
                String auth = null;


                try {
                    if (response.has("auth")) {
                        auth = response.getString("auth");
                    }
                    msg = response.getString("message");

                    if (msg.equals("fields updated!")) {

//                        Toast.makeText(context, "fields update", Toast.LENGTH_SHORT).show();
                    } else if (auth != null && auth.equals("authFailed")) {
                      try {
                          SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                          SharedPreferences.Editor editor = preferences.edit();
                          editor.clear();
                          editor.apply();


                          Intent intent = new Intent(getActivity(), Login.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                          //  showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                      }
                      catch (Exception ex){}
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

                //  pDialog.dismissWithAnimation();
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

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent i = new Intent(context, Login.class);
                        startActivity(i);
                    }
                });
        try {
            sd.show();
        }
        catch (Exception ex){}
    }


    private void getPeopleapi(ViewPager m, double longi, double lat) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "user/listUsers";

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("PageNo", "1");
            jsonParam.put("Count", "5");
            jsonParam.put("Sorting", "BestMatching");
            jsonParam.put("userID", userid);
            jsonParam.put("lng", longi);
            jsonParam.put("lat", lat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                pDialog.dismissWithAnimation();
                String status = null;
                JSONArray message = null;
                String msg = null;
                String auth = null;

                try {
                    if (response.has("status")) {
                        status = response.getString("status");
                        msg = response.getString("message");

                        if (status.equals("success")) {

                            if (msg.equals("No users at the moment!")) {
                                rippleBaackground.stopRippleAnimation();
                                rippleBaackground.setVisibility(View.GONE);
                                circleImageView.setVisibility(View.GONE);
                                nobodyfound.setVisibility(View.VISIBLE);

                            }
                            else {
                                message = response.getJSONArray("message");
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("latitude", String.valueOf(latitude));
                                editor.putString("longitude", String.valueOf(longitude));
                                editor.apply();

                                if (message.length() > 0) {
                                    m.setCurrentItem(1);
                                    nobodyfound.setVisibility(View.GONE);
                                }  else{
                                    rippleBaackground.stopRippleAnimation();
                                    rippleBaackground.setVisibility(View.GONE);
                                    circleImageView.setVisibility(View.GONE);
                                    nobodyfound.setVisibility(View.VISIBLE);

                                }
                            }
                        }

                    }

                    if (response.has("auth")) {
                        auth = response.getString("auth");
                        if (auth != null && auth.equals("authFailed")) {
                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();

                            SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();


                            if(!fragmentManager.isDestroyed()) {
                                Intent intent = new Intent(context, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ((AppCompatActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                startActivity(intent);
                            }

                            //  showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
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

                //  pDialog.dismissWithAnimation();
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


    Context context;
    SharedPreferences sharedpreferences;
    PortnVariable portnVariable;
    public String userid;
    public String token;
    public float aj_lat, aj_long;
    public double latitude, longitude;
    public String photos;
    RippleBackground rippleBaackground;
    CircleImageView circleImageView;
    ConstraintLayout nobodyfound;
    FusedLocationProviderClient client;
    ViewPager m;
    Location location;
    android.app.FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_main, container, false);

        context = root.getContext();
        fragmentManager = ((Activity) context).getFragmentManager();

        portnVariable = new PortnVariable();

        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token", "0");


        rippleBaackground = root.findViewById(R.id.content);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ContextCompat.checkSelfPermission(getActivity()
                , Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        m = getParentFragment().getView().findViewById(R.id.viewPagerhome);

        circleImageView = root.findViewById(R.id.centerImage);
        nobodyfound = root.findViewById(R.id.nodody);


        Callback asd2 = new Callback() {
            @Override
            public void onSuccess() {
                getImage();
            }

            @Override
            public void onError(Exception e) {

            }
        };


        Callback asd = new Callback() {
            @Override
            public void onSuccess() {
                getImage();


            }

            @Override
            public void onError(Exception e) {

            }
        };


        Picasso.get().load(R.drawable.avtr).into(circleImageView, asd);
        rippleBaackground.startRippleAnimation();

        try {

        } catch (Exception ex) {
        }
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            showdialog("Error", "Location Permission Denied", 1);
        }
    }

    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }


    private void getImage() {
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
                String status = null;
                JSONObject message = null;
                int age = 0;
                String profileurl;


                try {
                    if (response.has("status")) {
                        status = response.getString("status");
                        if (status.equals("success")) {
                            message = response.getJSONObject("message");
                            profileurl = message.getString("Photo1");
                            Picasso.get().load(profileurl).networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(circleImageView);
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
                //   System.out.println("XXX Volly Error :" + error);

                //pDialog.dismissWithAnimation();

//                mainshimmer.showShimmer(false);
//                mainshimmer.hideShimmer();

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }


}