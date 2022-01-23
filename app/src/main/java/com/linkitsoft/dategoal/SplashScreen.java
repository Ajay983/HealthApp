package com.linkitsoft.dategoal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class  SplashScreen extends AppCompatActivity {

    Button  selectlang;
    SharedPreferences sharedpreferences;
    public String userid;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        selectlang = findViewById(R.id.spinner);
        img = findViewById(R.id.imageView);



        if (ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }else{
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        if (ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        if (ContextCompat.checkSelfPermission(SplashScreen.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else{
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }




        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        userid = sharedpreferences.getString("userid","0");

        assert userid != null;
        if(!userid.equals("0")){
            Intent main = new Intent(SplashScreen.this,  Homescreen.class);
            startActivity(main);
            finish();
        }else {
            img.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //code for next page on click
//                    img.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(SplashScreen.this,Login.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });


                    //automatically navigate to next page

                    Intent intent = new Intent(SplashScreen.this,Login.class);
                    startActivity(intent);
                    finish();

                }
            },5000);

        }

        selectlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bottomsheet_SplashScreen bottomSheetDialog = Bottomsheet_SplashScreen.newInstance();
                bottomSheetDialog.show(getSupportFragmentManager(), "Bottomsheet_profile_friends");

            }
        });
//        String ap[] = new String[14];
//
//        ap[0] = "Select";
//        ap[1] = "English";
//        ap[2] = "French";
//        ap[3] = "Dutch";
//        ap[4] = "Spanish";
//        ap[5] = "Mandarin Chinese";
//        ap[6] = "Hindi";
//        ap[7] = "Standard Arabic";
//        ap[8] =  "Russian";
//        ap[9] = "Portuguese";
//        ap[10] = "Japanese";
//        ap[11] = "Bahasa Indonesia";
//        ap[12] = "Bahasa Malay";
//        ap[13] = "Thai";
//
//        ArrayAdapter adapter= new ArrayAdapter(this, R.layout.spinneritem, ap);
//        selectlang.setAdapter(adapter);
//
//        selectlang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
////                    Intent main = new Intent(SplashScreen.this, Signup.class);
////                    startActivity(main);
////                    finish();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });




    }

    @Override
    public void onResume() {
        super.onResume();
        userid = sharedpreferences.getString("userid","0");
        assert userid != null;
        if(!userid.equals("0")){
            finish();
        }
    }
}