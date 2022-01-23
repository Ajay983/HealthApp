package com.linkitsoft.dategoal.ui.signup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.linkitsoft.dategoal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnterBirthday  extends Fragment {


    FloatingActionButton imagebtnback;
    ImageButton next;
    SharedPreferences sharedpreferences;
    EditText edittext;
    Calendar myCalendar;
    Context context;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String datee ="";

    public EnterBirthday() {
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


    int mYear;
    int mMonth;
    int mDay;

    int maxYear;
    int maxMonth;
    int maxDay;

    int minYear;
    int minMonth;
    int minDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_enter_birthday, container, false);

     Activity parent = getActivity();
        context = getContext();


        imagebtnback = parent.findViewById(R.id.imageButton19);

        next = root.findViewById(R.id.imageButton2);

        myCalendar  = Calendar.getInstance();
        mYear = myCalendar.get(Calendar.YEAR);
        mMonth = myCalendar.get(Calendar.MONTH);
        mDay = myCalendar.get(Calendar.DAY_OF_MONTH);

        maxYear = mYear - 18;
        maxMonth = mMonth;
        maxDay = mDay;

        minYear = mYear - 90;
        minMonth = mMonth;
        minDay = mDay;



        edittext= (EditText) root.findViewById(R.id.editextbirthdate);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                if (year > maxYear ||monthOfYear > maxMonth && year == maxYear||
//                        dayOfMonth > maxDay && year == maxYear && monthOfYear == maxMonth){
//
//                    view.updateDate(maxYear, maxMonth, maxDay);
//                    myCalendar.set(Calendar.YEAR, maxYear);
//                    myCalendar.set(Calendar.MONTH, maxMonth);
//                    myCalendar.set(Calendar.DAY_OF_MONTH, maxDay);
//
//                    updateLabel();
//
//                }
//                else if (year < minYear ||monthOfYear < minMonth && year == minYear||
//                        dayOfMonth < minDay && year == minYear && monthOfYear == minMonth) {
//
//                    view.updateDate(minYear, minMonth, minDay);
//                    myCalendar.set(Calendar.YEAR, minYear);
//                    myCalendar.set(Calendar.MONTH, minMonth);
//                    myCalendar.set(Calendar.DAY_OF_MONTH, minDay);
//                    updateLabel();
//                }
//                else {

                    view.updateDate(year, monthOfYear, dayOfMonth);

                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
             //   }
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, month);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }



        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),R.style.MyDatePickerDialogTheme, date, mYear-18, mMonth,
                        mDay).show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if(!datee.equals(""))
                { if(Integer.parseInt(datee.split("/")[2])>=(mYear-18))
                {
                    showdialog("Invalid","You must be at least 18 years old to register for Dategoal.",3);
                }else {
                    // if(datee.split("/"))
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("dob", datee);
                    editor.apply();

                    ViewPager m = parent.findViewById(R.id.viewPagerhome);
                    m.setCurrentItem(4);
                }
                }
                else{
                    showdialog("Error","Must select a date",3);
                }
            }
        });

        return root;
    }



    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datee = sdf.format(myCalendar.getTime());
        edittext.setText(sdf.format(myCalendar.getTime()));
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