package nl.striemeijersgang0345;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import nl.striemeijersgang0345.fragments.ComplimentenFragment;
import nl.striemeijersgang0345.fragments.HomeFragment;
import nl.striemeijersgang0345.fragments.MeerFragment;
import nl.striemeijersgang0345.fragments.MuziekFragment;
import nl.striemeijersgang0345.fragments.SoundBoardFragment;
import nl.striemeijersgang0345.fragments.WolmTimer;
import nl.striemeijersgang0345.interfaces.MediaPlayerInterface;
import nl.striemeijersgang0345.interfaces.PlayerAdapter;
import nl.striemeijersgang0345.interfaces.TimerInterface;
import nl.striemeijersgang0345.interfaces.mainActivityInterface;
import nl.striemeijersgang0345.utils.CacheManager;
import nl.striemeijersgang0345.utils.Holidays;

public class MainActivity extends AppCompatActivity {

    private Fragment curFragment;
    private mainActivityInterface mInterface;
    FragmentManager.OnBackStackChangedListener defaultBackStackListener;

    private MediaPlayerInterface mediaPlayerInterface;
    private PlayerAdapter mMediaPlayer;

    private TimerInterface mTimerInterface;
    private Timer timer;
    private long prevTime;
    private long prevTotalTime;
    private long timerTimeStamp;

    protected static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private final int REQUEST_SETTINGS = 1;
    private final int REQUEST_ACCOUNT = 2;

    // Strings for user names
    public static String USER;
    public final static String KOEN = "Korn";
    public final static String KEVIN = "Kelf";
    public final static String WILLEM = "Wolm";
    public final static String GIJS = "Geiz";
    public final static String THOMAS = "Thomans";
    public final static String WESCEL = "Wexel";
    public final static String THIJS = "Thuns";
    public final static String YORAM = "Yorm";
    public final static String MICHIEL = "Kapitein Pluisbaard";


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNachtModus();
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        // Clear cache
        CacheManager.clearCache(this);

        // Set up app menu
        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(
                menuItem -> {
                    swapFragments(menuItem);
                    return true;
                });

