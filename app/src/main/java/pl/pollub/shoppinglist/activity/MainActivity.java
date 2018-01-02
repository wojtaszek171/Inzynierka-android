package pl.pollub.shoppinglist.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.fragment.LoginFragment;
import pl.pollub.shoppinglist.activity.fragment.RegistrationFragment;
import pl.pollub.shoppinglist.activity.fragment.WelcomeFragment;
import pl.pollub.shoppinglist.databinding.ActivityMainBinding;
import pl.pollub.shoppinglist.model.User;

import static pl.pollub.shoppinglist.util.MiscUtils.attachFragment;

public class MainActivity extends AppCompatActivity implements
        WelcomeFragment.OnWelcomeInteractionListener,
        LoginFragment.OnLoginInteractionListener {

    public static final String GO_TO_LOGIN_KEY = "goToLogin";
    public static final String GO_TO_REGISTRATION_KEY = "goToRegistration";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar.toolbar);

        if (isKeySet(GO_TO_LOGIN_KEY)) {
            attachFragment(new LoginFragment(), getSupportFragmentManager(),
                    binding.mainFragmentContainer.getId(), false);
        } else if (isKeySet(GO_TO_REGISTRATION_KEY)) {
            attachFragment(new RegistrationFragment(), getSupportFragmentManager(),
                    binding.mainFragmentContainer.getId(), false);
        } else if (User.getCurrentUser() != null) {
            openShoppingListsActivity(true);
        } else {
            attachFragment(new WelcomeFragment(), getSupportFragmentManager(),
                    binding.mainFragmentContainer.getId(), false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoginClick(View view) {
        attachFragment(new LoginFragment(), getSupportFragmentManager(),
                binding.mainFragmentContainer.getId(), true);
    }

    @Override
    public void onRegisterClick(View view) {
        attachFragment(new RegistrationFragment(), getSupportFragmentManager(),
                binding.mainFragmentContainer.getId(), true);
    }

    @Override
    public void onCreateListsClick(View view) {
        openShoppingListsActivity(false);
    }

    protected void openShoppingListsActivity(boolean clearActivityStack) {
        Intent intent = new Intent(this, ShoppingListsActivity.class);

        if (clearActivityStack) {
            // passing flag to clear activity stack so that the application will exit on 'back' button press
            // https://stackoverflow.com/a/16388608
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
    }

    private boolean isKeySet(String key) {
        return getIntent().getBooleanExtra(key, false);
    }
}
