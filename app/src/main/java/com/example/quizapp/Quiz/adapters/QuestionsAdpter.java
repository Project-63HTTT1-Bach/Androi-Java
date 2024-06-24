package com.example.quizapp.Quiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.Quiz.models.Question;

import java.util.ArrayList;

public class QuestionsAdpter extends RecyclerView.Adapter<QuestionsAdpter.DataviewHolder> {

    Context context;
    ArrayList<Question> questions;

    public QuestionsAdpter (Context context,ArrayList<Question> list){
        this.context = context;
        this.questions = list;
    }
    @Override
    public DataviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question,parent,false);
        return new DataviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataviewHolder holder, int position) {
        final Question item = questions.get(position);
//        holder.textView.setText(item.getQuestion());
    }

    @Override
    public int getItemCount() {
        return  questions.size();
    }

    public class  DataviewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public DataviewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = (TextView)itemView.findViewById(R.id.txt_question);
        }
    }
}