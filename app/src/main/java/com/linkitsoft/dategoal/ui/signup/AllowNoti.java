package com.linkitsoft.dategoal.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.linkitsoft.dategoal.Login;
import com.linkitsoft.dategoal.R;

public class AllowNoti extends AppCompatActivity {

    ImageButton imagebtnback;
    Button allow;
    Button ntallow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_noti);


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
                finish();
                Intent main = new Intent(AllowNoti.this, Login.class);
                startActivity(main);
                editor.putString("allownotifs", "true");
                editor.commit();
            }
        });

        ntallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent main = new Intent(AllowNoti.this, Login.class);
                startActivity(main);
                editor.putString("allownotifs", "false");
                editor.commit();
            }
        });


    }
}