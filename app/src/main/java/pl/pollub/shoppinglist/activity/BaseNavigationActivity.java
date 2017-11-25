package pl.pollub.shoppinglist.activity;

import android.content.ClipData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.databinding.NavigationHeaderBinding;
import pl.pollub.shoppinglist.model.User;

/**
 * @author Adrian
 * @since 2017-11-19
 */
public abstract class BaseNavigationActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private boolean created = false;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private NavigationHeaderBinding headerBinding;

    protected abstract DrawerLayout getDrawerLayout();

    protected abstract NavigationView getNavigationView();

    @Override
    protected void onStart() {
        super.onStart();
        if (created) {
            return;
        }

        drawerLayout = getDrawerLayout();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerToggle.syncState();
        navigationView = getNavigationView();
        navigationView.setNavigationItemSelectedListener(this);

        // show current user's name
        if (User.getCurrentUser() != null) {
            String username = User.getCurrentUser().getUsername();
            headerBinding = DataBindingUtil.bind(navigationView.getHeaderView(0));
            headerBinding.usernameLabel.setText(username);
        }else
        {
            Menu menu = navigationView.getMenu();
            MenuItem logoutItem = menu.findItem(R.id.nav_logout);
            logoutItem.setTitle("Menu główne");
        }

        created = true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Intent intent = new Intent();
        // navigation view item clicks handling
        if (itemId == R.id.nav_friends && !(this instanceof FriendsActivity)) {
            finish();
            intent.setClass(this, FriendsActivity.class);
        } else if (itemId == R.id.nav_lists && !(this instanceof ShoppingListsActivity)) {
            finish();
            intent.setClass(this, ShoppingListsActivity.class);
        } else if (itemId == R.id.nav_templates && !(this instanceof TemplatesActivity)) {
            finish();
            intent.setClass(this, TemplatesActivity.class);
        } else if (itemId == R.id.nav_custom_user_products && !(this instanceof CustomProductsListActivity)) {
            finish();
            intent.setClass(this, CustomProductsListActivity.class);
        } else if (itemId == R.id.nav_settings && !(this instanceof SettingsActivity)) {
            finish();
            intent.setClass(this, SettingsActivity.class);
        } else if (itemId == R.id.nav_logout) {
            finish();
            ParseUser.logOut();
            intent.setClass(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        startActivity(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}