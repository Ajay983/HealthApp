package com.linkitsoft.dategoal.ui.signup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.linkitsoft.dategoal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnterPassion extends Fragment {


    FloatingActionButton imagebtnback;

    ImageButton next;

    ToggleButton toggleButton;
    ToggleButton toggleButton2;
    ToggleButton toggleButton3;
    ToggleButton toggleButton4;
    ToggleButton toggleButton5;
    ToggleButton toggleButton6;
    ToggleButton toggleButton7;
    ToggleButton toggleButton8;
    ToggleButton toggleButton9;
    ToggleButton toggleButton10;
    ToggleButton toggleButton11;
    ToggleButton toggleButton12;
    ToggleButton toggleButton13;
    ToggleButton toggleButton14;
    ToggleButton toggleButton15;
    ToggleButton toggleButton16;
    ToggleButton toggleButton17;
    boolean status;
    String aj;
    View v;

    private SweetAlertDialog pDialog;
    public Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    SharedPreferences sharedpreferences;


    public EnterPassion() { // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_enter_passion, container, false);

        Activity parent = getActivity();

        imagebtnback = parent.findViewById(R.id.imageButton19);

        next = root.findViewById(R.id.imageButton3);
        List<String> passion;
        passion = new ArrayList<String>();

        context = getContext();


        toggleButton = root.findViewById(R.id.toggleButton);
        toggleButton.setChecked(false);
        toggleButton2 = root.findViewById(R.id.toggleButton2);
        toggleButton2.setChecked(false);
        toggleButton3 = root.findViewById(R.id.toggleButton3);
        toggleButton4 = root.findViewById(R.id.toggleButton4);
        toggleButton5 = root.findViewById(R.id.toggleButton5);
        toggleButton6 = root.findViewById(R.id.toggleButton6);
        toggleButton7 = root.findViewById(R.id.toggleButton7);
        toggleButton8 = root.findViewById(R.id.toggleButton8);
        toggleButton9 = root.findViewById(R.id.toggleButton9);
        toggleButton10 = root.findViewById(R.id.toggleButton10);
        toggleButton11 = root.findViewById(R.id.toggleButton11);
        toggleButton12 = root.findViewById(R.id.toggleButton12);
        toggleButton13 = root.findViewById(R.id.toggleButton13);
        toggleButton14 = root.findViewById(R.id.toggleButton14);
        toggleButton15 = root.findViewById(R.id.toggleButton15);
        toggleButton16 = root.findViewById(R.id.toggleButton16);
        toggleButton17 = root.findViewById(R.id.toggleButton17);


//        if (passion.isEmpty()){
//            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    if (b){
//                        toggleButton.setChecked(false);
//                    }else{
//                        Toast.makeText(parent, "hello", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//
//
//        }


        View.OnClickListener oc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                status = ((ToggleButton) v).isChecked();
                if (status) {


//                    next.setVisibility(View.VISIBLE);
//                    next.setEnabled(true);
                    passion.add(((ToggleButton) v).getText().toString());
                } else {
//                    next.setVisibility(View.GONE);
                    passion.remove(((ToggleButton) v).getText().toString());
                }
                if (!passion.isEmpty()) {
//                    next.setEnabled(true);
//                    next.setVisibility(View.VISIBLE);
                } else {
//                    next.setVisibility(View.INVISIBLE);
                }
            }
        };


        toggleButton.setOnClickListener(oc);
        toggleButton2.setOnClickListener(oc);
        toggleButton3.setOnClickListener(oc);
        toggleButton4.setOnClickListener(oc);
        toggleButton5.setOnClickListener(oc);
        toggleButton6.setOnClickListener(oc);
        toggleButton7.setOnClickListener(oc);
        toggleButton8.setOnClickListener(oc);
        toggleButton9.setOnClickListener(oc);
        toggleButton10.setOnClickListener(oc);
        toggleButton11.setOnClickListener(oc);
        toggleButton12.setOnClickListener(oc);
        toggleButton13.setOnClickListener(oc);
        toggleButton14.setOnClickListener(oc);
        toggleButton15.setOnClickListener(oc);
        toggleButton16.setOnClickListener(oc);
        toggleButton17.setOnClickListener(oc);


//        if (passion.isEmpty()){
//            toggleButton.setEnabled(false);
//            toggleButton2.setEnabled(false);
//            toggleButton3.setEnabled(false);
//            toggleButton4.setEnabled(false);
//            toggleButton5.setEnabled(false);
//            toggleButton6.setEnabled(false);
//            toggleButton7.setEnabled(false);
//            toggleButton8.setEnabled(false);
//            toggleButton9.setEnabled(false);
//            toggleButton10.setEnabled(false);
//            toggleButton11.setEnabled(false);
//            toggleButton12.setEnabled(false);
//            toggleButton13.setEnabled(false);
//            toggleButton14.setEnabled(false);
//            toggleButton15.setEnabled(false);
//            toggleButton16.setEnabled(false);
//            toggleButton17.setEnabled(false);
//
//        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String passionlist = "";

                if (!passion.isEmpty()) {
                    next.setEnabled(true);
//                    next.setVisibility(View.VISIBLE);

                    for (String p : passion) {

                        passionlist += p + ",";

                    }

//                    next.setVisibility(View.VISIBLE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("passionlist", passionlist);
                    editor.apply();
                    ViewPager m = parent.findViewById(R.id.viewPagerhome);
                    m.setCurrentItem(13);
                } else if (toggleButton.isChecked() || toggleButton2.isChecked() || toggleButton3.isChecked()
                        || toggleButton4.isChecked() || toggleButton5.isChecked() || toggleButton6.isChecked() || toggleButton7.isChecked()
                        || toggleButton8.isChecked() || toggleButton9.isChecked() || toggleButton10.isChecked() || toggleButton11.isChecked()
                        || toggleButton12.isChecked() || toggleButton13.isChecked() || toggleButton14.isChecked() || toggleButton15.isChecked()
                        || toggleButton16.isChecked()
                        || toggleButton17.isChecked() && passion.isEmpty()) {
                    showdialog("WARNING", "Reselect Passions", 1);
                    toggleButton.setChecked(false);
                    toggleButton2.setChecked(false);
                    toggleButton3.setChecked(false);
                    toggleButton4.setChecked(false);
                    toggleButton5.setChecked(false);
                    toggleButton6.setChecked(false);
                    toggleButton7.setChecked(false);
                    toggleButton8.setChecked(false);
                    toggleButton9.setChecked(false);
                    toggleButton10.setChecked(false);
                    toggleButton11.setChecked(false);
                    toggleButton12.setChecked(false);
                    toggleButton13.setChecked(false);
                    toggleButton14.setChecked(false);
                    toggleButton15.setChecked(false);
                    toggleButton16.setChecked(false);
                    toggleButton17.setChecked(false);

                } else {


                    showdialog("WARNING", "Atleast Select 5 Passion", 1);

                }


            }
        });

        return root;
    }

    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }


}