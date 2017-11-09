package pl.pollub.shoppinglist.model.complextype;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Adrian
 * @since 2017-11-08
 */
@Getter
public enum Category {
    // top level categories
    BAKERY(null, Icon.DEFAULT), // pieczywo
    DRY_GOODS(null, Icon.DEFAULT), // ryż, mąka, cukier itd.
    BEVERAGE(null, Icon.DEFAULT), // napoje, drinki
    ALCOHOLS(null, Icon.DEFAULT), // alkohole
    PRESERVE(null, Icon.DEFAULT), // konserwy itd.
    DAIRY(null, Icon.DEFAULT), // nabiał
    MEAT(null, Icon.DEFAULT), // mieso, kielbachy, itd.
    FRESH_PRODUCE(null, Icon.DEFAULT), //swieze warzywa i owoce
    FROZEN_FOOD(null, Icon.DEFAULT), // mrozonki
    NEWSPAPER(null, Icon.DEFAULT), // gazety itd.
    PERSONAL_CARE(null, Icon.DEFAULT), // szampony, mydła itd.
    COSMETIC(null, Icon.DEFAULT), // kosmetyki
    SNACK(null, Icon.DEFAULT), // przekąski
    FISH_OR_SEAFOOD(null, Icon.DEFAULT), // ryby, owoce morza
    PHARMACY(null, Icon.DEFAULT), // leki
    CLOTH(null, Icon.DEFAULT), // ubrania
    BABY_PRODUCT(null, Icon.DEFAULT), // produkty dla dzieci, kaszki, pampersy i inne takie
    ELECTRONICS(null, Icon.DEFAULT), // elektronika
    OTHER(null, Icon.DEFAULT), // inne

    // 1st subcategory level
    BREAD(BAKERY, Icon.DEFAULT),
    BAGUETTE(BAKERY, Icon.DEFAULT),

    RICE(DRY_GOODS, Icon.DEFAULT),
    SUGAR(DRY_GOODS, Icon.DEFAULT),

    JUICE(BEVERAGE, Icon.DEFAULT),
    SODA(BEVERAGE, Icon.DEFAULT),

    SAUCE(PRESERVE, Icon.DEFAULT),
    JAM(PRESERVE, Icon.DEFAULT),

    ;
    private final Category parent;
    private final Icon icon;

    Category(Category parent, Icon icon) {
        this.parent = parent;
        this.icon = icon;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Converter {
        public static Category stringToCategory(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (Exception e) {
                return OTHER;
            }
        }

        public static String categoryToString(Category category) {
            return category == null ? null : category.name();
        }
    }
}
