package com.example.quizapp.HomeAndDiscover.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.HomeAndDiscover.repositories.FriendRepository;
import com.example.quizapp.Quiz.adapters.QuizAdapter;
import com.example.quizapp.Quiz.models.Answer;
import com.example.quizapp.Quiz.models.Question;
import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.Quiz.models.Result;
import com.example.quizapp.Quiz.repositories.AnswerRepository;
import com.example.quizapp.Quiz.repositories.QuestionRepository;

import com.example.quizapp.Quiz.repositories.QuizRepository;
import com.example.quizapp.Quiz.repositories.ResultRepository;
import com.example.quizapp.R;
import com.example.quizapp.HomeAndDiscover.activities.AllQuizActivity;
import com.example.quizapp.HomeAndDiscover.activities.FindFriendsActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewLiveQuizzes;
    private UserRepository userRepository;
    private QuizRepository quizRepository;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private ResultRepository resultRepository;
    private FriendRepository friendRepository;
    private QuizAdapter quizAdapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "QuizAppPrefs";
    private static final String KEY_INIT_DATA_DONE = "initDataDone";
    private InitDataTask initDataTask;
    private int userId;
    public HomeFragment() {
    }

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

        sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        recyclerViewLiveQuizzes = view.findViewById(R.id.recyclerViewLiveQuizzes);
        recyclerViewLiveQuizzes.setLayoutManager(new LinearLayoutManager(getContext()));

        userRepository = new UserRepository(getContext());
        quizRepository = new QuizRepository(getContext());
        questionRepository = new QuestionRepository(getContext());
        answerRepository = new AnswerRepository(getContext());
        resultRepository = new ResultRepository(getContext());
        friendRepository = new FriendRepository(getContext());

        boolean initDataDone = sharedPreferences.getBoolean(KEY_INIT_DATA_DONE, false);

        boolean firstLogin = sharedPreferences.getBoolean("firstLogin", true);

        if (firstLogin) {
            resetInitDataFlag();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstLogin", false);
            editor.apply();
        }

        if (!initDataDone) {
            initDataTask = new InitDataTask();
            initDataTask.execute();
        } else {
            updateUI();
        }

        return view;
    }

    private class InitDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            initData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_INIT_DATA_DONE, true);
            editor.apply();
            if (isAdded()) {
                updateUI();
            }
        }
    }

    private void initData() {
        Random random = new Random();
        List<User> users = UserRepository.getUserList();
        Log.d("HomeFragment", "size: " + userRepository.getUserCount());
//        for (int i = 7; i < 7+3; i++) {
//            String username = "username" + (i + 1);
//            String password = "password" + (i + 1);
//            String fullname = "fullName" + (i + 1);
//            String phone = "phone" + (i + 1);
//            String birthday = "1990-01-01";
//            String email = "email" + (i + 1) + "@gmail.com";
//            String profilePicture = "user_avatar";
//            User user = new User(i, username, password, fullname, email, profilePicture, birthday, phone);
//            userRepository.addUser(user);
//        }

        for (int i = 0; i < 100; i++) {
            int quizId = i + 1;
            String quizName = "Quiz " + (i + 1);
            int creatorId = random.nextInt(userRepository.getUserCount()) + 1;
            String startTime = "2024-06-19 09:00";
            String endTime = "2024-06-19 10:00";
            String description = "Description for Quiz " + (i + 1);
            int isPublic = 1;
            int timeLimit = 60;
            String iconImage = "ic_quiz1";
            String quizCode = "QZ" + (1000 + i + 1);
            Quiz quiz = new Quiz(quizId, quizName, creatorId, startTime, endTime, description, isPublic, timeLimit, iconImage, quizCode);
            quizRepository.addQuiz(quiz);

            for (int j = 0; j < 5; j++) {
                int questionId = (i * 5) + j + 1;
                String questionText = "Question " + (j + 1) + " for " + quizName;
                String questionType = "Multiple Choice";
                Question question = new Question(questionId, quizId, questionText, questionType);
                questionRepository.addQuestion(question);

                for (int k = 0; k < 4; k++) {
                    int answerId = (questionId * 4) + k + 1;
                    String answerText = "Answer " + (k + 1) + " for " + questionText;
                    int isCorrect = (k == 0) ? 1 : 0;
                    Answer answer = new Answer(answerId, questionId, answerText, isCorrect);
                    answerRepository.addAnswer(answer);
                }
            }

            for (User user : users) {
                int resultId = (i * userRepository.getUserCount()) + user.getUserId();
                int userId = user.getUserId();
                int score = random.nextInt(101);
                String completionDate = "2024-06-19";
                int correctAnswers = random.nextInt(5);
                int incorrectAnswers = 5 - correctAnswers;
                Result result = new Result(resultId, userId, quizId, score, completionDate, correctAnswers, incorrectAnswers);
                resultRepository.addResult(result);
            }
        }

        for (int i = 0; i < 100; i++) {
            int friendId = i + 1;
            int userId = random.nextInt(userRepository.getUserCount()) + 1;
            int friendUserId = random.nextInt(userRepository.getUserCount()) + 1;
            Friend friend = new Friend(friendId, userId, friendUserId);
            friendRepository.addFriend(friend);
        }

    }

    private void updateUI() {
        Intent intent = getActivity().getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        Log.d("HomeFragment", "userEmail: " + userEmail);
        userId = userRepository.getUserId(userEmail);
        Log.d("HomeFragment", "userId: " + userId);

        quizRepository.filterQuizzesByUserId(userId);
        List<Quiz> quizList = QuizRepository.getQuizList();

        quizAdapter = new QuizAdapter(getContext(), quizList, userId);
        recyclerViewLiveQuizzes.setAdapter(quizAdapter);
    }

    public Uri getUri(int resId) {
        return Uri.parse("android.resource://" + this.getParentFragment() + "/" + resId);
    }

    private void resetInitDataFlag() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_INIT_DATA_DONE, false);
        editor.apply();
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
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        seeAllQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllQuizActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
