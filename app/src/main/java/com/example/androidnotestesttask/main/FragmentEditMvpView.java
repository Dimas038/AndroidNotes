package com.example.androidnotestesttask.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.androidnotestesttask.db.model.NoteModel;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FragmentEditMvpView extends MvpView {
    void onNoteSaveComplete(NoteModel model);
}
