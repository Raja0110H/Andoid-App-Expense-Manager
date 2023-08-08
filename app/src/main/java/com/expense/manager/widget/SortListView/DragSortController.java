package com.expense.manager.widget.SortListView;

import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class DragSortController extends SimpleFloatViewManager implements View.OnTouchListener, GestureDetector.OnGestureListener {
    public static final int CLICK_REMOVE = 0;
    public static final int FLING_REMOVE = 1;
    public static final int MISS = -1;
    public static final int ON_DOWN = 0;
    public static final int ON_DRAG = 1;
    public static final int ON_LONG_PRESS = 2;
    private boolean mCanDrag;
    private int mClickRemoveHitPos;
    private int mClickRemoveId;
    private int mCurrX;
    private int mCurrY;
    private GestureDetector mDetector;
    private int mDragHandleId;
    private int mDragInitMode;
    private boolean mDragging;
    /* access modifiers changed from: private */
    public DragSortListView mDslv;
    private int mFlingHandleId;
    private int mFlingHitPos;
    private GestureDetector mFlingRemoveDetector;
    private GestureDetector.OnGestureListener mFlingRemoveListener;
    /* access modifiers changed from: private */
    public float mFlingSpeed;
    private int mHitPos;
    /* access modifiers changed from: private */
    public boolean mIsRemoving;
    private int mItemX;
    private int mItemY;
    /* access modifiers changed from: private */
    public int mPositionX;
    /* access modifiers changed from: private */
    public boolean mRemoveEnabled;
    private int mRemoveMode;
    private boolean mSortEnabled;
    private int[] mTempLoc;
    private int mTouchSlop;

    public DragSortController(DragSortListView dslv) {
        this(dslv, 0, 0, 1);
    }

    public DragSortController(DragSortListView dslv, int dragHandleId, int dragInitMode, int removeMode) {
        this(dslv, dragHandleId, dragInitMode, removeMode, 0);
    }

    public DragSortController(DragSortListView dslv, int dragHandleId, int dragInitMode, int removeMode, int clickRemoveId) {
        this(dslv, dragHandleId, dragInitMode, removeMode, clickRemoveId, 0);
    }

    public DragSortController(DragSortListView dslv, int dragHandleId, int dragInitMode, int removeMode, int clickRemoveId, int flingHandleId) {
        super(dslv);
        this.mDragInitMode = 0;
        this.mSortEnabled = true;
        this.mRemoveEnabled = false;
        this.mIsRemoving = false;
        this.mHitPos = -1;
        this.mFlingHitPos = -1;
        this.mClickRemoveHitPos = -1;
        this.mTempLoc = new int[2];
        this.mDragging = false;
        this.mFlingSpeed = 500.0f;
        this.mFlingRemoveListener = new GestureDetector.SimpleOnGestureListener() {
            public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (DragSortController.this.mRemoveEnabled && DragSortController.this.mIsRemoving) {
                    int minPos = DragSortController.this.mDslv.getWidth() / 5;
                    if (velocityX > DragSortController.this.mFlingSpeed) {
                        if (DragSortController.this.mPositionX > (-minPos)) {
                            DragSortController.this.mDslv.stopDragWithVelocity(true, velocityX);
                        }
                    } else if (velocityX < (-DragSortController.this.mFlingSpeed) && DragSortController.this.mPositionX < minPos) {
                        DragSortController.this.mDslv.stopDragWithVelocity(true, velocityX);
                    }
                    boolean unused = DragSortController.this.mIsRemoving = false;
                }
                return false;
            }
        };
        this.mDslv = dslv;
        this.mDetector = new GestureDetector(dslv.getContext(), this);
        GestureDetector gestureDetector = new GestureDetector(dslv.getContext(), this.mFlingRemoveListener);
        this.mFlingRemoveDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
        this.mTouchSlop = ViewConfiguration.get(dslv.getContext()).getScaledTouchSlop();
        this.mDragHandleId = dragHandleId;
        this.mClickRemoveId = clickRemoveId;
        this.mFlingHandleId = flingHandleId;
        setRemoveMode(removeMode);
        setDragInitMode(dragInitMode);
    }

    public int getDragInitMode() {
        return this.mDragInitMode;
    }

    public void setDragInitMode(int mode) {
        this.mDragInitMode = mode;
    }

    public void setSortEnabled(boolean enabled) {
        this.mSortEnabled = enabled;
    }

    public boolean isSortEnabled() {
        return this.mSortEnabled;
    }

    public void setRemoveMode(int mode) {
        this.mRemoveMode = mode;
    }

    public int getRemoveMode() {
        return this.mRemoveMode;
    }

    public void setRemoveEnabled(boolean enabled) {
        this.mRemoveEnabled = enabled;
    }

    public boolean isRemoveEnabled() {
        return this.mRemoveEnabled;
    }

    public void setDragHandleId(int id) {
        this.mDragHandleId = id;
    }

    public void setFlingHandleId(int id) {
        this.mFlingHandleId = id;
    }

    public void setClickRemoveId(int id) {
        this.mClickRemoveId = id;
    }

    public boolean startDrag(int position, int deltaX, int deltaY) {
        int dragFlags = 0;
        if (this.mSortEnabled && !this.mIsRemoving) {
            dragFlags = 0 | 12;
        }
        if (this.mRemoveEnabled && this.mIsRemoving) {
            dragFlags = dragFlags | 1 | 2;
        }
        DragSortListView dragSortListView = this.mDslv;
        boolean startDrag = dragSortListView.startDrag(position - dragSortListView.getHeaderViewsCount(), dragFlags, deltaX, deltaY);
        this.mDragging = startDrag;
        return startDrag;
    }

    public boolean onTouch(View v, MotionEvent ev) {
        if (!this.mDslv.isDragEnabled() || this.mDslv.listViewIntercepted()) {
            return false;
        }
        this.mDetector.onTouchEvent(ev);
        if (this.mRemoveEnabled && this.mDragging && this.mRemoveMode == 1) {
            this.mFlingRemoveDetector.onTouchEvent(ev);
        }
        switch (ev.getAction() & 255) {
            case 0:
                this.mCurrX = (int) ev.getX();
                this.mCurrY = (int) ev.getY();
                break;
            case 1:
                if (this.mRemoveEnabled && this.mIsRemoving) {
                    int x = this.mPositionX;
                    if (x < 0) {
                        x = -x;
                    }
                    if (x > this.mDslv.getWidth() / 2) {
                        this.mDslv.stopDragWithVelocity(true, 0.0f);
                        break;
                    }
                }
                break;
            case 3:
                break;
        }
        this.mIsRemoving = false;
        this.mDragging = false;
        return false;
    }

    public void onDragFloatView(View floatView, Point position, Point touch) {
        if (this.mRemoveEnabled && this.mIsRemoving) {
            this.mPositionX = position.x;
        }
    }

    public int startDragPosition(MotionEvent ev) {
        return dragHandleHitPosition(ev);
    }

    public int startFlingPosition(MotionEvent ev) {
        if (this.mRemoveMode == 1) {
            return flingHandleHitPosition(ev);
        }
        return -1;
    }

    public int dragHandleHitPosition(MotionEvent ev) {
        return viewIdHitPosition(ev, this.mDragHandleId);
    }

    public int flingHandleHitPosition(MotionEvent ev) {
        return viewIdHitPosition(ev, this.mFlingHandleId);
    }

    public int viewIdHitPosition(MotionEvent ev, int id) {
        int i = id;
        int touchPos = this.mDslv.pointToPosition((int) ev.getX(), (int) ev.getY());
        int numHeaders = this.mDslv.getHeaderViewsCount();
        int numFooters = this.mDslv.getFooterViewsCount();
        int count = this.mDslv.getCount();
        if (touchPos == -1 || touchPos < numHeaders || touchPos >= count - numFooters) {
            return -1;
        }
        DragSortListView dragSortListView = this.mDslv;
        View item = dragSortListView.getChildAt(touchPos - dragSortListView.getFirstVisiblePosition());
        int rawX = (int) ev.getRawX();
        int rawY = (int) ev.getRawY();
        View dragBox = i == 0 ? item : item.findViewById(i);
        if (dragBox == null) {
            return -1;
        }
        dragBox.getLocationOnScreen(this.mTempLoc);
        int[] iArr = this.mTempLoc;
        if (rawX <= iArr[0] || rawY <= iArr[1] || rawX >= iArr[0] + dragBox.getWidth() || rawY >= this.mTempLoc[1] + dragBox.getHeight()) {
            return -1;
        }
        this.mItemX = item.getLeft();
        this.mItemY = item.getTop();
        return touchPos;
    }

    public boolean onDown(MotionEvent ev) {
        if (this.mRemoveEnabled && this.mRemoveMode == 0) {
            this.mClickRemoveHitPos = viewIdHitPosition(ev, this.mClickRemoveId);
        }
        int startDragPosition = startDragPosition(ev);
        this.mHitPos = startDragPosition;
        if (startDragPosition != -1 && this.mDragInitMode == 0) {
            startDrag(startDragPosition, ((int) ev.getX()) - this.mItemX, ((int) ev.getY()) - this.mItemY);
        }
        this.mIsRemoving = false;
        this.mCanDrag = true;
        this.mPositionX = 0;
        this.mFlingHitPos = startFlingPosition(ev);
        return true;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int i;
        int x1 = (int) e1.getX();
        int y1 = (int) e1.getY();
        int x2 = (int) e2.getX();
        int y2 = (int) e2.getY();
        int deltaX = x2 - this.mItemX;
        int deltaY = y2 - this.mItemY;
        if (this.mCanDrag && !this.mDragging && !((i = this.mHitPos) == -1 && this.mFlingHitPos == -1)) {
            if (i != -1) {
                if (this.mDragInitMode == 1 && Math.abs(y2 - y1) > this.mTouchSlop && this.mSortEnabled) {
                    startDrag(this.mHitPos, deltaX, deltaY);
                } else if (this.mDragInitMode != 0 && Math.abs(x2 - x1) > this.mTouchSlop && this.mRemoveEnabled) {
                    this.mIsRemoving = true;
                    startDrag(this.mFlingHitPos, deltaX, deltaY);
                }
            } else if (this.mFlingHitPos != -1) {
                if (Math.abs(x2 - x1) > this.mTouchSlop && this.mRemoveEnabled) {
                    this.mIsRemoving = true;
                    startDrag(this.mFlingHitPos, deltaX, deltaY);
                } else if (Math.abs(y2 - y1) > this.mTouchSlop) {
                    this.mCanDrag = false;
                }
            }
        }
        return false;
    }

    public void onLongPress(MotionEvent e) {
        if (this.mHitPos != -1 && this.mDragInitMode == 2) {
            this.mDslv.performHapticFeedback(0);
            startDrag(this.mHitPos, this.mCurrX - this.mItemX, this.mCurrY - this.mItemY);
        }
    }

    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public boolean onSingleTapUp(MotionEvent ev) {
        int i;
        if (!this.mRemoveEnabled || this.mRemoveMode != 0 || (i = this.mClickRemoveHitPos) == -1) {
            return true;
        }
        DragSortListView dragSortListView = this.mDslv;
        dragSortListView.removeItem(i - dragSortListView.getHeaderViewsCount());
        return true;
    }

    public void onShowPress(MotionEvent ev) {
    }
}
