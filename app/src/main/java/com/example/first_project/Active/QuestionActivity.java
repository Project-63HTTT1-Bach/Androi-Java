package com.example.first_project.Active;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.first_project.MainActivity;
import com.example.first_project.Model.QuestionModel;
import com.example.first_project.R;
import com.example.first_project.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    ActivityQuestionBinding binding;

    ArrayList<QuestionModel> list = new ArrayList<>();
    private  int count =0;
    private  int position = 0;
    private  int score = 0;
    CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        resetTimer();
        timer.start();

        String setName = getIntent().getStringExtra("category");

        if(setName.equals("History")){
            setHistory();
        }
        else if(setName.equals("Math")){
            setMath();
        } else if (setName.equals("Physical")) {
            setPhysiccal();
        } else if (setName.equals("Sience")) {
            setSience();
        }

        for (int i =0 ;i<4;i++){
            binding.optionConrainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer((Button) v);
                }
            });
        }
        playAnimation(binding.question,0,list.get(position).getQuestion());
        binding.btnNext.setEnabled(false);
        binding.btnNext.setAlpha((float) 0.3);
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer!=null){
                    timer.cancel();
                }

                timer.start();
                binding.btnNext.setEnabled(false);
                binding.btnNext.setAlpha((float) 0.3);
                enableOption(true);
                position++;
                if(position ==list.size()){
                    Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
                    intent.putExtra("score",score);
                    intent.putExtra("total",list.size());
                    startActivity(intent);
                    finish();
                    return;
                }

                count = 0;
                playAnimation(binding.question,0,list.get(position).getQuestion());
            }
        });
    }

    private void resetTimer() {

        timer  = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                Dialog dialog = new Dialog(QuestionActivity.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout);
                dialog.findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =  new Intent(QuestionActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();

            }
        };

    }

    private void setSience() {
        list.add(new QuestionModel("Nguyên thủ những nước nào sau đây tham dự Hội nghị Ianta (2/1945)?",
                "Anh, Pháp, Mĩ.","Anh, Mĩ, Liên Xô.","Anh, Pháp, Đức.","Mĩ, Liên Xô, Trung Quốc.","Anh, Mĩ, Liên Xô."));
        list.add(new QuestionModel("Một trong những nội dung quan trọng của Hội nghị Ianta là:",
                "Đàm phán, kí kết các hiệp ước với các nước phát xít bại trận.",
                "Thỏa thuận việc giải giáp phát xít Nhật ở Đông Dương.",
                "Thỏa thuận phân chia phạm vi ảnh hưởng ở châu Âu và châu Á.",
                "Các nước phát xít Đức, Italia kí văn kiện đầu hàng phe Đồng minh.",
                "Thỏa thuận phân chia phạm vi ảnh hưởng ở châu Âu và châu Á."));
        list.add(new QuestionModel("Hội nghị Ianta (2/1945) đã họp ở đâu?",
                "Anh",
                "Pháp",
                "Thụy Sĩ",
                "Liên Xô",
                "Liên Xô"));
        list.add(new QuestionModel("Theo quyết định của Hội nghị Ianta (2/1945), khu vực nào dưới đây thuộc phạm vi ảnh hưởng của Liên Xô?",
                "Đông Âu",
                "Tây Âu",
                "Đông Nam Á",
                "Tây Đức",
                "Đông Âu"));
    }

    private void setPhysiccal() {
        list.add(new QuestionModel("Nguyên thủ những nước nào sau đây tham dự Hội nghị Ianta (2/1945)?",
                "Anh, Pháp, Mĩ.","Anh, Mĩ, Liên Xô.","Anh, Pháp, Đức.","Mĩ, Liên Xô, Trung Quốc.","Anh, Mĩ, Liên Xô."));
        list.add(new QuestionModel("Một trong những nội dung quan trọng của Hội nghị Ianta là:",
                "Đàm phán, kí kết các hiệp ước với các nước phát xít bại trận.",
                "Thỏa thuận việc giải giáp phát xít Nhật ở Đông Dương.",
                "Thỏa thuận phân chia phạm vi ảnh hưởng ở châu Âu và châu Á.",
                "Các nước phát xít Đức, Italia kí văn kiện đầu hàng phe Đồng minh.",
                "Thỏa thuận phân chia phạm vi ảnh hưởng ở châu Âu và châu Á."));
        list.add(new QuestionModel("Hội nghị Ianta (2/1945) đã họp ở đâu?",
                "Anh",
                "Pháp",
                "Thụy Sĩ",
                "Liên Xô",
                "Liên Xô"));
        list.add(new QuestionModel("Theo quyết định của Hội nghị Ianta (2/1945), khu vực nào dưới đây thuộc phạm vi ảnh hưởng của Liên Xô?",
                "Đông Âu",
                "Tây Âu",
                "Đông Nam Á",
                "Tây Đức",
                "Đông Âu"));
        
    }

    private void playAnimation(View view, int value, String data) {
       view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
               .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                   @Override
                   public void onAnimationStart(@NonNull Animator animation) {

                       if(value==0&&count<4){
                           String option ="";
                           if(count ==0){
                               option = list.get(position).getOpA();
                           }else if(count ==1){
                               option = list.get(position).getOpB();
                           }else if(count ==2){
                               option = list.get(position).getOpC();
                           }else if(count ==3){
                               option = list.get(position).getOpD();
                           }

                           playAnimation(binding.optionConrainer.getChildAt(count),0,option);
                           count++;
                       }
                   }

                   @Override
                   public void onAnimationEnd(@NonNull Animator animation) {
                        if(value ==0 ) {


                            try {
                                ((TextView)view).setText(data);
                                binding.totalQuestion.setText(position+1+"/"+list.size());

                            } catch (Exception e){
                                ((Button)view).setText(data);
                            }
                            view.setTag(data);
                            playAnimation(view,1,data);
                        }
                   }

                   @Override
                   public void onAnimationCancel(@NonNull Animator animation) {

                   }

                   @Override
                   public void onAnimationRepeat(@NonNull Animator animation) {

                   }
               });
    }


    private void enableOption(boolean b) {
        for(int i = 0;i<4;i++) {
            binding.optionConrainer.getChildAt(i).setEnabled(b);
            if (b) {
                binding.optionConrainer.getChildAt(i).setBackgroundResource(R.drawable.btn_aqt);
            }
        }
    }

    private void checkAnswer(Button optionSelected) {

        if(timer!=null){
            timer.cancel();
        }

        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);

        if(optionSelected.getText().toString().equals(list.get(position).getCorrectAnswer()) ){

            score++;
            optionSelected.setBackgroundResource(R.drawable.correct);

        } else {
            optionSelected.setBackgroundResource(R.drawable.wrong_ans);

            Button correctOP = (Button) binding.optionConrainer.findViewWithTag(list.get(position).getCorrectAnswer());
            correctOP.setBackgroundResource(R.drawable.correct);
        }
    }

    private void setMath() {
        list.add(new QuestionModel("Nguyên thủ những nước nào sau đây tham dự Hội nghị Ianta (2/1945)?",
                "Anh, Pháp, Mĩ.","Anh, Mĩ, Liên Xô.","Anh, Pháp, Đức.","Mĩ, Liên Xô, Trung Quốc.","Anh, Mĩ, Liên Xô."));
        list.add(new QuestionModel("Một trong những nội dung quan trọng của Hội nghị Ianta là:",
                "Đàm phán, kí kết các hiệp ước với các nước phát xít bại trận.",
                "Thỏa thuận việc giải giáp phát xít Nhật ở Đông Dương.",
                "Thỏa thuận phân chia phạm vi ảnh hưởng ở châu Âu và châu Á.",
                "Các nước phát xít Đức, Italia kí văn kiện đầu hàng phe Đồng minh.",
                "Thỏa thuận phân chia phạm vi ảnh hưởng ở châu Âu và châu Á."));
        list.add(new QuestionModel("Hội nghị Ianta (2/1945) đã họp ở đâu?",
                "Anh",
                "Pháp",
                "Thụy Sĩ",
                "Liên Xô",
                "Liên Xô"));
        list.add(new QuestionModel("Theo quyết định của Hội nghị Ianta (2/1945), khu vực nào dưới đây thuộc phạm vi ảnh hưởng của Liên Xô?",
                "Đông Âu",
                "Tây Âu",
                "Đông Nam Á",
                "Tây Đức",
                "Đông Âu"));
    }

    private void setHistory() {
        list.add(new QuestionModel("Nguyên thủ những nước nào sau đây tham dự Hội nghị Ianta (2/1945)?",
                "Anh, Pháp, Mĩ.","Anh, Mĩ, Liên Xô.","Anh, Pháp, Đức.","Mĩ, Liên Xô, Trung Quốc.","Anh, Mĩ, Liên Xô."));
        list.add(new QuestionModel("Một trong những nội dung quan trọng của Hội nghị Ianta là:",
                "Đàm phán, kí kết các hiệp ước với các nước phát xít bại trận.",
                "Thỏa thuận việc giải giáp phát xít Nhật ở Đông Dương.",
                "Thỏa thuận phân chia phạm vi ảnh hưởng ở châu Âu và châu Á.",
                "Các nước phát xít Đức, Italia kí văn kiện đầu hàng phe Đồng minh.",
                "Thỏa thuận phân chia phạm vi ảnh hưởng ở châu Âu và châu Á."));
        list.add(new QuestionModel("Hội nghị Ianta (2/1945) đã họp ở đâu?",
                "Anh",
                "Pháp",
                "Thụy Sĩ",
                "Liên Xô",
                "Liên Xô"));
        list.add(new QuestionModel("Theo quyết định của Hội nghị Ianta (2/1945), khu vực nào dưới đây thuộc phạm vi ảnh hưởng của Liên Xô?",
                "Đông Âu",
                "Tây Âu",
                "Đông Nam Á",
                "Tây Đức",
                "Đông Âu"));

    }
}