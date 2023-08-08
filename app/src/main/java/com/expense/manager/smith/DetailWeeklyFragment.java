package com.expense.manager.smith;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.expense.manager.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailWeeklyFragment extends DetailFragment {
    Calendar e0;
    Calendar f0;

    public void onViewCreated(View view, Bundle bundle) {
        Calendar instance = Calendar.getInstance();
        this.W = instance;
        instance.set(11, 0);
        this.W.clear(12);
        this.W.clear(13);
        this.W.clear(14);
        Calendar calendar = this.W;
        calendar.set(7, calendar.getFirstDayOfWeek());
        getView().findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                DetailWeeklyFragment.this.W.add(3, 1);
                DetailWeeklyFragment.this.updateScreen();
            }
        });
        getView().findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                DetailWeeklyFragment.this.W.add(3, -1);
                DetailWeeklyFragment.this.updateScreen();
            }
        });
        updateScreen();
    }

    public void updateScreen() {
        this.f0 = (Calendar) this.W.clone();
        Calendar calendar = (Calendar) this.W.clone();
        this.e0 = calendar;
        calendar.add(3, 1);
        this.e0.add(14, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        ((TextView) getView().findViewById(R.id.currentMonth)).setText(simpleDateFormat.format(this.f0.getTime()) + " to " + simpleDateFormat.format(this.e0.getTime()));
        SimpleDateFormat simpleDateFormat2 = MyUtils.sdfDatabase;
        this.b0 = simpleDateFormat2.format(this.f0.getTime());
        this.c0 = simpleDateFormat2.format(this.e0.getTime());
        updateUI();
    }

    public String getMessageText() {
        StringBuilder sb = new StringBuilder();
        Locale locale = Locale.US;
        sb.append(new SimpleDateFormat("dd MMM yyyy", locale).format(this.f0.getTime()));
        sb.append(" to ");
        sb.append(new SimpleDateFormat("dd MMM yyyy", locale).format(this.e0.getTime()));
        return sb.toString();
    }
}
