package com.linkitsoft.dategoal.RecyclerAdpaters;

import android.content.Context;
import android.content.Intent;
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
import com.linkitsoft.dategoal.Models.FriendsModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.Profile.Profile;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class FreindsRecycler extends RecyclerView.Adapter<FreindsRecycler.ViewHolder> {

    public List<FriendsModel> friendsModelList;
    public Context context;
    Boolean grpadd;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    public String grpid,token;


    public FreindsRecycler(List<FriendsModel> friendsModelList, Boolean grpadd , String grpid,String token){
        this.friendsModelList = friendsModelList;
        this.grpadd = grpadd;
        this.grpid = grpid;
        this.token = token;
    }
    int layout=0;
    public FreindsRecycler(List<FriendsModel> friendsModelList, Boolean grpadd , String grpid,int layout){
        this.friendsModelList = friendsModelList;
        this.grpadd = grpadd;
        this.layout = layout;
        this.grpid = grpid;
    }

    @NonNull
    @Override
    public FreindsRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view= layout==0?LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friendlist, parent, false):LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        portnVariable = new PortnVariable();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreindsRecycler.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String name = friendsModelList.get(position).getName();
        String photo1 = friendsModelList.get(position).getPhoto1();
        String fid = friendsModelList.get(position).get_id();
        int is_request = friendsModelList.get(position).getIsRequested();
        int onlineStatus = friendsModelList.get(position).getOnlineStatus();


//        if (grpadd && is_request == 1){
//            holder.imgPending.setVisibility(View.VISIBLE);
//        }

        holder.setdata(name,photo1,onlineStatus);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grpadd && is_request == 0){

                    sendgrpreq(fid);
                }
                else if (grpadd && is_request == 1){
                    showdialog("Pending","Request Already Send",1);
                }

                else
                {
                Intent profile = new Intent(context, Profile.class);
                profile.putExtra("FrID", fid);
                context.startActivity(profile);}
            }
        });


    }

    private void sendgrpreq(String frid)  {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Sending group request...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com+"groupMember";

        JSONObject jsonParam =new JSONObject();
        try {

            jsonParam.put("userID",frid);
            jsonParam.put("groupID",grpid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri,requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String message = null;

                try {
                    if(response.has("status")) {
                        status = response.getString("status");

                        if (status.equals("already sent")) {

                            message = response.getString("message");

                            if (message.equals("Already Friends!")) {

                                showdialog("Exist", "Already a friend", 3);
                            } else if (message.equals("request already sent")) {

                                showdialog("Pending", "Request is already in pending", 3);
                            }
                        } else if (status.equals("success")) {

                            message = response.getString("message");

                            if (message.equals("Request Sent Sucessfully")) {

                                showdialog("Success", "Successfully sent group request", 2);
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
                //   System.out.println("XXX Volly Error :"+error);

                pDialog.dismissWithAnimation();
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
        ImageView imgPending;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            myTextView = itemView.findViewById(R.id.textView13);
            online = itemView.findViewById(R.id.onlinesymbol);
            circleImageView = itemView.findViewById(R.id.profile_image);
            imgPending = itemView.findViewById(R.id.imageView9);


        }

        public void setdata(String name, String photo1, int onlineStatus) {

            Picasso.get().load(photo1).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(circleImageView);
            myTextView.setText(name);
            online.setImageResource(onlineStatus==0?R.color.teal_200:R.color.gray);

        }
    }
}
