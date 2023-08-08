package com.expense.manager.widget.SortListView;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class SimpleFloatViewManager implements DragSortListView.FloatViewManager {
    private int mFloatBGColor = -16777216;
    private Bitmap mFloatBitmap;
    private ImageView mImageView;
    private ListView mListView;

    public SimpleFloatViewManager(ListView lv) {
        this.mListView = lv;
    }

    public void setBackgroundColor(int color) {
        this.mFloatBGColor = color;
    }

    public View onCreateFloatView(int position) {
        ListView listView = this.mListView;
        View v = listView.getChildAt((listView.getHeaderViewsCount() + position) - this.mListView.getFirstVisiblePosition());
        if (v == null) {
            return null;
        }
        v.setPressed(false);
        v.setDrawingCacheEnabled(true);
        this.mFloatBitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        if (this.mImageView == null) {
            this.mImageView = new ImageView(this.mListView.getContext());
        }
        this.mImageView.setBackgroundColor(this.mFloatBGColor);
        this.mImageView.setPadding(0, 0, 0, 0);
        this.mImageView.setImageBitmap(this.mFloatBitmap);
        this.mImageView.setLayoutParams(new ViewGroup.LayoutParams(v.getWidth(), v.getHeight()));
        return this.mImageView;
    }

    public void onDragFloatView(View floatView, Point position, Point touch) {
    }

    public void onDestroyFloatView(View floatView) {
        ((ImageView) floatView).setImageDrawable((Drawable) null);
        this.mFloatBitmap.recycle();
        this.mFloatBitmap = null;
    }
}
