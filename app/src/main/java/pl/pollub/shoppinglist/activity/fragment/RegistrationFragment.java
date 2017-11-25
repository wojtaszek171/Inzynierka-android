package pl.pollub.shoppinglist.activity.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.databinding.FragmentRegistrationBinding;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;

/**
 * @author Adrian
 * @since 2017-11-25
 */
public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);

        binding.emailInput.requestFocus();
        binding.registerButton.setOnClickListener(this::onRegisterClick);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle(R.string.menuRegister);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void onRegisterClick(View view) {
        //TODO: walidacja danych

        if (getText(binding.passwordInput).equals(getText(binding.pwdRepeatInput))) {
            User newUser = new User();
            newUser.setUsername(getText(binding.loginInput));
            newUser.setPassword(getText(binding.passwordInput));
            newUser.setEmail(getText(binding.emailInput));

            newUser.signUpInBackground(exception -> {
                if (exception == null) {

                    try {
                        UserData newUserData = new UserData();
                        User.getCurrentUser().setUserData(newUserData);
                        User.getCurrentUser().save();
                    } catch (ParseException e) {
                        Log.e("RegistrationActivity", exception.getMessage());
                    }

                    resetRegistrationForm();
                    String text = "Pomyślnie zarejestrowano!";
                    Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

                } else {
                    resetPasswordTextView();
                    Log.e("RegistrationActivity", exception.getMessage());
                }
            });
        }
    }

    private void resetPasswordTextView() {
        binding.passwordInput.setText("");
        binding.pwdRepeatInput.setText("");
    }

    private void resetRegistrationForm() {
        resetPasswordTextView();
        binding.emailInput.setText("");
        binding.loginInput.setText("");
        binding.loginInput.clearComposingText();
    }

    private String getText(TextView view) {
        return view.getText().toString();
    }
}
