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

public class EnterSexuality extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    Button stright;
    Button lesbian;
    Button bisexual;
    Button gay;
    Button queer;
    Button lgbtq;
    Button commingout;
    Button others;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    String sexuality;
    SharedPreferences sharedpreferences;

    public EnterSexuality() {
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
        View root = inflater.inflate(R.layout.activity_enter_sexuality, container, false);

     Activity parent = getActivity();

        imagebtnback = parent.findViewById(R.id.imageButton19);

        next = root.findViewById(R.id.imageButton2);

        stright = root.findViewById(R.id.button4);
        lesbian = root.findViewById(R.id.button5);
        bisexual = root.findViewById(R.id.button6);
        gay = root.findViewById(R.id.button7);
        queer = root.findViewById(R.id.button10);
        lgbtq = root.findViewById(R.id.button9);
        commingout = root.findViewById(R.id.button11);
        others = root.findViewById(R.id.button12);


        View.OnClickListener oc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sexuality = ((Button) v).getText().toString();


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("sexuality", sexuality);
                editor.apply();


                ViewPager m = parent.findViewById(R.id.viewPagerhome);
                m.setCurrentItem(7);
            }
        };

        stright.setOnClickListener(oc);
        lesbian.setOnClickListener(oc);
        bisexual.setOnClickListener(oc);
        gay.setOnClickListener(oc);
        queer.setOnClickListener(oc);
        lgbtq.setOnClickListener(oc);
        commingout.setOnClickListener(oc);
        others.setOnClickListener(oc);



        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }


}