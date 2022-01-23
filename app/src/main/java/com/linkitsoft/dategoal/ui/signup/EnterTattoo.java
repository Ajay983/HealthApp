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

public class EnterTattoo extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button yes;
    Button no;


    boolean tatto;
    SharedPreferences sharedpreferences;





    public EnterTattoo()
    {  // Required empty public constructor


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
        View root = inflater.inflate(R.layout.activity_enter_tattoo, container, false);

     Activity parent = getActivity();

        imagebtnback = parent.findViewById(R.id.imageButton19);

        next = root.findViewById(R.id.imageButton2);
        yes = root.findViewById(R.id.button4);
        no = root.findViewById(R.id.button5);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tatto = true;
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("tatto", tatto);
                editor.apply();
                ViewPager m = parent.findViewById(R.id.viewPagerhome);
                m.setCurrentItem(11);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tatto = false;
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("tatto", tatto);
                editor.apply();
                ViewPager m = parent.findViewById(R.id.viewPagerhome);
                m.setCurrentItem(11);
            }
        });




        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedpreferences = context.getSharedPreferences("MyPrefs", 0);
    }


}