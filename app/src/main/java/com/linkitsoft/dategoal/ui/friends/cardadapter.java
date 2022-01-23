package com.linkitsoft.dategoal.ui.friends;

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
import com.linkitsoft.dategoal.Models.FriendsModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class cardadapter extends RecyclerView.Adapter<cardadapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private cardadapter.ItemClickListener mClickListener;
    public List<FriendsModel> friendsModelList;
    SweetAlertDialog pDialog;
    Context context;
    PortnVariable portnVariable;
    public String userid,token;
    CardStackView cardStackView;
    // data is passed into the constructor
    public cardadapter(Context context, List<FriendsModel> friendsModelList, String userid, CardStackView cardStackView,String token) {
        this.mInflater = LayoutInflater.from(context);
        this.friendsModelList = friendsModelList;
        this.cardStackView = cardStackView;

        this.userid = userid;
        this.token = token;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public cardadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_friendrequest, parent, false);
        context = view.getContext();
        portnVariable = new PortnVariable();

        return new cardadapter.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull cardadapter.ViewHolder holder, int position) {

        String name= friendsModelList.get(position).getName();
        Date age= friendsModelList.get(position).getDOB();
        String address= friendsModelList.get(position).getCity();;
        String imageuri= friendsModelList.get(position).getPhoto1();;
        String pid= friendsModelList.get(position).get_id();;


       // Calendar calendar = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(age);
        int year = calendar.get(Calendar.YEAR);
        //Add one to month {0 - 11}
        /*int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);*/

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int cyear = now.get(Calendar.YEAR);
        int agee = 0;

        agee = cyear-year;

        Picasso.get().load(imageuri).into(holder.profilepic);
        holder.nametextview.setText(name);
        holder.ageaddtextview.setText(agee+" • "+address);

        holder.fabaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patchaccept(pid);
            }
        });
        holder.fabreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patchreject(pid);
            }
        });
    }

    private void patchaccept(String pid)  {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Accepting friends request...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com+"friend/requestaction/1";

        JSONObject jsonParam =new JSONObject();
        try {

            jsonParam.put("userID",userid);
            jsonParam.put("requestID", pid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.PUT, uri,requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String message = null;

                try {

                    status = response.getString("status");
                    if(status.equals("success")) {
                        message = response.getString("message");
                        cardStackView.swipe();
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

                pDialog.dismissWithAnimation();
               // showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
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

    private void patchreject(String pid)  {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitle("Rejecting friends requests...");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com+"friend/requestaction/0";

        JSONObject jsonParam =new JSONObject();
        try {

            jsonParam.put("userID",userid);
            jsonParam.put("requestID", pid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.PUT, uri,requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                pDialog.dismissWithAnimation();

                String status = null;
                String message = null;

                try {

                    status = response.getString("status");
                    if(status.equals("success")) {

                        cardStackView.swipe();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                System.out.println("XXX Volly Error :"+error);

                pDialog.dismissWithAnimation();
               // showdialog("Error","Kindly fix internet connection then try again or Contact customer support",1);
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

    // total number of cells
    @Override
    public int getItemCount() {
        return friendsModelList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nametextview;
        TextView ageaddtextview;
        ImageView profilepic;
        FloatingActionButton fabaccept;
        FloatingActionButton fabreject;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            nametextview = itemView.findViewById(R.id.txtName);
            ageaddtextview = itemView.findViewById(R.id.txtageandadd);
            profilepic = itemView.findViewById(R.id.item_image);
            fabaccept =  itemView.findViewById(R.id.fabaccpet);
            fabreject =  itemView.findViewById(R.id.fabreject);
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