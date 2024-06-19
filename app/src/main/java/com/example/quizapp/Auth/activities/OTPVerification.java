package com.example.quizapp.Auth.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.R;


public class OTPVerification extends AppCompatActivity {
    private EditText etDigit1, etDigit2, etDigit3, etDigit4, etDigit5, etDigit6;
    private TextView btnResend;
    private boolean resendEnabled = false;
    private int resendTime = 60;
    private Button btnVerify;
    private int selectedEtDigitPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpverification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etDigit1 = findViewById(R.id.etDigit1);
        etDigit2 = findViewById(R.id.etDigit2);
        etDigit3 = findViewById(R.id.etDigit3);
        etDigit4 = findViewById(R.id.etDigit4);
        etDigit5 = findViewById(R.id.etDigit5);
        etDigit6 = findViewById(R.id.etDigit6);

        btnVerify = findViewById(R.id.btnVerify);

        btnResend = findViewById(R.id.btnResend);

        final TextView tvEmail = findViewById(R.id.tvEmail);

        final String getEmail = getIntent().getStringExtra("email");
        tvEmail.setText(getEmail);

        etDigit1.addTextChangedListener(textWatcher);
        etDigit2.addTextChangedListener(textWatcher);
        etDigit3.addTextChangedListener(textWatcher);
        etDigit4.addTextChangedListener(textWatcher);
        etDigit5.addTextChangedListener(textWatcher);
        etDigit6.addTextChangedListener(textWatcher);

        showKeyBoard(etDigit1);
        startCountDownTimer();
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resendEnabled){
                    startCountDownTimer();
                }
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String generateOTP = etDigit1.getText().toString() + etDigit2.getText().toString() + etDigit3.getText().toString() + etDigit4.getText().toString() + etDigit5.getText().toString() + etDigit6.getText().toString();
            if(generateOTP.length() == 6){
 
            }}
        });

    }

    private void showKeyBoard(EditText etDigit){

        etDigit.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(etDigit, InputMethodManager.SHOW_IMPLICIT);
    }
    private void startCountDownTimer(){
        resendEnabled = false;
        btnResend.setTextColor(Color.parseColor("#99000000"));
        new CountDownTimer(resendTime * 60, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnResend.setText("Resend code (" +(millisUntilFinished/60) +")" );
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                btnResend.setText("Resend Code");
                btnResend.setTextColor(getResources().getColor(R.color.primaryColor));
            }
        }.start();
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > 0){
                if(selectedEtDigitPosition == 0){
                    showKeyBoard(etDigit2);
                    selectedEtDigitPosition = 1;
                }
                else if(selectedEtDigitPosition == 1){
                    showKeyBoard(etDigit3);
                    selectedEtDigitPosition = 2;
                }
                else if(selectedEtDigitPosition == 2){
                    showKeyBoard(etDigit4);
                    selectedEtDigitPosition = 3;
                }
                else if(selectedEtDigitPosition == 3){
                    showKeyBoard(etDigit5);
                    selectedEtDigitPosition = 4;
                }
                else if(selectedEtDigitPosition == 4){
                    showKeyBoard(etDigit6);
                    selectedEtDigitPosition = 5;
                }
            }
        }
    };
    public boolean onKeyUp(int KeyCode, KeyEvent event){
      if(KeyCode == KeyEvent.KEYCODE_DEL){
          if(selectedEtDigitPosition == 5){
              selectedEtDigitPosition = 4;
              showKeyBoard(etDigit5);
          }
          if(selectedEtDigitPosition == 4){
              selectedEtDigitPosition = 3;
              showKeyBoard(etDigit4);
          }
          if(selectedEtDigitPosition == 3){
              selectedEtDigitPosition = 2;
              showKeyBoard(etDigit3);
          }
          if(selectedEtDigitPosition == 2){
              selectedEtDigitPosition = 1;
              showKeyBoard(etDigit2);
          }
          if(selectedEtDigitPosition == 1){
              selectedEtDigitPosition = 0;
              showKeyBoard(etDigit1);
          }
          return true;
      }
      else {
            return super.onKeyUp(KeyCode, event);
      }
    }

}