package com.example.androidnotestesttask.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.androidnotestesttask.App;
import com.example.androidnotestesttask.BusAction;
import com.example.androidnotestesttask.R;
import com.example.androidnotestesttask.db.model.NoteModel;
import com.example.androidnotestesttask.main.FragmentEditMvpPresenter;
import com.example.androidnotestesttask.main.FragmentEditMvpView;
import com.example.androidnotestesttask.ui.base.fragment.BaseFragment;
import com.example.androidnotestesttask.ui.common.BackButtonListener;
import com.hwangjr.rxbus.RxBus;

import javax.inject.Inject;

import butterknife.BindView;
import ru.terrakok.cicerone.Router;

public class FragmentEdit extends BaseFragment implements FragmentEditMvpView, BackButtonListener {

    public static final String NOTE_MODEL = "NOTE_MODEL";

    @Inject
    Router router;
    @InjectPresenter
    FragmentEditMvpPresenter presenter;
    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextDefinition)
    EditText editTextDefinition;

    private MenuItem saveMenuItem;

    @ProvidePresenter
    public FragmentEditMvpPresenter createPresenter() {
        return new FragmentEditMvpPresenter();
    }

    public static FragmentEdit getNewInstance(NoteModel noteModel) {
        FragmentEdit fragment = new FragmentEdit();
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE_MODEL, noteModel);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(false);
        return fragment;
    }

    private NoteModel getNoteModel() {
        return getArguments().getParcelable(NOTE_MODEL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewInflated(View view, @Nullable Bundle savedInstanceState) {
        if (getNoteModel() != null) {
            editTextTitle.setText(getNoteModel().getTitle());
            editTextDefinition.setText(getNoteModel().getDefinition());
        }
    }

    @Override
    public int onInflateLayout() {
        return R.layout.fragment_edit;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        saveMenuItem = menu.add(0, 1, 0, R.string.save);
        saveMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (saveMenuItem != null && item.getItemId() == saveMenuItem.getItemId()) {
            if (validate()) {
                if (getNoteModel() == null) createOrUpdateModel(new NoteModel(), false);
                else createOrUpdateModel(getNoteModel(), true);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createOrUpdateModel(NoteModel noteModel, boolean update) {
        noteModel.setTitle(editTextTitle.getText().toString());
        noteModel.setDefinition(editTextDefinition.getText().toString());
        noteModel.setUpdated(System.currentTimeMillis());
        presenter.createOrUpdate(noteModel, update);
    }

    private boolean validate() {
        if (editTextTitle.getText() == null || editTextTitle.getText().length() == 0) {
            router.showSystemMessage(getString(R.string.error_title));
            return false;
        }

        if (editTextDefinition.getText() == null || editTextDefinition.getText().length() == 0) {
            router.showSystemMessage(getString(R.string.error_definition));
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        saveMenuItem = null;
    }

    @Override
    public boolean onBackPressed() {
        if (router != null) router.exit();
        return true;
    }

    @Override
    public void onNoteSaveComplete(NoteModel model) {
        RxBus.get().post(BusAction.NOTE_ADD_COMPLETE, model);
        router.exit();
    }
}
