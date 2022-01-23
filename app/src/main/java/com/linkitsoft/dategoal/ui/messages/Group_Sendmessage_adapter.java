package com.linkitsoft.dategoal.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkitsoft.dategoal.Models.GrupMessageModel;
import com.linkitsoft.dategoal.MyRecyclerViewAdapter;
import com.linkitsoft.dategoal.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Group_Sendmessage_adapter extends RecyclerView.Adapter<Group_Sendmessage_adapter.ViewHolder> {
    private LayoutInflater mInflater;
    private String data;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    public List<GrupMessageModel> userModelList;
    public String userid;
    String time_split;
    String am_pm, final_time;

    public Group_Sendmessage_adapter(Context context, List<GrupMessageModel> userModelList, String userid) {
        this.mInflater = LayoutInflater.from(context);
        this.userid = userid;
        this.userModelList = userModelList;
    }


    @NonNull
    @Override
    public Group_Sendmessage_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_chat, parent, false);
        return new Group_Sendmessage_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Group_Sendmessage_adapter.ViewHolder holder, int position) {
        String message = userModelList.get(position).getMessage();
        String other_user_idChat = userModelList.get(position).getUserID();
        String sender_name = userModelList.get(position).getName();
        String time_send = userModelList.get(position).getCreatedAt();

        holder.rec_msg.setVisibility(View.GONE);
        holder.my_msg.setVisibility(View.GONE);
        holder.sendTime.setVisibility(View.GONE);
        holder.recvTime.setVisibility(View.GONE);
        holder.tick.setVisibility(View.GONE);
        holder.sendName.setVisibility(View.GONE);
        holder.recvName.setVisibility(View.GONE);


//        try {
//             time_split = time_send.substring(11,16);
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            Date date = sdf.parse(time_split);
//            time_split = new SimpleDateFormat("hh:mm aa").format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        //get DB hours and minutes
        int time_split_hours = Integer.parseInt(time_send.substring(11, 13));
        String time_split_minutes = time_send.substring(14, 16);


        //get my time my day and splitting day time

        String myTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String myDay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        int split_myTime = Integer.parseInt(myTime.substring(0, 2));
        int split_myDay = Integer.parseInt(myDay.substring(0, 2));


        //get mongo db standard time and splitting day time


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        String localTime = date.format(currentLocalTime);
        int gmt_split = Integer.parseInt(localTime.substring(11, 13));
        int gmt_day_split = Integer.parseInt(localTime.substring(0, 2));


        //get difference of my time and standard gmt time

        int difference = split_myTime - gmt_split;

        if (split_myDay > gmt_day_split) {
            difference = 24 - gmt_split + split_myTime;

        }


        int hours_inc = time_split_hours + difference;
        am_pm = "am";
        final_time = String.valueOf(hours_inc);
        if (hours_inc == 12) {
            am_pm = "pm";
        } else if (hours_inc > 12) {
            hours_inc = hours_inc - 12;
            final_time = String.valueOf(hours_inc);
            am_pm = "pm";
        }


        if (userid.equals(other_user_idChat)) {
            holder.rec_msg.setVisibility(View.VISIBLE);
            holder.rec_msg.setText(message);
            holder.rec_msg.setGravity(Gravity.END);
            holder.sendTime.setVisibility(View.VISIBLE);
            holder.recvTime.setVisibility(View.GONE);
            holder.sendTime.setText(final_time + ":" + time_split_minutes + " " + am_pm);
            holder.tick.setVisibility(View.VISIBLE);
        } else {
            holder.my_msg.setVisibility(View.VISIBLE);
            holder.my_msg.setText(message);
            holder.my_msg.setGravity(Gravity.END);
            holder.recvName.setText(sender_name);
            holder.recvName.setVisibility(View.VISIBLE);
            holder.recvTime.setVisibility(View.VISIBLE);
            holder.sendTime.setVisibility(View.GONE);
            holder.recvTime.setText(final_time + ":" + time_split_minutes + " " + am_pm);

        }


    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView my_msg;
        TextView rec_msg;
        TextView sendName, recvName;
        TextView sendTime, recvTime;
        ImageView tick;

        ViewHolder(View itemView) {
            super(itemView);
            my_msg = itemView.findViewById(R.id.textView19);
            rec_msg = itemView.findViewById(R.id.textView17);
            sendName = itemView.findViewById(R.id.send_name);
            recvName = itemView.findViewById(R.id.recv_name);
            sendTime = itemView.findViewById(R.id.send_time);
            recvTime = itemView.findViewById(R.id.recv_time);
            tick = itemView.findViewById(R.id.tick);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            view.getContext().startActivity(new Intent(view.getContext(), ChatActivity.class));
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(MyRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
