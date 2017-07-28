package tomahaawk.github.tripidea.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.adapters.CheckinRecyclerViewAdapter;
import tomahaawk.github.tripidea.helper.FirebaseConfig;
import tomahaawk.github.tripidea.helper.Preferences;
import tomahaawk.github.tripidea.model.Checkin;
import tomahaawk.github.tripidea.model.User;

public class MyCheckinFragment extends Fragment {

    @BindView(R.id.rv_my_checkins)
    RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;

    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;

    private ArrayList<Checkin> userCheckinsList;
    private String userId;
    private String userName;
    private String userPhotoUrl;
    private User currentUser;


    public MyCheckinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab2_my_checkins, container, false);
        ButterKnife.bind(this, view);

        userCheckinsList = new ArrayList<>();

        Preferences sharedPreferences = new Preferences(getContext());
        userId = sharedPreferences.getUserId();
        userName = sharedPreferences.getUserName();
        userPhotoUrl = sharedPreferences.getUserPhotourl();

        currentUser = new User();
        currentUser.setPhotoUrl(userPhotoUrl);
        currentUser.setName(userName);

        getUserCheckins();

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //getCurrentUser();

        recyclerViewAdapter = new CheckinRecyclerViewAdapter(getContext(), currentUser, userCheckinsList);
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }

    private void getUserCheckins() {

        databaseReference = FirebaseConfig.getDatabaseReference().child("checkins").child(userId);
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userCheckinsList.clear();

                if(dataSnapshot != null && dataSnapshot.exists()) {

                    GenericTypeIndicator<HashMap<String,Checkin>> typeIndicator = new GenericTypeIndicator<HashMap<String,Checkin>>() {};
                    HashMap<String,Checkin> checkinHashMap = dataSnapshot.getValue(typeIndicator);
                    Collection<Checkin> checkinItems = checkinHashMap.values();

                    userCheckinsList.addAll(checkinItems);
                    
                }

                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(eventListener);
    }

    private void getCurrentUser() {

            databaseReference = FirebaseConfig.getDatabaseReference().child("users").child(userId);
            ValueEventListener userEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        currentUser = dataSnapshot.getValue(User.class);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            databaseReference.addListenerForSingleValueEvent(userEventListener);



    }

}
