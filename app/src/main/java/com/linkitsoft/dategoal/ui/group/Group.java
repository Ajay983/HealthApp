package com.linkitsoft.dategoal.ui.group;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Group extends Fragment {

    private GroupViewModel mViewModel;
    //init custom tab
    ColorStateList def;
    TextView item1;
    TextView item2;

    TextView select;
    ViewPager viewPager;
    PortnVariable portnVariable;
    Context context;
    String userid,token;
    ImageView badge;
    SharedPreferences sharedpreferences;
    public void initcustomtab(View root)
    {
        item1 = root.findViewById(R.id.item1);
        item2 = root.findViewById(R.id.item2);
        item1.setOnClickListener(myoc);
        item2.setOnClickListener(myoc);
        select = root.findViewById(R.id.select);
        def = item2.getTextColors();
    }

    View.OnClickListener myoc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.item1){
                select.animate().x(0).setDuration(100);
                item1.setTextColor(getResources().getColor(R.color.teal_200,getContext().getTheme()));
                item2.setTextColor(def);
                viewPager.setCurrentItem(0);
            } else if (view.getId() == R.id.item2){
                item1.setTextColor(def);
                item2.setTextColor(getResources().getColor(R.color.teal_200,getContext().getTheme()));
                int size = item2.getWidth();
                select.animate().x(size).setDuration(100);
                viewPager.setCurrentItem(1);
            }
        }
    };
    public static Group newInstance() {
        return new Group();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_group, container, false);

        viewPager=(ViewPager)root.findViewById(R.id.viewPagerGroup);
        context = root.getContext();
        portnVariable = new PortnVariable();
        badge = root.findViewById(R.id.imageView4);
        final groupsadapter adapter = new groupsadapter(getContext(),getChildFragmentManager(),2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    select.animate().x(0).setDuration(100);
                    item1.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
                    item2.setTextColor(def);
                    //viewPager.setCurrentItem(0);
                } else if (position==1){
                    item1.setTextColor(def);
                    item2.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
                    int size = item2.getWidth();
                    select.animate().x(size).setDuration(100);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initcustomtab(root);
        FloatingActionButton fabfilter = root.findViewById(R.id.floatingActionButton3);
        fabfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment bottomSheetDialog = NotificationFragment.newInstance();
                bottomSheetDialog.show(getParentFragmentManager(), "Notifications");
            }
        });
        getNotificationsStatus();



        return root;
    }
    private void getNotificationsStatus(){
        userid = sharedpreferences.getString("userid","0");
        token = sharedpreferences.getString("user_token","0");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = portnVariable.com + "user/Notification/UnRead";
        JSONObject jsonParam =new JSONObject();
        try {
            jsonParam.put("userID",userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, url,requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                String message = null;
                String auth = null;

                try {
                    status = response.getString("status");

                    if(status.equals("success")){
                        message = response.getString("message");
                        if (message.equals("No new notifications")){
                            badge.setVisibility(View.GONE);

                        }else if (message.equals("new notifications")){
                            badge.setVisibility(View.VISIBLE);

                        }
                        else if (auth != null && auth.equals("authFailed")) {
                            sharedpreferences.edit().remove("userid").commit();
                            sharedpreferences.edit().remove("user_token").commit();
                            showdialogAuth("Error", "Token Expired Kindly Login Again", 1);
                        }


                    }
                    else if(status.equals("success")) {


                    }
                    else if (status.equals("xxx")){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
              //  System.out.println("XXX Volly Error :"+error);
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
                        Intent i = new Intent(context, Login.class);
                        startActivity(i);
                    }
                })
                ;

        sd.setCancelable(false);

        sd.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }

}