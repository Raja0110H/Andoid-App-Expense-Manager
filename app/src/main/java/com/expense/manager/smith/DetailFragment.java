package com.expense.manager.smith;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.expense.manager.R;
import com.expense.manager.Model.bean.CategoryBean;
import com.expense.manager.Model.bean.DataBean;
import com.github.mikephil.charting.utils.Utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public abstract class DetailFragment extends BaseFragment {
    protected Calendar W;
    boolean X;
    protected MyDatabaseHandler Y;
    List<CategoryBean> Z;
    List<CategoryBean> a0;
    protected String b0 = "1";
    protected String c0 = "9";
    protected boolean d0 = true;

    public abstract String getMessageText();

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.Y = new MyDatabaseHandler(getActivity());
        return layoutInflater.inflate(R.layout.fragment_details, viewGroup, false);
    }

    /* access modifiers changed from: protected */
    public void updateUI() {
        double d;
        double d2;
        String totalAmountByTime = this.Y.getTotalAmountByTime(2, this.b0, this.c0);
        String totalAmountByTime2 = this.Y.getTotalAmountByTime(1, this.b0, this.c0);
        String replaceAll = totalAmountByTime.replaceAll(",", "");
        String replaceAll2 = totalAmountByTime2.replaceAll(",", "");
        try {
            d2 = MyUtils.RoundOff(Double.parseDouble(replaceAll));
            d = MyUtils.RoundOff(Double.parseDouble(replaceAll2));
        } catch (NumberFormatException e) {
            d2 = Utils.DOUBLE_EPSILON;
            d = 0.0d;
        }
        setTextValue(R.id.tvTotalIncome, " %.2f", d2);
        setTextValue(R.id.tvTotalExpense, "%.2f", d);
        if (d2 >= d) {
            setTextValue(R.id.tvTotalNet, "%.2f", d2 - d, R.color.detail_income);
        } else {
            setTextValue(R.id.tvTotalNet, "%.2f", d - d2, R.color.detail_expense);
        }
        updateListofCategories();
        if (this.d0) {
            try {
                updateExpectedUI();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            getView().findViewById(R.id.layout_reminder).setVisibility(View.GONE);
        }
    }

    private void updateListofCategories() {
        ListView listView = (ListView) getView().findViewById(R.id.cat_list_income);
        ListView listView2 = (ListView) getView().findViewById(R.id.cat_list_expense);
        boolean booleanValue = this.Y.existsColumnInTable().booleanValue();
        this.X = booleanValue;
        if (booleanValue) {
            this.a0 = this.Y.getCategoryList(2);
            this.Z = this.Y.getCategoryList(1);
        } else {
            this.Y.addcolumn();
        }
        for (CategoryBean categoryBean : this.a0) {
            categoryBean.setCategoryTotal(this.Y.getCategoryAmountByTime(categoryBean.getCategoryGroup(), categoryBean.getId(), this.b0, this.c0));
        }
        for (CategoryBean categoryBean2 : this.Z) {
            categoryBean2.setCategoryTotal(this.Y.getCategoryAmountByTime(categoryBean2.getCategoryGroup(), categoryBean2.getId(), this.b0, this.c0));
        }
        CategoryCustomAdapter categoryCustomAdapter = new CategoryCustomAdapter(getActivity(), this.a0);
        CategoryCustomAdapter categoryCustomAdapter2 = new CategoryCustomAdapter(getActivity(), this.Z);
        listView.setAdapter(categoryCustomAdapter);
        listView2.setAdapter(categoryCustomAdapter2);
        CategoryListHelper.getListViewSize(listView);
        CategoryListHelper.getListViewSize(listView2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DetailFragment.this.openDetailsFor(2, (int) j);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DetailFragment.this.openDetailsFor(1, (int) j);
            }
        });
    }

    public void openDetailsFor(int i, int i2) {
        Intent intent = new Intent(getActivity(), CategoryTimeListFragmentActivity.class);
        intent.putExtra("selection_type", i);
        intent.putExtra(CategoryTimeFragment.CATEGORY_TYPE, i2);
        intent.putExtra(CategoryTimeFragment.START_DATE, this.b0);
        intent.putExtra(CategoryTimeFragment.END_DATE, this.c0);
        intent.putExtra(CategoryTimeFragment.MESSAGE, getMessageText());
        startActivity(intent);
    }

    private void setTextValue(int i, String str, double d) {
        setTextValue(i, str, d, 0);
    }

    private void setTextValue(int i, String str, double d, int i2) {
        TextView textView = (TextView) getView().findViewById(i);
        if (i0().equals("¢") || i0().equals("₣") || i0().equals("₧") || i0().equals("﷼") || i0().equals("₨")) {
            textView.setText(String.format(str, new Object[]{Double.valueOf(d)}) + i0());
            if (i2 != 0) {
                textView.setTextColor(getResources().getColor(i2));
                return;
            }
            return;
        }
        textView.setText(i0() + String.format(str, new Object[]{Double.valueOf(d)}));
        if (i2 != 0) {
            textView.setTextColor(getResources().getColor(i2));
        }
    }

    private void updateExpectedUI() {
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = MyUtils.sdfDatabase;
        try {
            instance.setTime(simpleDateFormat.parse(this.b0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar instance2 = Calendar.getInstance();
        try {
        instance2.setTime(simpleDateFormat.parse(this.c0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double expectedTotal = getExpectedTotal(this.Y, 2, instance, instance2);
        double expectedTotal2 = getExpectedTotal(this.Y, 1, instance, instance2);
        setTextValue(R.id.tvTotalIncomeExpected, "%.2f", expectedTotal);
        setTextValue(R.id.tvTotalExpenseExpected, "%.2f", expectedTotal2);
    }

    private double getExpectedTotal(MyDatabaseHandler myDatabaseHandler, int i, Calendar calendar, Calendar calendar2) {
        double d = Utils.DOUBLE_EPSILON;
        for (DataBean dataBean : MyUtils.getFutureRecurringDataByTime(myDatabaseHandler, i, (Calendar) calendar.clone(), (Calendar) calendar2.clone())) {
            d += Double.parseDouble(dataBean.getAmount());
        }
        return d;
    }
}
