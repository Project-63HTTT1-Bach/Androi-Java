package com.example.quizapp.HomeAndDiscover.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.example.quizapp.Quiz.adapters.QuizAdapter;
import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.Quiz.repositories.QuizRepository;
import com.example.quizapp.R;
import com.example.quizapp.HomeAndDiscover.activities.AllQuizActivity;
import com.example.quizapp.HomeAndDiscover.activities.FindFriendsActivity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewLiveQuizzes;
    private UserRepository userRepository;
    private QuizRepository quizRepository;
    private QuizAdapter quizAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewLiveQuizzes = view.findViewById(R.id.recyclerViewLiveQuizzes);
        recyclerViewLiveQuizzes.setLayoutManager(new LinearLayoutManager(getContext()));

        userRepository = new UserRepository(getContext());
        quizRepository = new QuizRepository(getContext());
        initData();

        List<Quiz> quizList = QuizRepository.getQuizList();
        quizAdapter = new QuizAdapter(getContext(), quizList);
        recyclerViewLiveQuizzes.setAdapter(quizAdapter);

        return view;
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            String username = "username" + (i + 1);
            String password = "password" + (i + 1);
            String fullname = "fullName" + (i + 1);
            String phone = "phone" + (i + 1);
            String birthday="";
            String email = "email"+(i+1)+"@gmail.com";
            String profilePicture = "user_avatar";
            User user = new User(i, username, password,fullname,email,profilePicture,birthday,phone);
            userRepository.addUser(user);
        }
        Random random = new Random();
        List<User> users = UserRepository.getUserList();
        for (int i = 0; i < 100; i++) {
            int quizId = i + 1;
            String quizName = "Quiz " + (i + 1);
            int creatorId = users.get(random.nextInt(users.size())).getUserId();
            String createDate = "2024-06-19";
            int isPublic = 1;
            int timeLimit = 60;
            String iconImage = "ic_quiz1";
            Quiz quiz = new Quiz(quizId, quizName, creatorId, createDate, isPublic, timeLimit, iconImage);
            quizRepository.addQuiz(quiz);
        }
    }
    public Uri getUri(int resId) {
        return Uri.parse("android.resource://" + this.getParentFragment() + "/" + resId);
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout seeFindFriends = view.findViewById(R.id.seeFindFriends);
        TextView seeAllQuizzes = view.findViewById((R.id.seeAllQuizzes));

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
