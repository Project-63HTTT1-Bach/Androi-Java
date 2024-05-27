package com.example.quizapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.R;

public class MainActivity extends AppCompatActivity {
    private int selectedTab = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final LinearLayout llHome = findViewById(R.id.llHome);
        final LinearLayout llSearch = findViewById(R.id.llSearch);
        final LinearLayout llChart = findViewById(R.id.llChart);
        final LinearLayout llMe = findViewById(R.id.llMe);

        final ImageView ivAdd = findViewById(R.id.ivAdd);

        final ImageView ivHome = findViewById(R.id.ivHome);
        final ImageView ivSearch = findViewById(R.id.ivSearch);
        final ImageView ivChart = findViewById(R.id.ivChart);
        final ImageView ivMe = findViewById(R.id.ivMe);

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab!=1){
                    ivHome.setBackground(R.drawable.ic_navhomeselected);

                }
            }
        });
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        llMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}