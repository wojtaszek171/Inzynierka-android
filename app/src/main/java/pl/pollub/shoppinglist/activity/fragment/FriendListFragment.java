package pl.pollub.shoppinglist.activity.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;

import java.util.List;

import bolts.Task;
import lombok.Getter;
import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.FriendListViewAdapter;
import pl.pollub.shoppinglist.databinding.FragmentFriendListBinding;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;
import pl.pollub.shoppinglist.util.ToastUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFriendListInteractionListener} interface
 * to handle interaction events.
 *
 * @author Adrian
 * @since 2017-11-16
 */
public class FriendListFragment extends Fragment {

    @Getter
    private OnFriendListInteractionListener interactionListener;
    private FragmentFriendListBinding binding;
    private FriendListViewAdapter recyclerViewAdapter;
    private AppCompatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_list, container, false);
        onInteracted(null);
        binding.spinnerLayout.loadingSpinner.setVisibility(View.VISIBLE);
        binding.friendList.setVisibility(View.GONE);
        recyclerViewAdapter = new FriendListViewAdapter(getContext(), this);
        binding.friendList.setAdapter(recyclerViewAdapter);

        LinearLayoutManager recyclerLayoutManager = (LinearLayoutManager) binding.friendList.getLayoutManager();
        binding.friendList.addItemDecoration(
                new DividerItemDecoration(getContext(), recyclerLayoutManager.getOrientation())
        );
        binding.addFriendsButton.setOnClickListener(interactionListener::onSearchClick);
        binding.approveFriendsButton.setOnClickListener(interactionListener::onApproveClick);
        findAndBindFriends();

        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onInteracted(Uri uri) {
        if (interactionListener != null) {
            interactionListener.onFriendListInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFriendListInteractionListener
                && context instanceof AppCompatActivity) {
            interactionListener = (OnFriendListInteractionListener) context;
            activity = (AppCompatActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must extend/implement "
                    + AppCompatActivity.class.getSimpleName()
                    + " and " + OnFriendListInteractionListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
        activity = null;
    }

    private void findAndBindFriends() {
        Task<UserData> dataTask = User.getCurrentUser()
                .getUserData()
                .fetchInBackground();

        dataTask.onSuccessTask(task -> {
            List<User> friends = task.getResult().getFriends();

            if (friends == null || friends.isEmpty()) {
                return Task.cancelled();
            }

            return ParseObject.fetchAllInBackground(friends);
        }).continueWith(task -> {
            activity.runOnUiThread(() -> binding.spinnerLayout.loadingSpinner.setVisibility(View.GONE));

            if (!task.isFaulted() && !task.isCancelled()) {
                List<User> friends = task.getResult();

                activity.runOnUiThread(() -> {
                    binding.friendList.setVisibility(View.VISIBLE);
                    recyclerViewAdapter.setList(friends);
                });

                return null;
            } else if (task.isFaulted()) {
                activity.runOnUiThread(() -> ToastUtils.showLongToast(getContext(), "Nie udało się załadować znajomych!"));

                Log.w("FriendListFrag", task.getError());
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
    public interface OnFriendListInteractionListener {
        void onSearchClick(View view);

        void onApproveClick(View view);

        void onOpenMessagesClick(View view, User selectedFriend);

        void onFriendListInteraction(Uri uri);
    }
}
