package com.androidguide.criminalintent;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    public static final String EXTRA_CRIME_ID = "com.androidguide.criminalintent.crime_id";


    // 这里封装，供外部调用者使用。
    public static Intent newIntent(Context ctx, UUID crimeID) {
        Intent intent= new Intent(ctx, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeID);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        // 通过Intent获取外部传递给Activity的数据
        UUID id = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(id);
    }
}
