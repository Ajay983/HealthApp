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
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.Profile.Profile;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritRecycler extends RecyclerView.Adapter<FavoritRecycler.ViewHolder> {


    public List<FriendsModel> friendsModelList;
    public Context context;


    public FavoritRecycler(List<FriendsModel> friendsModelList){
        this.friendsModelList = friendsModelList;
    }


    @NonNull
    @Override
    public FavoritRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favs, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritRecycler.ViewHolder holder, int position) {

        String name = friendsModelList.get(position).getName();
        String photo1 = friendsModelList.get(position).getPhoto1();
        String fid = friendsModelList.get(position).get_id();

        holder.setdata(name,photo1);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profile = new Intent(context, Profile.class);
                profile.putExtra("FrID", fid);
                context.startActivity(profile);
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
        CircleImageView circleImageView;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;
            myTextView = itemView.findViewById(R.id.textView13);
            circleImageView = itemView.findViewById(R.id.profile_image);


        }

        public void setdata(String name, String photo1) {

            Picasso.get().load(photo1).into(circleImageView);
            myTextView.setText(name);
        }
    }
}
