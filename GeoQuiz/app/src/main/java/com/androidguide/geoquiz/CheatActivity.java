package com.androidguide.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    // 要确保与传递进的KEY一致
    private static final String ANSWER_IS_TRUE ="com.androidguide.geoquiz.answer_is_true";

    private boolean mAnswerTrue = false;

    private TextView tvAnswer;
    private Button btnShowAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        // get Intent data
        mAnswerTrue = getIntent().getBooleanExtra(ANSWER_IS_TRUE, false);

        tvAnswer = findViewById(R.id.tv_answer);

        btnShowAnswer = findViewById(R.id.btn_answer);
        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerTrue) {
                    tvAnswer.setText(R.string.btn_ok);
                } else {
                    tvAnswer.setText(R.string.btn_cancle);
                }
            }
        });
    }
}
