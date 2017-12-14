package com.example.sony.permisson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.Intent;


import android.graphics.*;
import android.net.Uri;
import android.os.Environment;


import android.provider.MediaStore;


import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;


import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static int IMG_RESULT = 2;
    private static final String TAG = MainActivity.class.toString();



    ImageView albumbtn;
    ImageView cambtn;
    CropImageView imv;
    ImageView btnDone;
    Uri file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        albumbtn = (ImageView) findViewById(R.id.albunBtn);
        cambtn = (ImageView) findViewById(R.id.camBtn);
        btnDone = (ImageView) findViewById(R.id.btndone);
        imv = (CropImageView) findViewById(R.id.imv);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},200);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},300);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(),android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            cambtn.setEnabled(false);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, 500);

        }



        btnDone.setVisibility(View.INVISIBLE);

        addListenner();

    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }



    private void addListenner() {

        cambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = Uri.fromFile(getOutputMediaFile());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                startActivityForResult(intent, 100);

            }
        });
        albumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, IMG_RESULT);

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap cropped = imv.getCroppedImage(500, 500);
                if (cropped != null)
                    imv.setImageBitmap(cropped);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imv.setImageUriAsync(file);
                btnDone.setVisibility(View.VISIBLE);
            }
        }

        if (requestCode == IMG_RESULT && resultCode == MainActivity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            imv.setImageUriAsync(selectedImage);
            btnDone.setVisibility(View.VISIBLE);

        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 200:{
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Read permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            case 300:{
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Write permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            case 500:{
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Capture permission denied", Toast.LENGTH_SHORT).show();
                    cambtn.setEnabled(true);
                }
            }
        }
    }

}
