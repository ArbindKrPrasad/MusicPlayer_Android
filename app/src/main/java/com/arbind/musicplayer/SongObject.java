package com.arbind.musicplayer;

public class SongObject {
    String path, name, album, artist;
    SongObject(String path, String name, String album, String artist){
        this.path = path;
        this.name = name;
        this.album = album;
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }
}
