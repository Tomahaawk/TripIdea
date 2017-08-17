package tomahaawk.github.tripidea.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.adapters.CheckinRecyclerViewAdapter;
import tomahaawk.github.tripidea.helper.FirebaseConfig;
import tomahaawk.github.tripidea.model.Checkin;
import tomahaawk.github.tripidea.model.User;


public class UserFoundProfileFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;

    private ArrayList<Checkin> userCheckinsList;

    private static final String ARG_PARAM1 = "USER_FOUND_ID";
    private static final String ARG_PARAM2 = "USER_FOUND_NAME";

    private String userFoundName;
    private String userFoundId;
    private User user;

    @BindView(R.id.rv_friend_profile)
    RecyclerView recyclerView;

    public UserFoundProfileFragment() {
        // Required empty public constructor
    }

    public static UserFoundProfileFragment newInstance(String userFoundId, String userFoundName) {
        UserFoundProfileFragment fragment = new UserFoundProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userFoundId);
        args.putString(ARG_PARAM2, userFoundName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideKeyboard();

        if (getArguments() != null) {
            userFoundId = getArguments().getString(ARG_PARAM1);
            userFoundName = getArguments().getString(ARG_PARAM2);

            userCheckinsList = new ArrayList<>();
            getUserCheckins();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_user_found_profile, container, false);
        ButterKnife.bind(this, view);

        getUserFound();
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        user = new User();
        user.setName(userFoundName);

        recyclerViewAdapter = new CheckinRecyclerViewAdapter(getContext(), user, userCheckinsList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void getUserFound() {

        databaseReference = FirebaseConfig.getDatabaseReference().child("users").child(userFoundId);
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null && dataSnapshot.exists()) {
                   user = dataSnapshot.getValue(User.class);
                }

                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(eventListener);
    }

    private void getUserCheckins() {

        databaseReference = FirebaseConfig.getDatabaseReference().child("checkins").child(userFoundId);
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

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
