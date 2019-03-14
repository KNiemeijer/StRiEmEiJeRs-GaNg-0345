package nl.striemeijersgang0345.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.striemeijersgang0345.MainActivity;
import nl.striemeijersgang0345.R;
import nl.striemeijersgang0345.interfaces.mainActivityInterface;

public class MeerFragment extends Fragment implements mainActivityInterface {

    private Context context;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.meer_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        MainActivity parent = (MainActivity) context;
        parent.setupActionBar(view, getString(R.string.meer));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        ((MainActivity) context).setActivityListener(MeerFragment.this);

    }

    @Override
    public void onStart() {
        super.onStart();

        MainActivity mainActivity = (MainActivity) context;
        MaterialCardView spelletjesKaart = view.findViewById(R.id.meer_card_spelletjes);
        spelletjesKaart.setOnClickListener(v -> mainActivity.swapFragmentsFromMeer(
                new SpelletjesFragment(), context.getResources().getString(R.string.spelletjes))
        );

        MaterialCardView timerKaart = view.findViewById(R.id.meer_card_timer);
        timerKaart.setOnClickListener(v -> mainActivity.swapFragmentsFromMeer(
                new WolmTimer(), context.getResources().getString(R.string.wolm_timer))
        );

        MaterialCardView waKaart = view.findViewById(R.id.meer_card_wa_generator);
        waKaart.setOnClickListener(v -> mainActivity.swapFragmentsFromMeer(
                new WAGenerator(), context.getResources().getString(R.string.WAgenerator))
        );
    }

    public void back() {
        ((MainActivity) context).goBack();
    }

    public void paintColours(String tekstKleur, String achtergrondKleur, String randKleur, String knopKleur) { }
}
