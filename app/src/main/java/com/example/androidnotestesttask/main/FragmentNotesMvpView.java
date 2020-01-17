package com.example.androidnotestesttask.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.androidnotestesttask.db.model.NoteModel;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FragmentNotesMvpView extends MvpView {
    void onLoadComplete(List<NoteModel> noteModels);

    @StateStrategyType(AddToEndStrategy.class)
    void onNoteAddComplete(NoteModel noteModel);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void onNoteClearComplete();
}
