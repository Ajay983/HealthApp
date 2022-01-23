package com.linkitsoft.dategoal.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.linkitsoft.dategoal.Models.MessageModel;
import com.linkitsoft.dategoal.MyRecyclerViewAdapter;
import com.linkitsoft.dategoal.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class chatadapter extends RecyclerView.Adapter<chatadapter.ViewHolder> {

    private String[] mData;


    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    public List<MessageModel> userModelList;
    public String userid;
    public String d;
    String final_time;
    String am_pm;
    String time_split;
    int difference;
    int counter = 0;
    Context context;

    static String st_date;

    // data is passed into the constructor
    public chatadapter(Context context, List<MessageModel> userModelList, String userid,String d) {
        this.mInflater = LayoutInflater.from(context);
        this.userid = userid;
        this.d = d;
        this.userModelList = userModelList;
    }
//    private class sentMessageHolder extends RecyclerView.ViewHolder{
//        TextView messageText;
//
//        public sentMessageHolder(@NonNull View itemView) {
//            super(itemView);
//            messageText = itemView.findViewById(R.id.sentTxt);
//        }
//    }
//    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
//
//        TextView nameTxt, messageTxt;
//
//        public ReceivedMessageHolder(@NonNull View itemView) {
//            super(itemView);
//
//
//            messageTxt = itemView.findViewById(R.id.receivedTxt);
//        }
//    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public chatadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = mInflater.inflate(R.layout.item_chat, parent, false);
        return new chatadapter.ViewHolder(view);


    }

    // binds the data to the TextView in each cell
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull chatadapter.ViewHolder holder, int position) {


        String message = userModelList.get(position).getMessage();
        String other_user_idChat = userModelList.get(position).getUserID();
        String time_send = userModelList.get(position).getCreatedAt();
        String is_view = userModelList.get(position).getIsViewed();

        int jik = userModelList.size() - 1;

//        index_date = userModelList.get(0).createdAt;
//        String index_date_split = d.substring(0, 10);
        String database_date = time_send.substring(0, 10);






        time_split = (time_send.substring(11, 16));





        holder.rec_msg.setVisibility(View.GONE);
        holder.my_msg.setVisibility(View.GONE);
        holder.sendTime.setVisibility(View.GONE);
        holder.recvTime.setVisibility(View.GONE);
        holder.msg_date.setVisibility(View.GONE);
        holder.tick.setVisibility(View.GONE);

//        holder.tick.setVisibility(View.GONE);






        //get DB hours and minutes
        int time_split_hours = Integer.parseInt(time_send.substring(11, 13));
        String time_split_minutes = time_send.substring(14, 16);


        //get my time my day and splitting day time

        String myTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String myDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        int split_myTime = Integer.parseInt(myTime.substring(0, 2));
        int split_myDay = Integer.parseInt(myDay.substring(8, 10));


//        Date Work

//        if (myDay.equals(database_date)){
//            holder.msg_date.setVisibility(View.VISIBLE);
//            holder.msg_date.setText("Today");
//        }
//
//
//        else {
//            holder.msg_date.setVisibility(View.VISIBLE);
//            holder.msg_date.setText(database_date);
//        }


        //get mongo db standard time and splitting  gmt day time


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
        date.setTimeZone(TimeZone.getTimeZone("GMT"));
        String localTime = date.format(currentLocalTime);
        int gmt_split = Integer.parseInt(localTime.substring(11, 13));
        int gmt_day_split = Integer.parseInt(localTime.substring(0, 2));


        //get difference of my time and standard gmt time

        if (split_myDay > gmt_day_split) {
            difference = 24 - gmt_split + split_myTime;

        } else {
            difference = split_myTime - gmt_split;

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

        Date c = Calendar.getInstance().getTime();
        //  System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);


//        try {
//            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
//            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
//            Date date = sdf2.parse(time_split);
//            String current_time_split = new SimpleDateFormat("hh:mm aa").format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String split_currentTime = currentTime.substring(0, 5);
//        String get_ct = split_currentTime.substring(0,2);


//        int get_ct_integer = Integer.parseInt(get_ct);
//        int get_mt_integer = Integer.parseInt(get_mt);
//// The parsed date
        final ZonedDateTime parsed = ZonedDateTime.parse(time_send);
//
//// The output format(s). Specify the one you need
        final DateTimeFormatter outputFormat2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String DB_date = String.valueOf(outputFormat2);
//        final DateTimeFormatter outputFormat3 = DateTimeFormatter.ofPattern("hh:mm a");
//
//// Print
//        // -> 2014/10/19
        //  System.out.println(outputFormat2.format(parsed)); // -> 2014-10-19
//        System.out.println(outputFormat3.format(parsed)); // -> 2014-10-19

//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            date1 = sdf.parse(time_split);
//            time_split = new SimpleDateFormat("hh:mm aa").format(date1);
//
//            time_split_current = new SimpleDateFormat("hh:mm aa").format(currentTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        int diff = currentTime.getHours() - date.getHours();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.HOUR, diff);
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//        Date resultdate = new Date(calendar.getTimeInMillis());
//        String dateInString = sdf.format(resultdate.getHours());





        if (userid.equals(other_user_idChat)) {
            holder.rec_msg.setVisibility(View.VISIBLE);
            holder.rec_msg.setText(message);
            holder.rec_msg.setGravity(Gravity.END);
            holder.sendTime.setVisibility(View.VISIBLE);
            holder.sendTime.setText(final_time + ":" + time_split_minutes + " " + am_pm);
            holder.tick.setVisibility(View.VISIBLE);
        } else {
            holder.my_msg.setVisibility(View.VISIBLE);
            holder.my_msg.setText(message);
            holder.my_msg.setGravity(Gravity.END);
            holder.recvTime.setVisibility(View.VISIBLE);
            holder.recvTime.setText(final_time + ":" + time_split_minutes + " " + am_pm);


        }


//        if (!d.equals(database_date)) {
//            System.out.println("hello how r u "+d);
//            holder.msg_date.setVisibility(View.VISIBLE);
//            holder.msg_date.setText(d);
//            d = database_date;
//        }
//
//        else if (jik == counter){
//            String dd = userModelList.get(0).createdAt;
//            System.out.println("bhaag" + dd);
//
//            holder.msg_date.setVisibility(View.VISIBLE);
//            holder.msg_date.setText(d);
//            d = dd.substring(0,10);
//        }


//
//
//
//        counter ++;


//        String tiem = mData[position].split(",")[1];
//        Boolean readstatus = mData[position].split(",")[2].equals("1");
//        Boolean incoming = mData[position].split(",")[3].equals("1");


//        if (incoming) {
//            holder.omessagetext.setVisibility(View.GONE);
//            holder.otimeago.setVisibility(View.GONE);
//            holder.greentick.setVisibility(View.GONE);


//        if (userid != other_user_idChat){
//            holder.rec_msg.setVisibility(View.VISIBLE);
//            holder.rec_msg.setText(message);
//            holder.rec_msg.setGravity(Gravity.RIGHT);
//
//        }else if (userid == other_user_idChat){
//            holder.my_msg.setVisibility(View.VISIBLE);
//            holder.my_msg.setText(message);
//            holder.rec_msg.setGravity(Gravity.LEFT);
//
//        }
//            holder.itimeago.setText(tiem);
//
//        } else {
//            holder.imessagetext.setVisibility(View.GONE);
//            holder.itimeago.setVisibility(View.GONE);
//            holder.greentick.setVisibility(readstatus ? View.VISIBLE : View.GONE);
//            holder.omessagetext.setText(message);
//            holder.otimeago.setText(tiem);
//        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return userModelList.size();

    }

    public void setClickListener(MessagesFriendListAdapter.ItemClickListener itemClickListener) {

    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView my_msg;
        TextView rec_msg;
        TextView sendTime, recvTime;
        ImageView tick;
        TextView msg_date;

        ViewHolder(View itemView) {
            super(itemView);
            my_msg = itemView.findViewById(R.id.textView19);
            rec_msg = itemView.findViewById(R.id.textView17);
            sendTime = itemView.findViewById(R.id.send_time);
            recvTime = itemView.findViewById(R.id.recv_time);
            tick = itemView.findViewById(R.id.tick);
            msg_date = itemView.findViewById(R.id.msg_date);
            msg_date.setVisibility(View.GONE);




            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            view.getContext().startActivity(new Intent(view.getContext(), ChatActivity.class));
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    public void setClickListener(MyRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}