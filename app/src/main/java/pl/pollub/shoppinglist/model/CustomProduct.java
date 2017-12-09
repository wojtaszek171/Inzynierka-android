package pl.pollub.shoppinglist.model;

import com.parse.ParseClassName;

import lombok.ToString;
import pl.pollub.shoppinglist.model.complextype.Category;
import pl.pollub.shoppinglist.model.complextype.Icon;
import pl.pollub.shoppinglist.model.complextype.Measure;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@ToString
@ParseClassName(CustomProduct.CLASS_NAME)
public class CustomProduct extends NamedEntity implements Product {
    public static final String CLASS_NAME = "CustomProduct";

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_MEASURE = "measure";
    public static final String KEY_ICON = "icon";

    public Category getCategory() {
        String category = getString(KEY_CATEGORY);
        return Category.fromString(category);
    }

    public Measure getMeasure() {
        String measure = getString(KEY_MEASURE);
        return Measure.fromString(measure);
    }

    public Icon getIcon() {
        String icon = getString(KEY_ICON);
        return Icon.fromString(icon);
    }

    public void setCategory(Category category) {
        put(KEY_CATEGORY, category.toString());
    }

    public void setMeasure(Measure measure) {
        put(KEY_MEASURE, measure.toString());
    }

    public void setIcon(Icon icon) {
        put(KEY_ICON, icon.toString());
    }
}
