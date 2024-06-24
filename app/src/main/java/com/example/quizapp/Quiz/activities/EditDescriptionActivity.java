package com.example.quizapp.Quiz.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Calendar;

import com.example.quizapp.R;

public class EditDescriptionActivity extends AppCompatActivity {
    private ImageView btnBack;
    private EditText inputTime, inputStartTime, inputEndTime;
    private ImageView imageViewBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);

        inputTime = (EditText) findViewById(R.id.input_time);
        inputStartTime = (EditText) findViewById(R.id.input_start_time);
        inputEndTime = (EditText) findViewById(R.id.input_end_time);

        imageViewBack.setOnClickListener(v -> finish());

        inputTime.setOnClickListener(v -> showTimePickerDialog(inputTime));

        inputStartTime.setOnClickListener(v -> showDateTimePickerDialog(inputStartTime));

        inputEndTime.setOnClickListener(v -> showDateTimePickerDialog(inputEndTime));

        btnBack.setOnClickListener(v -> finish());
    }

    private void showTimePickerDialog(EditText timeField) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            String time = String.format("%02d:%02d", hourOfDay, minuteOfHour);
            timeField.setText(time);
        }, hour, minute, true);
        timePickerDialog.show();
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
}