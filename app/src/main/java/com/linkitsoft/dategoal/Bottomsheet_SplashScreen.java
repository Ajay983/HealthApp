package com.linkitsoft.dategoal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Bottomsheet_SplashScreen extends BottomSheetDialogFragment {

    public static Bottomsheet_SplashScreen newInstance() {
        Bottomsheet_SplashScreen fragment = new Bottomsheet_SplashScreen();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottomsheet_splashscren, null);
        dialog.setContentView(contentView);

        View.OnClickListener myclik = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                startActivity(new Intent(getContext(),Login.class));
            }
        };

       contentView.findViewById(R.id.english).setOnClickListener(myclik);
       contentView.findViewById(R.id.French).setOnClickListener(myclik);
       contentView.findViewById(R.id.Dutch).setOnClickListener(myclik);
       contentView.findViewById(R.id.Spanish).setOnClickListener(myclik);
       contentView.findViewById(R.id.chine).setOnClickListener(myclik);
       contentView.findViewById(R.id.Hindi).setOnClickListener(myclik);
       contentView.findViewById(R.id.arab).setOnClickListener(myclik);
       contentView.findViewById(R.id.Russian).setOnClickListener(myclik);
       contentView.findViewById(R.id.Portuguese).setOnClickListener(myclik);
       contentView.findViewById(R.id.Japanese).setOnClickListener(myclik);
       contentView.findViewById(R.id.indo).setOnClickListener(myclik);
       contentView.findViewById(R.id.mly).setOnClickListener(myclik);
       contentView.findViewById(R.id.thai).setOnClickListener(myclik);


        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

}