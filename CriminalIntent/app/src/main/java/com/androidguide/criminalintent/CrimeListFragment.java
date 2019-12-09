package com.androidguide.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRView;
    private CrimeAdapter mCrimeAdapter;

    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使能 onCreateOptionsMenu()
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    // 菜单响应控制
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_crime_list, menu);

        MenuItem item = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            item.setTitle(R.string.show_subtitle);
        } else {
            item.setTitle(R.string.hide_subtitle);
        }
    }

    // 菜单项事件响应
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newInstance(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitleMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (null == mCrimeAdapter) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyDataSetChanged();
        }

        // 解决后退键更新菜单显示
        updateSubtitleMenu();
    }

    private void updateSubtitleMenu() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int size = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, size);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private Crime mCrime;
        private ImageView mImageViewSolved;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_crime_item, parent, false));

            itemView.setOnClickListener(this);

            mTextViewTitle = itemView.findViewById(R.id.tv_crime_item_title);
            mTextViewDate = itemView.findViewById(R.id.tv_crime_item_date);
            mImageViewSolved = itemView.findViewById(R.id.iv_crime_solved);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTextViewTitle.setText(mCrime.getTitle());
            mTextViewDate.setText(mCrime.getDate().toString());
            mImageViewSolved.setVisibility(mCrime.isSolved()?View.VISIBLE: View.GONE);
        }

        @Override
        public void onClick(View view) {
//            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            Intent intent = CrimePagerActivity.newInstance(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }

    // Adapter后面的CrimeHolder是关键，体现了两者间的关联。
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
