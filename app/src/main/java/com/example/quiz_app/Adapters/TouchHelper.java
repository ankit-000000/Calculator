package com.example.quiz_app.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz_app.Adapters.TopicRecycleViewAdapter;

public class TouchHlper extends ItemTouchHelper.SimpleCallback {

    private TopicRecycleViewAdapter adapter;

    public TouchHelper(TopicRecycleViewAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            int position = viewHolder.getAdapterPosition();
            adapter.deleteTopic(position);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
}
