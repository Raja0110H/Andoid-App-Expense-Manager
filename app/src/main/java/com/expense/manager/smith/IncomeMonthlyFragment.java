package com.expense.manager.smith;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.expense.manager.R;
import com.expense.manager.Model.bean.DataBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class IncomeMonthlyFragment extends BaseFragment {
    public static final String ARG_SECTION_NUMBER = "selection_type";
    IncomeExpenceListAdapter W;
    MyDatabaseHandler X;
    /* access modifiers changed from: private */
    public Calendar calendar;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public int selection = 1;
    private TextView title;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_manager_list, viewGroup, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.selection = arguments.getInt("selection_type", 1);
        }
        this.context = getActivity();
        this.X = new MyDatabaseHandler(getActivity());
        Calendar instance = Calendar.getInstance();
        this.calendar = instance;
        instance.set(5, 1);
        this.calendar.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        this.title = (TextView) inflate.findViewById(R.id.currentMonth);
        ListView listView = (ListView) inflate.findViewById(R.id.lvDistribution);
        listView.setEmptyView(inflate.findViewById(R.id.tvNoDataFound));
        inflate.findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IncomeMonthlyFragment.this.calendar.add(2, 1);
                IncomeMonthlyFragment.this.updateUI();
            }
        });
        inflate.findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IncomeMonthlyFragment.this.calendar.add(2, -1);
                IncomeMonthlyFragment.this.updateUI();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (j != 0) {
                    Intent intent = new Intent(IncomeMonthlyFragment.this.context, AddExpenceIncomeActivity.class);
                    intent.putExtra(TAG.CATEGORY, IncomeMonthlyFragment.this.selection);
                    intent.putExtra(TAG.DATA, (int) j);
                    IncomeMonthlyFragment.this.startActivity(intent);
                    return;
                }
                String refRecurring = IncomeMonthlyFragment.this.W.getItem(i).getRefRecurring();
                Intent intent2 = new Intent(IncomeMonthlyFragment.this.context, AddExpenceIncomeActivity.class);
                intent2.putExtra(TAG.CATEGORY, IncomeMonthlyFragment.this.selection);
                intent2.putExtra(TAG.RECURRING, Integer.parseInt(refRecurring));
                IncomeMonthlyFragment.this.startActivity(intent2);
            }
        });
        return inflate;
    }

    public void updateUI() {
        this.title.setText(new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(this.calendar.getTime()));
        String format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this.calendar.getTime());
        Calendar calendar2 = (Calendar) this.calendar.clone();
        calendar2.add(2, 1);
        List<DataBean> managerDataByTime = this.X.getManagerDataByTime(this.selection, format, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar2.getTime()));
        List<DataBean> futureRecurringDataByTime = MyUtils.getFutureRecurringDataByTime(this.X, this.selection, (Calendar) this.calendar.clone(), (Calendar) calendar2.clone());
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(managerDataByTime);
        arrayList.addAll(futureRecurringDataByTime);
        this.W = new IncomeExpenceListAdapter(this.context, arrayList);
        ((ListView) getView().findViewById(R.id.lvDistribution)).setAdapter(this.W);
    }

    public void onStart() {
        super.onStart();
        updateUI();
    }
}
