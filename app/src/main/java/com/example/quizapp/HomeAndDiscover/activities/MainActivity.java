package com.example.quizapp.HomeAndDiscover.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Auth.repositories.UserRepository;
import com.example.quizapp.R;
import com.example.quizapp.Leaderboard.fragments.ChartFragment;
import com.example.quizapp.HomeAndDiscover.fragments.DiscoveryFragment;
import com.example.quizapp.HomeAndDiscover.fragments.HomeFragment;
import com.example.quizapp.Auth.fragments.MeFragment;

public class MainActivity extends AppCompatActivity {
    private int selectedTab = 1;
    private String userEmail;
    private UserRepository userRepository;
    private int userId;

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

        userRepository = new UserRepository(this);

        // Lấy email từ Intent
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        userId = userRepository.getUserId(userEmail);

        final LinearLayout llHome = findViewById(R.id.llHome);
        final LinearLayout llDiscovery = findViewById(R.id.llDiscovery);
        final LinearLayout llChart = findViewById(R.id.llChart);
        final LinearLayout llMe = findViewById(R.id.llMe);

        final ImageView ivAdd = findViewById(R.id.ivAdd);

        final ImageView ivHome = findViewById(R.id.ivHome);
        final ImageView ivDiscovery = findViewById(R.id.ivDiscovery);
        final ImageView ivChart = findViewById(R.id.ivChart);
        final ImageView ivMe = findViewById(R.id.ivMe);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fcvFragment, HomeFragment.class, null)
                .commit();

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 1) {
                    ivHome.setImageResource(R.drawable.ic_navhomeselected);
                    ivDiscovery.setImageResource(R.drawable.ic_navsearch);
                    ivChart.setImageResource(R.drawable.ic_navchart);
                    ivMe.setImageResource(R.drawable.ic_navme);
                    selectedTab = 1;

                    Bundle args = new Bundle();
                    args.putString("userEmail", userEmail);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fcvFragment, HomeFragment.class, null)
                            .commit();
                }
            }
        });
        llDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 2) {
                    ivHome.setImageResource(R.drawable.ic_navhome);
                    ivDiscovery.setImageResource(R.drawable.ic_navsearchselected);
                    ivChart.setImageResource(R.drawable.ic_navchart);
                    ivMe.setImageResource(R.drawable.ic_navme);
                    selectedTab = 2;

                    Bundle args = new Bundle();
                    args.putString("userEmail", userEmail);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fcvFragment, DiscoveryFragment.class, null)
                            .commit();
                }
            }
        });
        llChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 3) {
                    ivHome.setImageResource(R.drawable.ic_navhome);
                    ivDiscovery.setImageResource(R.drawable.ic_navsearch);
                    ivChart.setImageResource(R.drawable.ic_navchartselected);
                    ivMe.setImageResource(R.drawable.ic_navme);
                    selectedTab = 3;

                    Bundle args = new Bundle();
                    args.putString("userEmail", userEmail);

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fcvFragment, ChartFragment.class, null)
                            .commit();
                }
            }
        });
        llMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTab != 4) {
                    ivHome.setImageResource(R.drawable.ic_navhome);
                    ivDiscovery.setImageResource(R.drawable.ic_navsearch);
                    ivChart.setImageResource(R.drawable.ic_navchart);
                    ivMe.setImageResource(R.drawable.ic_navmeselected);
                    selectedTab = 4;
                    MeFragment meFragment = new MeFragment();

                    Bundle args = new Bundle();
                    args.putString("userEmail", userEmail);

                    meFragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fcvFragment, meFragment, null)
                            .commit();
                }
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinQuizActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
}
