package com.linkitsoft.dategoal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class EnableLocation extends AppCompatActivity {

    ImageButton imagebtnback;
    Button allow;
    Button ntallow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_location);

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
                Intent main = new Intent(EnableLocation.this, Login.class);
                startActivity(main);
            }
        });

        ntallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(EnableLocation.this, Login.class);
                startActivity(main);
            }
        });



    }
}