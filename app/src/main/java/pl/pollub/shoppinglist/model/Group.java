package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;
import com.parse.ParseRelation;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.pollub.shoppinglist.util.Icon;

import static pl.pollub.shoppinglist.util.Icon.Converter.*;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@ParseClassName(Group.CLASS_NAME)
public class Group extends NamedEntity {
    public static final String CLASS_NAME = "Group";

    public static final String KEY_ICON = "icon";

    public Icon getIcon() {
        String icon = getString(KEY_ICON);
        return stringToIcon(icon);
    }

    public void setIcon(Icon icon) {
        put(KEY_ICON, iconToString(icon));
    }
}
