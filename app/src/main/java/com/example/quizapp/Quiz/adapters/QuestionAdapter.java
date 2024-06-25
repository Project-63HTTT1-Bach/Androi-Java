package com.example.quizapp.Quiz.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Quiz.activities.CreateQuestionActivity;
import com.example.quizapp.Quiz.models.Question;
import com.example.quizapp.R;

import java.util.List;
import java.util.function.Consumer;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private List<Question> questionList;
    private Consumer<Integer> onDeleteClickListener;

    public QuestionAdapter(List<Question> questionList, Consumer<Integer> onDeleteClickListener) {
        this.questionList = questionList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_manager, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.textViewQuestion.setText(question.getQuestionText());

        holder.btnDeleteQuestion.setOnClickListener(v -> onDeleteClickListener.accept(position));

        holder.btnEditQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), CreateQuestionActivity.class);
            intent.putExtra("questionId", question.getQuestionId());
            intent.putExtra("quizId", question.getQuizId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public Question getQuestionAt(int position) {
        return questionList.get(position);
    }

    public void removeQuestionAt(int position) {
        questionList.remove(position);
        notifyItemRemoved(position);
    }

    public void addQuestion(Question question) {
        questionList.add(question);
        notifyItemInserted(questionList.size() - 1);
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion;
        ImageView btnDeleteQuestion;
        ImageView btnEditQuestion;

        QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.txt_question);
            btnDeleteQuestion = itemView.findViewById(R.id.btn_delete_question);
            btnEditQuestion = itemView.findViewById(R.id.btn_edit_question);
        }
    }
}
