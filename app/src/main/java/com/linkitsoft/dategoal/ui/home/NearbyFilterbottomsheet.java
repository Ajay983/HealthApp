package com.linkitsoft.dategoal.ui.home;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.linkitsoft.dategoal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NearbyFilterbottomsheet extends BottomSheetDialogFragment {


   public static Home_List hm;
   public static String selected;

    public static NearbyFilterbottomsheet newInstance(Home_List ham,String selectead) {
        hm=ham;
        selected=selectead.toLowerCase();
        NearbyFilterbottomsheet fragment = new NearbyFilterbottomsheet();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottonsheet_nerbyfilter, null);
        dialog.setContentView(contentView);


        Button fab = contentView.findViewById(R.id.button16);

       if(selected.equals(fab.getText().toString().toLowerCase())) fab.setTextColor(Color.parseColor("#ff6743"));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                hm.setfrombottomsheet("NearBy");
            }
        });

         Button faba = contentView.findViewById(R.id.button17);
        if(selected.equals(faba.getText().toString().toLowerCase())) faba.setTextColor(Color.parseColor("#ff6743"));

        faba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        hm.setfrombottomsheet("BestMatching");
                    }
                });

         Button fabb = contentView.findViewById(R.id.button18);
        if(selected.equals(fabb.getText().toString().toLowerCase())) fabb.setTextColor(Color.parseColor("#ff6743"));

        fabb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        hm.setfrombottomsheet("Date");
                    }
                });

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

}