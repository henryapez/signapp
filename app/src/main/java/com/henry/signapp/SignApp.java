package com.henry.signapp;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Henry on 11/20/2016.
 */
public class SignApp extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
