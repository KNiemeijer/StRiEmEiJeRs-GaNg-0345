package nl.striemeijersgang0345.interfaces;

import nl.striemeijersgang0345.MainActivity;
import nl.striemeijersgang0345.other.MediaPlayerHolder;
import nl.striemeijersgang0345.listeners.PlaybackInfoListener;

/**
 * Allows {@link MainActivity} to control media playback of {@link MediaPlayerHolder}.
 */
public interface PlayerAdapter {

    void loadMedia(String resourceName);

    String getMediaName();

    void release();

    boolean isPlaying();

    void play();

    void reset();

    void pause();

    void initializeProgressCallback();

    void seekTo(int position);

    void setPlaybackInfoListener(PlaybackInfoListener listener);

    MediaPlayerHolder getMediaPlayerHolder();

    int getDuration();

    int getProgress();
}