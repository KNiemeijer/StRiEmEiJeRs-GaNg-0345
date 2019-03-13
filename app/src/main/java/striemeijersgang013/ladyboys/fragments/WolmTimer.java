package striemeijersgang013.ladyboys.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import striemeijersgang013.ladyboys.MainActivity;
import striemeijersgang013.ladyboys.R;
import striemeijersgang013.ladyboys.interfaces.TimerInterface;
import striemeijersgang013.ladyboys.interfaces.mainActivityInterface;
import striemeijersgang013.ladyboys.utils.InputFilterMinMax;

public class WolmTimer extends Fragment implements mainActivityInterface, TimerInterface {

    private Context context;
    private View view;
    private Timer timer;
    private TimerTask progressTask;
    private TimerTask countTask;
    private AtomicReference<Boolean> isPlaying;
    private AtomicReference<Boolean> hasBeenPlayed;
    private long prevTime = 0;
    private long prevTotalTime = 0;
    private int MIN_SECONDS = 0;
    private int MAX_SECONDS = 60;
    private int MIN_MINUTES = 0;
    private int MAX_MINUTES = 720;
    private int STEPSIZE = 5;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        ((MainActivity) context).setActivityListener(WolmTimer.this);
        ((MainActivity) context).setTimerListener(WolmTimer.this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wolmtimer_layout, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initialise variables
        this.view = view;
        isPlaying = new AtomicReference<>(false);
        hasBeenPlayed = new AtomicReference<>(false);

        // set up actionbar
        MainActivity parent = (MainActivity) context;
        parent.setupActionBar(view, getString(R.string.wolm_timer));

        // Get views from layout
        Button secondeMinus = view.findViewById(R.id.timer_seconde_minus);
        Button secondePlus = view.findViewById(R.id.timer_seconde_plus);
        Button minutenMinus = view.findViewById(R.id.timer_minuten_minus);
        Button minutenPlus = view.findViewById(R.id.timer_minuten_plus);
        EditText editSeconden = view.findViewById(R.id.timer_edit_seconden);
        EditText editMinuten = view.findViewById(R.id.timer_edit_minuten);
        ImageButton reset = view.findViewById(R.id.timer_reset);
        FloatingActionButton playpause = view.findViewById(R.id.timer_playpause);

        // Set edittext change listeners
        editSeconden.setFilters(new InputFilter[]{ new InputFilterMinMax(MIN_SECONDS, MAX_SECONDS) });
        editMinuten.setFilters(new InputFilter[] { new InputFilterMinMax(MIN_MINUTES, MAX_MINUTES) });

        // Set click listeners
        secondeMinus.setOnClickListener(v -> createMinusListener(editSeconden, MIN_SECONDS));
        secondePlus.setOnClickListener(v -> createMaxListener(editSeconden, MAX_SECONDS));
        minutenMinus.setOnClickListener(v -> createMinusListener(editMinuten, MIN_MINUTES));
        minutenPlus.setOnClickListener(v -> createMaxListener(editMinuten, MAX_MINUTES));

        // Define a new timer
        timer = new Timer(true);

        // Set the click listener for playing and pausing the timer
        playpause.setOnClickListener(v -> {
            if(isPlaying.get())
                pauseTasks(playpause);
            else {
                if(hasBeenPlayed.get())
                    resumeTasks(playpause, timer);
                else
                    playTasks(playpause, timer, editMinuten, editSeconden);
            }
        });

        reset.setOnClickListener(v -> resetTasks());
    }

    @Override
    public void onStop() {
        if(isPlaying.get()) {
            Log.d("prevTime on stop", Long.toString(prevTime));
            resetTasks();
            ((MainActivity) context).setTimerVars(timer, prevTime, prevTotalTime, new Date().getTime());
        }
        super.onStop();
    }

    @Override
    public void onDetach() {
        resetTasks();
        super.onDetach();
    }

