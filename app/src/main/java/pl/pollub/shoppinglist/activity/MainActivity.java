package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.fragment.LoginFragment;
import pl.pollub.shoppinglist.activity.fragment.RegistrationFragment;
import pl.pollub.shoppinglist.activity.fragment.WelcomeFragment;
import pl.pollub.shoppinglist.databinding.ActivityMainScreenBinding;

public class MainActivity extends AppCompatActivity implements
        WelcomeFragment.OnWelcomeInteractionListener,
        LoginFragment.OnLoginInteractionListener {

    public static final String GO_TO_LOGIN_KEY = "goToLogin";
    public static final String GO_TO_REGISTRATION_KEY = "goToRegistration";
    private ActivityMainScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_screen);
        setSupportActionBar(binding.toolbar.toolbar);

        if (savedInstanceState != null && savedInstanceState.getBoolean(GO_TO_LOGIN_KEY)) {
            attachLoginFragment();
        } else if (savedInstanceState != null && savedInstanceState.getBoolean(GO_TO_REGISTRATION_KEY)) {
            attachRegistrationFragment();
        }

        if (ParseUser.getCurrentUser() != null) {
            openShoppingListsActivity();
        } else {
            attachWelcomeFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoginClick(View view) {
        attachLoginFragment();
    }

    @Override
    public void onRegisterClick(View view) {
        attachRegistrationFragment();
    }

    @Override
    public void onCreateListsClick(View view) {
        openShoppingListsActivity();
    }

    protected void openShoppingListsActivity() {
        finish();
        Intent intent = new Intent(this, ShoppingListsActivity.class);
        startActivity(intent);
    }

    protected void attachWelcomeFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        WelcomeFragment fragment = new WelcomeFragment();
        transaction.replace(R.id.main_fragment_container, fragment)
                .commit();
    }

    protected void attachLoginFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        LoginFragment fragment = new LoginFragment();
        transaction
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_fragment_container, fragment)
                .addToBackStack(null).commit();
    }

    protected void attachRegistrationFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        RegistrationFragment fragment = new RegistrationFragment();
        transaction
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.main_fragment_container, fragment)
                .addToBackStack(null).commit();
    }
}
