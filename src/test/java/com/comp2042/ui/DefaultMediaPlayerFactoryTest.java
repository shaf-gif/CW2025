package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import com.comp2042.JavaFxTestBase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DefaultMediaPlayerFactoryTest extends JavaFxTestBase {

    @Test
    void createMediaReturnsCorrectMediaObject() {
        String testSource = "http://example.com/test.mp3";
        try (MockedConstruction<Media> mockedConstructionMedia = mockConstruction(Media.class,
                (mock, context) -> when(mock.getSource()).thenReturn((String) context.arguments().get(0)))) {
            DefaultMediaPlayerFactory factory = new DefaultMediaPlayerFactory();
            Media media = factory.createMedia(testSource);

            assertNotNull(media);
            assertEquals(1, mockedConstructionMedia.constructed().size());
            assertEquals(testSource, mockedConstructionMedia.constructed().get(0).getSource());
        }
    }

    @Test
    void createMediaPlayerReturnsCorrectMediaPlayerObject() {
        Media mockMedia = mock(Media.class);
        try (MockedConstruction<MediaPlayer> mockedConstructionMediaPlayer = mockConstruction(MediaPlayer.class,
                (mock, context) -> when(mock.getMedia()).thenReturn((Media) context.arguments().get(0)))) {
            DefaultMediaPlayerFactory factory = new DefaultMediaPlayerFactory();
            MediaPlayer mediaPlayer = factory.createMediaPlayer(mockMedia);

            assertNotNull(mediaPlayer);
            assertEquals(1, mockedConstructionMediaPlayer.constructed().size());
            assertEquals(mockMedia, mockedConstructionMediaPlayer.constructed().get(0).getMedia());
        }
    }

    @Test
    void getResourceReturnsNonNullUrlForExistingResource() {
        // Assuming there is a test resource, e.g., in src/main/resources or test/resources
        // For actual testing, you might need a valid dummy resource path
        // For now, we'll use a path that should exist (like one of the fxml files)
        String existingResourcePath = "/menuLayout.fxml"; // Example: path to a known resource

        DefaultMediaPlayerFactory factory = new DefaultMediaPlayerFactory();
        assertNotNull(factory.getResource(existingResourcePath), "Resource for " + existingResourcePath + " should not be null");
    }

    @Test
    void getResourceReturnsNullForNonExistingResource() {
        String nonExistingResourcePath = "/non_existent_resource.xyz";
        DefaultMediaPlayerFactory factory = new DefaultMediaPlayerFactory();
        assertNull(factory.getResource(nonExistingResourcePath), "Resource for " + nonExistingResourcePath + " should be null");
    }
}
