package com.example.quizapp.Quiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.example.quizapp.Quiz.repositories.QuizRepository;
import com.example.quizapp.R;

public class DescriptionQuizActivity extends AppCompatActivity {
    private ImageView btnBack;
    private int quizId;
    private TextView quizNameTextView;
    private TextView questionTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView timeLimitTextView;
    private TextView creatorIdTextView;
    private TextView descriptionTextView;
    private TextView quizCodeTextView;
    private UserRepository userRepository;
    private QuizRepository quizRepository;
    private AppCompatButton btnStart;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_description_quiz);

        userRepository = new UserRepository(this);
        quizRepository = new QuizRepository(this);

        quizNameTextView = (TextView) findViewById(R.id.textView);
        questionTextView = (TextView) findViewById(R.id.questions_text);
        startTimeTextView = (TextView) findViewById(R.id.timeStart);
        endTimeTextView = (TextView) findViewById(R.id.timeEnd);
        timeLimitTextView = (TextView) findViewById(R.id.textView5);
        creatorIdTextView = (TextView) findViewById(R.id.byName);
        descriptionTextView = (TextView) findViewById(R.id.decription);
        quizCodeTextView = (TextView) findViewById(R.id.quizCode);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnStart = (AppCompatButton) findViewById(R.id.btn_start);

        quizId = getIntent().getIntExtra("quizId", -1);
        String quizName = getIntent().getStringExtra("quizName");
        int creatorId = getIntent().getIntExtra("creatorId", -1);
        String startTime = getIntent().getStringExtra("startTime");
        String endTime = getIntent().getStringExtra("endTime");
        boolean isPublic = getIntent().getBooleanExtra("isPublic", false);
        String description = getIntent().getStringExtra("description");
        int timeLimit = getIntent().getIntExtra("timeLimit", 0);
        String iconImage = getIntent().getStringExtra("iconImage");
        String quizCode = getIntent().getStringExtra("quizCode");
        int question = quizRepository.getQuestionCountByQuizId(quizId);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);

        User creator = userRepository.getUser(creatorId);
        if (creator == null) {
            Toast.makeText(this, "Creator not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String creatorName = creator.getUsername();

        questionTextView.setText(String.valueOf(question));
        quizNameTextView.setText(quizName);
        startTimeTextView.setText(startTime);
        endTimeTextView.setText(endTime);
        timeLimitTextView.setText(String.valueOf(timeLimit) + " minutes");
        creatorIdTextView.setText(creatorName);
        descriptionTextView.setText(description);
        quizCodeTextView.setText(quizCode);

        btnBack.setOnClickListener(v -> finish());

        btnStart.setOnClickListener(v -> {
            Intent intent2 = new Intent(DescriptionQuizActivity.this, QuestionActivity.class);
            intent2.putExtra("quizId", quizId);
            intent2.putExtra("userId", userId);
            intent2.putExtra("timeLimit", timeLimit);
            startActivity(intent2);
        });
    }
}
