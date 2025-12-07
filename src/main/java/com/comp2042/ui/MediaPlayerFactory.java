package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * An interface for a factory that creates JavaFX {@code Media} and {@code MediaPlayer} objects.
 * This allows for dependency injection and easier testing of audio components.
 */
public interface MediaPlayerFactory {
    /**
     * Creates a new {@code Media} object from the given source string.
     * @param source The source string for the media (e.g., a file path or URL).
     * @return A new {@code Media} object.
     */
    Media createMedia(String source);
    /**
     * Creates a new {@code MediaPlayer} object for the given {@code Media}.
     * @param media The {@code Media} object to play.
     * @return A new {@code MediaPlayer} object.
     */
    MediaPlayer createMediaPlayer(Media media);
    /**
     * Retrieves a URL for a resource specified by its path.
     * This method is useful for abstracting resource loading from different contexts (e.g., file system, classpath).
     * @param path The path to the resource (e.g., "/audio/music.mp3").
     * @return A {@code URL} object for the resource, or {@code null} if not found.
     */
    URL getResource(String path);
}