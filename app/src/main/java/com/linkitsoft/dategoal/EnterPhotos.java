package com.linkitsoft.dategoal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

public class EnterPhotos extends AppCompatActivity {

    ImageButton imagebtnback;
    ImageButton imagebtnnext;

    ImageButton uploadimage1;
    ImageButton uploadimage2;
    ImageButton uploadimage3;
    ImageButton uploadimage4;
    ImageButton uploadimage5;
    ImageButton uploadimage6;

    Boolean img1;
    Boolean img2;
    Boolean img3;
    Boolean img4;
    Boolean img5;
    Boolean img6;

    public  final int GET_FROM_GALLERY = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_photos);

        //imagebtnback = findViewById(R.id.imageButton);
        imagebtnnext = findViewById(R.id.imageButton2);

        uploadimage1 = findViewById(R.id.imageButton4);
        uploadimage2 = findViewById(R.id.imageButton5);
        uploadimage3 = findViewById(R.id.imageButton6);
        uploadimage4 = findViewById(R.id.imageButton7);
        uploadimage5 = findViewById(R.id.imageButton8);
        uploadimage6 = findViewById(R.id.imageButton9);

        uploadimage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img1 = true;
                img2 = false;
                img3 = false;
                img4 = false;
                img5 = false;
                img6 = false;


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);
            }
        });

        uploadimage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img2 = true;
                img1 = false;
                img3 = false;
                img4 = false;
                img5 = false;
                img6 = false;


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);
            }
        });

        uploadimage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img3 = true;
                img1 = false;
                img2 = false;
                img4 = false;
                img5 = false;
                img6 = false;


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);
            }
        });

        uploadimage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img4 = true;
                img1 = false;
                img2 = false;
                img3 = false;
                img5 = false;
                img6 = false;


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);
            }
        });

        uploadimage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img5 = true;
                img1 = false;
                img2 = false;
                img3 = false;
                img4 = false;
                img6 = false;


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);
            }
        });

        uploadimage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img6 = true;
                img1 = false;
                img2 = false;
                img3 = false;
                img4 = false;
                img5 = false;


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , GET_FROM_GALLERY);
            }
        });


        imagebtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imagebtnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(EnterPhotos.this, AllowNoti.class);
                startActivity(main);
                //finish();
            }
        });

    }

    Uri selectedImage;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            selectedImage = data.getData();

            if(img1) {
                Picasso.get().load(selectedImage).into(uploadimage1);
            }
            if(img2) {
                Picasso.get().load(selectedImage).into(uploadimage2);
            }
            if(img3) {
                Picasso.get().load(selectedImage).into(uploadimage3);
            }

            if(img4) {
                Picasso.get().load(selectedImage).into(uploadimage4);
            }

            if(img5) {
                Picasso.get().load(selectedImage).into(uploadimage5);
            }
            if(img6) {
                Picasso.get().load(selectedImage).into(uploadimage6);
            }


        }
    }
}