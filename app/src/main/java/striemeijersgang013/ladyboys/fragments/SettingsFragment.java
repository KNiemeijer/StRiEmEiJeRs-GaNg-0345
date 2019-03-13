package striemeijersgang013.ladyboys.fragments;

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
import striemeijersgang013.ladyboys.R;

public class SettingsFragment extends PreferenceFragment {

    Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addPreferencesFromResource(R.xml.frag_settings);

        Preference pref = findPreference("settings_version");
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            pref.setTitle("App versie " +  pInfo.versionName);
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}