package tomahaawk.github.tripidea.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tomahaawk.github.tripidea.R;

public class MyCheckinFragment extends Fragment {


    public MyCheckinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab2_my_checkins, container, false);

        return view;
    }
}
