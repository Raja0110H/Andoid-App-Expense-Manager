package com.expense.manager.smith;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.expense.manager.R;
import com.expense.manager.Utile.LocaleHelper;
import com.expense.manager.Model.bean.AccountBean;
import com.expense.manager.Model.bean.CategoryBean;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {
    private List<AccountBean> accounts;
    protected Context h;
    protected AccountBean i;
    protected MyDatabaseHandler j;
    protected boolean k = true;

    public abstract void changeUserAccount();

    private void upadteAccountSpinner() {
    }

    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        this.h = this;
        MyDatabaseHandler myDatabaseHandler = new MyDatabaseHandler(this);
        this.j = myDatabaseHandler;
        this.accounts = myDatabaseHandler.getAccountList();
        updateCurrentAccount();
    }

    private void updateCurrentAccount() {
        this.i = this.j.getAccountModelById(MyUtils.getCurrentAccount(this.h));
    }

    public void onResume() {
        super.onResume();
        updateCurrentAccount();
        updateActionBarSpinner();
        this.accounts.size();
    }

    public void updateActionBarSpinner() {
        if (this.k) {
            upadteAccountSpinner();
        }
    }

    public void onNavigationItemChanged() {
        if (TextUtils.isEmpty(this.i.getId())) {
            j();
            updateActionBarSpinner();
        } else if (!MyUtils.getCurrentAccount(this.h).equalsIgnoreCase(this.i.getId())) {
            MyUtils.setAccount(this, this.i.getId());
            this.j = new MyDatabaseHandler(this.h);
            changeUserAccount();
        }
    }

    /* access modifiers changed from: protected */
    public void m(Activity activity) {
        finish();
        Intent intent = new Intent(this.h, activity.getClass());
        intent.setFlags(603979776);
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void j() {
        List<AccountBean> accountList = this.j.getAccountList();
        this.accounts = accountList;
        startActivity(new Intent(this, AddEditAccount.class));
//        if (accountList.size() == 0) {
//            try {
//                startActivity(new Intent(this, AddEditAccount.class));
//            } catch (Exception e) {
//
//            }
//
//        }
    }

    public void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));
    }

    /* access modifiers changed from: protected */
    public void l(String str) {
        final AccountBean accountModelById = this.j.getAccountModelById(str);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Account");
        builder.setMessage("Account Name");
        final EditText editText = new EditText(this);
        editText.setSingleLine(true);
        editText.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        editText.setText(accountModelById.getName());
        builder.setView(editText);
        builder.setPositiveButton(getString(R.string.action_done), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String obj = editText.getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    Toast.makeText(BaseActivity.this.h, "Enter Some Name for your Account", 0).show();
                    return;
                }
                accountModelById.setName(obj);
                long addUpdateAccountData = BaseActivity.this.j.addUpdateAccountData(accountModelById);
                if (addUpdateAccountData > 0) {
                    BaseActivity.this.i(String.valueOf(addUpdateAccountData));
                }
                dialogInterface.cancel();
                BaseActivity.this.updateActionBarSpinner();
                BaseActivity.this.changeUserAccount();
            }
        });
        builder.setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    /* access modifiers changed from: protected */
    public void n() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }








    /* access modifiers changed from: protected */
    public void i(String str) {
        CategoryBean categoryBean = new CategoryBean("Salary", "#66bc29", 2, 0);
        CategoryBean categoryBean2 = new CategoryBean("Other", "#BC8F8F", 2, 1);
        categoryBean.setAccountRef(str);
        categoryBean2.setAccountRef(str);
        this.j.addUpdateCategoryData(categoryBean);
        this.j.addUpdateCategoryData(categoryBean2);
        CategoryBean categoryBean4 = new CategoryBean("Travel", "#9400D3", 1, 0, "0");
        CategoryBean categoryBean42 = new CategoryBean("Food", "#228B22", 1, 1, "0");
        CategoryBean categoryBean5 = new CategoryBean("Entertainment", "#FF1493", 1, 2, "0");
        CategoryBean categoryBean6 = new CategoryBean("Other", "#696969", 1, 3, "0");
        categoryBean4.setAccountRef(str);
        categoryBean42.setAccountRef(str);
        categoryBean5.setAccountRef(str);
        categoryBean6.setAccountRef(str);
        this.j.addUpdateCategoryData(categoryBean4);
        this.j.addUpdateCategoryData(categoryBean42);
        this.j.addUpdateCategoryData(categoryBean5);
        this.j.addUpdateCategoryData(categoryBean6);
    }

    public String getCurrencySymbol() {
        AccountBean accountBean = this.i;
        if (accountBean != null) {
            return accountBean.getCurrencySymbol();
        }
        return MyUtils.currencyList[0][0];
    }
}
