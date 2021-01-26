package com.example.hitstory.ui.years;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hitstory.R;
import com.example.hitstory.adapters.yearsA.YearsAdapter;
import com.example.hitstory.databinding.YearsFragmentBinding;

public class YearsFragment extends Fragment {

    private YearsViewModel mViewModel;
    YearsFragmentBinding binding;
    YearsAdapter adapter;

    public static YearsFragment newInstance() {
        return new YearsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new YearsAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.years_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(YearsViewModel.class);
        binding.yearsRecycler.setAdapter(adapter);
        mViewModel.getData();
        mViewModel.data.observe(getViewLifecycleOwner(), strings -> {
            adapter.setStrings(strings);
        });
    }

}