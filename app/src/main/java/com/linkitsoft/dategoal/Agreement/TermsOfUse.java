package com.linkitsoft.dategoal.Agreement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.signup.Signup;

public class TermsOfUse extends AppCompatActivity {
    Button terms_next;
    String agreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);
        terms_next = findViewById(R.id.termsNext);
        terms_next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                agreed = "done";
                Intent intent = new Intent(TermsOfUse.this, Signup.class);
                intent.putExtra(agreed,"done");
                startActivity(intent);
                finish();
            }
        });
    }
}