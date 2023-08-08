package com.expense.manager.smith;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.expense.manager.R;
import com.expense.manager.Model.bean.AccountBean;
import com.expense.manager.passcode.PinEnterActivity;
import java.util.List;
import java.util.regex.Pattern;

public class AddEditAccount extends BaseActivity {
    private static long back_pressed;
    private List<AccountBean> accounts;
    private AccountBean curAccount;
    private EditText etAccEmail;
    private EditText etAccName;
    private MyDatabaseHandler f52db;

    public void changeUserAccount() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_manage_account);
//        ActionBar supportActionBar = getSupportActionBar();
//        supportActionBar.setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.app_name) + "</font>"));
        this.etAccName = (EditText) findViewById(R.id.etAccountName);
        this.etAccEmail = (EditText) findViewById(R.id.etAccountEmail);
        MyDatabaseHandler myDatabaseHandler = new MyDatabaseHandler(this);
        this.f52db = myDatabaseHandler;
        this.accounts = myDatabaseHandler.getAccountList();
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(TAG.DATA)) {
            MyDatabaseHandler myDatabaseHandler2 = this.f52db;
            this.curAccount = myDatabaseHandler2.getAccountModelById("" + extras.getInt(TAG.DATA));
        }
        if (this.curAccount == null) {
            this.curAccount = new AccountBean();
        }
        updateUI();
    }

    public void onBackPressed() {
        if (this.accounts.size() != 0) {
            super.onBackPressed();
        } else if (back_pressed + 3000 > System.currentTimeMillis()) {
            finishAffinity();
            System.exit(0);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.back_msg), 0).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    public void updateUI() {
        if (!TextUtils.isEmpty(this.curAccount.getName())) {
            this.etAccName.setText(this.curAccount.getName());
        }
        if (!TextUtils.isEmpty(this.curAccount.getEmail())) {
            this.etAccEmail.setText(this.curAccount.getEmail());
        }
    }

    public void onCreateClick(View view) {
        String obj = this.etAccName.getText().toString();
        String obj2 = this.etAccEmail.getText().toString();
        if (TextUtils.isEmpty(obj)) {
            Toast.makeText(this, "Enter Some Name for your Account", 0).show();
        } else if (!checkmail(obj2)) {
            Toast.makeText(this, "Enter valid email.", 0).show();
        } else {
            this.curAccount.setName(obj);
            this.curAccount.setEmail(obj2);
            this.curAccount.setCurrency(MyUtils.getLastCurrencyId(this));
            this.f52db.addUpdateAccountData(this.curAccount);
            setPasscodDialog();
        }
    }

    private void setPasscodDialog() {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.app_name)).setMessage(getString(R.string.set_password_msg)).setCancelable(false).setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AddEditAccount.this.startActivity(new Intent(AddEditAccount.this, PinEnterActivity.class));
                System.exit(0);
            }
        }).setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AddEditAccount.this.finish();
            }
        }).show();
    }

    private boolean checkmail(String str) {
        if (str == null || str.equalsIgnoreCase("")) {
            return false;
        }
        return Pattern.compile(".+@.+\\.[a-z]+").matcher(str).matches();
    }
}
