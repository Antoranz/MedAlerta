package com.example.myapplication.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MedAlertaSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private static final String KEY_VERIFICATED = "is_verificated";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String userId, String username) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putBoolean(KEY_VERIFICATED,false);
        editor.apply();
    }

    public boolean isVerificated() {
        return sharedPreferences.getBoolean(KEY_VERIFICATED, false);
    }

    public void verificateEmail() {
        editor.putBoolean(KEY_VERIFICATED,true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        if(sharedPreferences == null){
            return false;
        }
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }
}