package com.example.androidnotestesttask.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.androidnotestesttask.App;
import com.example.androidnotestesttask.db.helper.DatabaseHelper;
import com.example.androidnotestesttask.db.model.NoteModel;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class FragmentEditMvpPresenter extends MvpPresenter<FragmentEditMvpView> {

    private Disposable disposableAction;

    @Inject
    DatabaseHelper databaseHelper;

    public FragmentEditMvpPresenter() {
        App.INSTANCE.getAppComponent().inject(this);
    }

    public void createOrUpdate(NoteModel noteModel, boolean update) {
        disposeAction();
        disposableAction = Single.just(true)
                .subscribeOn(Schedulers.io())
                .map(ignored -> {
                    if (update) {
                        databaseHelper.updateNoteModel(noteModel);
                    } else {
                        databaseHelper.createNoteModel(noteModel);
                    }
                    return noteModel;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultNoteModel -> {
                    getViewState().onNoteSaveComplete(resultNoteModel);
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void disposeAction() {
        if (disposableAction != null) {
            if (!disposableAction.isDisposed()) {
                disposableAction.dispose();
            }
            disposableAction = null;
        }
    }
}
