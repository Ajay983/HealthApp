package com.linkitsoft.dategoal.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.linkitsoft.dategoal.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private View view;

    private FragmentActivity myContext;

    public MapWindowAdapter(FragmentActivity aContext) {
        this.myContext = aContext;

        LayoutInflater inflater = (LayoutInflater) myContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.info_bubble,
                null);
    }

    @Override
    public View getInfoContents(Marker marker) {

        if (marker != null
                && marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {

        final String title = marker.getTitle();
        final TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            titleUi.setText(title);
        } else {
            titleUi.setText("");
            titleUi.setVisibility(View.GONE);
        }

        final String snippet = marker.getSnippet();
        final TextView snippetUi = ((TextView) view
                .findViewById(R.id.snippet));
        final CircleImageView imageview = view
                .findViewById(R.id.profile_image);

        if (snippet != null) {

            String[] SnippetArray = snippet.split(",");
            snippetUi.setText(SnippetArray[0]);
           // imageview.setImageURI(Uri.parse());
            Picasso.get().load(SnippetArray[1]).into(imageview);
        } else {
            snippetUi.setText("");
        }


        return view;
    }
}