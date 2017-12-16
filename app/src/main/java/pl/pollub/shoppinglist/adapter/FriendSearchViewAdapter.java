package pl.pollub.shoppinglist.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;
import pl.pollub.shoppinglist.util.TimeUtils;

import static android.support.v4.view.ViewCompat.setBackgroundTintList;
import static pl.pollub.shoppinglist.util.MiscUtils.getRandomAvatarBackgroundColor;
import static pl.pollub.shoppinglist.util.ToastUtils.showToast;

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
        View view = inflater.inflate(R.layout.item_friend_sa, viewGroup, false);

        return view;
    }

    @Override
    protected void bindView(User item, int position, BaseRecyclerViewAdapter.ViewHolder viewHolder) {
        if (item == null || !item.isDataAvailable()) {
            throw new IllegalArgumentException("User is null or not fetched");
        }

        final TextView profileImage = (TextView) viewHolder.getView(R.id.profile_image_sa);
        final TextView usernameLabel = (TextView) viewHolder.getView(R.id.username_sa);
        final TextView lastActiveAtLabel = (TextView) viewHolder.getView(R.id.last_active_at_sa);
        final Button inviteButton = (Button) viewHolder.getView(R.id.action_button_sa);

        final String username = item.getUsername();
        final char firstChar = username.toUpperCase().charAt(0);
        profileImage.setText(Character.toString(firstChar));
        setBackgroundTintList(profileImage, ColorStateList.valueOf(getRandomAvatarBackgroundColor()));
        usernameLabel.setText(item.getUsername());
        lastActiveAtLabel.setText(TimeUtils.getRelativeTimeString(getContext(), item.getLastActiveAt()));

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
                        showToast(getContext(), "Wysłano zaproszenie!");
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
        button.setText("Wysłano");
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
