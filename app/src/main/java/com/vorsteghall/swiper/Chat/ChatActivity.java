package com.vorsteghall.swiper.Chat;

import android.icu.text.LocaleDisplayNames;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vorsteghall.swiper.Matches.MatchesActivity;
import com.vorsteghall.swiper.Matches.MatchesAdapter;
import com.vorsteghall.swiper.Matches.MatchesObject;
import com.vorsteghall.swiper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecylcerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;
    private String currentUserId, matchId, chatId;
    private EditText mSendEditText;
    private Button mSendButton;

    DatabaseReference mDatabaseUser, mDatabaseChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchId = getIntent().getExtras().getString("matchId");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches").child(matchId).child("ChatId");
        //Log.d("DCCDebug",mDatabaseUser.toString());
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();
        //Log.d("DCCDebug","out of it");
        mRecylcerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecylcerView.setNestedScrollingEnabled(false);
        mRecylcerView.setHasFixedSize(false);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecylcerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecylcerView.setAdapter(mChatAdapter);

        mSendEditText = findViewById(R.id.message);
        mSendButton = findViewById(R.id.send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage(){
        String sendMessageText = mSendEditText.getText().toString();
        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();
            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserId);
            newMessage.put("text", sendMessageText);
            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);
    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    chatId = dataSnapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                    //Log.d("DCCDebug","back 2 the chatid");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getChatMessages() {
        //Log.d("DCCDebug","getting msg");
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    //Log.d("DCCDebug","snap exists");
                    String message = null;
                    String createdByUser = null;
                    //Log.d("DCCDebug","1");
                    if(dataSnapshot.child("text").getValue()!=null){
                       // Log.d("DCCDebug","2");
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("createdByUser").getValue()!=null){
                        //Log.d("DCCDebug","3");
                        createdByUser = dataSnapshot.child("createdByUser").getValue().toString();
                    }
                    if(message != null && createdByUser != null){
                        //Log.d("DCCDebug","4");
                        boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserId)){
                            currentUserBoolean = true;
                        }
                        //Log.d("DCCDebug","5");
                        ChatObject newMessage = new ChatObject(message,currentUserBoolean);
                        //Log.d("DCCDebug",message);
                        resultsChat.add(newMessage);
                        //Log.d("DCCDebug","ggg");
                        mChatAdapter.notifyDataSetChanged();
                       // Log.d("DCCDebug","gg44g");
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private ArrayList<ChatObject> resultsChat = new ArrayList<ChatObject>();

    private List<ChatObject> getDataSetChat() {
        //Log.d("DCCDebug","ghuhg");
        return resultsChat;
    }
}
