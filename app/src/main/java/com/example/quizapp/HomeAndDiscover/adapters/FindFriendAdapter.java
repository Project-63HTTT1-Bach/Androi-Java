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

import com.example.quizapp.HomeAndDiscover.activities.FindFriendsActivity;
import com.example.quizapp.HomeAndDiscover.repositories.FriendRepository;
import com.example.quizapp.R;
import com.example.quizapp.Auth.models.User;
import com.example.quizapp.HomeAndDiscover.models.Friend;

import java.util.List;

public class FindFriendAdapter extends RecyclerView.Adapter<FindFriendAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;
    private FriendRepository friendRepository;
    private int currentUserId;

    public FindFriendAdapter(List<User> userList, Context context, int currentUserId, FriendRepository friendRepository) {
        this.userList = userList;
        this.context = context;
        this.currentUserId = currentUserId;
        this.friendRepository = friendRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_find_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.friendName.setText(user.getFullname());
        if (user.getProfilePicture() != null) {
            byte[] decodedString = Base64.decode(user.getProfilePicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.friendAvatar.setImageBitmap(decodedByte);
        } else {
            holder.friendAvatar.setImageResource(R.drawable.user_avatar);
        }

        holder.ivAddFriend.setOnClickListener(v -> {
            // Thêm bạn cho cả user hiện tại và user được thêm
            Friend friend1 = new Friend(0, currentUserId, user.getUserId());
            Friend friend2 = new Friend(0, user.getUserId(), currentUserId);
            friendRepository.addFriend(friend1);
            friendRepository.addFriend(friend2);

            // Ẩn nút sau khi thêm bạn
            holder.ivAddFriend.setVisibility(View.GONE);

            // Cập nhật danh sách bạn bè của người dùng hiện tại
            ((FindFriendsActivity) context).updateFriendList();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(List<User> newList) {
        userList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView friendName;
        ImageView friendAvatar;
        ImageView ivAddFriend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friendName);
            friendAvatar = itemView.findViewById(R.id.friendAvatar);
            ivAddFriend = itemView.findViewById(R.id.ivAddFriend);
        }
    }
}
