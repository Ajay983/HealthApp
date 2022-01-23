package com.linkitsoft.dategoal.ui.signup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;import android.app.Activity;
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

public class EnterRGoal  extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    EditText relationgoal;
    EditText dreamdate;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    String relgoal = "";
    String dreamdt = "";
    SharedPreferences sharedpreferences;





    public EnterRGoal(){    // Required empty public constructor


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
        View root = inflater.inflate(R.layout.activity_enter_r_goal, container, false);

        Activity parent = getActivity();

        imagebtnback = root.findViewById(R.id.imageButton19);

        relationgoal = root.findViewById(R.id.rgoal);
        dreamdate = root.findViewById(R.id.ddate);

        next = root.findViewById(R.id.imageButton3);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!relationgoal.getText().toString().isEmpty())
                {
                    relgoal = relationgoal.getText().toString();
                }

                if(!dreamdate.getText().toString().isEmpty())
                {
                    dreamdt = dreamdate.getText().toString();
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("dreamdt", dreamdt);
                editor.putString("relgoal", relgoal);
                editor.apply();


                ViewPager m = parent.findViewById(R.id.viewPagerhome);
                m.setCurrentItem(12);
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