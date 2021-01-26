package com.example.hitstory.ui.songs;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hitstory.data.network.MusicBrowser;
import com.example.hitstory.data.network.MusicLoaderListener;
import com.example.hitstory.data.models.MediaMetaData;

import java.util.List;

public class SongsListViewModel extends ViewModel {

    MutableLiveData<List<MediaMetaData>> data = new MutableLiveData<>();

    public void loadMusicData() {
        MusicBrowser.loadMusic("2014", new MusicLoaderListener() {
            @Override
            public void onLoadSuccess(List<MediaMetaData> listMusic) {
                data.setValue(listMusic);
            }

            @Override
            public void onLoadFailed() {
                //TODO SHOW FAILED REASON
            }

            @Override
            public void onLoadError() {
                //TODO SHOW ERROR
            }
        });
    }

}