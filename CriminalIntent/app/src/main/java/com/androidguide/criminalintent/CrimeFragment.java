package com.androidguide.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class CrimeFragment extends Fragment {
    // Intent数据传输ID
    private static final String ARG_CRIME_ID = "com.androidguide.criminalintent.crime_id";

    // Fragment的唯一识别tag
    private static final String DATE_PICKER_DIALOG = "com.androidguide.criminalintent.date_picker_dialog";

    // FileProvider 授权
    private static final String FILE_PROVIDER_AUTH = "com.androidguide.criminalintent.fileprovider";

    // 用于从日期控件返回值进使用
    private static final int REQUEST_DATE = 1;
    // 选择联系人
    private static final int REQUEST_CONTACT = 2;
    // 拍照
    private static final int REQUEST_CAMERA = 3;

    private Crime mCrime;

    private EditText mEtTitle;
    private Button mBtnDate;
    private CheckBox mCbSolved;
    private Button mBtnReport;
    private Button mBtnSuspect;
    private ImageView mImageViewPhoto;
    private ImageButton mImageButtonCamera;

    private File mPhotoFile;

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

        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
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

        mBtnReport = v.findViewById(R.id.btn_crime_report);
        mBtnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 隐式Intent的使用
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect));
                // 加入选择器
                i = Intent.createChooser(i, getString(R.string.crime_report_send));
                startActivity(i);
            }
        });

        final Intent pickIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mBtnSuspect = v.findViewById(R.id.btn_crime_suspect);
        mBtnSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   startActivityForResult(pickIntent, REQUEST_CONTACT);
            }
        });

        if (null != mCrime.getSuspect()) {
            mBtnSuspect.setText(mCrime.getSuspect());
        }

        // 判断系统是否有联系人APP，以避免应用崩溃。
        PackageManager packageManager = getActivity().getPackageManager();
        if (null == packageManager.resolveActivity(pickIntent, PackageManager.MATCH_DEFAULT_ONLY)) {
            mBtnSuspect.setEnabled(false);
        }

        mImageButtonCamera = v.findViewById(R.id.ib_crime_camera);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean enable = null != mPhotoFile && null != captureImage.resolveActivity(packageManager);
        mImageButtonCamera.setEnabled(enable);

        mImageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 实现调用camera进行拍照，并获取结果照片。

                // 获取应用内容的存储路径，并传递给Camera应用。
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        FILE_PROVIDER_AUTH,mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager()
                        .queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo info: cameraActivities) {
                    getActivity().grantUriPermission(info.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION); // 给Camera应用授予写入本地的权限
                }

                startActivityForResult(captureImage, REQUEST_CAMERA);
            }
        });

        mImageViewPhoto = v.findViewById(R.id.iv_crime_photo);
        updatePhotoView();

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
        } else if (REQUEST_CONTACT == requestCode && null != data) {
            // 从联系人中返回数据
            Uri uri = data.getData();

            // 指定只显示联系人的哪些字段
            String[] fields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // 查询
            Cursor cursor = getActivity().getContentResolver()
                    .query(uri, fields, null, null, null);
            try {
                if (0 == cursor.getCount()) {
                    return;
                }

                cursor.moveToFirst();
                String name = cursor.getString(0);
                mCrime.setSuspect(name);
                mBtnSuspect.setText(name);
            } finally {
                cursor.close();
            }
        } else if ( REQUEST_CAMERA == requestCode) {
            Uri uri = FileProvider.getUriForFile(getActivity(), FILE_PROVIDER_AUTH, mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
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

    private String getReport() {
        String strSovled = null;

        if (mCrime.isSolved()) {
            strSovled = getString(R.string.crime_report_solved);
        } else {
            strSovled = getString(R.string.crime_report_unsolved);
        }

        String strFmt = "EEE, MM dd";
        String strDate = DateFormat.format(strFmt, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (null == suspect) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect);
        }

        String report = getString(R.string.crime_report, mCrime.
                getTitle(), strDate, strSovled, suspect);
        return report;
    }

    private void updatePhotoView() {
        if (null == mPhotoFile || !mPhotoFile.exists()) { // NOTE: 2个判断条件
            mImageViewPhoto.setImageDrawable(null);
        } else {
            Bitmap bitmap = PhotoUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mImageViewPhoto.setImageBitmap(bitmap);
        }
    }
}
