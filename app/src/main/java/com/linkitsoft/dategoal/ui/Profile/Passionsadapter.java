package com.linkitsoft.dategoal.ui.Profile;

import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.linkitsoft.dategoal.R;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;



public class Passionsadapter extends RecyclerView.Adapter<Passionsadapter.ViewHolder> {

    public LinkedHashMap<String,Boolean> friendsModelList;
    public Context context;


    public Passionsadapter(LinkedHashMap<String,Boolean> friendsModelList){
        this.friendsModelList = friendsModelList;
    }

    @NonNull
    @Override
    public Passionsadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_passions, parent, false);
        context = parent.getContext();

        return new Passionsadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Passionsadapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

            List<String> passions =   new ArrayList(friendsModelList.keySet());
            String currentpassion = passions.get(position);
            Boolean ismatching = friendsModelList.get(currentpassion);

            if (currentpassion.equals("")){
                holder.myTextView.setVisibility(View.GONE);
            }else{
                holder.myTextView.setText(currentpassion);
            }


        if(ismatching) {
            holder.myTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.passiontag));
            holder.myTextView.setTextColor(Color.parseColor("#ffffff"));
        }
        else if (ismatching && passions.isEmpty()){
            holder.myTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return friendsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView myTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myTextView = itemView.findViewById(R.id.textView30);

        }


    }
}
