package pl.pollub.shoppinglist.util;

import android.support.design.widget.TabLayout;

/**
 * @author Adrian
 * @since 2017-12-16
 */
@FunctionalInterface
public interface OnTabSelectedListener extends TabLayout.OnTabSelectedListener {

    @Override
    void onTabSelected(TabLayout.Tab tab);

    @Override
    default void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    default void onTabReselected(TabLayout.Tab tab) {
    }
}
