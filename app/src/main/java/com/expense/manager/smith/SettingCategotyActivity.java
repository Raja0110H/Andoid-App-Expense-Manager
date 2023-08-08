package com.expense.manager.smith;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.expense.manager.R;
import com.expense.manager.Model.bean.CategoryBean;
import com.expense.manager.widget.SortListView.DragSortListView;
import java.util.List;

public class SettingCategotyActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout addExpense;
    private LinearLayout addIncome;
    private CategoryBean categoryBean;
    /* access modifiers changed from: private */
    public SettingCategoryCustomAdapter expenseAdpter;
    /* access modifiers changed from: private */
    public List<CategoryBean> expenseData;
    /* access modifiers changed from: private */
    public SettingCategoryCustomAdapter incomeAdapter;
    private DragSortListView incomeList;
    /* access modifiers changed from: private */
    public List<CategoryBean> inocmeData;
    Boolean l;
    DragSortListView m;
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        public void drop(int i, int i2) {
            CategoryBean item = SettingCategotyActivity.this.expenseAdpter.getItem(i);
            SettingCategotyActivity.this.expenseData.remove(item);
            SettingCategotyActivity.this.expenseData.add(i2, item);
            if (i < i2 && i != i2) {
                SettingCategotyActivity.this.j.addUpdateCategoryByDrod_IdtoOthers(i2, i, 1, item.getAccountRef());
                SettingCategotyActivity.this.j.addUpdateCategoryByDrod_Id(i, i2, Integer.parseInt(item.getId()), item, 1, item.getAccountRef());
            } else if (i > i2 && i != i2) {
                SettingCategotyActivity.this.j.addUpdateCategoryByDrod_IdtoOthersback(i2, i, 1, item.getAccountRef());
                SettingCategotyActivity.this.j.addUpdateCategoryByDrod_Idback(i, i2, Integer.parseInt(item.getId()), item, 1, item.getAccountRef());
            }
            SettingCategotyActivity.this.expenseAdpter.notifyDataSetChanged();
        }
    };
    private DragSortListView.DropListener onDrop1 = new DragSortListView.DropListener() {
        public void drop(int i, int i2) {
            CategoryBean item = SettingCategotyActivity.this.incomeAdapter.getItem(i);
            SettingCategotyActivity.this.inocmeData.remove(item);
            SettingCategotyActivity.this.inocmeData.add(i2, item);
            if (i < i2 && i != i2) {
                SettingCategotyActivity.this.j.addUpdateCategoryByDrod_IdtoOthers(i2, i, 2, item.getAccountRef());
                SettingCategotyActivity.this.j.addUpdateCategoryByDrod_Id(i, i2, Integer.parseInt(item.getId()), item, 2, item.getAccountRef());
            } else if (i > i2 && i != i2) {
                SettingCategotyActivity.this.j.addUpdateCategoryByDrod_IdtoOthersback(i2, i, 2, item.getAccountRef());
                SettingCategotyActivity.this.j.addUpdateCategoryByDrod_Idback(i, i2, Integer.parseInt(item.getId()), item, 2, item.getAccountRef());
            }
            SettingCategotyActivity.this.incomeAdapter.notifyDataSetChanged();
        }
    };
    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        public void remove(int i) {
            SettingCategotyActivity.this.expenseData.remove(SettingCategotyActivity.this.expenseAdpter.getItem(i));
            notifyAll();
        }
    };
    private DragSortListView.RemoveListener onRemove1 = new DragSortListView.RemoveListener() {
        public void remove(int i) {
            SettingCategotyActivity.this.inocmeData.remove(SettingCategotyActivity.this.incomeAdapter.getItem(i));
            notifyAll();
        }
    };

    public void onDestroy() {
        super.onDestroy();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_category_list);
//        AdAdmob adAdmob = new AdAdmob(this);
//        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
//        adAdmob.FullscreenAd(this);
        getSupportActionBar().setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>Category</font>"));
        this.incomeList = (DragSortListView) findViewById(R.id.cat_list_income);
        this.m = (DragSortListView) findViewById(R.id.cat_list_expense);
        this.addIncome = (LinearLayout) findViewById(R.id.cat_list_income_add);
        this.addExpense = (LinearLayout) findViewById(R.id.cat_list_expense_add);
        this.addIncome.setOnClickListener(this);
        this.addExpense.setOnClickListener(this);
        this.categoryBean = new CategoryBean();
        this.incomeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(SettingCategotyActivity.this.h, AddEditCategory.class);
                intent.putExtra(TAG.CATEGORY, 2);
                intent.putExtra(TAG.DATA, (int) j);
                SettingCategotyActivity.this.startActivity(intent);
            }
        });
        this.m.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(SettingCategotyActivity.this.h, AddEditCategory.class);
                intent.putExtra(TAG.CATEGORY, 1);
                intent.putExtra(TAG.DATA, (int) j);
                SettingCategotyActivity.this.startActivity(intent);
            }
        });
        n();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cat_list_expense_add) {
            Intent intent = new Intent(this.h, AddEditCategory.class);
            intent.putExtra(TAG.CATEGORY, 1);
            startActivity(intent);
        } else if (id == R.id.cat_list_income_add) {
            Intent intent2 = new Intent(this.h, AddEditCategory.class);
            intent2.putExtra(TAG.CATEGORY, 2);
            startActivity(intent2);
        }
    }

    public void onResume() {
        super.onResume();
        this.l = this.j.existsColumnInTable();
        updateUI();
    }

    private void updateUI() {
        this.m.setDropListener(this.onDrop);
        this.incomeList.setDropListener(this.onDrop1);
        this.m.setRemoveListener(this.onRemove);
        this.incomeList.setRemoveListener(this.onRemove1);
        if (this.l.booleanValue()) {
            this.inocmeData = this.j.getCategoryList(2);
            this.expenseData = this.j.getCategoryList(1);
        } else {
            this.j.addcolumn();
            this.inocmeData = this.j.getCategoryList(2);
            this.expenseData = this.j.getCategoryList(1);
        }
        this.incomeAdapter = new SettingCategoryCustomAdapter(this.h, this.inocmeData);
        this.expenseAdpter = new SettingCategoryCustomAdapter(this.h, this.expenseData);
        this.incomeList.setAdapter((ListAdapter) this.incomeAdapter);
        this.m.setAdapter((ListAdapter) this.expenseAdpter);
        CategoryListHelper.getListViewSize(this.incomeList);
        CategoryListHelper.getListViewSize(this.m);
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
}
