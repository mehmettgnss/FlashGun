package com.simcoder.snapchatclone.loginRegistration;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simcoder.snapchatclone.ChangepassActivity;
import com.simcoder.snapchatclone.Chat.ChatHelper;
import com.simcoder.snapchatclone.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class editprofile extends AppCompatActivity {
    Calendar date = Calendar.getInstance();
    DateFormat formatDate = DateFormat.getDateInstance();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(
            firebaseUser.getUid());
    Map<String, Object> yeniUser = new HashMap<String, Object>();
    EditText mName, mUsername, mDate, mPhone;
    Button mSave, mPass, mLogout;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        mName = (EditText) findViewById(R.id.txt_name);
        mUsername = (EditText) findViewById(R.id.txt_username);
        mDate = (EditText) findViewById(R.id.txt_date);
        mPhone = (EditText) findViewById(R.id.txt_number);
        mSave = (Button) findViewById(R.id.btn_save);
        mPass = (Button) findViewById(R.id.btn_pass);
        mPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChangepassActivity.class);
                startActivity(i);
            }
        });
        mLogout = (Button) findViewById(R.id.btn_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Yes", "No"};
                AlertDialog.Builder builder = new AlertDialog.Builder(editprofile.this);
                builder.setTitle("Are You Sure ?");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Yes")) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(editprofile.this, SplashScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            return;
                        } else if (items[item].equals("No")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    mUsername.setText(dataSnapshot.child("displayName").getValue().toString());
                    mUsername.setEnabled(true);
                    mName.setText(dataSnapshot.child("Name").getValue().toString());
                    mName.setEnabled(true);
                    mDate.setText(dataSnapshot.child("Datetime").getValue().toString());
                    mDate.setEnabled(true);
                    mPhone.setText(dataSnapshot.child("PhoneNumber").getValue().toString());
                    mPhone.setEnabled(true);
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("Save...",true);
                yeniUser.put("Name", mName.getText().toString());
                yeniUser.put("displayName", mUsername.getText().toString());
                yeniUser.put("PhoneNumber", mPhone.getText().toString());
                yeniUser.put("Datetime", mDate.getText().toString());
                databaseReference.updateChildren(yeniUser);



            }
        });
    }

    public void date(View view) {

        new DatePickerDialog(this, d, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatedate();
        }
    };

    private void updatedate() {
        mDate.setText(formatDate.format(date.getTime()));
    }
    private void showAlertDialog(String message, boolean isCancelable){
        dialog = ChatHelper.buildAlertDialog(getString(R.string.login_error_title), message,isCancelable,editprofile.this);
        dialog.show();
    }

    private void dismissAlertDialog() {
        dialog.dismiss();
    }
}