        // Swap first fragment
        curFragment = new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_view, curFragment, Integer.toString(R.id.nav_home))
                .commit();
        navigationView.setSelectedItemId(R.id.nav_home);

        // Make sure we can cycle back
        defaultBackStackListener = () -> {
            int nEntries = getSupportFragmentManager().getBackStackEntryCount();
            if (nEntries > 0) {

                // Set new item as checked
                int index = this.getSupportFragmentManager().getBackStackEntryCount() - 1;
                String tag = getSupportFragmentManager().getBackStackEntryAt(index).getName();
                assert tag != null;
                navigationView.setSelectedItemId(Integer.parseInt(tag));

                // Update title
                int itemId = getResources().getIdentifier(tag, "id", getPackageName());
                String title = navigationView
                        .getMenu()
                        .findItem(itemId)
                        .getTitle()
                        .toString();
                if (title.equals("Home"))
                    title = getString(R.string.app_name);
                changeActionBarTitle(title);
            } else { // Special case if backstack is empty
                navigationView.setSelectedItemId(R.id.nav_home);
                changeActionBarTitle(getString(R.string.app_name));
            }
        };
        getSupportFragmentManager().addOnBackStackChangedListener(defaultBackStackListener);

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(
                    @NonNull FragmentManager fm,
                    @NonNull Fragment f,
                    @NonNull View v,
                    Bundle savedInstanceState) {
                if(f instanceof MuziekFragment)
                    if (mMediaPlayer != null)
                        mediaPlayerInterface.setPlayerAdapter(mMediaPlayer);
            }
            @Override
            public void onFragmentStarted(
                    @NonNull FragmentManager fm,
                    @NonNull Fragment f) {
                super.onFragmentStarted(fm, f);
                if(f instanceof WolmTimer)
                    if (timer != null)
                        mTimerInterface.setTimer(timer, prevTime, prevTotalTime, timerTimeStamp);
            }
        }, true);

        // Check for holiday
        new Holidays().checkHolidays(findViewById(R.id.feestdagView));

        // Find user
        USER = getPreferences(Context.MODE_PRIVATE).getString("user", null);
        // if(USER == null) {
            Intent intent = AccountManager.newChooseAccountIntent(
                    null,
                    null,
                    new String[] {"com.google", "com.google.android.legacyimap"},
                    false,
                    "Selecteer een email account om te bepalen hoe je heet.",
                    null,
                    null,
                    null
            );
            startActivityForResult(intent, REQUEST_ACCOUNT);
      //   } else showWelcomeBack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        if(mMediaPlayer != null)
            mMediaPlayer.release();

        if(timer != null)
            timer.cancel();

        super.onDestroy();
    }

    /**
     * Register fragments so that the activity is aware of its existence.
     * @param activityListener Fragment's implementation of mainActivityInterface.
     */
    public void setActivityListener(mainActivityInterface activityListener) {
        this.mInterface = activityListener;
    }

    public void setTimerListener(TimerInterface timerListener) {
        this.mTimerInterface = timerListener;
    }

    public void setMediaPlayerListener(MediaPlayerInterface mediaPlayerListener) {
        this.mediaPlayerInterface = mediaPlayerListener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && !(event.isCanceled())) {
                mInterface.back();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Public method for handling callbacks from fragments.
     * This method only gets triggered whenever the webview cannot go back.
     * Also handles displaying the exit dialog.
     */
    public void goBack() {
        getSupportFragmentManager().addOnBackStackChangedListener(defaultBackStackListener);
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if(curFragment instanceof HomeFragment)
                showExitDialog();
            else {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // Pop all
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.container_view, new HomeFragment(), Integer.toString(R.id.nav_home))
                        .commit();
                changeActionBarTitle(getString(R.string.app_name));
            }
        }
        else
            showExitDialog();
    }

    private void showExitDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        alertDialogBuilder.setTitle("Afsluiten");
        alertDialogBuilder
                .setMessage("Weet je zeker dat je de app wil afsluiten?")
                .setCancelable(true)
                .setPositiveButton("Ja",
                        (dialog, id) -> {
                            moveTaskToBack(true);
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(-1);
                        })

                .setNegativeButton("Nee", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_cast:

                return true;
            case R.id.menu_settings:
                startActivityForResult(
                        new Intent(this, SettingsActivity.class),
                        REQUEST_SETTINGS);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles swapping of fragments when an item is clicked in the navigation menu.
     * @param menuItem ID of a menu item.
     */
    private void swapFragments(MenuItem menuItem)  {

        Fragment fragment = null;

        // Switch on menu item and handle accordingly
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_complimenten:
                fragment = new ComplimentenFragment();
                break;
            case R.id.nav_muziek:
                fragment = new MuziekFragment();
                break;
            case R.id.nav_soundboard:
                fragment = new SoundBoardFragment();
                break;
            case R.id.nav_more:
                fragment = new MeerFragment();
                break;
        }

        if(fragment != null)
            if (!Integer.toString(menuItem.getItemId()).equals(curFragment.getTag())) {
                menuItem.setChecked(true);
                curFragment = fragment;
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.container_view, curFragment, Integer.toString(menuItem.getItemId()))
                        .addToBackStack(Integer.toString(menuItem.getItemId()))
                        .commit();
        }
    }

    public void swapFragmentsFromMeer(Fragment fragment, String tag) {
        FragmentManager.OnBackStackChangedListener customBackStackListener = () -> { };
        getSupportFragmentManager().removeOnBackStackChangedListener(defaultBackStackListener);
        getSupportFragmentManager().addOnBackStackChangedListener(customBackStackListener);

        String navTag = Integer.toString(R.id.nav_more);

        curFragment = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container_view, curFragment, tag)
                .addToBackStack(navTag)
                .commit();

        // changeActionBarTitle(tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SETTINGS) {
            /* if(resultCode == Activity.RESULT_OK){
                Log.d("Intent result", "OK");
                setNachtModus();
            }*/
            /*getSupportFragmentManager()
                    .beginTransaction()
                    .detach(curFragment)
                    .commit();

            if(getSupportFragmentManager().getBackStackEntryCount() > 1)
                getSupportFragmentManager().popBackStack();*/
        }

        if(requestCode == REQUEST_ACCOUNT) {
            if(resultCode == Activity.RESULT_OK) {
                Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
                for (Account account : accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        String email = account.name.toLowerCase();
                        Log.d("Mail", email);
                        if(email.contains("koen") || email.contains("niemeijer"))
                            USER = KOEN;
                        if(email.contains("kevin") || email.contains("algera"))
                            USER = KEVIN;
                        if(email.contains("willem") || email.contains("smits"))
                            USER = WILLEM;
                        if(email.contains("gijs") || email.contains("cunnen"))
                            USER = GIJS;
                        if(email.contains("thomas") || email.contains("bardoel"))
                            USER = THOMAS;
                        if(email.contains("wescel") || email.contains("manders"))
                            USER = WESCEL;
                        if(email.contains("thijs") || email.contains("putten"))
                            USER = THIJS;
                        if(email.contains("yoram") || email.contains("carboex"))
                            USER = YORAM;
                        if(email.contains("michiel") || email.contains("arts") || email.contains("zeek"))
                            USER = MICHIEL;
                        Log.d("Current user is", USER);
                        if(USER == null) {
                            Toast.makeText(this, "Oeps, ik weet niet van wie dit emailadres is. Probeer een andere",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                                    null, false, null, null, null, null);
                            startActivityForResult(intent, REQUEST_ACCOUNT);
                        } else {
                            // Save to preferences
                            SharedPreferences.Editor edit = getPreferences(Context.MODE_PRIVATE).edit();
                            edit.putString("user", USER);
                            edit.apply();
                            showWelcomeBack();
                        }
                    }
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED)
                finish();
        }
    }

    /**
     * Update the title of the action bar.
     * @param text New title of the actionbar.
     */
    private void changeActionBarTitle(String text) {
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        TextView mToolBarTitle = findViewById(R.id.toolbar_title);
        mToolBarTitle.setText(text);
    }

    public void setupActionBar(View view, String title) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null) {
            actionbar.setDisplayShowTitleEnabled(false);
            actionbar.setIcon(R.mipmap.ic_wolm);
            changeActionBarTitle(title);
        }
    }

    private void setNachtModus() {
        String nachtmodus = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("nachtmodus", getString(R.string.nachtmodus_auto));
        assert nachtmodus != null;

        if(nachtmodus.equals(getString(R.string.nachtmodus_auto)))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        else if(nachtmodus.equals(getString(R.string.nachtmodus_altijd)))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else if(nachtmodus.equals(getString(R.string.nachtmodus_nooit)))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getDelegate().applyDayNight();
    }

    public void setPlayerAdapter(PlayerAdapter mMediaPlayer) {
        this.mMediaPlayer = mMediaPlayer;
    }

    public void setTimerVars(Timer timer, long prevTime, long prevTotalTime, long timeStamp) {
        this.timer = timer;
        this.prevTime = prevTime;
        this.prevTotalTime = prevTotalTime;
        this.timerTimeStamp = timeStamp;
    }

    private void showWelcomeBack() {
        // Show snackbar saying welcome back
        Snackbar.make(
                findViewById(R.id.drawer_layout),
                "Welkom terug " + USER + "!",
                Snackbar.LENGTH_LONG
        ).show();
    }
}
