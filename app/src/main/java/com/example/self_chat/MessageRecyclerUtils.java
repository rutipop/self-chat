package com.example.self_chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageRecyclerUtils {

    static class MessageHolder extends RecyclerView.ViewHolder{
        public final TextView text;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.sent_message_view);
        }
    }

    static class MessageCallBack extends DiffUtil.ItemCallback<Message>{

        @Override
        public boolean areItemsTheSame(@NonNull Message message, @NonNull Message t1) {
            return message == t1;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message message, @NonNull Message t1) {
            return message.equals(t1);
        }
    }


    static class MessageAddapter extends ListAdapter<Message, MessageHolder>{

        public MessageAddapter(){
            super(new MessageCallBack());
        }

        @NonNull
        @Override
        public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.one_message_layout,viewGroup,false);
            return new MessageHolder (itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {
            Message message = getItem(i);
            messageHolder.text.setText(message.text_in_message);


        }
    }

}
