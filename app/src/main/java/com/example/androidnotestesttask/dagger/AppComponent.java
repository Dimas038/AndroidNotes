package com.example.androidnotestesttask.dagger;

import com.example.androidnotestesttask.dagger.module.ApplicationModule;
import com.example.androidnotestesttask.dagger.module.NavigationModule;
import com.example.androidnotestesttask.main.FragmentEditMvpPresenter;
import com.example.androidnotestesttask.main.FragmentNotesMvpPresenter;
import com.example.androidnotestesttask.ui.FragmentEdit;
import com.example.androidnotestesttask.ui.FragmentNotes;
import com.example.androidnotestesttask.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NavigationModule.class
})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(FragmentNotes fragment);

    void inject(FragmentNotesMvpPresenter presenter);

    void inject(FragmentEdit fragment);

    void inject(FragmentEditMvpPresenter presenter);
}
