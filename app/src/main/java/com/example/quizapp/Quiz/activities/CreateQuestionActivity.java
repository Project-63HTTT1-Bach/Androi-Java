package com.example.quizapp.Quiz.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Quiz.adapters.AnswerAdapter;
import com.example.quizapp.Quiz.models.Answer;
import com.example.quizapp.Quiz.models.Question;
import com.example.quizapp.Quiz.repositories.AnswerRepository;
import com.example.quizapp.Quiz.repositories.QuestionRepository;
import com.example.quizapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CreateQuestionActivity extends AppCompatActivity {

    private EditText inputQuestionText;
    private RecyclerView recyclerViewAnswers;
    private FloatingActionButton fabAddAnswer;
    private AppCompatButton btnSaveAnswer;
    private AnswerAdapter answerAdapter;
    private QuestionRepository questionRepository;
    private AnswerRepository answerRepository;
    private int questionId;
    private int quizId;
    private List<Answer> answerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        inputQuestionText = findViewById(R.id.input_question_text);
        recyclerViewAnswers = findViewById(R.id.recycler_view_answers);
        fabAddAnswer = findViewById(R.id.fab_add_answer);
        btnSaveAnswer = findViewById(R.id.button_save_answer);
        ImageView btnBack = findViewById(R.id.btnBack);

        questionRepository = new QuestionRepository(this);
        answerRepository = new AnswerRepository(this);

        quizId = getIntent().getIntExtra("quizId", -1);
        questionId = getIntent().getIntExtra("questionId", -1);
        answerList = questionId != -1 ? answerRepository.getAnswersByQuestionId(questionId) : new ArrayList<>();

        answerAdapter = new AnswerAdapter(answerList, this::showDeleteConfirmationDialog, this::setCorrectAnswer);
        recyclerViewAnswers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAnswers.setAdapter(answerAdapter);

        if (questionId != -1) {
            Question question = questionRepository.getQuestionById(questionId);
            inputQuestionText.setText(question.getQuestionText());
        }

        fabAddAnswer.setOnClickListener(v -> showAddAnswerDialog());
        btnSaveAnswer.setOnClickListener(v -> saveQuestion());

        btnBack.setOnClickListener(v -> finish());
    }

    private void showAddAnswerDialog() {
        // Show dialog to add a new answer
        EditText input = new EditText(this);
        input.setHint("Enter answer text");

        new AlertDialog.Builder(this)
                .setTitle("Add New Answer")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String answerText = input.getText().toString().trim();
                    if (!answerText.isEmpty()) {
                        Answer newAnswer = new Answer(0, questionId, answerText, 0);
                        answerList.add(newAnswer);
                        answerAdapter.notifyItemInserted(answerList.size() - 1);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Answer")
                .setMessage("Are you sure you want to delete this answer?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    answerList.remove(position);
                    answerAdapter.notifyItemRemoved(position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void setCorrectAnswer(int position) {
        for (int i = 0; i < answerList.size(); i++) {
            answerList.get(i).setIsCorrect(i == position ? 1 : 0);
        }
        answerAdapter.notifyDataSetChanged();
    }

    private void saveQuestion() {
        String questionText = inputQuestionText.getText().toString().trim();

        if (questionText.isEmpty()) {
            Toast.makeText(this, "Question text cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Question question = new Question(questionId, quizId, questionText, "Multiple Choice");
        if (questionId == -1) {
            questionRepository.addQuestion(question);
        } else {
            questionRepository.updateQuestion(question);
        }

        for (Answer answer : answerList) {
            if (answer.getAnswerId() == 0) {
                answer.setQuestionId(question.getQuestionId());
                answerRepository.addAnswer(answer);
            } else {
                answerRepository.updateAnswer(answer);
            }
        }

        Toast.makeText(this, "Question saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
