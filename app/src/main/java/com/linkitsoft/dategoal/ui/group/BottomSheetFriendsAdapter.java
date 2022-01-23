package com.linkitsoft.dategoal.ui.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkitsoft.dategoal.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomSheetFriendsAdapter extends RecyclerView.Adapter<BottomSheetFriendsAdapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private BottomSheetFriendsAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public BottomSheetFriendsAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public BottomSheetFriendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_friendslistvert, parent, false);
        return new BottomSheetFriendsAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull BottomSheetFriendsAdapter.ViewHolder holder, int position) {

        String name=mData[position].split(",")[0];
        String age=mData[position].split(",")[1];
        String addres=mData[position].split(",")[2];
        String imageuri=mData[position].split(",")[3];

        Picasso.get().load(imageuri).into(holder.circleImageView);
        holder.username.setText(name);
        holder.messagetext.setText(age+" • "+addres);
          // holder.circleImageView.setImageURI(Uri.parse(imageuri));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView username;
        TextView messagetext;
        CircleImageView circleImageView;

        ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textView13);
            messagetext = itemView.findViewById(R.id.textView15);
            circleImageView = itemView.findViewById(R.id.profile_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            view.setBackgroundResource(R.drawable.bottomcardfriendsselected);
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    public void setClickListener(BottomSheetFriendsAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}