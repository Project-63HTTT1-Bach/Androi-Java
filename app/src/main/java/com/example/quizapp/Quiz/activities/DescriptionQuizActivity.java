package com.example.quizapp.Quiz.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.example.quizapp.R;

public class DescriptionQuizActivity extends AppCompatActivity {
    private ImageView btnBack;
    private TextView quizNameTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView timeLimitTextView;
    private TextView creatorIdTextView;
    private TextView descriptionTextView;
    private TextView quizCodeTextView;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_description_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);

        quizNameTextView = findViewById(R.id.textView);
        startTimeTextView = findViewById(R.id.timeStart);
        endTimeTextView = findViewById(R.id.timeEnd);
        timeLimitTextView = findViewById(R.id.textView5);
        creatorIdTextView = findViewById(R.id.byName);
        descriptionTextView = findViewById(R.id.decription);
        quizCodeTextView = findViewById(R.id.quizCode);
        btnBack = findViewById(R.id.btnBack);

        String quizName = getIntent().getStringExtra("quizName");
        int creatorId = getIntent().getIntExtra("creatorId", -1);
        String startTime = getIntent().getStringExtra("startTime");
        String endTime = getIntent().getStringExtra("endTime");
        boolean isPublic = getIntent().getBooleanExtra("isPublic", false);
        String description = getIntent().getStringExtra("description");
        int timeLimit = getIntent().getIntExtra("timeLimit", 0);
        String iconImage = getIntent().getStringExtra("iconImage");
        String quizCode = getIntent().getStringExtra("quizCode");

        User creator = userRepository.getUser(creatorId);
        if (creator == null) {
            Toast.makeText(this, "Creator not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String creatorName = creator.getUsername();

        quizNameTextView.setText(quizName);
        startTimeTextView.setText(startTime);
        endTimeTextView.setText(endTime);
        timeLimitTextView.setText(String.valueOf(timeLimit) + " minutes");
        creatorIdTextView.setText(creatorName);
        descriptionTextView.setText(description);
        quizCodeTextView.setText(quizCode);

        btnBack.setOnClickListener(v -> finish());
    }
}
