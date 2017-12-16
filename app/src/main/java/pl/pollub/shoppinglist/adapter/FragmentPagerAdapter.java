package pl.pollub.shoppinglist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * When pages are not visible to the user, their entire fragment may be destroyed, only keeping the saved state of that fragment.
 * This allows the pager to hold on to much less memory associated with each visited page as compared to FragmentPagerAdapter at the cost of potentially more overhead when switching between pages.
 *
 * @author Adrian
 * @since 2017-11-16
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final List<FragmentHolder> fragments = new ArrayList<>();

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public boolean addFragment(Fragment fragment, String title) {
        return fragments.add(new FragmentHolder(fragment, title));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    public String getItemTitle(int position) {
        return fragments.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @AllArgsConstructor
    @Getter
    private static class FragmentHolder {
        private Fragment fragment;
        private String title;
    }
}
