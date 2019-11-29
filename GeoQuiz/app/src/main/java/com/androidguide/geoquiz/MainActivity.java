package com.androidguide.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 类成员
    private Button btnOk;
    private Button btnCancle;
    private Button btnNext;
    private TextView tvQuestion;

    // 数组
    private Question[] mQuestions = new Question[] {
            new Question(R.string.qu_australia, true),
            new Question(R.string.qu_ocean, true),
            new Question(R.string.qu_mideast, false),
            new Question(R.string.qu_africa, false),
            new Question(R.string.qu_americas, true),
            new Question(R.string.qu_asia, true)
    };

    private  int mCurIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 按钮事件
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_ok, Toast.LENGTH_SHORT).show();
            }
        });
        btnCancle = findViewById(R.id.btn_cancle);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.toast_err, Toast.LENGTH_SHORT).show();
            }
        });

        tvQuestion = findViewById(R.id.tv_Question);
        updateQuestion();

        btnNext = findViewById(R.id.btn_Next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurIdx = (mCurIdx + 1)%mQuestions.length;
                updateQuestion();
            }
        });
    }

    private void updateQuestion() {
        int id = mQuestions[mCurIdx].getTextId();
        tvQuestion.setText(id);
        Toast.makeText(MainActivity.this, Integer.toString(mCurIdx), Toast.LENGTH_SHORT).show();
    }
}
