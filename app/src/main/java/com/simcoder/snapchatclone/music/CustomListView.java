package com.simcoder.snapchatclone.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.simcoder.snapchatclone.R;
import java.util.List;

public class CustomListView extends ArrayAdapter<music>  {
    private Context context;
    private List<music> musicList;
    public CustomListView(Context context, int resource, List<music> musics) {
        super(context, resource, musics);

        this.context = context;
        this.musicList = musics;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }
    private class ViewHolder
    {
        TextView musicname;
        TextView singer;
        TextView url;
        Button button;


    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }
    public View getCustomView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View myList = inflater.inflate(R.layout.recylerview_find_music, parent, false);
        holder = new ViewHolder();
        holder.musicname = (TextView) myList.findViewById(R.id.fmusic);
        holder.singer = (TextView) myList.findViewById(R.id.fsinger);
        holder.url = (TextView) myList.findViewById(R.id.furl);
        holder.button = (Button) myList.findViewById(R.id.findplaymusic);

        final ViewHolder finalHolder = holder;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),PlayAgainActivity.class);
                i.putExtra("musicurl",finalHolder.url.getText().toString());
                context.startActivity(i);
            }
        });


        music music = musicList.get(position);
        holder.musicname.setText(music.getMusicname());
        holder.singer.setText(music.getSinger());
        holder.url.setText(music.getUrl());

        myList.setTag(holder);
        return myList;
    }

}
