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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleListRecycler extends RecyclerView.Adapter<PeopleListRecycler.ViewHolder> {

    public List<FriendsModel> friendsModelList;
    public Context context;


    public PeopleListRecycler(List<FriendsModel> friendsModelList) {
        this.friendsModelList = friendsModelList;
    }

    @NonNull
    @Override
    public PeopleListRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friendslistvert, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleListRecycler.ViewHolder holder, int position) {


        holder.setIsRecyclable(false);

        String name = friendsModelList.get(position).getName();
        String photo1 = friendsModelList.get(position).getPhoto1();
        String fid = friendsModelList.get(position).get_id();
        Date dob = friendsModelList.get(position).getDOB();
        String city = friendsModelList.get(position).getCity();

        holder.setdata(name, photo1, dob, city);

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
        TextView username;
        TextView messagetext;
        CircleImageView circleImageView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;
            username = itemView.findViewById(R.id.textView13);
            messagetext = itemView.findViewById(R.id.textView15);
            circleImageView = itemView.findViewById(R.id.profile_image);
        }

        public void setdata(String name, String photo1, Date dob, String city) {

            try {
                Picasso.get().load(photo1).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(circleImageView);
            } catch (Exception ex) {
            }
            username.setText(name);

            if (dob != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dob);
                int year = calendar.get(Calendar.YEAR);

                Calendar now = Calendar.getInstance();
                now.setTime(new Date());
                int cyear = now.get(Calendar.YEAR);
                int agee = 0;

                agee = cyear - year;
                messagetext.setText(agee + " • " + city);

            }


        }
    }
}
