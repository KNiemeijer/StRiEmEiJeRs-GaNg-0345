package striemeijersgang013.ladyboys.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.mediarouter.app.MediaRouteActionProvider;
import androidx.mediarouter.media.MediaControlIntent;
import androidx.mediarouter.media.MediaItemStatus;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import androidx.mediarouter.media.MediaSessionStatus;
import androidx.mediarouter.media.RemotePlaybackClient;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import striemeijersgang013.ladyboys.MainActivity;
import striemeijersgang013.ladyboys.R;
import striemeijersgang013.ladyboys.adapters.MusicAdapter;
import striemeijersgang013.ladyboys.interfaces.MediaPlayerInterface;
import striemeijersgang013.ladyboys.interfaces.PlayerAdapter;
import striemeijersgang013.ladyboys.interfaces.mainActivityInterface;
import striemeijersgang013.ladyboys.listeners.OnSwipeTouchListener;
import striemeijersgang013.ladyboys.listeners.PlaybackInfoListener;
import striemeijersgang013.ladyboys.other.MediaPlayerHolder;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MuziekFragment extends Fragment implements mainActivityInterface,
        MusicAdapter.ItemClickListener, MediaPlayerInterface {

    private Context context;
    private MusicAdapter adapter;
    private SeekBar mSeekbarAudio;
    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    private ImageButton playpause;
    private TextView songTitle;
    private int curPosition;
    private View view;
    private TextView timer;
    private Handler mHandler;
    private int RECYCLER_MESSAGE = 1;
    private Runnable r;

    private MediaRouter mRouter;
    private MediaRouteSelector mSelector;

    // Variables to hold the currently selected route and its playback client
    private MediaRouter.RouteInfo mRoute;
    private RemotePlaybackClient remotePlaybackClient;


    public MuziekFragment() {
        setHasOptionsMenu(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        ((MainActivity) context).setActivityListener(MuziekFragment.this);
        ((MainActivity) context).setMediaPlayerListener(MuziekFragment.this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mRouter = MediaRouter.getInstance(context);
        mSelector = new MediaRouteSelector.Builder()
                .addControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO)
                .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.muziekfragment_layout, container, false);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.timer = view.findViewById(R.id.muziek_time);

        MainActivity parent = (MainActivity) context;
        parent.setupActionBar(view, getString(R.string.muziek));

        // Set title
        TextView title = view.findViewById(R.id.muziek_adviesText);
        title.setText("🎵🎵🎵");
        songTitle = view.findViewById(R.id.muziek_song);

        // Set songTitle swipe listener
        songTitle.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeRight() {
                previousSong();
            }
            public void onSwipeLeft() {
                nextSong();
            }
        });

        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                if(inputMessage.what == RECYCLER_MESSAGE) {
                    RecyclerView recyclerView = (RecyclerView) inputMessage.obj;
                    recyclerView.setBackgroundColor(getResources().getColor(R.color.achtergrondkleur));
                    recyclerView.setNestedScrollingEnabled(false);
                    ViewGroup recyclerParent = view.findViewById(R.id.muziek_achtergrond);
                    recyclerParent.addView(recyclerView);
                    mHandler.removeCallbacks(r);
                }
            }
        };
        r = () -> {
            ArrayList<String> filenames = new ArrayList<String>() {{
                add("Naas Remix - Gras.mp3");
                add("Summer - Wescel Manders.mp3");
                add("Hmnmnmmmnnaaaaaasssss.mp3");
                add("MAND.mp3");
                add("Zanger Zanger Bas - Zanger Bas.mp3");
                add("Das Geht Ab - Die Atzen.mp3");
                add("Rolls Sessie - Boef.mp3");
                add("RATATA - Boef.mp3");
                add("SVP (feat. JoeyAK, Young Ellens & Sevn Alias) - Boef.mp3");
                add("Paperchase - Boef.mp3");
                add("Range Sessie (album intro) - Boef.mp3");
            }};

            // set up the RecyclerView
            LinearLayoutManager layoutManager= new LinearLayoutManager(context);
            // RecyclerView recyclerView = view.findViewById(R.id.muziek_recycler);
            RecyclerView recyclerView = new RecyclerView(context);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new MusicAdapter(context, filenames);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
            mHandler.sendMessage(mHandler.obtainMessage(RECYCLER_MESSAGE, recyclerView));
        };
        mHandler.post(r);

        // Create a new media player
        mSeekbarAudio = view.findViewById(R.id.seekBar);

        playpause = view.findViewById(R.id.muziek_playpause);
        playpause.setOnClickListener(v -> {
            if(mPlayerAdapter.isPlaying()) {
                playpause.setBackground(getResources().getDrawable(R.drawable.ic_play_circle_outline_white_24dp));
                mPlayerAdapter.pause();
                timer.removeCallbacks(mUpdateTime);
            }
            else if(!songTitle.getText().equals("")) {
                playpause.setBackground(getResources().getDrawable(R.drawable.ic_pause_circle_outline_white_24dp));
                mPlayerAdapter.play();
                timer.post(mUpdateTime);
            }
        });

        ImageButton nextButton = view.findViewById(R.id.muziek_next);
        nextButton.setOnClickListener(v -> {
            if(!songTitle.getText().equals(""))
                nextSong();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();
        if(mPlayerAdapter == null || mSeekbarAudio == null) {
            initializeSeekbar();
            initializePlaybackController();
        }
        else {
            initializeSeekbar();
            mPlayerAdapter.setPlaybackInfoListener(new PlaybackListener());

            if(!mPlayerAdapter.isPlaying()) {
                // Load old media back
                mPlayerAdapter.loadMedia(mPlayerAdapter.getMediaName());
                mPlayerAdapter.seekTo(curPosition);
            }
            mSeekbarAudio.setMax(mPlayerAdapter.getDuration());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                mSeekbarAudio.setProgress(curPosition, true);
            else
                mSeekbarAudio.setProgress(curPosition);

            String song = mPlayerAdapter.getMediaName();
            setSongTitle(song);
            if(mPlayerAdapter.isPlaying()) {
                playpause.setBackground(getResources().getDrawable(R.drawable.ic_pause_circle_outline_white_24dp));
                timer.post(mUpdateTime); // start timer
            } else
                updatePlayer(curPosition);
        }
        ProgressBar loader = view.findViewById(R.id.muziek_loader);
        loader.setVisibility(View.GONE);

        mRouter.addCallback(mSelector, mediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onStop() {
        super.onStop();
        if(!songTitle.getText().equals("")) {
            ((MainActivity) context).setPlayerAdapter(mPlayerAdapter);
            curPosition = mPlayerAdapter.getProgress();
        }

        mRouter.removeCallback(mediaRouterCallback);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_muziek, menu);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.menu_cast);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(mSelector);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void back() {
        ((MainActivity) context).goBack();
    }

    public void paintColours(String tekstKleur, String randKleur, String achtergrondKleur, String knopKleur) { }

    public void setPlayerAdapter(PlayerAdapter mPlayerAdapter) {
        if(mPlayerAdapter != null)
            this.mPlayerAdapter = mPlayerAdapter;
    }

    public void onItemClick(View view, int position) {
        curPosition = position;
        playSong(position);
    }

    private void playSong(int position) {
        if(position >= adapter.getItemCount()) {
            curPosition = 0;
        }
        String song = adapter.getItem(curPosition);
        setSongTitle(song);

        mPlayerAdapter.reset();
        mPlayerAdapter.loadMedia(song);
        mPlayerAdapter.play();
        if(mPlayerAdapter.isPlaying()) {
            playpause.setBackground(getResources().getDrawable(R.drawable.ic_pause_circle_outline_white_24dp));
        }

        // Start timer
        timer.post(mUpdateTime);
    }

    private void nextSong() {
        // Reset timer
        timer.removeCallbacks(mUpdateTime);

        playpause.setBackground(getResources().getDrawable(R.drawable.ic_play_circle_outline_white_24dp));
        curPosition++;
        playSong(curPosition);
    }

    private void previousSong() {
        // Reset timer
        timer.removeCallbacks(mUpdateTime);

        playpause.setBackground(getResources().getDrawable(R.drawable.ic_play_circle_outline_white_24dp));
        if(curPosition <= 0)
            curPosition = adapter.getItemCount() - 1;
        else
            curPosition--;
        playSong(curPosition);
    }

    private void setSongTitle(String song) {
        if(song != null)
            songTitle.setText(song.substring(0, song.length() - 4));
    }

    private void initializePlaybackController() {
        MediaPlayerHolder mMediaPlayerHolder = new MediaPlayerHolder(context);
        mMediaPlayerHolder.setPlaybackInfoListener(new PlaybackListener());
        mPlayerAdapter = mMediaPlayerHolder;
    }

    private Runnable mUpdateTime = new Runnable() {
        public void run() {
            int currentDuration;
            if (mPlayerAdapter.isPlaying()) {
                currentDuration = mPlayerAdapter.getProgress();
                updatePlayer(currentDuration);
                timer.postDelayed(this, 1000);
            } else {
                timer.removeCallbacks(this);
            }
        }
    };

    private void updatePlayer(int currentDuration){
        timer.setText(milliSecondsToTimer((long) currentDuration));
    }

    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    private void initializeSeekbar() {
        mSeekbarAudio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    public class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onDurationChanged(int duration) {
            mSeekbarAudio.setMax(duration);
        }

        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mSeekbarAudio.setProgress(position, true);
                }
                else
                    mSeekbarAudio.setProgress(position);
            }
        }

        @Override
        public void onPlaybackCompleted() {
            nextSong();
        }
    }

    // Define the Callback object and its methods, save the object in a class variable
    private final MediaRouter.Callback mediaRouterCallback =
            new MediaRouter.Callback() {

                @Override
                public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo route) {
                    Log.d(TAG, "onRouteSelected: route=" + route);

                    if (route.supportsControlCategory(
                            MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)){
                        // Stop local playback (if necessary)
                        // ...

                        // Save the new route
                        mRoute = route;

                        // Attach a new playback client
                        remotePlaybackClient = new RemotePlaybackClient(context, mRoute);

                        // Start remote playback (if necessary)
                        remotePlaybackClient.play(
                                Uri.parse("file:///android_asset/muziek/Zanger Zanger Bas - Zanger Bas.mp3"),
                                MimeTypeMap.getSingleton()
                                        .getMimeTypeFromExtension("mp3"),
                                null,
                                0,
                                null,
                                new RemotePlaybackClient.ItemActionCallback() {
                                    @Override
                                    public void onResult(Bundle data, String sessionId, MediaSessionStatus sessionStatus, String itemId, MediaItemStatus itemStatus) {
                                        super.onResult(data, sessionId, sessionStatus, itemId, itemStatus);
                                    }
                                }
                        );
                    }
                }

                @Override
                public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo route, int reason) {
                    Log.d(TAG, "onRouteUnselected: route=" + route);

                    if (route.supportsControlCategory(
                            MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)){

                        // Changed route: tear down previous client
                        if (mRoute != null && remotePlaybackClient != null) {
                            remotePlaybackClient.release();
                            remotePlaybackClient = null;
                        }

                        // Save the new route
                        mRoute = route;

                        if (reason != MediaRouter.UNSELECT_REASON_ROUTE_CHANGED) {
                            // Resume local playback  (if necessary)
                            // ...
                        }
                    }
                }
            };
}
