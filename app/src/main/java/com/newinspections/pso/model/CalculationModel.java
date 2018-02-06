package com.newinspections.pso.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobiweb on 30/9/16.
 */
public class CalculationModel {

    String title="",description="",product="",values="";

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getProduct() {
        return product;
    }

    public String getValues() {
        return values;
    }
}
