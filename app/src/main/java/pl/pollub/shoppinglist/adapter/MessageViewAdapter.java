package pl.pollub.shoppinglist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import pl.pollub.shoppinglist.R;
import pl.pollub.shoppinglist.model.Message;
import pl.pollub.shoppinglist.model.User;

/**
 * @author Adrian
 * @since 2017-11-26
 */
public class MessageViewAdapter extends BaseRecyclerViewAdapter<Message> {
    private static final int VIEWTYPE_MESSAGE_SENT = 1;
    private static final int VIEWTYPE_MESSAGE_RECEIVED = 2;

    private final User currentUser = User.getCurrentUser();
    private int friendAvatarColor;

    public MessageViewAdapter(Context context) {
        super(context);
    }

    public MessageViewAdapter(Context context, OnViewHolderClick<Message> listener) {
        super(context, listener);
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

        Date currentTime = new Date();
        long differenceInMinutes = (currentTime.getTime() - message.getCreatedAt().getTime()) / 60_000;
        differenceInMinutes = differenceInMinutes < 0 ? 0 : differenceInMinutes;
        long differenceInHours = differenceInMinutes / 60;
        long differenceInDays = differenceInHours / 24;
        String time = null;

        if (differenceInMinutes < 60) {
            time = differenceInMinutes + " " + getContext().getString(R.string.minutes_ago);
        } else if (differenceInMinutes >= 60 && differenceInHours < 24) {
            SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");
            time = formatter.format(message.getCreatedAt());
        } else if (differenceInHours >= 24 && differenceInDays < 7) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, kk:mm");
            time = formatter.format(message.getCreatedAt());
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm");
            time = formatter.format(message.getCreatedAt());
        }

        timeText.setText(time);
        for (TextView extra : participantExtras) {
            String username = message.getAuthor().getUsername();

            switch (extra.getId()) {
                case R.id.r_friend_name_text:
                    extra.setText(username);
                    break;
                case R.id.r_profile_image:
                    char firstChar = username.toUpperCase().charAt(0);
                    extra.setText(Character.toString(firstChar));
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