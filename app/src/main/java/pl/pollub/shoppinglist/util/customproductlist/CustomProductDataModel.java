package pl.pollub.shoppinglist.util.customproductlist;

import lombok.Data;

/**
 * @author Jakub
 * @since 2017-10-25
 */
@Data
public class CustomProductDataModel {

    private static long localIdCounter;

    private long localId;
    private String name;
    private String category;
    private String description;

    public CustomProductDataModel(String name, String category, String description) {
        this.localId = getIncrementedCounter();
        this.name = name;
        this.category = category;
        this.description = description;
    }

    private long getIncrementedCounter() {
        localIdCounter++;
        return localIdCounter;
    }
}
