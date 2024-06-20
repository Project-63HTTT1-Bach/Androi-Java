package com.example.quizapp.Quiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.Quiz.models.Quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> implements Filterable {

    private List<Quiz> quizItems;
    private List<Quiz> quizItemsOld;
    private final Context context;

    public QuizAdapter(Context context, List<Quiz> quizItems) {
        this.context = context;
        this.quizItems = quizItems;
        this.quizItemsOld = quizItems;
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
        holder.quizName.setText(item.getQuizName());
        int imageResource = context.getResources().getIdentifier(item.getIconImage(), "drawable", context.getPackageName());
        holder.quizIcon.setImageResource(imageResource);
    }

    @Override
    public int getItemCount() {
        return quizItems.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    quizItems = quizItemsOld;
                } else {
                    List<Quiz> list = new ArrayList<>();
                    for (Quiz quiz : quizItemsOld) {
                        if (quiz.getQuizName().contains(strSearch.toLowerCase())) {
                            list.add(quiz);
                        }
                    }
                    quizItems = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = quizItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                quizItems = (List<Quiz>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {

        ImageView quizIcon, moreInfoIcon;
        TextView quizName;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizIcon = itemView.findViewById(R.id.quizIcon);
            quizName = itemView.findViewById(R.id.quizName);
            moreInfoIcon = itemView.findViewById(R.id.moreInfoIcon);
        }
    }
}
