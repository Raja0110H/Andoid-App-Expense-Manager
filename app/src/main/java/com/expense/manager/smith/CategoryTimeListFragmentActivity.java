package com.expense.manager.smith;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import com.expense.manager.R;

public class CategoryTimeListFragmentActivity extends BaseActivity {
    private Bundle mBundle;

    public void onCreate(Bundle bundle) {
        this.k = false;
        super.onCreate(bundle);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.app_name) + "</font>"));
        n();
        this.mBundle = getIntent().getExtras();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.option_add) {
            return super.onOptionsItemSelected(menuItem);
        }
        Intent intent = new Intent(this.h, AddExpenceIncomeActivity.class);
        intent.putExtra(TAG.CATEGORY, 2);
        startActivity(intent);
        ((BaseActivity) getParent()).changeUserAccount();
        return true;
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void changeUserAccount() {
        updateUI();
    }

    private void updateUI() {
        CategoryTimeFragment categoryTimeFragment = new CategoryTimeFragment();
        categoryTimeFragment.setArguments(this.mBundle);
        getSupportFragmentManager().beginTransaction().replace(16908290, categoryTimeFragment).commit();
    }
}
