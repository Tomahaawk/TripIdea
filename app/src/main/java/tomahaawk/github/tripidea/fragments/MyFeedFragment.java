package tomahaawk.github.tripidea.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tomahaawk.github.tripidea.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFeedFragment extends Fragment {


    public MyFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1_my_feed, container, false);

        TextView textView = (TextView) view.findViewById(R.id.textView2);

        return view;
    }

}
