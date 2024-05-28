package Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Question;

import java.util.ArrayList;

public class QuestionsAdpter extends RecyclerView.Adapter<QuestionsAdpter.viewHolder> {

    Context context;
    ArrayList<Question> list;


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public viewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
