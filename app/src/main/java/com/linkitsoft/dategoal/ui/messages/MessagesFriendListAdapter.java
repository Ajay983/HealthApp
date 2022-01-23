package com.linkitsoft.dategoal.ui.messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkitsoft.dategoal.MyRecyclerViewAdapter;
import com.linkitsoft.dategoal.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesFriendListAdapter extends RecyclerView.Adapter<MessagesFriendListAdapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    public Context context;

    // data is passed into the constructor
    public MessagesFriendListAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public MessagesFriendListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_messagefriend, parent, false);
        return new MessagesFriendListAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull MessagesFriendListAdapter.ViewHolder holder, int position) {

        String name=mData[position].split(",")[0];
        String imageuri=mData[position].split(",")[1];
        Boolean onlinestatus=mData[position].split(",")[2].equals("1");

        Picasso.get().load(imageuri).into(holder.circleImageView);
        holder.myTextView.setText(name);
        holder.online.setImageResource(onlinestatus?R.color.teal_200:R.color.gray);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(context,ChatActivity.class);
                context.startActivity(chat);
            }
        });
        // holder.circleImageView.setImageURI(Uri.parse(imageuri));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }

    public void setClickListener(ItemClickListener itemClickListener) {

    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        CircleImageView online;
        CircleImageView circleImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView13);
            online = itemView.findViewById(R.id.onlinesymbol);
            circleImageView = itemView.findViewById(R.id.profile_image);
            itemView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public void onClick(View view) {
            view.getContext().startActivity(new Intent(view.getContext(),ChatActivity.class));
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