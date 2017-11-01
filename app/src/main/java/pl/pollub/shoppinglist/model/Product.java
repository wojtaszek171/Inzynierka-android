package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.pollub.shoppinglist.util.Icon;
import pl.pollub.shoppinglist.util.Measure;

import static pl.pollub.shoppinglist.util.Icon.Converter.*;
import static pl.pollub.shoppinglist.util.Measure.Converter.*;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@ParseClassName(Product.CLASS_NAME)
public class Product extends NamedEntity {
    public static final String CLASS_NAME = "Product";

    public static final String KEY_CATEGORY_POINTER = "categoryId";
    public static final String KEY_MEASURE = "measure";
    public static final String KEY_PREDEFINED = "predefined";
    public static final String KEY_ICON = "icon";

    public Category getCategory() {
        return getEntity(KEY_CATEGORY_POINTER);
    }

    public Measure getMeasure() {
        String measure = getString(KEY_MEASURE);
        return stringToMeasure(measure);
    }

    public boolean isPredefined() {
        return getBoolean(KEY_PREDEFINED);
    }

    public Icon getIcon() {
        String icon = getString(KEY_ICON);
        return stringToIcon(icon);
    }

    public void setCategory(Category category) {
        put(KEY_CATEGORY_POINTER, category);
    }

    public void setMeasure(Measure measure) {
        put(KEY_MEASURE, measureToString(measure));
    }

    public void setPredefined(boolean predefined) {
        put(KEY_PREDEFINED, predefined);
    }

    public void setIcon(Icon icon) {
        put(KEY_ICON, iconToString(icon));
    }
}
