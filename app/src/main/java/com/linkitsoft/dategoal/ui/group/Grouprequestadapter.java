package com.linkitsoft.dategoal.ui.group;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
import com.linkitsoft.dategoal.Models.GroupModel;
import com.linkitsoft.dategoal.Models.NotificationModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.Profile.Profile;
import com.linkitsoft.dategoal.ui.friends.cardadapter;
import com.linkitsoft.dategoal.ui.home.NotificationFragment;
import com.linkitsoft.dategoal.ui.messages.ChatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class Grouprequestadapter extends RecyclerView.Adapter<Grouprequestadapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private cardadapter.ItemClickListener mClickListener;
    public List<GroupModel> groupModelList;
    public List<FriendsModel> friendsModelList;
    public List<NotificationModel> notificationModelList;
    SweetAlertDialog pDialog;
    Context context;
    PortnVariable portnVariable;
    public String userid, token;
    Boolean msg = false;
    Boolean status = false;
    int size = 0;
    NotificationFragment notificationFragment;
    GroupsRequest groupsRequest;
    Dialog dialog;

    // data is passed into the constructor
    public Grouprequestadapter(Context context, GroupsRequest groupsRequest, List<GroupModel> groupModelList, String userid, String token) {
        this.mInflater = LayoutInflater.from(context);
        this.groupModelList = groupModelList;
        size = groupModelList.size();
        this.userid = userid;
        this.token = token;
        this.groupsRequest = groupsRequest;
    }

    public Grouprequestadapter(Context context, NotificationFragment notificationFragment, List<NotificationModel> notificationModelList, String userid,
                               Boolean msg, Boolean status, Dialog dialog, String token) {
        this.mInflater = LayoutInflater.from(context);
        this.notificationModelList = notificationModelList;
        this.userid = userid;
        this.token = token;
        this.msg = msg;
        this.status = status;
        this.dialog = dialog;

        size = notificationModelList.size();
        this.notificationFragment = notificationFragment;

    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public Grouprequestadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_group_request, parent, false);
        portnVariable = new PortnVariable();
        context = view.getContext();

        return new Grouprequestadapter.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull Grouprequestadapter.ViewHolder holder, int position) {

        View.OnClickListener oc = null;
        if (status) {

            String username = notificationModelList.get(position).getName();
            String userphoto = notificationModelList.get(position).getProfileImage();
            String senderID = notificationModelList.get(position).getSenderID();
            String sender_msg = notificationModelList.get(position).getMessage();
            String notification_id = notificationModelList.get(position).get_id();
            String view_status = notificationModelList.get(position).getIsViewed();

            holder.nametextview.setText(username);
            Picasso.get().load(userphoto).into(holder.profilepic);
            holder.membertextview.setText(sender_msg);

            holder.v.setVisibility(View.GONE);
            holder.accpt.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);
            holder.ignore.setVisibility(View.GONE);
            holder.info.setVisibility(View.GONE);

            if (sender_msg.equals("Matched")) {
                holder.v.setVisibility(View.VISIBLE);
                holder.accpt.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
                holder.ignore.setVisibility(View.VISIBLE);
                holder.accpt.setImageResource(R.drawable.sayhi);
            } else if (sender_msg.equals("send you request")) {
                holder.accpt.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
                holder.ignore.setVisibility(View.GONE);
                holder.v.setVisibility(View.VISIBLE);

            } else if (sender_msg.equals("sent you group request")) {
                holder.accpt.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
                holder.ignore.setVisibility(View.GONE);
                holder.v.setVisibility(View.VISIBLE);
            }

            if (view_status.equals("1")) {
                holder.con.setBackground(ContextCompat.getDrawable(context, R.drawable.view_noti));
            }

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.con.setBackground(ContextCompat.getDrawable(context, R.drawable.view_noti));


                    if (sender_msg.equals("sent you group request")) {
                        patchreject(senderID, holder.getAdapterPosition(), notification_id);
                    } else if (sender_msg.equals("send you request")) {
                        patchfrreject(senderID, holder.getAdapterPosition(), notification_id);
                    } else if (sender_msg.equals("Matched")) {

                        notificationModelList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        size = notificationModelList.size();
                        notifyItemRangeChanged(holder.getAdapterPosition(), size);

                        viewUpdate1(notification_id);
                        if (notificationModelList.isEmpty()) {
                            notificationFragment.hiderecycler("match");
                        }
                    }
                }
            });

            holder.ignore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.reject.performClick();
