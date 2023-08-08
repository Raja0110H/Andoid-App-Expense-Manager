package com.expense.manager.smith;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import com.expense.manager.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailCustomFragment extends DetailFragment {
    Calendar e0;
    Calendar f0;
    private TextView tvEndDate;
    private TextView tvStartDate;

    public class MyDatePickerListner implements DatePickerDialog.OnDateSetListener {
        private Calendar mCalendar;

        public MyDatePickerListner(Calendar calendar) {
            this.mCalendar = calendar;
        }

        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            this.mCalendar.set(i, i2, i3);
            DetailCustomFragment.this.updateScreen();
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Calendar instance = Calendar.getInstance();
        this.W = instance;
        instance.set(11, 0);
        this.W.clear(12);
        this.W.clear(13);
        this.W.clear(14);
        Calendar calendar = (Calendar) this.W.clone();
        this.f0 = calendar;
        calendar.add(6, -17);
        Calendar calendar2 = (Calendar) this.W.clone();
        this.e0 = calendar2;
        calendar2.add(14, -1);
        getView().findViewById(R.id.buttonlayout).setVisibility(View.GONE);
        getView().findViewById(R.id.llDateBar).setVisibility(View.VISIBLE);
        this.tvStartDate = (TextView) getView().findViewById(R.id.tvStartDate);
        this.tvEndDate = (TextView) getView().findViewById(R.id.tvEndDate);
        this.tvStartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                DetailCustomFragment detailCustomFragment = DetailCustomFragment.this;
                detailCustomFragment.updateDateByUser(detailCustomFragment.f0);
            }
        });
        this.tvEndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                DetailCustomFragment detailCustomFragment = DetailCustomFragment.this;
                detailCustomFragment.updateDateByUser(detailCustomFragment.e0);
            }
        });
        updateScreen();
    }

    public void updateDateByUser(Calendar calendar) {
        new DatePickerDialog(getActivity(), new MyDatePickerListner(calendar), calendar.get(1), calendar.get(2), calendar.get(5)).show();
    }

    public void updateScreen() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String string = getString(R.string.start_date, simpleDateFormat.format(this.f0.getTime()));
        String string2 = getString(R.string.end_date, simpleDateFormat.format(this.e0.getTime()));
        this.tvStartDate.setText(string);
        this.tvEndDate.setText(string2);
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