    private TimerTask createProgressTask() {
        ProgressBar wolmTimer = view.findViewById(R.id.wolm_timer);

        return new TimerTask() {
            @Override
            public void run() {
                if(wolmTimer.getProgress() < wolmTimer.getMax())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wolmTimer.setProgress(wolmTimer.getProgress() + 1, true);
                    } else
                        wolmTimer.setProgress(wolmTimer.getProgress() + 1);
                else
                    progressTask.cancel();
            }
        };
    }

    private TimerTask createCountTask(long finalTimerTime) {
        int UPDATE_TIME = 1;
        int FINISH = 2;

        TextView timerText = view.findViewById(R.id.timer_text);

        // Define handler for writing to UI
        Handler mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                if(inputMessage.what == UPDATE_TIME)
                    timerText.setText((String) inputMessage.obj);
                if(inputMessage.what == FINISH) {
                    createMediaPlayer("soundboard/Wolm/netjes.mp3");
                    resetTasks();
                }
            }
        };

        // Create new timer for clock
        return new TimerTask() {
            long previousSec = finalTimerTime % 60;
            long taskTime = finalTimerTime; // Time in milliseconds

            @Override
            public void run() {
                if(taskTime > 0) {
                    taskTime = taskTime - 1;
                    int clockMinuten = (int) Math.floor((float) taskTime / 1000 / 60);
                    int clockSeconden = (int) taskTime / 1000 % 60;
                    if(clockSeconden != previousSec) {
                        @SuppressLint("DefaultLocale") String clockTime = String.format("%1$02d:%2$02d",
                                clockMinuten,
                                clockSeconden);
                        Message completeMessage = mHandler.obtainMessage(UPDATE_TIME, clockTime);
                        completeMessage.sendToTarget();
                        previousSec = clockSeconden;
                    }
                }
                else {
                    Message completeMessage = mHandler.obtainMessage(FINISH);
                    completeMessage.sendToTarget();
                    countTask.cancel();
                }
            }

            // Custom cancel task for storing actual time left before cancelled
            @Override
            public boolean cancel() {
                if(taskTime > 0) {
                    prevTime = taskTime;
                    //prevTotalTime = finalTimerTime;
                }
                return super.cancel();
            }
        };
    }

    private void playTasks(FloatingActionButton playpause, Timer timer, EditText editMinuten, EditText editSeconden) {
        // Retrieve views
        ProgressBar wolmTimer = view.findViewById(R.id.wolm_timer);
        ImageView doNot = view.findViewById(R.id.timer_donot);
        TextView timerText = view.findViewById(R.id.timer_text);

        // Sanitise input when edittext fields are empty
        String minuten = editMinuten.getText().toString();
        if(minuten.equals("")) {
            minuten = "0";
            editMinuten.setText(minuten);
        }
        String seconden = editSeconden.getText().toString();
        if(seconden.equals("")) {
            seconden = "0";
            editSeconden.setText(seconden);
        }
        long timerTime = Long.parseLong(minuten) * 60;
        timerTime += Long.parseLong(seconden);


        if(timerTime <= 0)
            Toast.makeText(context ,"Stel een tijd in om te beginnen", Toast.LENGTH_SHORT).show();
        else {
            playpause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_white_24dp));
            isPlaying.set(true);
            doNot.setVisibility(View.VISIBLE);

            // Set progress to 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                wolmTimer.setProgress(0, true);
            else
                wolmTimer.setProgress(0);

            // Play harses willem
            createMediaPlayer("soundboard/Mike/harses willem.mp3");

            // Set text in the timer
            @SuppressLint("DefaultLocale") String clockTime = String.format("%1$02d:%2$02d",
                    Integer.parseInt(minuten),
                    Integer.parseInt(seconden));
            timerText.setText(clockTime);

            // Create tasks
            progressTask = createProgressTask();
            countTask = createCountTask((timerTime + 1) * 1000);

            // Register total time
            prevTotalTime = timerTime * 1000;

            // Register countdown has been started
            hasBeenPlayed.set(true);

            timer.scheduleAtFixedRate(countTask, 0, 1);
            timer.scheduleAtFixedRate(progressTask, 0, timerTime * 10);
        }
    }

    private void resumeTasks(FloatingActionButton playpause, Timer timer) {
        playpause.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_white_24dp));
        isPlaying.set(true);
        ImageView donot = view.findViewById(R.id.timer_donot);
        donot.setVisibility(View.VISIBLE);

        progressTask = createProgressTask();
        countTask = createCountTask(prevTime);
        timer.scheduleAtFixedRate(countTask, 0, 1);
        Log.d("Previous resume", Long.toString(prevTime / 100));
        timer.scheduleAtFixedRate(progressTask, 0, prevTime / 100);
    }

    private void pauseTasks(FloatingActionButton playpause) {
        if(progressTask != null)
            progressTask.cancel();
        if(countTask != null)
            countTask.cancel();
        isPlaying.set(false);
        playpause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_white_24dp));
    }

    private void resetTasks() {
        ProgressBar wolmTimer = view.findViewById(R.id.wolm_timer);
        ImageView doNot = view.findViewById(R.id.timer_donot);
        TextView timerText = view.findViewById(R.id.timer_text);
        FloatingActionButton playpause = view.findViewById(R.id.timer_playpause);

        pauseTasks(playpause);

        doNot.setVisibility(View.GONE);
        timerText.setText(getString(R.string.timer_initial));
        hasBeenPlayed.set(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            wolmTimer.setProgress(0, true);
        else
            wolmTimer.setProgress(0);
    }

    private void createMediaPlayer(String file) {
        MediaPlayer mp = new MediaPlayer();
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd(file);
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.start();
        mp.setOnCompletionListener(mp1 -> mp.release());
    }

    @SuppressLint("SetTextI18n")
    private void createMinusListener(EditText minusEdit, int min) {
        String editText = minusEdit.getText().toString();
        if(!editText.equals("")) {
            int oldNum = Integer.parseInt(editText);
            int newNum = oldNum - STEPSIZE;
            if (newNum >= min) {
                if (oldNum < (min + STEPSIZE) && oldNum > min)
                    newNum = min;
                minusEdit.setText(Integer.toString(newNum));
            } else
                minusEdit.setText(Integer.toString(min));
        } else
            minusEdit.setText(Integer.toString(min));
    }

    @SuppressLint("SetTextI18n")
    private void createMaxListener(EditText maxEdit, int max) {
        String editText = maxEdit.getText().toString();
        if(!editText.equals("")) {
            int oldNum = Integer.parseInt(editText);
            int newNum = oldNum + STEPSIZE;
            if(newNum <= max) {
                if (oldNum < max && oldNum > (max - STEPSIZE))
                    newNum = max;
                maxEdit.setText(Integer.toString(newNum));
            } else
                maxEdit.setText(Integer.toString(max));
        } else
            maxEdit.setText(Integer.toString(STEPSIZE));
    }

    public void back() {
        ((MainActivity)context).swapFragmentsFromMeer(new MeerFragment(), "Meer");
    }

    public void paintColours(String tekstKleur, String achtergrondKleur, String randKleur, String knopKleur) { }

    /**
     * {@inheritDoc}
     */
    public void setTimer(Timer timer, long prevTime, long prevTotalTime, long timeStamp) {
        // Check if timer should be active
        long timeLeft = new Date().getTime() - timeStamp; // Time difference since timer stop and now
        if(timeLeft < prevTime) {
            // find views
            FloatingActionButton playpause = view.findViewById(R.id.timer_playpause);
            EditText editMinuten = view.findViewById(R.id.timer_edit_minuten);
            EditText editSeconden = view.findViewById(R.id.timer_edit_seconden);
            ProgressBar wolmTimer = view.findViewById(R.id.wolm_timer);

            prevTime = prevTime - timeLeft; // Update time left according to timestamp
            this.prevTime = prevTime;
            this.prevTotalTime = prevTotalTime;
            this.timer = new Timer(true); // Timer is obsolete?
            int progress = (int) Math.floor((prevTotalTime - prevTime) / (double) prevTotalTime * 100);
            Log.d("Progress at resume", Integer.toString(progress));
            wolmTimer.setMax(100);
            wolmTimer.setProgress(progress);
            hasBeenPlayed.set(true);
            Log.d("prevTime", Long.toString(prevTime));
            Log.d("prevTotalTime", Long.toString(prevTotalTime));

            int clockMinuten = (int) Math.floor((float) prevTotalTime / 1000 / 60);
            int clockSeconden = (int) prevTotalTime / 1000 % 60;

            editMinuten.setText(String.format(Locale.ENGLISH, "%1$02d", clockMinuten));
            editSeconden.setText(String.format(Locale.ENGLISH, "%1$02d", clockSeconden));

            resumeTasks(playpause, this.timer);
        }
    }
}
