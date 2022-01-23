package com.linkitsoft.dategoal.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.linkitsoft.dategoal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SignupPager extends AppCompatActivity {
    ViewPager viewPager;

    SharedPreferences sharedpreferences;
    Context context;
    Fragment fragment;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_pager);
        viewPager = findViewById(R.id.viewPagerhome);

        View view1 = findViewById(R.id.view);
        View view2 = findViewById(R.id.view2);
        View view3 = findViewById(R.id.view3);
        View view4 = findViewById(R.id.view4);
        View view5 = findViewById(R.id.view5);
        View view6 = findViewById(R.id.view6);
        View view7 = findViewById(R.id.view7);
        View view8 = findViewById(R.id.view8);
        sharedpreferences = getSharedPreferences("MyPrefs", 0);

        viewPager.setAdapter(new pageradapter(this, getSupportFragmentManager(), 14));
        viewPager.setCurrentItem(0);
        FloatingActionButton imagebtnback = findViewById(R.id.imageButton19);




//        mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
//        FragmentTransaction ft = mFragmentManager.beginTransaction();
//        ft.addToBackStack("tag of fragment");
//        ft.add(fragment, "your container id");
//        ft.commit();

        imagebtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean rel_check = sharedpreferences.getBoolean("religionyes",false);

                if (viewPager.getCurrentItem() <= 0) finish();




                else {


                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);



                }


            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {

                switch (position) {
                    case 1:
                        if (sharedpreferences.getString("mobnum", "null").equals("null")
                        ){
                            viewPager.setCurrentItem(0);
                        } else {
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#FF957B"));
                            view3.setBackgroundColor(Color.parseColor("#FF957B"));
                            view4.setBackgroundColor(Color.parseColor("#FF957B"));
                            view5.setBackgroundColor(Color.parseColor("#FF957B"));
                            view6.setBackgroundColor(Color.parseColor("#FF957B"));
                            view7.setBackgroundColor(Color.parseColor("#FF957B"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));
                        }
                        return;
                    case 2:
                        if (sharedpreferences.getString("verify", "null").equals("null")) {
                            viewPager.setCurrentItem(1);
                        } else {
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#FF957B"));
                            view3.setBackgroundColor(Color.parseColor("#FF957B"));
                            view4.setBackgroundColor(Color.parseColor("#FF957B"));
                            view5.setBackgroundColor(Color.parseColor("#FF957B"));
                            view6.setBackgroundColor(Color.parseColor("#FF957B"));
                            view7.setBackgroundColor(Color.parseColor("#FF957B"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));
                        }
                        return;
                    case 3:
                        if (sharedpreferences.getString("name", "null").equals("null")) {
                            viewPager.setCurrentItem(2);
                        } else {
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#ffffff"));
                            view3.setBackgroundColor(Color.parseColor("#FF957B"));
                            view4.setBackgroundColor(Color.parseColor("#FF957B"));
                            view5.setBackgroundColor(Color.parseColor("#FF957B"));
                            view6.setBackgroundColor(Color.parseColor("#FF957B"));
                            view7.setBackgroundColor(Color.parseColor("#FF957B"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));
                        }
                        return;
                    case 4:
                        if (sharedpreferences.getString("dob", "null").equals("null")) {
                            viewPager.setCurrentItem(3);
                        } else {
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#ffffff"));
                            view3.setBackgroundColor(Color.parseColor("#ffffff"));
                            view4.setBackgroundColor(Color.parseColor("#FF957B"));
                            view5.setBackgroundColor(Color.parseColor("#FF957B"));
                            view6.setBackgroundColor(Color.parseColor("#FF957B"));
                            view7.setBackgroundColor(Color.parseColor("#FF957B"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));
                        }
                        return;
                    case 5:
                        if (sharedpreferences.getString("gender", "null").equals("null")) {
                            viewPager.setCurrentItem(4);
                        } else {
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#ffffff"));
                            view3.setBackgroundColor(Color.parseColor("#ffffff"));
                            view4.setBackgroundColor(Color.parseColor("#ffffff"));
                            view5.setBackgroundColor(Color.parseColor("#FF957B"));
                            view6.setBackgroundColor(Color.parseColor("#FF957B"));
                            view7.setBackgroundColor(Color.parseColor("#FF957B"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));
                        }

                        return;
                    case 6:
                        if (sharedpreferences.getString("ethnicity", "null").equals("null")) {
                            viewPager.setCurrentItem(5);
                        } else {
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#ffffff"));
                            view3.setBackgroundColor(Color.parseColor("#ffffff"));
                            view4.setBackgroundColor(Color.parseColor("#ffffff"));
                            view5.setBackgroundColor(Color.parseColor("#ffffff"));
                            view6.setBackgroundColor(Color.parseColor("#FF957B"));
                            view7.setBackgroundColor(Color.parseColor("#FF957B"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));
                        }
                        return;
                    case 7:
                        if (sharedpreferences.getString("sexuality", "null").equals("null")) {
                            viewPager.setCurrentItem(6);
                        } else {
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#ffffff"));
                            view3.setBackgroundColor(Color.parseColor("#ffffff"));
                            view4.setBackgroundColor(Color.parseColor("#ffffff"));
                            view5.setBackgroundColor(Color.parseColor("#ffffff"));
                            view6.setBackgroundColor(Color.parseColor("#FF957B"));
                            view7.setBackgroundColor(Color.parseColor("#FF957B"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));
                        }
                        return;
                    case 8:
                        if (sharedpreferences.getString("herefor", "null").equals("null")) {
                            viewPager.setCurrentItem(7);
                        } else {
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#ffffff"));
                            view3.setBackgroundColor(Color.parseColor("#ffffff"));
                            view4.setBackgroundColor(Color.parseColor("#ffffff"));
                            view5.setBackgroundColor(Color.parseColor("#ffffff"));
                            view6.setBackgroundColor(Color.parseColor("#ffffff"));
                            view7.setBackgroundColor(Color.parseColor("#FF957B"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));
                        }
                        return;
                    case 9:
                        if (sharedpreferences.getString("rel","null").equals("no")){
                            viewPager.setCurrentItem(8);

                        }else {
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#ffffff"));
                            view3.setBackgroundColor(Color.parseColor("#ffffff"));
                            view4.setBackgroundColor(Color.parseColor("#ffffff"));
                            view5.setBackgroundColor(Color.parseColor("#ffffff"));
                            view6.setBackgroundColor(Color.parseColor("#ffffff"));
                            view7.setBackgroundColor(Color.parseColor("#FF957B"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));


                        }





                        return;
                    case 10:


                        view1.setBackgroundColor(Color.parseColor("#ffffff"));
                        view2.setBackgroundColor(Color.parseColor("#ffffff"));
                        view3.setBackgroundColor(Color.parseColor("#ffffff"));
                        view4.setBackgroundColor(Color.parseColor("#ffffff"));
                        view5.setBackgroundColor(Color.parseColor("#ffffff"));
                        view6.setBackgroundColor(Color.parseColor("#ffffff"));
                        view7.setBackgroundColor(Color.parseColor("#FF957B"));
                        view8.setBackgroundColor(Color.parseColor("#FF957B"));

                        return;
                    case 11:

                        view1.setBackgroundColor(Color.parseColor("#ffffff"));
                        view2.setBackgroundColor(Color.parseColor("#ffffff"));
                        view3.setBackgroundColor(Color.parseColor("#ffffff"));
                        view4.setBackgroundColor(Color.parseColor("#ffffff"));
                        view5.setBackgroundColor(Color.parseColor("#ffffff"));
                        view6.setBackgroundColor(Color.parseColor("#ffffff"));
                        view7.setBackgroundColor(Color.parseColor("#ffffff"));
                        view8.setBackgroundColor(Color.parseColor("#FF957B"));

                        return;
                    case 12:
                        if (sharedpreferences.getString("dreamdt", "null").equals("null")) {
                            viewPager.setCurrentItem(11);
                        } else {
                            synchronized (viewPager) {
                                viewPager.notifyAll();
                                viewPager.notify();
                            }
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#ffffff"));
                            view3.setBackgroundColor(Color.parseColor("#ffffff"));
                            view4.setBackgroundColor(Color.parseColor("#ffffff"));
                            view5.setBackgroundColor(Color.parseColor("#ffffff"));
                            view6.setBackgroundColor(Color.parseColor("#ffffff"));
                            view7.setBackgroundColor(Color.parseColor("#ffffff"));
                            view8.setBackgroundColor(Color.parseColor("#FF957B"));
                        }
                        return;
                    case 13:
                        if (sharedpreferences.getString("passionlist", "null").equals("null")) {
                            viewPager.setCurrentItem(12);
                        } else {
                            synchronized (viewPager) {
                                viewPager.notifyAll();
                                viewPager.notify();
                            }
                            view1.setBackgroundColor(Color.parseColor("#ffffff"));
                            view2.setBackgroundColor(Color.parseColor("#ffffff"));
                            view3.setBackgroundColor(Color.parseColor("#ffffff"));
                            view4.setBackgroundColor(Color.parseColor("#ffffff"));
                            view5.setBackgroundColor(Color.parseColor("#ffffff"));
                            view6.setBackgroundColor(Color.parseColor("#ffffff"));
                            view7.setBackgroundColor(Color.parseColor("#ffffff"));
                            view8.setBackgroundColor(Color.parseColor("#ffffff"));

                        }
                        return;
                    default:
                        return;

                }
            }
        });

    }


    @Override
    public void onDestroy() {

        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();


        super.onDestroy();

    }

    @Override
    public void onBackPressed() {


        if (viewPager.getCurrentItem() > 0) {
//            if (mFragmentManager.getBackStackEntryCount() > 0)
//                mFragmentManager.popBackStackImmediate();
//            else super.onBackPressed();
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            finish();
        }

//        new AlertDialog.Builder(this)
//                .setTitle("Really Exit?")
//                .setMessage("Are you sure you want to exit?")
//                .setNegativeButton(android.R.string.no, null)
//                .setPositiveButton(android.R.string.yes, new OnClickListener() {
//
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        WelcomeActivity.super.onBackPressed();
//                    }
//                }).create().show();
    }


}