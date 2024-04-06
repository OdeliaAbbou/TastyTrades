package com.example.tastytrades.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.tastytrades.Activities.AddRecipeActivity;
import com.example.tastytrades.Adapters.RecipieAdapter;
import com.example.tastytrades.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class BookRecipes {
    private ArrayList<Recipe> allRecipies = new ArrayList<>();
    private ArrayList<Recipe> tempRecipies = new ArrayList<>();


    public BookRecipes(){
    }
    public ArrayList<Recipe> getAllRecipes() {
        return allRecipies;

    }

    public BookRecipes setAllRecipies(ArrayList<Recipe> allRecipies) {
        this.allRecipies = allRecipies;
        return this;
    }

    public ArrayList<Recipe> getTempRecipies() {
        return tempRecipies;
    }

public Recipe findRecipe(String name){
    for (int i = 0; i < allRecipies.size(); i++) {
        if(allRecipies.get(i).getName().toLowerCase().equals(name.toLowerCase()))
            return allRecipies.get(i);
    }
    return null;
}
    public void actualiseRVbySearch(ArrayList<Recipe> arr) {
        tempRecipies.addAll(allRecipies);
        allRecipies.clear();
        allRecipies.addAll(arr);
    }
    public void addRecipie(Recipe recipe, RecipieAdapter recipieAdapter){
        allRecipies.add(recipe);
        recipieAdapter.notifyDataChange();
    }



}
