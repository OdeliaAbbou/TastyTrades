package com.example.tastytrades.Model;

import java.util.ArrayList;

public class Recipe {
    private String name;
    private String ingredients;
    private String instructions;
    private String poster = "";


    public Recipe() {
    }

    public Recipe(String name, String ingredients, String instructions,String poster) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.poster = poster;


    }

    public String getName() {
        return name;
    }


    public String getInstructions() {
        return instructions;
    }

    public String getPoster() {
        return poster;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }


}
