package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;

import lombok.ToString;
import pl.pollub.shoppinglist.model.complextype.Icon;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@ToString
@ParseClassName(ShoppingList.CLASS_NAME)
public class ShoppingList extends DescribedEntity {
    public static final String CLASS_NAME = "ShoppingList";

    public static final String KEY_ICON = "icon";
    public static final String KEY_STATUS = "status";
    public static final String KEY_TEMPLATE = "isTemplate";
    // TODO: is it needed at all? doesnt parse resolve the problem with synchronization?
    //public static final String KEY_LAST_SYNCHRONIZED_AT = "lastSynchronizedAt";

    public Icon getIcon() {
        String icon = getString(KEY_ICON);
        return Icon.fromString(icon);
    }

    public String getStatus() {
        return getString(KEY_STATUS);
    }

    public void setIcon(Icon icon) {
        put(KEY_ICON, icon.toString());
    }

    public void setStatus(String status) {
        put(KEY_STATUS, status);
    }
}
