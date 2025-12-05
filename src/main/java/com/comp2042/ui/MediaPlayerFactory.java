package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public interface MediaPlayerFactory {
    Media createMedia(String source);
    MediaPlayer createMediaPlayer(Media media);
    URL getResource(String path);
}