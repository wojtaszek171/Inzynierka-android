package pl.pollub.shoppinglist.util.customproductlist;

/**
 * Created by jrwoj on 25.10.2017.
 */

public class CustomProductDataModel {


    private String name;
    private String category;
    private String description;
    private String measure;
    private Double pricePerUnit;

    public CustomProductDataModel(String name, String category, String description, String measure, Double pricePerUnit) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.measure = measure;
        this.pricePerUnit = pricePerUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
