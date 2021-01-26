package com.example.hitstory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.hitstory.adapters.viewPager.ViewPagerAdapter;
import com.example.hitstory.databinding.ActivityMainBinding;
import com.example.hitstory.ui.songs.SongsListFragment;
import com.example.hitstory.ui.years.YearsFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ViewPagerAdapter pagerAdapter;
    public static boolean shuffleBoolean, repeatBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        initViewPager();
    }

    private void initViewPager() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SongsListFragment(),"Songs");
        pagerAdapter.addFragment(new YearsFragment(),"Years");
        binding.viewPager.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.setOffscreenPageLimit(3);
    }

}