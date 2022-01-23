package com.linkitsoft.dategoal.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.linkitsoft.dategoal.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnableLocation extends AppCompatActivity {

    ImageButton imagebtnback;
    Button allow;
    Button ntallow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_location);


        SharedPreferences sharedpreferences;
        sharedpreferences = getSharedPreferences("MyPrefs", 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();


        imagebtnback = findViewById(R.id.imageButton19);

        allow = findViewById(R.id.button32);
        ntallow = findViewById(R.id.button34);

        imagebtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(EnableLocation.this, Login.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

//                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                editor.putString("allowloc", "true");
                editor.commit();
            }
        });

        ntallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("allowloc", "false");
                editor.commit();
                showdialog("Warning","Your Location Is Required",1);
            }
        });


    }


    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(this, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }
}