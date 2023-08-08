package com.expense.manager.smith;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import com.expense.manager.R;

public class ExpenseFragmentActivity extends BaseActivity {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getSupportActionBar().setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>Expense Manager</font>"));
        n();
    }

    private void updateUI() {
        IncomeMonthlyFragment incomeMonthlyFragment = new IncomeMonthlyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("selection_type", 1);
        incomeMonthlyFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(16908290, incomeMonthlyFragment).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.option_add) {
            return super.onOptionsItemSelected(menuItem);
        }
        Intent intent = new Intent(this.h, AddExpenceIncomeActivity.class);
        intent.putExtra(TAG.CATEGORY, 1);
        startActivity(intent);
        return true;
    }

    public void onResume() {
        super.onResume();
        updateUI();
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

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Log.m953d("Expense Activity On Saved Instance State");
    }
}
