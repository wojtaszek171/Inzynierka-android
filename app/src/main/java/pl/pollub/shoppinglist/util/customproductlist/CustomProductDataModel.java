package pl.pollub.shoppinglist.util.customproductlist;

import java.util.concurrent.atomic.AtomicLong;

import lombok.Data;

/**
 * @author Jakub
 * @since 2017-10-25
 */
@Data
public class CustomProductDataModel {

    private static AtomicLong counter = new AtomicLong(1);

    private long localId;
    private String name;
    private String category;
    private String description;

    public CustomProductDataModel(String name, String category, String description) {
        this.localId = counter.getAndIncrement();
        this.name = name;
        this.category = category;
        this.description = description;
    }
}
