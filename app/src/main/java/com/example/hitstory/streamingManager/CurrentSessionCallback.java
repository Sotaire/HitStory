package com.example.hitstory.streamingManager;

import com.example.hitstory.data.models.MediaMetaData;

public interface CurrentSessionCallback {

    void updatePlaybackState(int state);

    void playSongComplete();

    void currentSeekBarPosition(int progress);

    void playCurrent(int indexP, MediaMetaData currentAudio);

    void playNext(int indexP, MediaMetaData currentAudio);

    void playPrevious(int indexP, MediaMetaData currentAudio);

}