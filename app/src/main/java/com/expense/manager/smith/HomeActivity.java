package com.expense.manager.smith;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.expense.manager.R;
import com.expense.manager.Utile.LocaleHelper;
import com.expense.manager.Model.bean.AccountBean;
import com.expense.manager.Model.bean.CategoryBean;
import com.expense.manager.passcode.LoginActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static long back_pressed;
    private String UserEmail = "";
    private String UserName = "";
    /* access modifiers changed from: private */
    public List<AccountBean> accounts;
    private Calendar calendar;
    private ArrayList<Integer> colors;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public String endTime = "9";
    List<CategoryBean> l;
    private LinearLayout layoutExpense;
    private LinearLayout layoutIncome;
    private LinearLayout layoutRecent;
    private ListView lvRecentTran;
    private ListView lvSpending;
    boolean m;
    private ConstraintLayout mainLayout;
    protected AccountBean n;
    MyDatabaseHandler o;
    List<CategoryBean> p;
    private PieChart pieChart;
    List<CategoryBean> q;
    List<CategoryBean> r;
    List<CategoryBean> s;
    /* access modifiers changed from: private */
    public String startTime = "1";
    private String totalAmount = "0";
    private TextView tvSpendingDate;
    private TextView tvSpendingLimitView;
    private TextView tvTodayExpense;
    private TextView tvTodayIncome;
    private TextView tvUserEmail;
    private TextView tvUserName;
    private TextView tvViewTransaction;
    private ArrayList<String> xVals;
    private ArrayList<PieEntry> yvalues;
    Context mContext = this;
    private AccountBean curAccount;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;

    public void ShowError(String str) {
    }

    Locale mLocale;
    String mString = "en";
    Ambaliya_MyApplication myApplication;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_home);
