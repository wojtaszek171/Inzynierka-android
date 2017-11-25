package pl.pollub.shoppinglist.activity.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.databinding.FragmentWelcomeBinding;

/**
 * @author Adrian
 * @since 2017-11-25
 */
public class WelcomeFragment extends Fragment {

    private FragmentWelcomeBinding binding;
    private OnWelcomeInteractionListener interactionListener;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false);

        binding.createListsButton.setOnClickListener(interactionListener::onCreateListsClick);
        binding.loginButton.setOnClickListener(interactionListener::onLoginClick);
        binding.registerButton.setOnClickListener(interactionListener::onRegisterClick);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWelcomeInteractionListener) {
            interactionListener = (OnWelcomeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWelcomeInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
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
    public interface OnWelcomeInteractionListener {
        void onCreateListsClick(View view);
        void onLoginClick(View view);
        void onRegisterClick(View view);
    }
}
