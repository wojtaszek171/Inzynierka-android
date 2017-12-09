package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;

import lombok.ToString;

/**
 * @author Adrian
 * @since 2017-12-01
 */
@ToString
@ParseClassName(Message.CLASS_NAME)
public class Message extends BaseEntity {
    public static final String CLASS_NAME = "Message";

    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CONTENT = "content";

    public User getAuthor() {
        return (User) getParseUser(KEY_AUTHOR);
    }

    public Message setAuthor(User receiver) {
        put(KEY_AUTHOR, receiver);
        return this;
    }

    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public Message setContent(String content) {
        put(KEY_CONTENT, content);
        return this;
    }
}
