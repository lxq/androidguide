package com.androidguide.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    // Intent数据传输ID
    private static final String ARG_CRIME_ID = "com.androidguide.criminalintent.crime_id";

    // Fragment的唯一识别tag
    private static final String DATE_PICKER_DIALOG = "com.androidguide.criminalintent.date_picker_dialog";

    // 用于从日期控件返回值进使用
    private static final int REQUEST_DATE = 1;

    private Crime mCrime;

    private EditText mEtTitle;
    private Button mBtnDate;
    private CheckBox mCbSolved;

    // 创建实实例的静态方法
    // 通过Bundle实现数据传递
    public static CrimeFragment newInstance(UUID crimeID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeID);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return  crimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID uuid = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(uuid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_crime, container,false);

        mEtTitle = v.findViewById(R.id.et_crime_title);
        mEtTitle.setText(mCrime.getTitle());
        mEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBtnDate = v.findViewById(R.id.btn_crime_date);
        updateDate();
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dlg = DatePickerFragment.newInstance(mCrime.getDate());
                dlg.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dlg.show(fm, DATE_PICKER_DIALOG);
            }
        });

        mCbSolved = v.findViewById(R.id.cb_crime_solved);
        mCbSolved.setChecked(mCrime.isSolved());
        mCbSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Activity.RESULT_OK != resultCode) {
            return;
        }
        if (REQUEST_DATE == requestCode) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_CRIME_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // 更新界面
        CrimeLab.get(getActivity()).update(mCrime);
    }

    private void updateDate() {
        mBtnDate.setText(mCrime.getDate().toString());
    }
}
