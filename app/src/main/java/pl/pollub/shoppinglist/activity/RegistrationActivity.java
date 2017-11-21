package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;

public class RegistrationActivity extends AppCompatActivity {
    private TextView registerEmail;
    private TextView registerLogin;
    private TextView registerPassword;
    private TextView registerRepeatedPassword;
    private Button registerButton;

    private String email;
    private String login;
    private String password;
    private String repeatedPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.menuRegister);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerEmail = findViewById(R.id.register_email);
        registerLogin = findViewById(R.id.register_login);
        registerPassword = findViewById(R.id.login_password);
        registerRepeatedPassword = findViewById(R.id.register_repeat_password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(view -> {
            fetchRegisterFormData();

            //TODO: walidacja danych

            if (password.equals(repeatedPassword)) {
                User newUser = new User();
                newUser.setUsername(login);
                newUser.setPassword(password);
                newUser.setEmail(email);

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
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                    } else {
                        resetPasswordTextView();
                        Log.e("RegistrationActivity", exception.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetPasswordTextView() {
        registerPassword.setText("");
        registerRepeatedPassword.setText("");
    }

    private void resetRegistrationForm() {
        resetPasswordTextView();
        registerEmail.setText("");
        registerLogin.setText("");
        registerLogin.clearComposingText();
    }

    private void fetchRegisterFormData() {
        email = registerEmail.getText().toString();
        login = registerLogin.getText().toString();
        password = registerPassword.getText().toString();
        repeatedPassword = registerRepeatedPassword.getText().toString();
    }
}
