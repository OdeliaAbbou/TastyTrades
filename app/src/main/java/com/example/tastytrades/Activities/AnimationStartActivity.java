package com.example.tastytrades.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.tastytrades.R;
import com.google.android.material.imageview.ShapeableImageView;

public class AnimationStartActivity extends AppCompatActivity {
    private ShapeableImageView animationStart_IMG_background;
    private ShapeableImageView Car_IMG_Logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_start);

        findViews();
        startAnimation(Car_IMG_Logo);
    }

    private void findViews() {
        animationStart_IMG_background = findViewById(R.id.animationStart_IMG_background);
        Car_IMG_Logo = findViewById(R.id.Car_IMG_Logo);
    }
    private void startAnimation(View view){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels; //screen height

        // Start state:
        view.setY(-height / 2.0f);
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view.setAlpha(0.0f);

        view.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .translationY(0)
                .setDuration(4000) //in miliseconds
                .setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(@NonNull Animator animation) {
                                TransactToMainActivity();
                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animation) {

                            }
                        }
                );

    }
    //redirection vers la page du jeux
    private void TransactToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}