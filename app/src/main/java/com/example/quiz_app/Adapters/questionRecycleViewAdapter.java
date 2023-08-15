package com.example.quiz_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz_app.Models.QuestionModel;
import com.example.quiz_app.R;

import java.util.ArrayList;

public class questionRecycleViewAdapter extends RecyclerView.Adapter<questionRecycleViewAdapter.ViewHolder> {
    Context context;
    ArrayList<QuestionModel> list;

    public questionRecycleViewAdapter(Context context, ArrayList<QuestionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public questionRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_questions,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull questionRecycleViewAdapter.ViewHolder holder, int position) {

        QuestionModel questionModel=list.get(position);
        holder.tv_question.setText(questionModel.getQuestion());

    }

    @Override
    public int getItemCount() {
        return list.size(); // Return the correct number of items in the list
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_question;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_question=itemView.findViewById(R.id.question);
        }
    }
}
