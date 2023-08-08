package com.expense.manager.widget;

import android.content.Context;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.expense.manager.R;

public class NumberPicker extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener, View.OnLongClickListener {
    private static final int DEFAULT_MAX = 31;
    private static final int DEFAULT_MIN = 1;
    /* access modifiers changed from: private */
    public static final char[] DIGIT_CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static final Formatter TWO_DIGIT_FORMATTER = new Formatter() {
        final StringBuilder a;
        final java.util.Formatter b;
        final Object[] c = new Object[1];

        {
            StringBuilder sb = new StringBuilder();
            this.a = sb;
            this.b = new java.util.Formatter(sb);
        }

        public String toString(int i) {
            this.c[0] = Integer.valueOf(i);
            StringBuilder sb = this.a;
            sb.delete(0, sb.length());
            this.b.format("%02d", this.c);
            return this.b.toString();
        }
    };
    protected int a;
    protected int b;
    protected int c;
    protected int d;
    /* access modifiers changed from: private */
    public boolean mDecrement;
    private NumberPickerButton mDecrementButton;
    /* access modifiers changed from: private */
    public String[] mDisplayedValues;
    private Formatter mFormatter;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public boolean mIncrement;
    private NumberPickerButton mIncrementButton;
    private OnChangedListener mListener;
    /* access modifiers changed from: private */
    public final InputFilter mNumberInputFilter;
    private final Runnable mRunnable;
    /* access modifiers changed from: private */
    public long mSpeed;
    private final EditText mText;

    public interface Formatter {
        String toString(int i);
    }

    public interface OnChangedListener {
        void onChanged(NumberPicker numberPicker, int i, int i2);
    }

    public NumberPicker(Context context) {
        this(context, (AttributeSet) null);
    }

