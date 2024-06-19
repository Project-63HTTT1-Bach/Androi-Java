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

import com.example.quizapp.R;
import com.example.quizapp.HomeAndDiscover.activities.AllQuizActivity;
import com.example.quizapp.HomeAndDiscover.activities.FindFriendsActivity;
import com.example.quizapp.HomeAndDiscover.adapters.FriendAdapter;
import com.example.quizapp.Quiz.adapters.QuizAdapter;
import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.Quiz.models.Quiz;

import java.util.ArrayList;
import java.util.List;

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

    private RecyclerView rvQuizzes;
    private RecyclerView rvFriends;
    private QuizAdapter quizAdapter;
    private FriendAdapter friendAdapter;
    private List<Quiz> quizItems;
    private List<Friend> friends;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);

        rvQuizzes = view.findViewById(R.id.rvQuizzes);
        rvFriends = view.findViewById(R.id.rvFriends);

        rvQuizzes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        quizItems = new ArrayList<>();
//        quizItems.add(new Quiz("Statistics Math Quiz", R.drawable.ic_quiz1));
//        quizItems.add(new Quiz("Developer Quiz", R.drawable.ic_quiz1));
//        quizItems.add(new Quiz("Matrices Quiz", R.drawable.ic_quiz1));
//        quizItems.add(new Quiz("Integer Quiz", R.drawable.ic_quiz1));
//        quizItems.add(new Quiz("Matrices Quiz", R.drawable.ic_quiz1));
//        // Add more items as needed
//
//        friends = new ArrayList<>();
//        friends.add(new Friend("Maren Workman", 325, R.drawable.user_avatar));
//        friends.add(new Friend("Brandon Matrovs", 124, R.drawable.user_avatar));
        // Add more items as needed

        quizAdapter = new QuizAdapter(quizItems);
        friendAdapter = new FriendAdapter(friends);

        rvQuizzes.setAdapter(quizAdapter);
        rvFriends.setAdapter(friendAdapter);

        return view;
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
