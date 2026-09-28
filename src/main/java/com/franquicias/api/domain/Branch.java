package com.franquicias.api.domain;

import java.util.ArrayList;
import java.util.List;

public class Branch {

    private String id;
    private String name;
    private List<Product> products = new ArrayList<>();

    public Branch() {
    }

    public Branch(String id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products != null ? products : new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
