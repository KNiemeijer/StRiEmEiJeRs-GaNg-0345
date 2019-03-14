package nl.striemeijersgang0345.interfaces;

import java.util.Timer;

public interface TimerInterface {

    /**
     * Method for setting a timer that when destroyed when the user left the fragment
     * @param timer The timer to keep
     * @param prevTime The time when it was last updated
     * @param prevTotalTime The time that was originally set in total
     */
    void setTimer(Timer timer, long prevTime, long prevTotalTime, long timeStamp);
}
