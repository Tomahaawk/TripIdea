package tomahaawk.github.tripidea.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.adapters.ViewPagerAdapter;
import tomahaawk.github.tripidea.helper.FirebaseConfig;


public class FriendsFragment extends Fragment {

    @BindView(R.id.nameFriend)
    EditText friendName;
    @BindView(R.id.listviewFriends)
    ListView listViewFriends;
    @BindView(R.id.searchFriends)
    Button searchFriends;

    private DatabaseReference databaseReference;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);

        databaseReference = FirebaseConfig.getDatabaseReference().child("users");

        searchFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String queryString = friendName.getText().toString();

                //searchForUsers(queryString);

            }
        });

        return view;
    }

    private void searchForUsers(String email) {

        databaseReference.startAt(email).orderByChild("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null && dataSnapshot.exists()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
