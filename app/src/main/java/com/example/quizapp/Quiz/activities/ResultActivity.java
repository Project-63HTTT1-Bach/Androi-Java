package com.example.quizapp.Quiz.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
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

import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    private TextView tvScore;
    private TextView tvCorrectAnswers;
    private TextView tvIncorrectAnswers;
    private ImageView btnBack;
    private TextView userName;
    private ImageView userImage;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);

        tvScore = (TextView) findViewById(R.id.tv_score);
        tvCorrectAnswers = (TextView) findViewById(R.id.tv_correct_answers);
        tvIncorrectAnswers = (TextView) findViewById(R.id.tv_incorrect_answers);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        userName = (TextView) findViewById(R.id.name_text);
        userImage = (ImageView) findViewById(R.id.user_image);

        int score = getIntent().getIntExtra("score", 0);
        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int incorrectAnswers = getIntent().getIntExtra("incorrectAnswers", 0);
        int userId = getIntent().getIntExtra("userId", -1);

        tvScore.setText(String.format(Locale.getDefault(), "Score: %d", score));
        tvCorrectAnswers.setText(String.format(Locale.getDefault(), "Correct Answers: %d", correctAnswers));
        tvIncorrectAnswers.setText(String.format(Locale.getDefault(), "Incorrect Answers: %d", incorrectAnswers));

        User user = userRepository.getUser(userId);
        if (user != null) {
            userName.setText(user.getUsername());
            Bitmap bitmap = decodeBase64(user.getProfilePicture());
            userImage.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }

        btnBack.setOnClickListener(v->finish());
    }

    private Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
