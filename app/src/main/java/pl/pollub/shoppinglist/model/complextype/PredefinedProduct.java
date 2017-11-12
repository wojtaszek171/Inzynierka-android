package pl.pollub.shoppinglist.model.complextype;

import android.support.annotation.NonNull;
import android.util.Log;

import lombok.Getter;
import pl.pollub.shoppinglist.model.Product;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@Getter
public enum PredefinedProduct implements Product {
    CHIPS("chips", Category.SNACK, Measure.WEIGHT, Icon.DEFAULT),
    SHAMPOO("shampoo", Category.PERSONAL_CARE, Measure.PIECE, Icon.DEFAULT),
    AA_BATTERY("AA battery", Category.ELECTRONICS, Measure.PIECE, Icon.DEFAULT),

    ;

    public static final PredefinedProduct DEFAULT = CHIPS;

    private String name;
    private Category category;
    private Measure measure;
    private Icon icon;

    PredefinedProduct(String name, Category category, Measure measure, Icon icon) {
        this.name = name;
        this.category = category;
        this.measure = measure;
        this.icon = icon;
    }

    public static PredefinedProduct fromString(@NonNull String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            Log.d("ComplexType", PredefinedProduct.class.getSimpleName()
                    + ": " + value + " not found.");
            return DEFAULT;
        }
    }
}
