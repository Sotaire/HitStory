/*
 * This is the source code of DMAudioStreaming for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry(dibakar.ece@gmail.com), 2017.
 */
package com.example.hitstory.data.network;


import com.example.hitstory.data.models.MediaMetaData;

import java.util.List;

public interface MusicLoaderListener {

    void onLoadSuccess(List<MediaMetaData> listMusic);

    void onLoadFailed();

    void onLoadError();
}
