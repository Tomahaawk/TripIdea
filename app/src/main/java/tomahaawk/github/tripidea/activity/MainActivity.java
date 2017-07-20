package tomahaawk.github.tripidea.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.fragments.CheckinFragment;
import tomahaawk.github.tripidea.fragments.ProfileFragment;
import tomahaawk.github.tripidea.fragments.TripsFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fab_checkin_id) FloatingActionButton fabCheckin;
    @BindView(R.id.fab_trips_id) FloatingActionButton fabTrips;
    @BindView(R.id.bottom_nav_id) BottomNavigationView bottomNavigationView;

    boolean defaultFabHide = false;

    FragmentTransaction ft = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Fragment checkinFragment = new CheckinFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_id, checkinFragment).commit();


        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.checkins:
                        changeFragment(0);
                        break;
                    case R.id.trips:
                        changeFragment(1);
                        break;
                    case R.id.profile:
                        changeFragment(2);
                        break;
                }
                return true;
            }
        });


    }

    private void changeFragment(int position) {

        Fragment fragment = null;

        if (position == 0) {
            fragment = new CheckinFragment();
            fabTransition(position);

        } else if (position == 1) {
            fragment = new TripsFragment();
            fabTransition(position);

        } else if (position == 2) {
            fragment = new ProfileFragment();
            fabTransition(position);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_id, fragment).commit();
    }

    private void fabTransition(int position) {


        switch (position) {
            case 0:
                if(defaultFabHide) {
                    fabCheckin.show();
                    defaultFabHide = false;
                }
                fabTrips.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        fabCheckin.show();
                    }
                });
                break;

            case 1:
                if(defaultFabHide) {
                    fabTrips.show();
                    defaultFabHide = false;
                }
                fabCheckin.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        super.onHidden(fab);
                        fabTrips.show();
                    }
                });
                break;

            default:
                fabTrips.hide();
                fabCheckin.hide();
                defaultFabHide = true;
        }

    }

}
