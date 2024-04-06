package com.example.tastytrades.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tastytrades.Adapters.RecipieAdapter;
import com.example.tastytrades.Model.BookRecipes;
import com.example.tastytrades.Model.Recipe;
import com.example.tastytrades.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference bookRef;
    private RecipieAdapter recipieAdapter;
    public FirebaseDatabase db;
    private RecyclerView main_RV_recipes;
    private TextView main_TXT_Title;
    private TextInputEditText main_Input_recipeNameInput;
    private Button main_Button_SearchRecipe;
    private Button  main_Button_allList;
    private Button main_Button_AddRecipie;
    private BookRecipes bookRecipes ;
    private ImageButton item_Button_See;

    //UUID id = UUID.randomUUID();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();

        main_Button_SearchRecipe.setOnClickListener(view -> searchRecipe());
        main_Button_allList.setOnClickListener(view -> showAllList());
        main_Button_AddRecipie.setOnClickListener(view -> changeActivity());


    }
    public boolean isExistRecipe(Recipe recipe){
    boolean exists = false;
        for (Recipe r : bookRecipes.getAllRecipes()) {
        if (r.getName().equals(recipe.getName())) {
            exists = true;
            break;
        }
    }
        return exists;
    }

    private void changeActivity() {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
        finish();
    }

    private void initViews() {

        bookRecipes = new BookRecipes();
        recipieAdapter = new RecipieAdapter(getApplicationContext(), bookRecipes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_RV_recipes.setLayoutManager(linearLayoutManager);
        main_RV_recipes.setAdapter(recipieAdapter);

        db = FirebaseDatabase.getInstance();
        bookRef = db.getReference("TastyRecipeBook");
        //bookRef.setValue(bookRecipes.getAllRecipes());

        readData();


    }
    private void readData() {
        // Read once from the database
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookRecipes.getAllRecipes().clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if (recipe != null ) {
                        if(!isExistRecipe(recipe))
                            bookRecipes.addRecipie(recipe,recipieAdapter);
                    } else {
                        Log.d("readData", "Failed to parse a recipe from Firebase");
                    }                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ccc", "Failed to read value.", error.toException());
            }
        });
    }


    private void findViews() {
        main_RV_recipes = findViewById(R.id.main_RV_recipes);
        main_TXT_Title = findViewById(R.id.main_TXT_Title);
        main_Input_recipeNameInput= findViewById(R.id.main_Input_recipeNameInput);
        main_Button_SearchRecipe= findViewById(R.id.main_Button_SearchRecipe);
        main_Button_allList= findViewById(R.id.main_Button_allList);
        main_Button_AddRecipie= findViewById(R.id.main_Button_AddRecipie);
        item_Button_See = findViewById(R.id.item_Button_See);
    }

    private void searchRecipe() {
        String name = main_Input_recipeNameInput.getText().toString();
        if (!name.isEmpty()){
            Recipe recipe = bookRecipes.findRecipe(name);

            if(recipe !=null){
                ArrayList<Recipe> listOfResearch = new ArrayList<>();
                listOfResearch.add(recipe);
                recipieAdapter.actualiseRVbySearch(listOfResearch);
                main_Button_allList.setVisibility(View.VISIBLE);
                main_Button_AddRecipie.setVisibility(View.INVISIBLE);
            }
            else
                toastAndVibrate("Did not find this recipe");
            main_Input_recipeNameInput.getText().clear();

        }
        else{
            toastAndVibrate("Enter a recipe Name");

        }
    }

    private void showAllList() {
        recipieAdapter.ashowAllRecipies();
        main_Button_allList.setVisibility(View.INVISIBLE);
        main_Button_AddRecipie.setVisibility(View.VISIBLE);

    }
    private void toastAndVibrate(String text) {
        vibrate();
        toast(text);
    }
    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
    private void toast(String text) {
        Toast.makeText(this,text, Toast.LENGTH_LONG).show();
    }

}