package com.androidguide.geoquiz;

public class Question {
    private int mTextId;
    private boolean mAnswer;

    public Question(int textId, boolean answer) {
        mTextId = textId;
        mAnswer = answer;
    }

    public int getTextId() {
        return mTextId;
    }

    public void setTextId(int textId) {
        mTextId = textId;
    }

    public boolean isAnswer() {
        return mAnswer;
    }

    public void setAnswer(boolean answer) {
        mAnswer = answer;
    }
}
