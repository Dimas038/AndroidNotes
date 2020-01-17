package com.example.androidnotestesttask.ui.recyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidnotestesttask.R;
import com.example.androidnotestesttask.db.model.NoteModel;
import com.example.androidnotestesttask.ui.recyclerview.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoteModelsAdapter extends RecyclerView.Adapter<NoteModelsAdapter.NoteViewHolder> {

    private final List<NoteModel> models = new ArrayList();

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new NoteViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position) {
        noteViewHolder.onBind(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setModels(List<NoteModel> models) {
        setModels(models, true);
    }

    public void setModels(List<NoteModel> models, boolean notify) {
        this.models.clear();
        if (models != null)
            this.models.addAll(models);
        if (notify)
            notifyDataSetChanged();
    }

    public void addModel(int position, NoteModel model) {
        if (model != null) {
            this.models.add(position, model);
            notifyItemInserted(position);
        }
    }

    private OnClickListener listener;

    public NoteModelsAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    public void updateOrAddModel(NoteModel noteModel) {
        for (int i = 0; i < models.size(); i++) {
            NoteModel model = models.get(i);
            if (model.getId() == noteModel.getId()) {
                models.set(i, noteModel);
                try {
                    notifyItemChanged(i);
                } catch (Exception ignored) {
                }
                return;
            }
        }
        addModel(0, noteModel);
    }

    protected class NoteViewHolder extends BaseViewHolder<NoteModel> {

        @BindView(R.id.textViewTitle)
        TextView textViewTitle;
        @BindView(R.id.textViewDate)
        TextView textViewDate;
        @BindView(R.id.textViewDefinition)
        TextView textViewDefinition;

        public NoteViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_note, true);
        }

        @Override
        public void onBind(NoteModel model) {
            super.onBind(model);
            textViewTitle.setText(model.getTitle());
            textViewDate.setText(model.getDateAndTime());
            textViewDefinition.setText(model.getDefinition());
        }

        @OnClick(R.id.clickableView)
        void clickableView() {
            if (listener != null) listener.onClick(getBindObject());
        }
    }

    public interface OnClickListener {
        void onClick(NoteModel noteModel);
    }
}
