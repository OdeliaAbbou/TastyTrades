package com.example.tastytrades;

import android.app.Application;

import com.example.tastytrades.Utilities.ImageLoader;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.initImageLoader(this);
    }
}
