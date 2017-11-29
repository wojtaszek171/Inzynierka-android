package pl.pollub.shoppinglist.activity.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import bolts.Task;
import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.FriendSearchViewAdapter;
import pl.pollub.shoppinglist.databinding.FragmentFriendSearchBinding;
import pl.pollub.shoppinglist.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFriendSearchInteractionListener} interface
 * to handle interaction events.
 *
 * @author Adrian
 * @since 2017-11-17
 */
public class FriendSearchFragment extends Fragment {
    public static final int MINIMUM_CHARS = 3;
    private OnFriendSearchInteractionListener interactionListener;
    private AppCompatActivity activity;
    private FragmentFriendSearchBinding binding;
    private String searchQuery;
    private FriendSearchViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_friend_search, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_search, container, false);
        onInteracted(null);
        binding.progressBar.setVisibility(View.GONE);
        binding.searchList.setVisibility(View.GONE);
        recyclerViewAdapter = new FriendSearchViewAdapter(getContext());
        binding.searchList.setAdapter(recyclerViewAdapter);
        binding.searchButton.setOnClickListener(this::searchUsers);

        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onInteracted(Uri uri) {
        if (interactionListener != null) {
            interactionListener.onFriendSearchInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFriendSearchInteractionListener
                && context instanceof AppCompatActivity) {
            interactionListener = (OnFriendSearchInteractionListener) context;
            activity = (AppCompatActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must extend/implement "
                    + AppCompatActivity.class.getSimpleName()
                    + " and " + OnFriendSearchInteractionListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    protected void searchUsers(View view) {
        String searchInput = binding.searchEditText.getText().toString();
        if (searchInput.length() >= MINIMUM_CHARS) {
            searchQuery = searchInput;
            binding.emptyLabel.setVisibility(View.GONE);
            binding.searchList.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            findAndBindUsers(searchQuery);
        } else {
            Toast.makeText(activity, "Wprowadź przynajmniej 3 znaki", Toast.LENGTH_SHORT).show();
        }
    }

    private void findAndBindUsers(String nameSubstring) {
        ParseQuery.getQuery(User.class)
                .whereStartsWith(User.KEY_USERNAME, nameSubstring)
                .findInBackground()
                .onSuccessTask(task -> {
                    final List<User> users = task.getResult();

                    if (users != null && users.size() == 0) {
                        return Task.cancelled();
                    }

                    return ParseObject.fetchAllInBackground(users);
                })
                .continueWith(task -> {
                    activity.runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));

                    if (!task.isFaulted() && !task.isCancelled()) {
                        final List<User> users = task.getResult();

                        activity.runOnUiThread(() -> {
                            binding.searchList.setVisibility(View.VISIBLE);
                            recyclerViewAdapter.setList(users);
                            recyclerViewAdapter.notifyDataSetChanged();
                        });

                        return null;
                    } else if (task.isFaulted()) {
                        activity.runOnUiThread(() -> Toast.makeText(
                                getContext(),
                                "Nie udało się wyszukać użytkowników!",
                                Toast.LENGTH_LONG
                        ).show());

                        Log.w("FriendSearchFrag", task.getError());
                    }

                    activity.runOnUiThread(() -> binding.emptyLabel.setVisibility(View.VISIBLE));
                    return null;
                });
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
    public interface OnFriendSearchInteractionListener {
        void onFriendSearchInteraction(Uri uri);
    }
}
