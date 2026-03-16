package com.nowhere;

import java.util.*;

public class Recipe {
    private UUID id;
    private String title;
    private String instructions;
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {
	ingredients = new ArrayList<>();
    }

    public Recipe(UUID id, String title, String instructions, List<Ingredient> ingredients) {
	this.id = id;
	this.title = title;
	this.instructions = instructions;
        this.ingredients = new ArrayList<>();
    }

    public UUID getId() {
	return id;
    }

    public String getTitle() {
	return title;
    }

    public String getInstructions() {
	return instructions;
    }

    public List<Ingredient> getIngredients() {
    	return ingredients;
    }

    public void setId(UUID id) {
	this.id = id;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public void setIngredients(List<Ingredient> ingredients) {
	this.ingredients = ingredients;
    }
}
