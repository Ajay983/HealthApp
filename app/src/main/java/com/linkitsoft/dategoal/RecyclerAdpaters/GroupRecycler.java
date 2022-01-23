 package com.linkitsoft.dategoal.RecyclerAdpaters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkitsoft.dategoal.Models.GroupModel;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.group.Groupchat;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupRecycler extends RecyclerView.Adapter<GroupRecycler.ViewHolder> {

    public List<GroupModel> groupModelList;
    public Context context;

    public GroupRecycler(List<GroupModel> groupModelList){
        this.groupModelList = groupModelList;
    }

    @NonNull
    @Override
    public GroupRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friendlist, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRecycler.ViewHolder holder, int position) {

        String name = groupModelList.get(position).getTitle();
        String photo1 = groupModelList.get(position).getProfilePic();
        String fid = groupModelList.get(position).get_id();
        String admin_id = groupModelList.get(position).getAdminId();
        holder.circleImageView2.setVisibility(View.GONE);

        holder.setdata(name,photo1);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(context, Groupchat.class);
                profile.putExtra("GrID", fid);
                profile.putExtra("GrName", name);
                profile.putExtra("GrImg", photo1);
                profile.putExtra("adminId",admin_id);
                context.startActivity(profile);
            }
        });


    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView myTextView;
        CircleImageView circleImageView,circleImageView2;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;
            myTextView = itemView.findViewById(R.id.textView13);
            circleImageView = itemView.findViewById(R.id.profile_image);
            circleImageView2 = itemView.findViewById(R.id.onlinesymbol);
        }

        public void setdata(String name, String photo1) {

            Picasso.get().load(photo1).into(circleImageView);
            myTextView.setText(name);
        }
    }
}
