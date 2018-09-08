package com.simcoder.snapchatclone.music;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.snapchatclone.R;

import java.util.HashMap;
import java.util.Map;

public class AddMusicActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = firebaseDatabase.getReference();
    private EditText txtMusic,txtSinger,txtId;
    private Spinner spinner;

    private String select;
    CountMusic countMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmusic);

        txtMusic = (EditText) findViewById(R.id.txt_music);
        txtSinger = (EditText) findViewById(R.id.txt_singer);
        txtId = (EditText) findViewById(R.id.txt_id);
        spinner= (Spinner) findViewById(R.id.spinner);


    }
    public void addmusic(View view){

        select = spinner.getSelectedItem().toString();
        getcount();
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMusicActivity.this);

        builder.setTitle("Confirm");
        builder.setMessage("Do You Want to Add The Music?");

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
    public void getcount(){
        myRef.child("musics").child(""+select).addValueEventListener(new ValueEventListener() {
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
        Map<String, String> newMusic = new HashMap<String, String>();
        newMusic.put("music", txtMusic.getText().toString());
        newMusic.put("singer", txtSinger.getText().toString());
        newMusic.put("musicid", txtId.getText().toString());

        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("musics").child(""+select).child("music"+countMusic.getMusiccount())
                .setValue(newMusic);
        Toast.makeText(getApplicationContext(),"Added Mucic to "+select,Toast.LENGTH_LONG).show();

        txtMusic.setText("");
        txtSinger.setText("");
        txtId.setText("");
    }
}
