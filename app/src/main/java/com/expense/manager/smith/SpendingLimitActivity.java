package com.expense.manager.smith;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;
import com.expense.manager.R;
import com.expense.manager.Model.bean.CategoryBean;
import java.util.ArrayList;
import java.util.List;

public class SpendingLimitActivity extends BaseActivity {
    /* access modifiers changed from: private */
    public Context context;
    private MyDatabaseHandler f89db;
    boolean l;
    private List<CategoryBean> list;
    private ListView lvSpending;
    private CategorySpendingLimitAdapter spendingLimitAdapter;

    public void changeUserAccount() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_spending_limit);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.app_name) + "</font>"));
        this.context = this;
        this.f89db = new MyDatabaseHandler(this);
        this.lvSpending = (ListView) findViewById(R.id.list_spending_limit);
        updateData();
        this.lvSpending.setEmptyView(findViewById(R.id.tv_nodata));
        this.lvSpending.setAdapter(this.spendingLimitAdapter);
        this.lvSpending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(SpendingLimitActivity.this.context, AddEditCategory.class);
                intent.putExtra(TAG.CATEGORY, 1);
                intent.putExtra(TAG.DATA, (int) j);
                SpendingLimitActivity.this.startActivity(intent);
            }
        });
        n();
    }

    private void updateData() {
        this.l = this.f89db.existsColumnInTable().booleanValue();
        this.list = new ArrayList();
        if (this.l) {
            this.list = this.f89db.getSpeningCategory();
        } else {
            this.f89db.addcolumn();
        }
        for (int i = 0; i < this.list.size(); i++) {
            CategoryBean categoryBean = this.list.get(i);
            categoryBean.setCategoryTotal(this.f89db.getCategoryAmount(categoryBean.getCategoryGroup(), categoryBean.getId()));
        }
        CategorySpendingLimitAdapter categorySpendingLimitAdapter = new CategorySpendingLimitAdapter(this.context, this.list, true);
        this.spendingLimitAdapter = categorySpendingLimitAdapter;
        this.lvSpending.setAdapter(categorySpendingLimitAdapter);
    }

    public void onResume() {
        super.onResume();
        updateData();
    }
}
