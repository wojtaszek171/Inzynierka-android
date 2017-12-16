package pl.pollub.shoppinglist.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseException;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.ShoppingListsActivity;
import pl.pollub.shoppinglist.databinding.FragmentRegistrationBinding;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;
import pl.pollub.shoppinglist.util.TextWatcher;

import static com.parse.ParseException.*;
import static pl.pollub.shoppinglist.util.ToastUtils.*;

/**
 * @author Adrian
 * @since 2017-11-25
 */
public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;
    private AppCompatActivity activity;
    private ActionBar actionBar;
    private boolean validated;
    private TextWatcher textWatcher = (text) -> clearValidation();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false);

        binding.emailInput.requestFocus();
        binding.registerButton.setOnClickListener(this::onRegisterClick);
        binding.emailInput.addTextChangedListener(textWatcher);
        binding.loginInput.addTextChangedListener(textWatcher);
        binding.passwordInput.addTextChangedListener(textWatcher);
        binding.pwdRepeatInput.addTextChangedListener(textWatcher);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle(R.string.register);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must extend "
                    + AppCompatActivity.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    private void onRegisterClick(View view) {
        if (!isFormValid()) {
            return;
        }

        final User newUser = new User();
        newUser.setUsername(getTrimmedText(binding.loginInput));
        newUser.setPassword(getTrimmedText(binding.passwordInput));
        newUser.setEmail(getTrimmedText(binding.emailInput));

        newUser.signUpInBackground().onSuccessTask(task -> {
            UserData newUserData = new UserData();
            newUser.setUserData(newUserData);

            return newUser.saveInBackground();
        }).continueWith(task -> {
            if (task.isFaulted() || task.isCancelled()) {
                Runnable showError = null;

                if (task.getError() instanceof ParseException) {
                    ParseException exception = (ParseException) task.getError();
                    switch (exception.getCode()) {
                        case INVALID_EMAIL_ADDRESS:
                        case EMAIL_MISSING:
                            showError = () -> binding.emailLayout.setError(getString(R.string.email_format_invalid));
                            break;
                        case USERNAME_MISSING:
                            showError = () -> binding.loginLayout.setError(getString(R.string.login_missing));
                            break;
                        case USERNAME_TAKEN:
                            showError = () -> binding.loginLayout.setError(getString(R.string.login_taken));
                            break;
                        case EMAIL_TAKEN:
                            showError = () -> binding.emailLayout.setError(getString(R.string.email_taken));
                            break;
                        case TIMEOUT:
                            showError = () -> showToast(getContext(), R.string.server_timed_out);
                            break;
                        case CONNECTION_FAILED:
                            showError = () -> showToast(getContext(), R.string.connection_failed);
                            break;
                        default:
                            Log.w("RegistrationActivity", exception);
                            showError = () -> showToast(getContext(), R.string.unknown_error);
                    }
                } else {
                    Log.w("RegistrationActivity", task.getError());
                    showError = () -> showToast(getContext(), R.string.unknown_error);
                }

                activity.runOnUiThread(showError);
                return null;
            }

            activity.runOnUiThread(() -> {
                resetRegistrationForm();
                showLongToast(getContext(), R.string.registration_completed);
            });
            Intent intent = new Intent(getActivity(), ShoppingListsActivity.class);
            // passing flag to clear activity stack so that the application will exit on 'back' button press
            // https://stackoverflow.com/a/16388608
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            return null;
        });
    }

    private boolean isFormValid() {
        boolean valid = true;
        validated = true;

        // email is valid
        if (!isEmailValid(getTrimmedText(binding.emailInput))) {
            binding.emailLayout.setError(getString(R.string.email_format_invalid));
            valid = false;
        }

        // username has at least 4 characters
        if (getTrimmedText(binding.loginInput).length() < 4) {
            binding.loginLayout.setError(getString(R.string.login_too_short));
            valid = false;
        }

        // password has at least 6 characters and is different than login
        if (getTrimmedText(binding.passwordInput).length() < 6
                || getTrimmedText(binding.passwordInput).equals(getTrimmedText(binding.loginInput))) {
            binding.passwordLayout.setError(getString(R.string.pwd_too_short_or_same_as_login));
            valid = false;
        }

        // passwords match
        if (!getTrimmedText(binding.passwordInput).equals(getTrimmedText(binding.pwdRepeatInput))) {
            binding.repeatPasswordLayout.setError(getString(R.string.passwords_dont_match));
            valid = false;
        }

        return valid;
    }

    public static boolean isEmailValid(String target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void clearValidation() {
        if (!validated) {
            return;
        }

        validated = false;
        binding.emailLayout.setError(null);
        binding.loginLayout.setError(null);
        binding.passwordLayout.setError(null);
        binding.repeatPasswordLayout.setError(null);
    }

    private void resetPasswordTextView() {
        binding.passwordInput.setText(null);
        binding.pwdRepeatInput.setText(null);
    }

    private void resetRegistrationForm() {
        resetPasswordTextView();
        binding.emailInput.setText(null);
        binding.loginInput.setText(null);
        binding.loginInput.clearComposingText();
    }

    private static String getTrimmedText(TextView view) {
        return view.getText().toString().trim();
    }
}
