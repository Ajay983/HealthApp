package com.linkitsoft.dategoal.Agreement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.linkitsoft.dategoal.R;

public class CopyRightPolicy extends AppCompatActivity {
    Button copyright_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_right_policy);
        copyright_next = findViewById(R.id.copyRightNext);
        copyright_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CopyRightPolicy.this, TermsOfUse.class);
                startActivity(intent);
                finish();
            }
        });
    }
}