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

public class send_message_adapter extends RecyclerView.Adapter<send_message_adapter.ViewHolder> {

    private LayoutInflater mInflater;
    private String data;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;

    public send_message_adapter(Context context, String data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_chat, parent, false);
        return new send_message_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull send_message_adapter.ViewHolder holder, int position) {
        String message = data;
        holder.msg_send.setText(message);

    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView msg_send;

        ViewHolder(View itemView) {
            super(itemView);
            msg_send = itemView.findViewById(R.id.sentTxt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            view.getContext().startActivity(new Intent(view.getContext(), ChatActivity.class));
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
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
