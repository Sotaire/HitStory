package com.example.hitstory.ui.songs;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.MediaMetadata;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hitstory.R;
import com.example.hitstory.adapters.music.MusicAdapter;
import com.example.hitstory.data.models.MediaMetaData;
import com.example.hitstory.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class SongsListFragment extends Fragment {

    private SongsListViewModel mViewModel;
    RecyclerView recyclerView;
    MusicAdapter adapter;
    public static ArrayList<MediaMetaData> data = new ArrayList<>();

    public static SongsListFragment newInstance() {
        return new SongsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.songs_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler);
        adapter = new MusicAdapter();
        recyclerView.setAdapter(adapter);
        clickL();
    }

    private void clickL() {
        adapter.setListener(position -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(DetailActivity.SONG, position);
            startActivity(intent);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SongsListViewModel.class);
        mViewModel.loadMusicData();
        mViewModel.data.observe(getViewLifecycleOwner(), new Observer<List<MediaMetaData>>() {
            @Override
            public void onChanged(List<MediaMetaData> mediaMetaData) {
                data = (ArrayList<MediaMetaData>) mediaMetaData;
                adapter.setData(data);
            }
        });
    }


}