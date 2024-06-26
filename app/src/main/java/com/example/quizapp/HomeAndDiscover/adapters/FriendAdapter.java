// FriendAdapter.java
package com.example.quizapp.HomeAndDiscover.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<Friend> friends;
    private Context context;
    private UserRepository userRepository;

    public FriendAdapter(Context context, List<Friend> friends, UserRepository userRepository) {
        this.friends = friends;
        this.context = context;
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void updateList(List<Friend> newList) {
        friends = newList;
        notifyDataSetChanged();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        ImageView friendAvatar;
        TextView friendName;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            friendAvatar = itemView.findViewById(R.id.friendAvatar);
            friendName = itemView.findViewById(R.id.friendName);
        }

        public void bind(Friend friend) {
            User user = userRepository.getUser(friend.getFriendUserId());
            if (user != null) {
                friendName.setText(user.getFullname());

                if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                    byte[] decodedString = Base64.decode(user.getProfilePicture(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    friendAvatar.setImageBitmap(decodedByte);
                } else {
                    friendAvatar.setImageResource(R.drawable.user_avatar);
                }
            } else {
                friendName.setText("Unknown");
                friendAvatar.setImageResource(R.drawable.user_avatar);
            }
        }
    }
}
