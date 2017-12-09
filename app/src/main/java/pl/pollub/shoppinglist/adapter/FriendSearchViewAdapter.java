package pl.pollub.shoppinglist.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;

public class FriendSearchViewAdapter extends BaseRecyclerViewAdapter<User> {

    public FriendSearchViewAdapter(Context context) {
        super(context);
    }

    public FriendSearchViewAdapter(Context context, OnViewHolderClick<User> listener) {
        super(context, listener);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_friend_search, viewGroup, false);

        return view;
    }

    @Override
    protected void bindView(User item, int position, BaseRecyclerViewAdapter.ViewHolder viewHolder) {
        if (item == null || !item.isDataAvailable()) {
            throw new IllegalArgumentException("User is null or not fetched");
        }

        final TextView usernameLabel = (TextView) viewHolder.getView(R.id.username);
        final TextView lastActiveAtLabel = (TextView) viewHolder.getView(R.id.lastActiveAt);
        final Button inviteButton = (Button) viewHolder.getView(R.id.actionButton);

        usernameLabel.setText(item.getUsername());

        Date lastActiveAt = item.getLastActiveAt();
        if (lastActiveAt == null) {
            lastActiveAtLabel.setText(R.string.never);
        } else {
            lastActiveAtLabel.setText(DateUtils.getRelativeTimeSpanString(
                    getContext(),
                    lastActiveAt.getTime()
            ));
        }

        final UserData itemData = item.getUserData();

        if (hasAlreadyBeenInvitedByCurrentUser(item)
                || item.equalsEntity(User.getCurrentUser())
                || isAlreadyFriendOfCurrentUser(item)) {
            markButtonAlreadyInvited(inviteButton);
        } else if (hasAlreadyInvitedCurrentUser(item)) {
            markButtonApproveInvitation(inviteButton);
        } else {
            resetButtonState(inviteButton);
            inviteButton.setOnClickListener(view -> {
                itemData.addUniqueInviter(User.getCurrentUser());
                itemData.saveInBackground(exception -> {
                    if (exception == null) {
                        Toast.makeText(getContext(), "Wysłano zaproszenie!", Toast.LENGTH_SHORT).show();
                    } else {
                        resetButtonState(inviteButton);
                    }
                });
                markButtonAlreadyInvited((Button) view);
            });
        }
    }

    private static boolean hasAlreadyBeenInvitedByCurrentUser(User user) {
        List<User> inviters = user.getUserData().getInviters();

        if (inviters == null) {
            return false;
        }

        for (User inviter : inviters) {
            if (inviter.equalsEntity(User.getCurrentUser())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAlreadyFriendOfCurrentUser(User user) {
        List<User> friends = User.getCurrentUser().getUserData().getFriends();

        if (friends == null || friends.isEmpty()) {
            return false;
        }

        for (User friend : friends) {
            if (friend.equalsEntity(user)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasAlreadyInvitedCurrentUser(User user) {
        List<User> inviters = User.getCurrentUser().getUserData().getInviters();

        if (inviters == null || inviters.isEmpty()) {
            return false;
        }

        for (User inviter : inviters) {
            if (inviter.equalsEntity(user)) {
                return true;
            }
        }
        return false;
    }

    private static void markButtonAlreadyInvited(Button button) {
        button.setText("Zaproszony");
        button.setEnabled(false);
    }

    private static void markButtonApproveInvitation(Button button) {
        button.setText("Zaprosił cię");
        button.setEnabled(false);
    }

    private static void resetButtonState(Button button) {
        button.setText("Zaproś");
        button.setEnabled(true);
    }
}
