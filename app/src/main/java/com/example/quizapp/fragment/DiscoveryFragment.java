package com.example.quizapp.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.quizapp.R;
import com.example.quizapp.Adapter.FriendAdapter;
import com.example.quizapp.Adapter.QuizAdapter;
import com.example.quizapp.models.Friend;
import com.example.quizapp.models.QuizItem;
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
    private List<QuizItem> quizItems;
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
        quizItems.add(new QuizItem("Statistics Math Quiz", R.drawable.ic_quiz1));
        quizItems.add(new QuizItem("Developer Quiz", R.drawable.ic_quiz1));
        quizItems.add(new QuizItem("Matrices Quiz", R.drawable.ic_quiz1));
        quizItems.add(new QuizItem("Integer Quiz", R.drawable.ic_quiz1));
        quizItems.add(new QuizItem("Matrices Quiz", R.drawable.ic_quiz1));
        // Add more items as needed

        friends = new ArrayList<>();
        friends.add(new Friend("Maren Workman", 325, R.drawable.user_avatar));
        friends.add(new Friend("Brandon Matrovs", 124, R.drawable.user_avatar));
        // Add more items as needed

        quizAdapter = new QuizAdapter(quizItems);
        friendAdapter = new FriendAdapter(friends);

        rvQuizzes.setAdapter(quizAdapter);
        rvFriends.setAdapter(friendAdapter);

        return view;
    }
}
