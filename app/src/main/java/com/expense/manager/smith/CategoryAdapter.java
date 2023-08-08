package com.expense.manager.smith;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.expense.manager.R;
import com.expense.manager.Model.bean.CategoryBean;
import com.expense.manager.widget.CircleImageView;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    AlertDialog a;
    int b = 0;
    private Context context;
    /* access modifiers changed from: private */
    public List<CategoryBean> data;
    private LayoutInflater influter;
    /* access modifiers changed from: private */
    public RadioButton mSelectedRB;
    /* access modifiers changed from: private */
    public TextView tvCategory;

    public long getItemId(int i) {
        return (long) i;
    }

    public CategoryAdapter(Context context2, List<CategoryBean> list, String str, AlertDialog alertDialog, TextView textView) {
        this.context = context2;
        this.data = list;
        this.a = alertDialog;
        this.tvCategory = textView;
        this.influter = (LayoutInflater) context2.getSystemService("layout_inflater");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(str)) {
                this.b = i;
            }
        }
    }

    public int getCount() {
        return this.data.size();
    }

    public CategoryBean getItem(int i) {
        return this.data.get(i);
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = this.influter.inflate(R.layout.list_item_select_category, (ViewGroup) null);
            viewHolder = new ViewHolder(this);
            viewHolder.c = (TextView) view.findViewById(R.id.cat_item_name);
            viewHolder.a = (CircleImageView) view.findViewById(R.id.cat_item_color);
            viewHolder.d = (RadioButton) view.findViewById(R.id.cat_item_select);
            viewHolder.b = (LinearLayout) view.findViewById(R.id.cat_list_item);
            view.setTag(viewHolder);
            if (i == this.b) {
                RadioButton radioButton = viewHolder.d;
                this.mSelectedRB = radioButton;
                radioButton.setChecked(true);
                this.b = i;
            }
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.c.setText(this.data.get(i).getName());
        viewHolder.a.setCircleBackgroundColor(Color.parseColor(this.data.get(i).getColor()));
        viewHolder.a.setBorderColor(Color.parseColor(this.data.get(i).getColor()));
        viewHolder.a.setColorFilter(Color.parseColor(this.data.get(i).getColor()));
        viewHolder.b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view2) {
                RadioButton radioButton = (RadioButton) view2.findViewById(R.id.cat_item_select);

                CategoryAdapter categoryAdapter = CategoryAdapter.this;
                if (!(i == categoryAdapter.b || categoryAdapter.mSelectedRB == null)) {
                    CategoryAdapter.this.mSelectedRB.setChecked(false);
                }
                CategoryAdapter categoryAdapter2 = CategoryAdapter.this;
                categoryAdapter2.b = i;
                RadioButton unused = categoryAdapter2.mSelectedRB = radioButton;
                if (CategoryAdapter.this.b != i) {
                    viewHolder.d.setChecked(false);
                } else {
                    viewHolder.d.setChecked(true);
                    if (!(CategoryAdapter.this.mSelectedRB == null || viewHolder.d == CategoryAdapter.this.mSelectedRB)) {
                        RadioButton unused2 = CategoryAdapter.this.mSelectedRB = viewHolder.d;
                    }
                }
                CategoryAdapter.this.tvCategory.setText(((CategoryBean) CategoryAdapter.this.data.get(i)).getName());
                CategoryAdapter.this.a.dismiss();
            }
        });
        return view;
    }

    public String getSelectedCategoryId() {
        if (this.data.size() == 0) {
            return "0";
        }
        return this.data.get(this.b).getId();
    }

    public String getCategoryName() {
        if (this.data.size() == 0) {
            return "Select Category";
        }
        return this.data.get(this.b).getName();
    }

    private class ViewHolder {
        CircleImageView a;
        LinearLayout b;
        TextView c;
        RadioButton d;

        private ViewHolder(CategoryAdapter categoryAdapter) {
        }
    }
}
