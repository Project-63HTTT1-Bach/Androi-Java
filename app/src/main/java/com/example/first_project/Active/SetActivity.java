package com.example.first_project.Active;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.first_project.Adapter.setAdapters;
import com.example.first_project.Model.setModel;
import com.example.first_project.R;
import com.example.first_project.databinding.ActivityMainBinding;
import com.example.first_project.databinding.ActivitySetBinding;
import com.example.first_project.databinding.ActivitySplashScreenBinding;
import com.example.first_project.databinding.ItemSetBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetActivity extends AppCompatActivity {

    ActivitySetBinding binding ;
    ArrayList<setModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.setRyce.setLayoutManager(linearLayoutManager);

        list.add(new setModel("SET-1"));
        list.add(new setModel("SET-2"));
        list.add(new setModel("SET-3"));
        list.add(new setModel("SET-4"));
        list.add(new setModel("SET-5"));
        list.add(new setModel("SET-6"));
        list.add(new setModel("SET-7"));
        list.add(new setModel("SET-8"));
        list.add(new setModel("SET-9"));
        list.add(new setModel("SET-10"));

        setAdapters adapters = new setAdapters(this,list);
        binding.setRyce.setAdapter(adapters);

    }
}