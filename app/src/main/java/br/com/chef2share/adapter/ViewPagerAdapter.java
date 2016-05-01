package br.com.chef2share.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Jonas on 14/11/2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private FragmentManager fragmentManager;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
    }
    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
        this.fragmentManager = fm;
    }
    @Override
    public Fragment getItem(int i) {
        return mFragments != null ? mFragments.get(i) : null;
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void refresh(List<Fragment> fragments){
        mFragments = fragments;
        notifyDataSetChanged();
    }
}
