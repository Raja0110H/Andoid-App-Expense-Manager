package com.expense.manager.smith;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expense.manager.R;
import com.expense.manager.Model.bean.DataBean;
import com.expense.manager.Model.bean.RecurringBean;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyUtils {
    public static final String[] Methods_TYPES = {"Wire Transfer", "Credit Card", "EMI", "Case Payments"};
    public static String PREF_KEY_ACCOUNT = MyDatabaseHandler.AccountTable.TABLE_NAME;
    public static String PREF_KEY_MAIN_SUMMARY = "summary";
    public static String PREF_KEY_VERSION = "version";
    public static String[] colorcode = {"#F03131", "#FA6900", "#399891", "#ff6b6b", "#594f4f", "#547980", "#fd8603", "#00cbe7", "#bfd92e", "#90b80f", "#669401", "#306b02", "#0bb7f3", "#306b02", "#1f8dd6", "#16579f", "#0a2849", "#000923", "#16c1c8", "#49cccc", "#7cd7cf", "#aee1d3", "#ffab03", "#fc7f03", "#fc3903", "#d1024e", "#a6026c", "#cc333f", "#eb6841", "#edc951"};
    public static String[][] currencyList = {new String[]{"$", "DOLLAR"}, new String[]{"£", "POUND"}, new String[]{"¥", "YEN"}, new String[]{"฿", "THAI BAHT"}, new String[]{"₤", "LIRA"}, new String[]{"₣", "FRENCH FRANC"}, new String[]{"₧", "PESETA"}, new String[]{"₩", "WON"}, new String[]{"€", "EURO"}, new String[]{"₹", "INDIAN RUPEE"}, new String[]{"﷼", "RIAL"}, new String[]{"₱", "PESO"}, new String[]{"П", "MALAYSIAN RINGGIT"}, new String[]{"R", "SOUTH AFRICAN RAND"}, new String[]{"Rp", "INDONESIAN RUPIAH"}, new String[]{"₨", "PAKISTAN RUPEE"}, new String[]{"$", "ARGENTINA PESO"}, new String[]{"₱", "PHILIPPINE PESO"}, new String[]{"₡", "COLON"}, new String[]{"₢", "CRUZEIRO"}, new String[]{"₥", "MILL"}, new String[]{"₦", "NAIRA"}, new String[]{"₪", "NEW SHEQEL"}, new String[]{"₫", "DONG"}, new String[]{"₭", "KIP"}, new String[]{"₮", "TUGRIK"}, new String[]{"₯", "DRACHMA"}, new String[]{"₰", "GERMAN PENNY"}, new String[]{"₲", "GUARAN"}, new String[]{"₳", "AUSTRA"}, new String[]{"₴", "HRYVNIA"}, new String[]{"₵", "CEDI"}, new String[]{"₶", "LIVRE TOURNOIS"}, new String[]{"₷", "SPESMILO"}, new String[]{"₸", "TENGE"}};
    public static String item_one = "com.demo.example.dailyincomeexpensemanager.inapp";
    public static String[][] language = {new String[]{"en", "English"}, new String[]{"es", "español"}, new String[]{"hi", "हिन्दी"}, new String[]{"ar","عربي"},
            new String[]{"bn","বাংলা"}, new String[]{"ru","pусский язык"},new String[]{"pt","português"},new String[]{"fr", "français"},new String[]{"mr","मराठी"},
            new String[]{"gu", "ગુજરાતી"},new String[]{"ta","தமிழ்"},new String[]{"ur","اردو"},new String[]{"te","తెలుగు"},new String[]{"ml","മലയാളം"},new String[]{"pa","ਪੰਜਾਬੀ"},
            new String[]{"de","Deutsch"},new String[]{"in","Indonesia"},new String[]{"ja","日本"},new String[]{"cni","中國人"},new String[]{"cnt","中国人"}};
    public static final SimpleDateFormat sdfDatabase = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat sdfUser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static double RoundOff(double d) {
        return Double.valueOf(new DecimalFormat("#.##").format(d)).doubleValue();
    }

    public static Calendar getNextUpdateCalender(int i, Calendar calendar, int i2) {
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.set(11, 0);
        calendar2.clear(12);
        calendar2.clear(13);
        calendar2.clear(14);
        if (i == 1) {
            calendar2.add(2, 1);
            int actualMaximum = calendar2.getActualMaximum(5);
            if (actualMaximum >= i2) {
                calendar2.set(5, i2);
            } else {
                calendar2.set(5, actualMaximum);
            }
        } else if (i == 0) {
            calendar2.add(3, 1);
        } else if (i == 2) {
            calendar2.add(3, 2);
        } else if (i == 3) {
            calendar2.add(2, 3);
            int actualMaximum2 = calendar2.getActualMaximum(5);
            if (actualMaximum2 >= i2) {
                calendar2.set(5, i2);
            } else {
                calendar2.set(5, actualMaximum2);
            }
        } else if (i == 4) {
            calendar2.add(2, 6);
            int actualMaximum3 = calendar2.getActualMaximum(5);
            if (actualMaximum3 >= i2) {
                calendar2.set(5, i2);
            } else {
                calendar2.set(5, actualMaximum3);
            }
        } else if (i == 5) {
            calendar2.add(1, 1);
            int actualMaximum4 = calendar2.getActualMaximum(5);
            if (actualMaximum4 >= i2) {
                calendar2.set(5, i2);
            } else {
                calendar2.set(5, actualMaximum4);
            }
        }
        return calendar2;
    }

    public static void setAccount(Context context, String str) {
        saveData(context, PREF_KEY_ACCOUNT, str);
    }

    public static int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getCurrentAccount(Context context) {
        return loadData(context, PREF_KEY_ACCOUNT, "1");
    }

    public static void setNotNeededWhatNew(Context context) {
        String str = PREF_KEY_VERSION;
        saveData(context, str, "" + getAppVersion(context));
    }

    public static boolean getWhatNewRequired(Context context) {
        String loadData = loadData(context, PREF_KEY_VERSION, "1");
        return !loadData.equalsIgnoreCase("" + getAppVersion(context));
    }

    public static void saveData(Context context, String str, String str2) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static String loadData(Context context, String str, String str2) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, str2);
    }

    public static int getLastCurrencyId(Context context) {
        String string = context.getApplicationContext().getSharedPreferences("setting", 0).getString("cur_name", "");
        int i = 0;
        while (true) {
            String[][] strArr = currencyList;
            if (i >= strArr.length) {
                return 0;
            }
            if (string.equalsIgnoreCase(strArr[i][1])) {
                return i;
            }
            i++;
        }
    }

    public static View getEmptyView(Activity activity, String str) {
        TextView textView = (TextView) activity.getLayoutInflater().inflate(R.layout.empty_textview, (ViewGroup) null);
        textView.setText(str);
        return textView;
    }

    public static void restartApp(Context context) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        launchIntentForPackage.addFlags(67108864);
        context.startActivity(launchIntentForPackage);
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static List<DataBean> getFutureRecurringDataByTime(MyDatabaseHandler myDatabaseHandler, int i, Calendar calendar, Calendar calendar2) {
        List<RecurringBean> recurringModelList = myDatabaseHandler.getRecurringModelList(i);
        ArrayList arrayList = new ArrayList();
        for (RecurringBean recurringBean : recurringModelList) {
            Calendar instance = Calendar.getInstance();
            try {
                instance.setTime(sdfDatabase.parse(recurringBean.getRecurringLastdate()));
                while (instance.before(calendar)) {
                    instance = (Calendar) getNextUpdateCalender(Integer.parseInt(recurringBean.getRecurringType()), instance, Integer.parseInt(recurringBean.getRecurringDate())).clone();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            while (instance.before(calendar2)) {
                DataBean convertToDataBean = recurringBean.convertToDataBean();
                convertToDataBean.setDate(sdfDatabase.format(instance.getTime()));
                instance.setTime(getNextUpdateCalender(Integer.parseInt(recurringBean.getRecurringType()), instance, Integer.parseInt(recurringBean.getRecurringDate())).getTime());
                arrayList.add(convertToDataBean);
            }
        }
        return arrayList;
    }

    public static int getUserSummary(Activity activity) {
        return Integer.parseInt(loadData(activity, PREF_KEY_MAIN_SUMMARY, "1"));
    }

    public static void setUserSummary(Activity activity, int i) {
        String str = PREF_KEY_MAIN_SUMMARY;
        saveData(activity, str, "" + i);
    }
}
