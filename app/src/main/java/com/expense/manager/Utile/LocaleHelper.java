package com.expense.manager.Utile;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelper {

     public static final int adsCount = 4;
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";


    private static String getPersistedData(Context context, String str) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SELECTED_LANGUAGE, str);
    }


    public static Context setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLayoutDirection(locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
        } else {
            configuration.locale = locale;
            configuration.setLocale(locale);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            return context.createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return context;
        }
    }

    public static Context onAttach(Context context) {
        return setLocale(context, getLanguageCode(context));
    }

    private static String LNCODE = "lncode";

    public static void setLanguageCode(Context Context, String string) {
        Context.getSharedPreferences(Context.getPackageName(), 0).edit()
                .putString(LNCODE, string).commit();
    }

    public static String getLanguageCode(Context Context) {
        return Context.getSharedPreferences(Context.getPackageName(), 0)
                .getString(LNCODE, "en");
    }



    private static String ISPINSET = "ispinset";

    public static void setIsPinSet(Context Context, boolean bool) {
        Context.getSharedPreferences(Context.getPackageName(), 0).edit()
                .putBoolean(ISPINSET, bool).commit();
    }

    public static boolean getIsPinSet(Context Context) {
        return Context.getSharedPreferences(Context.getPackageName(), 0)
                .getBoolean(ISPINSET, false);
    }

    private static String SEQURITY_QUESTION = "sequrity_question";

    public static void setSequrity_question(Context Context, int nnn) {
        Context.getSharedPreferences(Context.getPackageName(), 0).edit()
                .putInt(SEQURITY_QUESTION, nnn).commit();
    }

    public static int getSequrity_question(Context Context) {
        return Context.getSharedPreferences(Context.getPackageName(), 0)
                .getInt(SEQURITY_QUESTION, 0);
    }

    private static String SEQURITY_ANSWER = "sequrity_answer";

    public static void setSequrityAnswer(Context Context, String mS) {
        Context.getSharedPreferences(Context.getPackageName(), 0).edit()
                .putString(SEQURITY_ANSWER, mS).commit();
    }

    public static String getSequrityAnswer(Context Context) {
        return Context.getSharedPreferences(Context.getPackageName(), 0)
                .getString(SEQURITY_ANSWER, "not written");
    }



}
