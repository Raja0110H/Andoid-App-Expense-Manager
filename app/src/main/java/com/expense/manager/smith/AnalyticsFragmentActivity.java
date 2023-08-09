package com.expense.manager.smith;

import android.os.Bundle;
import android.text.Html;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.expense.manager.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;


public class AnalyticsFragmentActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public void changeUserAccount() {
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_analytics);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.analytics) + "</font>"));
        getSupportActionBar().setElevation(1.0f);
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.viewpager);
        this.viewPager = viewPager2;
        setupViewPager(viewPager2);
        this.tabLayout.setupWithViewPager(this.viewPager);
        n();

    }

    private void setupViewPager(ViewPager viewPager2) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AnalyticsExpenseFragment(), getString(R.string.expense));
        viewPagerAdapter.addFragment(new AnalyticsIncomeFragment(), getString(R.string.income));
        viewPager2.setAdapter(viewPagerAdapter);
    }

    public void onResume() {
        super.onResume();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(AnalyticsFragmentActivity this$0, FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int i) {
            return this.mFragmentList.get(i);
        }

        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(str);
        }

        public CharSequence getPageTitle(int i) {
            return this.mFragmentTitleList.get(i);
        }
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
