package com.example.hitstory.adapters.yearsA;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hitstory.R;
import com.example.hitstory.databinding.YearsItemBinding;

import java.util.ArrayList;

public class YearsAdapter extends RecyclerView.Adapter<YearsAdapter.YearsHolder> {

    YearsItemBinding binding;
    ArrayList<String> strings = new ArrayList<>();

    @NonNull
    @Override
    public YearsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.years_item,
                parent,false);
        return new YearsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull YearsHolder holder, int position) {
        binding.yearsTitle.setText(strings.get(position));
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
    }

    public class YearsHolder extends RecyclerView.ViewHolder {

        public YearsHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
        }
    }
}
