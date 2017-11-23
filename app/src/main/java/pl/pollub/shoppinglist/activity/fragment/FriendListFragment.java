package pl.pollub.shoppinglist.activity.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.FriendListViewAdapter;
import pl.pollub.shoppinglist.databinding.FragmentFriendListBinding;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;

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

    private OnFriendListInteractionListener interactionListener;
    private FragmentFriendListBinding binding;
    private FriendListViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_friend_approve, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_list, container, false);
        onInteracted(null);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.friendList.setVisibility(View.GONE);
        recyclerViewAdapter = new FriendListViewAdapter(getContext());
        binding.friendList.setAdapter(recyclerViewAdapter);
        binding.addFriendsButton.setOnClickListener(v -> {
            if (interactionListener != null) {
                interactionListener.onSearchClick(null);
            }
        });
        binding.approveFriendsButton.setOnClickListener(v -> {
            if (interactionListener != null) {
                interactionListener.onApproveClick(null);
            }
        });
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
        if (context instanceof OnFriendListInteractionListener) {
            interactionListener = (OnFriendListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFriendSearchInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    private void findAndBindFriends() {
        User.getCurrentUser()
                .getUserData()
                .fetchInBackground((userData, exception) -> {
                    binding.progressBar.setVisibility(View.GONE);
                    if (exception == null) {
                        List<User> friends = ((UserData) userData).getFriends();

                        if (friends != null && friends.size() > 0) {
                            binding.friendList.setVisibility(View.VISIBLE);
                            recyclerViewAdapter.setList(friends);
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(
                                getContext(),
                                "Nie udało się załadować znajomych!",
                                Toast.LENGTH_SHORT
                        ).show();
                        Log.w("FriendListFrag", exception);
                    }
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
        void onSearchClick(Uri uri);

        void onApproveClick(Uri uri);

        void onFriendListInteraction(Uri uri);
    }
}
