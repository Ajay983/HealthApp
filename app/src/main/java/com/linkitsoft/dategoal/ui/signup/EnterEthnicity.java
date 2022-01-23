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
import android.widget.Button;
import android.widget.ImageButton;

import com.linkitsoft.dategoal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EnterEthnicity extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    Button american;
    Button asian;
    Button black;
    Button latino;
    Button hawaiian;
    Button white;
    SharedPreferences sharedpreferences;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    String ethnicity;

    public EnterEthnicity() {
        // Required empty public constructor

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
        View root = inflater.inflate(R.layout.activity_enter_ethnicity, container, false);

     Activity parent = getActivity();

        imagebtnback = parent.findViewById(R.id.imageButton19);



        american = root.findViewById(R.id.button4);
        asian = root.findViewById(R.id.button5);
        black = root.findViewById(R.id.button6);
        latino = root.findViewById(R.id.button7);
        hawaiian = root.findViewById(R.id.button8);
        white = root.findViewById(R.id.button9);



        View.OnClickListener oc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ethnicity = ((Button) v).getText().toString();


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("ethnicity", ethnicity);
                editor.apply();


                ViewPager m = parent.findViewById(R.id.viewPagerhome);
                m.setCurrentItem(6);
            }
        };

        american.setOnClickListener(oc);
        asian.setOnClickListener(oc);
        black.setOnClickListener(oc);
        latino.setOnClickListener(oc);
        hawaiian.setOnClickListener(oc);
        white.setOnClickListener(oc);



        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }



}