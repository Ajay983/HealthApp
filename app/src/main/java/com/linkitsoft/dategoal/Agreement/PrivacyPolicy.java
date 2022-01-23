package com.linkitsoft.dategoal.Agreement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.linkitsoft.dategoal.R;

public class PrivacyPolicy extends AppCompatActivity {
    Button privacy_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy2);
        privacy_next = findViewById(R.id.privacyNext);
        privacy_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrivacyPolicy.this, CopyRightPolicy.class);
                startActivity(intent);
                finish();
            }
        });
    }
}