//                    notificationModelList.remove(holder.getAdapterPosition());
//                    notifyItemRemoved(holder.getAdapterPosition());
//                    size = notificationModelList.size();
//                    notifyItemRangeChanged(holder.getAdapterPosition(), size);
//
//                    viewUpdate1(notification_id);
//                    if (notificationModelList.isEmpty()) {
//                        notificationFragment.hiderecycler("match");
//                    }

                }
            });

            holder.accpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.con.setBackground(ContextCompat.getDrawable(context, R.drawable.view_noti));

                    if (sender_msg.equals("sent you group request")) {
                        patchaccept(senderID, holder.getAdapterPosition(), notification_id);
                    } else if (sender_msg.equals("send you request")) {
                        patchfraccept(senderID, holder.getAdapterPosition(), notification_id);
                    } else if (sender_msg.equals("Matched")) {

                        notificationModelList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        size = notificationModelList.size();
                        notifyItemRangeChanged(holder.getAdapterPosition(), size);
                        viewUpdate1(notification_id);

                        if (notificationModelList.isEmpty()) {
                            notificationFragment.hiderecycler("match");
                        }

                        Intent chat = new Intent(context, ChatActivity.class);
//                        chat.putExtra("FrID",senderID);
                        chat.putExtra("noti_id", senderID);
//                        chat.putExtra("frndPhoto",userphoto);
//                        chat.putExtra("fnrdName",username);
                        context.startActivity(chat);

                    }
                }
            });


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.con.setBackground(ContextCompat.getDrawable(context, R.drawable.view_noti));

                    if (sender_msg.equals("liked you") || sender_msg.equals("added you to favorites")
                            || sender_msg.equals("accepted your group request") || sender_msg.equals("accepted your friend request")) {
                        viewUpdate1(notification_id);
                        dialog.dismiss();
                    }
                    Intent profile = new Intent(context, Profile.class);
                    profile.putExtra("FrID", senderID);
                    context.startActivity(profile);
                }
            });


        } else {
            holder.accpt.setVisibility(View.VISIBLE);
            holder.reject.setVisibility(View.VISIBLE);
            holder.ignore.setVisibility(View.GONE);

            String name = groupModelList.get(position).getTitle();
            String photo1 = groupModelList.get(position).getProfilePic();
            int memeber = groupModelList.get(position).getMembers();
            String fid = groupModelList.get(position).get_id();

            Picasso.get().load(photo1).into(holder.profilepic);
            holder.nametextview.setText(name);
            holder.membertextview.setText(memeber + " Members");
            holder.info.setVisibility(View.VISIBLE);
            holder.info.setText("Group Request");
            holder.info.setVisibility(View.GONE);
            String noti = "";

            holder.accpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    patchaccept(fid, holder.getAdapterPosition(), noti);
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    patchreject(fid, holder.getAdapterPosition(), noti);

                }
            });

        }


    }

    private void viewUpdate1(String nid) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String uri = portnVariable.com + "user/noti/update";
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("userID", userid);
                jsonParam.put("notificationId", nid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                String message = null;

                try {

                    status = response.getString("status");

                    if (status.equals("failed")) {

                    } else if (status.equals("success")) {
                        message = response.getString("message");


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

    private void patchfraccept(String pid, int holder, String notification_id) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Accepting friends request...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "friend/requestaction/1";

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("requestID", pid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.PUT, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String message = null;

                try {

                    if (response.has("status")) {
                    status = response.getString("status");

                    if (status.equals("success")) {

                        notificationModelList.remove(holder);
                        notifyItemRemoved(holder);
                        size = notificationModelList.size();
                        notifyItemRangeChanged(holder, size);

                        viewUpdate1(notification_id);

                        if (notificationModelList.isEmpty()) {
                            notificationFragment.hiderecycler("frnd");
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
                //    System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();
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

    private void patchfrreject(String pid, int holder, String notification_id) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Rejecting friends requests...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "friend/requestaction/0";

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("requestID", pid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.PUT, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String message = null;

                try {

                    status = response.getString("status");

                    if (status.equals("success")) {

                        notificationModelList.remove(holder);
                        notifyItemRemoved(holder);
                        size = notificationModelList.size();
                        notifyItemRangeChanged(holder, size);

                        viewUpdate1(notification_id);
                        if (notificationModelList.isEmpty()) {
                            notificationFragment.hiderecycler("frnd");
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

    private void patchaccept(String pid, int holder, String notification_id) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Accepting group request...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "groupMember/request/1";

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("groupID", pid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.PUT, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String message = null;

                try {

                    status = response.getString("status");

                    if (status.contains("success") || status.equals("Success")) {


                        if (!notification_id.equals("")) {

                            notificationModelList.remove(holder);
                            notifyItemRemoved(holder);
                            size = notificationModelList.size();
                            notifyItemRangeChanged(holder, size);

                            viewUpdate1(notification_id);
                            if (notificationModelList.isEmpty()) {
                                notificationFragment.hiderecycler("grp");
                            }
                        } else {

                            groupModelList.remove(holder);
                            notifyItemRemoved(holder);
                            size = groupModelList.size();
                            notifyItemRangeChanged(holder, size);
                            if (groupModelList.isEmpty()) {
                                groupsRequest.shownoreq();
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
                //    System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();
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

    private void patchreject(String pid, int holder, String notification_id) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Rejecting group requests...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "groupMember/request/0";

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("groupID", pid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.PUT, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String message = null;

                try {

                    status = response.getString("status");

                    if (status.contains("success") || status.equals("Success")) {


                        if (!notification_id.equals("")) {
                            notificationModelList.remove(holder);
                            notifyItemRemoved(holder);
                            size = notificationModelList.size();
                            notifyItemRangeChanged(holder, size);

                            viewUpdate1(notification_id);
                            if (notificationModelList.isEmpty()) {
                                notificationFragment.hiderecycler("grp");
                            }
                        } else {

                            groupModelList.remove(holder);
                            notifyItemRemoved(holder);
                            size = groupModelList.size();
                            notifyItemRangeChanged(holder, size);
                            if (groupModelList.isEmpty()) {
                                groupsRequest.shownoreq();
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
                //  System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();
              //  showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
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

    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return size;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nametextview;
        TextView membertextview;
        CircleImageView profilepic;
        FloatingActionButton accpt, ignore;
        ImageButton hi;
        ImageButton reject;
        ConstraintLayout con;
        TextView info;
        View v;
        View mView;


        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            nametextview = itemView.findViewById(R.id.textView13);
            membertextview = itemView.findViewById(R.id.textView15);
            profilepic = itemView.findViewById(R.id.profile_image);
            accpt = itemView.findViewById(R.id.floatingActionButton5);
            reject = itemView.findViewById(R.id.imageButton10);
            con = itemView.findViewById(R.id.constraint_layout);
            info = itemView.findViewById(R.id.textView61);
//            hi = itemView.findViewById(R.id.floating_hi);
            ignore = itemView.findViewById(R.id.ignore);
            v = itemView.findViewById(R.id.view9);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    public void setClickListener(cardadapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}