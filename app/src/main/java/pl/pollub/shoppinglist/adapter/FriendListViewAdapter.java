package pl.pollub.shoppinglist.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.Arrays;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.activity.fragment.FriendListFragment;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.util.TimeUtils;

import static pl.pollub.shoppinglist.util.ToastUtils.showToast;

/**
 * @author Adrian
 * @since 2017-11-19
 */
public class FriendListViewAdapter extends BaseRecyclerViewAdapter<User> {

    private User currentUser = User.getCurrentUser();
    private FriendListFragment fragment;

    public FriendListViewAdapter(Context context, FriendListFragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    public FriendListViewAdapter(Context context, FriendListFragment fragment, OnViewHolderClick<User> listener) {
        super(context, listener);
        this.fragment = fragment;
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
            throw new IllegalArgumentException("User is null or not fetched");
        }

        final TextView usernameLabel = (TextView) viewHolder.getView(R.id.username_list);
        final TextView lastActiveAtLabel = (TextView) viewHolder.getView(R.id.lastActiveAt_list);
        final AppCompatImageButton messageButton = (AppCompatImageButton) viewHolder.getView(R.id.actionMessageButton_list);
        final AppCompatImageButton deleteButton = (AppCompatImageButton) viewHolder.getView(R.id.actionDeleteButton_list);

        usernameLabel.setText(item.getUsername());
        lastActiveAtLabel.setText(TimeUtils.getRelativeTimeString(getContext(), item.getLastActiveAt()));

        messageButton.setOnClickListener(view -> fragment
                .getInteractionListener()
                .onOpenMessagesClick(view, item));
        deleteButton.setOnClickListener(view -> onDeleteButtonClick(view, item, position));
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
                        removeItem(currentFriend);
                        showToast(getContext(), "Pomyślnie usunięto znajomego!");
                    }
                });
                break;
            case DialogInterface.BUTTON_NEGATIVE:
            case DialogInterface.BUTTON_NEUTRAL:
        }

        dialog.dismiss();
    }
}
