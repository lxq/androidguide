package com.androidguide.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 静态成员
    private static final String TAG = "MAINACTIVITY";
    private static final String KEY = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    // Intent传递数据时，KEY尽量带是包名，以保证整个系统的数据ID唯一性。
    private static final String ANSWER_IS_TRUE ="com.androidguide.geoquiz.answer_is_true";
    private static final String ANSWER_HAS_SHOWN ="com.androidguide.geoquiz.answer_haw_shown";

    // 类成员
    private Button btnOk;
    private Button btnCancle;
    private Button btnNext;
    private Button btnPrev;
    private TextView tvQuestion;

    private ImageButton imgBtnPrev;
    private ImageButton imgBtnNext;

    private Button btnCheat;

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
    private boolean mCheated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate()");

        // 获取Activity的状态
        if (savedInstanceState != null) {
            mCurIdx = savedInstanceState.getInt(KEY, 0);
        }

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
                mCheated = false;
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
                mCheated = false;
                updateQuestion();
            }
        });

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

        btnCheat = findViewById(R.id.btn_cheat);
        btnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start activity
                boolean flag = mQuestions[mCurIdx].isAnswer();
                Intent intent = newIntent(MainActivity.this, flag);
//                startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    // onSaveInstanceState()在onStop()前被调用
    // 默认实现将Activity的状态数据保存在Bundel对象中，Bundle是字符串与限定值之间的健值对结构。

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.d(TAG, "onSaveInstanceState()");
        savedInstanceState.putInt(KEY, mCurIdx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }
        if (REQUEST_CODE_CHEAT == requestCode) {
            if (null == data) {
                return;
            }
            mCheated = data.getBooleanExtra(ANSWER_HAS_SHOWN, false);
        }
    }

    private void updateQuestion() {
        int id = mQuestions[mCurIdx].getTextId();
        tvQuestion.setText(id);
    }

    private void checkAnswer(boolean flag) {
        boolean res = mQuestions[mCurIdx].isAnswer();
        int id = -1;
        if (mCheated) {
            id = R.string.toast_jugement;
        } else {
            if (flag == res) {
                id = R.string.toast_ok;
            } else  {
                id = R.string.toast_err;
            }
        }
        Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();
    }

    // static method
    public static Intent newIntent(Context ctx, boolean data) {
        Intent intent = new Intent(ctx, CheatActivity.class);
        intent.putExtra(ANSWER_IS_TRUE, data);
        return intent;
    }
}
