package com.androidguide.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    // 要确保与传递进的KEY一致
    private static final String ANSWER_IS_TRUE ="com.androidguide.geoquiz.answer_is_true";
    private static final String ANSWER_HAS_SHOWN ="com.androidguide.geoquiz.answer_haw_shown";

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
                setAnswerHasShown(true);
            }
        });
    }

    private void setAnswerHasShown(boolean flag) {
        Intent intent = new Intent();
        intent.putExtra(ANSWER_HAS_SHOWN,flag);
        // setResult()用于向父活动返回数据
        setResult(RESULT_OK, intent);
    }
}
