package com.simcoder.snapchatclone.music;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.simcoder.snapchatclone.MainActivity;
import com.simcoder.snapchatclone.R;
import com.simcoder.snapchatclone.fragment.MusicFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MusicPlayerActivity extends YouTubeBaseActivity {

    Button btnChange,btnMymusic,btnExit,btnLike;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(
            firebaseUser.getUid());
    private int a;
    private long count=1;
    String music,singer,displayName,url;
    String value;
    Random random = new Random();
    CountMusic countMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
        Bundle extras = getIntent().getExtras();
        value = extras.getString("data");

        btnChange = (Button) findViewById(R.id.btn_change);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
            }
        });
        btnMymusic = (Button) findViewById(R.id.btn_liked);
        btnMymusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MusicPlayerActivity.this,MymusicActivity.class);
                i.putExtra("data",value);
                startActivity(i);
            }
        });
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MusicPlayerActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnLike = (Button) findViewById(R.id.btn_like);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            displayName = dataSnapshot.child("displayName").getValue().toString();
                            getcount();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MusicPlayerActivity.this);

                        builder.setTitle("Confirm");
                        builder.setMessage("Do You Like This Music?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) { addmusic(); }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {dialog.dismiss(); }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youTubePlayerView);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                if(value.equals("Happy")){
                    myRef.child("musics").child("Happy").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final DatabaseReference databaseReference = firebaseDatabase.getReference("musics").child("Happy")
                                    .child("music"+(1+random.nextInt((int)dataSnapshot.getChildrenCount())));
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        music = dataSnapshot.child("music").getValue().toString();
                                        singer = dataSnapshot.child("singer").getValue().toString();
                                        url = dataSnapshot.child("musicid").getValue().toString();
                                        youTubePlayer.loadVideo(url);
                                    }catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
                if(value.equals("Smiling")){
                    myRef.child("musics").child("Smiling").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference databaseReference = firebaseDatabase.getReference("musics").child("Smiling")
                                    .child("music"+(1+random.nextInt((int)dataSnapshot.getChildrenCount())));
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        music = dataSnapshot.child("music").getValue().toString();
                                        singer = dataSnapshot.child("singer").getValue().toString();
                                        url = dataSnapshot.child("musicid").getValue().toString();
                                        youTubePlayer.loadVideo(url);

                                    }catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
                if(value.equals("Standart")){
                    myRef.child("musics").child("Standart").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference databaseReference = firebaseDatabase.getReference("musics").child("Standart")
                                    .child("music"+(1+random.nextInt((int)dataSnapshot.getChildrenCount())));
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        music = dataSnapshot.child("music").getValue().toString();
                                        singer = dataSnapshot.child("singer").getValue().toString();
                                        url = dataSnapshot.child("musicid").getValue().toString();
                                        youTubePlayer.loadVideo(url);

                                    }catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
                if(value.equals("Sad")){
                    myRef.child("musics").child("Sad").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            DatabaseReference databaseReference = firebaseDatabase.getReference("musics").child("Sad")
                                    .child("music"+(1+random.nextInt((int)dataSnapshot.getChildrenCount())));
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        music = dataSnapshot.child("music").getValue().toString();
                                        singer = dataSnapshot.child("singer").getValue().toString();
                                        url = dataSnapshot.child("musicid").getValue().toString();
                                        youTubePlayer.loadVideo(url);

                                    }catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };
        youTubePlayerView.initialize("42670392089-lnl9tps6msdt6qtihshvl0r43sjpee0j.apps.googleusercontent.com",onInitializedListener);
    }
    public void getcount(){
        myRef.child("usermusic").child(displayName).child(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countMusic = new CountMusic((int) dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void addmusic(){
        final Map<String, String> newMusic = new HashMap<String, String>();
        newMusic.put("displayName", displayName);
        newMusic.put("music", music);
        newMusic.put("singer", singer);
        newMusic.put("url", url);
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("usermusic").child(displayName).child(value).child("music"+countMusic.getMusiccount())
                .setValue(newMusic);
        Toast.makeText(getApplicationContext(),"Added Your Music",Toast.LENGTH_LONG).show();
    }


}
