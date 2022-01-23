package com.linkitsoft.dategoal.ui.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linkitsoft.dategoal.MyRecyclerViewAdapter;
import com.linkitsoft.dategoal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class recyclefavs extends RecyclerView.Adapter<recyclefavs.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public recyclefavs(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    View view;
    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public recyclefavs.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         view = mInflater.inflate(R.layout.item_favs, parent, false);
        return new recyclefavs.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull recyclefavs.ViewHolder holder, int position) {

        String name=mData[position].split(",")[0];
        String imageuri=mData[position].split(",")[1];
        Boolean onlinestatus=mData[position].split(",")[2].equals("1");

        Picasso.get().load(imageuri).into(holder.circleImageView);
        holder.myTextView.setText(name);
        final boolean[] flag = {true};

//        holder.fabh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(flag[0]){
//
//                    holder.fabh.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.heart));
//                    flag[0] = false;
//
//                }else if(!flag[0]){
//
//                    holder.fabh.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.heartsel));
//                    flag[0] = true;
//
//                }
//
//            }
//        });

        // holder.online.setImageResource(onlinestatus?R.drawable.heartsel:R.drawable.heart);
        // holder.circleImageView.setImageURI(Uri.parse(imageuri));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        FloatingActionButton fabh;
        CircleImageView online;
        CircleImageView circleImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView13);
            fabh = itemView.findViewById(R.id.floatingActionButton13);
            online = itemView.findViewById(R.id.onlinesymbol);
            circleImageView = itemView.findViewById(R.id.profile_image);
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
    public void setClickListener(MyRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}