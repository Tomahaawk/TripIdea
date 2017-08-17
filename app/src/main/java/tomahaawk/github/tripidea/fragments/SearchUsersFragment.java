package tomahaawk.github.tripidea.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import tomahaawk.github.tripidea.adapters.UsersFoundRecyclerViewAdapter;
import tomahaawk.github.tripidea.helper.FirebaseConfig;
import tomahaawk.github.tripidea.model.User;


public class SearchUsersFragment extends Fragment {

    @BindView(R.id.rv_users_found)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar_search)
    Toolbar toolbar;

    @BindView(R.id.sv_search)
    SearchView searchView;

    private DatabaseReference databaseReference;
    private ArrayList<User> userArrayList;
    private ArrayList<String> userKeys;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_users, container, false);
        ButterKnife.bind(this, view);

        databaseReference = FirebaseConfig.getDatabaseReference().child("users");
        userArrayList = new ArrayList<>();
        userKeys = new ArrayList<>();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Pesquisar...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //searchForUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchForUsers(newText);
                return true;
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerViewAdapter = new UsersFoundRecyclerViewAdapter(getContext(), userArrayList, userKeys, databaseReference);
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }

    private void searchForUsers(String email) {

        databaseReference.orderByChild("email").startAt(email).endAt(email+"\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userArrayList.clear();
                userKeys.clear();

                if(dataSnapshot != null && dataSnapshot.exists()) {

                    GenericTypeIndicator<HashMap<String,User>> typeIndicator = new GenericTypeIndicator<HashMap<String,User>>() {};
                    HashMap<String,User> userHashMap = dataSnapshot.getValue(typeIndicator);
                    Collection<User> usersFound = userHashMap.values();

                    userArrayList.addAll(usersFound);
                    userKeys.addAll(userHashMap.keySet());

                }

                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                hideKeyboard();
                break;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void swapFragment(String userId);
    }
}
