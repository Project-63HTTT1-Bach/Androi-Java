
<<<<<<<< HEAD:app/src/main/java/com/example/quizapp/Quiz/adapters/QuizAdapter.java
package com.example.quizapp.Quiz.adapters;
========
package com.example.quizapp.HomeAndDiscover.adapters;
>>>>>>>> TruongQuocBao:app/src/main/java/com/example/quizapp/HomeAndDiscover/adapters/QuizAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
<<<<<<<< HEAD:app/src/main/java/com/example/quizapp/Quiz/adapters/QuizAdapter.java
import com.example.quizapp.Quiz.models.Quiz;
========
import com.example.quizapp.HomeAndDiscover.models.Quiz;
>>>>>>>> TruongQuocBao:app/src/main/java/com/example/quizapp/HomeAndDiscover/adapters/QuizAdapter.java

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private List<Quiz> quizItems;
    private Context context;

    public QuizAdapter(Context context, List<Quiz> quizItems) {
        this.context = context;
        this.quizItems = quizItems;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
<<<<<<<< HEAD:app/src/main/java/com/example/quizapp/Quiz/adapters/QuizAdapter.java
        Quiz item = quizItems.get(position);
        holder.quizName.setText(item.getQuizName());
        int imageResource = context.getResources().getIdentifier(item.getIconImage(), "drawable", context.getPackageName());
        holder.quizIcon.setImageResource(imageResource);
========
//        Quiz item = quizItems.get(position);
//        holder.quizTitle.setText(item.getTitle());
//        holder.quizIcon.setImageResource(item.getIconResourceId());
>>>>>>>> TruongQuocBao:app/src/main/java/com/example/quizapp/HomeAndDiscover/adapters/QuizAdapter.java
    }

    @Override
    public int getItemCount() {
        return quizItems.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {

        ImageView quizIcon, moreInfoIcon;
        TextView quizName;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizIcon = itemView.findViewById(R.id.quizIcon);
            quizName = itemView.findViewById(R.id.quizName);
            moreInfoIcon = itemView.findViewById(R.id.moreInfoIcon);
        }
    }
}
