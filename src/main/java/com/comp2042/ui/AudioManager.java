package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private static AudioManager instance;
    private MediaPlayer backgroundMusicPlayer;
    private Map<String, Media> soundEffects;
    private double musicVolume = 0.5;
    private double sfxVolume = 0.7;
    private boolean musicEnabled = true;
    private boolean sfxEnabled = true;
    private static final String MENU_MUSIC = "/audio/menu_music.mp3";
    private static final String GAME_MUSIC = "/audio/game_music.mp3";
    private static final String BUTTON_CLICK = "/audio/button_click.wav";
    private static final String PIECE_DROP = "/audio/drop.wav";
        private static final String LINE_CLEAR = "/audio/line_clear.wav";
        private static final String ROTATE_SOUND = "/audio/rotate.wav";
    private final MediaPlayerFactory mediaPlayerFactory;

    private AudioManager(MediaPlayerFactory mediaPlayerFactory) {
        this.mediaPlayerFactory = mediaPlayerFactory;
        soundEffects = new HashMap<>();
        loadSoundEffects();
    }

    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager(new DefaultMediaPlayerFactory());
        }
        return instance;
    }

    public static synchronized AudioManager getInstance(MediaPlayerFactory factory) {
        if (instance == null) {
            instance = new AudioManager(factory);
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.dispose();
            instance = null;
        }
    }

    private void loadSoundEffects() {
        loadSoundEffect("button_click", BUTTON_CLICK);
        loadSoundEffect("drop", PIECE_DROP);
        loadSoundEffect("line_clear", LINE_CLEAR);
        loadSoundEffect("rotate", ROTATE_SOUND);
    }
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

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.dispose();
            backgroundMusicPlayer = null;
        }
    }

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

    public void playButtonClick() {
        playSoundEffect("button_click");
    }

    public void playDrop() {
        playSoundEffect("drop");
    }

    public void playLineClear() {
        playSoundEffect("line_clear");
    }

    public void playRotate() {
        playSoundEffect("rotate");
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(this.musicVolume);
        }
    }

    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
    }

    public void setSfxEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isSfxEnabled() {
        return sfxEnabled;
    }

    public void dispose() {
        stopBackgroundMusic();
        soundEffects.clear();
    }

    // For testing purposes only, allows injecting a mock instance
    public static void setInstanceForTest(AudioManager testInstance) {
        instance = testInstance;
    }
}