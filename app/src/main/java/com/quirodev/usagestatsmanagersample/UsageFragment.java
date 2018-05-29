package com.quirodev.usagestatsmanagersample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quirodev.usagestatsmanagersample.getUsageTime.Fragment1;
import com.quirodev.usagestatsmanagersample.getUsageTime.Fragment2;
import com.quirodev.usagestatsmanagersample.getUsageTime.Fragment3;
import com.quirodev.usagestatsmanagersample.getUsageTime.Fragment4;
import com.quirodev.usagestatsmanagersample.getUsageTime.UsageContract;
import com.quirodev.usagestatsmanagersample.getUsageTime.UsagePresenter_D;


public class UsageFragment extends Fragment {
    private ViewPager mviewPager;
    private PagerAdapter mpagerAdapter;
    private TabLayout mTab;
    TextView tex;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.appusage);

        mTab = (TabLayout) findViewById(R.id.tab);
        mviewPager = (ViewPager) findViewById(R.id.viewPager);
        mpagerAdapter = new pagerAdapter(getSupportFragmentManager());

        mviewPager.setAdapter(mpagerAdapter);
        mTab.setupWithViewPager(mviewPager);
        */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appusage, container, false);

        mTab = (TabLayout) view.findViewById(R.id.tab);
        mviewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mpagerAdapter = new pagerAdapter(getActivity().getSupportFragmentManager());
        mviewPager.setAdapter(mpagerAdapter);
        //mviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        /*
        @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((pagerAdapter)mpagerAdapter).getItem(position);
                switch(position) {
                    case 0:
                        ((MainActivity)getActivity()).setTime(((Fragment1)fragment).getTotal());
                        break;
                    case 1:
                        ((MainActivity)getActivity()).setTime(((Fragment2)fragment).getTotal());
                        break;
                    case 2:
                        ((MainActivity)getActivity()).setTime(((Fragment3)fragment).getTotal());
                        break;
                    case 3:
                        ((MainActivity)getActivity()).setTime(((Fragment4)fragment).getTotal());
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        */
        mTab.setupWithViewPager(mviewPager);

        return view;
    }

    class pagerAdapter extends FragmentPagerAdapter {
        private final static int itemCount = 4;

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Fragment1();
                case 1:
                    return new Fragment2();
                case 2:
                    return new Fragment3();
                case 3:
                    return new Fragment4();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return itemCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Daily";
                case 1:
                    return "Weekly";
                case 2:
                    return "Monthly";
                case 3:
                    return "Yearly";
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }
}