package com.androidguide.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 类成员
    private Button btnOk;
    private Button btnCancle;
    private Button btnNext;
    private Button btnPrev;
    private TextView tvQuestion;

    private ImageButton imgBtnPrev;
    private ImageButton imgBtnNext;

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
                checkAnswer(true);
            }
        });
        btnCancle = findViewById(R.id.btn_cancle);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
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

        btnPrev = findViewById(R.id.btn_pre);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurIdx == 0) {
                    Toast.makeText(MainActivity.this, R.string.info_qu, Toast.LENGTH_SHORT).show();
                    return;
                }
                mCurIdx = (mCurIdx - 1)%mQuestions.length;
                updateQuestion();
            }
        });;

        // 图标按钮
        imgBtnPrev = findViewById(R.id.imgBtn_prev);
        imgBtnPrev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mCurIdx == 0) {
                    Toast.makeText(MainActivity.this, R.string.info_qu, Toast.LENGTH_SHORT).show();
                    return;
                }
                mCurIdx = (mCurIdx - 1)%mQuestions.length;
                updateQuestion();
            }
        });
        imgBtnNext = findViewById(R.id.imgBtnNext);
        imgBtnNext.setOnClickListener(new View.OnClickListener() {
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
    }

    private void checkAnswer(boolean flag) {
        boolean res = mQuestions[mCurIdx].isAnswer();
        int id = -1;
        if (flag == res) {
            id = R.string.toast_ok;
        } else  {
            id = R.string.toast_err;
        }
        Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();
    }
}
