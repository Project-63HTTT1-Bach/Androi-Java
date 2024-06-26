package com.example.quizapp.Quiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.quizapp.R;

public class CreateQuizActivity extends AppCompatActivity {
    private ImageView btnBack;
    private TextView quizNameTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView timeLimitTextView;
    private TextView creatorIdTextView;
    private TextView descriptionTextView;
    private TextView quizCodeTextView;
    private UserRepository userRepository;
    private AppCompatButton btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_quiz);

        userRepository = new UserRepository(this);

        quizNameTextView = (TextView) findViewById(R.id.textView);
        startTimeTextView = (TextView) findViewById(R.id.timeStart);
        endTimeTextView = (TextView) findViewById(R.id.timeEnd);
        timeLimitTextView = (TextView) findViewById(R.id.textView5);
        creatorIdTextView = (TextView) findViewById(R.id.byName);
        descriptionTextView = (TextView) findViewById(R.id.decription);
        quizCodeTextView = (TextView) findViewById(R.id.quizCode);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnEdit = (AppCompatButton) findViewById(R.id.btnEdit);

        int quizId = getIntent().getIntExtra("quizId", -1);
        String quizName = getIntent().getStringExtra("quizName");
        int creatorId = getIntent().getIntExtra("creatorId", -1);
        String startTime = getIntent().getStringExtra("startTime");
        String endTime = getIntent().getStringExtra("endTime");
        boolean isPublic = getIntent().getBooleanExtra("isPublic", false);
        String description = getIntent().getStringExtra("description");
        int timeLimit = getIntent().getIntExtra("timeLimit", 0);
        String iconImage = getIntent().getStringExtra("iconImage");
        String quizCode = getIntent().getStringExtra("quizCode");

        Log.d("CreateQuizActivity", "creatorId: " + creatorId);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateQuizActivity.this, EditDescriptionActivity.class);
                intent.putExtra("quizId", quizId);
                intent.putExtra("quizName", quizName);
                intent.putExtra("creatorId", creatorId);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                intent.putExtra("description", description);
                intent.putExtra("isPublic", isPublic);
                intent.putExtra("timeLimit", timeLimit);
                intent.putExtra("iconImage", iconImage);
                intent.putExtra("quizCode", quizCode);
                startActivity(intent);
                finish();
            }
        });
    }
}