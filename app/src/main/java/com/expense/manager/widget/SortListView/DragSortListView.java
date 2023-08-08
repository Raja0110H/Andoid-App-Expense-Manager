package com.expense.manager.widget.SortListView;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.expense.manager.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DragSortListView extends ListView {
    private static final int DRAGGING = 4;
    public static final int DRAG_NEG_X = 2;
    public static final int DRAG_NEG_Y = 8;
    public static final int DRAG_POS_X = 1;
    public static final int DRAG_POS_Y = 4;
    private static final int DROPPING = 2;
    private static final int IDLE = 0;
    private static final int NO_CANCEL = 0;
    private static final int ON_INTERCEPT_TOUCH_EVENT = 2;
    private static final int ON_TOUCH_EVENT = 1;
    private static final int REMOVING = 1;
    private static final int STOPPED = 3;
    private static final int sCacheSize = 3;
    private AdapterWrapper mAdapterWrapper;
    private boolean mAnimate = false;
    /* access modifiers changed from: private */
    public boolean mBlockLayoutRequests = false;
    private MotionEvent mCancelEvent;
    private int mCancelMethod = 0;
    private HeightCache mChildHeightCache = new HeightCache(this, 3);
    private float mCurrFloatAlpha = 1.0f;
    private int mDownScrollStartY;
    /* access modifiers changed from: private */
    public float mDownScrollStartYF;
    private int mDragDeltaX;
    /* access modifiers changed from: private */
    public int mDragDeltaY;
    /* access modifiers changed from: private */
    public float mDragDownScrollHeight;
    private float mDragDownScrollStartFrac = 0.33333334f;
    private boolean mDragEnabled = true;
    private int mDragFlags = 0;
    private DragListener mDragListener;
    private DragScroller mDragScroller;
    private DragSortTracker mDragSortTracker;
    private int mDragStartY;
    /* access modifiers changed from: private */
    public int mDragState = 0;
    /* access modifiers changed from: private */
    public float mDragUpScrollHeight;
    private float mDragUpScrollStartFrac = 0.33333334f;
    private DropAnimator mDropAnimator;
    private DropListener mDropListener;
    /* access modifiers changed from: private */
    public int mFirstExpPos;
    private float mFloatAlpha = 1.0f;
    /* access modifiers changed from: private */
    public Point mFloatLoc = new Point();
    /* access modifiers changed from: private */
    public int mFloatPos;
    private View mFloatView;
    /* access modifiers changed from: private */
    public int mFloatViewHeight;
    /* access modifiers changed from: private */
    public int mFloatViewHeightHalf;
    private boolean mFloatViewInvalidated = false;
    private FloatViewManager mFloatViewManager = null;
    /* access modifiers changed from: private */
    public int mFloatViewMid;
    private boolean mFloatViewOnMeasured = false;
    private boolean mIgnoreTouchEvent = false;
    private boolean mInTouchEvent = false;
    /* access modifiers changed from: private */
    public int mItemHeightCollapsed = 1;
    private boolean mLastCallWasIntercept = false;
    private int mLastX;
    /* access modifiers changed from: private */
    public int mLastY;
    private LiftAnimator mLiftAnimator;
    private boolean mListViewIntercepted = false;
    /* access modifiers changed from: private */
    public float mMaxScrollSpeed = 0.5f;
    private DataSetObserver mObserver;
    private int mOffsetX;
    private int mOffsetY;
    private RemoveAnimator mRemoveAnimator;
    private RemoveListener mRemoveListener;
    /* access modifiers changed from: private */
    public float mRemoveVelocityX = 0.0f;
    private View[] mSampleViewTypes = new View[1];
    /* access modifiers changed from: private */
    public DragScrollProfile mScrollProfile = new DragScrollProfile() {
        public float getSpeed(float w, long t) {
            return DragSortListView.this.mMaxScrollSpeed * w;
        }
    };
    /* access modifiers changed from: private */
    public int mSecondExpPos;
    private float mSlideFrac = 0.0f;
    private float mSlideRegionFrac = 0.25f;
    /* access modifiers changed from: private */
    public int mSrcPos;
    private Point mTouchLoc = new Point();
    private boolean mTrackDragSort = false;
    private int mUpScrollStartY;
    /* access modifiers changed from: private */
    public float mUpScrollStartYF;
    /* access modifiers changed from: private */
    public boolean mUseRemoveVelocity;
    private int mWidthMeasureSpec = 0;
    private int mX;
    /* access modifiers changed from: private */
    public int mY;

    public interface DragListener {
        void drag(int i, int i2);
    }

    public interface DragScrollProfile {
        float getSpeed(float f, long j);
    }

    public interface DragSortListener extends DropListener, DragListener, RemoveListener {
    }

    public interface DropListener {
        void drop(int i, int i2);
    }

    public interface FloatViewManager {
        View onCreateFloatView(int i);

        void onDestroyFloatView(View view);

        void onDragFloatView(View view, Point point, Point point2);
    }

    public interface RemoveListener {
        void remove(int i);
    }

    static /* synthetic */ float l(DragSortListView x0, float x1) {
        float f = x0.mRemoveVelocityX + x1;
        x0.mRemoveVelocityX = f;
        return f;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DragSortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        AttributeSet attributeSet = attrs;
        int removeAnimDuration = 150;
        int dropAnimDuration = 150;
        if (attributeSet != null) {
            TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.DragSortListView, 0, 0);
            this.mItemHeightCollapsed = Math.max(1, a.getDimensionPixelSize(1, 1));
            boolean z = a.getBoolean(16, false);
            this.mTrackDragSort = z;
            if (z) {
                this.mDragSortTracker = new DragSortTracker();
            }
            float f = a.getFloat(8, this.mFloatAlpha);
            this.mFloatAlpha = f;
            this.mCurrFloatAlpha = f;
            this.mDragEnabled = a.getBoolean(2, this.mDragEnabled);
            float max = Math.max(0.0f, Math.min(1.0f, 1.0f - a.getFloat(14, 0.75f)));
            this.mSlideRegionFrac = max;
            this.mAnimate = max > 0.0f;
            setDragScrollStart(a.getFloat(4, this.mDragUpScrollStartFrac));
            this.mMaxScrollSpeed = a.getFloat(10, this.mMaxScrollSpeed);
            int removeAnimDuration2 = a.getInt(11, 150);
            int dropAnimDuration2 = a.getInt(6, 150);
            if (a.getBoolean(17, true)) {
                boolean removeEnabled = a.getBoolean(12, false);
                int removeMode = a.getInt(13, 1);
                boolean sortEnabled = a.getBoolean(15, true);
                int dragInitMode = a.getInt(5, 0);
                int dragHandleId = a.getResourceId(3, 0);
                int flingHandleId = a.getResourceId(7, 0);
                int clickRemoveId = a.getResourceId(0, 0);
                int bgColor = a.getColor(9, -16777216);
                DragSortController controller = new DragSortController(this, dragHandleId, dragInitMode, removeMode, clickRemoveId, flingHandleId);
                controller.setRemoveEnabled(removeEnabled);
                controller.setSortEnabled(sortEnabled);
                controller.setBackgroundColor(bgColor);
                this.mFloatViewManager = controller;
                setOnTouchListener(controller);
            }
            a.recycle();
            removeAnimDuration = removeAnimDuration2;
            dropAnimDuration = dropAnimDuration2;
        }
        this.mDragScroller = new DragScroller();
        if (removeAnimDuration > 0) {
            this.mRemoveAnimator = new RemoveAnimator(0.5f, removeAnimDuration);
        }
        if (dropAnimDuration > 0) {
            this.mDropAnimator = new DropAnimator(0.5f, dropAnimDuration);
        }
        this.mCancelEvent = MotionEvent.obtain(0, 0, 3, 0.0f, 0.0f, 0.0f, 0.0f, 0, 0.0f, 0.0f, 0, 0);
        this.mObserver = new DataSetObserver() {
            private void cancel() {
                if (DragSortListView.this.mDragState == 4) {
                    DragSortListView.this.cancelDrag();
                }
            }

            public void onChanged() {
                cancel();
            }

            public void onInvalidated() {
                cancel();
            }
        };
    }

    public void setFloatAlpha(float alpha) {
        this.mCurrFloatAlpha = alpha;
    }

    public float getFloatAlpha() {
        return this.mCurrFloatAlpha;
    }

    public void setMaxScrollSpeed(float max) {
        this.mMaxScrollSpeed = max;
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter != null) {
            this.mAdapterWrapper = new AdapterWrapper(adapter);
            adapter.registerDataSetObserver(this.mObserver);
            if (adapter instanceof DropListener) {
                setDropListener((DropListener) adapter);
            }
            if (adapter instanceof DragListener) {
                setDragListener((DragListener) adapter);
            }
            if (adapter instanceof RemoveListener) {
                setRemoveListener((RemoveListener) adapter);
            }
        } else {
            this.mAdapterWrapper = null;
        }
        super.setAdapter(this.mAdapterWrapper);
    }

    public ListAdapter getInputAdapter() {
        AdapterWrapper adapterWrapper = this.mAdapterWrapper;
        if (adapterWrapper == null) {
            return null;
        }
        return adapterWrapper.getAdapter();
    }

    private class AdapterWrapper extends BaseAdapter {
        private ListAdapter mAdapter;

        public AdapterWrapper(ListAdapter adapter) {
            this.mAdapter = adapter;
            adapter.registerDataSetObserver(new DataSetObserver( ) {
                public void onChanged() {
                    AdapterWrapper.this.notifyDataSetChanged();
                }

                public void onInvalidated() {
                    AdapterWrapper.this.notifyDataSetInvalidated();
                }
            });
        }

        public ListAdapter getAdapter() {
            return this.mAdapter;
        }

        public long getItemId(int position) {
            return this.mAdapter.getItemId(position);
        }

        public Object getItem(int position) {
            return this.mAdapter.getItem(position);
        }

        public int getCount() {
            return this.mAdapter.getCount();
        }

        public boolean areAllItemsEnabled() {
            return this.mAdapter.areAllItemsEnabled();
        }

        public boolean isEnabled(int position) {
            return this.mAdapter.isEnabled(position);
        }

        public int getItemViewType(int position) {
            return this.mAdapter.getItemViewType(position);
        }

        public int getViewTypeCount() {
            return this.mAdapter.getViewTypeCount();
        }

        public boolean hasStableIds() {
            return this.mAdapter.hasStableIds();
        }

        public boolean isEmpty() {
            return this.mAdapter.isEmpty();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            DragSortItemView v;
            if (convertView != null) {
                v = (DragSortItemView) convertView;
                View oldChild = v.getChildAt(0);
                View child = this.mAdapter.getView(position, oldChild, DragSortListView.this);
                if (child != oldChild) {
                    if (oldChild != null) {
                        v.removeViewAt(0);
                    }
                    v.addView(child);
                }
            } else {
                View child2 = this.mAdapter.getView(position, (View) null, DragSortListView.this);
                if (child2 instanceof Checkable) {
                    v = new DragSortItemViewCheckable(DragSortListView.this.getContext());
                } else {
                    v = new DragSortItemView(DragSortListView.this.getContext());
                }
                v.setLayoutParams(new LayoutParams(-1, -2));
                v.addView(child2);
            }
            DragSortListView dragSortListView = DragSortListView.this;
            dragSortListView.adjustItem(dragSortListView.getHeaderViewsCount() + position, v, true);
            return v;
        }
    }

    private void drawDivider(int expPosition, Canvas canvas) {
        ViewGroup expItem;
        int b;
        int t;
        Drawable divider = getDivider();
        int dividerHeight = getDividerHeight();
        if (divider != null && dividerHeight != 0 && (expItem = (ViewGroup) getChildAt(expPosition - getFirstVisiblePosition())) != null) {
            int l = getPaddingLeft();
            int r = getWidth() - getPaddingRight();
            int childHeight = expItem.getChildAt(0).getHeight();
            if (expPosition > this.mSrcPos) {
                t = expItem.getTop() + childHeight;
                b = t + dividerHeight;
            } else {
                b = expItem.getBottom() - childHeight;
                t = b - dividerHeight;
            }
            canvas.save();
            canvas.clipRect(l, t, r, b);
            divider.setBounds(l, t, r, b);
            divider.draw(canvas);
            canvas.restore();
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        float alphaMod;
        super.dispatchDraw(canvas);
        if (this.mDragState != 0) {
            int i = this.mFirstExpPos;
            if (i != this.mSrcPos) {
                drawDivider(i, canvas);
            }
            int i2 = this.mSecondExpPos;
            if (!(i2 == this.mFirstExpPos || i2 == this.mSrcPos)) {
                drawDivider(i2, canvas);
            }
        }
        View view = this.mFloatView;
        if (view != null) {
            int w = view.getWidth();
            int h = this.mFloatView.getHeight();
            int x = this.mFloatLoc.x;
            int width = getWidth();
            if (x < 0) {
                x = -x;
            }
            if (x < width) {
                float alphaMod2 = ((float) (width - x)) / ((float) width);
                alphaMod = alphaMod2 * alphaMod2;
            } else {
                alphaMod = 0.0f;
            }
            int alpha = (int) (this.mCurrFloatAlpha * 255.0f * alphaMod);
            canvas.save();
            Point point = this.mFloatLoc;
            canvas.translate((float) point.x, (float) point.y);
            canvas.clipRect(0, 0, w, h);
            canvas.saveLayerAlpha(0.0f, 0.0f, (float) w, (float) h, alpha, 31);
            this.mFloatView.draw(canvas);
            canvas.restore();
            canvas.restore();
        }
    }

    /* access modifiers changed from: private */
    public int getItemHeight(int position) {
        View v = getChildAt(position - getFirstVisiblePosition());
        if (v != null) {
            return v.getHeight();
        }
        return calcItemHeight(position, getChildHeight(position));
    }

    private void printPosData() {
        Log.d("mobeta", "mSrcPos=" + this.mSrcPos + " mFirstExpPos=" + this.mFirstExpPos + " mSecondExpPos=" + this.mSecondExpPos);
    }

    private class HeightCache {
        private SparseIntArray mMap;
        private int mMaxSize;
        private ArrayList<Integer> mOrder;

        public HeightCache(DragSortListView dragSortListView, int size) {
            this.mMap = new SparseIntArray(size);
            this.mOrder = new ArrayList<>(size);
            this.mMaxSize = size;
        }

        public void add(int position, int height) {
            int currHeight = this.mMap.get(position, -1);
            if (currHeight != height) {
                if (currHeight != -1) {
                    this.mOrder.remove(Integer.valueOf(position));
                } else if (this.mMap.size() == this.mMaxSize) {
                    this.mMap.delete(this.mOrder.remove(0).intValue());
                }
                this.mMap.put(position, height);
                this.mOrder.add(Integer.valueOf(position));
            }
        }

        public int get(int position) {
            return this.mMap.get(position, -1);
        }

        public void clear() {
            this.mMap.clear();
            this.mOrder.clear();
        }
    }

    /* access modifiers changed from: private */
    public int getShuffleEdge(int position, int top) {
        int numHeaders = getHeaderViewsCount();
        int numFooters = getFooterViewsCount();
        if (position <= numHeaders || position >= getCount() - numFooters) {
            return top;
        }
        int divHeight = getDividerHeight();
        int maxBlankHeight = this.mFloatViewHeight - this.mItemHeightCollapsed;
        int childHeight = getChildHeight(position);
        int itemHeight = getItemHeight(position);
        int otop = top;
        int i = this.mSecondExpPos;
        int i2 = this.mSrcPos;
        if (i <= i2) {
            if (position == i && this.mFirstExpPos != i) {
                otop = position == i2 ? (top + itemHeight) - this.mFloatViewHeight : (top + (itemHeight - childHeight)) - maxBlankHeight;
            } else if (position > i && position <= i2) {
                otop = top - maxBlankHeight;
            }
        } else if (position > i2 && position <= this.mFirstExpPos) {
            otop = top + maxBlankHeight;
        } else if (position == i && this.mFirstExpPos != i) {
            otop = top + (itemHeight - childHeight);
        }
        if (position <= i2) {
            return (((this.mFloatViewHeight - divHeight) - getChildHeight(position - 1)) / 2) + otop;
        }
        return (((childHeight - divHeight) - this.mFloatViewHeight) / 2) + otop;
    }

    private boolean updatePositions() {
        int edgeTop;
        int edgeBottom;
        int first = getFirstVisiblePosition();
        int startPos = this.mFirstExpPos;
        View startView = getChildAt(startPos - first);
        if (startView == null) {
            startPos = first + (getChildCount() / 2);
            startView = getChildAt(startPos - first);
        }
        int startTop = startView.getTop();
        int itemHeight = startView.getHeight();
        int edge = getShuffleEdge(startPos, startTop);
        int lastEdge = edge;
        int divHeight = getDividerHeight();
        int itemPos = startPos;
        int itemTop = startTop;
        if (this.mFloatViewMid < edge) {
            while (true) {
                if (itemPos < 0) {
                    break;
                }
                itemPos--;
                itemHeight = getItemHeight(itemPos);
                if (itemPos == 0) {
                    edge = (itemTop - divHeight) - itemHeight;
                    break;
                }
                itemTop -= itemHeight + divHeight;
                edge = getShuffleEdge(itemPos, itemTop);
                if (this.mFloatViewMid >= edge) {
                    break;
                }
                lastEdge = edge;
            }
        } else {
            int count = getCount();
            while (true) {
                if (itemPos >= count) {
                    break;
                } else if (itemPos == count - 1) {
                    edge = itemTop + divHeight + itemHeight;
                    break;
                } else {
                    itemTop += divHeight + itemHeight;
                    itemHeight = getItemHeight(itemPos + 1);
                    edge = getShuffleEdge(itemPos + 1, itemTop);
                    if (this.mFloatViewMid < edge) {
                        break;
                    }
                    lastEdge = edge;
                    itemPos++;
                }
            }
        }
        int count2 = getHeaderViewsCount();
        int numFooters = getFooterViewsCount();
        boolean updated = false;
        int oldFirstExpPos = this.mFirstExpPos;
        int oldSecondExpPos = this.mSecondExpPos;
        int i = first;
        float oldSlideFrac = this.mSlideFrac;
        int i2 = startPos;
        if (this.mAnimate != false) {
            int edgeToEdge = Math.abs(edge - lastEdge);
            View view = startView;
            int i3 = this.mFloatViewMid;
            if (i3 < edge) {
                edgeBottom = edge;
                edgeTop = lastEdge;
            } else {
                edgeTop = edge;
                edgeBottom = lastEdge;
            }
            int i4 = startTop;
            int i5 = itemHeight;
            int slideRgnHeight = (int) (this.mSlideRegionFrac * 0.5f * ((float) edgeToEdge));
            float slideRgnHeightF = (float) slideRgnHeight;
            int i6 = edgeToEdge;
            int edgeToEdge2 = edgeTop + slideRgnHeight;
            int i7 = edge;
            int edge2 = edgeBottom - slideRgnHeight;
            if (i3 < edgeToEdge2) {
                int i8 = slideRgnHeight;
                this.mFirstExpPos = itemPos - 1;
                this.mSecondExpPos = itemPos;
                this.mSlideFrac = (((float) (edgeToEdge2 - i3)) * 0.5f) / slideRgnHeightF;
            } else {
                if (i3 < edge2) {
                    this.mFirstExpPos = itemPos;
                    this.mSecondExpPos = itemPos;
                } else {
                    this.mFirstExpPos = itemPos;
                    this.mSecondExpPos = itemPos + 1;
                    this.mSlideFrac = ((((float) (edgeBottom - i3)) / slideRgnHeightF) + 1.0f) * 0.5f;
                }
            }
        } else {
            int i9 = startTop;
            int i10 = itemHeight;
            int i11 = edge;
            this.mFirstExpPos = itemPos;
            this.mSecondExpPos = itemPos;
        }
        if (this.mFirstExpPos < count2) {
            itemPos = count2;
            this.mFirstExpPos = itemPos;
            this.mSecondExpPos = itemPos;
        } else if (this.mSecondExpPos >= getCount() - numFooters) {
            itemPos = (getCount() - numFooters) - 1;
            this.mFirstExpPos = itemPos;
            this.mSecondExpPos = itemPos;
        }
        if (!(this.mFirstExpPos == oldFirstExpPos && this.mSecondExpPos == oldSecondExpPos && this.mSlideFrac == oldSlideFrac)) {
            updated = true;
        }
        int i12 = this.mFloatPos;
        if (itemPos == i12) {
            return updated;
        }
        DragListener dragListener = this.mDragListener;
        if (dragListener != null) {
            dragListener.drag(i12 - count2, itemPos - count2);
        }
        this.mFloatPos = itemPos;
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mTrackDragSort) {
            this.mDragSortTracker.appendState();
        }
    }

    private class SmoothAnimator implements Runnable {
        protected long a;
        private float mA;
        private float mAlpha;
        private float mB;
        private float mC;
        private boolean mCanceled;
        private float mD;
        private float mDurationF;

        public SmoothAnimator(float smoothness, int duration) {
            this.mAlpha = smoothness;
            this.mDurationF = (float) duration;
            float f = 1.0f / ((smoothness * 2.0f) * (1.0f - smoothness));
            this.mD = f;
            this.mA = f;
            this.mB = smoothness / ((smoothness - 1.0f) * 2.0f);
            this.mC = 1.0f / (1.0f - smoothness);
        }

        public float transform(float frac) {
            float f = this.mAlpha;
            if (frac < f) {
                return this.mA * frac * frac;
            }
            if (frac < 1.0f - f) {
                return this.mB + (this.mC * frac);
            }
            return 1.0f - ((this.mD * (frac - 1.0f)) * (frac - 1.0f));
        }

        public void start() {
            this.a = SystemClock.uptimeMillis();
            this.mCanceled = false;
            onStart();
            DragSortListView.this.post(this);
        }

        public void cancel() {
            this.mCanceled = true;
        }

        public void onStart() {
        }

        public void onUpdate(float frac, float smoothFrac) {
        }

        public void onStop() {
        }

        public void run() {
            if (!this.mCanceled) {
                float fraction = ((float) (SystemClock.uptimeMillis() - this.a)) / this.mDurationF;
                if (fraction >= 1.0f) {
                    onUpdate(1.0f, 1.0f);
                    onStop();
                    return;
                }
                onUpdate(fraction, transform(fraction));
                DragSortListView.this.post(this);
            }
        }
    }

    private class LiftAnimator extends SmoothAnimator {
        private float mFinalDragDeltaY;
        private float mInitDragDeltaY;

        public LiftAnimator(float smoothness, int duration) {
            super(smoothness, duration);
        }

        public void onStart() {
            this.mInitDragDeltaY = (float) DragSortListView.this.mDragDeltaY;
            this.mFinalDragDeltaY = (float) DragSortListView.this.mFloatViewHeightHalf;
        }

        public void onUpdate(float frac, float smoothFrac) {
            if (DragSortListView.this.mDragState != 4) {
                cancel();
                return;
            }
            int unused = DragSortListView.this.mDragDeltaY = (int) ((this.mFinalDragDeltaY * smoothFrac) + ((1.0f - smoothFrac) * this.mInitDragDeltaY));
            DragSortListView.this.mFloatLoc.y = DragSortListView.this.mY - DragSortListView.this.mDragDeltaY;
            DragSortListView.this.doDragFloatView(true);
        }
    }

    private class DropAnimator extends SmoothAnimator {
        private int mDropPos;
        private float mInitDeltaX;
        private float mInitDeltaY;
        private int srcPos;

        public DropAnimator(float smoothness, int duration) {
            super(smoothness, duration);
        }

        public void onStart() {
            this.mDropPos = DragSortListView.this.mFloatPos;
            this.srcPos = DragSortListView.this.mSrcPos;
            int unused = DragSortListView.this.mDragState = 2;
            this.mInitDeltaY = (float) (DragSortListView.this.mFloatLoc.y - getTargetY());
            this.mInitDeltaX = (float) (DragSortListView.this.mFloatLoc.x - DragSortListView.this.getPaddingLeft());
        }

        private int getTargetY() {
            int first = DragSortListView.this.getFirstVisiblePosition();
            int otherAdjust = (DragSortListView.this.mItemHeightCollapsed + DragSortListView.this.getDividerHeight()) / 2;
            View v = DragSortListView.this.getChildAt(this.mDropPos - first);
            if (v != null) {
                int i = this.mDropPos;
                int i2 = this.srcPos;
                if (i == i2) {
                    return v.getTop();
                }
                if (i < i2) {
                    return v.getTop() - otherAdjust;
                }
                return (v.getBottom() + otherAdjust) - DragSortListView.this.mFloatViewHeight;
            }
            cancel();
            return -1;
        }

        public void onUpdate(float frac, float smoothFrac) {
            int targetY = getTargetY();
            float deltaX = (float) (DragSortListView.this.mFloatLoc.x - DragSortListView.this.getPaddingLeft());
            float f = 1.0f - smoothFrac;
            if (f < Math.abs(((float) (DragSortListView.this.mFloatLoc.y - targetY)) / this.mInitDeltaY) || f < Math.abs(deltaX / this.mInitDeltaX)) {
                DragSortListView.this.mFloatLoc.y = ((int) (this.mInitDeltaY * f)) + targetY;
                DragSortListView.this.mFloatLoc.x = DragSortListView.this.getPaddingLeft() + ((int) (this.mInitDeltaX * f));
                DragSortListView.this.doDragFloatView(true);
            }
        }

        public void onStop() {
            DragSortListView.this.dropFloatView();
        }
    }

    private class RemoveAnimator extends SmoothAnimator {
        private int mFirstChildHeight = -1;
        private int mFirstPos;
        private float mFirstStartBlank;
        private float mFloatLocX;
        private int mSecondChildHeight = -1;
        private int mSecondPos;
        private float mSecondStartBlank;
        private int srcPos;

        public RemoveAnimator(float smoothness, int duration) {
            super(smoothness, duration);
        }

        public void onStart() {
            int i = -1;
            this.mFirstChildHeight = -1;
            this.mSecondChildHeight = -1;
            this.mFirstPos = DragSortListView.this.mFirstExpPos;
            this.mSecondPos = DragSortListView.this.mSecondExpPos;
            this.srcPos = DragSortListView.this.mSrcPos;
            int unused = DragSortListView.this.mDragState = 1;
            this.mFloatLocX = (float) DragSortListView.this.mFloatLoc.x;
            if (DragSortListView.this.mUseRemoveVelocity) {
                float minVelocity = ((float) DragSortListView.this.getWidth()) * 2.0f;
                if (DragSortListView.this.mRemoveVelocityX == 0.0f) {
                    DragSortListView dragSortListView = DragSortListView.this;
                    if (this.mFloatLocX >= 0.0f) {
                        i = 1;
                    }
                    float unused2 = dragSortListView.mRemoveVelocityX = ((float) i) * minVelocity;
                    return;
                }
                float minVelocity2 = minVelocity * 2.0f;
                if (DragSortListView.this.mRemoveVelocityX < 0.0f && DragSortListView.this.mRemoveVelocityX > (-minVelocity2)) {
                    float unused3 = DragSortListView.this.mRemoveVelocityX = -minVelocity2;
                } else if (DragSortListView.this.mRemoveVelocityX > 0.0f && DragSortListView.this.mRemoveVelocityX < minVelocity2) {
                    float unused4 = DragSortListView.this.mRemoveVelocityX = minVelocity2;
                }
            } else {
                DragSortListView.this.destroyFloatView();
            }
        }

        public void onUpdate(float frac, float smoothFrac) {
            View item;
            float f = 1.0f - smoothFrac;
            int firstVis = DragSortListView.this.getFirstVisiblePosition();
            View item2 = DragSortListView.this.getChildAt(this.mFirstPos - firstVis);
            if (DragSortListView.this.mUseRemoveVelocity) {
                float dt = ((float) (SystemClock.uptimeMillis() - this.a)) / 1000.0f;
                if (dt != 0.0f) {
                    float dx = DragSortListView.this.mRemoveVelocityX * dt;
                    int w = DragSortListView.this.getWidth();
                    DragSortListView dragSortListView = DragSortListView.this;
                    DragSortListView.l(dragSortListView, ((float) (dragSortListView.mRemoveVelocityX > 0.0f ? 1 : -1)) * dt * ((float) w));
                    this.mFloatLocX += dx;
                    Point F = DragSortListView.this.mFloatLoc;
                    float f2 = this.mFloatLocX;
                    F.x = (int) f2;
                    if (f2 < ((float) w) && f2 > ((float) (-w))) {
                        this.a = SystemClock.uptimeMillis();
                        DragSortListView.this.doDragFloatView(true);
                        return;
                    }
                } else {
                    return;
                }
            }
            if (item2 != null) {
                if (this.mFirstChildHeight == -1) {
                    this.mFirstChildHeight = DragSortListView.this.getChildHeight(this.mFirstPos, item2, false);
                    this.mFirstStartBlank = (float) (item2.getHeight() - this.mFirstChildHeight);
                }
                int blank = Math.max((int) (this.mFirstStartBlank * f), 1);
                ViewGroup.LayoutParams lp = item2.getLayoutParams();
                lp.height = this.mFirstChildHeight + blank;
                item2.setLayoutParams(lp);
            }
            int blank2 = this.mSecondPos;
            if (blank2 != this.mFirstPos && (item = DragSortListView.this.getChildAt(blank2 - firstVis)) != null) {
                if (this.mSecondChildHeight == -1) {
                    this.mSecondChildHeight = DragSortListView.this.getChildHeight(this.mSecondPos, item, false);
                    this.mSecondStartBlank = (float) (item.getHeight() - this.mSecondChildHeight);
                }
                int blank3 = Math.max((int) (this.mSecondStartBlank * f), 1);
                ViewGroup.LayoutParams lp2 = item.getLayoutParams();
                lp2.height = this.mSecondChildHeight + blank3;
                item.setLayoutParams(lp2);
            }
        }

        public void onStop() {
            DragSortListView.this.doRemoveItem();
        }
    }

    public void removeItem(int which) {
        this.mUseRemoveVelocity = false;
        removeItem(which, 0.0f);
    }

    public void removeItem(int which, float velocityX) {
        int i = this.mDragState;
        if (i == 0 || i == 4) {
            if (i == 0) {
                int headerViewsCount = getHeaderViewsCount() + which;
                this.mSrcPos = headerViewsCount;
                this.mFirstExpPos = headerViewsCount;
                this.mSecondExpPos = headerViewsCount;
                this.mFloatPos = headerViewsCount;
                View v = getChildAt(headerViewsCount - getFirstVisiblePosition());
                if (v != null) {
                    v.setVisibility(4);
                }
            }
            this.mDragState = 1;
            this.mRemoveVelocityX = velocityX;
            if (this.mInTouchEvent) {
                switch (this.mCancelMethod) {
                    case 1:
                        super.onTouchEvent(this.mCancelEvent);
                        break;
                    case 2:
                        super.onInterceptTouchEvent(this.mCancelEvent);
                        break;
                }
            }
            RemoveAnimator removeAnimator = this.mRemoveAnimator;
            if (removeAnimator != null) {
                removeAnimator.start();
            } else {
                doRemoveItem(which);
            }
        }
    }

    public void moveItem(int from, int to) {
        if (this.mDropListener != null) {
            int count = getInputAdapter().getCount();
            if (from >= 0 && from < count && to >= 0 && to < count) {
                this.mDropListener.drop(from, to);
            }
        }
    }

    public void cancelDrag() {
        if (this.mDragState == 4) {
            this.mDragScroller.stopScrolling(true);
            destroyFloatView();
            clearPositions();
            adjustAllItems();
            if (this.mInTouchEvent) {
                this.mDragState = 3;
            } else {
                this.mDragState = 0;
            }
        }
    }

    private void clearPositions() {
        this.mSrcPos = -1;
        this.mFirstExpPos = -1;
        this.mSecondExpPos = -1;
        this.mFloatPos = -1;
    }

    /* access modifiers changed from: private */
    public void dropFloatView() {
        int i;
        this.mDragState = 2;
        if (this.mDropListener != null && (i = this.mFloatPos) >= 0 && i < getCount()) {
            int numHeaders = getHeaderViewsCount();
            this.mDropListener.drop(this.mSrcPos - numHeaders, this.mFloatPos - numHeaders);
        }
        destroyFloatView();
        adjustOnReorder();
        clearPositions();
        adjustAllItems();
        if (this.mInTouchEvent) {
            this.mDragState = 3;
        } else {
            this.mDragState = 0;
        }
    }

    /* access modifiers changed from: private */
    public void doRemoveItem() {
        doRemoveItem(this.mSrcPos - getHeaderViewsCount());
    }

    private void doRemoveItem(int which) {
        this.mDragState = 1;
        RemoveListener removeListener = this.mRemoveListener;
        if (removeListener != null) {
            removeListener.remove(which);
        }
        destroyFloatView();
        adjustOnReorder();
        clearPositions();
        if (this.mInTouchEvent) {
            this.mDragState = 3;
        } else {
            this.mDragState = 0;
        }
    }

    private void adjustOnReorder() {
        int firstPos = getFirstVisiblePosition();
        if (this.mSrcPos < firstPos) {
            View v = getChildAt(0);
            int top = 0;
            if (v != null) {
                top = v.getTop();
            }
            setSelectionFromTop(firstPos - 1, top - getPaddingTop());
        }
    }

    public boolean stopDrag(boolean remove) {
        this.mUseRemoveVelocity = false;
        return stopDrag(remove, 0.0f);
    }

    public boolean stopDragWithVelocity(boolean remove, float velocityX) {
        this.mUseRemoveVelocity = true;
        return stopDrag(remove, velocityX);
    }

    public boolean stopDrag(boolean remove, float velocityX) {
        if (this.mFloatView == null) {
            return false;
        }
        this.mDragScroller.stopScrolling(true);
        if (remove) {
            removeItem(this.mSrcPos - getHeaderViewsCount(), velocityX);
        } else {
            DropAnimator dropAnimator = this.mDropAnimator;
            if (dropAnimator != null) {
                dropAnimator.start();
            } else {
                dropFloatView();
            }
        }
        if (this.mTrackDragSort) {
            this.mDragSortTracker.stopTracking();
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mIgnoreTouchEvent) {
            this.mIgnoreTouchEvent = false;
            return false;
        } else if (!this.mDragEnabled) {
            return super.onTouchEvent(ev);
        } else {
            boolean more = false;
            boolean lastCallWasIntercept = this.mLastCallWasIntercept;
            this.mLastCallWasIntercept = false;
            if (!lastCallWasIntercept) {
                saveTouchCoords(ev);
            }
            int i = this.mDragState;
            if (i == 4) {
                K(ev);
                return true;
            }
            if (i == 0 && super.onTouchEvent(ev)) {
                more = true;
            }
            switch (ev.getAction() & 255) {
                case 1:
                case 3:
                    doActionUpOrCancel();
                    return more;
                default:
                    if (!more) {
                        return more;
                    }
                    this.mCancelMethod = 1;
                    return more;
            }
        }
    }

    private void doActionUpOrCancel() {
        this.mCancelMethod = 0;
        this.mInTouchEvent = false;
        if (this.mDragState == 3) {
            this.mDragState = 0;
        }
        this.mCurrFloatAlpha = this.mFloatAlpha;
        this.mListViewIntercepted = false;
        this.mChildHeightCache.clear();
    }

    private void saveTouchCoords(MotionEvent ev) {
        int action = ev.getAction() & 255;
        if (action != 0) {
            this.mLastX = this.mX;
            this.mLastY = this.mY;
        }
        this.mX = (int) ev.getX();
        int y = (int) ev.getY();
        this.mY = y;
        if (action == 0) {
            this.mLastX = this.mX;
            this.mLastY = y;
        }
        this.mOffsetX = ((int) ev.getRawX()) - this.mX;
        this.mOffsetY = ((int) ev.getRawY()) - this.mY;
    }

    public boolean listViewIntercepted() {
        return this.mListViewIntercepted;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!this.mDragEnabled) {
            return super.onInterceptTouchEvent(ev);
        }
        saveTouchCoords(ev);
        this.mLastCallWasIntercept = true;
        int action = ev.getAction() & 255;
        if (action == 0) {
            if (this.mDragState != 0) {
                this.mIgnoreTouchEvent = true;
                return true;
            }
            this.mInTouchEvent = true;
        }
        boolean intercept = false;
        if (this.mFloatView == null) {
            if (super.onInterceptTouchEvent(ev)) {
                this.mListViewIntercepted = true;
                intercept = true;
            }
            switch (action) {
                case 1:
                case 3:
                    doActionUpOrCancel();
                    break;
                default:
                    if (!intercept) {
                        this.mCancelMethod = 2;
                        break;
                    } else {
                        this.mCancelMethod = 1;
                        break;
                    }
            }
        } else {
            intercept = true;
        }
        if (action == 1 || action == 3) {
            this.mInTouchEvent = false;
        }
        return intercept;
    }

    public void setDragScrollStart(float heightFraction) {
        setDragScrollStarts(heightFraction, heightFraction);
    }

    public void setDragScrollStarts(float upperFrac, float lowerFrac) {
        if (lowerFrac > 0.5f) {
            this.mDragDownScrollStartFrac = 0.5f;
        } else {
            this.mDragDownScrollStartFrac = lowerFrac;
        }
        if (upperFrac > 0.5f) {
            this.mDragUpScrollStartFrac = 0.5f;
        } else {
            this.mDragUpScrollStartFrac = upperFrac;
        }
        if (getHeight() != 0) {
            updateScrollStarts();
        }
    }

    private void continueDrag(int x, int y) {
        Point point = this.mFloatLoc;
        point.x = x - this.mDragDeltaX;
        point.y = y - this.mDragDeltaY;
        doDragFloatView(true);
        int minY = Math.min(y, this.mFloatViewMid + this.mFloatViewHeightHalf);
        int maxY = Math.max(y, this.mFloatViewMid - this.mFloatViewHeightHalf);
        int currentScrollDir = this.mDragScroller.getScrollDir();
        int i = this.mLastY;
        if (minY > i && minY > this.mDownScrollStartY && currentScrollDir != 1) {
            if (currentScrollDir != -1) {
                this.mDragScroller.stopScrolling(true);
            }
            this.mDragScroller.startScrolling(1);
        } else if (maxY < i && maxY < this.mUpScrollStartY && currentScrollDir != 0) {
            if (currentScrollDir != -1) {
                this.mDragScroller.stopScrolling(true);
            }
            this.mDragScroller.startScrolling(0);
        } else if (maxY >= this.mUpScrollStartY && minY <= this.mDownScrollStartY && this.mDragScroller.isScrolling()) {
            this.mDragScroller.stopScrolling(true);
        }
    }

    private void updateScrollStarts() {
        int padTop = getPaddingTop();
        int listHeight = (getHeight() - padTop) - getPaddingBottom();
        float heightF = (float) listHeight;
        float f = ((float) padTop) + (this.mDragUpScrollStartFrac * heightF);
        this.mUpScrollStartYF = f;
        float f2 = ((float) padTop) + ((1.0f - this.mDragDownScrollStartFrac) * heightF);
        this.mDownScrollStartYF = f2;
        this.mUpScrollStartY = (int) f;
        this.mDownScrollStartY = (int) f2;
        this.mDragUpScrollHeight = f - ((float) padTop);
        this.mDragDownScrollHeight = ((float) (padTop + listHeight)) - f2;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateScrollStarts();
    }

    private void adjustAllItems() {
        int first = getFirstVisiblePosition();
        int last = getLastVisiblePosition();
        int begin = Math.max(0, getHeaderViewsCount() - first);
        int end = Math.min(last - first, ((getCount() - 1) - getFooterViewsCount()) - first);
        for (int i = begin; i <= end; i++) {
            View v = getChildAt(i);
            if (v != null) {
                adjustItem(first + i, v, false);
            }
        }
    }

    private void adjustItem(int position) {
        View v = getChildAt(position - getFirstVisiblePosition());
        if (v != null) {
            adjustItem(position, v, false);
        }
    }

    /* access modifiers changed from: private */
    public void adjustItem(int position, View v, boolean invalidChildHeight) {
        int height;
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (position == this.mSrcPos || position == this.mFirstExpPos || position == this.mSecondExpPos) {
            height = calcItemHeight(position, v, invalidChildHeight);
        } else {
            height = -2;
        }
        if (height != lp.height) {
            lp.height = height;
            v.setLayoutParams(lp);
        }
        if (position == this.mFirstExpPos || position == this.mSecondExpPos) {
            int i = this.mSrcPos;
            if (position < i) {
                ((DragSortItemView) v).setGravity(80);
            } else if (position > i) {
                ((DragSortItemView) v).setGravity(48);
            }
        }
        int oldVis = v.getVisibility();
        int vis = 0;
        if (position == this.mSrcPos && this.mFloatView != null) {
            vis = 4;
        }
        if (vis != oldVis) {
            v.setVisibility(vis);
        }
    }

    /* access modifiers changed from: private */
    public int getChildHeight(int position) {
        View v;
        if (position == this.mSrcPos) {
            return 0;
        }
        View v2 = getChildAt(position - getFirstVisiblePosition());
        if (v2 != null) {
            return getChildHeight(position, v2, false);
        }
        int childHeight = this.mChildHeightCache.get(position);
        if (childHeight != -1) {
            return childHeight;
        }
        ListAdapter adapter = getAdapter();
        int type = adapter.getItemViewType(position);
        int typeCount = adapter.getViewTypeCount();
        if (typeCount != this.mSampleViewTypes.length) {
            this.mSampleViewTypes = new View[typeCount];
        }
        if (type >= 0) {
            View[] viewArr = this.mSampleViewTypes;
            if (viewArr[type] == null) {
                v = adapter.getView(position, (View) null, this);
                this.mSampleViewTypes[type] = v;
            } else {
                v = adapter.getView(position, viewArr[type], this);
            }
        } else {
            v = adapter.getView(position, (View) null, this);
        }
        int childHeight2 = getChildHeight(position, v, true);
        this.mChildHeightCache.add(position, childHeight2);
        return childHeight2;
    }

    /* access modifiers changed from: private */
    public int getChildHeight(int position, View item, boolean invalidChildHeight) {
        View child;
        int i;
        if (position == this.mSrcPos) {
            return 0;
        }
        if (position < getHeaderViewsCount() || position >= getCount() - getFooterViewsCount()) {
            child = item;
        } else {
            child = ((ViewGroup) item).getChildAt(0);
        }
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp != null && (i = lp.height) > 0) {
            return i;
        }
        int childHeight = child.getHeight();
        if (childHeight != 0 && !invalidChildHeight) {
            return childHeight;
        }
        measureItem(child);
        return child.getMeasuredHeight();
    }

    private int calcItemHeight(int position, View item, boolean invalidChildHeight) {
        return calcItemHeight(position, getChildHeight(position, item, invalidChildHeight));
    }

    private int calcItemHeight(int position, int childHeight) {
        int dividerHeight = getDividerHeight();
        boolean isSliding = this.mAnimate && this.mFirstExpPos != this.mSecondExpPos;
        int i = this.mFloatViewHeight;
        int i2 = this.mItemHeightCollapsed;
        int maxNonSrcBlankHeight = i - i2;
        int slideHeight = (int) (this.mSlideFrac * ((float) maxNonSrcBlankHeight));
        int i3 = this.mSrcPos;
        if (position == i3) {
            if (i3 == this.mFirstExpPos) {
                if (isSliding) {
                    return i2 + slideHeight;
                }
                return this.mFloatViewHeight;
            } else if (i3 == this.mSecondExpPos) {
                return i - slideHeight;
            } else {
                return this.mItemHeightCollapsed;
            }
        } else if (position == this.mFirstExpPos) {
            if (isSliding) {
                return childHeight + slideHeight;
            }
            return childHeight + maxNonSrcBlankHeight;
        } else if (position == this.mSecondExpPos) {
            return (childHeight + maxNonSrcBlankHeight) - slideHeight;
        } else {
            return childHeight;
        }
    }

    public void requestLayout() {
        if (!this.mBlockLayoutRequests) {
            super.requestLayout();
        }
    }

    private int adjustScroll(int movePos, View moveItem, int oldFirstExpPos, int oldSecondExpPos) {
        int childHeight = getChildHeight(movePos);
        int moveHeightBefore = moveItem.getHeight();
        int moveHeightAfter = calcItemHeight(movePos, childHeight);
        int moveBlankBefore = moveHeightBefore;
        int moveBlankAfter = moveHeightAfter;
        int i = this.mSrcPos;
        if (movePos != i) {
            moveBlankBefore -= childHeight;
            moveBlankAfter -= childHeight;
        }
        int maxBlank = this.mFloatViewHeight;
        int i2 = this.mFirstExpPos;
        if (!(i == i2 || i == this.mSecondExpPos)) {
            maxBlank -= this.mItemHeightCollapsed;
        }
        if (movePos <= oldFirstExpPos) {
            if (movePos > i2) {
                return 0 + (maxBlank - moveBlankAfter);
            }
            return 0;
        } else if (movePos == oldSecondExpPos) {
            if (movePos <= i2) {
                return 0 + (moveBlankBefore - maxBlank);
            }
            if (movePos == this.mSecondExpPos) {
                return 0 + (moveHeightBefore - moveHeightAfter);
            }
            return 0 + moveBlankBefore;
        } else if (movePos <= i2) {
            return 0 - maxBlank;
        } else {
            if (movePos == this.mSecondExpPos) {
                return 0 - moveBlankAfter;
            }
            return 0;
        }
    }

    private void measureItem(View item) {
        int hspec;
        ViewGroup.LayoutParams lp = item.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(-1, -2);
            item.setLayoutParams(lp);
        }
        int wspec = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, getListPaddingLeft() + getListPaddingRight(), lp.width);
        int i = lp.height;
        if (i > 0) {
            hspec = MeasureSpec.makeMeasureSpec(i, 1);
        } else {
            hspec = MeasureSpec.makeMeasureSpec(0, 0);
        }
        item.measure(wspec, hspec);
    }

    private void measureFloatView() {
        View view = this.mFloatView;
        if (view != null) {
            measureItem(view);
            int measuredHeight = this.mFloatView.getMeasuredHeight();
            this.mFloatViewHeight = measuredHeight;
            this.mFloatViewHeightHalf = measuredHeight / 2;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View view = this.mFloatView;
        if (view != null) {
            if (view.isLayoutRequested()) {
                measureFloatView();
            }
            this.mFloatViewOnMeasured = true;
        }
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    /* access modifiers changed from: protected */
    public void layoutChildren() {
        super.layoutChildren();
        View view = this.mFloatView;
        if (view != null) {
            if (view.isLayoutRequested() && !this.mFloatViewOnMeasured) {
                measureFloatView();
            }
            View view2 = this.mFloatView;
            view2.layout(0, 0, view2.getMeasuredWidth(), this.mFloatView.getMeasuredHeight());
            this.mFloatViewOnMeasured = false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean K(MotionEvent ev) {
        int action = ev.getAction() & 255;
        switch (ev.getAction() & 255) {
            case 1:
                if (this.mDragState == 4) {
                    stopDrag(false);
                }
                doActionUpOrCancel();
                return true;
            case 2:
                continueDrag((int) ev.getX(), (int) ev.getY());
                return true;
            case 3:
                if (this.mDragState == 4) {
                    cancelDrag();
                }
                doActionUpOrCancel();
                return true;
            default:
                return true;
        }
    }

    private void invalidateFloatView() {
        this.mFloatViewInvalidated = true;
    }

    public boolean startDrag(int position, int dragFlags, int deltaX, int deltaY) {
        FloatViewManager floatViewManager;
        View v;
        if (!this.mInTouchEvent || (floatViewManager = this.mFloatViewManager) == null || (v = floatViewManager.onCreateFloatView(position)) == null) {
            return false;
        }
        return startDrag(position, v, dragFlags, deltaX, deltaY);
    }

    public boolean startDrag(int position, View floatView, int dragFlags, int deltaX, int deltaY) {
        if (this.mDragState != 0 || !this.mInTouchEvent || this.mFloatView != null || floatView == null || !this.mDragEnabled) {
            return false;
        }
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        int pos = getHeaderViewsCount() + position;
        this.mFirstExpPos = pos;
        this.mSecondExpPos = pos;
        this.mSrcPos = pos;
        this.mFloatPos = pos;
        this.mDragState = 4;
        this.mDragFlags = 0;
        this.mDragFlags = 0 | dragFlags;
        this.mFloatView = floatView;
        measureFloatView();
        this.mDragDeltaX = deltaX;
        this.mDragDeltaY = deltaY;
        int i = this.mY;
        this.mDragStartY = i;
        Point point = this.mFloatLoc;
        point.x = this.mX - deltaX;
        point.y = i - deltaY;
        View srcItem = getChildAt(this.mSrcPos - getFirstVisiblePosition());
        if (srcItem != null) {
            srcItem.setVisibility(4);
        }
        if (this.mTrackDragSort) {
            this.mDragSortTracker.startTracking();
        }
        switch (this.mCancelMethod) {
            case 1:
                super.onTouchEvent(this.mCancelEvent);
                break;
            case 2:
                super.onInterceptTouchEvent(this.mCancelEvent);
                break;
        }
        requestLayout();
        LiftAnimator liftAnimator = this.mLiftAnimator;
        if (liftAnimator != null) {
            liftAnimator.start();
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void doDragFloatView(boolean forceInvalidate) {
        int movePos = getFirstVisiblePosition() + (getChildCount() / 2);
        View moveItem = getChildAt(getChildCount() / 2);
        if (moveItem != null) {
            doDragFloatView(movePos, moveItem, forceInvalidate);
        }
    }

    /* access modifiers changed from: private */
    public void doDragFloatView(int movePos, View moveItem, boolean forceInvalidate) {
        this.mBlockLayoutRequests = true;
        updateFloatView();
        int oldFirstExpPos = this.mFirstExpPos;
        int oldSecondExpPos = this.mSecondExpPos;
        boolean updated = updatePositions();
        if (updated) {
            adjustAllItems();
            setSelectionFromTop(movePos, (moveItem.getTop() + adjustScroll(movePos, moveItem, oldFirstExpPos, oldSecondExpPos)) - getPaddingTop());
            layoutChildren();
        }
        if (updated || forceInvalidate) {
            invalidate();
        }
        this.mBlockLayoutRequests = false;
    }

    private void updateFloatView() {
        int i;
        int i2;
        if (this.mFloatViewManager != null) {
            this.mTouchLoc.set(this.mX, this.mY);
            this.mFloatViewManager.onDragFloatView(this.mFloatView, this.mFloatLoc, this.mTouchLoc);
        }
        Point point = this.mFloatLoc;
        int floatX = point.x;
        int floatY = point.y;
        int padLeft = getPaddingLeft();
        int i3 = this.mDragFlags;
        if ((i3 & 1) == 0 && floatX > padLeft) {
            this.mFloatLoc.x = padLeft;
        } else if ((i3 & 2) == 0 && floatX < padLeft) {
            this.mFloatLoc.x = padLeft;
        }
        int numHeaders = getHeaderViewsCount();
        int numFooters = getFooterViewsCount();
        int firstPos = getFirstVisiblePosition();
        int lastPos = getLastVisiblePosition();
        int topLimit = getPaddingTop();
        if (firstPos < numHeaders) {
            topLimit = getChildAt((numHeaders - firstPos) - 1).getBottom();
        }
        if ((this.mDragFlags & 8) == 0 && firstPos <= (i2 = this.mSrcPos)) {
            topLimit = Math.max(getChildAt(i2 - firstPos).getTop(), topLimit);
        }
        int bottomLimit = getHeight() - getPaddingBottom();
        if (lastPos >= (getCount() - numFooters) - 1) {
            bottomLimit = getChildAt(((getCount() - numFooters) - 1) - firstPos).getBottom();
        }
        if ((this.mDragFlags & 4) == 0 && lastPos >= (i = this.mSrcPos)) {
            bottomLimit = Math.min(getChildAt(i - firstPos).getBottom(), bottomLimit);
        }
        if (floatY < topLimit) {
            this.mFloatLoc.y = topLimit;
        } else {
            int i4 = this.mFloatViewHeight;
            if (floatY + i4 > bottomLimit) {
                this.mFloatLoc.y = bottomLimit - i4;
            }
        }
        this.mFloatViewMid = this.mFloatLoc.y + this.mFloatViewHeightHalf;
    }

    /* access modifiers changed from: private */
    public void destroyFloatView() {
        View view = this.mFloatView;
        if (view != null) {
            view.setVisibility(View.GONE);
            FloatViewManager floatViewManager = this.mFloatViewManager;
            if (floatViewManager != null) {
                floatViewManager.onDestroyFloatView(this.mFloatView);
            }
            this.mFloatView = null;
            invalidate();
        }
    }

    public void setFloatViewManager(FloatViewManager manager) {
        this.mFloatViewManager = manager;
    }

    public void setDragListener(DragListener l) {
        this.mDragListener = l;
    }

    public void setDragEnabled(boolean enabled) {
        this.mDragEnabled = enabled;
    }

    public boolean isDragEnabled() {
        return this.mDragEnabled;
    }

    public void setDropListener(DropListener l) {
        this.mDropListener = l;
    }

    public void setRemoveListener(RemoveListener l) {
        this.mRemoveListener = l;
    }

    public void setDragSortListener(DragSortListener l) {
        setDropListener(l);
        setDragListener(l);
        setRemoveListener(l);
    }

    public void setDragScrollProfile(DragScrollProfile ssp) {
        if (ssp != null) {
            this.mScrollProfile = ssp;
        }
    }

    public void moveCheckState(int from, int to) {
        SparseBooleanArray cip = getCheckedItemPositions();
        int rangeStart = from;
        int rangeEnd = to;
        if (to < from) {
            rangeStart = to;
            rangeEnd = from;
        }
        int rangeEnd2 = rangeEnd + 1;
        int[] runStart = new int[cip.size()];
        int[] runEnd = new int[cip.size()];
        int runCount = buildRunList(cip, rangeStart, rangeEnd2, runStart, runEnd);
        if (runCount != 1 || runStart[0] != runEnd[0]) {
            if (from < to) {
                for (int i = 0; i != runCount; i++) {
                    setItemChecked(rotate(runStart[i], -1, rangeStart, rangeEnd2), true);
                    setItemChecked(rotate(runEnd[i], -1, rangeStart, rangeEnd2), false);
                }
                return;
            }
            for (int i2 = 0; i2 != runCount; i2++) {
                setItemChecked(runStart[i2], false);
                setItemChecked(runEnd[i2], true);
            }
        }
    }

    public void removeCheckState(int position) {
        SparseBooleanArray cip = getCheckedItemPositions();
        if (cip.size() != 0) {
            int[] runStart = new int[cip.size()];
            int[] runEnd = new int[cip.size()];
            int rangeStart = position;
            int rangeEnd = cip.keyAt(cip.size() - 1) + 1;
            int runCount = buildRunList(cip, rangeStart, rangeEnd, runStart, runEnd);
            for (int i = 0; i != runCount; i++) {
                if (runStart[i] != position && (runEnd[i] >= runStart[i] || runEnd[i] <= position)) {
                    setItemChecked(rotate(runStart[i], -1, rangeStart, rangeEnd), true);
                }
                setItemChecked(rotate(runEnd[i], -1, rangeStart, rangeEnd), false);
            }
        }
    }

    private static int buildRunList(SparseBooleanArray cip, int rangeStart, int rangeEnd, int[] runStart, int[] runEnd) {
        int runCount = 0;
        int i = findFirstSetIndex(cip, rangeStart, rangeEnd);
        if (i == -1) {
            return 0;
        }
        int currentRunStart = cip.keyAt(i);
        int currentRunEnd = currentRunStart + 1;
        for (int i2 = i + 1; i2 < cip.size(); i2++) {
            int keyAt = cip.keyAt(i2);
            int position = keyAt;
            if (keyAt >= rangeEnd) {
                break;
            }
            if (cip.valueAt(i2)) {
                if (position == currentRunEnd) {
                    currentRunEnd++;
                } else {
                    runStart[runCount] = currentRunStart;
                    runEnd[runCount] = currentRunEnd;
                    runCount++;
                    currentRunStart = position;
                    currentRunEnd = position + 1;
                }
            }
        }
        if (currentRunEnd == rangeEnd) {
            currentRunEnd = rangeStart;
        }
        runStart[runCount] = currentRunStart;
        runEnd[runCount] = currentRunEnd;
        int runCount2 = runCount + 1;
        if (runCount2 <= 1 || runStart[0] != rangeStart || runEnd[runCount2 - 1] != rangeStart) {
            return runCount2;
        }
        runStart[0] = runStart[runCount2 - 1];
        return runCount2 - 1;
    }

    private static int rotate(int value, int offset, int lowerBound, int upperBound) {
        int windowSize = upperBound - lowerBound;
        int value2 = value + offset;
        if (value2 < lowerBound) {
            return value2 + windowSize;
        }
        if (value2 >= upperBound) {
            return value2 - windowSize;
        }
        return value2;
    }

    private static int findFirstSetIndex(SparseBooleanArray sba, int rangeStart, int rangeEnd) {
        int size = sba.size();
        int i = insertionIndexForKey(sba, rangeStart);
        while (i < size && sba.keyAt(i) < rangeEnd && !sba.valueAt(i)) {
            i++;
        }
        if (i == size || sba.keyAt(i) >= rangeEnd) {
            return -1;
        }
        return i;
    }

    private static int insertionIndexForKey(SparseBooleanArray sba, int key) {
        int low = 0;
        int high = sba.size();
        while (high - low > 0) {
            int middle = (low + high) >> 1;
            if (sba.keyAt(middle) < key) {
                low = middle + 1;
            } else {
                high = middle;
            }
        }
        return low;
    }

    private class DragScroller implements Runnable {
        public static final int DOWN = 1;
        public static final int STOP = -1;
        public static final int UP = 0;
        private float dt;
        private int dy;
        private boolean mAbort;
        private long mCurrTime;
        private int mFirstFooter;
        private int mLastHeader;
        private long mPrevTime;
        private float mScrollSpeed;
        private boolean mScrolling = false;
        private int scrollDir;
        private long tStart;

        public boolean isScrolling() {
            return this.mScrolling;
        }

        public int getScrollDir() {
            if (this.mScrolling) {
                return this.scrollDir;
            }
            return -1;
        }

        public DragScroller() {
        }

        public void startScrolling(int dir) {
            if (!this.mScrolling) {
                this.mAbort = false;
                this.mScrolling = true;
                long uptimeMillis = SystemClock.uptimeMillis();
                this.tStart = uptimeMillis;
                this.mPrevTime = uptimeMillis;
                this.scrollDir = dir;
                DragSortListView.this.post(this);
            }
        }

        public void stopScrolling(boolean now) {
            if (now) {
                DragSortListView.this.removeCallbacks(this);
                this.mScrolling = false;
                return;
            }
            this.mAbort = true;
        }

        public void run() {
            int movePos;
            if (this.mAbort) {
                this.mScrolling = false;
                return;
            }
            int first = DragSortListView.this.getFirstVisiblePosition();
            int last = DragSortListView.this.getLastVisiblePosition();
            int count = DragSortListView.this.getCount();
            int padTop = DragSortListView.this.getPaddingTop();
            int listHeight = (DragSortListView.this.getHeight() - padTop) - DragSortListView.this.getPaddingBottom();
            int minY = Math.min(DragSortListView.this.mY, DragSortListView.this.mFloatViewMid + DragSortListView.this.mFloatViewHeightHalf);
            int maxY = Math.max(DragSortListView.this.mY, DragSortListView.this.mFloatViewMid - DragSortListView.this.mFloatViewHeightHalf);
            if (this.scrollDir == 0) {
                View v = DragSortListView.this.getChildAt(0);
                if (v == null) {
                    this.mScrolling = false;
                    return;
                } else if (first == 0 && v.getTop() == padTop) {
                    this.mScrolling = false;
                    return;
                } else {
                    this.mScrollSpeed = DragSortListView.this.mScrollProfile.getSpeed((DragSortListView.this.mUpScrollStartYF - ((float) maxY)) / DragSortListView.this.mDragUpScrollHeight, this.mPrevTime);
                }
            } else {
                View v2 = DragSortListView.this.getChildAt(last - first);
                if (v2 == null) {
                    this.mScrolling = false;
                    return;
                } else if (last != count - 1 || v2.getBottom() > listHeight + padTop) {
                    this.mScrollSpeed = -DragSortListView.this.mScrollProfile.getSpeed((((float) minY) - DragSortListView.this.mDownScrollStartYF) / DragSortListView.this.mDragDownScrollHeight, this.mPrevTime);
                } else {
                    this.mScrolling = false;
                    return;
                }
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            this.mCurrTime = uptimeMillis;
            float f = (float) (uptimeMillis - this.mPrevTime);
            this.dt = f;
            int round = Math.round(this.mScrollSpeed * f);
            this.dy = round;
            if (round >= 0) {
                this.dy = Math.min(listHeight, round);
                movePos = first;
            } else {
                this.dy = Math.max(-listHeight, round);
                movePos = last;
            }
            View moveItem = DragSortListView.this.getChildAt(movePos - first);
            int top = moveItem.getTop() + this.dy;
            if (movePos == 0 && top > padTop) {
                top = padTop;
            }
            boolean unused = DragSortListView.this.mBlockLayoutRequests = true;
            DragSortListView.this.setSelectionFromTop(movePos, top - padTop);
            DragSortListView.this.layoutChildren();
            DragSortListView.this.invalidate();
            boolean unused2 = DragSortListView.this.mBlockLayoutRequests = false;
            DragSortListView.this.doDragFloatView(movePos, moveItem, false);
            this.mPrevTime = this.mCurrTime;
            DragSortListView.this.post(this);
        }
    }

    private class DragSortTracker {
        StringBuilder a = new StringBuilder();
        File b;
        private int mNumFlushes = 0;
        private int mNumInBuffer = 0;
        private boolean mTracking = false;

        public DragSortTracker() {
            File file = new File(Environment.getExternalStorageDirectory(), "dslv_state.txt");
            this.b = file;
            if (!file.exists()) {
                try {
                    this.b.createNewFile();
                    Log.d("mobeta", "file created");
                } catch (IOException e) {
                    Log.w("mobeta", "Could not create dslv_state.txt");
                    Log.d("mobeta", e.getMessage());
                }
            }
        }

        public void startTracking() {
            this.a.append("<DSLVStates>\n");
            this.mNumFlushes = 0;
            this.mTracking = true;
        }

        public void appendState() {
            if (this.mTracking) {
                this.a.append("<DSLVState>\n");
                int children = DragSortListView.this.getChildCount();
                int first = DragSortListView.this.getFirstVisiblePosition();
                this.a.append("    <Positions>");
                for (int i = 0; i < children; i++) {
                    StringBuilder sb = this.a;
                    sb.append(first + i);
                    sb.append(",");
                }
                this.a.append("</Positions>\n");
                this.a.append("    <Tops>");
                for (int i2 = 0; i2 < children; i2++) {
                    StringBuilder sb2 = this.a;
                    sb2.append(DragSortListView.this.getChildAt(i2).getTop());
                    sb2.append(",");
                }
                this.a.append("</Tops>\n");
                this.a.append("    <Bottoms>");
                for (int i3 = 0; i3 < children; i3++) {
                    StringBuilder sb3 = this.a;
                    sb3.append(DragSortListView.this.getChildAt(i3).getBottom());
                    sb3.append(",");
                }
                this.a.append("</Bottoms>\n");
                StringBuilder sb4 = this.a;
                sb4.append("    <FirstExpPos>");
                sb4.append(DragSortListView.this.mFirstExpPos);
                sb4.append("</FirstExpPos>\n");
                StringBuilder sb5 = this.a;
                sb5.append("    <FirstExpBlankHeight>");
                DragSortListView dragSortListView = DragSortListView.this;
                int y = dragSortListView.getItemHeight(dragSortListView.mFirstExpPos);
                DragSortListView dragSortListView2 = DragSortListView.this;
                sb5.append(y - dragSortListView2.getChildHeight(dragSortListView2.mFirstExpPos));
                sb5.append("</FirstExpBlankHeight>\n");
                StringBuilder sb6 = this.a;
                sb6.append("    <SecondExpPos>");
                sb6.append(DragSortListView.this.mSecondExpPos);
                sb6.append("</SecondExpPos>\n");
                StringBuilder sb7 = this.a;
                sb7.append("    <SecondExpBlankHeight>");
                DragSortListView dragSortListView3 = DragSortListView.this;
                int y2 = dragSortListView3.getItemHeight(dragSortListView3.mSecondExpPos);
                DragSortListView dragSortListView4 = DragSortListView.this;
                sb7.append(y2 - dragSortListView4.getChildHeight(dragSortListView4.mSecondExpPos));
                sb7.append("</SecondExpBlankHeight>\n");
                StringBuilder sb8 = this.a;
                sb8.append("    <SrcPos>");
                sb8.append(DragSortListView.this.mSrcPos);
                sb8.append("</SrcPos>\n");
                StringBuilder sb9 = this.a;
                sb9.append("    <SrcHeight>");
                sb9.append(DragSortListView.this.mFloatViewHeight + DragSortListView.this.getDividerHeight());
                sb9.append("</SrcHeight>\n");
                StringBuilder sb10 = this.a;
                sb10.append("    <ViewHeight>");
                sb10.append(DragSortListView.this.getHeight());
                sb10.append("</ViewHeight>\n");
                StringBuilder sb11 = this.a;
                sb11.append("    <LastY>");
                sb11.append(DragSortListView.this.mLastY);
                sb11.append("</LastY>\n");
                StringBuilder sb12 = this.a;
                sb12.append("    <FloatY>");
                sb12.append(DragSortListView.this.mFloatViewMid);
                sb12.append("</FloatY>\n");
                this.a.append("    <ShuffleEdges>");
                for (int i4 = 0; i4 < children; i4++) {
                    StringBuilder sb13 = this.a;
                    DragSortListView dragSortListView5 = DragSortListView.this;
                    sb13.append(dragSortListView5.getShuffleEdge(first + i4, dragSortListView5.getChildAt(i4).getTop()));
                    sb13.append(",");
                }
                this.a.append("</ShuffleEdges>\n");
                this.a.append("</DSLVState>\n");
                int i5 = this.mNumInBuffer + 1;
                this.mNumInBuffer = i5;
                if (i5 > 1000) {
                    flush();
                    this.mNumInBuffer = 0;
                }
            }
        }

        public void flush() {
            if (this.mTracking) {
                boolean append = true;
                try {
                    if (this.mNumFlushes == 0) {
                        append = false;
                    }
                    FileWriter writer = new FileWriter(this.b, append);
                    writer.write(this.a.toString());
                    StringBuilder sb = this.a;
                    sb.delete(0, sb.length());
                    writer.flush();
                    writer.close();
                    this.mNumFlushes++;
                } catch (IOException e) {
                }
            }
        }

        public void stopTracking() {
            if (this.mTracking) {
                this.a.append("</DSLVStates>\n");
                flush();
                this.mTracking = false;
            }
        }
    }
}
