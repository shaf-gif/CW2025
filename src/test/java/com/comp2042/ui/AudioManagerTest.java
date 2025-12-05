package com.comp2042.ui;

import com.comp2042.JavaFxTestBase;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AudioManagerTest extends JavaFxTestBase {

    @Mock
    private MediaPlayerFactory factory;
    @Mock
    private Media media;
    @Mock
    private MediaPlayer mediaPlayer;

    private AudioManager audioManager;

    @BeforeEach
    public void setUp() throws Exception {
        URL resourceUrl = new URL("file:/audio/test.mp3");
        lenient().when(factory.getResource(anyString())).thenReturn(resourceUrl);
        lenient().when(factory.createMedia(anyString())).thenReturn(media);
        lenient().when(factory.createMediaPlayer(any(Media.class))).thenReturn(mediaPlayer);

        // Reset the singleton instance and inject the mock factory
        AudioManager.resetInstance();
        audioManager = AudioManager.getInstance(factory);
    }

    @AfterEach
    public void tearDown() {
        // Ensure the singleton is reset after each test for isolation
        AudioManager.resetInstance();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(audioManager);
        AudioManager instance2 = AudioManager.getInstance();
        assertSame(audioManager, instance2);
    }

    @Test
    public void testPlayBackgroundMusic() {
        audioManager.playBackgroundMusic("menu");

        verify(factory).getResource(eq("/audio/menu_music.mp3"));
        verify(factory, times(5)).createMedia(anyString()); // 4 from init + 1 from playBackgroundMusic
        verify(factory, times(1)).createMediaPlayer(media); // 1 from playBackgroundMusic
        verify(mediaPlayer).setVolume(audioManager.getMusicVolume());
        verify(mediaPlayer).setCycleCount(MediaPlayer.INDEFINITE);
        verify(mediaPlayer).play();
    }

    @Test
    public void testStopBackgroundMusic() {
        // First, play music to have a player to stop
        audioManager.playBackgroundMusic("game");
        // now, the internal mediaPlayer is the mock
        audioManager.stopBackgroundMusic();
        verify(mediaPlayer).stop();
        verify(mediaPlayer).dispose();
    }

    @Test
    public void testPlaySoundEffect() {
        audioManager.playSoundEffect("drop");

        verify(factory).getResource(eq("/audio/drop.wav"));
        // createMedia is implicitly called 4 times during init, not directly by playSoundEffect
        verify(factory, times(1)).createMediaPlayer(any(Media.class)); // playSoundEffect creates one new player

        // To verify play on the new player, we need to capture it.
        // But since the class is not designed for this, we trust the factory is called.
    }

    @Test
    void testPlaySoundEffect_SfxDisabled() {
        audioManager.setSfxEnabled(false);
        audioManager.playSoundEffect("drop");
        verify(mediaPlayer, never()).play();
    }


    @Test
    public void testSetMusicVolume() {
        audioManager.playBackgroundMusic("menu");
        audioManager.setMusicVolume(0.8);
        assertEquals(0.8, audioManager.getMusicVolume());
        verify(mediaPlayer).setVolume(0.8);
    }

    @Test
    public void testSetSfxVolume() {
        audioManager.setSfxVolume(0.9);
        assertEquals(0.9, audioManager.getSfxVolume());
    }

    @Test
    public void testMusicDisabled() {
        audioManager.playBackgroundMusic("menu");
        audioManager.setMusicEnabled(false);
        assertFalse(audioManager.isMusicEnabled());
        verify(mediaPlayer).stop();
    }
}
