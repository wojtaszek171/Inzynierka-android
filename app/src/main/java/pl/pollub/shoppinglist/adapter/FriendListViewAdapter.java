package pl.pollub.shoppinglist.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Arrays;
import java.util.Date;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.User;

/**
 * @author Adrian
 * @since 2017-11-19
 */
public class FriendListViewAdapter extends BaseRecyclerViewAdapter<User> {

    private User currentUser = User.getCurrentUser();

    public FriendListViewAdapter(Context context) {
        super(context);
    }

    public FriendListViewAdapter(Context context, OnViewHolderClick<User> listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_friend_list, viewGroup, false);

        return view;
    }

    @Override
    protected void bindView(User item, int position, BaseRecyclerViewAdapter.ViewHolder viewHolder) {
        if (item == null) {
            Toast.makeText(getContext(), "FriendListAdapter: User is null", Toast.LENGTH_LONG).show();
            Log.w("FriendListAdapter", "User is null");
            return;
        }

        final TextView usernameLabel = (TextView) viewHolder.getView(R.id.username_list);
        final TextView lastActiveAtLabel = (TextView) viewHolder.getView(R.id.lastActiveAt_list);
        final AppCompatImageButton messageButton = (AppCompatImageButton) viewHolder.getView(R.id.actionMessageButton_list);
        final AppCompatImageButton deleteButton = (AppCompatImageButton) viewHolder.getView(R.id.actionDeleteButton_list);

        item.fetchIfNeededInBackground((updatedFriend, exception) ->
                onFetchDone((User) updatedFriend, exception, position, usernameLabel, lastActiveAtLabel, messageButton, deleteButton));
    }

    // object fields must be initialized before calling this method
    private void onFetchDone(User updatedFriend, ParseException exception, int currentPosition,
                             TextView usernameLabel, TextView lastActiveAtLabel,
                             AppCompatImageButton messageButton, AppCompatImageButton deleteButton) {
        if (exception == null) {
            usernameLabel.setText(updatedFriend.getUsername());

            Date lastActiveAtTime = updatedFriend.getLastActiveAt();

            if (lastActiveAtTime == null) {
                lastActiveAtLabel.setText("never");
            } else {
                lastActiveAtLabel.setText(DateUtils.getRelativeTimeSpanString(
                        getContext(),
                        lastActiveAtTime.getTime()
                ));
            }

            messageButton.setOnClickListener(view -> onMessageButtonClick(view, updatedFriend));
            deleteButton.setOnClickListener(view -> onDeleteButtonClick(view, updatedFriend));
        } else {
            Toast.makeText(getContext(), "FriendListAdapter " + exception.getMessage(), Toast.LENGTH_LONG).show();
            Log.w("FriendListAdapter", exception.getMessage());
        }
    }

    private void onMessageButtonClick(View view, User currentFriend) {
        Toast.makeText(getContext(), "messageButton: nie zaimplementowano", Toast.LENGTH_SHORT).show();
    }

    private void onDeleteButtonClick(View view, User currentFriend) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setIcon(R.drawable.ic_action_delete)
                .setTitle("Usuń znajomego")
                .setMessage("Czy na pewno chcesz usunąć znajomego? Tej operacji nie da się odwrócić.")
                .setPositiveButton(R.string.ok, ((dialog, which) -> onAlertDialogButtonClick(dialog, which, currentFriend)))
                .setNegativeButton(R.string.cancel, ((dialog, which) -> onAlertDialogButtonClick(dialog, which, currentFriend)))
                .create()
                .show();
    }

    private void onAlertDialogButtonClick(DialogInterface dialog, int which, User currentFriend) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                currentUser.getUserData().removeFriend(currentFriend);
                currentFriend.getUserData().removeFriend(currentUser);
                ParseObject.saveAllInBackground(Arrays.asList(currentUser, currentFriend), exception -> {
                    if (exception == null) {
                        Toast.makeText(getContext(), "Pomyślnie usunięto znajomego!", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case DialogInterface.BUTTON_NEGATIVE:
            case DialogInterface.BUTTON_NEUTRAL:
        }

        dialog.dismiss();
    }
}
