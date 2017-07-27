package tomahaawk.github.tripidea.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.model.Checkin;
import tomahaawk.github.tripidea.model.User;

public class CheckinRecyclerViewAdapter extends RecyclerView.Adapter<CheckinRecyclerViewAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Checkin> checkins;
    private User user;

    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback{
        private View v;
        private GoogleMap gMap;
        private MapView mapView;


        public ViewHolder(View itemView) {
            super(itemView);
            v = itemView;

            mapView = (MapView) itemView.findViewById(R.id.map_lite);

            if(mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

            MapsInitializer.initialize(context);
            gMap = googleMap;

        }
    }

    public CheckinRecyclerViewAdapter (Context context, User user, ArrayList<Checkin> data) {
        this.context = context;
        this.checkins = data;
        this.user = user;
    }

    @Override
    public CheckinRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_checkin, parent, false);

        ViewHolder vh = new ViewHolder(view);


        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CircleImageView profilePicture = (CircleImageView) holder.v.findViewById(R.id.user_pic_checkin_layout);
        TextView userName = (TextView) holder.v.findViewById(R.id.user_name_checkin_layout);

        Checkin checkin = checkins.get(position);

        userName.setText(user.getName());
        Picasso.with(context).load(user.getPhotoUrl()).noFade().into(profilePicture);

        GoogleMap thisMap = holder.gMap;
        if(thisMap != null) {
            thisMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(checkin.getLatitude(),checkin.getLongitude()), 10) );

        }

    }

    @Override
    public int getItemCount() {

        return checkins.size();
    }

}
