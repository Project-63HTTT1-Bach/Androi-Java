package com.example.first_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.first_project.Active.QuestionActivity;
import com.example.first_project.Active.SetActivity;
import com.example.first_project.Adapter.setAdapters;
import com.example.first_project.Model.setModel;
import com.example.first_project.databinding.ActivitySetBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.first_project.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivitySetBinding binding ;
    CardView history,Siecne,Math,Physical;
    ArrayList<setModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Siecne = findViewById(R.id.CV_Sience);
        Math = findViewById(R.id.CV_Math);
        Physical = findViewById(R.id.CV_Physical);
        history=findViewById(R.id.CV_History);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("category","History");
                startActivity(intent);
            }
        });
        Math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("category","Math");
                startActivity(intent);
            }
        });

        Physical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("category","Physical");
                startActivity(intent);
            }
        });
        Siecne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("category","Sience");
                startActivity(intent);
            }
        });
    }

}