package com.linkitsoft.dategoal.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.FriendsModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.RecyclerAdpaters.PeopleListRecycler;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_List extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_List newInstance(String param1, String param2) {
        Home_List fragment = new Home_List();
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
    List<FriendsModel> friendsModelList;
    RecyclerView rcv;
    PeopleListRecycler adapter;
    Button btn;
    public float lat, lng;

    ShimmerFrameLayout mainshimmer;
    android.app.FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View contentView = inflater.inflate(R.layout.fragment_home__list, container, false);

        friendsModelList = new ArrayList<FriendsModel>();
        context = contentView.getContext();
        fragmentManager = ((Activity) context).getFragmentManager();

        portnVariable = new PortnVariable();
        rcv = contentView.findViewById(R.id.recyclernearby);
        mainshimmer = contentView.findViewById(R.id.mainshimmer);
        mainshimmer.showShimmer(true);
//        lng = sharedpreferences.getString("lng",null);
//        lat = sharedpreferences.getString("lat",null);
        getPeopleapi("BestMatching");

        btn = contentView.findViewById(R.id.button15);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NearbyFilterbottomsheet bottomSheetDialog = NearbyFilterbottomsheet.newInstance(Home_List.this, ((Button) view).getText().toString());
                bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
            }
        });

        return contentView;

    }


    public void setfrombottomsheet(String selected) {
        getPeopleapi(selected);
        if (selected.equals("BestMatching")){
            btn.setText("Best Matching");
        }else if (selected.equals("NearBy")){
            btn.setText("Near By");
        }else {
            btn.setText("Just Joined");

        }

    }


    private void getPeopleapi(String sorta) {

        mainshimmer.startShimmer();
        mainshimmer.showShimmer(true);
        String latitude = sharedpreferences.getString("latitude", null);
        String longitude = sharedpreferences.getString("longitude", null);

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token","0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "user/listUsers";

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("PageNo", "1");
            jsonParam.put("Count", "1000");
            jsonParam.put("Sorting",sorta);
            jsonParam.put("userID", userid);
//            jsonParam.put("lat", 67.0167);
//            jsonParam.put("lng", 24.8532);
            jsonParam.put("lat",latitude);
            jsonParam.put("lng",longitude);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        friendsModelList = new ArrayList<FriendsModel>();
        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //   pDialog.dismissWithAnimation();

                String status = null;
                JSONArray message = null;
                String msg = null;

                String auth = null;

                try {

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

                                adapter = new PeopleListRecycler(friendsModelList);
                                rcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                rcv.setAdapter(adapter);
                                rcv.setHasFixedSize(true);


                                mainshimmer.showShimmer(false);
                                mainshimmer.hideShimmer();


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

                            // showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
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
                //  showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
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

        sd.setCancelable(false);

        sd.show();
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }


}