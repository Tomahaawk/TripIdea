package tomahaawk.github.tripidea.activity;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import tomahaawk.github.tripidea.R;
import tomahaawk.github.tripidea.helper.Base64Converter;
import tomahaawk.github.tripidea.helper.FirebaseConfig;
import tomahaawk.github.tripidea.helper.Preferences;
import tomahaawk.github.tripidea.model.User;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    @BindView(R.id.bt_login_id) SignInButton bt_login;
    @BindView(R.id.imageView2)
    ImageView backGroundImage;

    private DatabaseReference userReference;
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient mGoogleApiClient;

    private User newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loadBackgroundImage();

        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        verifyUserAlreadyAuthenticated(firebaseAuth);

        userReference = FirebaseConfig.getDatabaseReference().child("users");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if(result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);

        } else {

           /*TODO*/
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {


        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {

                            instantiateUser(account);

                            newUser.setId(firebaseAuth.getCurrentUser().getUid());
                            userReference = FirebaseConfig.getDatabaseReference().child("users").child(newUser.getId());

                            ValueEventListener newUserListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        //User exists
                                    } else {
                                        newUser.saveUser();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            userReference.addListenerForSingleValueEvent(newUserListener);

                            Preferences sharedPreferences = new Preferences(getApplicationContext());
                            sharedPreferences.saveUserInfo(newUser.getId(), newUser.getName());
                            openMainActivity();

                        } else {
                            /*TODO*
                            / Criar exceções de signin
                           */
                        }
                    }
                });
    }

    private void openMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();

    }

    private void instantiateUser(GoogleSignInAccount account) {

        Uri photoUrl = account.getPhotoUrl();
        String stringPhotoUrl = photoUrl.toString();

        newUser = new User();
        newUser.setEmail(account.getEmail());
        newUser.setName(account.getDisplayName());
        newUser.setPhotoUrl(stringPhotoUrl);
    }

    private void loadBackgroundImage() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int width = size.x;

        Picasso.with(this).load(R.drawable.login_background).resize(width, height).centerCrop().noFade().into(backGroundImage);

    }

    private void verifyUserAlreadyAuthenticated(FirebaseAuth firebaseAuth) {

        if(firebaseAuth.getCurrentUser() != null) {
            openMainActivity();
        }

    }

}
