package nl.striemeijersgang0345;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import nl.striemeijersgang0345.fragments.SettingsFragment;


public class SettingsActivity extends AppCompatActivity {

    private Intent returnIntent;
    private boolean NIGHT_CHANGED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setupActionBar();

        returnIntent = new Intent();
        returnIntent.putExtra("result", "result");

        // Display the fragment as the main content.
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
                if (key.equals(getString(R.string.settings_nachtmodus))) {
                    setNachtModus();
                }
            }
        );
    }

    /**
     * Set up the {@link ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            int result = (NIGHT_CHANGED) ? Activity.RESULT_OK : Activity.RESULT_CANCELED;
            setResult(result, returnIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            int result = (NIGHT_CHANGED) ? Activity.RESULT_OK : Activity.RESULT_CANCELED;
            setResult(result, returnIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNachtModus() {
        String nachtmodus = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("nachtmodus", getString(R.string.nachtmodus_auto));
        assert nachtmodus != null;

        // Check for location permission
        askForPermission();

        if(nachtmodus.equals(getString(R.string.nachtmodus_auto)))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        else if(nachtmodus.equals(getString(R.string.nachtmodus_altijd)))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else if(nachtmodus.equals(getString(R.string.nachtmodus_nooit)))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getDelegate().applyDayNight();
    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MainActivity.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MainActivity.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }
}
