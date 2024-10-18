package com.example.kingpho.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kingpho.R;
import com.example.kingpho.model.Chating;

import java.util.List;

public class ChatingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Chating> chatingList;
    public static int MESSAGE_SEND = 1;
    public static int MESSAGE_RECEVICE = 2;

    public ChatingAdapter(List<Chating> chatingList) {
        this.chatingList = chatingList;
    }

    @Override
    public int getItemViewType(int position) {
        Chating chat = chatingList.get(position);
        if (chat.isSend()) {
            return MESSAGE_SEND;
        } else{
            return MESSAGE_RECEVICE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_SEND) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_recevice, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chating chat = chatingList.get(position);
        if (holder.getItemViewType() == MESSAGE_SEND) {
            ((SentMessageViewHolder) holder).bind(chat);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(chat);
        }
    }

    @Override
    public int getItemCount() {
        if (chatingList != null ){
            return chatingList.size();
        }
        return 0;
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messSend;
        private final TextView timeSend;

        SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messSend = itemView.findViewById(R.id.messSend);
            timeSend = itemView.findViewById(R.id.timeSend);
        }

        void bind(Chating chat) {
            messSend.setText(chat.getContent());
            timeSend.setText(chat.getTime());
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messReceive;
        private final TextView timeReceive;

        ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messReceive = itemView.findViewById(R.id.messRecevice);
            timeReceive = itemView.findViewById(R.id.timeRecevice);
        }

        void bind(Chating chat) {
            messReceive.setText(chat.getContent());
            timeReceive.setText(chat.getTime());
        }
    }
}
