package com.example.hitstory.adapters.music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hitstory.R;
import com.example.hitstory.data.models.MediaMetaData;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {

    ArrayList<MediaMetaData> data = new ArrayList<>();
    ClickListener listener;

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.inflate_allsongsitem,
                parent,
                false
        ));
    }

    public void setData(ArrayList<MediaMetaData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        holder.title.setText(data.get(position).getMediaArtist());
        holder.body.setText(data.get(position).getMediaTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MusicHolder extends RecyclerView.ViewHolder {

        ImageView mediaArt,play;
        TextView title,body;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_mediaTitle);
            body = itemView.findViewById(R.id.text_mediaDesc);
            mediaArt = itemView.findViewById(R.id.img_mediaArt);
            play = itemView.findViewById(R.id.img_playState);
            itemView.setOnClickListener(view -> {
                listener.click(getAdapterPosition());
            });
        }

    }
}
