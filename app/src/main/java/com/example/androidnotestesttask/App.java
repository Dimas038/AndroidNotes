package com.example.androidnotestesttask;

import android.app.Application;

import com.example.androidnotestesttask.dagger.AppComponent;
import com.example.androidnotestesttask.dagger.DaggerAppComponent;
import com.example.androidnotestesttask.dagger.module.ApplicationModule;

public class App extends Application {

    public static App INSTANCE;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .applicationModule(new ApplicationModule(this)).build();
        }
        return appComponent;
    }
}
