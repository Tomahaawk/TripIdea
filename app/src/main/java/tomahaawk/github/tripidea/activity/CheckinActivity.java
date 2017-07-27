package tomahaawk.github.tripidea.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.helper.FirebaseConfig;
import tomahaawk.github.tripidea.helper.Preferences;
import tomahaawk.github.tripidea.model.Checkin;

public class CheckinActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.fab_save_checkin)
    FloatingActionButton fabSaveCheckin;

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean permissionAccesLocationGranted;

    private GoogleApiClient googleApiClient;
    private DatabaseReference databaseReference;

    private final LatLng defaultLocation = new LatLng(0,0);
    private static final int DEFAULT_ZOOM = 10;

    private CameraPosition cameraPosition;
    private GoogleMap gMap;
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        ButterKnife.bind(this);


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();


        fabSaveCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCheckin();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap = googleMap;
        updateLocation();
        getDeviceLocation();
       /* LatLng sydney = new LatLng(-34, 151);
        gMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        permissionAccesLocationGranted = false;
        switch (requestCode) {

            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAccesLocationGranted = true;
                }
        }

        updateLocation();

    }

    private void getDeviceLocation() {

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionAccesLocationGranted = true;

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }

        if(permissionAccesLocationGranted) {
            lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }

        if(cameraPosition != null) {
            gMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } else if (lastKnownLocation != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

        } else {
            Log.d("No location", "Current location is null. Using default");
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
            gMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void updateLocation() {

        if(gMap == null) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionAccesLocationGranted = true;

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }

        if(permissionAccesLocationGranted) {
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(true);

        } else {
            gMap.setMyLocationEnabled(false);
            gMap.getUiSettings().setMyLocationButtonEnabled(false);
            lastKnownLocation = null;
        }
    }

    private void saveCheckin() {

        Preferences preferences = new Preferences(this);
        String currentUserId = preferences.getUserId();
        databaseReference = FirebaseConfig.getDatabaseReference().child("checkins").child(currentUserId);

        Checkin newCheckin = new Checkin();
        newCheckin.setLatitude(lastKnownLocation.getLatitude());
        newCheckin.setLongitude(lastKnownLocation.getLongitude());
        newCheckin.setMessage("novo checkin");

        String checkinId = databaseReference.push().getKey(); //"" + String.valueOf(SystemClock.currentThreadTimeMillis());
        databaseReference.child(checkinId).setValue(newCheckin);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
