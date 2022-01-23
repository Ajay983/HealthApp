package com.linkitsoft.dategoal.ui.friends;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.Models.FriendsModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.RecyclerAdpaters.FavoritRecycler;
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
 * Use the {@link Friendsfavorites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Friendsfavorites extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Friendsfavorites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Friendsfavorites.
     */
    // TODO: Rename and change types and number of parameters
    public static Friendsfavorites newInstance(String param1, String param2) {
        Friendsfavorites fragment = new Friendsfavorites();
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

    SwipeRefreshLayout swipeRefreshLayout;

    FavoritRecycler adapter;
    SharedPreferences sharedpreferences;
    PortnVariable portnVariable;
    public String userid,token;
    Context context;
    List<FriendsModel> friendsModelList;
    RecyclerView recyclerView;
    TextView lablenodata;


    ShimmerFrameLayout mainshimmer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_friendsfavorites, container, false);
        context = root.getContext();
        portnVariable = new PortnVariable();
        friendsModelList = new ArrayList<FriendsModel>();
        recyclerView = root.findViewById(R.id.recycleview);
        lablenodata = root.findViewById(R.id.textView51);
        mainshimmer = root.findViewById(R.id.mainshimmer);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        mainshimmer.showShimmer(true);
        // If auto-start is set to false


        friendsModelList.clear();
//        adapter.notifyDataSetChanged();
        getfvrtapi();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                friendsModelList.clear();
                adapter.notifyDataSetChanged();
                getfvrtapi();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        // String[] data = {"Simran Jakranda,https://i.pinimg.com/564x/b8/03/78/b80378993da7282e58b35bdd3adbce89.jpg,0", "Wasif Ali,https://wallpaperaccess.com/full/2213424.jpg,0", "fffk,https://media1.popsugar-assets.com/files/thumbor/DRKaReuIoQvFSthlzcbVnfj9hhQ/fit-in/728xorig/filters:format_auto-!!-:strip_icc-!!-/2020/03/16/237/n/1922398/d2a2fc375e7055358f6460.62104801_/i/post-malone-best-performance-pictures.jpg,0", "Paul Rudd,https://media1.popsugar-assets.com/files/thumbor/DRKaReuIoQvFSthlzcbVnfj9hhQ/fit-in/728xorig/filters:format_auto-!!-:strip_icc-!!-/2020/03/16/237/n/1922398/d2a2fc375e7055358f6460.62104801_/i/post-malone-best-performance-pictures.jpg,0", "Rameez Asghar,https://s3.amazonaws.com/pixpa.com/com/articles/1562328087-653328-rodrigo-soares-8bfwbuksqqo-unsplashjpg.png,1", "New Entry,https://media.istockphoto.com/photos/angry-lion-roaring-picture-id1184240863?k=6&m=1184240863&s=612x612&w=0&h=rUmWeSiYspBwgfsgPBH8-4fyWJvMiylYDA2KIxlWpx0=,1"};
        // set up the RecyclerView
       /* recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), numberOfColumns));
        adapter = new recyclefavs(root.getContext(), data);
        adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getContext(), Profile.class));
            }
        });
        recyclerView.setAdapter(adapter);*/



        return root;
    }








    public void getfvrtapi() {
        mainshimmer.showShimmer(true);
        mainshimmer.startShimmer();

        userid = sharedpreferences.getString("userid", "0");
        token = sharedpreferences.getString("user_token","0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "favorite/listfavorites";
        System.out.println("Token: "+token+" uri: "+uri+" userid: "+userid );

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("PageNo", "1");
            jsonParam.put("Count", "1000");
            jsonParam.put("userID", userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String status = null;
                JSONArray message = null;
                String auth = null;
                friendsModelList = new ArrayList<FriendsModel>();


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
                    if (status.equals("failed")) {

                        //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
                        lablenodata.setVisibility(View.VISIBLE);

                    } else if (status.equals("success")) {

                        message = response.getJSONArray("message");

                        if (message.length() == 0) {

                            //showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
                            //label to show you have no friends
                            lablenodata.setVisibility(View.VISIBLE);

                            adapter = new FavoritRecycler(friendsModelList);
                            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);

                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            lablenodata.setVisibility(View.GONE);

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

                            adapter = new FavoritRecycler(friendsModelList);
                            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);


                        }
                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();
                mainshimmer.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                System.out.println("XXX Volly Error :" + error);
                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();
                mainshimmer.setVisibility(View.GONE);
               // showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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

    @Override
    public void onStop() {
        super.onStop();
        recyclerView.setVisibility(View.INVISIBLE);
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