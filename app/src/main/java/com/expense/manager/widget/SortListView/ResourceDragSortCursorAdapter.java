package com.expense.manager.widget.SortListView;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ResourceDragSortCursorAdapter extends DragSortCursorAdapter {
    private int mDropDownLayout;
    private LayoutInflater mInflater;
    private int mLayout;

    @Deprecated
    public ResourceDragSortCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c);
        this.mDropDownLayout = layout;
        this.mLayout = layout;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public ResourceDragSortCursorAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.mDropDownLayout = layout;
        this.mLayout = layout;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public ResourceDragSortCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, c, flags);
        this.mDropDownLayout = layout;
        this.mLayout = layout;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return this.mInflater.inflate(this.mLayout, parent, false);
    }

    public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
        return this.mInflater.inflate(this.mDropDownLayout, parent, false);
    }

    public void setViewResource(int layout) {
        this.mLayout = layout;
    }

    public void setDropDownViewResource(int dropDownLayout) {
        this.mDropDownLayout = dropDownLayout;
    }
}
