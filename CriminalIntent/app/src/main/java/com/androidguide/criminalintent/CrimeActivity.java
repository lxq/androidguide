package com.androidguide.criminalintent;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {


    // 这里封装，供外部调用者使用。
    public static Intent newIntent(Context ctx, UUID crimeID) {
        Intent intent= new Intent(ctx, CrimeActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crimeID);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
