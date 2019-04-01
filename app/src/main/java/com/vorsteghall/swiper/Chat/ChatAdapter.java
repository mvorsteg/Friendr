package com.vorsteghall.swiper.Chat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.vorsteghall.swiper.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders>{
    private List<ChatObject> chatList;
    private Context context;


    public ChatAdapter(List<ChatObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders rcv = new ChatViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders matchesViewHolders, int i) {
        matchesViewHolders.mMessage.setText(chatList.get(i).getMessage());
        if(chatList.get(i).getCurrentUser()){
           // Log.d("DCCDebug","adap 1");
            matchesViewHolders.mMessage.setGravity(Gravity.END);
            //Log.d("DCCDebug","adap 2");
            matchesViewHolders.mMessage.setTextColor(Color.parseColor("#404040"));
            //Log.d("DCCDebug","adap 3");
            matchesViewHolders.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
            //Log.d("DCCDebug","adap 4");
        }else{
            //Log.d("DCCDebug","adap a");
            matchesViewHolders.mMessage.setGravity(Gravity.START);
            matchesViewHolders.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            matchesViewHolders.mContainer.setBackgroundColor(Color.parseColor("#2DB4C8"));
           // Log.d("DCCDebug","adap b");
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
