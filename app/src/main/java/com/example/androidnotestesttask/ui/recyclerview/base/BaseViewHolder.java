package com.example.androidnotestesttask.ui.recyclerview.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseViewHolder<TYPE> extends RecyclerView.ViewHolder implements BaseViewHolderInterface<TYPE> {

    private final boolean useButterKnifeBinder;
    private TYPE object;

    public BaseViewHolder(ViewGroup parent, @LayoutRes int layoutResId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
        this.useButterKnifeBinder = true;
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int layoutResId, boolean useButterKnifeBinder) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
        this.useButterKnifeBinder = useButterKnifeBinder;
    }

    @Override
    public void onBind(TYPE object) {
        this.object = object;
        if (useButterKnifeBinder) ButterKnife.bind(this, itemView);
    }

    public TYPE getBindObject() {
        return object;
    }

}