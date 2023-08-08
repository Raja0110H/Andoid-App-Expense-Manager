package com.expense.manager.widget.SortListView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

public class DragSortItemView extends ViewGroup {
    private int mGravity = 48;

    public DragSortItemView(Context context) {
        super(context);
        setLayoutParams(new AbsListView.LayoutParams(-1, -2));
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    public int getGravity() {
        return this.mGravity;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View child = getChildAt(0);
        if (child != null) {
            if (this.mGravity == 48) {
                child.layout(0, 0, getMeasuredWidth(), child.getMeasuredHeight());
            } else {
                child.layout(0, getMeasuredHeight() - child.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        View child = getChildAt(0);
        if (child == null) {
            setMeasuredDimension(0, width);
            return;
        }
        if (child.isLayoutRequested()) {
            measureChild(child, widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, 0));
        }
        if (heightMode == 0) {
            LayoutParams lp = getLayoutParams();
            if (lp.height > 0) {
                height = lp.height;
            } else {
                height = child.getMeasuredHeight();
            }
        }
        setMeasuredDimension(width, height);
    }
}
