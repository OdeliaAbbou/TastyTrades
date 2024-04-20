package com.example.tastytrades.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastytrades.Adapters.RecipieAdapter;
import com.example.tastytrades.Model.BookRecipes;
import com.example.tastytrades.Model.Recipe;
import com.example.tastytrades.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

public class AddRecipeActivity extends AppCompatActivity {
    private TextView AddRecipe_TXT_Title;
    private TextView AddRecipe_TXT_miniTitle;
    private TextInputEditText addRecipe_Input_name;
    private TextInputEditText addRecipe_Input_Ingredients;
    private TextInputEditText addRecipe_Input_Instructions;
    private TextInputEditText addRecipe_Input_URL;
    private Button AddRecipe_Button_AddRecipeB;
    private Recipe recipe;
    private RecipieAdapter recipieAdapter;
    private FirebaseDatabase db;
    private DatabaseReference bookRef;
    private BookRecipes bookRecipes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        findViews();
        initViews();
        readData();
        AddRecipe_Button_AddRecipeB.setOnClickListener(view -> addRecipe());

    }
    private void readData() {
        // Read once from the database
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookRecipes.getAllRecipes().clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        bookRecipes.addRecipie(recipe,recipieAdapter);
                    } else {
                        Log.d("readData", "Failed to parse a recipe from Firebase");
                    }                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("readData", "Failed to read value.", error.toException());
            }
        });
    }

    private void initViews() {
        bookRecipes = new BookRecipes();
        recipieAdapter = new RecipieAdapter(getApplicationContext(), bookRecipes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        db = FirebaseDatabase.getInstance();
        bookRef = db.getReference("TastyRecipeBook");

    }

    private void changeActivity() {
        if (!isFinishing() && !isDestroyed()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void addRecipe() {

        String name =addRecipe_Input_name.getText().toString();;
        String ingredients = addRecipe_Input_Ingredients.getText().toString();
        String instructions = addRecipe_Input_Instructions.getText().toString();
        String URL = addRecipe_Input_URL.getText().toString();
        recipe = new Recipe(name,ingredients, instructions,URL);
        addRecipeToFirebase(recipe);
        toastAndVibrate("Recipie added successfully!");
        changeActivity();

    }
    public void addRecipeToFirebase(Recipe recipe){
        // Getting a unique key for each new recipe
       String key = recipe.getName();

        bookRef.child(key).setValue(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("addRecipeToFirebase", "Recipe successfully added to Firebase.");
                bookRecipes.addRecipie(recipe,recipieAdapter);
            }
        }).addOnFailureListener(e -> Log.d("addRecipeToFirebase", "Failed to add recipe to Firebase.", e));
    }

    private void findViews() {
        AddRecipe_TXT_Title = findViewById(R.id.AddRecipe_TXT_Title);
        AddRecipe_TXT_miniTitle = findViewById(R.id.AddRecipe_TXT_miniTitle);
        addRecipe_Input_name= findViewById(R.id.addRecipe_Input_name);
        addRecipe_Input_Ingredients= findViewById(R.id.addRecipe_Input_Ingredients);
        addRecipe_Input_Instructions= findViewById(R.id.addRecipe_Input_Instructions);
        addRecipe_Input_URL= findViewById(R.id.addRecipe_Input_URL);
        AddRecipe_Button_AddRecipeB= findViewById(R.id.AddRecipe_Button_AddRecipeB);

    }
    private void toastAndVibrate(String text) {
        vibrate();
        toast(text);
    }
    private void toast(String text) {
        Toast.makeText(this,text, Toast.LENGTH_LONG).show();
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
}