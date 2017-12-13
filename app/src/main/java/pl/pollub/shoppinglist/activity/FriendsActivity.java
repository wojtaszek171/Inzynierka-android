package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
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

        attachFriendFunctionalityFragment(FriendListFragment.class, false);

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
    public void onSearchClick(View view) {
        attachFriendFunctionalityFragment(FriendSearchFragment.class, true);
    }

    @Override
    public void onApproveClick(View view) {
        attachFriendFunctionalityFragment(FriendApproveFragment.class, true);
    }

    @Override
    public void onOpenMessagesClick(View view, User selectedFriend) {
        Intent intent = new Intent(this, MessagesActivity.class);
        intent.putExtra(User.CLASS_NAME, selectedFriend);
        startActivity(intent);
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

    protected void attachFriendFunctionalityFragment(Class<? extends Fragment> fragmentClass, boolean addToBackStack) {
        if (User.getCurrentUser() == null) {
            Toast.makeText(this, "You must be logged in to use this functionality!", Toast.LENGTH_LONG).show();
            return;
        }

        Fragment fragment;

        try {
            fragment = fragmentClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.friend_fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
