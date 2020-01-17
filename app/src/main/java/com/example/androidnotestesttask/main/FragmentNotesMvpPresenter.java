package com.example.androidnotestesttask.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.androidnotestesttask.App;
import com.example.androidnotestesttask.BusAction;
import com.example.androidnotestesttask.db.helper.DatabaseHelper;
import com.example.androidnotestesttask.db.model.NoteModel;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class FragmentNotesMvpPresenter extends MvpPresenter<FragmentNotesMvpView> {

    private Disposable disposableAction;

    @Inject
    DatabaseHelper databaseHelper;

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.NOTE_ADD_COMPLETE)
            }
    )
    public void onNoteAddComplete(NoteModel noteModel) {
        getViewState().onNoteAddComplete(noteModel);
    }

    public FragmentNotesMvpPresenter() {
        App.INSTANCE.getAppComponent().inject(this);
        RxBus.get().register(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        load();
    }

    public void load() {
        disposeAction();
        disposableAction = Single.just(true)
                .subscribeOn(Schedulers.io())
                .map(ignored -> {
                    List<NoteModel> noteModels = databaseHelper.getAllNoteModel(false);
                    return noteModels;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(noteModels -> {
                    getViewState().onLoadComplete(noteModels);
                });
    }

    public void clear() {
        disposeAction();
        disposableAction = Observable.just(true)
                .subscribeOn(Schedulers.io())
                .map(ignored -> {
                    databaseHelper.clearAllNoteModel();
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                    getViewState().onNoteClearComplete();
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            RxBus.get().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
