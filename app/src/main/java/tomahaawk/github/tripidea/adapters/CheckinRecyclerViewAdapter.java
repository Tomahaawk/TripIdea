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

import butterknife.BindView;
import butterknife.ButterKnife;
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

        @BindView(R.id.user_pic_checkin_layout) CircleImageView profilePicture;
        @BindView(R.id.user_name_checkin_layout) TextView userName;
        @BindView(R.id.map_lite) MapView mapView;


        public ViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ButterKnife.bind(this, v);

            initializeMapView();

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

            MapsInitializer.initialize(context);
            gMap = googleMap;
            mapView.onResume();
            notifyDataSetChanged();

        }

        public void initializeMapView() {
            if(mapView != null) {
                mapView.onCreate(null);
                mapView.getMapAsync(this);
            }
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Checkin checkin = checkins.get(position);

        holder.userName.setText(this.user.getName());
        Picasso.with(context).load(this.user.getPhotoUrl()).noFade().into(holder.profilePicture);

        LatLng latLng = new LatLng(checkin.getLatitude(),checkin.getLongitude());

        if(holder.gMap != null) {
            holder.gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10) );

        }

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {

            if(holder != null && holder.gMap != null) {
                holder.gMap.clear();
            }
    }



    @Override
    public int getItemCount() {

        return checkins.size();
    }

}