    public NumberPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NumberPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mRunnable = new Runnable() {
            public void run() {
                if (NumberPicker.this.mIncrement) {
                    NumberPicker numberPicker = NumberPicker.this;
                    numberPicker.h(numberPicker.a + 1);
                    NumberPicker.this.mHandler.postDelayed(this, NumberPicker.this.mSpeed);
                } else if (NumberPicker.this.mDecrement) {
                    NumberPicker numberPicker2 = NumberPicker.this;
                    numberPicker2.h(numberPicker2.a - 1);
                    NumberPicker.this.mHandler.postDelayed(this, NumberPicker.this.mSpeed);
                }
            }
        };
        this.mSpeed = 300;
        setOrientation(1);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.dialog_number_picker, this, true);
        this.mHandler = new Handler();
        NumberPickerInputFilter numberPickerInputFilter = new NumberPickerInputFilter();
        this.mNumberInputFilter = new NumberRangeKeyListener();
        NumberPickerButton numberPickerButton = (NumberPickerButton) findViewById(R.id.increment);
        this.mIncrementButton = numberPickerButton;
        numberPickerButton.setOnClickListener(this);
        this.mIncrementButton.setOnLongClickListener(this);
        this.mIncrementButton.setNumberPicker(this);
        NumberPickerButton numberPickerButton2 = (NumberPickerButton) findViewById(R.id.decrement);
        this.mDecrementButton = numberPickerButton2;
        numberPickerButton2.setOnClickListener(this);
        this.mDecrementButton.setOnLongClickListener(this);
        this.mDecrementButton.setNumberPicker(this);
        EditText editText = (EditText) findViewById(R.id.timepicker_input);
        this.mText = editText;
        editText.setOnFocusChangeListener(this);
        editText.setFilters(new InputFilter[]{numberPickerInputFilter});
        editText.setRawInputType(2);
        if (!isEnabled()) {
            setEnabled(false);
        }
        this.d = 1;
        this.b = 31;
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mIncrementButton.setEnabled(z);
        this.mDecrementButton.setEnabled(z);
        this.mText.setEnabled(z);
    }

    public void setOnChangeListener(OnChangedListener onChangedListener) {
        this.mListener = onChangedListener;
    }

    public void setFormatter(Formatter formatter) {
        this.mFormatter = formatter;
    }

    public void setRange(int i, int i2) {
        this.d = i;
        this.b = i2;
        this.a = i;
        j();
    }

    public void setRange(int i, int i2, String[] strArr) {
        this.mDisplayedValues = strArr;
        this.d = i;
        this.b = i2;
        this.a = i;
        j();
    }

    public void setCurrent(int i) {
        this.a = i;
        j();
    }

    public void setSpeed(long j) {
        this.mSpeed = j;
    }

    public void onClick(View view) {
        validateInput(this.mText);
        if (!this.mText.hasFocus()) {
            this.mText.requestFocus();
        }
        if (R.id.increment == view.getId()) {
            h(this.a + 1);
        } else if (R.id.decrement == view.getId()) {
            h(this.a - 1);
        }
    }

    private String formatNumber(int i) {
        Formatter formatter = this.mFormatter;
        if (formatter != null) {
            return formatter.toString(i);
        }
        return String.valueOf(i);
    }

    /* access modifiers changed from: protected */
    public void h(int i) {
        if (i > this.b) {
            i = this.d;
        } else if (i < this.d) {
            i = this.b;
        }
        this.c = this.a;
        this.a = i;
        i();
        j();
    }

    /* access modifiers changed from: protected */
    public void i() {
        OnChangedListener onChangedListener = this.mListener;
        if (onChangedListener != null) {
            onChangedListener.onChanged(this, this.c, this.a);
        }
    }

    /* access modifiers changed from: protected */
    public void j() {
        String[] strArr = this.mDisplayedValues;
        if (strArr == null) {
            this.mText.setText(formatNumber(this.a));
        } else {
            this.mText.setText(strArr[this.a - this.d]);
        }
        EditText editText = this.mText;
        editText.setSelection(editText.getText().length());
    }

    private void validateCurrentView(CharSequence charSequence) {
        int i;
        int selectedPos = getSelectedPos(charSequence.toString());
        if (selectedPos >= this.d && selectedPos <= this.b && (i = this.a) != selectedPos) {
            this.c = i;
            this.a = selectedPos;
            i();
        }
        j();
    }

    public void onFocusChange(View view, boolean z) {
        if (!z) {
            validateInput(view);
        }
    }

    private void validateInput(View view) {
        String valueOf = String.valueOf(((TextView) view).getText());
        if ("".equals(valueOf)) {
            j();
        } else {
            validateCurrentView(valueOf);
        }
    }

    public boolean onLongClick(View view) {
        this.mText.clearFocus();
        if (R.id.increment == view.getId()) {
            this.mIncrement = true;
            this.mHandler.post(this.mRunnable);
        } else if (R.id.decrement == view.getId()) {
            this.mDecrement = true;
            this.mHandler.post(this.mRunnable);
        }
        return true;
    }

    public void cancelIncrement() {
        this.mIncrement = false;
    }

    public void cancelDecrement() {
        this.mDecrement = false;
    }

    private class NumberPickerInputFilter implements InputFilter {
        private NumberPickerInputFilter() {
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            if (NumberPicker.this.mDisplayedValues == null) {
                return NumberPicker.this.mNumberInputFilter.filter(charSequence, i, i2, spanned, i3, i4);
            }
            String valueOf = String.valueOf(charSequence.subSequence(i, i2));
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(spanned.subSequence(0, i3)));
            sb.append(valueOf);
            sb.append(spanned.subSequence(i4, spanned.length()));
            String lowerCase = String.valueOf(sb.toString()).toLowerCase();
            for (String str : NumberPicker.this.mDisplayedValues) {
                if (str.toLowerCase().startsWith(lowerCase)) {
                    return valueOf;
                }
            }
            return "";
        }
    }

    private class NumberRangeKeyListener extends NumberKeyListener {
        public int getInputType() {
            return 2;
        }

        private NumberRangeKeyListener() {
        }

        /* access modifiers changed from: protected */
        public char[] getAcceptedChars() {
            return NumberPicker.DIGIT_CHARACTERS;
        }

        public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
            CharSequence filter = super.filter(charSequence, i, i2, spanned, i3, i4);
            if (filter == null) {
                filter = charSequence.subSequence(i, i2);
            }
            String str = String.valueOf(spanned.subSequence(0, i3)) + filter + spanned.subSequence(i4, spanned.length());
            if ("".equals(str)) {
                return str;
            }
            if (NumberPicker.this.getSelectedPos(str) > NumberPicker.this.b) {
                return "";
            }
            return filter;
        }
    }

    public int getSelectedPos(String str) {
        if (this.mDisplayedValues == null) {
            return Integer.parseInt(str);
        }
        for (int i = 0; i < this.mDisplayedValues.length; i++) {
            str = str.toLowerCase();
            if (this.mDisplayedValues[i].toLowerCase().startsWith(str)) {
                return this.d + i;
            }
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return this.d;
        }
    }

    public int getCurrent() {
        return this.a;
    }
}
