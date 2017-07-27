package tomahaawk.github.tripidea.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.activity.LoginActivity;
import tomahaawk.github.tripidea.helper.Base64Converter;
import tomahaawk.github.tripidea.helper.FirebaseConfig;
import tomahaawk.github.tripidea.helper.Preferences;
import tomahaawk.github.tripidea.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.collapsingtb_layout_id)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.tv_profile_name)
    TextView tvProfileName;
    @BindView(R.id.civ_profile_picture)
    CircleImageView profilePicture;
    @BindView(R.id.tv_logout) TextView tvLogout;

    private ValueEventListener eventListener;
    private User user;
    private String userId;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Preferences sharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        sharedPreferences = new Preferences(getActivity());
        userId = sharedPreferences.getUserId();

        databaseReference = FirebaseConfig.getDatabaseReference().child("users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null) {
                   user = dataSnapshot.getValue(User.class);
                   tvProfileName.setText(user.getName());
                   Picasso.with(getContext()).load(user.getPhotoUrl()).noFade().into(profilePicture);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Typeface font = Typer.set(getContext()).getFont(Font.ROBOTO_MEDIUM);
        tvProfileName.setTypeface(font);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logOut();
            }
        });


        return view;
    }

    private void logOut() {

        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

}
