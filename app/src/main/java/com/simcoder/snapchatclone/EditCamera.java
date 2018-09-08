package com.simcoder.snapchatclone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.simcoder.snapchatclone.PhotoEdit.PhotoMainActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

public class EditCamera extends AppCompatActivity {
    ImageView capturedImage, uploadmedia;
    Bitmap bitmap;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_camera);
        capturedImage = (ImageView) findViewById(R.id.captured_image);
        uploadmedia = (ImageView) findViewById(R.id.upload_media);




        try {
            bitmap = BitmapFactory.decodeStream(getApplication().openFileInput("imageToSend"));
            capturedImage.setImageBitmap(bitmap);

        } catch (Exception e) {

            e.printStackTrace();
            finish();
            return;
        }


        uploadmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileLocation = SaveImageToStorage(bitmap);
                Intent intent = new Intent(getApplicationContext(), ChooseReceiverActivity.class);
                startActivity(intent);
                return;
            }
        });


    }

    public String SaveImageToStorage(Bitmap bitmap) {
        String fileName = "imageToSend2";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public String SaveImageToStorage2(Bitmap bitmap) {
        String fileName = "imageToSend3";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public void filters(View view){
        String fileLocation = SaveImageToStorage2(bitmap);
        Intent intent = new Intent(getApplicationContext(), PhotoMainActivity.class);
        startActivity(intent);
        return;
    }

}
