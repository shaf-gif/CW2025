package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Default implementation of the {@code MediaPlayerFactory} interface.
 * This factory creates standard JavaFX {@code Media} and {@code MediaPlayer} objects.
 */
public class DefaultMediaPlayerFactory implements MediaPlayerFactory {

    /**
     * Constructs a new DefaultMediaPlayerFactory.
     * This factory provides default implementations for creating JavaFX Media and MediaPlayer objects.
     */
    public DefaultMediaPlayerFactory() {
        // Default constructor
    }
    /**
     * Creates a new {@code Media} object from the specified source string.
     * @param source The source string for the media (e.g., a file path or URL).
     * @return A new {@code Media} object.
     */
    @Override
    public Media createMedia(String source) {
        return new Media(source);
    }

    /**
     * Creates a new {@code MediaPlayer} object for the given {@code Media}.
     * @param media The {@code Media} object to play.
     * @return A new {@code MediaPlayer} object.
     */
    @Override
    public MediaPlayer createMediaPlayer(Media media) {
        return new MediaPlayer(media);
    }

    /**
     * Retrieves a URL for a resource specified by its path.
     * @param path The path to the resource (e.g., "/audio/music.mp3").
     * @return A {@code URL} object for the resource, or {@code null} if not found.
     */
    @Override
    public URL getResource(String path) {
        return getClass().getResource(path);
    }
}