package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class DefaultMediaPlayerFactory implements MediaPlayerFactory {
    @Override
    public Media createMedia(String source) {
        return new Media(source);
    }

    @Override
    public MediaPlayer createMediaPlayer(Media media) {
        return new MediaPlayer(media);
    }

    @Override
    public URL getResource(String path) {
        return getClass().getResource(path);
    }
}