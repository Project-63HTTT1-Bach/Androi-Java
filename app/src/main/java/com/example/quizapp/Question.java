package com.example.quizapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

<<<<<<<< HEAD:app/src/main/java/com/example/quizapp/Question.java
public class Question extends AppCompatActivity {
========
public class setting extends AppCompatActivity {
>>>>>>>> origin/develop:app/src/main/java/com/example/quizapp/setting.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
<<<<<<<< HEAD:app/src/main/java/com/example/quizapp/Question.java
        setContentView(R.layout.activity_question);
========
        setContentView(R.layout.activity_setting);
>>>>>>>> origin/develop:app/src/main/java/com/example/quizapp/setting.java
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}