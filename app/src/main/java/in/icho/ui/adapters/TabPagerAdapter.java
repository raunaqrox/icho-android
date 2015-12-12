package in.icho.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class TabPagerAdapter extends FragmentStatePagerAdapter {


    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = new Fragment();
        switch (i) {
            case 0:
//			fragment = new Fragment();
                break;
            case 1:
//			fragment = new CampusFragment();
                break;
            case 2:
//			fragment = new NationalFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int i) {
        switch (i) {
            case 0:
                return "Popular";
            case 1:
                return "New";
            case 2:
                return "Trending";
            default:
                return "";
        }
    }
}