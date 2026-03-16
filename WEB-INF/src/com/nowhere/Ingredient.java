package com.nowhere;

import java.util.*;

public class Ingredient {
    private UUID id;
    private String quantity;
    private String name;

    public Ingredient() {}

    public Ingredient(UUID id, String name, String quantity) {
	this.id = id;
	this.name = name;
	this.quantity = quantity;
    }

    public UUID getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public String getQuantity() {
	return quantity;
    }

    public void setId(UUID id) {
	this.id = id;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setQuantity(String quantity) {
	this.quantity = quantity;
    }
}
