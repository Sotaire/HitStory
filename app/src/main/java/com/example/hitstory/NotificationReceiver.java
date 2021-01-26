package com.example.hitstory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import static com.example.hitstory.App.ACTION_NEXT;
import static com.example.hitstory.App.ACTION_PLAY;
import static com.example.hitstory.App.ACTION_PREVIOUS;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Intent intent1 = new Intent(context,AudioService.class);
        if (actionName != null){
            switch (actionName){
                case ACTION_PLAY:
                    intent1.putExtra("ActionName", "playPause");
                    context.startService(intent1);
                    break;
                case ACTION_NEXT:
                    intent1.putExtra("ActionName", "next");
                    context.startService(intent1);
                    break;
                case ACTION_PREVIOUS:
                    intent1.putExtra("ActionName", "previous");
                    context.startService(intent1);
                    break;
            }
        }
    }
}
