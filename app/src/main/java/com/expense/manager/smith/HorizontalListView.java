package com.expense.manager.smith;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;
import java.util.LinkedList;
import java.util.Queue;

public class HorizontalListView extends AdapterView<ListAdapter> {
    protected ListAdapter a;
    protected int b;
    protected int c;
    protected Scroller d;
    public boolean mAlwaysOverrideTouch = true;
    /* access modifiers changed from: private */
    public boolean mDataChanged = false;
    private DataSetObserver mDataObserver = new DataSetObserver() {
        public void onChanged() {
            synchronized (HorizontalListView.this) {
                boolean unused = HorizontalListView.this.mDataChanged = true;
            }
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }

        public void onInvalidated() {
            HorizontalListView.this.reset();
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }
    };
    private int mDisplayOffset = 0;
    private GestureDetector mGesture;
    /* access modifiers changed from: private */
    public int mLeftViewIndex = -1;
    private int mMaxX = Integer.MAX_VALUE;
    private GestureDetector.OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {
        public boolean onDown(MotionEvent motionEvent) {
            return HorizontalListView.this.f(motionEvent);
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return HorizontalListView.this.g(motionEvent, motionEvent2, f, f2);
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            HorizontalListView horizontalListView;
            synchronized (HorizontalListView.this) {
                horizontalListView = HorizontalListView.this;
                horizontalListView.c += (int) f;
            }
            horizontalListView.requestLayout();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            Rect viewRect = new Rect();
            int i = 0;
            while (true) {
                if (i >= HorizontalListView.this.getChildCount()) {
                    break;
                }
                View child = HorizontalListView.this.getChildAt(i);
                viewRect.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
                if (viewRect.contains((int) e.getX(), (int) e.getY())) {
                    if (HorizontalListView.this.mOnItemClicked != null) {
                        OnItemClickListener b = HorizontalListView.this.mOnItemClicked;
                        HorizontalListView horizontalListView = HorizontalListView.this;
                        int c = horizontalListView.mLeftViewIndex + 1 + i;
                        HorizontalListView horizontalListView2 = HorizontalListView.this;
                        b.onItemClick(horizontalListView, child, c, horizontalListView2.a.getItemId(horizontalListView2.mLeftViewIndex + 1 + i));
                    }
                    if (HorizontalListView.this.mOnItemSelected != null) {
                        OnItemSelectedListener d = HorizontalListView.this.mOnItemSelected;
                        HorizontalListView horizontalListView3 = HorizontalListView.this;
                        int c2 = horizontalListView3.mLeftViewIndex + 1 + i;
                        HorizontalListView horizontalListView4 = HorizontalListView.this;
                        d.onItemSelected(horizontalListView3, child, c2, horizontalListView4.a.getItemId(horizontalListView4.mLeftViewIndex + 1 + i));
                    }
                } else {
                    i++;
                }
            }
            return true;
        }

        public void onLongPress(MotionEvent motionEvent) {
            int childCount = HorizontalListView.this.getChildCount();
            int i = 0;
            while (i < childCount) {
                View childAt = HorizontalListView.this.getChildAt(i);
                if (!isEventWithinView(motionEvent, childAt)) {
                    i++;
                } else if (HorizontalListView.this.mOnItemLongClicked != null) {
                    OnItemLongClickListener e = HorizontalListView.this.mOnItemLongClicked;
                    HorizontalListView horizontalListView = HorizontalListView.this;
                    int c = horizontalListView.mLeftViewIndex + 1 + i;
                    HorizontalListView horizontalListView2 = HorizontalListView.this;
                    e.onItemLongClick(horizontalListView, childAt, c, horizontalListView2.a.getItemId(horizontalListView2.mLeftViewIndex + 1 + i));
                    return;
                } else {
                    return;
                }
            }
        }

        private boolean isEventWithinView(MotionEvent motionEvent, View view) {
            Rect rect = new Rect();
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            rect.set(i, i2, view.getWidth() + i, view.getHeight() + i2);
            return rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
        }
    };
    /* access modifiers changed from: private */
    public OnItemClickListener mOnItemClicked;
    /* access modifiers changed from: private */
    public OnItemLongClickListener mOnItemLongClicked;
    /* access modifiers changed from: private */
    public OnItemSelectedListener mOnItemSelected;
    private Queue<View> mRemovedViewQueue = new LinkedList();
    private int mRightViewIndex = 0;

    public View getSelectedView() {
        return null;
    }

    public void setSelection(int i) {
    }

    public HorizontalListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private synchronized void initView() {
        this.mLeftViewIndex = -1;
        this.mRightViewIndex = 0;
        this.mDisplayOffset = 0;
        this.b = 0;
        this.c = 0;
        this.mMaxX = Integer.MAX_VALUE;
        this.d = new Scroller(getContext());
        this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelected = onItemSelectedListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClicked = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClicked = onItemLongClickListener;
    }

