package com.expense.manager.widget.SortListView;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleDragSortCursorAdapter extends ResourceDragSortCursorAdapter {
    protected int[] j;
    protected int[] k;
    String[] l;
    private CursorToStringConverter mCursorToStringConverter;
    private int mStringConversionColumn = -1;
    private ViewBinder mViewBinder;

    public interface CursorToStringConverter {
        CharSequence convertToString(Cursor cursor);
    }

    public interface ViewBinder {
        boolean setViewValue(View view, Cursor cursor, int i);
    }

    @Deprecated
    public SimpleDragSortCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c);
        this.k = to;
        this.l = from;
        findColumns(c, from);
    }

    public SimpleDragSortCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, flags);
        this.k = to;
        this.l = from;
        findColumns(c, from);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        ViewBinder binder = this.mViewBinder;
        int count = this.k.length;
        int[] from = this.j;
        int[] to = this.k;
        for (int i = 0; i < count; i++) {
            View v = view.findViewById(to[i]);
            if (v != null) {
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }
                if (bound) {
                    continue;
                } else {
                    String text = cursor.getString(from[i]);
                    if (text == null) {
                        text = "";
                    }
                    if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        setViewImage((ImageView) v, text);
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a  view that can be bounds by this SimpleCursorAdapter");
                    }
                }
            }
        }
    }

    public ViewBinder getViewBinder() {
        return this.mViewBinder;
    }

    public void setViewBinder(ViewBinder viewBinder) {
        this.mViewBinder = viewBinder;
    }

    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            v.setImageURI(Uri.parse(value));
        }
    }

    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    public int getStringConversionColumn() {
        return this.mStringConversionColumn;
    }

    public void setStringConversionColumn(int stringConversionColumn) {
        this.mStringConversionColumn = stringConversionColumn;
    }

    public CursorToStringConverter getCursorToStringConverter() {
        return this.mCursorToStringConverter;
    }

    public void setCursorToStringConverter(CursorToStringConverter cursorToStringConverter) {
        this.mCursorToStringConverter = cursorToStringConverter;
    }

    public CharSequence convertToString(Cursor cursor) {
        CursorToStringConverter cursorToStringConverter = this.mCursorToStringConverter;
        if (cursorToStringConverter != null) {
            return cursorToStringConverter.convertToString(cursor);
        }
        int i = this.mStringConversionColumn;
        if (i > -1) {
            return cursor.getString(i);
        }
        return super.convertToString(cursor);
    }

    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int count = from.length;
            int[] iArr = this.j;
            if (iArr == null || iArr.length != count) {
                this.j = new int[count];
            }
            for (int i = 0; i < count; i++) {
                this.j[i] = c.getColumnIndexOrThrow(from[i]);
            }
            return;
        }
        this.j = null;
    }

    public Cursor swapCursor(Cursor c) {
        findColumns(c, this.l);
        return super.swapCursor(c);
    }

    public void changeCursorAndColumns(Cursor c, String[] from, int[] to) {
        this.l = from;
        this.k = to;
        findColumns(c, from);
        super.changeCursor(c);
    }
}
