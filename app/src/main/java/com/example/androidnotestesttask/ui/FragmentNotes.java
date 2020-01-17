package com.example.androidnotestesttask.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.androidnotestesttask.App;
import com.example.androidnotestesttask.R;
import com.example.androidnotestesttask.Screens;
import com.example.androidnotestesttask.db.model.NoteModel;
import com.example.androidnotestesttask.main.FragmentNotesMvpPresenter;
import com.example.androidnotestesttask.main.FragmentNotesMvpView;
import com.example.androidnotestesttask.ui.base.fragment.BaseFragment;
import com.example.androidnotestesttask.ui.common.BackButtonListener;
import com.example.androidnotestesttask.ui.recyclerview.adapter.NoteModelsAdapter;
import com.example.androidnotestesttask.utils.SimpleDivider;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.terrakok.cicerone.Router;

public class FragmentNotes extends BaseFragment implements FragmentNotesMvpView, BackButtonListener {

    @Inject
    Router router;
    @InjectPresenter
    FragmentNotesMvpPresenter presenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private NoteModelsAdapter adapter;
    private MenuItem addMenuItem;
    private MenuItem clearMenuItem;

    @ProvidePresenter
    FragmentNotesMvpPresenter createPresenter() {
        return new FragmentNotesMvpPresenter();
    }

    public static FragmentNotes getNewInstance() {
        FragmentNotes fragment = new FragmentNotes();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.setRetainInstance(false);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewInflated(View view, @Nullable Bundle savedInstanceState) {
        initRecyclerView();
    }

    @Override
    public int onInflateLayout() {
        return R.layout.fragment_notes;
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SimpleDivider(getContext()));

        adapter = new NoteModelsAdapter(new NoteModelsAdapter.OnClickListener() {
            @Override
            public void onClick(NoteModel noteModel) {
                router.navigateTo(Screens.FRAGMENT_EDIT, noteModel);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        clearMenuItem = menu.add(0, 9, 0, R.string.clear);
        clearMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        addMenuItem = menu.add(0, 99, 0, R.string.create);
        addMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (addMenuItem != null && item.getItemId() == addMenuItem.getItemId()) {
            router.navigateTo(Screens.FRAGMENT_EDIT);
            return true;
        } else if (clearMenuItem != null && item.getItemId() == clearMenuItem.getItemId()) {
            presenter.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        addMenuItem = null;
        clearMenuItem = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
    }

    @Override
    public boolean onBackPressed() {
        if (router != null) router.exit();
        return true;
    }

    @Override
    public void onLoadComplete(List<NoteModel> noteModels) {
        if (adapter != null) adapter.setModels(noteModels);
    }

    @Override
    public void onNoteAddComplete(NoteModel noteModel) {
        if (adapter != null) adapter.updateOrAddModel(noteModel);
    }

    @Override
    public void onNoteClearComplete() {
        if (adapter != null) adapter.setModels(null);
    }
}
