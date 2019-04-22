package com.vorsteghall.swiper.Cards;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vorsteghall.swiper.Cards.cards;
import com.vorsteghall.swiper.R;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class arrayAdapter extends ArrayAdapter<cards> {

    private LinkedList<String> bio;

    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
        this.bio = new LinkedList<>();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);
        Log.d("DCCDebug","aaa");
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);

        name.setText(card_item.getName());
        bio.push(card_item.getBio());
        switch(card_item.getProfileImageUrl()) {
            case "default":
                image.setImageResource(R.mipmap.ic_launcher);
                break;
            default:
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }

        return convertView;
    }

    public String getBio(){ return bio.getLast();}
    public void updateBio(){
        Log.d("DCCDebug",bio.size()+"");
        bio.removeLast();
        Log.d("DCCDebug", "H "+bio.getFirst());
    }
}
