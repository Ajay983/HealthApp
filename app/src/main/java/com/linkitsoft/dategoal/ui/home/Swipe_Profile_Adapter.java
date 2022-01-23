package com.linkitsoft.dategoal.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.linkitsoft.dategoal.Models.PassionModel;
import com.linkitsoft.dategoal.Models.PeopleModel;
import com.linkitsoft.dategoal.PortnVariable;
import com.linkitsoft.dategoal.R;
import com.linkitsoft.dategoal.ui.Profile.Bottomsheeet_Profile_more;
import com.linkitsoft.dategoal.ui.Profile.Bottomsheet_profile_compare;
import com.linkitsoft.dategoal.ui.Profile.Bottomsheet_profile_friends;
import com.linkitsoft.dategoal.ui.Profile.Passionsadapter;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.teresaholfeld.stories.StoriesProgressView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static com.google.android.flexbox.AlignItems.STRETCH;
import static com.google.android.flexbox.FlexDirection.ROW;
import static com.google.android.flexbox.FlexWrap.WRAP;

public class Swipe_Profile_Adapter extends RecyclerView.Adapter<Swipe_Profile_Adapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private Swipe_Profile_Adapter.ItemClickListener mClickListener;
    public List<PeopleModel> userModelList;
    Context context;
    FragmentManager manager;
    boolean flag = false;
    boolean ibl = false;
    boolean ifr = false;
    boolean isreq = false;
    SweetAlertDialog pDialog;
    PortnVariable portnVariable;
    public String userid,token;
    public String other_user_id;
    public int count;
    long pressTime = 0L;
    long limit = 500L;
    List<String> list = new ArrayList<String>();
    int counter = 0;
    static int b = 0;

    String profileurl, profileurl2, profileurl3, profileurl4, profileurl5, profileurl6;

    List<PassionModel> passion = null;

    @NonNull
    Swipe_Profile_Adapter.ViewHolder mholder;



    // data is passed into the constructor
    public Swipe_Profile_Adapter(Context context, List<PeopleModel> userModelList, String userid,String token) {
        this.mInflater = LayoutInflater.from(context);
        this.userid = userid;
        this.userModelList = userModelList;
        this.token = token;
    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public Swipe_Profile_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        View view = mInflater.inflate(R.layout.item_swipeable_profile, parent, false);
        context = view.getContext();
        manager = ((AppCompatActivity) context).getSupportFragmentManager();
        portnVariable = new PortnVariable();


        return new Swipe_Profile_Adapter.ViewHolder(view);
    }


    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull Swipe_Profile_Adapter.ViewHolder holder, int position) {
        String name = userModelList.get(position).getName();
        String hc = userModelList.get(position).get_id();
        Date age = userModelList.get(position).getDOB();
        String address = userModelList.get(position).getCity();
        profileurl = userModelList.get(position).getProfilePic();
        profileurl2 = userModelList.get(position).getPhoto2();
        profileurl3 = userModelList.get(position).getPhoto3();
        profileurl4 = userModelList.get(position).getPhoto4();
        profileurl5 = userModelList.get(position).getPhoto5();
        profileurl6 = userModelList.get(position).getPhoto6();
        String mtperc = userModelList.get(position).getMatching();
        boolean match;

        LinkedHashMap<String, Boolean> passionlist = new LinkedHashMap<String, Boolean>();


        passion = userModelList.get(position).getPassions();



        if (passion.size() == 0) {

        } else {


            for (int i = 0; i < passion.size(); i++) {

                match = passion.get(i).getMatch();
                passionlist.put((passion.get(i)).getPassion(), match);

            }

            FlexboxLayoutManager fblm = new FlexboxLayoutManager(context, ROW);
            fblm.setFlexWrap(WRAP);
//                            fblm.setAlignContent(AlignContent.STRETCH);
            fblm.setAlignItems(STRETCH);
            Passionsadapter adapter = new Passionsadapter(passionlist);
            holder.pssions.setLayoutManager(fblm);
            holder.pssions.setAdapter(adapter);
            holder.pssions.setHasFixedSize(true);

        }

        if (profileurl.length() > 0)
        {
            list.add(profileurl);
         //   b = 1;
        }
        if (profileurl2.length() > 0)
        {
            list.add(profileurl2);
          //  b = 1;
        }
        if (profileurl3.length() > 0)
        {
            list.add(profileurl3);
           // b = 1;
        }
        if (profileurl4.length() > 0)
        {
            list.add(profileurl4);
          //  b = 1;
        }
        if (profileurl5.length() > 0)
        {
            list.add(profileurl5);
           // b = 1;
        }
        if (profileurl6.length() > 0)
        {
            list.add(profileurl6);
           // b = 1;
        }
        if (b < 1)
        {
        story_view(list, holder);
        }

     /*   if (profileurl.length() > 0 && profileurl2.length() > 0 && profileurl3.length() > 0 && profileurl4.length() > 0
                && profileurl5.length() > 0 && profileurl6.length() > 0) {
            if (b < 1) {
                list.add(profileurl);
                list.add(profileurl2);
                list.add(profileurl3);
                list.add(profileurl4);
                list.add(profileurl5);
                list.add(profileurl6);
                story_view(list, holder);
                b = 1;

            }


        } else if (profileurl.length() > 0 && profileurl2.length() > 0 && profileurl3.length() > 0 && profileurl4.length() > 0
                && profileurl5.length() > 0) {
            if (b < 1) {
                list.add(profileurl);
                list.add(profileurl2);
                list.add(profileurl3);
                list.add(profileurl4);
                list.add(profileurl5);
                story_view(list, holder);


            }
        } else if (profileurl.length() > 0 && profileurl2.length() < 0 && profileurl3.length() > 0 && profileurl4.length() > 0) {
            if (b < 1) {
                list.add(profileurl);
                list.add(profileurl2);
                list.add(profileurl3);
                list.add(profileurl4);
                story_view(list, holder);
                b = 1;

            }

        } else if (profileurl.length() > 0 && profileurl2.length() > 0 && profileurl3.length() > 0) {
            if (b < 1) {
                list.add(profileurl);
                list.add(profileurl2);
                list.add(profileurl3);
                story_view(list, holder);
                b = 1;

            }


        } else if (profileurl.length() > 0 && profileurl2.length() > 0) {
            if (b < 1) {
                list.add(profileurl);
                list.add(profileurl2);
                story_view(list, holder);
                b = 1;

            }

        } else {
            if (b < 1) {
                list.add(profileurl);
                story_view(list, holder);
                b = 1;

            }

        }
*/

        String eth = userModelList.get(position).getEthinicity();
        String frcnt = userModelList.get(position).getFriends();
        String pid = userModelList.get(position).get_id();


        int vfstatus = userModelList.get(position).getIsVerifified();
        boolean ifriend = userModelList.get(position).getFriend();
        boolean iblock = userModelList.get(position).getBlocked();
        boolean ifvt = userModelList.get(position).getFavorite();
        boolean isre = userModelList.get(position).getRequested();

        mholder = holder;
        ibl = iblock;
        ifr = ifriend;
        isreq = isre;


        // Calendar calendar = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(age);
        int year = calendar.get(Calendar.YEAR);
        //Add one to month {0 - 11}
        /*int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);*/

        Calendar now = Calendar.getInstance();
        now.setTime(new

                Date());
        int cyear = now.get(Calendar.YEAR);
        int agee = 0;

        agee = cyear - year;



        holder.img_swipe.setOnTouchListener(onTouchListener);

        holder.back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.storiesProgressView.reverse();
            }
        });
        holder.back_img.setOnTouchListener(onTouchListener);

        holder.forw_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.storiesProgressView.skip();
            }
        });
        holder.forw_img.setOnTouchListener(onTouchListener);


        holder.nametextview.setText(name);
        holder.ethnictextview.setText(eth);
        holder.frcounttextview.setText(frcnt);
        holder.matchprtextview.setText(mtperc + "%");
        holder.nameinfotextview.setText(name + "'s Info");
        holder.ageaddtextview.setText(agee + " • " + address);
        if (vfstatus == 0) {
            holder.vefifylogo.setVisibility(View.GONE);
        } else holder.vefifylogo.setVisibility(View.VISIBLE);

        if (ifvt) {
            holder.fabh.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heartsel));
            flag = true;
        } else {
            holder.fabh.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart));
            flag = false;
        }


        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bottomsheeet_Profile_more mbottomSheetDialog = Bottomsheeet_Profile_more.newInstance(pid, ibl, flag, ifr,isreq, Swipe_Profile_Adapter.this);
                mbottomSheetDialog.show(manager, "mybtommore");

            }
        });

        holder.fabh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag) {
                    fvrtapi(pid, "remove", holder);
                } else {
                    fvrtapi(pid, "Add", holder);
                }
            }
        });


        holder.friendscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bottomsheet_profile_friends bottomSheetDialog = Bottomsheet_profile_friends.newInstance(pid, name,token);
                bottomSheetDialog.show(manager, "Bottomsheet_profile_friends");

            }
        });

        holder.comparecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bottomsheet_profile_compare bottomSheetDialog = Bottomsheet_profile_compare.newInstance(Integer.parseInt(mtperc), passion, true);
                bottomSheetDialog.show(manager, "Bottomsheet_profile_compare");
            }
        });


    }


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    mholder.storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    mholder.storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    private void story_view(List<String> list_new, Swipe_Profile_Adapter.ViewHolder holder) {
        //  System.out.println("List: " + list_new);
        holder.storiesProgressView.setStoriesCount(list_new.size());
        holder.storiesProgressView.setStoryDuration(8000L);
        b = 0;
        holder.storiesProgressView.startStories();

        Picasso.get().load(list_new.get(0)).fit().centerCrop().networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.img_swipe, new Callback() {
            @Override
            public void onSuccess() {


                holder.storiesProgressView.startStories();
            }

            @Override
            public void onError(Exception e) {
            }
        });

        holder.storiesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
                if (counter < list_new.size()) {

                    counter++;
                    Picasso.get().load(list_new.get(counter)).fit().centerCrop().into(holder.img_swipe);
                }
            }

            @Override
            public void onPrev() {
                if (counter > 0) {
                    counter--;
                    Picasso.get().load(list_new.get(counter)).fit().centerCrop().into(holder.img_swipe);

                }
            }

            @Override
            public void onComplete() {
                counter = 0;
                Picasso.get().load(list_new.get(0)).fit().centerCrop().into(holder.img_swipe);
                holder.storiesProgressView.startStories();


            }
        });


    }


    private void fvrtapi(String pid, String urlname, Swipe_Profile_Adapter.ViewHolder holder) {

//        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.setTitle("Updating favorite...");
//        pDialog.show();


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String uri = portnVariable.com + "favorite/" + urlname;

        JSONObject jsonParam = new JSONObject();
        try {

            jsonParam.put("userID", userid);
            jsonParam.put("favoriteID", pid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONObject requestBody = jsonParam;


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, uri, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //   pDialog.dismissWithAnimation();

                String status = null;
                String msg = "none";


                try {

                    status = response.getString("status");

                    if (status.equals("success")) {

                        if (urlname.equals("Add")) {

                            holder.fabh.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heartsel));
                            flag = true;

                            msg = "User added to favorite";
                        } else {

                            holder.fabh.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart));
                            flag = false;

                            msg = "User removed from favorite";
                        }



                    Configuration croutonConfiguration = new Configuration.Builder()
                            .setDuration(2000).build();
                    // Define custom styles for crouton
                    Style style = new Style.Builder()
                            .setBackgroundColorValue(context.getResources().getColor(R.color.orangeasli, context.getTheme()))
                            .setGravity(Gravity.CENTER)
                            .setConfiguration(croutonConfiguration)
                            .setHeight(300)
                            .setTextColorValue(context.getResources().getColor(R.color.white, context.getTheme())).build();
                    // Display notice with custom style and configuration
                    Crouton.showText((Activity) context, msg, style);
                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error report
                //  System.out.println("XXX Volly Error :" + error);

                pDialog.dismissWithAnimation();
                //showdialog("Error", "Kindly fix internet connection then try again or Contact customer support", 1);
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("authorization","bearer " +token);
                return header;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        requestQueue.add(myReq);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public void setblockfrombottomsheet(Boolean selected) {
        ibl = selected;


    }



    public void setfrndfrombottomsheet(Boolean selected , Boolean selectd) {
        ifr = selected;
        isreq = selectd;
    }

    public void setfvrtfrombottomsheet(Boolean selected) {
        if (selected) {
            mholder.fabh.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heartsel));
            flag = true;
        } else {
            mholder.fabh.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart));
            flag = false;

        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nametextview;
        TextView ageaddtextview;
        TextView nameinfotextview;
        TextView ethnictextview;
        TextView frcounttextview;
        TextView matchprtextview;
        ImageView vefifylogo;
        FloatingActionButton btn;
        FloatingActionButton btn2;
        CardView friendscard;
        CardView comparecard;
        private Context context;
        FloatingActionButton fabh;
        RecyclerView pssions;

        ImageView img_swipe;
        Button back_img, forw_img;
        StoriesProgressView storiesProgressView;

