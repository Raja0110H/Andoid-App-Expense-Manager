package com.expense.manager.smith;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.expense.manager.Utile.LocaleHelper;


public class Ambaliya_MyApplication extends MultiDexApplication {


    private static Ambaliya_MyApplication instance;

    public static Ambaliya_MyApplication getInstance() {
        return instance;
    }

    static Context a;


    public void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        a = this;
        instance = this;


    }

}