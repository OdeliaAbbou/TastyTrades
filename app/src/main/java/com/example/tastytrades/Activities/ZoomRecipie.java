package com.example.tastytrades.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tastytrades.Model.Recipe;
import com.example.tastytrades.R;
import com.example.tastytrades.Utilities.ImageLoader;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ZoomRecipie extends AppCompatActivity {
    private TextView zoom_TXT_Title;
    private TextView zoom_TXT_name;
    private ShapeableImageView zoom_IMG_background;
    private TextView zoom_TXT_ingredientsTitle;
    private TextView zoom_TXT_ingredients;
    private TextView zoom_TXT_instructionTitle;
    private TextView zoom_TXT_Instructions;
    private Recipe recipe;
    public final String defaultIMG = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTg60O2tligU0k5gSNPKj7QYsuVB7KuVSIcOA&s";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_recipie);
        findViews();
        loadRecipie();
    }

    private void findViews() {
        zoom_TXT_Title = findViewById(R.id.zoom_TXT_Title);
        zoom_TXT_name = findViewById(R.id.zoom_TXT_name);
        zoom_IMG_background = findViewById(R.id.zoom_IMG_background);
        zoom_TXT_ingredientsTitle = findViewById(R.id.zoom_TXT_ingredientsTitle);
        zoom_TXT_ingredients = findViewById(R.id.zoom_TXT_ingredients);
        zoom_TXT_instructionTitle = findViewById(R.id.zoom_TXT_instructionTitle);
        zoom_TXT_Instructions = findViewById(R.id.zoom_TXT_Instructions);

    }

    private void loadRecipie() {
        String jsonRecipe = getIntent().getStringExtra("recipe");

        if (jsonRecipe != null) {
            try {
                recipe = new Gson().fromJson(jsonRecipe, Recipe.class);
                String nom = recipe.getName();
                String ingredients = recipe.getIngredients();
                String instructions = recipe.getInstructions();
                String URL = recipe.getPoster();
                if(URL.isEmpty())
                    URL = defaultIMG;

                zoom_TXT_name.setText(nom);
                zoom_TXT_ingredients.setText(ingredients);
                zoom_TXT_Instructions.setText(instructions);

                if (!isFinishing() && !isDestroyed()) {
                    Glide.with(this).load(URL).into(zoom_IMG_background);
                }
            } catch (JsonSyntaxException e) {
                Log.e("ZoomRecipie", "Error parsing JSON", e);
            }
        } else {
            changeActivity();
        }
    }


    private void changeActivity() {
        if (!isFinishing() && !isDestroyed()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}