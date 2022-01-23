package com.linkitsoft.dategoal.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.App_Socket;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.FriendsModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.RecyclerAdpaters.PeopleListRecycler;
import com.linkitsoft.dategoal.ui.Profile.Profile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_Map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_Map extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GoogleMap mMap;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_Map() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_Map.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_Map newInstance(String param1, String param2) {
        Home_Map fragment = new Home_Map();
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
    public String userid,token;
    public float lat, lng;
    List<FriendsModel> friendsModelList;
    List<FriendsModel> friendsModelList1;
    RecyclerView rcv;
    PeopleListRecycler adapter;
    App_Socket app_socket;
    String other_msg;
    String type;


    SupportMapFragment mapFragment;
    TextView latLong;
    android.app.FragmentManager fragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home__map, container, false);
        ;

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        friendsModelList = new ArrayList<FriendsModel>();
        context = view.getContext();
        fragmentManager = ((Activity) context).getFragmentManager();

        portnVariable = new PortnVariable();
        app_socket = new App_Socket();
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token","0");

        app_socket.userid = userid;



        getPeopleapi();

        return view;
    }

//    private Emitter.Listener onNewMessage = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String abc;
//                    String type;
//                    try {
//                        other_msg = data.getString("message");
//                        type = data.getString("type");
//
//                    } catch (JSONException e) {
//                        return;
//                    }
//
//                    if (!other_msg.isEmpty() && type.equals("chat")){
//                        Toast.makeText(context, "Chat Message Received", Toast.LENGTH_SHORT).show();
//
//                    }else if (other_msg.equals("AddedToFavorite") && type.equals("noti")){
//                        Toast.makeText(context, "Added to Favourite", Toast.LENGTH_SHORT).show();
//                    }else if (!other_msg.isEmpty() && type.equals("GroupMessage")){
//                        Toast.makeText(context, "Group Message", Toast.LENGTH_SHORT).show();
//                    }else if (other_msg.equals("GroupRequest") && type.equals("noti")){
//                        Toast.makeText(context, "Group Invite", Toast.LENGTH_SHORT).show();
//                    }else if (other_msg.equals("FriendRequest") && type.equals("noti")){
//                        Toast.makeText(context, "friend request received", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }
//            });
//        }
//    };

    private void getPeopleapi() {

        String latitude = sharedpreferences.getString("latitude", null);
        String longitude = sharedpreferences.getString("longitude", null);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "user/listUsers";

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("PageNo", "1");
            jsonParam.put("Count", "5");
            jsonParam.put("Sorting", "BestMatching");
            jsonParam.put("userID", userid);
//            jsonParam.put("lat", 67.0167);
//            jsonParam.put("lng", 24.8532);
            jsonParam.put("lat", latitude);
            jsonParam.put("lng", longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                String status = null;
//                JSONObject message = null;
                JSONArray message = null;
                String msg = null;

                String auth = null;


                try {

                    if (response.has("auth")) {
                        auth = response.getString("auth");
                        if (auth != null && auth.equals("authFailed")) {
                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();
                           // showdialogAuth("Error", "Token Expired Kindly Login Again", 1);

                            if(!fragmentManager.isDestroyed()) {
                                Intent intent = new Intent(context, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                ((AppCompatActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                startActivity(intent);
                            }

                        }
                    }
                    if (response.has("status")) {
                        status = response.getString("status");

                        if (status.equals("success")) {
                            msg = response.getString("message");
                            if (!msg.equals("No users at the moment!")) {
                            message = response.getJSONArray("message");

                            if (message.length() != 0) {

                                Gson gson = new Gson();
                                Type type = new TypeToken<FriendsModel>() {
                                }.getType();
                                JSONObject result;
                                String id;
                                for (int i = 0; i < message.length(); i++) {
                                    id = ((JSONObject) message.get(i)).getString("_id");
                                    result = ((JSONObject) message.get(i)).getJSONObject("Details").put("_id", id);
                                    friendsModelList.add(gson.fromJson(String.valueOf(result), type));
                                }
                                mapFragment.getMapAsync(Home_Map.this::onMapReady);
                            }
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
             //   System.out.println("XXX Volly Error :" + error);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("authorization","bearer " +token);
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

                        Intent intent = new Intent(getActivity(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ((AppCompatActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        startActivity(intent);

                    }
                })
                ;
        sd.setCanceledOnTouchOutside(false);
        sd.setCancelable(false);
try {
    sd.show();
}catch (Exception ex){}
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

//        app_socket.socket.connect();
//        String aj = app_socket.socket.id();
//        Toast.makeText(context, "ID:"+aj, Toast.LENGTH_SHORT).show();
//        app_socket.socket.on(userid,onNewMessage);
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new MapWindowAdapter(mapFragment.getActivity()));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent profile = new Intent(context, Profile.class);
                profile.putExtra("FrID", marker.getSnippet().split(",")[2]);
                context.startActivity(profile);
            }
        });
        // Add a marker in Sydney and move the camera


        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int cyear = now.get(Calendar.YEAR);
        friendsModelList.get(0);


        float latlngrandm = 0.05f;
        LatLng sydney = null;
        for (FriendsModel friend : friendsModelList) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(friend.DOB);
            int year = calendar.get(Calendar.YEAR);

            int agee = 0;

            List<Double> co_values = friend.getGeometry().getCoordinates();
            if (co_values.isEmpty()) {
                sydney = new LatLng(24.999886 + latlngrandm, 67.064827 + latlngrandm);

            } else {
                sydney = new LatLng(co_values.get(1), co_values.get(0));
            }
            agee = cyear - year;


            // lat long from api to map:


            mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title(friend.name)
                    .snippet(agee + " years old," + friend.Photo1 + "," + friend._id)
                    .icon((BitmapDescriptorFactory.fromResource(R.drawable.markermap))));
//            latlngrandm+=0.05f;
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sydney.latitude, sydney.longitude), 1.0f));
        mMap.setMaxZoomPreference(100);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }
}