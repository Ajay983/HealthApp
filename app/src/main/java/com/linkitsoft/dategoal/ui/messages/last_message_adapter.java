package com.linkitsoft.dategoal.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkitsoft.dategoal.Models.ChatListModel;
import com.linkitsoft.dategoal.MyRecyclerViewAdapter;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class last_message_adapter extends RecyclerView.Adapter<last_message_adapter.ViewHolder> {
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    public List<ChatListModel> userModelList;
    public String userid;
    String time_split;
    PortnVariable portnVariable;
    int layout = 0;
    public Context context;
    String final_time;
    String am_pm;

    public last_message_adapter(Context context, List<ChatListModel> userModelList,String userid) {
        this.mInflater = LayoutInflater.from(context);
        this.userid = userid;
        this.userModelList = userModelList;
    }



    @NonNull
    @Override
    public last_message_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view;
//        view = mInflater.inflate(R.layout.item_last_msgs, parent, false);
//        context = parent.getContext();
//        return new last_message_adapter.ViewHolder(view);
        View view;
        view = layout == 0 ? LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_last_msgs, parent, false) : LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        portnVariable = new PortnVariable();

        return new last_message_adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull last_message_adapter.ViewHolder holder, int position) {
//        String message = userModelList.get(position).getMessage();
//        String other_user_idChat = userModelList.get(position).getUserID();
//        String sender_name = userModelList.get(position).getName();
//        String time_send = userModelList.get(position).getCreatedAt();


//        try {
//            time_split = time_send.substring(11,16);
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            Date date = sdf.parse(time_split);
//            time_split = new SimpleDateFormat("hh:mm aa").format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        if (userid.equals(other_user_idChat)){
//            holder.rec_msg.setVisibility(View.VISIBLE);
//            holder.rec_msg.setText(message);
//            holder.rec_msg.setGravity(Gravity.END);
//            holder.sendName.setText(sender_name);
//            holder.sendName.setVisibility(View.VISIBLE);
//            holder.sendTime.setVisibility(View.VISIBLE);
//            holder.recvTime.setVisibility(View.GONE);
//            holder.sendTime.setText(time_split);
//        }else {
//            holder.my_msg.setVisibility(View.VISIBLE);
//            holder.my_msg.setText(message);
//            holder.my_msg.setGravity(Gravity.END);
//            holder.recvName.setText(sender_name);
//            holder.recvName.setVisibility(View.VISIBLE);
//            holder.recvTime.setVisibility(View.VISIBLE);
//            holder.sendTime.setVisibility(View.GONE);
//            holder.recvTime.setText(time_split);
//
//        }
        String lastMsg = userModelList.get(position).getMessage();
        String lastTime = userModelList.get(position).getCreatedAt();
        String lastName = userModelList.get(position).getName();
        String lastPic = userModelList.get(position).getProfilePic();
        String sender_id = userModelList.get(position).getSenderId();
        String receiver_id = userModelList.get(position).getReciverId();
        int view_status = userModelList.get(position).getIsViewed();


        if (view_status == 0 && !sender_id.equals(userid)){

            holder.img_indicator.setVisibility(View.VISIBLE);

        }else {
            holder.img_indicator.setVisibility(View.GONE);
        }


        int time_split_hours = Integer.parseInt(lastTime.substring(11, 13));
        String time_split_minutes = lastTime.substring(14, 16);

        String myTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        int split_myTime = Integer.parseInt(myTime.substring(0, 2));


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        String localTime = date.format(currentLocalTime);
        int gmt_split = Integer.parseInt(localTime.substring(11, 13));
        int difference = split_myTime - gmt_split;



        int hours_inc = time_split_hours + difference;
        am_pm = "am";
        final_time = String.valueOf(hours_inc);
        if(hours_inc == 12){
            am_pm = "pm";
        }
        else if (hours_inc > 12) {
            hours_inc = hours_inc - 12;
            final_time = String.valueOf(hours_inc);
            am_pm = "pm";
        }

        time_split = final_time+":"+time_split_minutes+ " " +am_pm;



//        try {
//             time_split = lastTime.substring(11,16);
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            Date date = sdf.parse(time_split);
//            time_split = new SimpleDateFormat("hh:mm aa").format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        holder.setdata(lastName, lastPic,lastMsg,time_split);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chat = new Intent(context, ChatActivity.class);
                chat.putExtra("FrID", receiver_id);
                chat.putExtra("myID",sender_id);
                chat.putExtra("fnrdName",lastName);
                chat.putExtra("frndPhoto",lastPic);
                context.startActivity(chat);
            }
        });





    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView last_name,last_msg,last_time;
        CircleImageView circleImageView;
        View mView;
        ImageView img_indicator;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            last_name = itemView.findViewById(R.id.textView13);
            last_msg = itemView.findViewById(R.id.textView15);
            last_time = itemView.findViewById(R.id.imageButton10);
            circleImageView = itemView.findViewById(R.id.profile_image);
            img_indicator = itemView.findViewById(R.id.unread_message);
            itemView.setOnClickListener(this);
        }

        public void setdata(String name, String photo1, String message,String time ) {
            last_name.setText(name);
            Picasso.get().load(photo1).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(circleImageView);
            last_msg.setText(message);
            last_time.setText(time);


        }

        @Override
        public void onClick(View view) {
//            view.getContext().startActivity(new Intent(view.getContext(), ChatActivity.class));
//            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
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

