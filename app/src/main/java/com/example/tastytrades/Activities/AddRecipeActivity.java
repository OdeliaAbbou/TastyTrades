package com.example.tastytrades.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {
    private TextView AddRecipe_TXT_Title;
    private TextView AddRecipe_TXT_miniTitle;
    private TextInputEditText addRecipe_Input_name;
    private TextInputEditText addRecipe_Input_Ingredients;
    private TextInputEditText addRecipe_Input_Instructions;
    private Button AddRecipe_Button_AddRecipeB;
    private Button AddRecipe_Button_AddImage;
    private ShapeableImageView addRecipe_IMG_img;
    private Recipe recipe;
    private RecipieAdapter recipieAdapter;
    private FirebaseDatabase db;
    private DatabaseReference bookRef;
    private BookRecipes bookRecipes;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String imageFromDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        findViews();
        initViews();
        readData();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        AddRecipe_Button_AddRecipeB.setOnClickListener(view -> addRecipe());
        AddRecipe_Button_AddImage.setOnClickListener(view -> pickImage());


    }
    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            addRecipe_IMG_img.setImageURI(selectedImageUri);
            uploadImage(selectedImageUri);
        }
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
    if (recipe == null) {
        if (addRecipe_Input_name.getText().toString().isEmpty() || addRecipe_Input_Ingredients.getText().toString().isEmpty() || addRecipe_Input_Instructions.getText().toString().isEmpty())
            toastAndVibrate("Data can't be empty.");
        else {
            if(bookRecipes.isExistRecipeByName(addRecipe_Input_name.getText().toString()))
                toastAndVibrate("Recipe Name already used.");
            else {

                String name = addRecipe_Input_name.getText().toString();
                String ingredients = addRecipe_Input_Ingredients.getText().toString();
                String instructions = addRecipe_Input_Instructions.getText().toString();

                recipe = new Recipe(name, ingredients, instructions, imageFromDB);


                addRecipeToFirebase(recipe);
                toastAndVibrate("Recipe added successfully!");
                changeActivity();
            }
        }
    }
}



    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        imageFromDB = imageUrl;
                        Log.d("UploadImage", "Image URL: " + imageUrl);
                    }))
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Failed to upload image to Firebase", e);
                        Toast.makeText(getApplicationContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("UploadImage", "File path is null");
        }
    }


    public void addRecipeToFirebase(Recipe recipe){
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
        AddRecipe_Button_AddRecipeB= findViewById(R.id.AddRecipe_Button_AddRecipeB);
        AddRecipe_Button_AddImage = findViewById(R.id.AddRecipe_Button_AddImage);
        addRecipe_IMG_img = findViewById(R.id.addRecipe_IMG_img);

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