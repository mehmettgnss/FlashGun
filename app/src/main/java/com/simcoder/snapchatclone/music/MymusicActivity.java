package com.simcoder.snapchatclone.music;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.snapchatclone.R;

import java.util.ArrayList;
import java.util.List;

public class MymusicActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(
            firebaseUser.getUid());
    String value, username;
    ListView listView;
    String selected;
    private List<music> mylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymusic);
        listView = (ListView) findViewById(R.id.musiclist);
        mylist = new ArrayList<music>();
        Bundle extras = getIntent().getExtras();
        value = extras.getString("data");



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("displayName").getValue().toString();
                myRef.child("usermusic").child(username).child(value).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 1; i <= dataSnapshot.getChildrenCount(); i++) {
                            myRef.child("usermusic").child(username).child(value).child("music" + i).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    mylist.add(new music(dataSnapshot.child("music").getValue().toString(),
                                            dataSnapshot.child("singer").getValue().toString(),
                                            dataSnapshot.child("url").getValue().toString()));
                                    CustomListView adapter = new CustomListView(MymusicActivity.this, R.layout.recylerview_find_music, mylist);
                                    listView.setAdapter(adapter);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
