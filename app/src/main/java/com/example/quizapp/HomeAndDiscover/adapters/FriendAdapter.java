// FriendAdapter.java
package com.example.quizapp.HomeAndDiscover.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.R;
import com.example.quizapp.HomeAndDiscover.models.Friend;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friends;
    private List<Friend> friendsOld;

    private Context context;
    public FriendAdapter(Context context, List<Friend> friends) {
        this.friends = friends;
        this.friendsOld = friends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
//        Friend friend = friends.get(position);
//        holder.friendName.setText(friend.getFriendUserId());
//        holder.friendAvatar.setImageResource(friend.getAvatarResourceId());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        ImageView friendAvatar;
        TextView friendName, friendPoints;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendAvatar = itemView.findViewById(R.id.friendAvatar);
            friendName = itemView.findViewById(R.id.friendName);
//            friendPoints = itemView.findViewById(R.id.friendPoints);
        }
    }
}
