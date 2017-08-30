package ae.ac.ud.ceit.cts;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

import ae.ac.ud.ceit.cts.tabs.search.CrimeSearchFragment;
import ae.ac.ud.ceit.cts.tabs.feeds.ListCrimesFragment;
import ae.ac.ud.ceit.cts.tabs.manage.ManageCrimeFragment;

/**
 * Created by atalla on 22/02/17.
 */

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Feeds", "New", "Search" };
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
       // return PageFragment.newInstance(position + 1);
        switch (position){
            case 0:
                return ListCrimesFragment.newInstance(position + 1);
            case 2:
                return CrimeSearchFragment.newInstance(position + 1);
            default:
                return ManageCrimeFragment.newInstance(position + 1);
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}