package tomahaawk.github.tripidea.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.activity.MainActivity;
import tomahaawk.github.tripidea.activity.SearchActivity;
import tomahaawk.github.tripidea.fragments.FriendsFragment;
import tomahaawk.github.tripidea.fragments.UserFoundProfileFragment;
import tomahaawk.github.tripidea.helper.Preferences;
import tomahaawk.github.tripidea.model.Friend;
import tomahaawk.github.tripidea.model.FriendStatus;
import tomahaawk.github.tripidea.model.User;


public class UsersFoundRecyclerViewAdapter extends RecyclerView.Adapter<UsersFoundRecyclerViewAdapter.ViewHolder> {

    private DatabaseReference databaseReference;
    private Context context;
    private View v;
    private ArrayList<User> users;
    private ArrayList<String> usersId;


    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.cv_profile_photo_users_found)
        CircleImageView profilePhoto;

        @BindView(R.id.tv_user_name_users_found)
        TextView userName;

        @BindView(R.id.cl_user_found)
        ConstraintLayout userFound;

        public ViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            ButterKnife.bind(this, v);

        }
    }

    public UsersFoundRecyclerViewAdapter(Context context, ArrayList<User> users, ArrayList<String> userId, DatabaseReference reference) {
        this.databaseReference = reference;
        this.context = context;
        this.users = users;
        this.usersId = userId;
    }

    @Override
    public UsersFoundRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_users_found, parent, false);

        ViewHolder vh = new ViewHolder(view);

        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final User user = users.get(position);
        holder.userName.setText(user.getName());
        Picasso.with(context).load(user.getPhotoUrl()).noFade().into(holder.profilePhoto);

        final String userFoundId = usersId.get(position);

        holder.userFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = UserFoundProfileFragment.newInstance(userFoundId, user.getName());

                FragmentManager fragmentManager = ((SearchActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.addToBackStack("UserFound");
                ft.add(R.id.fl_activiy_search, fragment);
                ft.commit();

                //Friend newFriend = newFriend(user, userFoundId);

                //databaseReference.child(userFoundId).child("friends").push().setValue(newFriend);

            }
        });

    }

    @Override
    public int getItemCount() {

        return users.size();
    }


    private Friend newFriend(User user, String userId) {

        Friend newFriend = new Friend();
        newFriend.setFriendStatus(FriendStatus.PENDING);
        newFriend.setName(user.getName());
        newFriend.setPhotoUrl(user.getPhotoUrl());
        newFriend.setUserId(userId);

        return newFriend;
    }




}
