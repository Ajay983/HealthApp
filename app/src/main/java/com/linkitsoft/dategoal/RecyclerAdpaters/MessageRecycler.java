package com.linkitsoft.dategoal.RecyclerAdpaters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkitsoft.dategoal.Models.FriendsModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.messages.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRecycler extends RecyclerView.Adapter<MessageRecycler.ViewHolder> {
    public List<FriendsModel> friendsModelList;
    public Context context;
    Boolean grpadd;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    public String grpid;

    public MessageRecycler(List<FriendsModel> friendsModelList, Boolean grpadd, String grpid) {
        this.friendsModelList = friendsModelList;
        this.grpadd = grpadd;
        this.grpid = grpid;
    }

    int layout = 0;

    public MessageRecycler(List<FriendsModel> friendsModelList, Boolean grpadd, String grpid, int layout) {
        this.friendsModelList = friendsModelList;
        this.grpadd = grpadd;
        this.layout = layout;
        this.grpid = grpid;
    }


    @NonNull
    @Override
    public MessageRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = layout == 0 ? LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_friendlist, parent, false) : LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        portnVariable = new PortnVariable();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecycler.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        String name = friendsModelList.get(position).getName();
        String photo1 = friendsModelList.get(position).getPhoto1();
        String fid = friendsModelList.get(position).get_id();
        int onlineStatus = friendsModelList.get(position).getOnlineStatus();

        holder.setdata(name, photo1, onlineStatus);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (grpadd) {
//
//                } else {
                Intent chat = new Intent(context, ChatActivity.class);
                chat.putExtra("FrID", fid);
                chat.putExtra("fnrdName",name);
                chat.putExtra("frndPhoto",photo1);
                context.startActivity(chat);
//                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return friendsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView myTextView;
        CircleImageView online;
        CircleImageView circleImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            myTextView = itemView.findViewById(R.id.textView13);
            online = itemView.findViewById(R.id.onlinesymbol);
            circleImageView = itemView.findViewById(R.id.profile_image);

//            circleImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context,Profile.class);
//                    context.startActivity(intent);
//                }
//            });

        }

        public void setdata(String name, String photo1, int onlineStatus) {

            Picasso.get().load(photo1).into(circleImageView);
            myTextView.setText(name);
            online.setImageResource(onlineStatus == 0 ? R.color.teal_200 : R.color.gray);

        }
    }
}
