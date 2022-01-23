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
import android.widget.EditText;
import android.widget.ImageButton;

import com.linkitsoft.dategoal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnterName extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    EditText nametxt;
    SharedPreferences sharedpreferences;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context context;


    public EnterName() {
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
        View root = inflater.inflate(R.layout.activity_enter_name, container, false);

        Activity parent = getActivity();
        context = getContext();


        nametxt = root.findViewById(R.id.editextname);

        imagebtnback = parent.findViewById(R.id.imageButton19);

        next = root.findViewById(R.id.imageButton2);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regx = "^[\\p{L} .'-]+$";
                if(nametxt.getText().toString().matches(regx)){

                if (!nametxt.getText().toString().isEmpty())
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("name", nametxt.getText().toString());
                    editor.apply();

                    ViewPager m = parent.findViewById(R.id.viewPagerhome);
                    m.setCurrentItem(3);
                }
                else
                    {
                    showdialog("Error","Must enter your name",3);
                }
                }
                else
                {
                    showdialog("Invalid name","Please enter a proper name",3);
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