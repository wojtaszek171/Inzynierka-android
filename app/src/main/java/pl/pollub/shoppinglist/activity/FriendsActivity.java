package pl.pollub.shoppinglist.activity;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.fragment.FriendApproveFragment;
import pl.pollub.shoppinglist.activity.fragment.FriendListFragment;
import pl.pollub.shoppinglist.activity.fragment.FriendSearchFragment;
import pl.pollub.shoppinglist.model.User;

/**
 * @author Adrian
 * @since 2017-11-16
 */
public class FriendsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        FriendListFragment.OnFriendListInteractionListener,
        FriendSearchFragment.OnFriendSearchInteractionListener,
        FriendApproveFragment.OnFriendApproveInteractionListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        toolbar = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        setTitle(R.string.friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (User.getCurrentUser() != null) {
            String user = User.getCurrentUser().getUsername();
            View hView = navigationView.getHeaderView(0);
            TextView username = hView.findViewById(R.id.user_pseudonym);
            username.setText(user);
        }

        attachFriendListFragment();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_friends: {
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            }
            case R.id.nav_lists: {
                Intent intent = new Intent(FriendsActivity.this, ShoppingListsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_templates: {
                Intent intent = new Intent(FriendsActivity.this, TemplatesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_custom_user_products: {
                Intent intent = new Intent(FriendsActivity.this, CustomProductsListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(FriendsActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                ParseUser.logOut();
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        //drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menulists, menu);
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
            transaction.setCustomAnimations(R.anim.grow_from_middle, R.anim.shrink_to_middle)
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
            transaction.setCustomAnimations(R.anim.grow_from_middle, R.anim.shrink_to_middle)
                    .replace(R.id.friend_fragment_container, fragment)
                    .addToBackStack(null).commit();
        }
    }
}
