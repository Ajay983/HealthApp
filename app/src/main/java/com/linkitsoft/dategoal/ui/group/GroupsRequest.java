package com.linkitsoft.dategoal.ui.group;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.linkitsoft.dategoal.Models.GroupModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
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
 * Use the {@link GroupsRequest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsRequest extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupsRequest() {
        // Required empty public constructor
    }

    ShimmerFrameLayout mainshimmer;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupsRequest.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupsRequest newInstance(String param1, String param2) {
        GroupsRequest fragment = new GroupsRequest();
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
    RecyclerView recyclerView;
    Context context;
    SharedPreferences sharedpreferences;
    PortnVariable portnVariable;
    public String userid,token;
    List<GroupModel> groupModelList;
    Grouprequestadapter adapter;
    TextView labelnodata;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root  = inflater.inflate(R.layout.fragment_groups_request, container, false);

        recyclerView = root.findViewById(R.id.recycleview);
        groupModelList = new ArrayList<GroupModel>();
        context = root.getContext();
        portnVariable = new PortnVariable();


        mainshimmer = root.findViewById(R.id.mainshimmer);
        labelnodata = root.findViewById(R.id.textView53);

        mainshimmer.showShimmer(true); // If auto-start is set to false
//        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);


        getgroupreqsapi();

        return root;
    }
    private void getgroupreqsapi()  {
        mainshimmer.showShimmer(true);
        mainshimmer.startShimmer();

        userid = sharedpreferences.getString("userid","0");
        token = sharedpreferences.getString("user_token","0");

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com+"group/request/list";

        JSONObject jsonParam =new JSONObject();
        try {

            jsonParam.put("PageNo","1");
            jsonParam.put("Count","1000");
            jsonParam.put("userID",userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri,requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String status = null;
                JSONArray message = null;
                String auth = null;
                groupModelList = new ArrayList<GroupModel>();


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

                    if(status.equals("success")) {

                        message = response.getJSONArray("message");

                        if(message.length() == 0){
                            labelnodata.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            labelnodata.setVisibility(View.GONE);
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<GroupModel>>(){}.getType();
                            groupModelList = gson.fromJson(String.valueOf(message), type);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            adapter = new Grouprequestadapter(context, GroupsRequest.this,groupModelList,userid,token);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);

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
              //  System.out.println("XXX Volly Error :"+error);

                mainshimmer.showShimmer(false);
                mainshimmer.hideShimmer();
                mainshimmer.setVisibility(View.GONE);

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
    public void shownoreq(){
        labelnodata.setVisibility(View.VISIBLE);

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