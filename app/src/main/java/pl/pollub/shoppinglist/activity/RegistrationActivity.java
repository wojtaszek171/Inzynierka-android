package pl.pollub.shoppinglist.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import pl.pollub.shoppinglist.R;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.menuRegister);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerEmail = findViewById(R.id.register_email);
        registerLogin = findViewById(R.id.register_login);
        registerPassword = findViewById(R.id.login_password);
        registerRepeatedPassword = findViewById(R.id.register_repeat_password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fetchRegisterFormData();

                //TODO: walidacja danych

                if (password.equals(repeatedPassword)) {
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(login);
                    newUser.setPassword(password);
                    newUser.setEmail(email);

                    newUser.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            Context context = getApplicationContext();
                            String text;

                            if (e == null) {
                                // Hooray! Let them use the app now.

                                resetRegistrationForm();
                                text = "Pomyślnie zarejestrowano!";

                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                resetPasswordTextView();
                                text = e.getMessage();

                            }
                            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
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
    }

    private void fetchRegisterFormData() {
        email = registerEmail.getText().toString();
        login = registerLogin.getText().toString();
        password = registerPassword.getText().toString();
        repeatedPassword = registerRepeatedPassword.getText().toString();
    }
}
