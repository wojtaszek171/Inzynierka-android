package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import pl.pollub.shoppinglist.R;

public class SettingsActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return findViewById(R.id.drawer_layout);
    }

    @Override
    protected NavigationView getNavigationView() {
        return findViewById(R.id.nav_view);
    }

    public void onAboutButtonClick(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
