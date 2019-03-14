package nl.striemeijersgang0345.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import nl.striemeijersgang0345.MainActivity;
import nl.striemeijersgang0345.R;

public class SettingsFragment extends PreferenceFragment {

    Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addPreferencesFromResource(R.xml.frag_settings);

        Preference versionPref = findPreference("settings_version");
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionPref.setTitle("App versie " +  pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Preference feedbackPref = findPreference("settings_feedback");
        feedbackPref.setOnPreferenceClickListener(preference -> {
            String berichtje = "Hey Koen, je mag best een complimentje voor jezelf toevoegen ;)";
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=31618708422&text=" + berichtje;
            sendIntent.setData(Uri.parse(url));
            startActivity(sendIntent);
            return true;
        });

        Preference codePref = findPreference("settings_code");
        codePref.setOnPreferenceClickListener(preference -> {
            String url = "https://github.com/KNiemeijer/StRiEmEiJeRs-GaNg-0345";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return true;
        });

        Preference inlogPref = findPreference("settings_inlog");
        if(MainActivity.USER != null)
            inlogPref.setTitle("Je bent ingelogd als: " + MainActivity.USER);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}