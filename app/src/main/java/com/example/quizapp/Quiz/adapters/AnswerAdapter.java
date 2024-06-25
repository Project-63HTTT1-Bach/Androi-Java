package com.example.quizapp.Quiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Quiz.models.Answer;
import com.example.quizapp.R;

import java.util.List;
import java.util.function.Consumer;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    private List<Answer> answerList;
    private Consumer<Integer> onDeleteClickListener;
    private Consumer<Integer> onCorrectClickListener;

    public AnswerAdapter(List<Answer> answerList, Consumer<Integer> onDeleteClickListener, Consumer<Integer> onCorrectClickListener) {
        this.answerList = answerList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onCorrectClickListener = onCorrectClickListener;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer_manager, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answerList.get(position);
        holder.textViewAnswer.setText(answer.getAnswerText());
        holder.radioCorrect.setChecked(answer.getIsCorrect() == 1);

        holder.btnDeleteAnswer.setOnClickListener(v -> onDeleteClickListener.accept(position));
        holder.radioCorrect.setOnClickListener(v -> onCorrectClickListener.accept(position));
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public Answer getAnswerAt(int position) {
        return answerList.get(position);
    }

    public void removeAnswerAt(int position) {
        answerList.remove(position);
        notifyItemRemoved(position);
    }

    public void addAnswer(Answer answer) {
        answerList.add(answer);
        notifyItemInserted(answerList.size() - 1);
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAnswer;
        ImageView btnDeleteAnswer;
        RadioButton radioCorrect;

        AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAnswer = itemView.findViewById(R.id.txt_answer);
            btnDeleteAnswer = itemView.findViewById(R.id.btn_delete_answer);
            radioCorrect = itemView.findViewById(R.id.radio_correct);
        }
    }
}
