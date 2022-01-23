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

public class EnterReligionYes  extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    Button christ;
    Button judasm;
    Button hindu;
    Button buddh;
    Button islam;
    Button daoism;
    Button freesprit;
    Button others;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    String religion;
    SharedPreferences sharedpreferences;


    public EnterReligionYes(){ // Required empty public constructor

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
        View root = inflater.inflate(R.layout.activity_enter_religion_yes, container, false);

        Activity parent = getActivity();

        imagebtnback = parent.findViewById(R.id.imageButton19);

        next = root.findViewById(R.id.imageButton2);
        christ = root.findViewById(R.id.button4);
        judasm = root.findViewById(R.id.button5);
        hindu = root.findViewById(R.id.button6);
        buddh = root.findViewById(R.id.button7);
        islam = root.findViewById(R.id.button10);
        daoism = root.findViewById(R.id.button9);
        freesprit = root.findViewById(R.id.button11);
        others = root.findViewById(R.id.button12);

        View.OnClickListener oc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                religion = ((Button) v).getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("religion", religion);
                editor.apply();

                ViewPager m = parent.findViewById(R.id.viewPagerhome);
                m.setCurrentItem(10);
            }
        };

        christ.setOnClickListener(oc);
        judasm.setOnClickListener(oc);
        hindu.setOnClickListener(oc);
        buddh.setOnClickListener(oc);
        islam.setOnClickListener(oc);
        daoism.setOnClickListener(oc);
        freesprit.setOnClickListener(oc);
        others.setOnClickListener(oc);



        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }

}