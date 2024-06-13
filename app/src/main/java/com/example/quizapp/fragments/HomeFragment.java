package com.example.quizapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.activities.AllQuizActivity;
import com.example.quizapp.activities.FindFriendsActivity;
import com.example.quizapp.activities.SettingActivity;
import com.example.quizapp.adapters.QuizAdapter;

import com.example.quizapp.models.QuizItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewLiveQuizzes;
    private QuizAdapter liveQuizAdapter;
    private List<QuizItem> liveQuizList;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Thiết lập RecyclerView
        recyclerViewLiveQuizzes = view.findViewById(R.id.recyclerViewLiveQuizzes);
        recyclerViewLiveQuizzes.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách Live Quiz
        liveQuizList = new ArrayList<>();
        liveQuizList.add(new QuizItem("Statistics Math Quiz", R.drawable.ic_quiz1));
        liveQuizList.add(new QuizItem("Integers Quiz", R.drawable.ic_quiz2));
        // Thêm các item khác nếu cần

        // Thiết lập Adapter cho RecyclerView
        liveQuizAdapter = new QuizAdapter(liveQuizList);
        recyclerViewLiveQuizzes.setAdapter(liveQuizAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout seeFindFriends = (LinearLayout) view.findViewById(R.id.seeFindFriends);
        TextView seeAllQuizzes = (TextView) view.findViewById((R.id.seeAllQuizzes));

        seeFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindFriendsActivity.class);
                startActivity(intent);
            }
        });

        seeAllQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllQuizActivity.class);
                startActivity(intent);
            }
        });
    }
}
