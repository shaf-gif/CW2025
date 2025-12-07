package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all audio playback within the application, including background music and sound effects.
 * Implements a singleton pattern to ensure a single point of control for audio resources.
 * This manager allows for playing, stopping, and controlling the volume of different audio tracks,
 * as well as enabling/disabling music and sound effects.
 */
public class AudioManager {

    /** The singleton instance of the AudioManager. */
    private static AudioManager instance;
    /** The MediaPlayer instance for playing background music. */
    private MediaPlayer backgroundMusicPlayer;
    /** A map to store loaded sound effects by name. */
    private Map<String, Media> soundEffects;
    /** The current volume level for background music (0.0 to 1.0). */
    private double musicVolume = 0.5;
    /** The current volume level for sound effects (0.0 to 1.0). */
    private double sfxVolume = 0.7;
    /** Flag indicating whether background music is enabled. */
    private boolean musicEnabled = true;
    /** Flag indicating whether sound effects are enabled. */
    private boolean sfxEnabled = true;
    /** Path to the menu background music file. */
    private static final String MENU_MUSIC = "/audio/menu_music.mp3";
    /** Path to the game background music file. */
    private static final String GAME_MUSIC = "/audio/game_music.mp3";
    /** Path to the button click sound effect file. */
    private static final String BUTTON_CLICK = "/audio/button_click.wav";
    /** Path to the piece drop sound effect file. */
    private static final String PIECE_DROP = "/audio/drop.wav";
    /** Path to the line clear sound effect file. */
    private static final String LINE_CLEAR = "/audio/line_clear.wav";
    /** Path to the rotate sound effect file. */
    private static final String ROTATE_SOUND = "/audio/rotate.wav";
    /** The factory used to create Media and MediaPlayer instances. */
    private final MediaPlayerFactory mediaPlayerFactory;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the AudioManager with a specific MediaPlayerFactory.
     * Loads all sound effects upon instantiation.
     * @param mediaPlayerFactory The factory to be used for creating media players.
     */
    private AudioManager(MediaPlayerFactory mediaPlayerFactory) {
        this.mediaPlayerFactory = mediaPlayerFactory;
        soundEffects = new HashMap<>();
        loadSoundEffects();
    }

