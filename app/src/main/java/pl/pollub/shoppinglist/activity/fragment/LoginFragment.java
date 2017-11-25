package pl.pollub.shoppinglist.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.ShoppingListsActivity;
import pl.pollub.shoppinglist.databinding.FragmentLoginBinding;

/**
 * @author Adrian
 * @since 2017-11-25
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private OnLoginInteractionListener interactionListener;
    private ActionBar actionBar;
    private StringBuilder feedbackMessage = new StringBuilder(0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        binding.loginInput.requestFocus();
        binding.loginButton.setOnClickListener(this::onLoginClick);
        binding.registerButton.setOnClickListener(interactionListener::onRegisterClick);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        actionBar.setTitle(R.string.menuLogInto);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginInteractionListener) {
            interactionListener = (OnLoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    protected void onLoginClick(View view) {
        ParseUser.logInInBackground(getText(binding.loginInput), getText(binding.passwordInput), this::onLoginDone);
    }

    private String getText(TextView view) {
        return view.getText().toString();
    }

    private void onLoginDone(ParseUser user, ParseException exception) {
        feedbackMessage.setLength(0);

        if (exception == null && user != null) {
            feedbackMessage.append("Zalogowano jako ").append(user.getUsername());
            // passing application context as argument so the application will exit on 'back' button press
            Intent intent = new Intent(getActivity().getApplicationContext(), ShoppingListsActivity.class);
            startActivity(intent);
        } else if (user == null) {
            feedbackMessage.append("Nieprawidłowy login i/lub hasło!");
        } else {
            feedbackMessage.append(exception.getMessage());
        }

        Toast.makeText(getContext(), feedbackMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLoginInteractionListener {
        void onRegisterClick(View view);
    }
}
