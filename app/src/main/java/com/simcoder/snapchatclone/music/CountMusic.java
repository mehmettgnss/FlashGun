package com.simcoder.snapchatclone.music;

public class CountMusic {

    private int musiccount=1;

    public CountMusic(int musiccount){
        this.musiccount = musiccount+1;
    }

    public  int getMusiccount(){return musiccount;}

}