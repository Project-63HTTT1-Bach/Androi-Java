package com.example.quizapp.Leaderboard.adapters;// LeaderboardAdapter.java

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.Leaderboard.models.LeaderboardItem;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private final List<LeaderboardItem> leaderboardItems;

    public LeaderboardAdapter(List<LeaderboardItem> leaderboardItems) {
        this.leaderboardItems = leaderboardItems;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardItem item = leaderboardItems.get(position);
        holder.position.setText(String.valueOf(item.getPosition()));
        holder.name.setText(item.getName());
        holder.points.setText(String.valueOf(item.getPoints()) + " points");
        holder.avatar.setImageResource(item.getAvatarResourceId());
        holder.medal.setImageResource(item.getMedalResourceId());

        // Set top icon visibility and resource based on position
        if (item.getPosition() == 1) {
            holder.topIcon.setVisibility(View.VISIBLE);
            holder.topIcon.setImageResource(R.drawable.ic_top1);
        } else if (item.getPosition() == 2) {
            holder.topIcon.setVisibility(View.VISIBLE);
            holder.topIcon.setImageResource(R.drawable.ic_top2);
        } else if (item.getPosition() == 3) {
            holder.topIcon.setVisibility(View.VISIBLE);
            holder.topIcon.setImageResource(R.drawable.ic_top3);
        } else {
            holder.topIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return leaderboardItems.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        TextView position, name, points;
        ImageView avatar, medal, topIcon;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.position);
            name = itemView.findViewById(R.id.name);
            points = itemView.findViewById(R.id.points);
            avatar = itemView.findViewById(R.id.avatar);
            medal = itemView.findViewById(R.id.medal);
            topIcon = itemView.findViewById(R.id.topIcon);
        }
    }
}
