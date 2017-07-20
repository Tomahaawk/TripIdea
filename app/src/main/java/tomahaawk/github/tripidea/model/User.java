package tomahaawk.github.tripidea.model;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import tomahaawk.github.tripidea.helper.FirebaseConfig;

public class User {

    private String id;
    private String name;
    private String email;
    private String photoUrl;

    public User() {
    }

    public void saveUser() {

        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("users").child( getId() ).setValue( this );
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
