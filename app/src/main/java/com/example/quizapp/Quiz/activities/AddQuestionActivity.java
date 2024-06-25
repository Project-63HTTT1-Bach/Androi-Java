package com.example.quizapp.Quiz.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Quiz.adapters.QuestionAdapter;
import com.example.quizapp.Quiz.models.Question;
import com.example.quizapp.Quiz.repositories.QuestionRepository;
import com.example.quizapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {
    private RecyclerView recyclerViewQuestions;
    private FloatingActionButton fabAddQuestion;
    private QuestionRepository questionRepository;
    private QuestionAdapter questionAdapter;
    private int quizId;
    private ImageView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        recyclerViewQuestions = findViewById(R.id.recycler_view_questions);
        fabAddQuestion = findViewById(R.id.fab_add_question);
        btnBack = findViewById(R.id.btnBack);
        TextView textViewTitle = findViewById(R.id.textView7);

        quizId = getIntent().getIntExtra("quizId", -1);
        String quizName = getIntent().getStringExtra("quizName");
        textViewTitle.setText(quizName);

        questionRepository = new QuestionRepository(this);
        List<Question> questionList = questionRepository.getQuestionsByQuizId(quizId);

        questionAdapter = new QuestionAdapter(questionList, this::showDeleteConfirmationDialog);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestions.setAdapter(questionAdapter);

        fabAddQuestion.setOnClickListener(v -> showAddQuestionDialog());

        btnBack.setOnClickListener(v -> finish());
    }

    private void showAddQuestionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Question");

        final EditText input = new EditText(this);
        input.setHint("Enter question text");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String questionText = input.getText().toString().trim();
            if (!questionText.isEmpty()) {
                Question newQuestion = new Question(0, quizId, questionText, "Multiple Choice");
                questionRepository.addQuestion(newQuestion);
                questionAdapter.addQuestion(newQuestion);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Question")
                .setMessage("Are you sure you want to delete this question?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    questionRepository.deleteQuestion(questionAdapter.getQuestionAt(position));
                    questionAdapter.removeQuestionAt(position);
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
