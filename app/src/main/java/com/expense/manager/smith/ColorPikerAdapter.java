package com.expense.manager.smith;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.expense.manager.R;
import com.expense.manager.widget.CircleImageView;

public class ColorPikerAdapter extends BaseAdapter {
    String[] a;
    private LayoutInflater influter;
    private Context mContext;

    public long getItemId(int i) {
        return (long) i;
    }

    public ColorPikerAdapter(Context context, String[] strArr) {
        this.mContext = context;
        this.a = strArr;
        this.influter = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.a.length;
    }

    public String getItem(int i) {
        return this.a[i];
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.influter.inflate(R.layout.list_item_color, (ViewGroup) null);
        }
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.color_piker_color);
        circleImageView.setColorFilter(Color.parseColor(this.a[i]));
        circleImageView.setBorderColor(Color.parseColor(this.a[i]));
        circleImageView.setCircleBackgroundColor(Color.parseColor(this.a[i]));
        return view;
    }
}
