package com.expense.manager.smith;

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

public class MyCategoryCustomAdapter extends BaseAdapter {
    int a = 0;
    private Context context;
    private List<CategoryBean> data;
    private LayoutInflater influter;
    /* access modifiers changed from: private */
    public RadioButton mSelectedRB;

    public long getItemId(int i) {
        return (long) i;
    }

    public MyCategoryCustomAdapter(Context context2, List<CategoryBean> list, String str) {
        this.context = context2;
        this.data = list;
        this.influter = (LayoutInflater) context2.getSystemService("layout_inflater");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(str)) {
                this.a = i;
            }
        }
    }

    public int getCount() {
        return this.data.size();
    }

    public CategoryBean getItem(int i) {
        return this.data.get(i);
    }

    private class ViewHolder {
        CircleImageView a;
        LinearLayout b;
        TextView c;
        RadioButton d;

        private ViewHolder(MyCategoryCustomAdapter myCategoryCustomAdapter) {
        }
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
            if (i == this.a) {
                RadioButton radioButton = viewHolder.d;
                this.mSelectedRB = radioButton;
                radioButton.setChecked(true);
                this.a = i;
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

                MyCategoryCustomAdapter myCategoryCustomAdapter = MyCategoryCustomAdapter.this;
                if (!(i == myCategoryCustomAdapter.a || myCategoryCustomAdapter.mSelectedRB == null)) {
                    MyCategoryCustomAdapter.this.mSelectedRB.setChecked(false);
                }
                MyCategoryCustomAdapter myCategoryCustomAdapter2 = MyCategoryCustomAdapter.this;
                myCategoryCustomAdapter2.a = i;
                RadioButton unused = myCategoryCustomAdapter2.mSelectedRB = radioButton;
                if (MyCategoryCustomAdapter.this.a != i) {
                    viewHolder.d.setChecked(false);
                    return;
                }
                viewHolder.d.setChecked(true);
                if (MyCategoryCustomAdapter.this.mSelectedRB != null && viewHolder.d != MyCategoryCustomAdapter.this.mSelectedRB) {
                    RadioButton unused2 = MyCategoryCustomAdapter.this.mSelectedRB = viewHolder.d;
                }
            }
        });
        return view;
    }

    public String getSelectedCategoryId() {
        if (this.data.size() == 0) {
            return "0";
        }
        return this.data.get(this.a).getId();
    }
}