//        new AdAdmob(this).BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        this.context = this;
        this.o = new MyDatabaseHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.app_name) + "</font>"));
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        View headerView = navigationView.getHeaderView(0);
        this.tvUserEmail = (TextView) headerView.findViewById(R.id.tv_useremail);
        this.tvUserName = (TextView) headerView.findViewById(R.id.tv_username);
        this.tvUserEmail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.getAccount();
            }
        });





        initOnlyUser();
        firstTimeLaunch();
        Calendar instance = Calendar.getInstance();
        this.calendar = instance;
        instance.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        this.tvTodayIncome = (TextView) findViewById(R.id.tv_todayincome);
        this.tvTodayExpense = (TextView) findViewById(R.id.tv_todayexpense);
        this.tvSpendingDate = (TextView) findViewById(R.id.tv_spending_date);
        this.tvSpendingLimitView = (TextView) findViewById(R.id.tv_spendinglimit_date);
        this.tvViewTransaction = (TextView) findViewById(R.id.tv_viewTransaction);
        this.pieChart = (PieChart) findViewById(R.id.pie_chart);
        this.lvRecentTran = (ListView) findViewById(R.id.list_recent_tran);
        this.lvSpending = (ListView) findViewById(R.id.list_spending_limit);
        this.layoutIncome = (LinearLayout) findViewById(R.id.layout_main_imcome);
        this.layoutExpense = (LinearLayout) findViewById(R.id.layout_main_expense);
        this.tvSpendingDate.setText(new SimpleDateFormat("MMM yyyy", Locale.US).format(this.calendar.getTime()));
        PrepareDataForChart();
        this.layoutIncome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, IncomeFragmentActivity.class));

            }
        });
        this.layoutExpense.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, ExpenseFragmentActivity.class));
            }
        });
        this.tvViewTransaction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, DetailFragmentActivity.class));
            }
        });
        this.tvSpendingLimitView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, SpendingLimitActivity.class));
            }
        });


    }

    private void initOnlyUser() {
        curAccount = new AccountBean();
        this.curAccount.setName("App User");
        this.curAccount.setEmail("appuser@user.com");
        this.curAccount.setCurrency(MyUtils.getLastCurrencyId(this));
        this.o.addUpdateAccountData(this.curAccount);
    }


    private void firstTimeLaunch() {
        this.accounts = this.o.getAccountList();
        this.n = new AccountBean();
        this.mainLayout = (ConstraintLayout) findViewById(R.id.layout_main);
        if (LocaleHelper.getIsPinSet(this)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
//        else if (this.accounts.size() == 0) {
//            startActivity(new Intent(this, AddEditAccount.class));
//        }
//        else if (!Preferences.getInstance().getLoginStatus()) {
//            Preferences.getInstance().setLoginStatus(true);
//            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.app_name)).setMessage(getString(R.string.dialog_enable_password)).setCancelable(false).setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    HomeActivity.this.startActivity(new Intent(HomeActivity.this, PinEnterActivity.class));
//                    System.exit(0);
//                }
//            }).setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i) {
//                }
//            }).show();
//        }
        if (MyUtils.getWhatNewRequired(this)) {
//            startActivity(new Intent(this, WhatsNewActivity.class));
        }
        IntializeMyDataBaseWithDefaultValues();
    }

    private void IntializeMyDataBaseWithDefaultValues() {
        if (this.o.getCount(MyDatabaseHandler.Category.TABLE_NAME) == 0) {
            i("1");
        }
    }

    public void getAccount() {
        this.accounts = new ArrayList();
        this.accounts = this.o.getAccountList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.dialog_account_list, (ViewGroup) null);
        builder.setView(inflate);
        ListView listView = (ListView) inflate.findViewById(R.id.list_account);
        listView.setAdapter(new ArrayAdapter(this.context, R.layout.listview_txt, this.accounts));
        final AlertDialog create = builder.create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                HomeActivity homeActivity = HomeActivity.this;
                homeActivity.n = (AccountBean) homeActivity.accounts.get(i);
                HomeActivity.this.onNavigationItemChanged();
                create.dismiss();
                HomeActivity.this.setTodayIncomeExpense();
            }
        });
        ((Button) inflate.findViewById(R.id.btn_newAccount)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HomeActivity.this.j();
                create.dismiss();
            }
        });
        create.show();
    }

    public void attachBaseContext(Context context2) {
        super.attachBaseContext(LocaleHelper.onAttach(context2));
    }

    public void changeUserAccount() {
        m(this);
        setTodayIncomeExpense();
    }

    public void setTodayIncomeExpense() {
        AccountBean accountModelById = this.o.getAccountModelById(MyUtils.getCurrentAccount(this.context));
        this.n = accountModelById;
        if (accountModelById != null) {
            this.UserName = accountModelById.getName();
            this.UserEmail = this.n.getEmail();
        } else {
            this.UserName = getResources().getString(R.string.app_name);
            this.UserEmail = "example@gmail.com";
        }
        int userSummary = MyUtils.getUserSummary(this);
        PrepareDataForChart();
        Calendar instance = Calendar.getInstance();
        instance.set(11, 0);
        instance.clear(12);
        instance.clear(13);
        instance.clear(14);
        this.tvUserName.setText(this.UserName);
        this.tvUserEmail.setText(this.UserEmail);
        if (userSummary == 1) {
            setSummaryType(getString(R.string.daily));
            Calendar calendar2 = (Calendar) instance.clone();
            Calendar calendar22 = (Calendar) calendar2.clone();
            calendar22.add(6, 1);
            calendar22.add(14, -1);
            setDetails(calendar2, calendar22);
        } else if (userSummary == 2) {
            setSummaryType(getString(R.string.weekly));
            setDetails(getWeekStart(instance), getWeekEnd(instance));
        } else if (userSummary == 3) {
            setSummaryType(getString(R.string.monthly));
            setDetails(getMonthStart(instance), getMonthEnd(instance));
        }
    }

    private void setSummaryType(String str) {
        ((TextView) findViewById(R.id.tv_income_type)).setText(str);
        ((TextView) findViewById(R.id.tv_expense_type)).setText(str);
    }

    private void setDetails(Calendar calendar2, Calendar calendar22) {
        double d;
        SimpleDateFormat simpleDateFormat = MyUtils.sdfDatabase;
        String format = simpleDateFormat.format(calendar2.getTime());
        String format2 = simpleDateFormat.format(calendar22.getTime());
        String totalAmountByTime = this.o.getTotalAmountByTime(2, format, format2);
        String totalAmountByTime2 = this.o.getTotalAmountByTime(1, format, format2);
        String replaceAll = totalAmountByTime.replaceAll(",", "");
        String replaceAll2 = totalAmountByTime2.replaceAll(",", "");
        double d2 = Utils.DOUBLE_EPSILON;
        try {
            double RoundOff = MyUtils.RoundOff(Double.parseDouble(replaceAll));
            d = MyUtils.RoundOff(Double.parseDouble(replaceAll2));
            d2 = RoundOff;
        } catch (NumberFormatException e) {
            d = Utils.DOUBLE_EPSILON;
        }
        setTextValue(this.tvTodayIncome, " %.2f", d2);
        setTextValue(this.tvTodayExpense, "%.2f", d);
    }

    public void onResume() {
        super.onResume();
        setTodayIncomeExpense();
    }

    private Calendar getMonthStart(Calendar calendar2) {
        Calendar calendar22 = (Calendar) calendar2.clone();
        calendar22.set(5, 1);
        return calendar22;
    }

    private Calendar getMonthEnd(Calendar calendar2) {
        Calendar calendar22 = (Calendar) calendar2.clone();
        calendar22.add(2, 1);
        calendar22.add(14, -1);
        return calendar22;
    }

    private Calendar getWeekEnd(Calendar calendar2) {
        Calendar calendar22 = (Calendar) calendar2.clone();
        calendar22.add(3, 1);
        calendar22.add(14, -1);
        return calendar22;
    }

    private Calendar getWeekStart(Calendar calendar2) {
        Calendar calendar22 = (Calendar) calendar2.clone();
        calendar22.set(7, calendar2.getFirstDayOfWeek());
        return calendar22;
    }

    private void setTextValue(TextView textView, String str, double d) {
        if (getCurrencySymbol().equals("¢") || getCurrencySymbol().equals("₣") || getCurrencySymbol().equals("₧") || getCurrencySymbol().equals("﷼") || getCurrencySymbol().equals("₨")) {
            textView.setText(String.format(str, new Object[]{Double.valueOf(d)}) + getCurrencySymbol());
            return;
        }
        textView.setText(getCurrencySymbol() + String.format(str, new Object[]{Double.valueOf(d)}));
    }

    private void PrepareDataForChart() {
        boolean booleanValue = this.o.existsColumnInTable().booleanValue();
        this.m = booleanValue;
        if (booleanValue) {
            this.r = this.o.getCategoryList(2);
            this.p = this.o.getAllCategory();
            this.q = this.o.getSpeningCategory();
        } else {
            this.o.addcolumn();
        }
        for (int i = 0; i < this.r.size(); i++) {
            CategoryBean categoryBean = this.r.get(i);
            categoryBean.setCategoryTotal(this.o.getCategoryAmount(categoryBean.getCategoryGroup(), categoryBean.getId()));
        }
        this.l = new ArrayList();
        for (int i2 = 0; i2 < this.q.size(); i2++) {
            CategoryBean categoryBean2 = this.q.get(i2);
            categoryBean2.setCategoryTotal(this.o.getCategoryAmount(categoryBean2.getCategoryGroup(), categoryBean2.getId()));
            if (!this.o.getCategoryAmount(categoryBean2.getCategoryGroup(), categoryBean2.getId()).equals("0") && this.l.size() < 5) {
                this.l.add(categoryBean2);
            }
        }
        ArrayList arrayList = new ArrayList();
        this.s = arrayList;
        arrayList.clear();
        for (int i3 = 0; i3 < this.p.size(); i3++) {
            CategoryBean categoryBean3 = this.p.get(i3);
            if (!this.o.getCategoryAmount(categoryBean3.getId()).equals("0") && this.s.size() < 5) {
                this.s.add(categoryBean3);
            }
        }
        this.yvalues = new ArrayList<>();
        this.xVals = new ArrayList<>();
        this.colors = new ArrayList<>();
        for (int i4 = 0; i4 < this.r.size(); i4++) {
            this.yvalues.add(new PieEntry(Float.parseFloat(this.r.get(i4).getCategoryTotal()), this.r.get(i4).getName()));
            this.xVals.add(this.r.get(i4).getName());
            this.colors.add(Integer.valueOf(Color.parseColor(this.r.get(i4).getColor())));
        }
        CategoryCustomAdapter categoryCustomAdapter = new CategoryCustomAdapter(this.context, this.s);
        CategorySpendingLimitAdapter categorySpendingLimitAdapter = new CategorySpendingLimitAdapter(this.context, this.l, false);
        this.lvRecentTran.setAdapter(categoryCustomAdapter);
        this.lvSpending.setAdapter(categorySpendingLimitAdapter);
        this.lvRecentTran.setEmptyView(findViewById(R.id.tv_notransaction));
        this.lvSpending.setEmptyView(findViewById(R.id.tv_noSpent));
        this.lvSpending.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        this.lvSpending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i5, long j) {
                Intent intent = new Intent(HomeActivity.this.context, AddEditCategory.class);
                intent.putExtra(TAG.CATEGORY, 1);
                intent.putExtra(TAG.DATA, (int) j);
                HomeActivity.this.startActivity(intent);
            }
        });
        this.lvRecentTran.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i5, long j) {
                int categoryGroup = HomeActivity.this.s.get(i5).getCategoryGroup();
                Intent intent = new Intent(HomeActivity.this, CategoryTimeListFragmentActivity.class);
                intent.putExtra(CategoryTimeFragment.CATEGORY_TYPE, (int) j);
                intent.putExtra("selection_type", categoryGroup);
                intent.putExtra(CategoryTimeFragment.START_DATE, HomeActivity.this.startTime);
                intent.putExtra(CategoryTimeFragment.END_DATE, HomeActivity.this.endTime);
                intent.putExtra(CategoryTimeFragment.MESSAGE, "");
                HomeActivity.this.startActivity(intent);
            }
        });
        setPieChart();
    }

    private void setPieChart() {
        this.pieChart.setUsePercentValues(true);
        PieDataSet pieDataSet = new PieDataSet(this.yvalues, "");
        pieDataSet.setColors((List<Integer>) this.colors);
        pieDataSet.setDrawIcons(false);
        pieDataSet.setSliceSpace(3.0f);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setIconsOffset(new MPPointF(0.0f, 40.0f));
        pieDataSet.setSelectionShift(5.0f);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        this.pieChart.setData(pieData);
        this.pieChart.getDescription().setEnabled(false);
        this.pieChart.setDrawHoleEnabled(true);
        this.pieChart.setTransparentCircleRadius(30.0f);
        this.pieChart.setHoleRadius(30.0f);
        this.pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        this.pieChart.setExtraOffsets(0.0f, 5.0f, 0.0f, 5.0f);
        this.pieChart.setDrawSliceText(false);
        Legend legend = this.pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXEntrySpace(7.0f);
        legend.setYEntrySpace(0.0f);
        legend.setYOffset(10.0f);
        legend.setWordWrapEnabled(true);
        legend.setDrawInside(false);
        legend.getCalculatedLineSizes();
        legend.setEnabled(true);
        this.pieChart.setRotationAngle(0.0f);
        this.pieChart.setRotationEnabled(true);
        this.pieChart.highlightValues((Highlight[]) null);
        this.pieChart.setHighlightPerTapEnabled(true);
        this.pieChart.invalidate();
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();


         if (itemId == 16908332) {
            onBackPressed();
            return true;
        }  else {
            if (itemId == R.id.action_analytics) {
                startActivity(new Intent(this, AnalyticsFragmentActivity.class));
            } else if (itemId == R.id.action_settings) {

                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                return true;
            } else if (itemId == R.id.action_details) {
                startActivity(new Intent(this, DetailFragmentActivity.class));
                return true;
            }
            ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
            return true;
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.option_add) {
            startActivity(new Intent(this, AddExpenceIncomeActivity.class));
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            drawerLayout.closeDrawer((int) GravityCompat.START);
        } else if (back_pressed + 3000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {

            Toast.makeText(getBaseContext(), getString(R.string.back_msg), 0).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Locale locale = this.mLocale;
        if (locale != null) {
            configuration.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        }
    }


}
