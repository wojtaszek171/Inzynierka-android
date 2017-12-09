package pl.pollub.shoppinglist.activity.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseObject;

import java.util.List;

import bolts.Task;
import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.adapter.FriendApproveViewAdapter;
import pl.pollub.shoppinglist.databinding.FragmentFriendApproveBinding;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFriendApproveInteractionListener} interface
 * to handle interaction events.
 *
 * @author Adrian
 * @since 2017-11-17
 */
public class FriendApproveFragment extends Fragment {

    private OnFriendApproveInteractionListener interactionListener;
    private AppCompatActivity activity;
    private FragmentFriendApproveBinding binding;
    private FriendApproveViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_approve, container, false);
        onInteracted(null);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.approvalList.setVisibility(View.GONE);
        recyclerViewAdapter = new FriendApproveViewAdapter(getContext());
        binding.approvalList.setAdapter(recyclerViewAdapter);
        findAndBindInviters();

        return binding.getRoot();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onInteracted(Uri uri) {
        if (interactionListener != null) {
            interactionListener.onFriendApproveInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFriendApproveInteractionListener
                && context instanceof AppCompatActivity) {
            interactionListener = (OnFriendApproveInteractionListener) context;
            activity = (AppCompatActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must extend/implement "
                    + AppCompatActivity.class.getSimpleName()
                    + " and " + OnFriendApproveInteractionListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
        activity = null;
    }

    private void findAndBindInviters() {
        Task<UserData> dataTask = User.getCurrentUser()
                .getUserData()
                .fetchInBackground();

        dataTask.onSuccessTask(task -> {
            final List<User> inviters = task.getResult().getInviters();
            return ParseObject.fetchAllInBackground(inviters);
        }).continueWith(task -> {
            activity.runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));

            if (!task.isFaulted()) {
                final List<User> inviters = task.getResult();

                activity.runOnUiThread(() -> {
                    binding.approvalList.setVisibility(View.VISIBLE);
                    recyclerViewAdapter.setList(inviters);
                    recyclerViewAdapter.notifyDataSetChanged();
                });

                return null;
            } else {
                activity.runOnUiThread(() -> Toast.makeText(
                        getContext(),
                        "Nie udało się załadować zapraszających!",
                        Toast.LENGTH_LONG
                ).show());

                Log.w("FriendApproveFrag", task.getError());
            }

            //activity.runOnUiThread(() -> binding.emptyLabel.setVisibility(View.VISIBLE));
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
    public interface OnFriendApproveInteractionListener {
        void onFriendApproveInteraction(Uri uri);
    }
}