    public ListAdapter getAdapter() {
        return this.a;
    }

    public void setAdapter(ListAdapter listAdapter) {
        ListAdapter listAdapter2 = this.a;
        if (listAdapter2 != null) {
            listAdapter2.unregisterDataSetObserver(this.mDataObserver);
        }
        this.a = listAdapter;
        listAdapter.registerDataSetObserver(this.mDataObserver);
        reset();
    }

    public synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    private void addAndMeasureChild(View view, int i) {
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(-1, -1);
        }
        addViewInLayout(view, i, layoutParams, true);
        view.measure(MeasureSpec.makeMeasureSpec(getWidth(), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(getHeight(), Integer.MIN_VALUE));
    }

    /* access modifiers changed from: protected */
    public synchronized void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.a != null) {
            if (this.mDataChanged) {
                int i5 = this.b;
                initView();
                removeAllViewsInLayout();
                this.c = i5;
                this.mDataChanged = false;
            }
            if (this.d.computeScrollOffset()) {
                this.c = this.d.getCurrX();
            }
            if (this.c <= 0) {
                this.c = 0;
                this.d.forceFinished(true);
            }
            int i6 = this.c;
            int i7 = this.mMaxX;
            if (i6 >= i7) {
                this.c = i7;
                this.d.forceFinished(true);
            }
            int i62 = this.b - this.c;
            removeNonVisibleItems(i62);
            fillList(i62);
            positionItems(i62);
            this.b = this.c;
            if (!this.d.isFinished()) {
                post(new Runnable() {
                    public void run() {
                        HorizontalListView.this.requestLayout();
                    }
                });
            }
        }
    }

    private void fillList(int i) {
        View childAt = getChildAt(getChildCount() - 1);
        int i2 = 0;
        fillListRight(childAt != null ? childAt.getRight() : 0, i);
        View childAt2 = getChildAt(0);
        if (childAt2 != null) {
            i2 = childAt2.getLeft();
        }
        fillListLeft(i2, i);
    }

    private void fillListRight(int i, int i2) {
        while (i + i2 < getWidth() && this.mRightViewIndex < this.a.getCount()) {
            View view = this.a.getView(this.mRightViewIndex, this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(view, -1);
            i += view.getMeasuredWidth();
            if (this.mRightViewIndex == this.a.getCount() - 1) {
                this.mMaxX = (this.b + i) - getWidth();
            }
            if (this.mMaxX < 0) {
                this.mMaxX = 0;
            }
            this.mRightViewIndex++;
        }
    }

    private void fillListLeft(int i, int i2) {
        int i3;
        while (i + i2 > 0 && (i3 = this.mLeftViewIndex) >= 0) {
            View view = this.a.getView(i3, this.mRemovedViewQueue.poll(), this);
            addAndMeasureChild(view, 0);
            i -= view.getMeasuredWidth();
            this.mLeftViewIndex--;
            this.mDisplayOffset -= view.getMeasuredWidth();
        }
    }

    private void removeNonVisibleItems(int i) {
        View childAt = getChildAt(0);
        while (childAt != null && childAt.getRight() + i <= 0) {
            this.mDisplayOffset += childAt.getMeasuredWidth();
            this.mRemovedViewQueue.offer(childAt);
            removeViewInLayout(childAt);
            this.mLeftViewIndex++;
            childAt = getChildAt(0);
        }
        View childAt2 = getChildAt(getChildCount() - 1);
        while (childAt2 != null && childAt2.getLeft() + i >= getWidth()) {
            this.mRemovedViewQueue.offer(childAt2);
            removeViewInLayout(childAt2);
            this.mRightViewIndex--;
            childAt2 = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(int i) {
        if (getChildCount() > 0) {
            this.mDisplayOffset += i;
            int i2 = this.mDisplayOffset;
            int i3 = 0;
            while (i3 < getChildCount()) {
                View childAt = getChildAt(i3);
                int measuredWidth = childAt.getMeasuredWidth() + i2;
                childAt.layout(i2, 0, measuredWidth, childAt.getMeasuredHeight());
                i3++;
                i2 = measuredWidth;
            }
        }
    }

    public synchronized void scrollTo(int i) {
        Scroller scroller = this.d;
        int i2 = this.c;
        scroller.startScroll(i2, 0, i - i2, 0);
        requestLayout();
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.mGesture.onTouchEvent(motionEvent) | super.dispatchTouchEvent(motionEvent);
    }

    /* access modifiers changed from: protected */
    public boolean g(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        synchronized (this) {
            this.d.fling(this.c, 0, (int) (-f), 0, 0, this.mMaxX, 0, 0);
        }
        requestLayout();
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean f(MotionEvent motionEvent) {
        this.d.forceFinished(true);
        return true;
    }
}
