package com.linkitsoft.dategoal.ui.group;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Models.GroupMembersModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMembersRecycler extends RecyclerView.Adapter<GroupMembersRecycler.ViewHolder> {

    public List<GroupMembersModel> friendsModelList;
    public Context context;
    PortnVariable portnVariable;
    public String grpid;
    public String token;
    SweetAlertDialog pDialog;
    public String userid;
    int size = 0;
    public Boolean isAdmin;
    public Dialog dialog;


    public GroupMembersRecycler(List<GroupMembersModel> friendsModelList , String grpid, String userid, Boolean isAdmin, String token, Dialog dialog){
        this.friendsModelList = friendsModelList;
        this.grpid = grpid;
        this.userid = userid;
        this.isAdmin = isAdmin;
        this.token = token;
        this.dialog = dialog;
        size = friendsModelList.size();
    }
    int layout=0;
    public GroupMembersRecycler(List<GroupMembersModel> friendsModelList,String grpid,int layout){
        this.friendsModelList = friendsModelList;
        this.layout = layout;
        this.grpid = grpid;
    }


    @NonNull
    @Override
    public GroupMembersRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view= layout==0? LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groupmembers_list, parent, false)
                :LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        portnVariable = new PortnVariable();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupMembersRecycler.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        String name = friendsModelList.get(position).getName();
        String photo1 = friendsModelList.get(position).getPhoto1();
        String role = friendsModelList.get(position).getRole();
        String id = friendsModelList.get(position).getId();

        if (role.equals("admin" )){
            holder.img_admin.setVisibility(View.VISIBLE);
        }
        else{
            if (isAdmin){
                holder.remove_member.setVisibility(View.VISIBLE);
                holder.remove_member.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeMember(id,holder.getAdapterPosition(),grpid);
                    }
                });
            }
        }

        holder.setdata(name,photo1);


//        holder.setdata(name,photo1);
//
//
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(grpadd){
//
//                    sendgrpreq(fid);
//                }
//
//                else
//                {
//                    Intent profile = new Intent(context, Profile.class);
//                    profile.putExtra("FrID", fid);
//                    context.startActivity(profile);}
//            }
//        });


    }

    private void removeMember(String userid, int holder,String groupid) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Removing User...");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String uri = portnVariable.com + "groupMember/removegroup";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            jsonParam.put("groupID", groupid);

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

                try {
                    if(response.has("status")) {
                        status = response.getString("status");
                        if (status.equals("success")) {
                            showdialog("Success", "User Removed From Group", 2);
                            dialog.dismiss();
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
              //  System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();
                //showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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

//    private void sendgrpreq(String frid)  {
//
//        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.setTitle("Sending group request...");
//        pDialog.show();
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//        String uri = portnVariable.com+"groupMember";
//
//        JSONObject jsonParam =new JSONObject();
//        try {
//
//            jsonParam.put("userID",frid);
//            jsonParam.put("groupID",grpid);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        final JSONObject requestBody = jsonParam;
//
//
//        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri,requestBody, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                pDialog.dismissWithAnimation();
//
//                String status = null;
//                String message = null;
//
//                try {
//                    status = response.getString("status");
//
//                    if(status.equals("failed")){
//
//                        showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
//                    }
//                    else if(status.equals("already sent")){
//
//                        message = response.getString("message");
//
//                        if(message.equals("Already Friends!")){
//
//                            showdialog("Exist","Already a friend",3);
//                        }
//                        else if(message.equals("request already sent")){
//
//                            showdialog("Pending","Request is already in pending",3);
//                        }
//                    }
//                    else if(status.equals("success")) {
//
//                        message = response.getString("message");
//
//                        if(message.equals("Request Sent Sucessfully")){
//
//                            showdialog("Success","Successfully sent group request",2);
//                        }
//
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error report
//                System.out.println("XXX Volly Error :"+error);
//
//                pDialog.dismissWithAnimation();
//                showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
//            }
//        }
//        );
//        requestQueue.add(myReq);
//    }

    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }
    @Override
    public int getItemCount() {
        return friendsModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView myTextView;
        CircleImageView online;
        CircleImageView circleImageView;
        ImageView img_admin;
        ImageView remove_member;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            myTextView = itemView.findViewById(R.id.textView13);
            online = itemView.findViewById(R.id.onlinesymbol);
            online.setVisibility(View.GONE);
            circleImageView = itemView.findViewById(R.id.profile_image);
            img_admin = itemView.findViewById(R.id.imageView8);
            remove_member = itemView.findViewById(R.id.imageView10);

        }

        public void setdata(String name, String photo1) {

            Picasso.get().load(photo1).into(circleImageView);
            myTextView.setText(name);


        }
    }
}
