package tomahaawk.github.tripidea.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private final static String FILE_NAME = "tripidea.preferences";
    private static final int MODE = Context.MODE_PRIVATE;
    private static final String KEY_USER_ID = "loggedUserId";
    private static final String KEY_USER_NAME = "loggedUserName";
    private static final String KEY_USER_PHOTOURL = "loggedUserPhotoUrl";

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public Preferences(Context ctx) {
        this.context = ctx;
        this.preferences = context.getSharedPreferences(FILE_NAME, MODE);
        this.editor = preferences.edit();
    }

    public void saveUserInfo (String userId, String userName, String photoUrl) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_PHOTOURL, photoUrl);
        editor.commit();
    }

    public String getUserId() {
        return preferences.getString(KEY_USER_ID, null);
    }

    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, null);
    }

    public String getUserPhotourl() { return preferences.getString(KEY_USER_PHOTOURL, null); }
}
