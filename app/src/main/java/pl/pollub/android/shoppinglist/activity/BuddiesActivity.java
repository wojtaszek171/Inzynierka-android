package pl.pollub.android.shoppinglist.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import pl.pollub.android.shoppinglist.BR;
import pl.pollub.android.shoppinglist.R;
import pl.pollub.android.shoppinglist.databinding.ActivityBuddiesBinding;
import pl.pollub.android.shoppinglist.model.Buddy;
import pl.pollub.android.shoppinglist.util.AppDatabase;
import pl.pollub.android.shoppinglist.util.recyclerview.*;
import pl.pollub.android.shoppinglist.util.recyclerview.binder.*;
import pl.pollub.android.shoppinglist.viewmodel.BuddiesViewModel;

public class BuddiesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityBuddiesBinding viewBinding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private BuddiesViewModel buddiesViewModel = new BuddiesViewModel();
    private final ObservableList<Buddy> buddies = new ObservableArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_buddies);

        setSupportActionBar(viewBinding.toolbarLayout.toolbar);
        setTitle(R.string.friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = viewBinding.drawerLayout;
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewBinding.burgerLayout.navView.setNavigationItemSelectedListener(this);
        viewBinding.buddiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        buddiesViewModel.setModel(buddies);
        viewBinding.setBuddiesViewModel(buddiesViewModel);

        getBuddiesAsync();
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
                Intent intent = new Intent(BuddiesActivity.this, ListsList.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_templates: {
                Intent intent = new Intent(BuddiesActivity.this, TemplatesList.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_settings: {
                Intent intent = new Intent(BuddiesActivity.this, Settings.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_logout: {
                Intent intent = new Intent(BuddiesActivity.this, MainScreen.class);
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

    private void getBuddiesAsync() {
        new AsyncTask<Void, Void, List<Buddy>>() {
            @Override
            protected List<Buddy> doInBackground(Void... params) {
                AppDatabase.getInstance(BuddiesActivity.this.getApplicationContext()).populateInitialData();
                return AppDatabase.getInstance(BuddiesActivity.this.getApplicationContext())
                        .buddyDao().findAll();
            }

            @Override
            protected void onPostExecute(List<Buddy> buddies) {
                BuddiesActivity.this.buddies.addAll(buddies);
            }
        }.execute();
    }

    public ClickHandler<BuddiesViewModel> clickHandler() {
        return null;
    }

    public LongClickHandler<BuddiesViewModel> longClickHandler() {
        return null;
    }

    public ItemBinder<Buddy> itemViewBinder() {
        return new ItemBinderBase<>(BR.buddy, R.layout.item_buddy);
    }
}
