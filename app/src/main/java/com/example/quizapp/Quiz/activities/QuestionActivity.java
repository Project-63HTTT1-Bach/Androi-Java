package com.example.quizapp.Quiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Quiz.adapters.OptionAdapter;
import com.example.quizapp.Quiz.models.Answer;
import com.example.quizapp.Quiz.models.Question;
import com.example.quizapp.Quiz.models.Result;
import com.example.quizapp.Quiz.repositories.AnswerRepository;
import com.example.quizapp.Quiz.repositories.QuestionRepository;
import com.example.quizapp.Quiz.repositories.ResultRepository;
import com.example.quizapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {
    private ImageView btnBack;
    private TextView tvQuestion;
    private TextView tvQuestionCount;
    private TextView tvTimer;
    private ProgressBar progressBar;
    private RecyclerView rvOptions;
    private List<Question> questions;
    private List<Answer> answers;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private CountDownTimer timer;
    private CountDownTimer answerTimer;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private ResultRepository resultRepository;
    private OptionAdapter optionAdapter;
    private int totalTimeLimit;
    private int userId;
    private int quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        tvQuestion = findViewById(R.id.tv_question);
        tvQuestionCount = findViewById(R.id.tv_question_count);
        tvTimer = findViewById(R.id.tv_timer);
        progressBar = findViewById(R.id.progressBar);
        rvOptions = findViewById(R.id.rv_options);

        questionRepository = new QuestionRepository(this);
        answerRepository = new AnswerRepository(this);
        resultRepository = new ResultRepository(this);

        quizId = getIntent().getIntExtra("quizId", -1);
        userId = getIntent().getIntExtra("userId", -1);
        if (quizId == -1 || userId == -1) {
            Toast.makeText(this, "Invalid quiz or user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        questions = questionRepository.getQuestionsByQuizId(quizId);
        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, "No questions found for this quiz", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        totalTimeLimit = getIntent().getIntExtra("timeLimit", 0) * 60000;

        rvOptions.setLayoutManager(new LinearLayoutManager(this));
        startQuizTimer(totalTimeLimit);
        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            tvQuestion.setText(question.getQuestionText());
            tvQuestionCount.setText((currentQuestionIndex + 1) + "/" + questions.size());
            progressBar.setProgress((int) (((currentQuestionIndex + 1) / (float) questions.size()) * 100));

            answers = answerRepository.getAnswersByQuestionId(question.getQuestionId());
            optionAdapter = new OptionAdapter(answers, this::onOptionSelected);
            rvOptions.setAdapter(optionAdapter);
        } else {
            finishQuiz();
        }
    }

    private void startQuizTimer(int totalTime) {
        timer = new CountDownTimer(totalTime, 1000) {
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                tvTimer.setText(timeFormatted);
            }

            public void onFinish() {
                finishQuiz();
            }
        }.start();
    }

    private void onOptionSelected(int position) {
        Answer selectedAnswer = answers.get(position);
        boolean isCorrect = selectedAnswer.getIsCorrect() == 1;

        if (isCorrect) {
            correctAnswers++;
        } else {
            incorrectAnswers++;
        }

        optionAdapter.setAnswerSelected(position, isCorrect);

        if (answerTimer != null) {
            answerTimer.cancel();
        }

        answerTimer = new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                loadNextQuestion();
            }
        }.start();
    }

    private void loadNextQuestion() {
        currentQuestionIndex++;
        loadQuestion();
    }

    private void finishQuiz() {
        int score = (int) (((double) correctAnswers / questions.size()) * 100);
        String completionDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Result result = new Result(0, userId, getIntent().getIntExtra("quizId", -1), score, completionDate, correctAnswers, incorrectAnswers);
        resultRepository.addResult(result);

        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("incorrectAnswers", incorrectAnswers);
        intent.putExtra("userId", userId);
        startActivity(intent);

        Toast.makeText(this, "Quiz completed", Toast.LENGTH_SHORT).show();
        finish();
    }
}
