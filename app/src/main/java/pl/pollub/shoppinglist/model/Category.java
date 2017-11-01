package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;

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
@ParseClassName(Category.CLASS_NAME)
public class Category extends NamedEntity {
    public static final String CLASS_NAME = "category";

    public static final String KEY_PARENT_POINTER = "parentId";
    public static final String KEY_ICON = "icon";

    public Category getParent() {
        return getEntity(KEY_PARENT_POINTER);
    }

    public Icon getIconPath() {
        String icon = getString(KEY_ICON);
        return stringToIcon(icon);
    }

    public void setParent(Category parent) {
        put(KEY_PARENT_POINTER, parent);
    }

    public void setIcon(Icon icon) {
        put(KEY_ICON, iconToString(icon));
    }
}
