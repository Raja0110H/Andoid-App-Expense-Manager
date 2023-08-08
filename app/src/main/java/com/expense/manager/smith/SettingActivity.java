package com.expense.manager.smith;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.NotificationCompat;

import com.expense.manager.R;
import com.expense.manager.Utile.LocaleHelper;
import com.expense.manager.Utile.Preferences;
import com.expense.manager.passcode.PinEnterActivity;

import java.util.Calendar;
import java.util.Locale;

public class SettingActivity extends BaseActivity {
    //    private Switch aSwitchNotification;
    /* access modifiers changed from: private */
    public Switch aSwitchPassword;
    String l;
    String m = "en";
    int n;
    int o;
    Locale p;
    private TextView tvPassowrd;
    TextView txtCurrency;

    public void onDestroy() {
        super.onDestroy();
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_settings);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.action_settings) + "</font>"));
        this.tvPassowrd = (TextView) findViewById(R.id.tvPassowrd);
        this.aSwitchPassword = (Switch) findViewById(R.id.switch_password);
        txtCurrency = findViewById(R.id.txtCurrency);
//        this.aSwitchNotification = (Switch) findViewById(R.id.switch_notification);
        if (LocaleHelper.getIsPinSet(this)) {
            this.tvPassowrd.setText(getString(R.string.disable_password));
            this.aSwitchPassword.setChecked(true);
        } else {
            this.tvPassowrd.setText(getString(R.string.enable_password));
            this.aSwitchPassword.setChecked(false);
        }
//        if (Preferences.getInstance().getNotification()) {
//            this.aSwitchNotification.setChecked(true);
//        } else {
//            this.aSwitchNotification.setChecked(false);
//        }
        this.aSwitchPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SettingActivity.this.startActivity(new Intent(SettingActivity.this, PinEnterActivity.class));
                    SettingActivity.this.finish();
                    return;
                }
                new AlertDialog.Builder(SettingActivity.this).setTitle(SettingActivity.this.getResources().getString(R.string.app_name)).setMessage(SettingActivity.this.getString(R.string.msg_disable_password)).setCancelable(false).setPositiveButton(SettingActivity.this.getString(R.string.action_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Preferences.getInstance().setFirstTimeLaunch(false);
                        LocaleHelper.setIsPinSet(SettingActivity.this, false);
                        SettingActivity.this.aSwitchPassword.setChecked(false);
                        System.exit(0);
                    }
                }).show();
            }
        });
//        this.aSwitchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
//                if (z) {
//                    SettingActivity.this.setTime();
//                    return;
//                }
//                Preferences.getInstance().setNotification(false);
//                ((AlarmManager) SettingActivity.this.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(PendingIntent.getService(SettingActivity.this, 0, new Intent(SettingActivity.this, NotifyService.class), 0));
//                Toast.makeText(SettingActivity.this.getApplicationContext(), "Alarm Cancelled", 0).show();
//            }
//        });
        n();

    }



    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {


        ((TextView) findViewById(R.id.tvSummeryCurrency)).setText(this.i.getCurrencyName());
        txtCurrency.setText(this.i.getCurrencySymbol());
        this.m = LocaleHelper.getLanguageCode(this);

    }

    public void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));
    }

    public void onSelectItem(View view) {
        selectItem(Integer.parseInt(view.getTag().toString()));
    }

    private void selectItem(int i) {
        switch (i) {
            case 1:
                currencyPickerDialog();
                return;
            case 2:
                startActivity(new Intent(this, SettingCategotyActivity.class));
                return;

            case 9:
                Toast.makeText(this, "Coming Soon in Future", Toast.LENGTH_SHORT).show();
                return;



            case 5:
                selectUserSummaryType();
                return;

            default:
                return;
        }
    }


    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Locale locale = this.p;
        if (locale != null) {
            configuration.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    private void selectUserSummaryType() {
        String[] strArr = {getString(R.string.daily), getString(R.string.monthly)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(strArr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SettingActivity.this.userSummarySelection(i);
            }
        });
        builder.create().show();
    }

    public void userSummarySelection(int i) {
        if (i == 0) {
            i = 1;
        } else if (i == 1) {
            i = 3;
        }
        MyUtils.setUserSummary(this, i);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private void currencyPickerDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_currency_list);
        ListView listView = (ListView) dialog.findViewById(R.id.listView1);
        listView.setAdapter(new CurrencyPickerAdapter(this));
        dialog.setCancelable(true);
        dialog.setTitle(getString(R.string.select_currency));
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SettingActivity.this.i.setCurrency(i);
                SettingActivity settingActivity = SettingActivity.this;
                settingActivity.j.addUpdateAccountData(settingActivity.i);
                SettingActivity.this.updateUI();
                dialog.dismiss();
            }
        });
    }

    public void changeUserAccount() {
        updateUI();
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public class CurrencyPickerAdapter extends ArrayAdapter<String[]> {
        public CurrencyPickerAdapter(Context context) {
            super(context, R.layout.list_item_currency, MyUtils.currencyList);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = SettingActivity.this.getLayoutInflater().inflate(R.layout.list_item_currency, viewGroup, false);
            String[] item = (String[]) getItem(i);
            ((TextView) inflate.findViewById(R.id.tvCurrencyName)).setText(item[1]);
            ((TextView) inflate.findViewById(R.id.tvCurrencySymbol)).setText(item[0]);
            return inflate;
        }
    }


}
