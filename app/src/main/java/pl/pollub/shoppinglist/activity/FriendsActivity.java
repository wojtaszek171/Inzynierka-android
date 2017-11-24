package pl.pollub.shoppinglist.activity;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.widget.Toast;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.fragment.FriendApproveFragment;
import pl.pollub.shoppinglist.activity.fragment.FriendListFragment;
import pl.pollub.shoppinglist.activity.fragment.FriendSearchFragment;
import pl.pollub.shoppinglist.databinding.ActivityFriendsBinding;
import pl.pollub.shoppinglist.model.User;

/**
 * @author Adrian
 * @since 2017-11-16
 */
public class FriendsActivity extends BaseNavigationActivity implements
        FriendListFragment.OnFriendListInteractionListener,
        FriendSearchFragment.OnFriendSearchInteractionListener,
        FriendApproveFragment.OnFriendApproveInteractionListener {

    private ActivityFriendsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friends);

        setSupportActionBar(binding.toolbarLayout.toolbar);
        setTitle(R.string.friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attachFriendListFragment();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menulistswithfriends, menu);
        return true;
    }

    @Override
    public void onSearchClick(Uri uri) {
        attachFriendSearchFragment();
    }

    @Override
    public void onApproveClick(Uri uri) {
        attachFriendApproveFragment();
    }

    @Override
    public void onFriendListInteraction(Uri uri) {
        setTitle(R.string.friends);
    }

    @Override
    public void onFriendApproveInteraction(Uri uri) {
        setTitle("Zaproszenia");
    }

    @Override
    public void onFriendSearchInteraction(Uri uri) {
        setTitle("Dodaj znajomych");
    }

    protected void attachFriendListFragment() {
        if (User.getCurrentUser() == null) {
            Toast.makeText(this, "You must be logged in to use this functionality!", Toast.LENGTH_LONG).show();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FriendListFragment fragment = new FriendListFragment();
            transaction.replace(R.id.friend_fragment_container, fragment)
                    .commit();
        }
    }

    protected void attachFriendSearchFragment() {
        if (User.getCurrentUser() == null) {
            Toast.makeText(this, "You must be logged in to use this functionality!", Toast.LENGTH_LONG).show();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FriendSearchFragment fragment = new FriendSearchFragment();
            transaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.friend_fragment_container, fragment)
                    .addToBackStack(null).commit();
        }
    }

    protected void attachFriendApproveFragment() {
        if (User.getCurrentUser() == null) {
            Toast.makeText(this, "You must be logged in to use this functionality!", Toast.LENGTH_LONG).show();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FriendApproveFragment fragment = new FriendApproveFragment();
            transaction
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.friend_fragment_container, fragment)
                    .addToBackStack(null).commit();
        }
    }
}
