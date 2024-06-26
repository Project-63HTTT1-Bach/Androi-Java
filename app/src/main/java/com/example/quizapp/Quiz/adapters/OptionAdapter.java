package com.example.quizapp.Quiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Quiz.models.Answer;
import com.example.quizapp.R;

import java.util.List;
import java.util.function.Consumer;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {

    private final List<Answer> options;
    private final Consumer<Integer> onOptionSelected;
    private int selectedPosition = -1;
    private boolean isCorrect = false;

    public OptionAdapter(List<Answer> options, Consumer<Integer> onOptionSelected) {
        this.options = options;
        this.onOptionSelected = onOptionSelected;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        Answer answer = options.get(position);
        holder.optionText.setText(answer.getAnswerText());

        holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(
                selectedPosition == position ? (isCorrect ? R.color.Green : R.color.Red) : R.color.primaryColor
        ));

        holder.cardView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            selectedPosition = adapterPosition;
            isCorrect = options.get(adapterPosition).getIsCorrect() == 1;
            notifyDataSetChanged();
            onOptionSelected.accept(adapterPosition);
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public void setAnswerSelected(int position, boolean isCorrect) {
        this.selectedPosition = position;
        this.isCorrect = isCorrect;
        notifyDataSetChanged();
    }

    public static class OptionViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView optionText;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView2);
            optionText = itemView.findViewById(R.id.option_text);
        }
    }
}
