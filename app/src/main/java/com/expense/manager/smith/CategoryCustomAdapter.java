package com.expense.manager.smith;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.expense.manager.R;
import com.expense.manager.Model.bean.CategoryBean;
import com.expense.manager.widget.CircleImageView;
import java.util.List;

public class CategoryCustomAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryBean> data;
    private LayoutInflater influter;

    public CategoryCustomAdapter(Context context2, List<CategoryBean> list) {
        this.context = context2;
        this.data = list;
        this.influter = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.data.size();
    }

    public CategoryBean getItem(int i) {
        return this.data.get(i);
    }

    public long getItemId(int i) {
        return (long) Integer.parseInt(getItem(i).getId());
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.influter.inflate(R.layout.list_item_category, (ViewGroup) null);
            viewHolder = new ViewHolder(this);
            viewHolder.b = (TextView) view.findViewById(R.id.cat_item_name);
            viewHolder.c = (TextView) view.findViewById(R.id.tvTotalAmount);
            viewHolder.a = (CircleImageView) view.findViewById(R.id.cat_item_color);
            ImageView imageView = (ImageView) view.findViewById(R.id.grabber);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CategoryBean item = getItem(i);
        viewHolder.b.setText(item.getName());
        viewHolder.a.setColorFilter(Color.parseColor(item.getColor()));
        viewHolder.a.setCircleBackgroundColor(Color.parseColor(item.getColor()));
        viewHolder.a.setBorderColor(Color.parseColor(item.getColor()));
        if (!TextUtils.isEmpty(item.getCategoryTotal())) {
            String currencySymbol = ((BaseActivity) this.context).getCurrencySymbol();
            if (currencySymbol.equals("¢") || currencySymbol.equals("₣") || currencySymbol.equals("₧") || currencySymbol.equals("﷼") || currencySymbol.equals("₨")) {
                if (item.getCategoryGroup() == 2) {
                    viewHolder.c.setTextColor(this.context.getResources().getColor(R.color.detail_income));
                    TextView textView = viewHolder.c;
                    textView.setText("+ " + item.getCategoryTotal() + currencySymbol);
                } else {
                    TextView textView2 = viewHolder.c;
                    textView2.setText("- " + item.getCategoryTotal() + currencySymbol);
                    viewHolder.c.setTextColor(this.context.getResources().getColor(R.color.detail_expense));
                }
            } else if (item.getCategoryGroup() == 2) {
                viewHolder.c.setTextColor(this.context.getResources().getColor(R.color.detail_income));
                TextView textView3 = viewHolder.c;
                textView3.setText("+ " + currencySymbol + item.getCategoryTotal());
            } else {
                TextView textView4 = viewHolder.c;
                textView4.setText("- " + currencySymbol + item.getCategoryTotal());
                viewHolder.c.setTextColor(this.context.getResources().getColor(R.color.detail_expense));
            }
        }
        return view;
    }

    private class ViewHolder {
        CircleImageView a;
        TextView b;
        TextView c;

        private ViewHolder(CategoryCustomAdapter categoryCustomAdapter) {
        }
    }
}
