package com.simcoder.snapchatclone.music;

public class music {
    private String musicname;
    private String singer;
    private String url;
    public music(String musicname, String singer,  String url) {
        this.musicname = musicname;
        this.singer = singer;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getMusicname() {
        return musicname;
    }

    public String getSinger() {return singer;}
}
