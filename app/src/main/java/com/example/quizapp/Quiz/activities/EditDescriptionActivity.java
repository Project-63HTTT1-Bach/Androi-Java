package com.example.quizapp.Quiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Calendar;

import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.Quiz.repositories.QuizRepository;
import com.example.quizapp.R;

public class EditDescriptionActivity extends AppCompatActivity {
    private ImageView btnBack;
    private EditText inputTime, inputStartTime, inputEndTime, inputQuizName, inputDescription;
    private AppCompatButton btnEditQuestions, btnSave;
    private QuizRepository quizRepository;

    private int quizId = -1;
    private String quizName;
    private int creatorId;
    private String startTime;
    private String endTime;
    private boolean isPublic;
    private String description;
    private int timeLimit;
    private String iconImage;
    private String quizCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);

        inputQuizName = findViewById(R.id.input_quiz_name);
        inputTime = findViewById(R.id.input_time);
        inputStartTime = findViewById(R.id.input_start_time);
        inputEndTime = findViewById(R.id.input_end_time);
        inputDescription = findViewById(R.id.input_description);
        btnBack = findViewById(R.id.btnBack);
        btnEditQuestions = findViewById(R.id.button_edit_question);
        btnSave = findViewById(R.id.button_save);

        quizRepository = new QuizRepository(this);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            quizId = intent.getIntExtra("quizId", -1);
            quizName = intent.getStringExtra("quizName");
            creatorId = intent.getIntExtra("creatorId", -1);
            startTime = intent.getStringExtra("startTime");
            endTime = intent.getStringExtra("endTime");
            isPublic = intent.getBooleanExtra("isPublic", false);
            description = intent.getStringExtra("description");
            timeLimit = intent.getIntExtra("timeLimit", 0);
            iconImage = intent.getStringExtra("iconImage");
            quizCode = intent.getStringExtra("quizCode");

            inputQuizName.setText(quizName);
            inputStartTime.setText(startTime);
            inputEndTime.setText(endTime);
            inputTime.setText(timeLimit > 0 ? timeLimit + " minutes" : "");
            inputDescription.setText(description);
        }

        if (creatorId == -1) {
            creatorId = intent.getIntExtra("userId", -1);
        }

        btnBack.setOnClickListener(v -> finish());

        inputStartTime.setOnClickListener(v -> showDateTimePickerDialog(inputStartTime));
        inputEndTime.setOnClickListener(v -> showDateTimePickerDialog(inputEndTime));

        btnEditQuestions.setOnClickListener(v -> {
            if (quizId == -1) {
                saveQuizDetails(true); // Save and navigate to AddQuestionActivity
            } else {
                navigateToAddQuestionActivity();
            }
        });

        btnSave.setOnClickListener(v -> saveQuizDetails(false));
    }

    private void showDateTimePickerDialog(EditText dateTimeField) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            calendar.set(year1, monthOfYear, dayOfMonth);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minuteOfHour) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minuteOfHour);
                String dateTime = String.format("%04d-%02d-%02d %02d:%02d", year1, monthOfYear + 1, dayOfMonth, hourOfDay, minuteOfHour);
                dateTimeField.setText(dateTime);
            }, hour, minute, true);
            timePickerDialog.show();
        }, year, month, day);
        datePickerDialog.show();
    }

    private void saveQuizDetails(boolean navigateToQuestions) {
        String quizName = inputQuizName.getText().toString().trim();
        String startTime = inputStartTime.getText().toString().trim();
        String endTime = inputEndTime.getText().toString().trim();
        String timeLimitStr = inputTime.getText().toString().replace(" minutes", "").trim();
        String description = inputDescription.getText().toString().trim();

        // Validation
        if (quizName.isEmpty()) {
            Toast.makeText(this, "Quiz name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startTime.isEmpty()) {
            Toast.makeText(this, "Start time is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endTime.isEmpty()) {
            Toast.makeText(this, "End time is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (timeLimitStr.isEmpty()) {
            Toast.makeText(this, "Time limit is required", Toast.LENGTH_SHORT).show();
            return;
        }

        int timeLimit;
        try {
            timeLimit = Integer.parseInt(timeLimitStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid time limit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quizId == -1) {
            Quiz newQuiz = new Quiz(0, quizName, creatorId, startTime, endTime, description, isPublic ? 1 : 0, timeLimit, iconImage, quizCode);
            if (quizRepository.addQuiz(newQuiz)) {
                Toast.makeText(this, "Quiz added successfully", Toast.LENGTH_SHORT).show();
                if (navigateToQuestions) {
                    navigateToAddQuestionActivity();
                } else {
                    finish();
                }
            } else {
                Toast.makeText(EditDescriptionActivity.this, "Failed to add quiz", Toast.LENGTH_LONG).show();
            }
        } else {
            Quiz updatedQuiz = new Quiz(quizId, quizName, creatorId, startTime, endTime, description, isPublic ? 1 : 0, timeLimit, iconImage, quizCode);
            if (quizRepository.updateQuiz(updatedQuiz)) {
                Toast.makeText(this, "Quiz updated successfully", Toast.LENGTH_SHORT).show();
                if (navigateToQuestions) {
                    navigateToAddQuestionActivity();
                } else {
                    finish();
                }
            } else {
                Toast.makeText(EditDescriptionActivity.this, "Failed to update quiz", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void navigateToAddQuestionActivity() {
        Intent addQuestionIntent = new Intent(EditDescriptionActivity.this, AddQuestionActivity.class);
        addQuestionIntent.putExtra("quizId", quizId);
        addQuestionIntent.putExtra("quizName", inputQuizName.getText().toString().trim());
        startActivity(addQuestionIntent);
    }
}
