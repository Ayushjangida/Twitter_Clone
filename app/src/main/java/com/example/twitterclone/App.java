package com.example.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("cWuhV9g8V6hlIE8GDgdHxA5IYt21ELqzfAFTigr5")
                // if defined
                .clientKey("hxCA5KkSB6YtGdNspfkXRxBKgUedH4P66pg3F1e9")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
