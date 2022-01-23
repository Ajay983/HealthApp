package com.linkitsoft.dategoal.RecyclerAdpaters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.linkitsoft.dategoal.Models.GroupModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.group.Groupchat;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class DeleteGroupRecycler extends RecyclerView.Adapter<DeleteGroupRecycler.ViewHolder> {

    public List<GroupModel> groupModelList;
    public Context context;
    public String userid;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    int size = 0;
    public String token;

    public DeleteGroupRecycler(List<GroupModel> groupModelList,String userid,String token){
        this.groupModelList = groupModelList;
        this.userid = userid;
        this.token = token;
        size = groupModelList.size();
    }

    @NonNull
    @Override
    public DeleteGroupRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delete_group, parent, false);
        portnVariable = new PortnVariable();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String Grpname = groupModelList.get(position).getTitle();
        String Grpphoto1 = groupModelList.get(position).getProfilePic();
        String GroupId = groupModelList.get(position).get_id();
        String admin_id = groupModelList.get(position).getAdminId();
        int GroupMemeber = groupModelList.get(position).getMembers();

        holder.group_members.setText("Group Members: "+GroupMemeber);

        if (admin_id.equalsIgnoreCase(userid)){
            holder.group_admin.setVisibility(View.VISIBLE);
            holder.delete_group.setVisibility(View.GONE);

        }

        holder.setdata(Grpname,Grpphoto1);

        holder.delete_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteGroup(userid,holder.getAdapterPosition(),GroupId);
            }
        });

        holder.grp_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent group_chat = new Intent(context,Groupchat.class);
                group_chat.putExtra("GrID",GroupId);
                group_chat.putExtra("GrName",Grpname);
                group_chat.putExtra("GrImg",Grpphoto1);
                group_chat.putExtra("adminId",admin_id);
                context.startActivity(group_chat);
            }
        });


    }

    private void deleteGroup(String userid, int holder, String group_id) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Leaving Group...");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String uri = portnVariable.com + "groupMember/removegroup";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
            jsonParam.put("groupID", group_id);

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
                String auth = null;

                try {


                    if (response.has("status")) {
                        status = response.getString("status");

                        if (status.equals("success")) {

                            groupModelList.remove(holder);
                            notifyItemRemoved(holder);
                            size = groupModelList.size();
                            notifyItemRangeChanged(holder, size);
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
                //    System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();
              //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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
    public int getItemCount() {
        return groupModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        CircleImageView grp_img;
        TextView grp_name;
        ImageButton delete_group;
        TextView group_members,group_admin;



        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;
            grp_img = itemView.findViewById(R.id.profile_image);
            grp_name = itemView.findViewById(R.id.textView13);
            delete_group = itemView.findViewById(R.id.imageButton10);
            group_admin = itemView.findViewById(R.id.textView16);
            group_members = itemView.findViewById(R.id.textView15);




        }

        public void setdata(String name, String photo1) {
            Picasso.get().load(photo1).into(grp_img);
            grp_name.setText(name);


        }
    }

    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }

}

