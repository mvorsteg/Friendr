package com.vorsteghall.swiper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.vorsteghall.swiper.Cards.arrayAdapter;
import com.vorsteghall.swiper.Cards.cards;
import com.vorsteghall.swiper.Matches.MatchesActivity;

import java.util.ArrayList;
import java.util.List;
//HEY Sooo
public class MainActivity extends AppCompatActivity {

    private cards cards_data[];
    private com.vorsteghall.swiper.Cards.arrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth mAuth;
    private String userSex;
    private String oppositeUserSex;
    private String currentUid;
    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("DebugDCC", "aaaaaaaa 8");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        //Log.d("DebugDCC", "gg 8");
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        //Log.d("DebugDCC", "aaaaaaaa aac");


        checkUserSex();
        //Log.d("DebugDCC", "bbb 8");
        rowItems = new ArrayList<>();
        /*al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");*/

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        //Log.d("DebugDCC", "c 12dq");
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                //Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUid).setValue(true);

                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUid).setValue(true);
                isConnectionMatch(userId);

                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionMatch(final String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUid).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "new connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUid).child("ChatId").setValue(key);
                    usersDb.child(currentUid).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkUserSex(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("sex") != null){
                        userSex = dataSnapshot.child("sex").getValue().toString();
                        switch (userSex){
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }
                        getOppositeSex();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    public void getOppositeSex(){
        //Log.d("DCCDebug", "looking for opposite sex");
        //Log.d("DCCDebug", userSex);
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("sex").getValue() != null) {
                    //Log.d("DCCDebug", "checking for objects");
                    if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUid) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUid) && !dataSnapshot.getKey().toString().equals(currentUid)){
                        //Log.d("DCCDebug", "objects exist!");
                        String profileImageUrl = "default";
                        if (!dataSnapshot.child("profileImageUrl").getValue().equals("default")) {
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                        String bio = "";
                        if (dataSnapshot.child("bio").exists()) {
                            bio = dataSnapshot.child("bio").getValue().toString();
                        }
                        cards item = new cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString(), profileImageUrl, bio);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
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

    public void logoutUser(View view){
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void goToSettings(View view){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        return;
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
        return;
    }
}
