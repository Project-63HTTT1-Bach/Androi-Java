
package com.example.quizapp.HomeAndDiscover.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.HomeAndDiscover.models.Quiz;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private List<Quiz> quizItems;

    public QuizAdapter(List<Quiz> quizItems) {
        this.quizItems = quizItems;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz item = quizItems.get(position);
        holder.quizTitle.setText(item.getTitle());
        holder.quizIcon.setImageResource(item.getIconResourceId());
    }

    @Override
    public int getItemCount() {
        return quizItems.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {

        ImageView quizIcon, moreInfoIcon;
        TextView quizTitle;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizIcon = itemView.findViewById(R.id.quizIcon);
            quizTitle = itemView.findViewById(R.id.quizTitle);
            moreInfoIcon = itemView.findViewById(R.id.moreInfoIcon);
        }
    }
}
