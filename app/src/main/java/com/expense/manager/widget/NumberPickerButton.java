package com.expense.manager.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageButton;
import com.expense.manager.R;

public class NumberPickerButton extends ImageButton {
    private NumberPicker mNumberPicker;

    public NumberPickerButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public NumberPickerButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NumberPickerButton(Context context) {
        super(context);
    }

    public void setNumberPicker(NumberPicker numberPicker) {
        this.mNumberPicker = numberPicker;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        cancelLongpressIfRequired(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        cancelLongpressIfRequired(motionEvent);
        return super.onTrackballEvent(motionEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 23 || i == 66) {
            cancelLongpress();
        }
        return super.onKeyUp(i, keyEvent);
    }

    private void cancelLongpressIfRequired(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
            cancelLongpress();
        }
    }

    private void cancelLongpress() {
        if (R.id.increment == getId()) {
            this.mNumberPicker.cancelIncrement();
        } else if (R.id.decrement == getId()) {
            this.mNumberPicker.cancelDecrement();
        }
    }
}
