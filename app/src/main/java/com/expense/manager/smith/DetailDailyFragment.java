package com.expense.manager.smith;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.expense.manager.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailDailyFragment extends DetailFragment {
    Calendar e0;
    Calendar f0;

    public void onViewCreated(View view, Bundle bundle) {
        Calendar instance = Calendar.getInstance();
        this.W = instance;
        instance.set(11, 0);
        this.W.clear(12);
        this.W.clear(13);
        this.W.clear(14);
        getView().findViewById(R.id.nextMonth).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                DetailDailyFragment.this.W.add(6, 1);
                DetailDailyFragment.this.updateScreen();
            }
        });
        getView().findViewById(R.id.prevMonth).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                DetailDailyFragment.this.W.add(6, -1);
                DetailDailyFragment.this.updateScreen();
            }
        });
        updateScreen();
    }

    public void updateScreen() {
        this.f0 = (Calendar) this.W.clone();
        Calendar calendar = (Calendar) this.W.clone();
        this.e0 = calendar;
        calendar.add(6, 1);
        this.e0.add(14, -1);
        ((TextView) getView().findViewById(R.id.currentMonth)).setText(new SimpleDateFormat("dd MMM yyyy", Locale.US).format(this.f0.getTime()));
        SimpleDateFormat simpleDateFormat = MyUtils.sdfDatabase;
        this.b0 = simpleDateFormat.format(this.f0.getTime());
        this.c0 = simpleDateFormat.format(this.e0.getTime());
        updateUI();
    }

    public String getMessageText() {
        return new SimpleDateFormat("dd MMM yyyy", Locale.US).format(this.f0.getTime());
    }
}
