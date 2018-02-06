package com.newinspections.pso.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Exd on 1/24/2018.
 */

public class ProductsModel {

    @SerializedName("Products")
    private List<Products> products = new ArrayList<Products>();

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public List<Products> getProducts() {
        return products;
    }

    public static class Products {
        @SerializedName("product_id")
        private String productId;
        @SerializedName("product_name")
        private String productsName;

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setProductsName(String productsName) {
            this.productsName = productsName;
        }

        public String getProductId() {
            return productId;
        }

        public String getProductsName() {
            return productsName;
        }
    }
}
