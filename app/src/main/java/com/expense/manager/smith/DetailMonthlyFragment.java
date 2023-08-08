package com.expense.manager.smith;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.expense.manager.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailMonthlyFragment extends DetailFragment {
    Calendar e0;

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Calendar instance = Calendar.getInstance();
        this.W = instance;
        instance.set(5, 1);
        this.W.set(11, 0);
        this.W.clear(12);
        this.W.clear(13);
        this.W.clear(14);
        getView().findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                DetailMonthlyFragment.this.W.add(2, 1);
                DetailMonthlyFragment.this.updateScreen();
            }
        });
        getView().findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                DetailMonthlyFragment.this.W.add(2, -1);
                DetailMonthlyFragment.this.updateScreen();
            }
        });
        updateScreen();
    }

    public void updateScreen() {
        ((TextView) getView().findViewById(R.id.currentMonth)).setText(new SimpleDateFormat("MMMM yyyy", Locale.US).format(this.W.getTime()));
        Calendar calendar = (Calendar) this.W.clone();
        this.e0 = calendar;
        calendar.add(2, 1);
        SimpleDateFormat simpleDateFormat = MyUtils.sdfDatabase;
        this.b0 = simpleDateFormat.format(this.W.getTime());
        this.c0 = simpleDateFormat.format(this.e0.getTime());
        updateUI();
    }

    public String getMessageText() {
        return new SimpleDateFormat("MMMM yyyy", Locale.US).format(this.W.getTime());
    }
}
