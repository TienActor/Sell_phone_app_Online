package com.example.mobile_app.User;

import com.example.mobile_app.Model.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartResponse {
    @SerializedName("products")
    private Map<String, Product> products;

    @SerializedName("message")
    private String message;

    public List<Product> getProducts() {
        return new ArrayList<>(products.values());
    }

    public String getMessage() {
        return message;
    }

    public boolean isCartEmpty() {
        return products == null || products.isEmpty();
    }
}

