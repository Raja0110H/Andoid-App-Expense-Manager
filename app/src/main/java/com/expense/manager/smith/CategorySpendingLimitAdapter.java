package com.expense.manager.smith;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expense.manager.R;
import com.expense.manager.Utile.Notification;
import com.expense.manager.Model.bean.CategoryBean;

import java.util.List;

public class CategorySpendingLimitAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryBean> data;
    private LayoutInflater influter;
    private boolean isNotification = false;

    public CategorySpendingLimitAdapter(Context context2, List<CategoryBean> list, boolean z) {
        this.context = context2;
        this.data = list;
        this.isNotification = z;
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

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v40, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: com.demo.example.dailyincomeexpensemanager.CategorySpendingLimitAdapter$ViewHolder} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0190  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x01c8  */
    @androidx.annotation.RequiresApi(api = 21)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public View getView(int paramInt, View view, ViewGroup paramViewGroup) {
        ViewHolder viewHolder1;

        if (view == null) {
              view = this.influter.inflate(R.layout.list_item_spending_limit, null);
            viewHolder1 = new ViewHolder(this,null);
            viewHolder1.a = (TextView) view.findViewById(R.id.tv_item_category);
            viewHolder1.b = (TextView) view.findViewById(R.id.tv_item_overspent);
            viewHolder1.d = (TextView) view.findViewById(R.id.tv_item_count);
            viewHolder1.c = (ProgressBar) view.findViewById(R.id.pb_limit);
            view.setTag(viewHolder1);
        } else {

            viewHolder1 = (ViewHolder) view.getTag();

        }
        CategoryBean categoryBean = getItem(paramInt);
        viewHolder1.a.setText(categoryBean.getName());
        viewHolder1.c.setMax(100);
        int i = Integer.parseInt(categoryBean.getCategoryTotal());
        if (categoryBean.getCategoryLimit().equals("0")) {
            paramInt = 1;
        } else {
            paramInt = Integer.parseInt(categoryBean.getCategoryLimit());
        }
        paramInt = i * 100 / paramInt;
        viewHolder1.c.setProgress(paramInt);
        String str2 = categoryBean.getCategoryTotal().replaceAll(",", "");
        String str1 = categoryBean.getCategoryLimit().replaceAll(",", "");
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str1)) {
            StringBuilder stringBuilder;
            String str = ((BaseActivity) this.context).getCurrencySymbol();
            if (str.equals("¢") || str.equals("₣") || str.equals("₧") || str.equals("﷼") || str.equals("₨")) {
                try {
                    TextView textView1 = viewHolder1.d;
                    StringBuilder stringBuilder1 = new StringBuilder();

                    stringBuilder1.append("");
                    double d = Double.parseDouble(str2);
                    try {
                        stringBuilder1.append((int) MyUtils.RoundOff(d));
                        stringBuilder1.append(str);
                        stringBuilder1.append("/");
                        stringBuilder1.append((int) MyUtils.RoundOff(Double.parseDouble(str1)));
                        stringBuilder1.append(str);
                        textView1.setText(stringBuilder1.toString());
                    } catch (NumberFormatException numberFormatException) {
                    }
                } catch (NumberFormatException numberFormatException) {
                }
                TextView textView = viewHolder1.d;
                stringBuilder = new StringBuilder();
                stringBuilder.append("0");
                stringBuilder.append(str);
                stringBuilder.append("/");
                stringBuilder.append(str1);
                stringBuilder.append(str);
                textView.setText(stringBuilder.toString());
            }
            try {
                TextView textView = viewHolder1.d;
                StringBuilder stringBuilder1 = new StringBuilder();

                stringBuilder1.append("");
                stringBuilder1.append(str);
                stringBuilder1.append((int) MyUtils.RoundOff(Double.parseDouble(str)));
                stringBuilder1.append("/");
                stringBuilder1.append(str);
                stringBuilder1.append((int) MyUtils.RoundOff(Double.parseDouble(str1)));
                textView.setText(stringBuilder1.toString());
            } catch (NumberFormatException numberFormatException) {
                TextView textView = viewHolder1.d;
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append(str);
                stringBuilder1.append("0/");
                stringBuilder1.append(str1);
                textView.setText(stringBuilder1.toString());
            }
        }
        if (90 < paramInt) {
            viewHolder1.a.setTextColor(Color.parseColor("#df151a"));
            viewHolder1.b.setVisibility(0);
            if (this.isNotification) {
                Context context = this.context;
                Notification.showNotification(context, context.getString(R.string.over_spent_msg));
            }
            viewHolder1.c.setProgressTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.highlight)));
        } else {
            viewHolder1.a.setTextColor(Color.parseColor(categoryBean.getColor()));
            viewHolder1.b.setVisibility(8);
            viewHolder1.c.setProgressTintList(ColorStateList.valueOf(Color.parseColor(categoryBean.getColor())));
        }
        return view;
    }

    private class ViewHolder {
        TextView a;
        TextView b;
        ProgressBar c;
        TextView d;

        private ViewHolder(CategorySpendingLimitAdapter categorySpendingLimitAdapter) {

        }
       ViewHolder(CategorySpendingLimitAdapter categorySpendingLimitAdapter, CategorySpendingLimitAdapter var2_2) {
            this(categorySpendingLimitAdapter);
        }
    }
}
