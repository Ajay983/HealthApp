package com.linkitsoft.dategoal.ui.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.linkitsoft.dategoal.R;


public class homechild extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homechild() {
        // Required empty public constructor
    }
    ColorStateList def;
    TextView item1;
    TextView item2;
    TextView item3;

    TextView select;

    ViewPager viewPager;

    public void initcustomtab(View root)
    {
        item1 = root.findViewById(R.id.item1);
        item2 = root.findViewById(R.id.item2);
        item3 = root.findViewById(R.id.item3);
        item1.setOnClickListener(myoc);
        item2.setOnClickListener(myoc);
        item3.setOnClickListener(myoc);
        select = root.findViewById(R.id.select);
        def = item2.getTextColors();
    }



    View.OnClickListener myoc = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.item1){
                select.animate().x(0).setDuration(100);
                item1.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
                item2.setTextColor(def);
                item3.setTextColor(def);
                viewPager.setCurrentItem(0);
            } else if (view.getId() == R.id.item2){
                item1.setTextColor(def);
                item2.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
                item3.setTextColor(def);
                int size = item2.getWidth();
                select.animate().x(size).setDuration(100);
                viewPager.setCurrentItem(1);


                if (Build.VERSION.SDK_INT >= 21) {
                    Window window =  getActivity().getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor( getContext().getResources().getColor(R.color.white));
                }


            } else if (view.getId() == R.id.item3){

                startActivity(new Intent(getActivity(),Swipe.class));
                getActivity().overridePendingTransition(R.anim.lefttrigh, R.anim.righttoleft);



            }
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homechild.
     */
    // TODO: Rename and change types and number of parameters
    public static homechild newInstance(String param1, String param2) {
        homechild fragment = new homechild();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View root = inflater.inflate(R.layout.fragment_homechild, container, false);

        viewPager = root.findViewById(R.id.viewPagerhomechild);
        final homefragmentadapter adapter = new homefragmentadapter(getContext(),getParentFragmentManager(),2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    select.animate().x(0).setDuration(100);
                    item1.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
                    item2.setTextColor(def);
                    item3.setTextColor(def);
                  //  viewPager.setCurrentItem(0);
                } else if (position==1){
                    item1.setTextColor(def);
                    item2.setTextColor(getResources().getColor(R.color.orangeasli,getContext().getTheme()));
                    item3.setTextColor(def);
                    int size = item2.getWidth();
                    select.animate().x(size).setDuration(100);
                   // viewPager.setCurrentItem(1);


                    if (Build.VERSION.SDK_INT >= 21) {
                        Window window =  getActivity().getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor( getContext().getResources().getColor(R.color.white));
                    }


                } else if (position==2){

                    startActivity(new Intent(getActivity(),Swipe.class));
                    getActivity().overridePendingTransition(R.anim.lefttrigh, R.anim.righttoleft);



                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initcustomtab(root);

        return  root;
    }
}