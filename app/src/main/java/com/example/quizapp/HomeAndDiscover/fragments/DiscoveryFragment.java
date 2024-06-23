package com.example.quizapp.HomeAndDiscover.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.example.quizapp.HomeAndDiscover.repositories.FriendRepository;
import com.example.quizapp.Quiz.repositories.QuizRepository;
import com.example.quizapp.R;
import com.example.quizapp.HomeAndDiscover.activities.AllQuizActivity;
import com.example.quizapp.HomeAndDiscover.activities.FindFriendsActivity;
import com.example.quizapp.HomeAndDiscover.adapters.FriendAdapter;
import com.example.quizapp.Quiz.adapters.QuizAdapter;
import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.Quiz.models.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String userEmail;

    private RecyclerView rvQuizzes;
    private RecyclerView rvFriends;
    private QuizAdapter quizAdapter;
    private FriendAdapter friendAdapter;
    private QuizRepository quizRepository;
    private FriendRepository friendRepository;
    private UserRepository userRepository;

    public DiscoveryFragment() {
        // Required empty public constructor
    }

    public static DiscoveryFragment newInstance(String param1, String param2) {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
            userEmail = getArguments().getString("userEmail");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);

        rvQuizzes = (RecyclerView) view.findViewById(R.id.rvQuizzes);
        rvFriends = (RecyclerView) view.findViewById(R.id.rvFriends);
        rvQuizzes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        friendRepository = new FriendRepository(getContext());
        quizRepository = new QuizRepository(getContext());
        initData();

        int userId = 1;
        quizRepository.filterQuizzesByUserId(userId);

        List<Quiz> quizList = QuizRepository.getQuizList();
        quizAdapter = new QuizAdapter(getContext(), quizList);
        rvQuizzes.setAdapter(quizAdapter);

        friendRepository.filterFriendByUserId(userId);

        List<Friend> friendList = FriendRepository.getFriendList();
        friendAdapter = new FriendAdapter(getContext(), friendList);
        rvFriends.setAdapter(friendAdapter);

        return view;
    }

    private void initData(){
        Random random = new Random();
        List<User> users = UserRepository.getUserList();
        for (int i = 0; i < 100; i++) {
            int friendId = i + 1;
            int userId = users.get(random.nextInt(users.size())).getUserId();
            int friendUserId = users.get(random.nextInt(users.size())).getUserId();
            Friend friend = new Friend(friendId, userId, friendUserId);
            friendRepository.addFriend(friend);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView seeFindFriends = (TextView) view.findViewById(R.id.seeFindFriends);
        TextView seeAllQuizzes = (TextView) view.findViewById((R.id.seeAllQuizzes));

        seeFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindFriendsActivity.class);
                startActivity(intent);
            }
        });

        seeAllQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllQuizActivity.class);
                startActivity(intent);
            }
        });
    }
}
