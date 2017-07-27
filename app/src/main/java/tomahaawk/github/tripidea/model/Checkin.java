package tomahaawk.github.tripidea.model;


import android.location.Location;

import com.google.firebase.database.DatabaseReference;

import tomahaawk.github.tripidea.helper.FirebaseConfig;

public class Checkin {

    private String id;
    private double latitude;
    private double longitude;
    private String message;

    public Checkin() {

    }

/*    public void saveCheckin() {

        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("checkins").

    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
