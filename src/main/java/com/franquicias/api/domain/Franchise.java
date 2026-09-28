package com.franquicias.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Raíz del agregado. Las sucursales y sus productos se guardan embebidos
 * en el mismo documento, de modo que cada operación es atómica.
 */
@Document(collection = "franchises")
public class Franchise {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private List<Branch> branches = new ArrayList<>();

    public Franchise() {
    }

    public Franchise(String id, String name, List<Branch> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches != null ? branches : new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Branch> getBranches() { return branches; }
    public void setBranches(List<Branch> branches) { this.branches = branches; }
}
