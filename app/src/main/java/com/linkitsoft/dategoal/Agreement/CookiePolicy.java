package com.linkitsoft.dategoal.Agreement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.linkitsoft.dategoal.R;

public class CookiePolicy extends AppCompatActivity {
    Button cookie_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookie_policy);
        cookie_next = findViewById(R.id.cookieNext);
        cookie_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CookiePolicy.this, PrivacyPolicy.class);
                startActivity(intent);
                finish();
            }
        });
    }
}