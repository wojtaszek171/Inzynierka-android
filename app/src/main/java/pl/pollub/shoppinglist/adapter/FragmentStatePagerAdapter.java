package pl.pollub.shoppinglist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * When pages are not visible to the user, their entire fragment may be destroyed, only keeping the saved state of that fragment.
 * This allows the pager to hold on to much less memory associated with each visited page as compared to FragmentPagerAdapter at the cost of potentially more overhead when switching between pages.
 *
 * @author Adrian
 * @since 2017-11-16
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class FragmentStatePagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentTitles = new ArrayList<>();

    public FragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private boolean addFragment(Fragment fragment, String title) {
        return fragments.add(fragment) && fragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