//        SwipeListener swipeListener;


        ViewHolder(View itemView) {

            super(itemView);

            nametextview = itemView.findViewById(R.id.textView21);
            ageaddtextview = itemView.findViewById(R.id.textView24);
            nameinfotextview = itemView.findViewById(R.id.textView25);
            ethnictextview = itemView.findViewById(R.id.textView29);
            frcounttextview = itemView.findViewById(R.id.numberoffriends);
            matchprtextview = itemView.findViewById(R.id.textView26);
            vefifylogo = itemView.findViewById(R.id.imageView5);
            pssions = itemView.findViewById(R.id.grii);

            btn = itemView.findViewById(R.id.floatingActionButton12);
            btn2 = itemView.findViewById(R.id.floatingActionButton10);
            comparecard = itemView.findViewById(R.id.cardView3);
            friendscard = itemView.findViewById(R.id.cardView2);
            fabh = itemView.findViewById(R.id.floatingActionButton13);




            storiesProgressView = itemView.findViewById(R.id.stories);
            img_swipe = itemView.findViewById(R.id.image);
            back_img = itemView.findViewById(R.id.prev);
            forw_img = itemView.findViewById(R.id.forward);
            context = itemView.getContext();

            counter = 0;




            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        Window window = ((Activity) context).getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(((Activity) context).getResources().getColor(R.color.white));
                    }
                    ((Activity) context).finish();


                }
            });


            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    public void setClickListener(Swipe_Profile_Adapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }


    public void showdialog(String title, String content, int type) {

        final SweetAlertDialog sd = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(content);
        sd.show();
    }


}