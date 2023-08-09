package com.expense.manager.smith;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.expense.manager.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class DetailFragmentActivity extends BaseActivity {
    private View lastView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private void updateUI() {
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_details);
//        AdAdmob adAdmob = new AdAdmob(this);
//        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
//        adAdmob.FullscreenAd(this);
//        getSupportActionBar().setElevation(1.0f);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.recent_transaction) + "</font>"));
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.viewpager);
        this.viewPager = viewPager2;
        setupViewPager(viewPager2);
        this.tabLayout.setupWithViewPager(this.viewPager);
        n();
        updateUI();
    }

    private void setupViewPager(ViewPager viewPager2) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPagerAdapter.addFragment(new DetailOverallFragment(), getString(R.string.all));
        viewPagerAdapter.addFragment(new DetailDailyFragment(), getString(R.string.daily));
        viewPagerAdapter.addFragment(new DetailWeeklyFragment(), getString(R.string.weekly));
        viewPagerAdapter.addFragment(new DetailMonthlyFragment(), getString(R.string.monthly));
        viewPagerAdapter.addFragment(new DetailYearlyFragment(), getString(R.string.yearly));
        viewPagerAdapter.addFragment(new DetailCustomFragment(), getString(R.string.custom));
        viewPager2.setAdapter(viewPagerAdapter);
    }

    public void onResume() {
        super.onResume();
    }

    public void changeUserAccount() {
        updateUI();
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        finish();
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(DetailFragmentActivity this$0, FragmentManager fragmentManager) {
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
}
