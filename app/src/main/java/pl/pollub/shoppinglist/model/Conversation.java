package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.ToString;

/**
 * @author Adrian
 * @since 2017-12-03
 */
@ToString
@ParseClassName(Conversation.CLASS_NAME)
public class Conversation extends BaseEntity {
    public static final String CLASS_NAME = "Conversation";

    public static final String KEY_MESSAGES = "messages";
    public static final String KEY_PARTICIPANTS = "participants";

    public List<Message> getMessages() {
        return getList(KEY_MESSAGES);
    }

    public void setMessages(List<Message> messages) {
        put(KEY_MESSAGES, messages);
    }

    public boolean addUniqueMessage(Message message) {
        addUnique(KEY_MESSAGES, message);
        return true;
    }

    public boolean addUniqueMessages(Collection<Message> messages) {
        addAllUnique(KEY_MESSAGES, messages);
        return true;
    }

    public boolean removeMessage(Message message) {
        removeAll(KEY_MESSAGES, Collections.singletonList(message));
        return true;
    }

    public boolean removeMessages(Collection<Message> messages) {
        removeAll(KEY_MESSAGES, messages);
        return true;
    }

    public List<User> getParticipants() {
        return getList(KEY_PARTICIPANTS);
    }

    public void setParticipants(List<User> participants) {
        put(KEY_PARTICIPANTS, participants);
    }

    public boolean addUniqueParticipant(User participant) {
        addUnique(KEY_PARTICIPANTS, participant);
        return true;
    }

    public boolean addUniqueParticipants(Collection<User> participants) {
        addAllUnique(KEY_PARTICIPANTS, participants);
        return true;
    }

    public boolean removeParticipant(User participant) {
        removeAll(KEY_PARTICIPANTS, Collections.singletonList(participant));
        return true;
    }

    public boolean removeParticipants(Collection<User> participant) {
        removeAll(KEY_PARTICIPANTS, participant);
        return true;
    }
}
