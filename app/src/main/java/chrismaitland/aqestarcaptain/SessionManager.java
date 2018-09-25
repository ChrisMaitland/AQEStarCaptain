package chrismaitland.aqestarcaptain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context ctx;

    int Private_Mode = 0;

    private static final String PREF_NAME = "Pref";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFILE = "profile";
    public static final String KEY_RATING = "rating";
    public static final String KEY_COINS = "coins";

    public SessionManager(Context context) {
        this.ctx = context;
        pref = ctx.getSharedPreferences(PREF_NAME, Private_Mode);
        editor = pref.edit();
    }

    /**
     * Create a login session
     * @param name
     * @param email
     */
    public void createLoginSession(String name, String email) {
        editor.putBoolean(IS_LOGGED_IN, true);

        editor.putString(KEY_NAME, name);

        editor.putString(KEY_EMAIL, email);

        editor.commit();

    }

    public void selectProfile(String profile) {
        editor.putString(KEY_PROFILE, profile);

        editor.commit();
    }

    public void setRating(String rating) {
        editor.putString(KEY_RATING, rating);

        editor.commit();
    }

    public void setCoins(String coins) {
        editor.putString(KEY_COINS, coins);

        editor.commit();
    }

    /**
     * Checks users login status & redirects if error
     */
    public void checkLogin() {
        if(!this.isLoggedIn()) {
            // If user isn't logged in, redirect back
            Intent i = new Intent(ctx, MainActivity.class);
            // Close all activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // New flag, start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ctx.startActivity(i);
        }
    }

    /**
     * Stores session data
     * @return
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user profile name
        user.put(KEY_PROFILE, pref.getString(KEY_PROFILE, null));

        // user rating value
        user.put(KEY_RATING, pref.getString(KEY_RATING, null));

        // users space coins
        user.put(KEY_COINS, pref.getString(KEY_COINS, null));

        return user;
    }

    /**
     * Clear all session details
     */
    public void logoutUser() {
        editor.clear();
        editor.commit();

        // After clearing session, direct user to login page
        Intent i = new Intent(ctx, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);

    }

    /**
     * check for active login
     * @return
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }
}
