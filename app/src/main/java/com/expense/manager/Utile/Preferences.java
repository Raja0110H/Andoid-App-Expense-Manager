package com.expense.manager.Utile;

import android.content.SharedPreferences;

import com.expense.manager.smith.Ambaliya_MyApplication;

public class Preferences {
    private static final String FIRST_TIME_LAUNCH = "first_time_launch";
    public static Preferences INSTANCE;
    public final String GCM_TOKEN = "gcm_token";
    public final String PHOTO_URI = "pref_user_photo_local";
    private final String PREFERENCE_FILE = "hidvideo_preferences";
    public final String PREF_ANSWER = "pref_answer";
    public final String PREF_ISLOGIN = "pref_user_islogin";
    public final String PREF_ISNotification = "pref_user_isnotification";
    public final String PREF_ISPERMISSION = "pref_user_permission";
    public final String PREF_LANGUAGE = "pref_language";
    public final String PREF_PIN = "pref_pin";
    public final String PREF_PIN_INNER = "pref_pin_inner";
    public final String PREF_QUESTION = "pref_question";
    public final String PREF_SIGNIN_OBJ = "pref_user_signin";
    public final String PREF_USER_EMAIL = "pref_user_email";
    public final String PREF_USER_ID = "pref_id";
    public final String PREF_USER_NAME = "pref_user_name";
    public final String PREF_USER_PHONE = "pref_user_phone";
    private SharedPreferences.Editor mEdit;
    private SharedPreferences mPrefs;

    private Preferences() {
        SharedPreferences sharedPreferences = Ambaliya_MyApplication.getInstance().getSharedPreferences("hidvideo_preferences", 0);
        this.mPrefs = sharedPreferences;
        this.mEdit = sharedPreferences.edit();
    }

    public static Preferences getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Preferences();
        }
        return INSTANCE;
    }

    public String getUserId() {
        return getInstance().mPrefs.getString("pref_id", "");
    }

    public String getPrefValue(String str) {
        return this.mPrefs.getString(str, "");
    }

    public void SavePrefValue(String str, String str2) {
        this.mEdit.putString(str, str2);
        save();
    }

    public boolean getPrefBoolean(String str) {
        return this.mPrefs.getBoolean(str, false);
    }

    public void SavePrefBoolean(String str, boolean z) {
        this.mEdit.putBoolean(str, z);
        save();
    }

    public boolean getLoginStatus() {
        return this.mPrefs.getBoolean("pref_user_islogin", false);
    }

    public void setLoginStatus(boolean z) {
        this.mEdit.putBoolean("pref_user_islogin", true);
        save();
    }

    public String getLanguage() {
        return this.mPrefs.getString("pref_language", "");
    }

    public void setLanguage(String str) {
        this.mEdit.putString("pref_language", str);
        save();
    }


    public boolean getNotification() {
        return this.mPrefs.getBoolean("pref_user_isnotification", false);
    }

    public void setNotification(boolean z) {
        this.mEdit.putBoolean("pref_user_isnotification", z);
        save();
    }

    public boolean getFirstTimeLaunch() {
        return this.mPrefs.getBoolean(FIRST_TIME_LAUNCH, false);
    }

    public void setFirstTimeLaunch(boolean z) {
        this.mEdit.putBoolean(FIRST_TIME_LAUNCH, z);
        this.mEdit.commit();
    }

    private void save() {
        this.mEdit.apply();
    }

    public void ClearPrefsValue() {
        getInstance().mPrefs.edit().commit();
    }
}
