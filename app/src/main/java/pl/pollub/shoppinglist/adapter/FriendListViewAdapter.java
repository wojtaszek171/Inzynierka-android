package pl.pollub.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.model.UserData;

/**
 * @author Adrian
 * @since 2017-11-19
 */
public class FriendListViewAdapter extends BaseRecyclerViewAdapter<User> {
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
    protected void bindView(User item, BaseRecyclerViewAdapter.ViewHolder viewHolder) {
        if (item == null) {
            Toast.makeText(getContext(), "FriendListAdapter: User is null", Toast.LENGTH_LONG).show();
            Log.w("FriendListAdapter", "User is null");
            return;
        }
        final TextView usernameLabel = (TextView) viewHolder.getView(R.id.username_list);
        final TextView lastActiveAtLabel = (TextView) viewHolder.getView(R.id.lastActiveAt_list);
        final AppCompatImageButton messageButton = (AppCompatImageButton) viewHolder.getView(R.id.actionMessageButton_list);
        final AppCompatImageButton deleteButton = (AppCompatImageButton) viewHolder.getView(R.id.actionDeleteButton_list);

        item.fetchIfNeededInBackground((updatedItem, exception) -> {
            if (exception == null) {
                User updatedUser = (User) updatedItem;

                usernameLabel.setText(updatedUser.getUsername());

                Date lastActiveAtTime = updatedUser.getLastActiveAt();
                if (lastActiveAtTime == null) {
                    lastActiveAtLabel.setText("never");
                } else {
                    lastActiveAtLabel.setText(DateUtils.getRelativeTimeSpanString(
                            getContext(),
                            lastActiveAtTime.getTime()
                    ));
                }
            } else {
                Toast.makeText(getContext(), "FriendListAdapter " + exception.getMessage(), Toast.LENGTH_LONG).show();
                Log.w("FriendListAdapter", exception.getMessage());
            }
        });

        //final UserData itemData = item.getUserData();

        messageButton.setOnClickListener(view -> {
            Toast.makeText(getContext(), "messageButton: nie zaimplementowano", Toast.LENGTH_SHORT).show();
        });

        deleteButton.setOnClickListener(view -> {
            Toast.makeText(getContext(), "deleteButton: nie zaimplementowano", Toast.LENGTH_SHORT).show();
        });
    }
}
