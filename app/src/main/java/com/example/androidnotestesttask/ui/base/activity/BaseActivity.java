package com.example.androidnotestesttask.ui.base.activity;

import com.arellomobile.mvp.MvpAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends MvpAppCompatActivity {

    private Unbinder unbinder;

    public void initButterKnife() {
        unbinder = ButterKnife.bind(this);
    }

    private void releaseButterKnife() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    protected void onDestroy() {
        releaseButterKnife();
        super.onDestroy();
    }
}