    /**
     * Returns the singleton instance of AudioManager, creating it if it doesn't already exist
     * with a {@code DefaultMediaPlayerFactory}.
     * @return The singleton instance of AudioManager.
     */
    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager(new DefaultMediaPlayerFactory());
        }
        return instance;
    }

    /**
     * Returns the singleton instance of AudioManager, creating it if it doesn't already exist
     * with a specified {@code MediaPlayerFactory}. This method is primarily for dependency injection
     * (e.g., for testing).
     * @param factory The {@code MediaPlayerFactory} to use for creating media players if the instance is null.
     * @return The singleton instance of AudioManager.
     */
    public static synchronized AudioManager getInstance(MediaPlayerFactory factory) {
        if (instance == null) {
            instance = new AudioManager(factory);
        }
        return instance;
    }

    /**
     * Resets the singleton instance of AudioManager, stopping any active music and disposing resources.
     * This method is primarily for testing or reinitialization scenarios.
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.dispose();
            instance = null;
        }
    }

    /**
     * Loads all predefined sound effects into the {@code soundEffects} map.
     */
    private void loadSoundEffects() {
        loadSoundEffect("button_click", BUTTON_CLICK);
        loadSoundEffect("drop", PIECE_DROP);
        loadSoundEffect("line_clear", LINE_CLEAR);
        loadSoundEffect("rotate", ROTATE_SOUND);
    }
    /**
     * Loads a single sound effect from a given path and stores it with a specified name.
     * Error messages are printed to {@code System.err} if the file cannot be found or loaded.
     * @param name The logical name to associate with the sound effect.
     * @param path The path to the sound effect file (e.g., "/audio/button_click.wav").
     */
    private void loadSoundEffect(String name, String path) {
        try {
            URL resource = mediaPlayerFactory.getResource(path);
            if (resource != null) {
                Media sound = mediaPlayerFactory.createMedia(resource.toString());
                soundEffects.put(name, sound);
            } else {
                System.err.println("Could not find audio file: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error loading sound effect " + name + ": " + e.getMessage());
        }
    }

    /**
     * Plays background music. Stops any currently playing music before starting the new one.
     * Music playback is skipped if {@code musicEnabled} is false.
     * Error messages are printed to {@code System.err} if the music file cannot be found or played.
     * @param musicType A string indicating which music to play, e.g., "menu" or "game".
     */
    public void playBackgroundMusic(String musicType) {
        stopBackgroundMusic();
        if (!musicEnabled) return;
        try {
            String musicPath = musicType.equals("menu") ? MENU_MUSIC : GAME_MUSIC;
            URL resource = mediaPlayerFactory.getResource(musicPath);
            if (resource != null) {
                Media music = mediaPlayerFactory.createMedia(resource.toString());
                backgroundMusicPlayer = mediaPlayerFactory.createMediaPlayer(music);
                backgroundMusicPlayer.setVolume(musicVolume);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundMusicPlayer.play();
            } else {
                System.err.println("Could not find music file: " + musicPath);
            }
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    /**
     * Stops the currently playing background music and releases its resources.
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.dispose();
            backgroundMusicPlayer = null;
        }
    }

    /**
     * Plays a specific sound effect if sound effects are enabled.
     * The media player for the sound effect is disposed after it finishes playing.
     * Error messages are printed to {@code System.err} if the sound effect is not found.
     * @param effectName The name of the sound effect to play (e.g., "button_click").
     */
    public void playSoundEffect(String effectName) {
        if (!sfxEnabled) {
            return;
        }

        Media sound = soundEffects.get(effectName);
        if (sound != null) {
            MediaPlayer mediaPlayer = mediaPlayerFactory.createMediaPlayer(sound);
            mediaPlayer.setVolume(sfxVolume);
            mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
            mediaPlayer.play();
        } else {
            System.err.println("Sound effect not found: " + effectName);
        }
    }

    /**
     * Plays the button click sound effect.
     */
    public void playButtonClick() {
        playSoundEffect("button_click");
    }

    /**
     * Plays the piece drop sound effect.
     */
    public void playDrop() {
        playSoundEffect("drop");
    }

    /**
     * Plays the line clear sound effect.
     */
    public void playLineClear() {
        playSoundEffect("line_clear");
    }

    /**
     * Plays the rotate sound effect.
     */
    public void playRotate() {
        playSoundEffect("rotate");
    }

    /**
     * Sets the volume for background music. The volume is clamped between 0.0 and 1.0.
     * If background music is currently playing, its volume is updated immediately.
     * @param volume The desired volume level (0.0 to 1.0).
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(this.musicVolume);
        }
    }

    /**
     * Sets the volume for sound effects. The volume is clamped between 0.0 and 1.0.
     * @param volume The desired volume level (0.0 to 1.0).
     */
    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    /**
     * Returns the current volume level for background music.
     * @return The current music volume (0.0 to 1.0).
     */
    public double getMusicVolume() {
        return musicVolume;
    }

    /**
     * Returns the current volume level for sound effects.
     * @return The current SFX volume (0.0 to 1.0).
     */
    public double getSfxVolume() {
        return sfxVolume;
    }

    /**
     * Enables or disables background music. If disabled, any playing background music will be stopped.
     * @param enabled True to enable music, false to disable.
     */
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
    }

    /**
     * Enables or disables sound effects.
     * @param enabled True to enable sound effects, false to disable.
     */
    public void setSfxEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
    }

    /**
     * Checks if background music is currently enabled.
     * @return True if music is enabled, false otherwise.
     */
    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    /**
     * Checks if sound effects are currently enabled.
     * @return True if sound effects are enabled, false otherwise.
     */
    public boolean isSfxEnabled() {
        return sfxEnabled;
    }

    /**
     * Stops any playing background music and clears all loaded sound effects, releasing resources.
     */
    public void dispose() {
        stopBackgroundMusic();
        soundEffects.clear();
    }

    /**
     * For testing purposes only, allows injecting a mock {@code AudioManager} instance.
     * Use with caution as it bypasses the singleton pattern.
     * @param testInstance The mock AudioManager instance to set.
     */
    public static void setInstanceForTest(AudioManager testInstance) {
        instance = testInstance;
    }
}