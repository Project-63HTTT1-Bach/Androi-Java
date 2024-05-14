package com.example.first_project.Active;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.first_project.MainActivity;
import com.example.first_project.R;
import com.example.first_project.databinding.ActivityMainBinding;
import com.example.first_project.databinding.ActivityScoreBinding;

public class ScoreActivity extends AppCompatActivity {

    ActivityScoreBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        int totalScore = getIntent().getIntExtra("total",0);
        int correct = getIntent().getIntExtra("score",0);
        int wrong = totalScore - correct;

        binding.total.setText(String.valueOf(totalScore));
        binding.correct.setText(String.valueOf(correct));
        binding.wrong.setText(String.valueOf(wrong));

        binding.BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}