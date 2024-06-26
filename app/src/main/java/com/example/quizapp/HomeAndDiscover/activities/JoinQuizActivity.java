package com.example.quizapp.HomeAndDiscover.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Auth.repositories.UserRepository;
import com.example.quizapp.Quiz.activities.CreateQuizActivity;
import com.example.quizapp.Quiz.activities.DescriptionQuizActivity;
import com.example.quizapp.Quiz.activities.EditDescriptionActivity;
import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.Quiz.repositories.QuizRepository;
import com.example.quizapp.R;

public class JoinQuizActivity extends AppCompatActivity {
    private ImageView btnBack;
    private AppCompatButton btnCreateQuiz, btnJoinQuiz;
    private int userId;
    private EditText quizCode;
    private QuizRepository quizRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quizRepository = new QuizRepository(this);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCreateQuiz = (AppCompatButton) findViewById(R.id.btn_create_quiz);
        btnJoinQuiz = (AppCompatButton) findViewById(R.id.btn_join_quiz);
        quizCode = (EditText) findViewById(R.id.edit_text_code);

        btnCreateQuiz.setOnClickListener(v -> {
            Intent intent2 = new Intent(JoinQuizActivity.this, EditDescriptionActivity.class);
            intent2.putExtra("userId", userId);
            startActivity(intent2);
        });

        btnJoinQuiz.setOnClickListener(v -> {
            String code = quizCode.getText().toString().trim();
            if (!code.isEmpty()) {
                Quiz quiz = quizRepository.getQuizByCode(code);
                if (quiz != null) {
                    Intent intent2 = new Intent(JoinQuizActivity.this, DescriptionQuizActivity.class);
                    intent2.putExtra("quizId", quiz.getQuizId());
                    intent2.putExtra("quizName", quiz.getQuizName());
                    intent2.putExtra("creatorId", quiz.getCreatorId());
                    intent2.putExtra("startTime", quiz.getStartTime());
                    intent2.putExtra("endTime", quiz.getEndTime());
                    intent2.putExtra("isPublic", quiz.getIsPublic());
                    intent2.putExtra("description", quiz.getDescription());
                    intent2.putExtra("timeLimit", quiz.getTimeLimit());
                    intent2.putExtra("iconImage", quiz.getIconImage());
                    intent2.putExtra("quizCode", quiz.getQuizCode());
                    startActivity(intent2);
                } else {
                    Toast.makeText(this, "Quiz code not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a quiz code", Toast.LENGTH_SHORT).show();
            }
        });
    }
}