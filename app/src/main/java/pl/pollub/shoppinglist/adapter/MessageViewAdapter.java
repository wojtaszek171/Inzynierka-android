package pl.pollub.shoppinglist.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.Message;
import pl.pollub.shoppinglist.model.User;
import pl.pollub.shoppinglist.util.TimeUtils;

import static android.support.v4.view.ViewCompat.setBackgroundTintList;
import static pl.pollub.shoppinglist.util.MiscUtils.getRandomAvatarBackgroundColor;

/**
 * @author Adrian
 * @since 2017-11-26
 */
public class MessageViewAdapter extends BaseRecyclerViewAdapter<Message> {
    private static final int VIEWTYPE_MESSAGE_SENT = 1;
    private static final int VIEWTYPE_MESSAGE_RECEIVED = 2;

    private final User currentUser = User.getCurrentUser();
    private final ColorStateList friendAvatarColors;

    public MessageViewAdapter(Context context) {
        super(context);
        friendAvatarColors = ColorStateList.valueOf(getRandomAvatarBackgroundColor());
    }

    public MessageViewAdapter(Context context, OnViewHolderClick<Message> listener) {
        super(context, listener);
        friendAvatarColors = ColorStateList.valueOf(getRandomAvatarBackgroundColor());
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case VIEWTYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.item_message_sent, viewGroup, false);
                break;
            case VIEWTYPE_MESSAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_message_received, viewGroup, false);
                break;
            default:
                throw new IllegalArgumentException("viewType should be in (1, 2) range but was " + viewType);
        }

        return view;
    }

    @Override
    protected void bindView(Message item, int position, BaseRecyclerViewAdapter.ViewHolder viewHolder) {
        if (item == null || !item.isDataAvailable()) {
            throw new IllegalArgumentException("Message is null or not fetched");
        }

        TextView messageText, timeText;

        switch (viewHolder.getItemViewType()) {
            case VIEWTYPE_MESSAGE_SENT:
                messageText = (TextView) viewHolder.getView(R.id.s_message_body_text);
                timeText = (TextView) viewHolder.getView(R.id.s_message_time_text);

                updateRowView(item, messageText, timeText);
                break;
            case VIEWTYPE_MESSAGE_RECEIVED:
                messageText = (TextView) viewHolder.getView(R.id.r_message_body_text);
                timeText = (TextView) viewHolder.getView(R.id.r_message_time_text);
                TextView nameText = (TextView) viewHolder.getView(R.id.r_friend_name_text);
                TextView profileImage = (TextView) viewHolder.getView(R.id.r_profile_image);

                updateRowView(item, messageText, timeText, nameText, profileImage);
                break;
            default:
                throw new IllegalArgumentException("viewType should be in (1, 2) range but was "
                        + viewHolder.getItemViewType());
        }
    }

    private void updateRowView(Message message, TextView messageText, TextView timeText, TextView... participantExtras) {
        messageText.setText(message.getContent());
        timeText.setText(TimeUtils.getRelativeTimeString(getContext(), message.getCreatedAt()));

        for (TextView extra : participantExtras) {
            String username = message.getAuthor().getUsername();

            switch (extra.getId()) {
                case R.id.r_friend_name_text:
                    extra.setText(username);
                    break;
                case R.id.r_profile_image:
                    char firstChar = username.toUpperCase().charAt(0);
                    extra.setText(Character.toString(firstChar));
                    setBackgroundTintList(extra, friendAvatarColors);
                    break;
                default:
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);
        if (message == null || !message.isDataAvailable()) {
            throw new IllegalArgumentException("Message is null or not fetched");
        }

        if (currentUser.equalsEntity(message.getAuthor())) {
            return VIEWTYPE_MESSAGE_SENT;
        } else {
            return VIEWTYPE_MESSAGE_RECEIVED;
        }
    }
}