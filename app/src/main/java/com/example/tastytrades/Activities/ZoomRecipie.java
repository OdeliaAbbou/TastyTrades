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
    public final String defaultIMG = "https://firebasestorage.googleapis.com/v0/b/tastyrecipe-880ca.appspot.com/o/images%2Ffood.jpeg?alt=media&token=a50e9a48-e837-4af9-9818-bac0aa00692b";

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
                String imageUrl = recipe.getPoster();
                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = defaultIMG;
                }

                Glide.with(this)
                        .load(imageUrl)
                        .into(zoom_IMG_background);

                zoom_TXT_name.setText(recipe.getName());
                zoom_TXT_ingredients.setText(recipe.getIngredients());
                zoom_TXT_Instructions.setText(recipe.getInstructions());

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