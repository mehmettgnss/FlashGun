package com.simcoder.snapchatclone.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.face.Face;


import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Landmark;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.snapchatclone.music.AddMusicActivity;
import com.simcoder.snapchatclone.music.MusicPlayerActivity;
import com.simcoder.snapchatclone.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MusicFragment extends Fragment {
    Uri filePath;
    Button btnSelect,btnDedect,btnEnjoy,mAddmusic;
    ImageView mImage,mEmo;
    TextView txtSmiling;
    Bitmap myBitmap;
    private String admin = "PphvAOEijMUNma6DS4i2RMKqPpX2";
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(
            firebaseUser.getUid());
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    public static MusicFragment newInstance(){
        MusicFragment fragment = new MusicFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        try {
            if (txtSmiling.getText()!="")
                btnEnjoy.setVisibility(View.VISIBLE);
        }catch (Exception e){}

        btnSelect = (Button) view.findViewById(R.id.buttonSelectImage);
        mImage = (ImageView) view.findViewById(R.id.selectedImage);
        mEmo = (ImageView) view.findViewById(R.id.emoimage);
        mAddmusic = (Button) view.findViewById(R.id.btn_addmusic);
        btnEnjoy = (Button) view.findViewById(R.id.btn_enjoy);
        txtSmiling = (TextView) view.findViewById(R.id.txt_smiling);
        btnDedect = (Button) view.findViewById(R.id.btn_dedect);
        if(!databaseReference.getKey().equals(admin)){
           mAddmusic.setVisibility(View.INVISIBLE);
        }
        mAddmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), AddMusicActivity.class);
                startActivity(in);
            }
        });
        btnDedect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBitmap == null){
                    Toast.makeText(getActivity(),
                            "Please Select Image !",
                            Toast.LENGTH_LONG).show();
                }else{
                    detectFace();

                }
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Lets Show!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            cameraIntent();
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
        btnEnjoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtSmiling.getText().toString().equals("")){
                 Intent i = new Intent(getActivity(), MusicPlayerActivity.class);
                 i.putExtra("data",txtSmiling.getText().toString());
                 startActivity(i);}
                 else{
                    Toast.makeText(getActivity(),
                            "Show me :D",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private void cameraIntent() {

        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent() {

        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            filePath = data.getData();

            try {
                myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                mImage.setImageBitmap(myBitmap);
            }catch (Exception e){e.printStackTrace();}

        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){

            filePath = data.getData();

            myBitmap = (Bitmap)data.getExtras().get("data");
            mImage.setImageBitmap(myBitmap);
        }
    }
    private void detectFace(){

        //Create a Paint object for drawing with
        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(5);
        myRectPaint.setColor(Color.GREEN);
        myRectPaint.setStyle(Paint.Style.STROKE);

        Paint landmarksPaint = new Paint();
        landmarksPaint.setStrokeWidth(10);
        landmarksPaint.setColor(Color.RED);
        landmarksPaint.setStyle(Paint.Style.STROKE);

        Paint smilingPaint = new Paint();
        smilingPaint.setStrokeWidth(4);
        smilingPaint.setColor(Color.YELLOW);
        smilingPaint.setStyle(Paint.Style.STROKE);

        boolean somebodySmiling = false;

        //Create a Canvas object for drawing on
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);



        com.google.android.gms.vision.face.FaceDetector faceDetector =
                new com.google.android.gms.vision.face.FaceDetector.Builder(getActivity().getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_LANDMARKS)
                        .setClassificationType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                        .build();

        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
        for(int i=0; i<faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
            List<Landmark> landmarks = thisFace.getLandmarks();
            for(int l=0; l<landmarks.size(); l++){
                PointF pos = landmarks.get(l).getPosition();
                tempCanvas.drawPoint(pos.x, pos.y, landmarksPaint);
            }
            final float a = 0.8000f;
            final float b = 0.1050f;
            final float c = 0.0090f;
            float smilingProbability = thisFace.getIsSmilingProbability();
            if(smilingProbability>=a){
                txtSmiling.setText("Happy");
                mEmo.setImageResource(R.drawable.happy);
            }
            else if(smilingProbability<a&&smilingProbability>=b){
                txtSmiling.setText("Smiling");
                mEmo.setImageResource(R.drawable.smiling);
            }
            else if(smilingProbability<b&&smilingProbability>=c){
                txtSmiling.setText("Standart");
                mEmo.setImageResource(R.drawable.suspicious);
            }
            else{
                txtSmiling.setText("Sad");
                mEmo.setImageResource(R.drawable.unhappy);
            }
        }

    }

}
