package com.example.quizapp.livequiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.livequiz.model.LiveQuiz;

import java.util.List;

public class LiveQuizAdapter extends RecyclerView.Adapter<LiveQuizAdapter.LiveQuizViewHolder> {
    private List<LiveQuiz> liveQuizList;

    public static class LiveQuizViewHolder extends RecyclerView.ViewHolder {
        public ImageView quizImageView;
        public TextView quizTitleTextView;
        public ImageView moreInfoImageView;

        public LiveQuizViewHolder(View itemView) {
            super(itemView);
            quizImageView = itemView.findViewById(R.id.quizImage);
            quizTitleTextView = itemView.findViewById(R.id.quizTitle);
            moreInfoImageView = itemView.findViewById(R.id.moreInfoImage);
        }
    }

    public LiveQuizAdapter(List<LiveQuiz> liveQuizList) {
        this.liveQuizList = liveQuizList;
    }

    @Override
    public LiveQuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_live_quiz, parent, false);
        return new LiveQuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LiveQuizViewHolder holder, int position) {
        LiveQuiz currentItem = liveQuizList.get(position);
        holder.quizImageView.setImageResource(currentItem.getImageResId());
        holder.quizTitleTextView.setText(currentItem.getTitle());
        holder.moreInfoImageView.setImageResource(R.drawable.ic_quizmoreinfo);
    }

    @Override
    public int getItemCount() {
        return liveQuizList.size();
    }
}

