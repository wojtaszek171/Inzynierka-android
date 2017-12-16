package pl.pollub.shoppinglist.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.Arrays;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.util.TimeUtils;
import pl.pollub.shoppinglist.util.ToastUtils;

import static android.support.v4.view.ViewCompat.setBackgroundTintList;
import static pl.pollub.shoppinglist.util.MiscUtils.getRandomAvatarBackgroundColor;

/**
 * @author Adrian
 * @since 2017-11-19
 */
public class FriendApproveViewAdapter extends BaseRecyclerViewAdapter<User> {
    public FriendApproveViewAdapter(Context context) {
        super(context);
    }

    public FriendApproveViewAdapter(Context context, OnViewHolderClick<User> listener) {
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
        final Button approveButton = (Button) viewHolder.getView(R.id.action_button_sa);

        final String username = item.getUsername();
        final char firstChar = username.toUpperCase().charAt(0);
        profileImage.setText(Character.toString(firstChar));
        setBackgroundTintList(profileImage, ColorStateList.valueOf(getRandomAvatarBackgroundColor()));
        usernameLabel.setText(username);
        lastActiveAtLabel.setText(TimeUtils.getRelativeTimeString(getContext(), item.getLastActiveAt()));

        if (hasAlreadyBeenApprovedByCurrentUser(item)) {
            markButtonAlreadyApproved(approveButton);
        } else {
            final User currentUser = User.getCurrentUser();

            resetButtonState(approveButton);
            approveButton.setOnClickListener(view -> {
                currentUser.getUserData().addUniqueFriend(item);
                currentUser.getUserData().removeInviter(item);
                item.getUserData().addUniqueFriend(User.getCurrentUser());

                ParseObject.saveAllInBackground(Arrays.asList(currentUser, item), exception -> {
                    if (exception == null) {
                        ToastUtils.showToast(getContext(), "Potwierdzono znajomość!");
                    } else {
                        resetButtonState(approveButton);
                    }
                });

                markButtonAlreadyApproved((Button) view);
            });
        }
    }

    private static boolean hasAlreadyBeenApprovedByCurrentUser(User user) {
        List<User> friends = User.getCurrentUser().getUserData().getFriends();

        if (friends == null) {
            return false;
        }

        for (User friend : friends) {
            if (friend.equalsEntity(user)) {
                return true;
            }
        }
        return false;
    }

    private static void markButtonAlreadyApproved(Button button) {
        button.setText("Potwierdzono");
        button.setEnabled(false);
    }

    private static void resetButtonState(Button button) {
        button.setText("Potwierdź");
        button.setEnabled(true);
    }
}
