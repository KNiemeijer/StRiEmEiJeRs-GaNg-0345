package striemeijersgang013.ladyboys.listeners;

import androidx.annotation.IntDef;
import striemeijersgang013.ladyboys.MainActivity;
import striemeijersgang013.ladyboys.other.MediaPlayerHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Allows {@link MediaPlayerHolder} to report media playback duration and progress updates to
 * the {@link MainActivity}.
 */
public abstract class PlaybackInfoListener {

    @IntDef({State.INVALID, State.PLAYING, State.PAUSED, State.RESET, State.COMPLETED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

        int INVALID = -1;
        int PLAYING = 0;
        int PAUSED = 1;
        int RESET = 2;
        int COMPLETED = 3;
    }

    public void onDurationChanged(int duration) {
    }

    public void onPositionChanged(int position) {
    }

    public void onStateChanged(@State int state) {
    }

    public void onPlaybackCompleted() {
    }
}