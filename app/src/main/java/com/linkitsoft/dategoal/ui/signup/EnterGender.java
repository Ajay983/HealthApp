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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnterGender  extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    Button male;
    Button female;
    Button trans;
    SharedPreferences sharedpreferences;
    Context context;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    String gender;

    public EnterGender() {
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
        View root = inflater.inflate(R.layout.activity_enter_gender, container, false);
        Activity parent = getActivity();
        context = getContext();


        imagebtnback = parent.findViewById(R.id.imageButton19);

        male = root.findViewById(R.id.button4);
        female =root.findViewById(R.id.button5);
        trans = root.findViewById(R.id.button6);


        View.OnClickListener oc = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gender = ((Button) v).getText().toString();


                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("gender", gender);
                editor.apply();

                ViewPager m = parent.findViewById(R.id.viewPagerhome);
                m.setCurrentItem(5);

            }
        };

        male.setOnClickListener(oc);
        female.setOnClickListener(oc);
        trans.setOnClickListener(oc);


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