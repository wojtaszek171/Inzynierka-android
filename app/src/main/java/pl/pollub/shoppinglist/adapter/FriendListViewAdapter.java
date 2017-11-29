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
        if (item == null || !item.isDataAvailable()) {
            throw new NullPointerException("User is null or not fetched");
        }

        final TextView usernameLabel = (TextView) viewHolder.getView(R.id.username_list);
        final TextView lastActiveAtLabel = (TextView) viewHolder.getView(R.id.lastActiveAt_list);
        final AppCompatImageButton messageButton = (AppCompatImageButton) viewHolder.getView(R.id.actionMessageButton_list);
        final AppCompatImageButton deleteButton = (AppCompatImageButton) viewHolder.getView(R.id.actionDeleteButton_list);

        usernameLabel.setText(item.getUsername());

        Date lastActiveAtTime = item.getLastActiveAt();

        if (lastActiveAtTime == null) {
            lastActiveAtLabel.setText(R.string.never);
        } else {
            lastActiveAtLabel.setText(DateUtils.getRelativeTimeSpanString(
                    getContext(),
                    lastActiveAtTime.getTime()
            ));
        }

        messageButton.setOnClickListener(view -> onMessageButtonClick(view, item));
        deleteButton.setOnClickListener(view -> onDeleteButtonClick(view, item, position));
    }

    private void onMessageButtonClick(View view, User currentFriend) {
        Toast.makeText(getContext(), "messageButton: nie zaimplementowano", Toast.LENGTH_SHORT).show();
    }

    private void onDeleteButtonClick(View view, User currentFriend, int positionOnViewport) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setIcon(R.drawable.ic_action_delete)
                .setTitle("Usuń znajomego")
                .setMessage("Czy na pewno chcesz usunąć znajomego? Tej operacji nie da się odwrócić.")
                .setPositiveButton(R.string.ok, ((dialog, buttonId) ->
                        onAlertDialogButtonClick(dialog, buttonId, currentFriend, positionOnViewport)))
                .setNegativeButton(R.string.cancel, ((dialog, buttonId) ->
                        onAlertDialogButtonClick(dialog, buttonId, currentFriend, positionOnViewport)))
                .create()
                .show();
    }

    private void onAlertDialogButtonClick(DialogInterface dialog, int buttonId, User currentFriend, int friendPosition) {
        switch (buttonId) {
            case DialogInterface.BUTTON_POSITIVE:
                currentUser.getUserData().removeFriend(currentFriend);
                currentFriend.getUserData().removeFriend(currentUser);
                ParseObject.saveAllInBackground(Arrays.asList(currentUser, currentFriend), exception -> {
                    if (exception == null) {
                        Toast.makeText(getContext(), "Pomyślnie usunięto znajomego!", Toast.LENGTH_SHORT).show();
                        //notifyItemRemoved(friendPosition); TODO: nie działa
                    }
                });
                break;
            case DialogInterface.BUTTON_NEGATIVE:
            case DialogInterface.BUTTON_NEUTRAL:
        }

        dialog.dismiss();
    }
}
