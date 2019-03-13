package striemeijersgang013.ladyboys.interfaces;

import striemeijersgang013.ladyboys.MainActivity;
import striemeijersgang013.ladyboys.other.MediaPlayerHolder;
import striemeijersgang013.ladyboys.listeners.PlaybackInfoListener;

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