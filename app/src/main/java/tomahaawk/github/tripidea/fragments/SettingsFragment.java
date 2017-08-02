package tomahaawk.github.tripidea.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.activity.LoginActivity;
import tomahaawk.github.tripidea.helper.FirebaseConfig;


public class SettingsFragment extends Fragment {

    @BindView(R.id.tv_logout) TextView tvLogout;

    private FirebaseAuth firebaseAuth;


    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_profile_content, container, false);
        ButterKnife.bind(this, view);



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
