package com.example.tastytrades.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tastytrades.Activities.ZoomRecipie;
import com.example.tastytrades.Model.BookRecipes;
import com.example.tastytrades.Model.Recipe;
import com.example.tastytrades.R;
import com.example.tastytrades.Utilities.ImageLoader;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecipieAdapter extends RecyclerView.Adapter<RecipieAdapter.RecipeViewHolder> {
    BookRecipes bookRecipes = new BookRecipes();
    public final String defaultIMG = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTg60O2tligU0k5gSNPKj7QYsuVB7KuVSIcOA&s";

    private Context context;

    public RecipieAdapter(Context context , BookRecipes bookRecipes) {
        this.context = context;
        this.bookRecipes = bookRecipes;

    }

    @NonNull
    @Override
    public RecipieAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontale_recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipieAdapter.RecipeViewHolder holder, int position) {

        Recipe recipe = bookRecipes.getAllRecipes().get(position);
        if(recipe.getPoster().equals(""))
            ImageLoader.getInstance().load(defaultIMG, holder.recipe_IMG_poster);
        else
            ImageLoader.getInstance().load(recipe.getPoster(), holder.recipe_IMG_poster);
        holder.recipe_LBL_name.setText(recipe.getName());
        holder.recipe_LBL_ingredients.setText(recipe.getIngredients());
        holder.recipe_LBL_instructions.setText(recipe.getInstructions());


        holder.item_Button_See.findViewById(R.id.item_Button_See).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                Intent intent = new Intent(context, ZoomRecipie.class);
                //je redirige vers une activity mais je ne suis pas ds une activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("recipe", new Gson().toJson(recipe));
                context.startActivity(intent);
            }
        });

    }

    public BookRecipes getBookRecipes() {
        return bookRecipes;
    }

    public void setBookRecipes(BookRecipes bookRecipes) {
        this.bookRecipes = bookRecipes;
    }

    @Override
    public int getItemCount() {
        return bookRecipes.getAllRecipes().size();
    }


    public void actualiseRVbySearch(ArrayList<Recipe> arr) {
        bookRecipes.actualiseRVbySearch(arr);
        notifyDataChange();
    }

    public void ashowAllRecipies() {
        bookRecipes.getAllRecipes().clear();
        bookRecipes.getAllRecipes().addAll(bookRecipes.getTempRecipies());
        notifyDataChange();
    }

    public void notifyDataChange(){
        this.notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView recipe_IMG_poster;
        private MaterialTextView recipe_LBL_name;
        private MaterialTextView recipe_LBL_ingredients;
        private MaterialTextView recipe_LBL_instructions;
        private ImageButton item_Button_See;
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            findViews();
        }

        private void findViews() {
            recipe_IMG_poster = itemView.findViewById(R.id.recipe_IMG_poster);
            recipe_LBL_name = itemView.findViewById(R.id.recipe_LBL_name);
            recipe_LBL_ingredients = itemView.findViewById(R.id.recipe_LBL_ingredients);
            recipe_LBL_instructions = itemView.findViewById(R.id.recipe_LBL_instructions);
            item_Button_See= itemView.findViewById(R.id.item_Button_See);
        }

    }

}

