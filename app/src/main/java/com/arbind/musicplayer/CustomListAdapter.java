package com.arbind.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter {
    Context context;
    ArrayList<SongObject> arrayList;
    int resource;

    public CustomListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SongObject> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.song_view, null, false);
        //ImageView imageView = v.findViewById(R.id.imageView2);
        TextView name = v.findViewById(R.id.textView4);
        TextView artist = v.findViewById(R.id.textView6);
        TextView album = v.findViewById(R.id.textView7);
        name.setText(arrayList.get(position).getName());
        artist.setText(arrayList.get(position).getArtist());
        album.setText(arrayList.get(position).getAlbum());
        return v;
    }
}
