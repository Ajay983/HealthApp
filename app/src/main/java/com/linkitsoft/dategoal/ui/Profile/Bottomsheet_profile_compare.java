package com.linkitsoft.dategoal.ui.Profile;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.linkitsoft.dategoal.Models.PassionModel;
import com.linkitsoft.dategoal.R;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marcinmoskala.arcseekbar.ArcSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import static com.google.android.flexbox.AlignItems.*;
import static com.google.android.flexbox.FlexDirection.*;
import static com.google.android.flexbox.FlexWrap.*;

public class Bottomsheet_profile_compare extends BottomSheetDialogFragment {
    private static int mtc;
    private static Boolean sp = false;
    private static JSONArray passion = null;
    private static List<PassionModel> passions = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    public static Bottomsheet_profile_compare newInstance(int match, JSONArray passio) {
        Bottomsheet_profile_compare fragment = new Bottomsheet_profile_compare();
        mtc = match;
        passion = passio;
        return fragment;
    }
    public static Bottomsheet_profile_compare newInstance(int match, List<PassionModel> passio, Boolean Swipe) {
        Bottomsheet_profile_compare fragment = new Bottomsheet_profile_compare();
        mtc = match;
        passions = passio;
        sp = Swipe;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ArcSeekBar seekBar;
    TextView perc;
    boolean match;
    RecyclerView passionsRecylview;



    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottomsheet_profile_compare, null);
        dialog.setContentView(contentView);
        setWhiteNavigationBar(dialog);
        seekBar = contentView.findViewById(R.id.arcSeekBar);
        perc = contentView.findViewById(R.id.textView39);
        seekBar.setProgress(mtc);
        perc.setText(mtc+"%");
        passionsRecylview = contentView.findViewById(R.id.grii);

        setWhiteNavigationBar(dialog);

        FloatingActionButton fab = contentView.findViewById(R.id.floatingActionButton8);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        LinkedHashMap<String, Boolean> passionlist = new LinkedHashMap<String, Boolean>();

        //if navigate from swipe page
        if(sp){
            if (passions.size() == 0) {

            } else {


                for (int i = 0; i < passions.size(); i++) {

                    match = passions.get(i).getMatch();
                    passionlist.put((passions.get(i)).getPassion(), match);

                }
            }
            FlexboxLayoutManager fblm = new FlexboxLayoutManager(contentView.getContext(), ROW);
            fblm.setFlexWrap(WRAP);
//                            fblm.setAlignContent(AlignContent.STRETCH);
            fblm.setAlignItems(STRETCH);
            Passionsadapter adapter = new Passionsadapter(passionlist);
            passionsRecylview.setLayoutManager(fblm);
            passionsRecylview.setAdapter(adapter);
            passionsRecylview.setHasFixedSize(true);


        }
        //if navigate from porifle page
        else{

        if (passion.length() == 0) {

        } else {


            for (int i = 0; i < passion.length(); i++) {

                try {
                    match = ((JSONObject) passion.get(i)).getBoolean("match");
                    passionlist.put(((JSONObject) passion.get(i)).getString("passion"), match);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
            FlexboxLayoutManager fblm = new FlexboxLayoutManager(contentView.getContext(), ROW);
            fblm.setFlexWrap(WRAP);
//                            fblm.setAlignContent(AlignContent.STRETCH);
            fblm.setAlignItems(STRETCH);
            Passionsadapter adapter = new Passionsadapter(passionlist);
            passionsRecylview.setLayoutManager(fblm);
            passionsRecylview.setAdapter(adapter);
            passionsRecylview.setHasFixedSize(true);


        }
    }

}