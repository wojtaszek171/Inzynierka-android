package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.fragment.FriendApproveFragment;
import pl.pollub.shoppinglist.activity.fragment.FriendListFragment;
import pl.pollub.shoppinglist.activity.fragment.FriendSearchFragment;
import pl.pollub.shoppinglist.adapter.FragmentPagerAdapter;
import pl.pollub.shoppinglist.databinding.ActivityFriendsBinding;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.util.OnTabSelectedListener;

import static pl.pollub.shoppinglist.util.ToastUtils.showLongToast;

/**
 * @author Adrian
 * @since 2017-11-16
 */
public class FriendsActivity extends BaseNavigationActivity implements
        FriendListFragment.OnFriendListInteractionListener,
        FriendSearchFragment.OnFriendSearchInteractionListener,
        FriendApproveFragment.OnFriendApproveInteractionListener {

    private ActivityFriendsBinding binding;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friends);

        setSupportActionBar(binding.toolbar);
        setTitle(R.string.friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(new FriendListFragment(), getString(R.string.my_friends));
        fragmentPagerAdapter.addFragment(new FriendApproveFragment(), getString(R.string.invites));
        fragmentPagerAdapter.addFragment(new FriendSearchFragment(), getString(R.string.search_engine));
        binding.tabBar.addTab(binding.tabBar.newTab()
                        .setText(fragmentPagerAdapter.getItemTitle(0)));
        binding.tabBar.addTab(binding.tabBar.newTab()
                        .setText(fragmentPagerAdapter.getItemTitle(1)));
        binding.tabBar.addTab(binding.tabBar.newTab()
                        .setText(fragmentPagerAdapter.getItemTitle(2)));
        binding.tabBar.setTabGravity(TabLayout.GRAVITY_FILL);
        binding.tabBar.addOnTabSelectedListener((OnTabSelectedListener) tab ->
                binding.friendViewPager.setCurrentItem(tab.getPosition()));

        binding.friendViewPager.setAdapter(fragmentPagerAdapter);
        binding.friendViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(binding.tabBar)
        );
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return binding.drawerLayout;
    }

    @Override
    protected NavigationView getNavigationView() {
        return binding.navViewLayout.navView;
    }

    @Override
    public void onOpenMessagesClick(View view, User selectedFriend) {
        Intent intent = new Intent(this, MessagesActivity.class);
        intent.putExtra(User.CLASS_NAME, selectedFriend);
        startActivity(intent);
    }

    @Override
    public void onFriendListInteraction(Uri uri) {
    }

    @Override
    public void onFriendApproveInteraction(Uri uri) {
    }

    @Override
    public void onFriendSearchInteraction(Uri uri) {
    }
}
